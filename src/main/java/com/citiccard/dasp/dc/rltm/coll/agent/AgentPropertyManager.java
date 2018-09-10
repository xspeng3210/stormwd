package com.citiccard.dasp.dc.rltm.coll.agent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.citiccard.dasp.dc.rltm.coll.agent.impl.CanalAgent;
import com.citiccard.dasp.dc.rltm.coll.dao.CanalCliDao;
import com.citiccard.dasp.dc.rltm.coll.model.CanalCliProperty;



public class AgentPropertyManager {
	private static Logger LOG=LoggerFactory.getLogger(AgentPropertyManager.class);
    
	/**
	 * 从本地获取canal配置
	 */
	public CanalCliProperty getCliPropertyLocal(){
		Properties prop=new Properties();
		CanalCliProperty cliProperty=new CanalCliProperty();
		InputStream in=CanalAgent.class.getClassLoader().getResourceAsStream("canal.properties");
	    try{
	    	prop.load(in);
	    	cliProperty.setDclZkServer(prop.getProperty("zkServer"));
	    	cliProperty.setDclDestination(prop.getProperty("instance"));
	    	cliProperty.setDclSubscribe(prop.getProperty("subscribe"));
	    	cliProperty.setDclUserName("");
	    	cliProperty.setDclPassword("");
	    }catch(IOException e){
	    	e.printStackTrace();
	    }
	    return cliProperty;
	}
	
	/**
	 * canal配置
	 */
	public CanalCliProperty getCliPropertyWrited(){
		CanalCliProperty cliProperty= new CanalCliProperty();
		cliProperty.setDclZkServer("22.106.103.5:2181,22.106.103.51:2181,22.106.103.54:2181");
    	cliProperty.setDclDestination("dasp-user-two");
    	cliProperty.setDclSubscribe(".*\\..*");
    	cliProperty.setDclUserName("");
    	cliProperty.setDclPassword("");
        return cliProperty;
	}
	
	/**
	 * 从主控管理中心读取canal配置
	 * 
	 */
	public CanalCliProperty getCliPropertyFromManager(String destination){
		// 1 获取配置信息
		CanalCliProperty cliProperty =null;
		try{
			cliProperty=CanalCliDao.getCliProperty(destination);
		}catch(Exception e){
			LOG.error("[{}]","从主控管理系统获取canal client 配置信息失败!");
			e.printStackTrace();
		}
		return cliProperty;
	}
}
