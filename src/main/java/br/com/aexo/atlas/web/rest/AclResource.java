package br.com.aexo.atlas.web.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import br.com.aexo.atlas.application.AtlasService;
import br.com.aexo.atlas.domain.Acl;
import br.com.aexo.atlas.infraestructure.rest.Result;
import br.com.aexo.atlas.infraestructure.rest.Results;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RequestScoped
@Api("Acls")
@Path("acls")
public class AclResource {

	@Inject
	private AtlasService service;

	@Inject
	private Result result;

	@GET
	@ApiOperation(value = "list acls", notes = "list acls note")
	public Response list() {
		List<Acl> acls = service.listAcls();
		return result.use(Results.representation()).from(acls).serialize();
	}

	@POST
	@ApiOperation(value = "save acl", notes = "save acl")
	public Response save(Acl acl) {
		service.saveAcl(acl);
		return result.use(Results.representation()).from(acl).serialize();
	}

	@DELETE
	@Path("/{appId:.*}")
	@ApiOperation(value = "remove acl", notes = "remove acl")
	public Response delete(@PathParam("appId")String appId) {
		service.removeAcl("/"+appId);
		return result.use(Results.representation()).from("").serialize();
	}

}
