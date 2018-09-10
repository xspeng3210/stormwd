package com.citiccard.dasp.dc.rltm.coll.counter;

import com.citiccard.dasp.dc.rltm.coll.dao.RedisDao;

/**
 * date  2018年7月3日下午4:57:27
 * author huangqin
 */
public class RedisCounter implements Conter {

	private RedisDao redisDao=null;
	//Test
	private String redisHost="22.106.103.3";
	private int redisPort =6379;
	
	private static final String DELEMTER ="_";
	
	public RedisCounter(){
		
		redisDao=RedisDao.getInstance(redisHost,redisPort);
	}
	public String get(String topic, String partition) {
		// 获取Redis执行topic的记时器
		String key=gegenerateKey(topic,partition);
		long messageId=redisDao.incr(key);
		return String.valueOf(messageId);
	}
	
	/**
	 * 生成MessageId,redis的key
	 */
	
	private String gegenerateKey(String topic,String partition){
		String key=topic + DELEMTER + partition;
		return key;
	}

}
