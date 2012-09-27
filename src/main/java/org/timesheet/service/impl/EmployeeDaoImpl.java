package org.timesheet.service.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.timesheet.domain.Employee;
import org.timesheet.service.dao.EmployeeDao;

@Repository("employeeDao")
public class EmployeeDaoImpl extends HibernateDao<Employee, Long> implements EmployeeDao {

	public boolean removeEmployee(Employee employee) {
		Query employeeTaskQuery = currentSession().createQuery(
				"from Task t where :id in elements(t.assignedEmployees)");
		employeeTaskQuery.setParameter("id", employee.getId());
		
		if (!employeeTaskQuery.list().isEmpty()) {
			return false;
		}
		
		Query employeeTimesheetQuery = currentSession().createQuery(
				"from Timesheet t where t.who.id = :id");
		employeeTimesheetQuery.setParameter("id", employee.getId());
		
		if (!employeeTimesheetQuery.list().isEmpty()) {
			return false;
		}
		
		remove(employee);
		return true;
	}
}
