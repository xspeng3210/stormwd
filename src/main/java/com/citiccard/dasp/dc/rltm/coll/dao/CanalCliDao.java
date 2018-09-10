package com.citiccard.dasp.dc.rltm.coll.dao;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.citiccard.dasp.dc.rltm.coll.model.CanalCliMessageVo;
import com.citiccard.dasp.dc.rltm.coll.model.CanalCliProperty;
import com.citiccard.dasp.dc.rltm.coll.model.DbInfoMessageVo;
import com.citiccard.dasp.dc.rltm.coll.model.TableMetaData;
import com.citiccard.dasp.dc.rltm.coll.util.CommonUtil;



/**
 * date  2018年7月3日下午5:06:36
 * author huangqin
 */
public class CanalCliDao {
	private static Logger LOG=LoggerFactory.getLogger(CanalCliDao.class);
    private static String MANAGER_URL="http://22.106.103.67:8383/dasp-dc-coll-ctrl/getcanalclipro/";
    //测试环境虚拟机 与数据
   // private static String META_URL="http://22.106.103.67:8381/dasp-bf-meta-data-mgmt/columns/sqlanalysis";
    private static String META_URL="http://28.104.84.205:8081/dasp-bf-meta-data-mgmt/columns/sqlanalysis";
    //测试环境  28.104.84.200 dasp_mdm_test
    // private static String META_URL="http://startcard-dasp.testapps.oa.citicbank.com/dasp-bf-meta-data-mgmt/columns/sqlanalysis";
    //开发环境  28.106.103.237 test
    // private static String META_URL="http://startcard-dasp.testapps.oa.citicbank.com/dasp-bf-meta-data-mgmt/columns/sqlanalysis";
    
    
    /*
     * 查询canalClient配置信息
     */
    
    public static CanalCliProperty getCliProperty(String destination)throws Exception{
    	URL url =new URL(MANAGER_URL + destination);
    	HttpURLConnection conn=(HttpURLConnection) url.openConnection();
    	conn.setDoOutput(true);
    	conn.setDoInput(true);
    	conn.setRequestMethod("GET");
    	conn.setRequestProperty("connection", "Keep-Alive");
    	conn.setRequestProperty("Charset", "Keep-UTF-8");
    	conn.setUseCaches(false);
    	conn.setRequestProperty("Content-Type", "application/json");
    	conn.connect();
    	if(conn.getResponseCode() == 200){
    		LOG.info("[{}]","连接成功");
    	}else{
    		LOG.warn("[{}]","连接失败");
    	}
    	InputStream is=conn.getInputStream();
    	String propertyStr=null;
    	try{
    		byte[] data=new byte[is.available()];
    		is.read(data);
    		propertyStr=new String(data,"UTF-8");
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	CanalCliMessageVo canlaCliVO=CommonUtil.getObjectAsBean(propertyStr,CanalCliMessageVo.class);
    	CanalCliProperty cliProperty=canlaCliVO.getData();
    	//没有设置username和password
    	cliProperty.setDclUserName("");
    	cliProperty.setDclPassword("");
    	
    	return cliProperty;
    }
    
    /**
     * 元数据上报元数据管理系统
     */
    public static DbInfoMessageVo  sentTableMetaData(TableMetaData meta)throws Exception{
    	
    	URL url =new URL(META_URL); 
    	HttpURLConnection conn=(HttpURLConnection) url.openConnection();
    	conn.setDoOutput(true);
    	conn.setDoInput(true);
    	conn.setRequestMethod("PUT");
    	conn.setRequestProperty("connection", "Keep-Alive");
    	conn.setRequestProperty("Charset", "Keep-UTF-8");
    	conn.setUseCaches(false);
    	conn.setRequestProperty("Content-Type", "application/json");
    	conn.setConnectTimeout(500000);
    	conn.setReadTimeout(5000000);
    	conn.connect();
    	OutputStream out=conn.getOutputStream();
    	out.write(CommonUtil.beanToJson(meta).getBytes("utf-8"));
    	out.flush();
    	out.close();
    	if(conn.getResponseCode() == 200){
    		LOG.info("[{}]","连接成功");
    	}else{
    		LOG.warn("[{}]","连接失败");
    	}
    	InputStream is=conn.getInputStream();
    	String result=null;
    	try{
    		byte[] data=new byte[is.available()];
    		is.read(data);
    		result=new String(data,"UTF-8");
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	DbInfoMessageVo resultInfo=CommonUtil.getObjectAsBean(result,DbInfoMessageVo.class);
        return resultInfo;
    }

}
