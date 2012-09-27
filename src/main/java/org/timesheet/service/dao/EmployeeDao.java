package org.timesheet.service.dao;

import org.timesheet.domain.Employee;
import org.timesheet.service.GenericDao;

public interface EmployeeDao extends GenericDao<Employee, Long> {
	
	boolean removeEmployee(Employee employee);
	
}
