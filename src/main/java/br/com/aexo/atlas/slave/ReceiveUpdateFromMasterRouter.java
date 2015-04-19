package br.com.aexo.atlas.slave;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;

public class ReceiveUpdateFromMasterRouter extends RouteBuilder {

	private String hostname;
	private int port;

	public ReceiveUpdateFromMasterRouter( String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	@Override
	public void configure() throws Exception {
		from("restlet:http://" + hostname + ":" + port + "/update-notify?restletMethods=get")
		.to(ExchangePattern.InOnly, "vm:updateAppsMarathon")
		.transform(constant("ok"));
	}

}
