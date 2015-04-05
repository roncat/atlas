package br.com.aexo.atlas;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import br.com.aexo.atlas.master.AtlasMasterConfiguration;

public class AtlasMasterConfigurationTest {
	
	private AtlasMasterConfiguration config;
	private TestingServer zk;

	@Before
	public void setup() throws Exception {
		zk = new TestingServer();
		config = new AtlasMasterConfiguration();
	}
	
	@After
	public void tearDown() throws Exception{
		zk.stop();
	}
	
	@Test
	public void deveriaConfigurarAAplicacaoNoZookeeperAoObterUmClient() throws Exception {
		config.setZookeeper(zk.getConnectString());
		config.setPath("atlas");
		
		CuratorFramework client = config.getCuratorFramework();
		
		assertThat(client.checkExists().forPath("/applications"), is(notNullValue()));
		assertThat(client.checkExists().forPath("/groups"), is(notNullValue()));
	}
	
}
