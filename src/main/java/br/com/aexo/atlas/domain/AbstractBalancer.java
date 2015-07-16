package br.com.aexo.atlas.domain;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import br.com.aexo.atlas.infraestructure.util.HttpCall;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractBalancer implements Balancer {

	private static ExecutorService pool = Executors.newCachedThreadPool();

	private final String marathonUrl;

	public AbstractBalancer(){
		this("");
	}
	
	public AbstractBalancer(String marathonUrl) {
		this.marathonUrl = marathonUrl;
	}

	@Override
	public abstract String getActualConfiguration();

	@Override
	public abstract void reload();

	@Override
	public abstract void writeConfiguration(String script, List<Acl> acls);

	@Override
	public String testConfiguration(String script,List<Acl> acls) {

		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);

		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("nashorn");

		ObjectMapper mapper = new ObjectMapper();
		String aclsJson = "";
		try {
			aclsJson = mapper.writeValueAsString(acls);
		} catch (JsonProcessingException e1) {
			throw new RuntimeException(e1);
		}
		
		engine.getContext().setAttribute("atlasAcls", aclsJson, ScriptContext.ENGINE_SCOPE);
		engine.getContext().setAttribute("marathonTasks", HttpCall.getJSON(marathonUrl + "v2/tasks"), ScriptContext.ENGINE_SCOPE);
		engine.getContext().setAttribute("marathonApps", HttpCall.getJSON(marathonUrl + "v2/apps"), ScriptContext.ENGINE_SCOPE);
		engine.getContext().setAttribute("config", writer, ScriptContext.ENGINE_SCOPE);
		try {
			execWithFuture(engine, script);
		} catch (Exception e) {
			e.printStackTrace(writer);
		}
		return sw.toString();
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
