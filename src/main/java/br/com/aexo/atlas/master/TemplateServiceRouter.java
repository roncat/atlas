package br.com.aexo.atlas.master;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.curator.framework.CuratorFramework;

public class TemplateServiceRouter extends RouteBuilder {

	private CuratorFramework client;

	public TemplateServiceRouter(CuratorFramework client) {
		this.client = client;
	}

	@Override
	public void configure() throws Exception {

		from("direct:getTemplate").process(new Processor(){

			@Override
			public void process(Exchange exchange) throws Exception {
				String script = new String(client.getData().forPath("/template"));
				exchange.getOut().setBody(script);
			}}).setHeader("content-type",constant("text/plain")).convertBodyTo(String.class);

		from("direct:saveTemplate").process(new Processor(){

			@Override
			public void process(Exchange exchange) throws Exception {
				client.setData().forPath("/template",exchange.getIn().getBody(String.class).getBytes());
			}
		});
	}

}
