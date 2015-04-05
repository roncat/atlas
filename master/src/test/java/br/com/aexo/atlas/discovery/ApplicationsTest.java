package br.com.aexo.atlas.discovery;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.aexo.atlas.master.applications.Application;
import br.com.aexo.atlas.master.applications.Applications;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

public class ApplicationsTest {
	
	private TestingServer zk;
	private CuratorFramework client;
	private Applications applications;
	private ObjectMapper mapper;

	@Before
	public void setup() throws Exception {
		zk = new TestingServer();
		client = CuratorFrameworkFactory.builder().retryPolicy(new ExponentialBackoffRetry(1000, 3)).connectString(zk.getConnectString()).build();
		client.start();

		client.create().forPath("/applications");
		
		mapper = new ObjectMapper();

		applications =  new Applications(client,mapper);
		
	}

	@After
	public void tearDown() throws Exception {
		zk.stop();
	}
	
	@Test
	public void deveriaRecuperarAsAplicacoesDoZookeeper() throws Exception {


		Application appIn = new Application("app","acl","group");
		client.create().forPath("/applications/app",mapper.writeValueAsBytes(appIn));

		List<Application> apps = applications.list();

		assertThat(apps.size(),is(1));
		assertThat(apps, hasItem(appIn));
	}
	
	
	

}
