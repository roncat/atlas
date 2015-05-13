package br.com.aexo.atlas;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.aexo.atlas.master.AtlasMaster;
import br.com.aexo.atlas.slave.AtlasSlave;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

public class ForceUpdateNotifyTest {

	private String zk;
	private CuratorFramework client;
	private FakeMarathon marathon;
	private TestingServer testingServer;

	@Before
	public void setup() throws Exception {
		testingServer = new TestingServer();
		zk = testingServer.getConnectString();
		client = CuratorFrameworkFactory.builder().namespace("atlas").connectString(zk).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();

		marathon = new FakeMarathon("localhost", 19000);
		marathon.start();
	}

	@After
	public void tearDown() throws Exception {
		marathon.stop();
		client.close();
		testingServer.stop();
	}

	@Test
	public void shouldBeUpdateSlavesToAMasterNoLeaderWithForceUpdateFlagSetted() throws Exception {
		FileUtils.deleteDirectory(new File("target/tmp"));
		AtlasMaster master1 = new AtlasMaster(zk, "atlas", "localhost:19000", "localhost", 18081, "http://localhost:18081/update-notify");
		master1.start();

		AtlasMaster master2 = new AtlasMaster(zk, "atlas", "localhost:19000", "localhost", 18082, "http://localhost:18082/update-notify");
		master2.start();

		AtlasSlave slave1 = new AtlasSlave(zk, "atlas", "localhost:19000", "localhost", 18091, "target/tmp/?fileName=haproxy1.cfg", "touch?args=target/tmp/ha1");
		slave1.start();

		String callbackNotLeader = "";
		if (!master1.isLeader()) {
			callbackNotLeader = master1.getCallback();
		}
		if (!master2.isLeader()) {
			callbackNotLeader = master2.getCallback();
		}

		// efetua a chamada
		String result = Request.Post(callbackNotLeader+"?forceUpdate=true").bodyString("", ContentType.APPLICATION_JSON).connectTimeout(1000).socketTimeout(1000).execute().returnContent().asString();

		assertThat(result, is("ok"));

		Thread.sleep(3000);
		// valida se escreveu o arquivo de config 1 no slave 1
		assertThat(new File("target/tmp/haproxy1.cfg").exists(), is(true));
		// valida se rodou o comando no slave 1
		assertThat(new File("target/tmp/ha1").exists(), is(true));

		slave1.stop();
		master1.stop();
		master2.stop();
	}

}
