package br.com.aexo.atlas.domain;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

public class BalancerProducer {

	@Inject
	private AtlasConfiguration config;

	@ApplicationScoped
	@Produces
	public Balancer balancerProvider() {
		return new HaProxy(config.getConfigFileName(), config.getCommand(), config.getMarathonURL());
	}

}
