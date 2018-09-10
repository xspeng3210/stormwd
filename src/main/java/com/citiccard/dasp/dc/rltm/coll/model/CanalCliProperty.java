package com.citiccard.dasp.dc.rltm.coll.model;

import java.io.Serializable;
/**
 * CanalCliProperty ������Ϣ
 *
 */

public class CanalCliProperty implements Serializable {
	private static final long serialVersionUID = 1L;
	
	Integer dclId;
	String dclCanalIp;
	String dclUserName;
	String dclPassword;
	String dclZkServer;
	String dclDestination;
	String dclSubscribe;
	String dclCreateTime;
	String dclCreateUser;
	String dclModifyTime;
	String dclModifyUser;
	
	public Integer getDclId() {
		return dclId;
	}
	
	public void setDclId(Integer dclId) {
		this.dclId = dclId;
	}
	
	public String getDclCanalIp() {
		return dclCanalIp;
	}

	public void setDclCanalIp(String dclCanalIp) {
		this.dclCanalIp = dclCanalIp;
	}

	public String getDclUserName() {
		return dclUserName;
	}

	public void setDclUserName(String dclUserName) {
		this.dclUserName = dclUserName;
	}

	public String getDclPassword() {
		return dclPassword;
	}

	public void setDclPassword(String dclPassword) {
		this.dclPassword = dclPassword;
	}

	public String getDclZkServer() {
		return dclZkServer;
	}

	public void setDclZkServer(String dclZkServer) {
		this.dclZkServer = dclZkServer;
	}

	public String getDclDestination() {
		return dclDestination;
	}

	public void setDclDestination(String dclDestination) {
		this.dclDestination = dclDestination;
	}

	public String getDclSubscribe() {
		return dclSubscribe;
	}

	public void setDclSubscribe(String dclSubscribe) {
		this.dclSubscribe = dclSubscribe;
	}

	public String getDclCreateTime() {
		return dclCreateTime;
	}

	public void setDclCreateTime(String dclCreateTime) {
		this.dclCreateTime = dclCreateTime;
	}

	public String getDclCreateUser() {
		return dclCreateUser;
	}

	public void setDclCreateUser(String dclCreateUser) {
		this.dclCreateUser = dclCreateUser;
	}

	public String getDclModifyTime() {
		return dclModifyTime;
	}

	public void setDclModifyTime(String dclModifyTime) {
		this.dclModifyTime = dclModifyTime;
	}

	public String getDclModifyUser() {
		return dclModifyUser;
	}

	public void setDclModifyUser(String dclModifyUser) {
		this.dclModifyUser = dclModifyUser;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	 public String toString() {
	    	return "CanalCliProperty [dclId="+dclId+",dclCanalIp="+dclCanalIp+",dclUserName="+dclUserName
	    			+",dclPassword="+dclPassword+",dclZkServer="+dclZkServer+",dclDestination="+dclDestination
	    			+",dclSubscribe="+dclSubscribe+",dclCreateTime="+dclCreateTime+",dclCreateUser="+dclCreateUser
	    			+",dclModifyTime="+dclModifyTime+",dclModifyUser="+dclModifyUser+"]";
	    }

	/**
	 * date  2018年7月4日下午5:30:00
	 * author huangqin
	 */
	public String getDcDestination() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	

}
