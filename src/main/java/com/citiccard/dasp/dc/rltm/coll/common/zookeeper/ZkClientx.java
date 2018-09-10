package com.citiccard.dasp.dc.rltm.coll.common.zookeeper;

import java.util.Map;

import org.I0Itec.zkclient.IZkConnection;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.CreateMode;

import com.alibaba.otter.canal.common.zookeeper.ZooKeeperx;
import com.google.common.base.Function;
import com.google.common.collect.MigrateMap;

public class ZkClientx extends ZkClient {
	//对于zkclient进行一次缓存，皮面一个jvm内部使用多个zk connection
	private static Map<String,ZkClientx> clients=MigrateMap.makeComputingMap(new Function<String, ZkClientx>() {
	   
		public ZkClientx apply(String servers){
			return new ZkClientx(servers);
		}
	
	});
	
	public static ZkClientx getZkClient(String servers){
		return clients.get(servers);
	}
	
	public ZkClientx(String serverstring){
		this(serverstring,Integer.MAX_VALUE);
	}
	
	public ZkClientx(String zkServers,int connectionTimeout){
		this(new ZooKeeperx(zkServers),connectionTimeout);
	}
	
	public ZkClientx(String zkServers,int sessionTimeout,int connectionTimeout){
		this(new ZooKeeperx(zkServers,sessionTimeout),connectionTimeout);
	}
	
	public ZkClientx(String zkServers,int sessionTimeout,int connectionTimeout,ZkSerializer zkSerializer){
		this(new ZooKeeperx(zkServers,sessionTimeout),connectionTimeout,zkSerializer);
	}
	
	private ZkClientx(IZkConnection connection,int connectionTimeout){
		this(connection,connectionTimeout,new ByteSerializer());
	}
	
	private ZkClientx(IZkConnection zkconnection,int connectionTimeout,ZkSerializer zkSerializer){
		super(zkconnection,connectionTimeout,zkSerializer);
	}
	
	public String createPersistentSequential(String path,boolean createParents)
		throws ZkInterruptedException,IllegalArgumentException,ZkException,RuntimeException{
		try{
			return create(path,null,CreateMode.PERSISTENT_SEQUENTIAL);
		}catch(ZkNoNodeException e){
			if(!createParents){
				throw e;
			}
			String parentDir=path.substring(0,path.lastIndexOf('/'));
			createPersistent(parentDir,createParents);
			return createPersistentSequential(path, createParents);
		}
	}
	
	public String createPersistentSequential(String path,Object data,boolean createParents)
			throws ZkInterruptedException,IllegalArgumentException,ZkException,RuntimeException{
			try{
				return create(path,data,CreateMode.PERSISTENT_SEQUENTIAL);
			}catch(ZkNoNodeException e){
				if(!createParents){
					throw e;
				}
				String parentDir=path.substring(0,path.lastIndexOf('/'));
				createPersistent(parentDir,createParents);
				return createPersistentSequential(path,data, createParents);
			}
		}
	
	public void createPersistent(String path,Object data,boolean createParents)
	 throws ZkInterruptedException,IllegalAccessException,ZkException,RuntimeException{
		try{
			create(path,data,CreateMode.PERSISTENT);
		}catch(ZkNodeExistsException e){
			if(!createParents){
				throw e;
			}
		}catch(ZkNoNodeException e){
			if(!createParents){
				throw e;
			}
			String parentDir=path.substring(0,path.lastIndexOf('/'));
			createPersistent(parentDir,createParents);
			createPersistent(parentDir,data,createParents);
		}
		
	}

}
