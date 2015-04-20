package br.com.aexo.atlas;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collection;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.hamcrest.text.pattern.internal.naming.Path;
import org.junit.Before;
import org.junit.Test;

import br.com.aexo.atlas.master.AtlasMaster;
import br.com.aexo.atlas.slave.AtlasSlave;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

public class IntegrationTest {

	private String zk;
	private CuratorFramework client;

	@Before
	public void setup() throws Exception {
		@SuppressWarnings("resource")
		TestingServer testingServer = new TestingServer();
		zk = testingServer.getConnectString();
		client = CuratorFrameworkFactory.builder().namespace("atlas").connectString(zk).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();
	}

	@Test
	public void deveriaRegistrarNoZookeeperOSlaveNaPortaCorretamente() throws Exception {
		AtlasSlave slave = new AtlasSlave(zk,"172.19.160.111:8080", "localhost",8080,"target/?fileName=haproxy.cfg","ls");
		slave.start();

		ServiceDiscovery<Object> discovery = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/servers").build();
		discovery.start();

		Collection<ServiceInstance<Object>> instances = discovery.queryForInstances("slave");

		assertThat(instances.size(), is(1));
		assertThat(instances, hasItem(hasProperty("address",is("localhost"))));
		assertThat(instances, hasItem(hasProperty("port",is(8080))));
		slave.stop();
	}
	
	

	@Test
	public void deveriaRegistrarNoZookeeperOMasterNaPortaCorretamente() throws Exception {
		AtlasMaster master = new AtlasMaster(zk, "localhost",8080);
		master.start();

		ServiceDiscovery<Object> discovery = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/servers").build();
		discovery.start();

		Collection<ServiceInstance<Object>> instances = discovery.queryForInstances("master");

		assertThat(instances.size(), is(1));
		assertThat(instances, hasItem(hasProperty("address",is("localhost"))));
		assertThat(instances, hasItem(hasProperty("port",is(8080))));
		master.stop();
	}
	
	@Test
	public void deveriaComunicar() throws Exception {
		new File("target/tmp").delete();
		AtlasMaster master = new AtlasMaster(zk, "localhost", 8081);
		master.start();

		AtlasSlave slave1 = new AtlasSlave(zk,"172.19.160.111:8080", "localhost",8082,"target/tmp/?fileName=haproxy1.cfg","touch?args=target/ha1");
		slave1.start();
		
		AtlasSlave slave2 = new AtlasSlave(zk,"172.19.160.111:8080", "localhost",8083,"target/tmp/?fileName=haproxy2.cfg","touch?args=target/ha2");
		slave2.start();
	
		HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:8081/update-notify").openConnection();
		int responseCode = con.getResponseCode();

		
		assertThat(responseCode,is(200));

		
		
		slave1.stop();
		slave2.stop();
		master.stop();
	}
}
