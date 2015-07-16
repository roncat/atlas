package br.com.aexo.atlas.web;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import br.com.aexo.atlas.application.AtlasService;

@WebListener
public class RegisterAtlasInMarathon implements ServletContextListener {

	@Inject
	private AtlasService atlasService;

	public void contextInitialized(ServletContextEvent event) {
		atlasService.registryInMarathonEventsSubscriber();
	}

	public void contextDestroyed(ServletContextEvent event) {
		atlasService.deRegistryInMarathonEventsSubscriber();
	}
}
