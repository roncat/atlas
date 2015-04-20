package br.com.aexo.atlas.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Topology {

	private Map<Integer, Map<String,Application>> topology = new HashMap<>();


	public Set<Integer> getPorts() {
		return topology.keySet();
	}
	
	public Application getApp(Integer servicePort, String appId) {
		
		if (!topology.containsKey(servicePort)) {
			topology.put(servicePort,new HashMap<String,Application>());
		}

		Map<String,Application> apps = topology.get(servicePort);
		
		if (!apps.containsKey(appId)) {
			apps.put(appId,new Application(appId));
		}
		
		return apps.get(appId);
	}
	
	public Collection<Application> apps(Integer port){
		return topology.get(port).values();
	}
	

}
