package org.timesheet.service.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.timesheet.domain.Manager;
import org.timesheet.service.dao.ManagerDao;

@Repository("managerDao")
public class ManagerDaoImpl extends HibernateDao<Manager, Long> implements ManagerDao {

	public boolean removeManager(Manager manager) {
		Query managerQuery = currentSession().createQuery(
				"from Task t where t.manager.id = :id");
		managerQuery.setParameter("id", manager.getId());
		
		if (!managerQuery.list().isEmpty()) {
			return false;
		}
		
		remove(manager);
		return true;
	}
}
