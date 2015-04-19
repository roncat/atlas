package br.com.aexo.atlas.marathon;

import java.util.ArrayList;
import java.util.List;

public class MarathonTask {

	private String appId;
	private String id;
	private String host;
	private List<Integer> ports;
	private List<Integer> servicePorts;
	private List<HealthCheck> healthCheckResults = new ArrayList<HealthCheck>();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appId == null) ? 0 : appId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MarathonTask other = (MarathonTask) obj;
		if (appId == null) {
			if (other.appId != null)
				return false;
		} else if (!appId.equals(other.appId))
			return false;
		return true;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public List<Integer> getPorts() {
		return ports;
	}

	public void setPorts(List<Integer> ports) {
		this.ports = ports;
	}

	public List<Integer> getServicePorts() {
		return servicePorts;
	}

	public void setServicePorts(List<Integer> servicePorts) {
		this.servicePorts = servicePorts;
	}

	public List<HealthCheck> getHealthCheckResults() {
		return healthCheckResults;
	}

	public void setHealthCheckResults(List<HealthCheck> healthCheckResults) {
		this.healthCheckResults = healthCheckResults;
	}

	@Override
	public String toString() {
		return "Task [appId=" + appId + ", id=" + id + ", host=" + host
				+ ", ports=" + ports + ", servicePorts=" + servicePorts
				+ ", healthCheckResults=" + healthCheckResults + "]";
	}

	public boolean isAlive() {
		for (HealthCheck hc : healthCheckResults) {
			if (!hc.isAlive()) {
				return false;
			}
		}
		return true;
	}

}
