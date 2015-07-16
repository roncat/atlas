package br.com.aexo.atlas.domain;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AtlasConfiguration {

	private final String marathonURL;
	private final String callback;
	private final String configFileName;
	private final String command;
	private final String namespace;
	private String zkServerUrl;

	public AtlasConfiguration() {
		namespace = System.getProperty("NAMESPACE");
		marathonURL = System.getProperty("MARATHON_URL");
		callback = System.getProperty("CALLBACK");
		configFileName = System.getProperty("CONFIG_FILE_NAME");
		command = System.getProperty("COMMAND");
		zkServerUrl = System.getProperty("ZK_SERVER");
	}

	public String getMarathonURL() {
		return marathonURL;
	}

	public String getCallback() {
		return callback;
	}

	public String getConfigFileName() {
		return configFileName;
	}

	public String getCommand() {
		return command;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getZookepperURL() {
		return zkServerUrl;
	}

}
