package com.citiccard.dasp.dc.rltm.coll.meta;

import com.citiccard.dasp.dc.rltm.coll.model.TableMetaData;

/**
 * date  2018年7月4日上午10:24:47
 * author huangqin
 */
public interface MetaManger {
	/**
	 * 元数据上报
	 */
	
	void sentMetaInfo(TableMetaData metaData);
    
	void start();
	
	void stop();
	
}
