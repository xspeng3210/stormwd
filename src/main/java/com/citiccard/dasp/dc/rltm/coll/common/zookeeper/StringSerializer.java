package com.citiccard.dasp.dc.rltm.coll.common.zookeeper;

import java.io.UnsupportedEncodingException;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

/**
 * 基于String的序列化
 */
public class StringSerializer implements ZkSerializer {

	@Override
	public Object deserialize(byte[] bytes) throws ZkMarshallingError {
		try{
			return new String(bytes,"utf-8");
		}catch(final UnsupportedEncodingException e){
			throw new ZkMarshallingError(e);
		}
	
	}

	@Override
	public byte[] serialize(final Object data) throws ZkMarshallingError {
		try{
			return ((String) data).getBytes("utf-8");
		}catch(final UnsupportedEncodingException e){
			throw new ZkMarshallingError(e);
		}
	
	}

}
