package br.com.aexo.atlas.master;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class TestScriptResourceRouter extends RouteBuilder {

	private final String hostname;
	private final Integer port;
	private final String marathonUrl;
	private ExecutorService pool = Executors.newCachedThreadPool();

	public TestScriptResourceRouter(String marathonUrl, String hostname, Integer port) {
		this.marathonUrl = marathonUrl;
		this.hostname = hostname;
		this.port = port;
	}

	@Override
	public void configure() throws Exception {

		from("restlet:http://" + hostname + ":" + port + "/test-script?restletMethod=post").process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {

				Processor type = new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						exchange.getIn().setHeader("content-type", "application/json");
						exchange.getIn().setHeader("accept", "application/json");
					}
				};

				// marathon apps
				String apps = getContext().createProducerTemplate().request("http4://" + marathonUrl + "/v2/apps", type).getOut().getBody(String.class);
				String tasks = getContext().createProducerTemplate().request("http4://" + marathonUrl + "/v2/tasks", type).getOut().getBody(String.class);
				String acls = getContext().createProducerTemplate().request("http4://" + hostname + ":" + port + "/acls", type).getOut().getBody(String.class);

				String script = exchange.getIn().getBody(String.class);

				StringWriter sw = new StringWriter();
				PrintWriter writer = new PrintWriter(sw);

				ScriptEngineManager factory = new ScriptEngineManager();
				ScriptEngine engine = factory.getEngineByName("nashorn");
				engine.getContext().setAttribute("atlasAcls", acls, ScriptContext.ENGINE_SCOPE);
				engine.getContext().setAttribute("marathonTasks", tasks, ScriptContext.ENGINE_SCOPE);
				engine.getContext().setAttribute("marathonApps", apps, ScriptContext.ENGINE_SCOPE);
				engine.getContext().setAttribute("config", writer, ScriptContext.ENGINE_SCOPE);
				execWithFuture(engine, script);
				exchange.getOut().setBody(sw.toString());
			}
		});
	}

	private void execWithFuture(final ScriptEngine engine, final String script) throws Exception {
		final Callable<Object> c = new Callable<Object>() {
			public Object call() throws Exception {
				return engine.eval(script);
			}
		};

		final Future<Object> f = pool.submit(c);
		f.get(10, TimeUnit.SECONDS);
		
	}

}
