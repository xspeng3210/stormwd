package com.citiccard.dasp.dc.rltm.coll.model;
/**
 * DDL信息
 *
 */
public class TableMetaData {
	private String sql;
	private String schema;
	private String tableName;
	private Long executeTime;
	private String executeType;
	
	/**
	 * 清空数据
	 */
	public void clear() {
		this.sql = "";
		this.schema = "";
		this.executeTime = 0L;
		this.executeType = "";
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Long getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Long executeTime) {
		this.executeTime = executeTime;
	}

	public String getExecuteType() {
		return executeType;
	}

	public void setExecuteType(String executeType) {
		this.executeType = executeType;
	}
	
	@Override
	public String toString() {
		return "TableMetaData [sql="+sql+",schema="+schema+",tableName="+tableName+",executeTime="
				+ executeTime+",executeType="+executeType+"]";
	}

}
