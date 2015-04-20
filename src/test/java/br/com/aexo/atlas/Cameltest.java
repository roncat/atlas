package br.com.aexo.atlas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
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

				//
				// JacksonDataFormat tasks = new JacksonDataFormat(Tasks.class);
				// tasks.disableFeature(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				//
				// Processor processor = new Processor() {
				//
				// @Override
				// public void process(Exchange exchange) throws Exception {
				//
				// if (exchange.getIn().getBody() instanceof Tasks){
				// Topology topology = new Topology();
				// Tasks marathonTasks = exchange.getIn().getBody(Tasks.class);
				//
				// for (Task mtask : marathonTasks.getTasks()){
				// for (Integer x=0 ; x< mtask.getServicePorts().size() ; x++){
				// Integer servicePort = mtask.getServicePorts().get(x);
				// Integer port = mtask.getPorts().get(x);
				// String host = mtask.getHost();
				// boolean alive = mtask.isAlive();
				//
				// Application app =
				// topology.getApp(servicePort,mtask.getAppId());
				// app.setAcl("path_beg /");
				// app.add(new Instance(host,port,alive));
				// }
				// }
				// } else if (exchange.getIn().getBody() instanceof Apps) {
				//
				// }
				//
				// exchange.getOut().setBody(topology);
				//
				// }
				// };

				class Data {
					private Map<String, Object> marathonApps;
					private Map<String, Object> marathonTasks;
					private Map<String, Object> topology;

					public Map<String, Object> getMarathonApps() {
						return marathonApps;
					}

					public void setMarathonApps(Map<String, Object> marathonApps) {
						this.marathonApps = marathonApps;
					}

					public Map<String, Object> getMarathonTasks() {
						return marathonTasks;
					}

					public void setMarathonTasks(
							Map<String, Object> marathonTasks) {
						this.marathonTasks = marathonTasks;
					}

					public Map<String, Object> getTopology() {
						return topology;
					}

					public void setTopology(Map<String, Object> topology) {
						this.topology = topology;
					}

				}
				
				

				Processor joinAppAndTasks = new Processor() {
					
					Data data = new Data();
					
					@Override
					public void process(Exchange exchange) throws Exception {
						if (exchange.getIn().getHeader("type").equals("apps")) {
							data.setMarathonApps(exchange.getIn().getBody(Map.class));
						} else if (exchange.getIn().getHeader("type")
								.equals("tasks")) {
							data.setMarathonTasks(exchange.getIn().getBody(Map.class));
						}
						exchange.getOut().setBody(data);
					}
				};

				from("vm:updateAppsMarathon")
						.setHeader("accept",constant("application/json"))
						.setHeader("content-type",constant("application/json"))
						.to("http4://172.19.160.111:8080/v2/apps").unmarshal()
						.json(JsonLibrary.Jackson)
						.setHeader("type", constant("apps"))
						.process(joinAppAndTasks)
						.transform(constant(null))
						.setHeader("accept",constant("application/json"))
						.setHeader("content-type",constant("application/json"))
						.to("http4://172.19.160.111:8080/v2/tasks").unmarshal()
						.json(JsonLibrary.Jackson)
						.setHeader("type", constant("tasks"))
						.process(joinAppAndTasks)
						.to("language://javascript:classpath:teste.js")
						.to("velocity:teste.vm")
						.to("mock:results");

			}
		};
	}
}
