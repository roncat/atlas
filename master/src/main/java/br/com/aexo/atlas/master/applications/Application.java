package br.com.aexo.atlas.master.applications;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import br.com.aexo.atlas.master.server.Instance;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Application {

	private String name;
	
	@JsonIgnore
	private Set<Instance> instances = new LinkedHashSet<>();
	private String acl;
	private String group;
	
	
	

	public Application() {
	}

	public Application(String name, String acl, String group) {
		this.name = name;
		this.acl = acl;
		this.group = group;
	}

	public String getAcl() {
		return acl;
	}

	public void setAcl(String acl) {
		this.acl = acl;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}


	public void setInstances(Set<Instance> instances) {
		this.instances = instances;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Instance> getInstances() {
		return Collections.unmodifiableSet(instances);
	}

	public void register(Instance instance) {
		instances.add(instance);
	}
	
	public void clearInstances(){
		instances.removeAll(Collections.EMPTY_SET);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acl == null) ? 0 : acl.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Application other = (Application) obj;
		if (acl == null) {
			if (other.acl != null)
				return false;
		} else if (!acl.equals(other.acl))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
	
}
