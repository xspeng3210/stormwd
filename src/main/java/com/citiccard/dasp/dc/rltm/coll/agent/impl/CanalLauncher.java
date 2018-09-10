package com.citiccard.dasp.dc.rltm.coll.agent.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.zookeeper.ZkClientx;
import com.citiccard.dasp.dc.rltm.coll.agent.AgentPropertyManager;
import com.citiccard.dasp.dc.rltm.coll.common.zookeeper.imp.ZookeeperMessageIdManager;
import com.citiccard.dasp.dc.rltm.coll.kafka.KafkaSender;
import com.citiccard.dasp.dc.rltm.coll.meta.AgentMetaManager;
import com.citiccard.dasp.dc.rltm.coll.meta.MetaManger;
import com.citiccard.dasp.dc.rltm.coll.model.CanalCliProperty;
import com.citiccard.dasp.dc.rltm.coll.store.IDataStore;
import com.citiccard.dasp.dc.rltm.coll.store.imp.RocksDbClient;

public class CanalLauncher {
	private static Logger LOG=LoggerFactory.getLogger(CanalLauncher.class);
	private static String INSTANCE_DESTINATION="example";
	private static String AGENT_ID="1";

	public static void main(String[] args) {
		LOG.info("[{}]","Canal Agent Starting!");
		CanalAgent canalAgent =new CanalAgent();
		if(args.length>0&&args[0]!=null&&args[0].length()>0) {
			canalAgent.setDestination(args[0]);		
		}else {
			canalAgent.setDestination(INSTANCE_DESTINATION);
		}
		LOG.info("[{}]","Destination:"+canalAgent.getDestination());
		LOG.info("[{}]","主控管理系统获取canal client 配置信息！");
		AgentPropertyManager propertyManager=new AgentPropertyManager();
		CanalCliProperty cliProperty= propertyManager.getCliPropertyFromManager(canalAgent.getDestination());
		if(cliProperty==null) {
			LOG.info("[{}]","读取本地配置文件！");
			cliProperty=propertyManager.getCliPropertyLocal();
		}
		LOG.info("[{}]",cliProperty.toString());
		LOG.info("[{}]","init ZookeeperMessageIdManager!");
		canalAgent.setAgentId(AGENT_ID);
		ZkClientx zKClientx=new ZkClientx(cliProperty.getDclZkServer());
		ZookeeperMessageIdManager zmIdManager=new ZookeeperMessageIdManager();
		zmIdManager.setZkClientx(zKClientx);
		canalAgent.setZookeeperMessageIdManager(zmIdManager);
		LOG.info("[{}]","init metamanager!");
		MetaManger metaManager=new AgentMetaManager();
		canalAgent.setMetaManager(metaManager);
		LOG.info("[{}]","init kafka！");
		KafkaSender kafkaSender =new KafkaSender();
		canalAgent.setKafkaSender(kafkaSender);
		LOG.info("[{}]","init rockClient！");
		IDataStore rockClient =new RocksDbClient();
		canalAgent.setRockClient(rockClient);
		LOG.info("[{}]","init canalConnector！");
		CanalConnector connector =CanalConnectors.newClusterConnector(cliProperty.getDclZkServer(),
				cliProperty.getDclDestination(),cliProperty.getDclUserName(),cliProperty.getDclPassword());
		connector.connect();
		connector.subscribe(cliProperty.getDclSubscribe());
		canalAgent.setConnector(connector);
		LOG.info("[{}]","do fetch! agentId:"+canalAgent.getAgentId()+" destination:"+canalAgent.getDestination());
		canalAgent.fetch();
	}
}

