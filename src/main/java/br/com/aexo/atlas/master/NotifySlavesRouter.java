package br.com.aexo.atlas.master;

import java.util.Collection;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;

public class NotifySlavesRouter extends RouteBuilder {

	private CuratorFramework client;

	public NotifySlavesRouter(CuratorFramework client) {
		this.client = client;
	}

	@Override
	public void configure() throws Exception {

		ServiceDiscovery<Object> discovery = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/servers").build();
		discovery.start();

		from("seda:notify-slaves").process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				Collection<ServiceInstance<Object>> instances = discovery.queryForInstances("slave");
				exchange.getOut().setBody(instances);
			}
		}).split()
		.body().to("seda:notify-slave");
	}
}
