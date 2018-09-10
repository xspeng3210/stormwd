package com.citiccard.dasp.dc.rltm.coll.kafka;

import java.util.Map;

import static com.citiccard.dasp.dc.rltm.coll.common.Const.KEY_DELIMETER;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * date  2018年7月4日上午9:16:26
 * author huangqin
 * 
 * 自定义kafka分区类，根据传入的Key,解析出在Prepartition出设置partition
 */
public class DaspKafkaPartitioner implements Partitioner {

	private static Logger LOG=LoggerFactory.getLogger(DaspKafkaPartitioner.class);
    

	
	public void configure(Map<String, ?> arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public void close() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 使用key中已预置的分区号进行分区
	 */
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		String innerKey =(String) key;
		
		String[] items =innerKey.split(KEY_DELIMETER);
		
		return Integer.parseInt(items[1]);
	}

}
