package com.citiccard.dasp.dc.rltm.coll.model;
/**
 * ���ݺ�messageId�Ķ�Ӧ��ϵBean
 *
 */

import static com.citiccard.dasp.dc.rltm.coll.common.Const.MESSAGE_DELIMITER;
public class MessageAndPartition {
	
	private String batchId;
	
	private int indexId;
	
	private int partitionNum;
	
	private String messageId;
	
	@Override
	public String toString() {
		return "MessageAndPartition [batchId="+batchId+",indexId="+indexId+"partitionNum="+partitionNum
				+",messageId="+messageId+"]";
	}
	
	public MessageAndPartition (String batchId,int indexId,int partitionNum) {
		super();
		this.batchId = batchId;
		this.indexId = indexId;
		this.partitionNum = partitionNum;
		this.messageId = this.batchId+MESSAGE_DELIMITER+this.indexId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result+ ((batchId==null)?0:batchId.hashCode());
		result = prime * result+indexId;
		result = prime * result+ ((messageId==null)?0:messageId.hashCode());
		result = prime * result+partitionNum;
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
		MessageAndPartition other = (MessageAndPartition)obj;
		if(batchId==null) {
			if(other.batchId!=null) {
				return false;
			   }
			}else if(!batchId.equals(other.batchId)) {
				return false;
			}
		if(indexId!=other.indexId) {
			return false;
		}
		if(messageId==null) {
			if(other.messageId!=null) {
				return false;
			   }
			}else if(!messageId.equals(other.messageId)) {
				return false;
			}
		if(partitionNum!=other.partitionNum) {
			return false;
		}
		return true;
	}
	
	public String getBatchId() {
		return batchId;
	}
	
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	
	public int getIndexId() {
		return indexId;
	}
	
	public void setIndexId(int indexId) {
		this.indexId = indexId;
	}

	public int getPartitionNum() {
		return partitionNum;
	}

	public void setPartitionNum(int partitionNum) {
		this.partitionNum = partitionNum;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
