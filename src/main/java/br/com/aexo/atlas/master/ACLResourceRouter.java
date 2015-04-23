package br.com.aexo.atlas.master;

import org.apache.camel.builder.RouteBuilder;

/**
 * provides rest router services manage from acl
 * 
 * @author euprogramador
 *
 */
public class ACLResourceRouter extends RouteBuilder {

	private String host;
	private Integer port;

	public ACLResourceRouter(String host, Integer port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public void configure() throws Exception {
		// rest endpoints
		from("restlet:http://" + host + ":" + port + "/acls(*)?restletMethod=get").to("direct:listAcls");
		from("restlet:http://" + host + ":" + port + "/acls(*)?restletMethod=post").to("direct:saveAcl");
		from("restlet:http://" + host + ":" + port + "/acls(*)?restletMethod=delete").to("direct:deleteAcl");
	}
}