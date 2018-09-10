package com.citiccard.dasp.dc.rltm.coll.model;
/**
 * 
 *封装messageId相关的agent和topic信息
 */
public class MessageIdInfo {
	String agentId;
	String topic;
	String partition;
	
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
	public String getPartition() {
		return partition;
	}
	public void setPartition(String partition) {
		this.partition = partition;
	}
	
	@Override
	public String toString() {
		return "MessageInfo [agentId="+agentId+",topic="+topic+",partition="+partition+"]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result+ ((agentId==null)?0:agentId.hashCode());
		result = prime * result+ ((partition==null)?0:partition.hashCode());
		result = prime * result+ ((topic==null)?0:topic.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this==obj) {
			return true;
		}
		if(obj==null) {
			return false;
		}
		if(getClass()!=obj.getClass()) {
			return false;
		}
		MessageIdInfo other = (MessageIdInfo)obj;
		if(agentId==null) {
			if(other.agentId!=null) {
				return false;
			   }
			}else if(!agentId.equals(other.agentId)) {
				return false;
			}
		if(partition==null) {
			if(other.partition!=null) {
				return false;
			   }
			}else if(!partition.equals(other.partition)) {
				return false;
			}
		if(topic==null) {
			if(other.topic!=null) {
				return false;
			   }
			}else if(!topic.equals(other.topic)) {
				return false;
			}
		return true;
	}

}
