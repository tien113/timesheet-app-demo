package org.timesheet.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.timesheet.domain.Employee;
import org.timesheet.domain.Manager;
import org.timesheet.domain.Task;
import org.timesheet.service.dao.*;
import org.timesheet.web.editors.ManagerEditor;
import org.timesheet.web.exceptions.TaskDeleteException;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private TaskDao taskDao;
    private EmployeeDao employeeDao;
    private ManagerDao managerDao;

    @Autowired
    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Autowired
    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Autowired
    public void setManagerDao(ManagerDao managerDao) {
        this.managerDao = managerDao;
    }

    public EmployeeDao getEmployeeDao() {
        return employeeDao;
    }

    public TaskDao getTaskDao() {
        return taskDao;
    }

    public ManagerDao getManagerDao() {
        return managerDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showTasks(Model model) {
        model.addAttribute("tasks", taskDao.list());

        return "tasks/list";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String deleteTask(@PathVariable("id") long id) 
            throws TaskDeleteException {

        Task toDelete = taskDao.find(id);
        boolean wasDeleted = taskDao.removeTask(toDelete);

        if (!wasDeleted) {
            throw new TaskDeleteException(toDelete);
        }

        // everything OK, see remaining tasks
        return "redirect:/tasks";
    }

    @ExceptionHandler(TaskDeleteException.class)
    public ModelAndView handleDeleteException(TaskDeleteException e) {
        ModelMap model = new ModelMap();
        model.put("task", e.getTask());
        return new ModelAndView("tasks/delete-error", model);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getTask(@PathVariable("id") long id, Model model) {
        Task task = taskDao.find(id);
        model.addAttribute("task", task);

        // add all remaining employees
        List<Employee> employees = employeeDao.list();
        Set<Employee> unassignedEmployees = new HashSet<Employee>();

        for (Employee employee : employees) {
            if (!task.getAssignedEmployees().contains(employee)) {
                unassignedEmployees.add(employee);
            }
        }

        model.addAttribute("unassigned", unassignedEmployees);

        return "tasks/view";
    }

    @RequestMapping(value = "/{id}/employees/{employeeId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeEmployee(
            @PathVariable("id") long taskId,
            @PathVariable("employeeId") long employeeId) {

        Employee employee = employeeDao.find(employeeId);
        Task task = taskDao.find(taskId);

        task.removeEmployee(employee);
        taskDao.update(task);
    }

    @RequestMapping(value = "/{id}/employees/{employeeId}", method = RequestMethod.PUT)
    public String addEmployee(
            @PathVariable("id") long taskId,
            @PathVariable("employeeId") long employeeId) {

        Employee employee = employeeDao.find(employeeId);
        Task task = taskDao.find(taskId);

        task.addEmployee(employee);
        taskDao.update(task);
        
        return "redirect:/tasks/" + taskId;
    }

    @RequestMapping(params = "new", method = RequestMethod.GET)
    public String createTaskForm(Model model) {
        model.addAttribute("task", new Task());

        // list of managers to choose from
        List<Manager> managers = managerDao.list();
        model.addAttribute("managers", managers);

        return "tasks/new";
    }
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Manager.class, new ManagerEditor(managerDao));
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addTask(Task task) {
        // generate employees
        List<Employee> employees = reduce(employeeDao.list());

        task.setAssignedEmployees(employees);
        taskDao.add(task);

        return "redirect:/tasks";
    }

    private List<Employee> reduce(List<Employee> employees) {
        List<Employee> reduced = new ArrayList<Employee>();
        Random random = new Random();
        int amount = random.nextInt(employees.size()) + 1;

        // max. five employees
        amount = amount > 5 ? 5 : amount;

        for (int i = 0; i < amount; i++) {
            int randomIdx = random.nextInt(employees.size());
            Employee employee = employees.get(randomIdx);
            reduced.add(employee);
            employees.remove(employee);
        }

        return reduced;
    }
    
    
}