package br.com.aexo.atlas.master;

import java.util.Collection;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;

public class ReceiveUpdateMarathonTasksRouter extends RouteBuilder {

	private int port;
	private String hostname;
	private CuratorFramework client;

    public ReceiveUpdateMarathonTasksRouter(CuratorFramework client, String hostname,int port) {
		this.client = client;
		this.hostname = hostname;
		this.port = port;
	}
	
	@Override
	public void configure() throws Exception {
		
		ServiceDiscovery<Object> discovery = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/servers").build();
		discovery.start();

		
		from("restlet:http://"+hostname+":"+port+"/update-notify?restletMethods=get").process(new Processor() {
			public void process(Exchange exchange) throws Exception {
				getContext().createProducerTemplate().request("vm:notify-slaves", null);
			}
		}).transform(constant("ok"));

		
		
		
		from("vm:notify-slaves").process(new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception {
				Collection<ServiceInstance<Object>> instances = discovery.queryForInstances("slave");
				exchange.getOut().setBody(instances);
			}
		}).split().body().parallelProcessing().to("vm:notify-slave");

		
		from("vm:notify-slave").process(new Processor(){

			@Override
			public void process(Exchange exchange) throws Exception {
				
				ServiceInstance<Object> body = exchange.getIn().getBody(ServiceInstance.class);

				getContext().createProducerTemplate().request("http4://"+body.getAddress()+":"+body.getPort()+"/update-notify", null);
			
			}});
		
		
	}
}
