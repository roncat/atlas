package br.com.aexo.atlas.infraestructure.rest;

import javax.ws.rs.core.Response;


public class StatusResultView implements ResultView<StatusResultView>{

	public Response notFound(){
		return Response.status(404).build();
	}

	public Response ok() {
		return Response.status(200).build();
	}
	
	public Response conflict(Object error){
		return Response.status(409).entity(error).build();
	}

	public Response precondictionFailed(Object errors) {
		return Response.status(412).entity(errors).build();
	}
	
}
