package com.citiccard.dasp.dc.rltm.coll.model;

import java.io.Serializable;
/**
 * ���ݿ���������Ϣ
 *
 */
public class DbInfoMessageVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	String status;
	
	String data;
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "DbInfoMessageVo [status="+status+",data="+data+"]";
	}

}
