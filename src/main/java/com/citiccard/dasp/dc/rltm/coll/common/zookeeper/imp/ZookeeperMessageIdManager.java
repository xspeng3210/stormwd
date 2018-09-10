package com.citiccard.dasp.dc.rltm.coll.common.zookeeper.imp;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.otter.canal.common.utils.JsonUtils;
import com.alibaba.otter.canal.common.zookeeper.ZkClientx;

import com.citiccard.dasp.dc.rltm.coll.common.zookeeper.MessageIdManager;
import com.citiccard.dasp.dc.rltm.coll.common.zookeeper.ZookeeperPathUtils;
import com.citiccard.dasp.dc.rltm.coll.model.MessageIdRange;

public class ZookeeperMessageIdManager implements MessageIdManager {

	private ZkClientx zkClientx;
	
	
	public void updateMesssageIdRange(MessageIdRange range){
		String path=ZookeeperPathUtils.getAgentTopicMessageIdPath(range.getAgentId(),range.getTopic(),
				range.getPartitionNum());
		byte[]  data=JsonUtils.marshalToByte(range,SerializerFeature.WriteClassName);
		zkClientx.writeData(path, data);
		
	}
	
	public MessageIdRange getMessageIdRange(String agentId,String topic,String partitionNum){
		String path=ZookeeperPathUtils.getAgentTopicMessageIdPath(agentId,topic,partitionNum);
		MessageIdRange range=null;
		if(!zkClientx.exists(path)){
			range =new MessageIdRange();
			range.setAgentId(agentId);
			range.setTopic(topic);
			range.setPartitionNum(partitionNum);
			
			byte[]  data=JsonUtils.marshalToByte(range,SerializerFeature.WriteClassName);
			
			zkClientx.createPersistent(path,data,true);
		}else{
			byte[] result=zkClientx.readData(path,true);
			if(result == null){
				return null;
			}
			range=JsonUtils.unmarshalFromByte(result, MessageIdRange.class);
		}
		return range;
	}
	
	public void setZkClientx(ZkClientx zkClientx){
		this.zkClientx=zkClientx;
	}

	
	
}
