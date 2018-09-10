package com.citiccard.dasp.dc.rltm.coll.common.zookeeper;

import java.text.MessageFormat;

public class ZookeeperPathUtils {

	public static final String ZOOKEEPER_SEPARATOR = "/";

	public static final String COLL = "coll";
	public static final String AGENTS = "agents";
	public static final String TOPICS = "topics";
	public static final String PARTITIONS = "partitions";
	public static final String MESSAGE = "message";

	/**
	 * 采集器中对应topic的oddset
	 */
	public static final String AGENT_TOPIC_OFFSET = ZOOKEEPER_SEPARATOR + COLL + ZOOKEEPER_SEPARATOR + AGENTS
			+ ZOOKEEPER_SEPARATOR + "{0}" + ZOOKEEPER_SEPARATOR + TOPICS + ZOOKEEPER_SEPARATOR + "{1}"
			+ ZOOKEEPER_SEPARATOR + PARTITIONS + ZOOKEEPER_SEPARATOR + "{2}" + ZOOKEEPER_SEPARATOR + MESSAGE;

	public static String getAgentTopicMessageIdPath(String agentId, String topic, String partition) {
		return MessageFormat.format(AGENT_TOPIC_OFFSET, agentId, topic, partition);
	}
}
