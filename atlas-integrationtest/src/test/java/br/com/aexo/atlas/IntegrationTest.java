package br.com.aexo.atlas;

import java.util.Collection;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
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
		AtlasSlave slave = new AtlasSlave();
		slave.start(zk, "localhost",3001);

		ServiceDiscovery<Object> discovery = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/servers").build();
		discovery.start();

		Collection<ServiceInstance<Object>> instances = discovery.queryForInstances("slave");

		assertThat(instances.size(), is(1));
		assertThat(instances, hasItem(hasProperty("address",is("localhost"))));
		assertThat(instances, hasItem(hasProperty("port",is(3001))));
	}
	
	

	@Test
	public void deveriaRegistrarNoZookeeperOMasterNaPortaCorretamente() throws Exception {
		AtlasMaster slave = new AtlasMaster();
		slave.start(zk, "localhost",3001);

		ServiceDiscovery<Object> discovery = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/servers").build();
		discovery.start();

		Collection<ServiceInstance<Object>> instances = discovery.queryForInstances("master");

		assertThat(instances.size(), is(1));
		assertThat(instances, hasItem(hasProperty("address",is("localhost"))));
		assertThat(instances, hasItem(hasProperty("port",is(3001))));
	}
	
	@Test
	public void deveriaComunicar() throws Exception {
		AtlasMaster master = new AtlasMaster();
		master.start(zk, "localhost", 1000);
		
		AtlasSlave slave1 = new AtlasSlave();
		slave1.start(zk, "localhost",3001);
		
		AtlasSlave slave2 = new AtlasSlave();
		slave2.start(zk, "localhost",3002);

		
		
	}
}
