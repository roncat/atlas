package br.com.aexo.atlas.master.groups;

import java.util.LinkedHashSet;
import java.util.Set;

import br.com.aexo.atlas.master.server.Instance;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Group {

	private String name;

	@JsonIgnore
	private Set<Instance> instances = new LinkedHashSet<>();

	public Group(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Instance> getInstances() {
		return instances;
	}

	public void setInstances(Set<Instance> instances) {
		this.instances = instances;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((instances == null) ? 0 : instances.hashCode());
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
		Group other = (Group) obj;
		if (instances == null) {
			if (other.instances != null)
				return false;
		} else if (!instances.equals(other.instances))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
