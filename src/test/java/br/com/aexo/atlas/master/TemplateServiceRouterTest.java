package br.com.aexo.atlas.master;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class TemplateServiceRouterTest extends CamelTestSupport {

	private String zk;
	private CuratorFramework client;

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {

		@SuppressWarnings("resource")
		TestingServer testingServer = new TestingServer();
		zk = testingServer.getConnectString();
		client = CuratorFrameworkFactory.builder().namespace("atlas").connectString(zk).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();

		client.create().forPath("/template", new byte[] {});

		return new TemplateServiceRouter(client);
	}

	@Test
	public void shouldBeGetTemplate() throws Exception {
		String script = "teste";
		client.setData().forPath("/template", script.getBytes());
		Exchange request = context.createProducerTemplate().request("direct:getTemplate", null);
		String scriptRetornado = request.getOut().getBody(String.class);
		assertThat(scriptRetornado, is(script));
	}

	@Test
	public void shouldBeSaveTemplate() throws Exception {
		String script = "teste";
		context.createProducerTemplate().sendBody("direct:saveTemplate", script);
		String scriptSalvo = new String(client.getData().forPath("/template"));
		assertThat(scriptSalvo, is(script));
	}

	@Test
	public void shouldBeSaveLargeTemplate() throws Exception {
		StringBuffer largeScript = new StringBuffer();

		for (Integer x = 0; x < 1024*1000; x++) {
			largeScript.append('q');
		}

		
		context.createProducerTemplate().sendBody("direct:saveTemplate", largeScript.toString());
		String scriptSalvo = new String(client.getData().forPath("/template"));
		assertThat(scriptSalvo, is(largeScript.toString()));
	}

}
