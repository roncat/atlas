package br.com.aexo.atlas;

import java.net.HttpURLConnection;
import java.net.URL;
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
import org.mockito.Mockito;

import br.com.aexo.atlas.master.AtlasMaster;
import br.com.aexo.atlas.slave.AtlasSlave;
import br.com.aexo.atlas.slave.UpdateMarathonTasksEvent;

import com.google.common.eventbus.Subscribe;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

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
		AtlasSlave slave = new AtlasSlave(zk, "localhost",8080);
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
		
		class EventsHandler {
			@Subscribe
			public void updateMarathonTasksHandler(UpdateMarathonTasksEvent event){
			}
		}
		
		AtlasMaster master = new AtlasMaster(zk, "localhost", 8081);
		master.start();

		EventsHandler handler = spy(new EventsHandler());
		
		AtlasSlave slave1 = new AtlasSlave(zk, "localhost",8082);
		slave1.getEventBus().register(handler);
		
		slave1.start();
		
		AtlasSlave slave2 = new AtlasSlave(zk, "localhost",8083);
		slave2.getEventBus().register(handler);
		slave2.start();
	
		HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:8081/update-notify").openConnection();
		int responseCode = con.getResponseCode();

		
		assertThat(responseCode,is(200));
		verify(handler,Mockito.atLeast(2)).updateMarathonTasksHandler(Mockito.any(UpdateMarathonTasksEvent.class));
		
		slave1.stop();
		slave2.stop();
		master.stop();
	}
}
