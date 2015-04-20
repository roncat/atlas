package br.com.aexo.atlas.marathon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tasks {

	private List<Task> tasks = new ArrayList<>();

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	

	@Override
	public String toString() {
		return "MarathonTasks [tasks=" + tasks + "]";
	}

}
