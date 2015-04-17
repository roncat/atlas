package br.com.aexo.atlas.slave;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import com.google.common.eventbus.EventBus;

public class ReceiveUpdateMarathonTasksRouter extends RouteBuilder {

	private String hostname;
	private int port;
	private EventBus eventBus;

	public ReceiveUpdateMarathonTasksRouter(EventBus eventBus, String hostname, int port) {
		this.eventBus = eventBus;
		this.hostname = hostname;
		this.port = port;
	}

	@Override
	public void configure() throws Exception {
		from("restlet:http://" + hostname + ":" + port + "/update-notify?restletMethods=get")
		.process(new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception {
				eventBus.post(new UpdateMarathonTasksEvent());
			}
		})
		.transform(constant("ok"));
	}

}
