package com.citiccard.dasp.dc.rltm.coll.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtil {
	private static ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * ��һ������ת��json�ַ���
	 */
	public static <T> String beanToJson(T t) {
		String json = null;
		try {
			json = mapper.writeValueAsString(t);
		}catch(JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * ��json�ַ���ת���ɶ���
	 */
	public static <T> T getObjectAsBean(String jsonInput,Class<T> clz) {
		T t = null;
		try {
			t = mapper.readValue(jsonInput,clz);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return t;
	}

}
