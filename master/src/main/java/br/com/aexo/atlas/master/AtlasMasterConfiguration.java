package br.com.aexo.atlas.master;

import io.dropwizard.Configuration;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AtlasMasterConfiguration extends Configuration {

	@NotEmpty
	private String zookeeper;

	@NotEmpty
	private String path;

	@JsonProperty
	public String getZookeeper() {
		return zookeeper;
	}

	@JsonProperty
	public void setZookeeper(String zookeeper) {
		this.zookeeper = zookeeper;
	}

	@JsonProperty
	public String getPath() {
		return path;
	}

	@JsonProperty
	public void setPath(String path) {
		this.path = path;
	}

	public CuratorFramework getCuratorFramework() {
		try {
			CuratorFramework client = CuratorFrameworkFactory.builder()
					.retryPolicy(new ExponentialBackoffRetry(1000, 3))
					.connectString(zookeeper).namespace(path).build();
			client.start();

			if (client.checkExists().forPath("/applications") == null) {
				client.create().forPath("/applications");
			}

			return client;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
