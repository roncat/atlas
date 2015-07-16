package br.com.aexo.atlas.infraestructure;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import br.com.aexo.atlas.domain.AtlasConfiguration;

/**
 * utility class for production of objects in the context CDI
 * 
 * @author carlosr
 *
 */
public class Resources {

	@Inject
	private AtlasConfiguration config;
	
	@Produces
	@ApplicationScoped
	public CuratorFramework providesCuratorFramework() {
		// TODO implementar para vir de fora como variaveis de ambiente
		String namespace = config.getNamespace();
		String zk = config.getZookepperURL();
		CuratorFramework client = CuratorFrameworkFactory.builder().namespace(namespace).connectString(zk).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();
		try {
			if (client.checkExists().forPath("/acls") == null) {
				client.create().forPath("/acls");
			}

			if (client.checkExists().forPath("/template") == null) {
				client.create().forPath("/template", IOUtils.toByteArray(getClass().getResourceAsStream("/scriptdefault.cfg")));
			}
		} catch (Exception e) {
			//TODO melhorar implementação do logging
			throw new RuntimeException(e);
		}

		return client;
	}

}