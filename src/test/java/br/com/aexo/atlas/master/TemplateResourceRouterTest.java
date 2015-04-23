package br.com.aexo.atlas.master;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.Test;

public class TemplateResourceRouterTest extends CamelTestSupport {

	private String host;
	private Integer port;
	private String url;

	protected RouteBuilder[] createRouteBuilders() throws Exception {
		host = "localhost";
		port = 32342;

		url = "http://" + host + ":" + port + "/template";
		return new RouteBuilder[] { new TemplateResourceRouter(host, port), new RouteBuilder() {
			@Override
			public void configure() throws Exception {
			
				from("direct:getTemplate").to("mock:getTemplate");
				from("direct:saveTemplate").to("mock:saveTemplate");
			
			}

		} };
	}

	@EndpointInject(uri = "mock:getTemplate")
	private MockEndpoint getTemplate;

	@EndpointInject(uri = "mock:saveTemplate")
	private MockEndpoint saveTemplate;

	@Test
	public void shouldBeHandlerRequestForGetTemplate() throws Exception {
		getTemplate.expectedMinimumMessageCount(1);
		Request.Get(url).execute().returnContent().asString();
		getTemplate.assertIsSatisfied();
	}
 
	@Test
	public void shoulBeHandlerRequestForSaveTemplate() throws Exception {
		saveTemplate.expectedMinimumMessageCount(1);
		try {
			Request.Post(url).version(HttpVersion.HTTP_1_1).useExpectContinue().bodyString("teste", ContentType.APPLICATION_ATOM_XML).execute().discardContent();
		}catch(Exception e){
		e.printStackTrace();
		}
		saveTemplate.assertIsSatisfied();
	}

}
