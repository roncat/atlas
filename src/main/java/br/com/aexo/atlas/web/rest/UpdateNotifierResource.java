package br.com.aexo.atlas.web.rest;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import br.com.aexo.atlas.application.AtlasService;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api("UpdateNotifier")
@Path("/update-notify")
public class UpdateNotifierResource {

	@Inject
	private AtlasService service;
	
	@POST
	@ApiOperation("receive update configuration notifier")
	public Response receive(){
		service.updateNotify();
		return Response.ok().build();
	}
	
}
