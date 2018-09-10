package com.citiccard.dasp.dc.rltm.coll.model;

import java.io.Serializable;
/**
 * topic��ÿ����������һ��MessageIdRange��ά��messageId
 *
 */
public class MessageIdRange implements Serializable {
	private static final long serialVersionUID = -64871836202676049L;
	private Long step = 10L; //���10 ˢ��һ��zk
	private Long messageId = 1L;
	private String agentId;
	private String topic;
	private String partitionNum;
	
	public Long getStep() {
		return step;
	}
	public void setStep(Long step) {
		this.step = step;
	}
	public Long getMessageId() {
		return messageId;
	}
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getPartitionNum() {
		return partitionNum;
	}
	public void setPartitionNum(String partitionNum) {
		this.partitionNum = partitionNum;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String toString() {
		return "MessageIdRange [step="+step+",messageId="+messageId+",agentId="+agentId+",topic="+topic
				+",partitionNum="+partitionNum+"]";
	}

	
	
	
	
	
	
	
	
	
	
}
