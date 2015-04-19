package br.com.aexo.atlas.master;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;

public class AtlasMaster {

	private CamelContext context;
	private ServiceInstance<Object> instance;
	private ServiceDiscovery<Object> service;

	public AtlasMaster(String zk, String hostname, Integer port) throws Exception {

		CuratorFramework client = CuratorFrameworkFactory.builder().namespace("atlas").connectString(zk).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();

		context = new DefaultCamelContext();
		context.addRoutes(new ReceiveUpdateMarathonTasksRouter(client, hostname, port));
		context.addRoutes(new NotifySlavesRouter(client));
		context.addRoutes(new NotifySlaveRouter());

		instance = ServiceInstance.builder().name("master").address(hostname).port(port).build();
		service = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/servers").thisInstance(instance).build();

	}

	public static void main(String[] args) throws Exception {
		String zk = args[0];
		String hostname = args[1];
		Integer port = Integer.parseInt(args[2]);

		new AtlasMaster(zk, hostname, port).start();
	}

	public void start() throws Exception {
		context.start();
		service.start();
	}

	public void stop() throws Exception {
		context.stop();
	}

}
