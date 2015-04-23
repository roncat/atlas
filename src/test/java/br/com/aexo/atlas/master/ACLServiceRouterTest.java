package br.com.aexo.atlas.master;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class ACLServiceRouterTest extends CamelTestSupport {

	private String zk;
	private CuratorFramework client;
	private String url;

	protected RouteBuilder createRouteBuilder() throws Exception {

		@SuppressWarnings("resource")
		TestingServer testingServer = new TestingServer();
		zk = testingServer.getConnectString();
		client = CuratorFrameworkFactory.builder().namespace("atlas").connectString(zk).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();

		client.create().forPath("/acls");

		String hostname = "localhost";
		Integer port = 31280;
		url = "http://" + hostname + ":" + port + "/acls";

		return new ACLServiceRouter(hostname, port, client);
	}

	@Test
	public void shouldBeListACLResources() throws Exception {
		Exchange message = context.createProducerTemplate().request("direct:listAcls",null);
		
		assertThat(message.getIn().getBody(String.class),is("[]"));
	}
	
	@Test
	public void shouldBeSaveACLResource() throws Exception {
		Exchange message = context.createProducerTemplate().request("direct:saveAcl", new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.getOut().setBody("{\"appId\":\"/app/app-1\",\"acl\":\"path_beg teste\"}");
				exchange.getOut().setHeader("content-type", "application/json; charset=UTF-8");
			}
		});
		assertThat(message.getIn().getBody(),is("OK"));
	}

	@Test
	public void shouldBeRemoveACLResource() throws Exception {
		Exchange message = context.createProducerTemplate().request("direct:deleteAcl", new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.getOut().setHeader(Exchange.HTTP_URI, url+"/teste");
			}
		});
		assertThat(message.getOut().getBody(),is(nullValue()));
	}

}
