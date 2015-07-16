package br.com.aexo.atlas.web.rest;

import java.io.FileNotFoundException;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.script.ScriptException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import br.com.aexo.atlas.application.AtlasService;
import br.com.aexo.atlas.domain.Rule;
import br.com.aexo.atlas.infraestructure.rest.Result;
import br.com.aexo.atlas.infraestructure.rest.Results;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api("rules")
@RequestScoped
@Path("/rules")
public class RuleResource {
	@Inject
	private Result result;

	@Inject
	private AtlasService service;

	@GET
	@ApiOperation("retrive rules")
	public Response getRule() throws FileNotFoundException, ScriptException, NoSuchMethodException {
		List<Rule> rules = service.getRules();
		return result.use(Results.representation()).from(rules).serialize();
	}

}
