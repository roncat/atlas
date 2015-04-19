package br.com.aexo.atlas.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtlasApps {

	private Map<String, App> apps = new HashMap<>();

	public App getApp(String appId) {

		if (!apps.containsKey(appId)) {
			apps.put(appId, new App(appId));
		}

		return apps.get(appId);
	}

	public List<App> getApps() {
		return new ArrayList<App>(apps.values());
	}


}
