package br.com.aexo.atlas.master.applications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Applications {

	private CuratorFramework client;
	private ObjectMapper mapper;

	public Applications(CuratorFramework client, ObjectMapper mapper) {
		this.client = client;
		this.mapper = mapper;
	}

	public List<Application> list() {
		try {

			List<String> zkApps = client.getChildren().forPath("/applications");

			List<Application> apps = new ArrayList<>();

			for (String zkApp : zkApps) {
				Application app = mapper.readValue(
						client.getData().forPath(
								String.format("/applications/%s", zkApp)),
						Application.class);

				apps.add(app);
			}
			return Collections.unmodifiableList(apps);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
