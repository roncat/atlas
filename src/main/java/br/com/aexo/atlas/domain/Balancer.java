package br.com.aexo.atlas.domain;

import java.util.List;

public interface Balancer {

	/**
	 * retrive actual configuration in balancer
	 * 
	 * @return
	 */
	public abstract String getActualConfiguration();

	public abstract void reload();

	/**
	 * write configuration for script
	 * 
	 * @param marathonUrl
	 */
	public abstract void writeConfiguration(String script, List<Acl> acls);

	/**
	 * test configuration from script
	 * 
	 * @param script
	 * @param marathonUrl
	 * @return
	 */
	public abstract String testConfiguration(String script, List<Acl> acls);

}