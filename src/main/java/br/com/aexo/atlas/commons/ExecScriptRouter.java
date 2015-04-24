package br.com.aexo.atlas.commons;

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

/**
 * class that contains the responsible route to run in 10 seconds last script.
 * 
 * @author euprogramador
 *
 */
public class ExecScriptRouter extends RouteBuilder {

	private ExecutorService pool = Executors.newCachedThreadPool();

	@Override
	public void configure() throws Exception {

		from("direct:execScript").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {

				String script = exchange.getIn().getBody(String.class);

				StringWriter sw = new StringWriter();
				PrintWriter writer = new PrintWriter(sw);

				ScriptEngineManager factory = new ScriptEngineManager();
				ScriptEngine engine = factory.getEngineByName("nashorn");

				engine.getContext().setAttribute("atlasAcls", exchange.getIn().getHeader("acls"), ScriptContext.ENGINE_SCOPE);
				engine.getContext().setAttribute("marathonTasks", exchange.getIn().getHeader("tasks"), ScriptContext.ENGINE_SCOPE);
				engine.getContext().setAttribute("marathonApps", exchange.getIn().getHeader("apps"), ScriptContext.ENGINE_SCOPE);
				engine.getContext().setAttribute("config", writer, ScriptContext.ENGINE_SCOPE);
				try {
				execWithFuture(engine, script);
				} catch (Exception e){
					e.printStackTrace(writer);
				}
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
