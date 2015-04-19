package br.com.aexo.atlas.marathon;

public class HealthCheck {

	private boolean alive;

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	@Override
	public String toString() {
		return "HealthCheck [alive=" + alive + "]";
	}
	
	
	
}
