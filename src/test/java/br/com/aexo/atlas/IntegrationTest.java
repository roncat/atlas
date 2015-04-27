package br.com.aexo.atlas;

import java.io.File;

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

public class IntegrationTest {

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
	public void tearDown() throws Exception{
		marathon.stop();
		client.close();
		testingServer.stop();
	}
	

	@Test
	public void deveriaComunicarUmaAtualizacaoDeStatus() throws Exception {
		new File("target/tmp").delete();
		AtlasMaster master = new AtlasMaster(zk, "localhost:19000","localhost", 18081,"http://localhost:18081/update-notify");
		master.start();

		String aclsURI = "http://localhost:18081/acls";
		// cadastrando uma nova acl
		String acl = "{\"appId\":\"/sucupirafake\",\"acl\":\"path_beg /usr\"}";
		String salva = Request.Post(aclsURI).useExpectContinue().version(HttpVersion.HTTP_1_1).bodyString(acl, ContentType.APPLICATION_JSON)
				.execute().returnContent().asString();

		assertThat(salva, is(acl));

		AtlasSlave slave1 = new AtlasSlave(zk, "localhost:19000", "localhost", 18082, "target/tmp/?fileName=haproxy1.cfg", "touch?args=target/tmp/ha1");
		slave1.start();

		AtlasSlave slave2 = new AtlasSlave(zk, "localhost:19000", "localhost", 18083, "target/tmp/?fileName=haproxy2.cfg", "touch?args=target/tmp/ha2");
		slave2.start();

		// efetua a chamada
		String result = Request.Post("http://localhost:18081/update-notify").bodyString("", ContentType.APPLICATION_JSON).connectTimeout(1000).socketTimeout(1000).execute().returnContent().asString();

		assertThat(result, is("ok"));

		Thread.sleep(3000);
		// valida se escreveu o arquivo de config 1 no slave 1
		assertThat(new File("target/tmp/haproxy1.cfg").exists(), is(true));
		// valida se rodou o comando no slave 1
		assertThat(new File("target/tmp/ha1").exists(), is(true));

		// valida se escreveu o arquivo de confg 2 no slave 2
		assertThat(new File("target/tmp/haproxy2.cfg").exists(), is(true));
		// valida se rodou o comando no slave 2
		assertThat(new File("target/tmp/ha2").exists(), is(true));

		slave1.stop();
		slave2.stop();
		master.stop();
	}

	@Test
	public void deveriaGerenciarAsAclsViaApi() throws Exception {

		AtlasMaster master = new AtlasMaster(zk,"localhost:19000", "localhost", 18081,"http://localhost:18081/update-notify");
		master.start();

		
		String aclsURI = "http://localhost:18081/acls";
		String acls = Request.Get(aclsURI).connectTimeout(1000).socketTimeout(1000).execute().returnContent().asString();

		assertThat(acls, is("[]"));

		// cadastrando uma nova acl
		String acl = "{\"appId\":\"/sucupira/backend/modcluster\",\"acl\":\"teste\"}";
		String salva = Request.Post(aclsURI).useExpectContinue().version(HttpVersion.HTTP_1_1)
				.bodyString(acl, ContentType.APPLICATION_JSON).execute().returnContent().asString();

		assertThat(salva, is(acl));

		// valida cadastramento
		acls = Request.Get(aclsURI).connectTimeout(1000).socketTimeout(1000).execute().returnContent().asString();
		assertThat(acls, is("[{\"appId\":\"/sucupira/backend/modcluster\",\"acl\":\"teste\"}]"));

		// removendo acl cadastrada
		HttpResponse response = Request.Delete("http://localhost:18081/acls/sucupira/backend/modcluster").connectTimeout(1000).socketTimeout(1000).execute().returnResponse();

		assertThat(response.getStatusLine().getStatusCode(), is(200));

		// valida a remoção
		acls = Request.Get(aclsURI).connectTimeout(1000).socketTimeout(1000).execute().returnContent().asString();
		assertThat(acls, is("[]"));
	}
}
