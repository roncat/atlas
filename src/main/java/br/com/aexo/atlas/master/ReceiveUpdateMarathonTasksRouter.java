package br.com.aexo.atlas.master;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;

public class ReceiveUpdateMarathonTasksRouter extends RouteBuilder {

	private int port;
	private String hostname;
	private CuratorFramework client;

	public ReceiveUpdateMarathonTasksRouter(CuratorFramework client, String hostname, int port) {
		this.client = client;
		this.hostname = hostname;
		this.port = port;
	}

	@Override
	public void configure() throws Exception {

		ServiceDiscovery<Object> discovery = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/servers").build();
		discovery.start();

		from("restlet:http://" + hostname + ":" + port + "/update-notify?restletMethods=get")
		.to(ExchangePattern.InOnly, "seda:notify-slaves")
		.transform(constant("ok"));

	}
}
