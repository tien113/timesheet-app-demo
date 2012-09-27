package org.timesheet.service.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.timesheet.domain.Timesheet;
import org.timesheet.service.dao.TimesheetDao;

@Repository("timesheetDao")
public class TimesheetDaoImpl extends HibernateDao<Timesheet, Long> implements TimesheetDao {

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Timesheet> list() {
		return currentSession().createCriteria(Timesheet.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
	}
}
