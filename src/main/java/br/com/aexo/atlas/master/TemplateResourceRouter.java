package br.com.aexo.atlas.master;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

public class TemplateResourceRouter extends RouteBuilder {

	private String host;
	private Integer port;

	public TemplateResourceRouter(String host, Integer port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public void configure() throws Exception {
		from("restlet:http://" + host + ":" + port + "/template?restletMethods=get,post")
		.choice()
		.when(header(Exchange.HTTP_METHOD).isEqualTo("GET")).to("direct:getTemplate")
		.when(header(Exchange.HTTP_METHOD).isEqualTo("POST")).to("direct:saveTemplate")
		.otherwise().to("mock:discard")
		.endChoice();
	}

}
