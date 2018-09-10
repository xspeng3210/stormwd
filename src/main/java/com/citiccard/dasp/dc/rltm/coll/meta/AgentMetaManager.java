package com.citiccard.dasp.dc.rltm.coll.meta;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.citiccard.dasp.dc.rltm.coll.dao.CanalCliDao;
import com.citiccard.dasp.dc.rltm.coll.model.DbInfoMessageVo;
import com.citiccard.dasp.dc.rltm.coll.model.TableMetaData;



/**
 * date  2018年7月4日上午10:15:00
 * author huangqin
 */
public class AgentMetaManager  implements MetaManger{
	private static Logger LOG=LoggerFactory.getLogger(AgentMetaManager.class);
	private ExecutorService executor;
	
	public AgentMetaManager(){
		start();
	}
	
	public void start(){
		executor =Executors.newSingleThreadExecutor();
	}

	public void sentMetaInfo(final TableMetaData metaData){
		executor.submit(new Runnable() {
	
			public void run() {
				doSent(metaData);
			}
		});
	}
	
	public void stop(){
		executor.shutdown();
	}
	
	private void doSent(TableMetaData metaData){
		String tableName=metaData.getTableName();
		try{
			DbInfoMessageVo resultInfo=CanalCliDao.sentTableMetaData(metaData);
			LOG.info("[{}]", tableName +"===>>result info:" +resultInfo.toString());
		}catch(Exception e){
			LOG.info("[{}]", "数据上报管理中心失败，tableName："+tableName );
			
			e.printStackTrace();
		}
	}

	

}
