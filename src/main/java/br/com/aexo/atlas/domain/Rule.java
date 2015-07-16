package br.com.aexo.atlas.domain;

public class Rule {

	private String appId;
	private String acl;
	private String instances;
	private String discovery;

	public Rule(String appId, String acl, String instances, String discovery) {
		super();
		this.appId = appId;
		this.acl = acl;
		this.instances = instances;
		this.discovery = discovery;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAcl() {
		return acl;
	}

	public void setAcl(String acl) {
		this.acl = acl;
	}

	public String getInstances() {
		return instances;
	}

	public void setInstances(String instances) {
		this.instances = instances;
	}

	public String getDiscovery() {
		return discovery;
	}

	public void setDiscovery(String discovery) {
		this.discovery = discovery;
	}
}
