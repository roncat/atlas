package br.com.aexo.atlas;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class FakeMarathon extends RouteBuilder {

	private final String hostname;
	private final Integer port;

	public FakeMarathon(String hostname, Integer port) {
		super();
		this.hostname = hostname;
		this.port = port;
	}

	public void start() throws Exception{
		CamelContext context = new DefaultCamelContext();
		context.addRoutes(this);
		context.start();
	}
	
	@Override
	public void configure() throws Exception {
		from("restlet:http://" + hostname + ":" + port + "/v2/apps").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.getOut().setHeader("accept", "application/json");
				exchange.getOut().setHeader("content-type", "application/json");
				exchange.getOut().setBody(getClass().getClassLoader().getResourceAsStream("apps.json"));
			}
		});

		from("restlet:http://" + hostname + ":" + port + "/v2/tasks").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.getOut().setHeader("accept", "application/json");
				exchange.getOut().setHeader("content-type", "application/json");
				exchange.getOut().setBody(getClass().getClassLoader().getResourceAsStream("tasks.json"));
			}
		});
	}

}
