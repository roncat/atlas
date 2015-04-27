package br.com.aexo.atlas.master;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;

/**
 * route provides centralized endpoint for notification updates in marathon
 * 
 * @author euprogramador
 *
 */
public class ReceiveUpdateMarathonTasksRouter extends RouteBuilder {

	private int port;
	private String hostname;

	public ReceiveUpdateMarathonTasksRouter( String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	@Override
	public void configure() throws Exception {
		from("restlet:http://" + hostname + ":" + port + "/update-notify?restletMethods=post").to(ExchangePattern.InOnly, "seda:notify-slaves").transform(constant("ok"));

	}
}
