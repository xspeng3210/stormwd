package com.citiccard.dasp.dc.rltm.coll.model;

import java.util.HashMap;
import java.util.Map;

public class BinlogReport {
	private Long messageId;
	private String agentId;
	private Long batchId; //��Ϣ�ķ������κ�
	private Long batchSize;//�����ε���������
	private Long tranMessID;//���ݿ������
	private byte splitId;//���ݵĲ��ID
	private Integer DataLen;//�������ݵĳ���
	private String DataLenType;//�������ݵĳ�������
	
	/**
	 * Ԥ����TableNum����Ϊ1���ֽڱ�ʾ���֧��һ������Ϊ�漰16����
	 */
	private Byte tableNum;
	private String tablename;
	private String schema;
	private String eventType;
	private String sendToKafkaTime;
	private String getBinlogTime;
	private String spoutTime;
	//sql���ִ��ʱ��
	private String sqlExecuteTime;
	private Map<String,String>body = new HashMap<String,String>();
	
	/**
	 * �������
	 */
	public void clear() {
		this.messageId = -1L;
		this.agentId = "";
		this.batchId = -1L;
		this.batchSize = -1L;
		this.tranMessID = -1L;
		this.tableNum = -1;
		this.splitId = -1;
		this.DataLen = 0;
		this.DataLenType = "";
		
		this.tablename = "";
		this.schema = "";
		this.eventType = "";
		this.sendToKafkaTime = "";
		this.getBinlogTime = "";
		this.spoutTime = "";
		this.sqlExecuteTime = "";
		this.body.clear();
	}
	
	public String getSqlExecuteTime() {
		return sqlExecuteTime;
	}

	public void setSqlExecuteTime(String sqlExecuteTime) {
		this.sqlExecuteTime = sqlExecuteTime;
	}
	
	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	public String getSendToKafkaTime() {
		return sendToKafkaTime;
	}

	public void setSendToKafkaTime(String sendToKafkaTime) {
		this.sendToKafkaTime = sendToKafkaTime;
	}
	
	public String getGetBinlogTime() {
		return getBinlogTime;
	}

	public void setGetBinlogTime(String getBinlogTime) {
		this.getBinlogTime = getBinlogTime;
	}
	
	public Map<String, String> getBody() {
		return body;
	}

	public void setBody(Map<String, String> body) {
		this.body = body;
	}
	
	public String getSpoutTime() {
		return spoutTime;
	}

	public void setSpoutTime(String spoutTime) {
		this.spoutTime = spoutTime;
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
	
	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
	
	public Long getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(Long batchSize) {
		this.batchSize = batchSize;
	}
	
	public Long getTranMessID() {
		return tranMessID;
	}

	public void setTranMessID(Long tranMessID) {
		this.tranMessID = tranMessID;
	}

	
	public String getSchema() {
		return schema;
	}

	
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	
}
