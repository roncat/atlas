package br.com.aexo.atlas.master;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * responsible route for exposing an endpoint of the last script execution test
 * parameter as the body of the request
 * 
 * @author euprogramador
 *
 */
public class TestScriptResourceRouter extends RouteBuilder {

	private final String hostname;
	private final Integer port;
	private final String marathonUrl;

	public TestScriptResourceRouter(String marathonUrl, String hostname, Integer port) {
		this.marathonUrl = marathonUrl;
		this.hostname = hostname;
		this.port = port;
	}

	@Override
	public void configure() throws Exception {

		from("restlet:http://" + hostname + ":" + port + "/test-script?restletMethod=post").process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {

				Processor type = new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						exchange.getIn().setHeader("content-type", "application/json");
						exchange.getIn().setHeader("accept", "application/json");
					}
				};

				// marathon apps
				String apps = getContext().createProducerTemplate().request("http4://" + marathonUrl + "/v2/apps", type).getOut().getBody(String.class);
				String tasks = getContext().createProducerTemplate().request("http4://" + marathonUrl + "/v2/tasks", type).getOut().getBody(String.class);
				String acls = getContext().createProducerTemplate().request("http4://" + hostname + ":" + port + "/acls", type).getOut().getBody(String.class);

				exchange.getOut().setHeader("acls", acls);
				exchange.getOut().setHeader("apps", apps);
				exchange.getOut().setHeader("tasks", tasks);

				exchange.getOut().setBody(exchange.getIn().getBody());
			}
		}).to("direct:execScript");
	}

}
