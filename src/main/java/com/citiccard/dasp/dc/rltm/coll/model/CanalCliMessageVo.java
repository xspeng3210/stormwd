package com.citiccard.dasp.dc.rltm.coll.model;

import java.io.Serializable;
import java.util.List;
import com.citiccard.dasp.dc.rltm.coll.model.CanalCliProperty;

public class CanalCliMessageVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	String code;
	
	String message;
	
	CanalCliProperty data;
	
	List<CanalCliProperty> datas;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
  	
	public String getMessage() {
		return message;
	}
	
	public CanalCliProperty getData() {
		return data;
	}
	
	public void setData(CanalCliProperty data) {
		this.data = data;
	}
	
	public List<CanalCliProperty> getDatas(){
		return datas;
	}
	
	public void setDatas(List<CanalCliProperty> datas){
		this.datas = datas;
	}
	
    public static long getSerialversionuid() {
    	return serialVersionUID;
    }
    
    
}
