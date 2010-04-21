package com.focaplo.myfuse.dao.hibernate;

import java.util.List;

import com.focaplo.myfuse.dao.IProjectDao;
import com.focaplo.myfuse.model.ExperimentImage;
import com.focaplo.myfuse.model.ExperimentNote;
import com.focaplo.myfuse.model.ToDo;
import com.focaplo.myfuse.model.WorkLog;
import com.focaplo.myfuse.model.WorkPlan;
import com.focaplo.myfuse.model.WorkPlanItem;

public class ProjectDao extends UniversalDao implements
		IProjectDao {

	@SuppressWarnings("unchecked")
	public List<WorkPlanItem> getPlanItemsOfWorkPlan(Long planId) {
		return this.getSessionFactory().getCurrentSession().createQuery("from WorkPlanItem where workPlan.id=?").setLong(0, planId).list();
	}

	@SuppressWarnings("unchecked")
	public List<WorkPlan> getWorkPlanOfProject(Long id) {
		return this.getSessionFactory().getCurrentSession().createQuery("from WorkMonthlyPlan where managedProject.id=?").setLong(0, id).list();
	}

	@SuppressWarnings("unchecked")
	public List<ExperimentNote> getNotesOfProject(Long projectId) {
		return this.getSessionFactory().getCurrentSession().createQuery("from ExperimentNote where managedProject.id=?").setLong(0, projectId).list();
	}

	@SuppressWarnings("unchecked")
	public List<ToDo> getToDosOfProject(Long projectId) {
		return this.getSessionFactory().getCurrentSession().createQuery("from ToDo where managedProject.id=?").setLong(0, projectId).list();
	}

	@SuppressWarnings("unchecked")
	public List<WorkLog> getWorkLogsOfToDo(Long toDoId) {
		return this.getSessionFactory().getCurrentSession().createQuery("from WorkLog where toDo.id=?").setLong(0, toDoId).list();
	}

	public List<ExperimentImage> getExperimentImagesOfNote(Long noteId) {
		return this.getSessionFactory().getCurrentSession().createQuery("from ExperimentImage where experimentNote.id=?").setLong(0, noteId).list();
	}

}
