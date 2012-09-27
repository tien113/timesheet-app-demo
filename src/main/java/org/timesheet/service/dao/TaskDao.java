package org.timesheet.service.dao;

import org.timesheet.domain.Task;
import org.timesheet.service.GenericDao;

public interface TaskDao extends GenericDao<Task, Long> {

	boolean removeTask(Task task);
}
