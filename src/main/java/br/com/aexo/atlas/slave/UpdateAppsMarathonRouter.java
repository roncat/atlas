package br.com.aexo.atlas.slave;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class UpdateAppsMarathonRouter extends RouteBuilder {

	private String marathonUrl;
	private String fileDest;
	private String command;

	public UpdateAppsMarathonRouter(String marathonUrl, String fileDest, String command) {
		this.marathonUrl = marathonUrl;
		this.fileDest = fileDest;
		this.command = command;
	}

	@Override
	public void configure() throws Exception {

		from("seda:updateAppsMarathon") //
				.removeHeaders("CamelHttp*") //
				.transform(constant(null)) //
				.setHeader("accept", constant("application/json")) //
				.setHeader("content-type", constant("application/json")) //
				.to("http4://" + marathonUrl + "/v2/apps")

				.convertBodyTo(String.class) //

				.setHeader("marathonApps") //
				.javaScript("body") //
				.removeHeaders("CamelHttp*") //
				.transform(constant(null)) //
				.setHeader("accept", constant("application/json")) //
				.setHeader("content-type", constant("application/json")) //
				.to("http4://" + marathonUrl + "/v2/tasks") //

				.convertBodyTo(String.class) //

				.setHeader("marathonTasks") //
				.javaScript("body") //
				.removeHeaders("CamelHttp*") //
				.transform(constant(null)) //
				.to("language://javascript:classpath:marathonHelperTemplate.js") //
				.to("velocity:file:templates/default.vm") //
				.to("file:" + fileDest)//
				.transform(constant(null)) //
				.to("exec:" + command);
	}

}
