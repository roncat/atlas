package br.com.aexo.atlas.master;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;

/**
 * Provides atlas master functions of registry acls and notify updates from
 * slaves
 * 
 * @author euprogramador
 *
 */
public class AtlasMaster {

	private CamelContext context;
	private ServiceInstance<Object> instance;
	private ServiceDiscovery<Object> service;

	public AtlasMaster(String zk,String marathonUrl, String hostname, Integer port) throws Exception {

		CuratorFramework client = CuratorFrameworkFactory.builder().namespace("atlas").connectString(zk).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();

		// create path to acls does not exists
		if (client.checkExists().forPath("/acls") == null) {
			client.create().forPath("/acls");
		}
		
		if (client.checkExists().forPath("/template") == null) {
			client.create().forPath("/template","teste".getBytes());
		}

		// create routes camel from master
		context = new DefaultCamelContext();
		context.addRoutes(new ReceiveUpdateMarathonTasksRouter(client, hostname, port));
		context.addRoutes(new NotifySlavesRouter(client));
		context.addRoutes(new NotifySlaveRouter());
		context.addRoutes(new ACLResourceRouter(hostname, port));
		context.addRoutes(new ACLServiceRouter(hostname, port, client));
		context.addRoutes(new TemplateResourceRouter(hostname, port));
		context.addRoutes(new TemplateServiceRouter(client));
		context.addRoutes(new UIAtlasMasterRouter(hostname,port));
		context.addRoutes(new AclRulesReosourceRouter(marathonUrl,hostname,port));
		context.addRoutes(new TestScriptResourceRouter(marathonUrl,hostname,port));

		// registry service in servers for name master for discovery service
		instance = ServiceInstance.builder().name("master").address(hostname).port(port).build();
		service = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/servers").thisInstance(instance).build();

	}

	public static void main(String[] args) throws Exception {
		String zk = args[0];
		String marathonUrl = args[1];
		String hostname = args[2];
		Integer port = Integer.parseInt(args[3]);

		new AtlasMaster(zk, marathonUrl, hostname, port).start();
	}

	/**
	 * start master
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		context.start();
		service.start();
	}

	/**
	 * stop master
	 * 
	 * @throws Exception
	 */
	public void stop() throws Exception {
		context.stop();
	}

}
