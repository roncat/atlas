package br.com.aexo.atlas.slave;

import java.util.Collection;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.strategies.RandomStrategy;

public class UpdateAppsMarathonRouter extends RouteBuilder {

	private String marathonUrl;
	private String fileDest;
	private String command;
	private CuratorFramework client;

	public UpdateAppsMarathonRouter(String marathonUrl, String fileDest, String command,CuratorFramework client) {
		this.marathonUrl = marathonUrl;
		this.fileDest = fileDest;
		this.command = command;
		this.client = client;
	}

	@Override
	public void configure() throws Exception {
		 
		ServiceDiscovery<Object> discovery = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/servers").build();
		discovery.start();
		ServiceProvider provider = discovery.serviceProviderBuilder().serviceName("master").providerStrategy(new RandomStrategy()).build();
		provider.start();
		
		
		from("seda:updateAppsMarathon") //

				.removeHeaders("CamelHttp*") //
				.transform(constant(null)) //
				.setHeader("accept", constant("application/json")) //
				.setHeader("content-type", constant("application/json")) //
				.to("http4://" + marathonUrl + "/v2/apps")
				.convertBodyTo(String.class) //
				.setHeader("marathonApps") //
				.javaScript("body") //
				
				.removeHeaders("CamelHttp*") //
				.transform(constant(null)) //
				.setHeader("accept", constant("application/json")) //
				.setHeader("content-type", constant("application/json")) //
				.to("http4://" + marathonUrl + "/v2/tasks") //
				.convertBodyTo(String.class) //
				.setHeader("marathonTasks") //
				.javaScript("body") //

				.removeHeaders("CamelHttp*") //
				.transform(constant(null)) //
				.setHeader("accept", constant("application/json")) //
				.setHeader("content-type", constant("application/json")) //
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						String hostname = provider.getInstance().getAddress();
						Integer port = provider.getInstance().getPort();
						
						Exchange ex = getContext().createProducerTemplate().request("http4://" + hostname + ":" + port + "/acls", null);
						exchange.getOut().setBody(ex.getOut().getBody());
						exchange.getOut().setHeader("marathonApps",exchange.getIn().getHeader("marathonApps"));
						exchange.getOut().setHeader("marathonTasks",exchange.getIn().getHeader("marathonTasks"));
					}
				})
				.convertBodyTo(String.class) //
				.setHeader("atlasAcls") //
				.javaScript("body") //
				
				.removeHeaders("CamelHttp*") //
				.transform(constant(null)) //
				
				.to("language://javascript:classpath:marathonHelperTemplate.js") //
				.to("velocity:file:templates/default.vm") //
				.to("file:" + fileDest)//
				.transform(constant(null)) //
				.to("exec:" + command);
	}

}
