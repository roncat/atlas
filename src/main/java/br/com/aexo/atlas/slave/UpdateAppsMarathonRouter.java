package br.com.aexo.atlas.slave;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.strategies.RandomStrategy;

public class UpdateAppsMarathonRouter extends RouteBuilder {

	private String marathonUrl;
	private String fileDest;
	private String command;
	private CuratorFramework client;

	public UpdateAppsMarathonRouter(String marathonUrl, String fileDest, String command, CuratorFramework client) {
		this.marathonUrl = marathonUrl;
		this.fileDest = fileDest;
		this.command = command;
		this.client = client;
	}

	@Override
	public void configure() throws Exception {

		ServiceDiscovery<Object> discovery = ServiceDiscoveryBuilder.builder(Object.class).client(client).basePath("/servers").build();
		discovery.start();

		@SuppressWarnings({ "rawtypes", "unchecked" })
		ServiceProvider provider = discovery.serviceProviderBuilder().serviceName("master").providerStrategy(new RandomStrategy()).build();
		provider.start();

		from("seda:updateAppsMarathon") //
				.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {

						// atlas master
						String hostname = provider.getInstance().getAddress();
						Integer port = provider.getInstance().getPort();

						Processor type = new Processor() {

							@Override
							public void process(Exchange exchange) throws Exception {
								exchange.getIn().setHeader("content-type", "application/json");
								exchange.getIn().setHeader("accept", "application/json");
							}
						};

						// marathon apps
						Exchange ex = getContext().createProducerTemplate().request("http4://" + marathonUrl + "/v2/apps", type);

						String apps = ex.getOut().getBody(String.class);

						ex = getContext().createProducerTemplate().request("http4://" + marathonUrl + "/v2/tasks", type);
						String tasks = ex.getOut().getBody(String.class);

						ex = getContext().createProducerTemplate().request("http4://" + hostname + ":" + port + "/acls", type);
						String acls = ex.getOut().getBody(String.class);

						ex = getContext().createProducerTemplate().request("http4://" + hostname + ":" + port + "/template", null);
						String script = ex.getOut().getBody(String.class);

						exchange.getOut().setHeader("acls", acls);
						exchange.getOut().setHeader("tasks", tasks);
						exchange.getOut().setHeader("apps", apps);
						
						exchange.getOut().setBody(script);
					}
				})
				.to("direct:execScript") //
				.to("file:" + fileDest)//
				.transform(constant(null)) //
				.to("exec:" + command);
	}
}
