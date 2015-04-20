package br.com.aexo.atlas.master;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.curator.x.discovery.ServiceInstance;

public class NotifySlaveRouter extends RouteBuilder {


	@Override
	public void configure() throws Exception {
		from("seda:notify-slave").process(new Processor() {
			public void process(Exchange exchange) throws Exception {

				@SuppressWarnings("unchecked")
				ServiceInstance<Object> body = exchange.getIn().getBody(ServiceInstance.class);
				getContext().createProducerTemplate().request("http4://" + body.getAddress() + ":" + body.getPort() + "/update-notify", null);
			}
		});
	}
}
