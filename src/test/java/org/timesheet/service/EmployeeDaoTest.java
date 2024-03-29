package org.timesheet.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.timesheet.domain.Employee;
import org.timesheet.service.impl.InMemoryDao;

public class EmployeeDaoTest {
	
	private GenericDao<Employee, Long> employeeDao = new InMemoryDao<Employee, Long>();
	
	@Before
	public void setUp() {
		for (int i = 0;  i < 5; i++) {
			Employee e = new Employee("Mike " + i, "IT");
			employeeDao.add(e);
		}
	}
	
	@Test
	public void testAdd() {
		int oldSize = employeeDao.list().size();
		Employee e = new Employee("Bob", "IT");
		employeeDao.add(e);
		int newSize = employeeDao.list().size();
		
		assertFalse(oldSize == newSize);
	}
	
	@Test
	public void testRemove() {
		int oldSize = employeeDao.list().size();
		Employee e = employeeDao.find(1L);
		employeeDao.remove(e);
		int newSize = employeeDao.list().size();
		
		assertFalse(oldSize == newSize);
	}
	
	@Test
	public void testList() {
		List<Employee> list = employeeDao.list();
		assertNotNull(list);
		assertFalse(list.isEmpty());
	}

}
