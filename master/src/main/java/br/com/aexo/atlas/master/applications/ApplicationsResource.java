package br.com.aexo.atlas.master.applications;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/apps")
@Produces(MediaType.APPLICATION_JSON)
public class ApplicationsResource {

	private final Applications applications;
	
	public ApplicationsResource(Applications applications) {
		this.applications = applications;
	}

	@GET
	public List<Application> listApps(){
		return applications.list();
	}
	
}

