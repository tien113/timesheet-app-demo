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
import org.timesheet.domain.Manager;
import org.timesheet.service.dao.ManagerDao;
import org.timesheet.web.exceptions.ManagerDeleteException;

@Controller
@RequestMapping("/managers")
public class ManagerController {

    private ManagerDao managerDao;

    @Autowired
    public void setManagerDao(ManagerDao managerDao) {
        this.managerDao = managerDao;
    }

    public ManagerDao getManagerDao() {
        return managerDao;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String showManagers(Model model) {
        List<Manager> employees = managerDao.list();
        model.addAttribute("managers", employees);

        return "managers/list";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String deleteManager(@PathVariable("id") long id)
            throws ManagerDeleteException {

        Manager toDelete = managerDao.find(id);
        boolean wasDeleted = managerDao.removeManager(toDelete);

        if (!wasDeleted) {
            throw new ManagerDeleteException(toDelete);
        }

        // everything OK, see remaining managers
        return "redirect:/managers";
    }

    @ExceptionHandler(ManagerDeleteException.class)
    public ModelAndView handleDeleteException(ManagerDeleteException e) {
        ModelMap model = new ModelMap();
        model.put("manager", e.getManager());
        return new ModelAndView("managers/delete-error", model);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getManager(@PathVariable("id") long id, Model model) {
        Manager manager = managerDao.find(id);
        model.addAttribute("manager", manager);

        return "managers/view";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String updateManager(@PathVariable("id") long id, Manager manager) {
        manager.setId(id);
        managerDao.update(manager);

        return "redirect:/managers";
    }

    @RequestMapping(params = "new", method = RequestMethod.GET)
    public String createManagerForm(Model model) {
        model.addAttribute("manager", new Manager());
        return "managers/new";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addManager(Manager manager) {
        managerDao.add(manager);

        return "redirect:/managers";
    }
}
