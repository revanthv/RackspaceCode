package test.rackSpace;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/*
 * This class has the list of tasks
 */
@XmlRootElement(name = "tasksList")
public class Tasks {

	private List<Task> tasksList;

	public List<Task> getTasks() {
		return tasksList;
	}

	public void setTasks(List<Task> tasks) {
		this.tasksList = tasks;
	}
}
