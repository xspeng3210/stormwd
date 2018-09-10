package com.citiccard.dasp.dc.rltm.coll.kafka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * date  2018年7月4日上午9:28:24
 * author huangqin
 */
public class KafkaDaspConsumer {
	public static void main(String[] args) {
		Properties properties =new Properties();
		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("bootstrap.servers", "22.106.103.98:9092,22.106.103.102:9092,22.106.103.103:9092");
		properties.put("group.id", "test");
		
		KafkaConsumer<String,String> consumer=new KafkaConsumer<>(properties);
		
		consumer.subscribe(Arrays.asList("act_proc_db_r2"));
		int count =0;
		Map<String,Map<String,String>> partZero=new HashMap<>();
		Map<String,Map<String,String>> partOne=new HashMap<>();
		Map<String,Map<String,String>> partTwo=new HashMap<>();
		List<Map<String,Map<String,String>>> parts= new ArrayList<>();
		parts.add(0,partZero);
		parts.add(1,partOne);
		parts.add(2,partTwo);
		while(count < 100000){
			ConsumerRecords<String,String> records = consumer.poll(100);
			if(records.isEmpty()){
				try{
					Thread.sleep(3000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			
			for(ConsumerRecord<String, String> record: records){
				 checkRecord(record,parts);
				 count++;
				 System.out.println("当前消费第:" + count +"条");
			
			}
		}
		consumer.close();
		
	}
	
    /**
     * 默认3分区，检验同一分区，同一batchI到的messageId 是否连续
     */
    private static void checkRecord(ConsumerRecord<String, String> record,
    		List<Map<String,Map<String,String>>> parts){
    	System.out.println(record.key() + "  "+record.value());
    	String key=record.key();
    	if(key == null){
    		return;
    	}
    	String[] keyList =key.split(":");
    	String partition =keyList[1];
    	String batchId=keyList[2];
    	String messageId=keyList[3];
    	Map<String,Map<String,String>> part=parts.get(Integer.parseInt(partition));
    	if(part.get(batchId)==null){
    		Map<String,String> batMap= new HashMap<>();
    		part.put(batchId, batMap);
    		System.out.println("**********开始消费" + batchId + "batch 第" + partition +
    				"个partition 的第一条数据：" + "tableNames:" +record.topic() + "," +"partition:" +
    				partition + ",batchId:" +batchId);
    	}else{
    		String lastMessageId=part.get(batchId).get("LastMessageId");
    		if(Long.parseLong(lastMessageId) + 1!=Long.parseLong(lastMessageId)){
    			System.out.println("============对十数据### tableName:"+ record.topic() + "," +"partition:" +
        				partition + ",batchId:" +batchId + ",messageId:" +messageId);
    		}else{
    			System.out.println("########成功消费 tableName:"+ record.topic() + "," +"partition:" +
        				partition + ",batchId:" +batchId + ",messageId:" +messageId);
    		}
    		part.get(batchId).put("LastMessageId", messageId);
    	}
    }

}
