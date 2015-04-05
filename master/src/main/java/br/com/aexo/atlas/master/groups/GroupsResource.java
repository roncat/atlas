package br.com.aexo.atlas.master.groups;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
public class GroupsResource {

	private final Groups groups;

	public GroupsResource(Groups groups) {
		this.groups = groups;
	}

	@GET
	public List<Group> listgroups() {
		return groups.list();
	}

}
