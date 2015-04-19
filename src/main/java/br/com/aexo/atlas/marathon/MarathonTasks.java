package br.com.aexo.atlas.marathon;

import java.util.ArrayList;
import java.util.List;

public class MarathonTasks {

	private List<MarathonTask> tasks = new ArrayList<>();

	public List<MarathonTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<MarathonTask> tasks) {
		this.tasks = tasks;
	}

	@Override
	public String toString() {
		return "MarathonTasks [tasks=" + tasks + "]";
	}

}
