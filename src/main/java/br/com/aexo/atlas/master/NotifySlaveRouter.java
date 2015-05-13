package br.com.aexo.atlas.master;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.curator.x.discovery.ServiceInstance;

/**
 * route provides notification one slave via http
 * 
 * @author euprogramador
 *
 */
public class NotifySlaveRouter extends RouteBuilder {
	
	private LeaderElection leader;

	public NotifySlaveRouter(LeaderElection leader){
		this.leader = leader;
	}

	@Override
	public void configure() throws Exception {
		from("seda:notify-slave").process(new Processor() {
			public void process(Exchange exchange) throws Exception {

				if (!leader.isLeader() && !exchange.getIn().getHeader("forceUpdate",Boolean.class)){
					return;
				}
				
				@SuppressWarnings("unchecked")
				ServiceInstance<Object> body = exchange.getIn().getBody(ServiceInstance.class);
				getContext().createProducerTemplate().request("http4://" + body.getAddress() + ":" + body.getPort() + "/update-notify", null);
			}
		});
	}
}
