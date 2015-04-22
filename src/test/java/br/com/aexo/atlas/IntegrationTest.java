package br.com.aexo.atlas;

import java.io.File;
import java.util.Collection;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
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
		AtlasSlave slave = new AtlasSlave(zk, "172.19.160.111:8080", "localhost", 8080, "target/?fileName=haproxy.cfg", "ls");
		slave.start();

		ServiceDiscovery<Object> discovery = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/servers").build();
		discovery.start();

		Collection<ServiceInstance<Object>> instances = discovery.queryForInstances("slave");

		assertThat(instances.size(), is(1));
		assertThat(instances, hasItem(hasProperty("address", is("localhost"))));
		assertThat(instances, hasItem(hasProperty("port", is(8080))));
		slave.stop();
	}

	@Test
	public void deveriaRegistrarNoZookeeperOMasterNaPortaCorretamente() throws Exception {
		AtlasMaster master = new AtlasMaster(zk, "localhost", 8080);
		master.start();

		ServiceDiscovery<Object> discovery = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/servers").build();
		discovery.start();

		Collection<ServiceInstance<Object>> instances = discovery.queryForInstances("master");

		assertThat(instances.size(), is(1));
		assertThat(instances, hasItem(hasProperty("address", is("localhost"))));
		assertThat(instances, hasItem(hasProperty("port", is(8080))));
		master.stop();
	}

	@Test
	public void deveriaComunicarUmaAtualizacaoDeStatus() throws Exception {
		new File("target/tmp").delete();
		AtlasMaster master = new AtlasMaster(zk, "localhost", 8081);
		master.start();

		String aclsURI = "http://localhost:8081/acls";
		// cadastrando uma nova acl
		String salva = Request.Post(aclsURI).useExpectContinue().version(HttpVersion.HTTP_1_1).bodyString("{\"appId\":\"/sucupirafake\",\"acl\":\"path_beg /usr\"}", ContentType.APPLICATION_JSON)
				.execute().returnContent().asString();

		assertThat(salva, is("OK"));

		AtlasSlave slave1 = new AtlasSlave(zk, "172.19.160.111:8080", "localhost", 8082, "target/tmp/?fileName=haproxy1.cfg", "touch?args=target/tmp/ha1");
		slave1.start();

		AtlasSlave slave2 = new AtlasSlave(zk, "172.19.160.111:8080", "localhost", 8083, "target/tmp/?fileName=haproxy2.cfg", "touch?args=target/tmp/ha2");
		slave2.start();

		// efetua a chamada
		String result = Request.Get("http://localhost:8081/update-notify").connectTimeout(1000).socketTimeout(1000).execute().returnContent().asString();

		assertThat(result, is("OK"));

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

		AtlasMaster master = new AtlasMaster(zk, "localhost", 8081);
		master.start();

		
		String aclsURI = "http://localhost:8081/acls";
		String acls = Request.Get(aclsURI).connectTimeout(1000).socketTimeout(1000).execute().returnContent().asString();

		assertThat(acls, is("[]"));

		// cadastrando uma nova acl
		String salva = Request.Post(aclsURI).useExpectContinue().version(HttpVersion.HTTP_1_1)
				.bodyString("{\"appId\":\"/sucupira/backend/modcluster\",\"acl\":\"teste\"}", ContentType.APPLICATION_JSON).execute().returnContent().asString();

		assertThat(salva, is("OK"));

		// valida cadastramento
		acls = Request.Get(aclsURI).connectTimeout(1000).socketTimeout(1000).execute().returnContent().asString();
		assertThat(acls, is("[{\"appId\":\"/sucupira/backend/modcluster\",\"acl\":\"teste\"}]"));

		// removendo acl cadastrada
		HttpResponse response = Request.Delete("http://localhost:8081/acls/sucupira/backend/modcluster").connectTimeout(1000).socketTimeout(1000).execute().returnResponse();

		assertThat(response.getStatusLine().getStatusCode(), is(200));

		// valida a remoção
		acls = Request.Get(aclsURI).connectTimeout(1000).socketTimeout(1000).execute().returnContent().asString();
		assertThat(acls, is("[]"));
	}
}
