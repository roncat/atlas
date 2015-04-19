package br.com.aexo.atlas.marathon;

import java.util.ArrayList;
import java.util.List;

import br.com.aexo.atlas.model.App;

public class Marathon {

	List<App> apps = new ArrayList<App>();

	public List<App> getApps() {
		return apps;
	}

	public void setApps(List<App> apps) {
		this.apps = apps;
	}

	@Override
	public String toString() {
		return "Marathon [apps=" + apps + "]";
	}

}
