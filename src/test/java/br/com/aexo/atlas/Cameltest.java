package br.com.aexo.atlas;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class Cameltest extends CamelTestSupport {

	@Test
	public void test() {

		context.createProducerTemplate().request("vm:updateAppsMarathon", null);

	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {

				JoinMarathonData joinMarathonData = new JoinMarathonData();

				from("vm:updateAppsMarathon") //
						.setHeader("accept", constant("application/json")) //
						.setHeader("content-type", constant("application/json")) //
						.to("http4://172.19.160.111:8080/v2/apps") //
						.convertBodyTo(String.class) //
						.setHeader("type", constant("apps")) //
						.process(joinMarathonData) //
						.transform(constant(null)) //
						.setHeader("accept", constant("application/json")) //
						.setHeader("content-type", constant("application/json")) //
						.to("http4://172.19.160.111:8080/v2/tasks") //
						.setHeader("type", constant("tasks")) //
						.process(joinMarathonData) //
						.to("language://javascript:classpath:marathonHelperTemplate.js") //
						.to("velocity:default.vm") //
						.to("mock:results");

			}
		};
	}
}
