package org.timesheet.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.timesheet.domain.Employee;
import org.timesheet.service.dao.EmployeeDao;
import org.timesheet.web.exceptions.EmployeeDeleteException;

@Controller
@RequestMapping("employees")
public class EmployeeController {
	
	private EmployeeDao employeeDao;

	@Autowired
	public void setEmployeeDao(EmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}
	
	public EmployeeDao getEmployeeDao() {
		return employeeDao;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String showEmployees(Model model) {
		List<Employee> employees = employeeDao.list();
		model.addAttribute("employees", employees);
		
		return "employees/list";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String getEmployee(@PathVariable("id") long id, Model model) {
		Employee employee = employeeDao.find(id);
		model.addAttribute("employee", employee);
		
		return "employees/view";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String updateEmployee(@PathVariable("id") long id, Employee employee) {
		employee.setId(id);
		employeeDao.update(employee);
		
		return "redirect:/employees";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String deleteEmployee(@PathVariable("id") long id) throws EmployeeDeleteException {
		
		Employee toDelete = employeeDao.find(id);
		boolean wasDelete = employeeDao.removeEmployee(toDelete);
		
		if (!wasDelete) {
			throw new EmployeeDeleteException(toDelete);
		}
		
		return "redirect:/employees";
	}
	
	@ExceptionHandler(EmployeeDeleteException.class)
	public ModelAndView handleDeleteException(EmployeeDeleteException e) {
		ModelMap model = new ModelMap();
		model.put("employee", e.getEmployee());
		return new ModelAndView("employees/delete-error", model);
	}
	
	@RequestMapping(params = "new", method = RequestMethod.GET)
	public String createEmployeeForm(Model model) {
		model.addAttribute("employee", new Employee());
		return "employees/new";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String addEmployee(Employee employee) {
	    employeeDao.add(employee);

	    return "redirect:/employees";
	}
	
	

}
