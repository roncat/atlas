package br.com.aexo.atlas.slave;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;

public class AtlasSlave {

	public static void main(String[] args) throws Exception {

		String zk = args[0];
		String hostname = args[1];
		Integer port =Integer.parseInt(args[2]);

		new AtlasSlave().start(zk, hostname,port);
	
	}

	public void start(String zk,String hostname,int port) throws Exception {
		
		CuratorFramework client = CuratorFrameworkFactory.builder().namespace("atlas").connectString(zk).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();

		ServiceInstance<Object> instance = ServiceInstance.builder().name("slave").address(hostname).port(port).build();
		ServiceDiscovery<Object> service = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/servers").thisInstance(instance).build();
		service.start();
	}

}
