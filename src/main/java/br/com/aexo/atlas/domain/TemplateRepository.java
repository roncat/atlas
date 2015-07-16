package br.com.aexo.atlas.domain;

import javax.inject.Inject;

import org.apache.curator.framework.CuratorFramework;

public class TemplateRepository {

	@Inject
	private CuratorFramework client;

	public String getTemplate() {
		try {
			return new String(client.getData().forPath("/template"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void saveTemplate(String template) {
		try {
			client.setData().forPath("/template", template.getBytes());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
