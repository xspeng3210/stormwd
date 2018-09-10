package com.citiccard.dasp.dc.rltm.coll.common.zookeeper;

import com.citiccard.dasp.dc.rltm.coll.model.MessageIdRange;

public interface MessageIdManager {
	void updateMesssageIdRange(MessageIdRange range);
	MessageIdRange getMessageIdRange(String agentId,String topic,String partitinNum);

}
