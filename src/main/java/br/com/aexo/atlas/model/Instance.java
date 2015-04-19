package br.com.aexo.atlas.model;


public class Instance {

	private String host;
	private boolean alive;
	private Integer port;

	public Instance(String host, Integer port, boolean alive) {
		this.host = host;
		this.port = port;
		this.alive = alive;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

}
