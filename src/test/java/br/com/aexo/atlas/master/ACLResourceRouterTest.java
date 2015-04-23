package br.com.aexo.atlas.master;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.Test;

public class ACLResourceRouterTest extends CamelTestSupport {

	private String host;
	private Integer port;
	private String url;

	@Override
	protected RouteBuilder[] createRouteBuilders() throws Exception {
		host = "localhost";
		port = 32456;
		url = "http://" + host + ":" + port + "/acls";
		return new RouteBuilder[] { new ACLResourceRouter(host, port), new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				from("direct:listAcls").to("mock:listAcls");
				from("direct:saveAcl").to("mock:saveAcls");
				from("direct:deleteAcl").to("mock:deleteAcls");
			}
		} };
	}

	@EndpointInject(uri = "mock:listAcls")
	protected MockEndpoint listAcls;

	@EndpointInject(uri = "mock:saveAcls")
	protected MockEndpoint saveAcls;

	@EndpointInject(uri = "mock:deleteAcls")
	protected MockEndpoint deleteAcls;

	@Test
	public void shoudBeInvokeListAclsHandlerToRequestListAcls() throws Exception {
		listAcls.expectedMinimumMessageCount(1);

		Request.Get(url).connectTimeout(1000).socketTimeout(1000).execute().returnContent().asString();

		listAcls.assertIsSatisfied();
	}

	@Test
	public void shoudBeInvokesaveAclsHandlerToRequestSaveAcls() throws Exception {
		saveAcls.expectedMinimumMessageCount(1);
		saveAcls.expectedBodiesReceived("mybody");
		saveAcls.expectedHeaderReceived("content-type", "application/json; charset=UTF-8");

		Request.Post(url).version(HttpVersion.HTTP_1_1).bodyString("mybody", ContentType.APPLICATION_JSON).execute();

		saveAcls.assertIsSatisfied();
	}

	@Test
	public void shoudBeInvokedeleteAclsHandlerToRequestDeleteAcls() throws Exception {
		deleteAcls.expectedMinimumMessageCount(1);

		Request.Delete(url).version(HttpVersion.HTTP_1_1).execute();

		deleteAcls.assertIsSatisfied();
	}

}
