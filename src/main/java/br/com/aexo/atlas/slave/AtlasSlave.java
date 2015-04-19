package br.com.aexo.atlas.slave;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;

public class AtlasSlave {

	private CamelContext context;
	private CuratorFramework client;
	private ServiceInstance<Object> instance;
	private ServiceDiscovery<Object> service;
	private ReceiveUpdateFromMasterRouter receiveUpdateMarathonTasksRouter;

	public AtlasSlave(String zk, String hostname, Integer port) throws Exception {
		context = new DefaultCamelContext();
		receiveUpdateMarathonTasksRouter = new ReceiveUpdateFromMasterRouter(hostname,port);

		client = CuratorFrameworkFactory.builder().namespace("atlas").connectString(zk).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();
		
		instance = ServiceInstance.builder().name("slave").address(hostname).port(port).build();
		service = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/servers").thisInstance(instance).build();
		
	}
	

	public static void main(String[] args) throws Exception {

		String zk = args[0];
		String hostname = args[1];
		Integer port =Integer.parseInt(args[2]);

		new AtlasSlave(zk, hostname,port).start();
	
	}

	public void start() throws Exception {
		context.addRoutes(receiveUpdateMarathonTasksRouter);
		context.start(); 
		service.start();
	}

	public void stop() throws Exception {
		context.stop();
	}


}
