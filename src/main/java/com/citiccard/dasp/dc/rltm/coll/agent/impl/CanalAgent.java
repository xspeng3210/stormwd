package com.citiccard.dasp.dc.rltm.coll.agent.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.Message;
import com.citiccard.dasp.dc.rltm.coll.common.PrePartitioner;
import com.citiccard.dasp.dc.rltm.coll.common.zookeeper.imp.ZookeeperMessageIdManager;
import com.citiccard.dasp.dc.rltm.coll.kafka.KafkaSender;
import com.citiccard.dasp.dc.rltm.coll.meta.MetaManger;
import com.citiccard.dasp.dc.rltm.coll.model.BinlogReport;
import com.citiccard.dasp.dc.rltm.coll.model.MessageIdRange;
import com.citiccard.dasp.dc.rltm.coll.model.TableMetaData;
import com.citiccard.dasp.dc.rltm.coll.store.IDataStore;
import com.citiccard.dasp.dc.rltm.coll.util.CommonUtil;
import com.citiccard.dasp.dc.rltm.coll.agent.FetchAgent;
import com.google.common.base.Function;
import com.google.common.collect.MigrateMap;




/**
 * 采集数据客户端
 */
public class CanalAgent implements FetchAgent {
	private static Logger LOG=LoggerFactory.getLogger(CanalAgent.class);
	
	private String destination;
	private CanalConnector connector=null;
	private int batchSize=150;//设定canal每次尝试获取binlog的批次数量,默认是150
	
	//元数据采集上报
	private MetaManger metaManager=null;
	//消息封装messageId管理
	private String agentId;
	private ZookeeperMessageIdManager zookeeperMessageIdManager=null;
	long bachId=-1L;
	long bachNum=-1L;
	//数据发送kafka
	private int partitions=3;//kafka默认的分区数量
	private static Map<String,PrePartitioner>  prePartitioners=MigrateMap
			    .makeComputingMap(new Function<String,PrePartitioner>(){
			    	public PrePartitioner apply(String topic){
			    		return new PrePartitioner();
			    	}
			    }); 
	private KafkaSender kafkaSender=null;
	//数据保存
	private IDataStore rockClient=null;
	
	private BinlogReport binlogReport=new BinlogReport();
	private TableMetaData tableMetaData=new TableMetaData();
	
	
	private long bachCount=0L;
	private long bachCountAll=0L;
	private long afterGetWithoutAckAll=0L;
	private long afterParseAndSendEntryAll=0L;
	private long afterCanalACKTimeAll=0L;
	private long getBinlogTime=0L;
	
	public void fetch(){
		while(true){
			long beforGetWithoutAck=System.currentTimeMillis();
			
			Message message=connector.getWithoutAck(100);
		
			bachId=message.getEntries().size();
			
			
			/**
			 * 测试获取binlog事件start
			 */
			long afterGetWithoutAck=System.currentTimeMillis();
			getBinlogTime=afterGetWithoutAck;
			
			if(bachNum>0){
				long getWithoutAckSpendTime=afterGetWithoutAck -beforGetWithoutAck;
				afterGetWithoutAckAll +=getWithoutAckSpendTime;
				LOG.debug("canal[{}]",
						"获取"+bachNum+"个批次的数据花费时间:"+getWithoutAckSpendTime);
		
			}
			/**
			 * 测试获取binlog时间end
			 */
		    if(bachId ==-1 || bachNum ==0){
		    	try{
		    		Thread.sleep(2000);
		    	}catch(InterruptedException e){
		    		e.printStackTrace();
		    	}
		    	LOG.info("[{}]","没有数据！请在数据库中执行操作！");
		    	continue;
		    }
		    
		    /**
		     * 数据解析  发送kafka start
		     */
		    long beforParseAndSendEntey=System.currentTimeMillis();
		    List<Entry> entrys=message.getEntries();
		    bachCount =0;
		    for(Entry entry:entrys){
		    	if(entry.getEntryType()== EntryType.TRANSACTIONBEGIN
		    			||entry.getEntryType()==EntryType.TRANSACTIONEND){
		    		LOG.info("[{}]","EntryType:"+entry.getEntryType());
		    		continue;
				    
		    	}
		    	parseAndSendEntry(entry);
		    }
		    
		    /**
		     * 数据解析发送 kafka end
		     */
		    bachCount +=bachCount;
		    long afterParseAndSendEntry=System.currentTimeMillis();
		    
		    long getParseAndSendEntrySpendTime=afterParseAndSendEntry -beforParseAndSendEntey;
		    afterParseAndSendEntryAll = afterParseAndSendEntryAll +getParseAndSendEntrySpendTime;
		    
		    long beforCanalACKTime=System.currentTimeMillis();
		    connector.ack(bachId);
		    long afterCanalACKTime=System.currentTimeMillis();
		    long getCanalACKTime=afterCanalACKTime-beforCanalACKTime;
		    afterCanalACKTime +=getCanalACKTime;

		}
		
	}
	
