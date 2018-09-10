package com.citiccard.dasp.dc.rltm.coll.hbase;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;





/**
 * date  2018年7月3日下午5:59:57
 * author huangqin
 */
public class HbaseDaspClient {
	private Configuration config=null;
	private Connection connection=null;
	private Table table=null;
	private List<Put> putList =new ArrayList<Put>();
	private static String TABLE_NAME="canaltest:binlog_data";
	private static String TABLE_FAMILY_NAME="family01";
	private static String FAMILY_COLUM_NAME="binlog";
	
	
    public HbaseDaspClient(){
    	init();
    }
    /**
     * 初始化变量
     */

    private void init(){
    	config=HBaseConfiguration.create();
    	try{
    		connection = ConnectionFactory.createConnection(config);
    		table=connection.getTable(TableName.valueOf(TABLE_NAME));;
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
    
    /**
     * 保存到hbase
     */
    public void saveData(String key,String value){
    	String[] keyArray=key.split(":");
    	try{
    		Put p=new Put(Bytes.toBytes(keyArray[1] +keyArray[2] +keyArray[3]));
    		p.addColumn(Bytes.toBytes(TABLE_FAMILY_NAME),Bytes.toBytes(FAMILY_COLUM_NAME),Bytes.toBytes(value));
    	    table.put(p);
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
    
    /**
     * 批量保存到hbase
     */
    
   public void savePutList(String key,String value){
	   String[] keyAarray =key.split(":");
	   //kafka分区号，batchId,messageId 作为rowkey
	   Put p=new Put(Bytes.toBytes(keyAarray[1] +keyAarray[2] +keyAarray[3]));
	   p.addColumn(Bytes.toBytes(TABLE_FAMILY_NAME),Bytes.toBytes(FAMILY_COLUM_NAME),Bytes.toBytes(value));
	   putList.add(p);
   }
   
   /**
    * 数据暂存putList
    */
   
   public void saveDataList(){
	  try{
		  table.put(putList);
		  putList.clear();
	  }catch(IOException e){
		  e.printStackTrace();
	  }
   }
   
   /**
    * 关闭hbase连接
    */
   
   public void closeConnection(){
	   try{
		   connection.close();
		   table.close();
	   }catch(IOException e){
		 e.printStackTrace();   
	   }
   }
   
  public static void main(String[] args) {
	HbaseDaspClient hbaseCli=new HbaseDaspClient();
	hbaseCli.saveData("canal_test_storm_four:1:ac8af694-7dde-47e3-8ad1-18722f0d8d1:3",
			"ac8af694-7dde-47e3-8ad1-1872c2f0d8d1");
	hbaseCli.closeConnection();
  }
}

