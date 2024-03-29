package org.timesheet.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.timesheet.domain.Employee;
import org.timesheet.domain.Manager;
import org.timesheet.domain.Task;
import org.timesheet.service.TimesheetService;
import org.timesheet.service.dao.*;
import org.timesheet.web.editors.EmployeeEditor;
import org.timesheet.web.editors.ManagerEditor;

@Controller
@RequestMapping("/timesheet-service")
public class TimesheetServiceController {

	private TimesheetService service;
    private EmployeeDao employeeDao;
    private ManagerDao managerDao;

    @Autowired
    public void setService(TimesheetService service) {
        this.service = service;
    }

    @Autowired
    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Autowired
    public void setManagerDao(ManagerDao managerDao) {
        this.managerDao = managerDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showMenu(Model model) {
        model.addAttribute("busiestTask", service.busiestTask());
        model.addAttribute("employees", employeeDao.list());
        model.addAttribute("managers", managerDao.list());

        return "timesheet-service/menu";
    }
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Employee.class, new EmployeeEditor(employeeDao));
        binder.registerCustomEditor(Manager.class, new ManagerEditor(managerDao));
    }

    @RequestMapping(value = "/manager-tasks/{id}", method = RequestMethod.GET)
    public String showManagerTasks(@PathVariable("id") long id, Model model) {
        Manager manager = managerDao.find(id);
        List<Task> tasks = service.tasksForManager(manager);

        model.addAttribute("manager", manager);
        model.addAttribute("tasks", tasks);

        return "timesheet-service/manager-tasks";
    }

    @RequestMapping(value = "/employee-tasks/{id}", method = RequestMethod.GET)
    public String showEmployeeTasks(@PathVariable("id") long id, Model model) {
        Employee employee = employeeDao.find(id);
        List<Task> tasks = service.tasksForEmployee(employee);
        
        model.addAttribute("employee", employee);
        model.addAttribute("tasks", tasks);
        
        return "timesheet-service/employee-tasks";
    }

}