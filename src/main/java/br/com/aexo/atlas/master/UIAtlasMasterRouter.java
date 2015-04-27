package br.com.aexo.atlas.master;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.restlet.RestletConstants;
import org.restlet.Response;

/**
 * routes responsible for providing the UI
 * 
 * @author euprogramador
 *
 */
public class UIAtlasMasterRouter extends RouteBuilder {

	private String hostname;
	private Integer port;

	public UIAtlasMasterRouter(String hostname, Integer port) {
		this.hostname = hostname;
		this.port = port;
	}

	@Override
	public void configure() throws Exception {
		from("restlet:http://" + hostname + ":" + port + "/ui{*}?restletMethod=get").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				Message msg = exchange.getOut();
				String url = exchange.getIn().getHeader("CamelHttpUri").toString();

				if (!url.startsWith("http://" + hostname + ":" + port + "/ui/")) {
					Response response = exchange.getIn().getHeader(RestletConstants.RESTLET_RESPONSE, Response.class);

					response.setLocationRef("http://" + hostname + ":" + port + "/ui/");

					exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, 302);
//					exchange.getOut().setHeader("location", "http://" + hostname + ":" + port + "/ui/");
					return;
				}

				String[] split = url.split(hostname + ":" + port + "/ui/");
				String relativePath = split.length == 2 ? split[1] : "";

				if (relativePath.isEmpty()) {
					relativePath = "index.html";
				}

				String pathStr = "web/" + relativePath;
				MimetypesFileTypeMap map = new MimetypesFileTypeMap();

				String mimeType = map.getContentType(pathStr);

				try {
					if (!new File(getClass().getClassLoader().getResource(pathStr).toURI()).exists()) {
						throw new RuntimeException("not found");
					}

					msg.setBody(getClass().getClassLoader().getResourceAsStream(pathStr));
					msg.setHeader(Exchange.CONTENT_TYPE, mimeType);
					msg.setHeader(Exchange.HTTP_RESPONSE_CODE, "200");
				} catch (Exception e) {
					msg.setBody(relativePath + " not found.");
					msg.setHeader(Exchange.HTTP_RESPONSE_CODE, "404");
				}
			}
		});
	}
}