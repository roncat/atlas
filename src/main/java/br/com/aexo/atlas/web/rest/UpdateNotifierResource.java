package br.com.aexo.atlas.web.rest;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;

import br.com.aexo.atlas.application.AtlasService;
import br.com.aexo.atlas.domain.AtlasConfiguration;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api("UpdateNotifier")
@Path("/update-notify")
public class UpdateNotifierResource {

	@Inject
	private AtlasService service;

	@Inject
	private AtlasConfiguration config;

	@POST
	@ApiOperation("receive update configuration notifier")
	public Response receive() {
		service.updateNotify();
		return Response.ok().build();
	}

	@GET
	public Response getServers() {
		try {
			Response.ok(
					Request.Get(config.getMarathonURL() //
					.concat("/v2/eventSubscriptions")) //
					.version(HttpVersion.HTTP_1_1) //
					.execute().returnContent().asStream()
					).build();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}

