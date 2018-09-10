package com.citiccard.dasp.dc.rltm.coll.dao;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * date  2018年7月3日下午5:40:49
 * author huangqin
 */
public class RedisController {
	
	private JedisPool jedisPool;
	
	private static RedisController redisController;
	
	public static RedisController getRedisController(String ip,int port){
		if(null==redisController){
			redisController =new RedisController(ip,port);	
		}
		return redisController;
	}
	
	private RedisController(String ip,int port){
		jedisPool =new JedisPool(createJedisConfig(),ip,port,5000,"redis@dasp");
	}
	
	private  JedisPoolConfig createJedisConfig(){
		JedisPoolConfig poolConfig=new JedisPoolConfig();
		//set pool parameter
		poolConfig.setMaxIdle(100);
		poolConfig.setMaxWaitMillis(3000);
		poolConfig.setTestOnBorrow(true);
		return poolConfig;
	}
	public Jedis getController(){
		return jedisPool.getResource();
	}
	
	public void destory(){
		jedisPool.destroy();
	}

}
