package com.citiccard.dasp.dc.rltm.coll.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * date  2018年7月4日上午10:05:31
 * author huangqin
 */
public class KafkaSender {
	private Properties properties =new Properties();
	private Producer<String,String> producer  =null;
	
	public  KafkaSender(){
		init();
	}
	
	public static void main(String[] args) {
		
	}
	
	private void init(){
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("bootstrap.servers", "192.168.48.90:9092,192.168.48.100:9092,192.168.48.110:9092");
		
		properties.put("acks", "all");
		properties.put("partitioner.class", "com.citiccard.dasp.dc.rltm.coll.kafka.DaspKafkaPartitioner");
		producer =new KafkaProducer<>(properties);
	
	}
	
	/**
	 * 发送数据到kafka
	 */
	public void send(String key,String value){
		String[] keyList=key.split(":");
		producer.send(new ProducerRecord<String,String>(keyList[0],key,value));
	}

}
