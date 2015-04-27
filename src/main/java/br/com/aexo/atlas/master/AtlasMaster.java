package br.com.aexo.atlas.master;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import br.com.aexo.atlas.commons.ExecScriptRouter;

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
	private LeaderElection leader;
	private String marathonUrl;
	private String hostname;
	private Integer port;
	private ExecutorService pool = Executors.newCachedThreadPool();
	private String callback;
			

	public AtlasMaster(String zk,String marathonUrl, String hostname, Integer port, String callback) throws Exception {

		this.marathonUrl = marathonUrl;
		this.hostname = hostname;
		this.port = port;
		this.callback = callback;
		
		CuratorFramework client = CuratorFrameworkFactory.builder().namespace("atlas").connectString(zk).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();

		// create path to acls does not exists
		if (client.checkExists().forPath("/acls") == null) {
			client.create().forPath("/acls");
		}
		
		if (client.checkExists().forPath("/template") == null) {
			Path path = Paths.get(getClass().getClassLoader().getResource("scriptdefault.cfg").toURI());
			client.create().forPath("/template",Files.readAllBytes(path));
		}
		
		leader = new LeaderElection(client);

		// create routes camel from master
		context = new DefaultCamelContext();
		context.addRoutes(new ReceiveUpdateMarathonTasksRouter( hostname, port));
		context.addRoutes(new NotifySlavesRouter(client));
		context.addRoutes(new NotifySlaveRouter(leader));
		context.addRoutes(new ACLResourceRouter(hostname, port));
		context.addRoutes(new ACLServiceRouter(hostname, port, client));
		context.addRoutes(new TemplateResourceRouter(hostname, port));
		context.addRoutes(new TemplateServiceRouter(client));
		context.addRoutes(new UIAtlasMasterRouter(hostname,port));
		context.addRoutes(new ACLRulesResourceRouter(marathonUrl,hostname,port));
		context.addRoutes(new TestScriptResourceRouter(marathonUrl,hostname,port));
		context.addRoutes(new ExecScriptRouter());

		// registry service in servers for name master for discovery service
		instance = ServiceInstance.builder().name("master").address(hostname).port(port).build();
		service = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/servers").thisInstance(instance).build();
		
	}

	public static void main(String[] args) throws Exception {
		String zk = args[0];
		String marathonUrl = args[1];
		String hostname = args[2];
		Integer port = Integer.parseInt(args[3]);
		String callback = args[4];
		
		new AtlasMaster(zk, marathonUrl, hostname, port,callback).start();
	}

	/**
	 * start master
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {
		context.start();
		service.start();
		
		
		// registry callback in marathon
		Request.Post("http://"
				.concat(marathonUrl)
				.concat("/v2/eventSubscriptions?callbackUrl=")
				.concat(callback)
		).version(HttpVersion.HTTP_1_1).bodyString("", ContentType.APPLICATION_JSON).execute();
		
		
		pool.submit(new Runnable() {
			@Override
			public void run() {
				try {
					leader.start();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
				
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
