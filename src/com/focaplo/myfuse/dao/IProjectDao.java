package com.focaplo.myfuse.dao;

import java.util.List;

import com.focaplo.myfuse.model.ExperimentImage;
import com.focaplo.myfuse.model.ExperimentNote;
import com.focaplo.myfuse.model.ToDo;
import com.focaplo.myfuse.model.WorkLog;
import com.focaplo.myfuse.model.WorkPlan;
import com.focaplo.myfuse.model.WorkPlanItem;

public interface IProjectDao extends IUniversalDao {

	List<WorkPlanItem> getPlanItemsOfWorkPlan(Long planId);

	List<WorkPlan> getWorkPlanOfProject(Long id);

	List<ExperimentNote> getNotesOfProject(Long projectId);

	List<ToDo> getToDosOfProject(Long projectId);

	List<WorkLog> getWorkLogsOfToDo(Long toDoId);

	List<ExperimentImage> getExperimentImagesOfNote(Long noteId);
}
