package br.com.aexo.atlas.slave;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;

import br.com.aexo.atlas.commons.ExecScriptRouter;

public class AtlasSlave {

	private CamelContext context;
	private CuratorFramework client;
	private ServiceDiscovery<Object> service;

	public AtlasSlave(String zk, String namespace, String marathonUrl, String hostname, Integer port, String fileDest, String command) throws Exception {
		
		client = CuratorFrameworkFactory.builder().namespace(namespace).connectString(zk).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();
		
		context = new DefaultCamelContext();
		context.addRoutes(new ReceiveUpdateFromMasterRouter(hostname, port));
		context.addRoutes(new UpdateAppsMarathonRouter(marathonUrl, fileDest, command,client));
		context.addRoutes(new ExecScriptRouter());

		service = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/servers").thisInstance(ServiceInstance.builder().name("slave").address(hostname).port(port).build()).build();
	}

	public static void main(String[] args) throws Exception {

		String zk = System.getenv("ZK");
		String namespace = System.getenv("NAMESPACE");
		String marathonUrl = System.getenv("MARATHON_URL");
		String hostname = System.getenv("HOSTNAME");
		Integer port = Integer.getInteger(System.getenv("PORT"));
		
		String fileDest = System.getenv("CONF_BALANCER_DEST");
		String command = System.getenv("COMMAND");
		new AtlasSlave(zk, namespace, marathonUrl, hostname, port, fileDest, command).start();

	}

	public void start() throws Exception {

		context.start();
		service.start();
	}

	public void stop() throws Exception {
		context.stop();
	}

}
