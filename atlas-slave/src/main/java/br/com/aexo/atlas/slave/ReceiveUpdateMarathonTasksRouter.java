package br.com.aexo.atlas.slave;

import org.apache.camel.builder.RouteBuilder;

public class ReceiveUpdateMarathonTasksRouter extends RouteBuilder {

	private String hostname;
	private int port;

	public ReceiveUpdateMarathonTasksRouter(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	@Override
	public void configure() throws Exception {
		from("restlet:http://" + hostname + ":" + port + "/update-notify?restletMethods=get").transform(constant("ok")).to("mock:results");
	}

}
