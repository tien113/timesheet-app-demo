package org.timesheet.service.dao;

import org.timesheet.domain.Manager;
import org.timesheet.service.GenericDao;

public interface ManagerDao extends GenericDao<Manager, Long> {

	boolean removeManager(Manager manager);
}
