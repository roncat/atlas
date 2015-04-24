package br.com.aexo.atlas.master;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class AclRulesReosourceRouter extends RouteBuilder {

	private String marathonUrl;
	private String hostname;
	private Integer port;

	public AclRulesReosourceRouter(String marathonUrl,String hostname, Integer port) {
		this.marathonUrl = marathonUrl;
		this.hostname = hostname;
		this.port = port;
	}

	@Override
	public void configure() throws Exception {

		
		
		from("restlet:http://" + hostname + ":" + port + "/rules{*}?restletMethod=get").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				InputStream script = getClass().getClassLoader().getResourceAsStream("rules.js");
				
				Processor type = new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						exchange.getIn().setHeader("content-type", "application/json");
						exchange.getIn().setHeader("accept", "application/json");
					}
				};

				String acls = getContext().createProducerTemplate().request("direct:listAcls", null).getIn().getBody(String.class);
				String apps = getContext().createProducerTemplate().request("http4://" + marathonUrl + "/v2/apps", type).getOut().getBody(String.class);
				String tasks = getContext().createProducerTemplate().request("http4://" + marathonUrl + "/v2/tasks", type).getOut().getBody(String.class);
				
				StringWriter sw = new StringWriter();
				PrintWriter writer = new PrintWriter(sw);

				ScriptEngineManager factory = new ScriptEngineManager();
		        ScriptEngine engine = factory.getEngineByName("nashorn");
		        engine.getContext().setAttribute("acls", acls, ScriptContext.ENGINE_SCOPE);
		        engine.getContext().setAttribute("tasks", tasks, ScriptContext.ENGINE_SCOPE);
		        engine.getContext().setAttribute("apps", apps, ScriptContext.ENGINE_SCOPE);
		        engine.getContext().setAttribute("saida", writer, ScriptContext.ENGINE_SCOPE);
				engine.eval(new InputStreamReader(script));
				
				exchange.getOut().setHeader("content-type","application/json");
				exchange.getOut().setBody(sw.toString());
			}
		});

	}
}
