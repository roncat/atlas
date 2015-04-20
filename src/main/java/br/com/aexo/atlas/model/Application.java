package br.com.aexo.atlas.model;

import java.util.ArrayList;
import java.util.List;

public class Application {

	private String appId;
	private String acl;
	private List<Instance> instances = new ArrayList<>();

	public String safeId(){
		return appId.replace("/", "-");
	}
	
	public Application(String appId) {
		this.appId = appId;
	}

	public void add(Instance instance) {
		instances.add(instance);
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public List<Instance> getInstances() {
		return instances;
	}

	public void setInstances(List<Instance> instances) {
		this.instances = instances;
	}

	public String getAcl() {
		return acl;
	}

	public void setAcl(String acl) {
		this.acl = acl;
	}

}
