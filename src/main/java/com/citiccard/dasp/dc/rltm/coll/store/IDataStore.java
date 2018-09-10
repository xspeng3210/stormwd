package com.citiccard.dasp.dc.rltm.coll.store;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.rocksdb.RocksDBException;

/**
 *���ݴ洢�ӿ� 
 *
 */

public interface IDataStore {
	void put(String key,String value);
	
	/**
	 * ��ʱд��
	 */
	void periodPut(String key,String value);
	
	String get(String key)throws RocksDBException,UnsupportedEncodingException;
	
	List<String> getList();
	
	void close();
	
	
	
	
	
	
	
	
	
	
	

}
