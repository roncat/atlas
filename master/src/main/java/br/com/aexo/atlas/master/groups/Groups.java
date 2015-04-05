package br.com.aexo.atlas.master.groups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;

public class Groups {

	private CuratorFramework client;

	public Groups(CuratorFramework client) {
		this.client = client;
	}

	public List<Group> list() {
		try {

			List<String> zkGroups = client.getChildren().forPath("/groups");

			List<Group> groups = new ArrayList<>();

			for (String zkGroup : zkGroups) {
				Group group = new Group(zkGroup);
				groups.add(group);
			}
			return Collections.unmodifiableList(groups);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