	/**
	 * 处理Entry 数据
	 */
	private void parseAndSendEntry(Entry entry){
	RowChange rowChange=null;
	EventType enventType=EventType.QUERY;
	try{
		rowChange=RowChange.parseFrom(entry.getStoreValue());
		if(rowChange !=null){
			enventType=rowChange.getEventType();
		}
	}catch (Exception e){
		throw new RuntimeException("ERROR ## parse has an erro"+entry.toString(),e);
	}
	String tableName=entry.getHeader().getTableName();
	String schemaName=entry.getHeader().getSchemaName();
	
	LOG.info("[{}]","schemaName:"+schemaName);
	LOG.info("[{}]","tableName:"+tableName);
	
	//元数据上报
	if(rowChange !=null &&rowChange.getIsDdl()){
		TableMetaData metaData=new TableMetaData();
		metaData.setSql(rowChange.getSql());
		metaData.setSchema(rowChange.getDdlSchemaName());
		metaData.setTableName(entry.getHeader().getTableName());
		metaData.setExecuteTime(entry.getHeader().getExecuteTime());
		metaData.setExecuteType(rowChange.getEventType().toString());
		LOG.info("[{}]","数据上报管理中心:"+metaData.toString());
		metaManager.sentMetaInfo(metaData);

	
	}
	
	/**
	 * 1 解析dml变更数据
	 * 2 数据分区
	 * 3数据保存RocksDB
	 * 4数据发送kafka
	 */
	
	//数据解析封装binlogReport 注意一个entry包含多条更新记录
	List<RowData> rowDatas=new ArrayList<RowData>();
	if(rowChange !=null){
		rowDatas=rowChange.getRowDatasList();
	}
	for(RowData rowData : rowDatas){
		//清空记录
		this.binlogReport.clear();
		//封装body
		if(enventType== EventType.DELETE){
			putColumnToBody(rowData.getBeforeColumnsList());
		}else if(enventType== EventType.INSERT){
			putColumnToBody(rowData.getAfterColumnsList());

		}else if(enventType== EventType.UPDATE){
			putColumnToBody(rowData.getAfterColumnsList());

		}
		//topic预分区
		PrePartitioner  prePartition=getPrePartitioner(schemaName);
		//一个topic的每个分区独有一个messageIdRange在维护该分区的messageId
		MessageIdRange messageIdRange=prePartition.getMessageIdRange();
		//封装head
		putHeaderInfo(entry,rowChange,messageIdRange);
		//生成对应的key
		String key=prePartition.generateKey(messageIdRange);
		//查看是否需要将messageIdRange写到zk
		prePartition.updateMessageIdRange(messageIdRange);
		//发送kafka
		//kafkaSender.send(key,CommonUtil.beanToJson(binlogReport));
		LOG.info("[{}]","成功发送kafka:"+key);
		//数据保存RocksDB
		//rockClient.periodPut(key,CommonUtil.beanToJson(binlogReport));
		bachCount++;
	}

}
	/**
	 * 封装header
	 */
	private void putHeaderInfo(Entry entry,RowChange rowChange,MessageIdRange messageIdRange){
		this.binlogReport.setMessageId(messageIdRange.getMessageId());
		this.binlogReport.setAgentId(messageIdRange.getAgentId());
		this.binlogReport.setBatchId(bachId);
		this.binlogReport.setBatchSize(bachNum);
		
		this.binlogReport.setSqlExecuteTime(entry.getHeader().getExecuteTime()+"");
		this.binlogReport.setTablename(entry.getHeader().getTableName());
		this.binlogReport.setSchema(entry.getHeader().getSchemaName());
		this.binlogReport.setEventType(entry.getEntryType().name());
		this.binlogReport.setSendToKafkaTime(System.currentTimeMillis()+"");
		this.binlogReport.setGetBinlogTime(getBinlogTime + "");
		

	}
	/**
	 * 將Column封裝到binlogreport的body
	 */
	
	private void putColumnToBody(List<Column> columnsList){
		for(Column column : columnsList){
			binlogReport.getBody().put(column.getName(),column.getValue());
		}
		
	}
	
	/**
	 * 每個topic會生成一個預分區
	 */
	private PrePartitioner getPrePartitioner(String topic){
		PrePartitioner prePartition=prePartitioners.get(topic);
		prePartition.setAgentId(agentId);
		prePartition.setTopic(topic);
		prePartition.setPartitions(partitions);
		prePartition.setZookeeperMessageIdManager(zookeeperMessageIdManager);
		return prePartition;
				
	}


	public CanalConnector getConnector() {
		return connector;
	}

	public void setConnector(CanalConnector connector) {
		this.connector = connector;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public MetaManger getMetaManager() {
		return metaManager;
	}

	public void setMetaManager(MetaManger metaManager) {
		this.metaManager = metaManager;
	}

	public ZookeeperMessageIdManager getZookeeperMessageIdManager() {
		return zookeeperMessageIdManager;
	}

	public void setZookeeperMessageIdManager(ZookeeperMessageIdManager zookeeperMessageIdManager) {
		this.zookeeperMessageIdManager = zookeeperMessageIdManager;
	}

	public KafkaSender getKafkaSender() {
		return kafkaSender;
	}

	public void setKafkaSender(KafkaSender kafkaSender) {
		this.kafkaSender = kafkaSender;
	}

	public IDataStore getRockClient() {
		return rockClient;
	}

	public void setRockClient(IDataStore rockClient) {
		this.rockClient = rockClient;
	}


	
}
