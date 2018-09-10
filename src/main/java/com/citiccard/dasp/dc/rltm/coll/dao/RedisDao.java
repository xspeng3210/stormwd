package com.citiccard.dasp.dc.rltm.coll.dao;

import redis.clients.jedis.Jedis;

/**
 * date  2018年7月3日下午5:49:31
 * author huangqin
 */
public class RedisDao {
	private RedisController redisController=null;
	
	private static RedisDao redisDao=null;
	
	private RedisDao(String hostname,int port){
		redisController=RedisController.getRedisController(hostname,port);
	}
	
	public static RedisDao getInstance(String hostName,int port){
		if(null==redisDao){
			redisDao =new RedisDao(hostName, port);
		}
		return redisDao;
	}

	
	public String hget(String key,String field){
		Jedis jedis=redisController.getController();
		String rslt=null;
		try{
			rslt=jedis.hget(key, field);
			
		}finally{
			jedis.close();
		}
		
		return rslt;
		
	}
	
    public long incr(String key){
    	Jedis jedis=redisController.getController();
    	long messageId=1;
    	try{
    		if(jedis.exists(key)){
    			messageId =jedis.incr(key);
    		}else{
    			jedis.set(key, String.valueOf(messageId));
    		}
    	}finally{
    		jedis.close();
    	}
    	return messageId;
    }
}
