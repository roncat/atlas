package br.com.aexo.atlas.domain;

import java.io.InputStreamReader;
import java.util.List;

import javax.inject.Inject;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import br.com.aexo.atlas.web.rest.RuleResource;

public class RuleRepository {

	@Inject
	private AtlasConfiguration config;

	@SuppressWarnings("unchecked")
	public List<Rule> getRules() {
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("nashorn");

			engine.eval(new InputStreamReader(RuleResource.class.getResourceAsStream("/rules.js")));
			Object resultado = ((Invocable) engine).invokeFunction("processRules", config.getMarathonURL(), this);
			return (List<Rule>) resultado;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
