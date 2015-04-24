package br.com.aexo.atlas.commons;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class ExecScriptRouterTest extends CamelTestSupport {

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new ExecScriptRouter();
	}

	@Test
	public void shouldBeExecuteScript() {
		Exchange ex = context.createProducerTemplate().request("direct:execScript", new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.getIn().setHeader("acls", "");
				exchange.getIn().setHeader("tasks", "");
				exchange.getIn().setHeader("apps", "");
				exchange.getIn().setBody("config.print('teste');");
			}
		});

		assertThat(ex.getOut().getBody(), is("teste"));
	}

	@Test
	public void shouldBeExecuteScriptIn10Seconds() {
		Exchange ex =	context.createProducerTemplate().request("direct:execScript", new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.getIn().setHeader("acls", "");
				exchange.getIn().setHeader("tasks", "");
				exchange.getIn().setHeader("apps", "");
				exchange.getIn().setBody("while(true){}");
			}
		});
		
		assertThat(ex.getOut().getBody(String.class), containsString("java.util.concurrent.TimeoutException"));
	}

}
