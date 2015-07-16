package br.com.aexo.atlas.web.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.aexo.atlas.application.AtlasService;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RequestScoped
@Api("template")
@Path("/")
public class TemplateResource {

	@Inject
	private AtlasService service;

	@GET
	@Path("template")
	@ApiOperation(value = "get template")
	public Response getTemplate() {
		String template = service.getTemplate();
		return Response.ok(template).header("content-type", "text/plain").build();
	}

	@POST
	@Path("template")
	@ApiOperation(value = "save template", consumes = "text/plain")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response saveTemplate(String template) {
		service.saveTemplate(template);
		return Response.ok().build();
	}

	@POST
	@Path("template")
	public Response updateNotify() {
		service.updateNotify();
		return Response.ok("ok").build();
	}

	@POST
	@Path("test-script")
	public Response testScript(String script) {
		String result = service.testScript(script);
		return Response.ok(result).header("content-type", "text/plain").build();
	}

}
