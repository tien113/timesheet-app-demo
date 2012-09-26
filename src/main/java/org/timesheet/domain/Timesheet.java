package org.timesheet.domain;

public class Timesheet {

	private Employee who;
	private Task task;
	private Integer hours;
	
	public Timesheet(Employee who, Task task, Integer hours) {
		this.who = who;
		this.task = task;
		this.hours = hours;
	}

	public Employee getWho() {
		return who;
	}

	public Task getTask() {
		return task;
	}

	public Integer getHours() {
		return hours;
	}
	
	public void alterHours(Integer hours) {
		this.hours = hours;
	}
	
	@Override
	public String toString() {
		return "Timesheet [who=" + who + ", task=" + task + ", hours=" + hours + "]";
	}
}
