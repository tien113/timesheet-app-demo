package org.timesheet.service;

import java.util.List;

import org.timesheet.domain.Employee;
import org.timesheet.domain.Manager;
import org.timesheet.domain.Task;


public interface TimesheetService {
	
	Task busiestTask();
	
	List<Task> tasksForEmployee(Employee e);
	
	List<Task> tasksForManager(Manager m);
	
}
