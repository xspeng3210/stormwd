package com.citiccard.dasp.dc.rltm.coll.common;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


import static com.citiccard.dasp.dc.rltm.coll.common.Const.KEY_DELIMETER;
import com.citiccard.dasp.dc.rltm.coll.common.zookeeper.imp.ZookeeperMessageIdManager;
import com.citiccard.dasp.dc.rltm.coll.model.MessageIdRange;
import com.google.common.base.Function;
import com.google.common.collect.MigrateMap;

/**
 * 预置的分区信息，数据从Canal中获取后，第一步，先计算分区信息，并将其填写入kafka和rocksDB
 */
public class PrePartitioner {
	private String agentId;
	private String topic;

	private Long messageIdStep = 10L;//默认messageId在zk上的步长为10
	private int partitions = 3;//默认3个分区

	private final AtomicInteger counter = new AtomicInteger(new SecureRandom().nextInt());

	private ZookeeperMessageIdManager zookeeperMessageIdManager = null;

	private Map<String, MessageIdRange> messageIdRanges = MigrateMap
			.makeComputingMap(new Function<String, MessageIdRange>() {
				public MessageIdRange apply(String agentIdTopicPartitionNum) {
					String[] keys = agentIdTopicPartitionNum.split(KEY_DELIMETER);
					return zookeeperMessageIdManager.getMessageIdRange(keys[0], keys[1], keys[2]);
				}
			});

	/**
	 * 
	 *返回该条数对应分区的MessageIdRange 
	 */
	public MessageIdRange getMessageIdRange() {
	//获取分区号
		int partitionNum = this.getPartitionNum();
	String agentIdTopicPartitionNum=this.agentId+KEY_DELIMETER+this.topic+KEY_DELIMETER+partitionNum;
	MessageIdRange messageIdRange=messageIdRanges.get(agentIdTopicPartitionNum);
	return messageIdRange;
	}

	private int getPartitionNum() {
		int nextValue = counter.getAndIncrement();
		int currentPartition = PrePartitioner.toPositive(nextValue) % partitions;
		return currentPartition;
	}
	public static int toPositive(int number) {
		return number & 0x7fffffff;
	}
	
	public String generateKey(MessageIdRange messageIdRange) {
		String key=messageIdRange.getTopic()+KEY_DELIMETER+messageIdRange.getPartitionNum()+KEY_DELIMETER
		+ messageIdRange.getMessageId();	
		messageIdRange.setMessageId(messageIdRange.getMessageId()+1);
		messageIdRange.setStep(messageIdRange.getStep()-1);
	return key;
	}
	/**
	 * 刷新zk上的messageIdRange
	 */
	public void updateMessageIdRange(MessageIdRange messageIdRange) {
		if(messageIdRange.getStep()==0|| messageIdRange.getStep()<0) {
			zookeeperMessageIdManager.updateMesssageIdRange(messageIdRange);
			messageIdRange.setStep(messageIdStep);
		}
	}
	
	public String getAgentId() {
		return agentId;
	}
	
	public void setAgentId(String agentId) {
		this.agentId=agentId;
	}
	
	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		this.topic=topic;
	}

	public Long getMessageIdStep() {
		return messageIdStep;
	}

	public void setMessageIdStep(Long messageIdStep) {
		this.messageIdStep = messageIdStep;
	}

	public int getPartitions() {
		return partitions;
	}

	public void setPartitions(int partitions) {
		this.partitions = partitions;
	}

	public ZookeeperMessageIdManager getZookeeperMessageIdManager() {
		return zookeeperMessageIdManager;
	}

	public void setZookeeperMessageIdManager(ZookeeperMessageIdManager zookeeperMessageIdManager) {
		this.zookeeperMessageIdManager = zookeeperMessageIdManager;
	}


	public String toString(){
		return "PrePartitioner[agentId=" +agentId +",topic=" + topic +",messageIdStep="+ messageIdStep
				+",partitions" + partitions +"]";
	}
	
}
