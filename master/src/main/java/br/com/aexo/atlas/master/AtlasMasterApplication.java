package br.com.aexo.atlas.master;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.apache.curator.framework.CuratorFramework;

import br.com.aexo.atlas.master.applications.Applications;
import br.com.aexo.atlas.master.applications.ApplicationsResource;
import br.com.aexo.atlas.master.groups.Groups;
import br.com.aexo.atlas.master.groups.GroupsResource;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AtlasMasterApplication extends
		Application<AtlasMasterConfiguration> {

	public static void main(String[] args) throws Exception {
		new AtlasMasterApplication().run(args);
	}

	@Override
	public void initialize(Bootstrap<AtlasMasterConfiguration> bootstrap) {
		super.initialize(bootstrap);
	}

	@Override
	public void run(AtlasMasterConfiguration configuration,
			Environment environment) throws Exception {

		CuratorFramework client = configuration.getCuratorFramework();
		
		ObjectMapper mapper = new ObjectMapper();

		Applications applications = new Applications(client,mapper);
		Groups groups  = new Groups(client);
		
		environment.jersey().register(new ApplicationsResource(applications));
		environment.jersey().register(new GroupsResource(groups));
	}

}