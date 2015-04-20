package br.com.aexo.atlas;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class JoinMarathonData implements Processor {

	private JoinedMarathonData data = new JoinedMarathonData();

	public void process(Exchange exchange) throws Exception {
		if (exchange.getIn().getHeader("type").equals("apps")) {
			data.setMarathonApps(exchange.getIn().getBody(String.class));
		} else if (exchange.getIn().getHeader("type").equals("tasks")) {
			data.setMarathonTasks(exchange.getIn().getBody(String.class));
		}
		exchange.getOut().setBody(data);
	}
}
