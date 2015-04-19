package br.com.aexo.atlas.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class App {

	private String id;

	private Map<Integer, List<Instance>> instances = new HashMap<>();

	public App(String appId) {
		id = appId;
	}

	public Set<Integer> ports() {
		return instances.keySet();
	}

	public List<Instance> instances(Integer port) {
		return instances.get(port);
	}

	public void add(Integer servicePort, Instance instance){
		getInstances(servicePort).add(instance);
	}
	
	private List<Instance> getInstances(Integer servicePort) {
		
		if (!instances.containsKey(servicePort)) {
			instances.put(servicePort, new ArrayList<>());
		}
		
		return instances.get(servicePort);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
