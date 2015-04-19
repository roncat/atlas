package br.com.aexo.atlas.slave;

import org.apache.camel.builder.RouteBuilder;

public class UpdateAppsMarathonRouter extends RouteBuilder {

	private String marathonUrl;

	public UpdateAppsMarathonRouter(String marathonUrl) {
		this.marathonUrl = marathonUrl;
	}

	@Override
	public void configure() throws Exception {

		from("vm:updateAppsMarathon").to("http4://" + marathonUrl + "/v2/apps").marshal().json().to("mock:results");

	}

}
