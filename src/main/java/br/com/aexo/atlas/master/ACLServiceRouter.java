package br.com.aexo.atlas.master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.curator.framework.CuratorFramework;

/**
 * routes provides rest acl management
 * 
 * @author euprogramador
 *
 */
public class ACLServiceRouter extends RouteBuilder {

	private CuratorFramework client;
	private String host;
	private Integer port;

	public ACLServiceRouter(String host, Integer port, CuratorFramework client) {
		this.host = host;
		this.port = port;
		this.client = client;
	}

	@Override
	public void configure() throws Exception {

		// camel rest endpoints manage acls ha-proxy
		rest("/acls/{*}")

		.delete().to("direct:deleteAcl")

		.get().to("direct:listAcls")

		.post().to("direct:saveAcl");

		// route list acls from zookeeper
		from("direct:listAcls").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				List<Map<String, String>> acls = new ArrayList<>();

				List<String> list = client.getChildren().forPath("/acls");
				for (String appId : list) {
					HashMap<String, String> app = new HashMap<String, String>();

					app.put("appId", java.net.URLDecoder.decode(appId, "ISO-8859-1"));
					app.put("acl", new String(client.getData().forPath("/acls/" + appId)));

					acls.add(app);
				}

				exchange.getOut().setBody(acls);
			}
		}).marshal().json(JsonLibrary.Jackson).setHeader("content-type").constant("application/json").to("mock:result");

		// route save acls from zookeeper
		from("direct:saveAcl")
				.convertBodyTo(String.class)
				.setHeader("content")
				.body()
				.transform()
				.constant(client)
				.transform()
				.javaScript(
						"var app = JSON.parse(headers.content);var client = body; app.appId = '/acls/'+encodeURIComponent(app.appId); if ( client.checkExists().forPath(app.appId)==null) { client.create().forPath(app.appId,app.acl.getBytes()); } else { client.setData().forPath(app.appId,app.acl.getBytes()); }")
				.transform().constant("OK");

		// route delete acls from zookeeper
		from("direct:deleteAcl").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				String url = exchange.getIn().getHeader("CamelHttpUri").toString();
				exchange.getOut().setHeader("content", url.split(host + ":" + port + "/acls")[1]);
			}
		}).transform().constant(client).transform()
				.javaScript("var client = body; appId = '/acls/'+encodeURIComponent(headers.content); if ( client.checkExists().forPath(appId)!=null) { client.delete().forPath(appId); }").transform()
				.constant(null);
	}
}
