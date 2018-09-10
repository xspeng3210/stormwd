package com.citiccard.dasp.dc.rltm.coll.store.imp;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.citiccard.dasp.dc.rltm.coll.store.IDataStore;

public class RocksDbClient implements IDataStore {
	private static final Logger logger = LoggerFactory.getLogger(RocksDbClient.class);
	private static RocksDB db;
	private ScheduledExecutorService executor;
	private Set<Map<String,String>> putTasks;
	private long period = 5000;
	
	public RocksDbClient() {
		start();
	}
	
	private void start() {
		/**
		 * 1 ���ص�dll�ļ�������ʹ��ϵͳ��Ҫ���м���
		 * 2 windows��64��λ�п�����Ҫ����������dll����
		 * 3 linux����Ҫ����
		 */
		Options options = new Options();
		options.setCreateIfMissing(true);
		try {
			String linuxPath = "/opt/rocksFile";
			db = RocksDB.open(options,linuxPath);
		}catch(RocksDBException e) {
			e.printStackTrace();
		}
		executor = Executors.newScheduledThreadPool(1);
		putTasks = Collections.synchronizedSet(new HashSet<Map<String,String>>());
		//������ʱ��������
		executor.scheduleAtFixedRate(new Runnable() {
			public void run() {
				List<Map<String,String>> tasks = new ArrayList<Map<String,String>>(putTasks);
				for(Map<String,String> task: tasks) {
					try {
						//��ʱ���ڴ��е�����ֵˢ��rocksDB��
						task.forEach((k,v) ->{
							put(k,v);
							logger.info("д��ɹ���[{}]",k);
						});
						putTasks.remove(task);
					}catch (Exception e) {
						logger.error("period put"+task.toString()+"rocksDB failed!",e);
					}
				}
			}
		}, period, period, TimeUnit.MILLISECONDS);
	}

	@Override
	public void put(String key,String value) {
		try {
			db.put(key.getBytes("utf-8"),value.getBytes("utf-8"));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void close() {
		db.close();
		executor.shutdownNow();
	}
	
	@Override
	public String get(String key)throws RocksDBException,UnsupportedEncodingException{
		String value = new String(db.get(key.getBytes("utf-8")),StandardCharsets.UTF_8);
		return value;
	}
	
	@Override
	public List<String> getList(){
		return null;
	}
	
	@Override
	public void periodPut(String key,String value) {
		Map<String, String> putTask = new HashMap<String,String>();
		putTask.put(key, value);
		putTasks.add(putTask);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
