package br.com.aexo.atlas;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.http.HttpHeaders;
import org.junit.Test;

import br.com.aexo.atlas.marathon.MarathonTask;
import br.com.aexo.atlas.marathon.MarathonTasks;
import br.com.aexo.atlas.model.App;
import br.com.aexo.atlas.model.AtlasApps;
import br.com.aexo.atlas.model.Instance;

import com.fasterxml.jackson.databind.DeserializationFeature;

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

//				JacksonDataFormat apps = new JacksonDataFormat(Marathon.class);
//				apps.disableFeature(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				
//				from("vm:updateAppsMarathon").to("http4://172.19.170.210:8080/v2/apps").unmarshal(apps).to("mock:results");
			
				JacksonDataFormat tasks = new JacksonDataFormat(MarathonTasks.class);
				tasks.disableFeature(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				
				from("vm:updateAppsMarathon")
				.setHeader(HttpHeaders.ACCEPT,constant("application/json"))
				.setHeader(Exchange.CONTENT_TYPE,constant("application/json"))
				.to("http4://172.19.160.111:8080/v2/tasks")
				.unmarshal(tasks)
				.process(new Processor() {
					
					@Override
					public void process(Exchange exchange) throws Exception {

						AtlasApps apps = new AtlasApps();
						MarathonTasks marathonTasks = exchange.getIn().getBody(MarathonTasks.class);
						
						for (MarathonTask mtask : marathonTasks.getTasks()){
							App app = apps.getApp(mtask.getAppId());
							
							for (Integer x=0 ; x< mtask.getServicePorts().size() ; x++){
								
								Integer servicePort = mtask.getServicePorts().get(x);
								Integer port = mtask.getPorts().get(x);
								String host = mtask.getHost(); 
								boolean alive = mtask.isAlive();
								app.add(servicePort,new Instance(host,port,alive));
							}
						}
						
						exchange.getOut().setBody(apps);
						
					}
				})
				.to("velocity:default.vm")
				.to("mock:results");
			
				
			}
		};
	}
}
