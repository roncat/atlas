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
	private ServiceDiscovery<Object> service;

	public AtlasSlave(String zk, String marathonUrl, String hostname, Integer port, String fileDest, String command) throws Exception {
		context = new DefaultCamelContext();
		context.addRoutes(new ReceiveUpdateFromMasterRouter(hostname, port));
		context.addRoutes(new UpdateAppsMarathonRouter(marathonUrl, fileDest, command));

		client = CuratorFrameworkFactory.builder().namespace("atlas").connectString(zk).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();

		service = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/servers").thisInstance(ServiceInstance.builder().name("slave").address(hostname).port(port).build()).build();
	}

	public static void main(String[] args) throws Exception {

		String zk = args[0];
		String marathonUrl = args[1];
		String hostname = args[2];
		Integer port = Integer.parseInt(args[3]);

		String fileDest = args[4];
		String command = args[5];
		new AtlasSlave(zk, marathonUrl, hostname, port, fileDest, command).start();

	}

	public void start() throws Exception {

		context.start();
		service.start();
	}

	public void stop() throws Exception {
		context.stop();
	}

}
