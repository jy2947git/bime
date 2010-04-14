package com.focaplo.myfuse.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.focaplo.myfuse.model.ExperimentImage;
import com.focaplo.myfuse.model.ExperimentNote;
import com.focaplo.myfuse.model.ExperimentProtocol;
import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.ManagedProject;
import com.focaplo.myfuse.model.ToDo;
import com.focaplo.myfuse.model.WorkLog;
import com.focaplo.myfuse.model.WorkMonthlyPlan;
import com.focaplo.myfuse.model.WorkPlan;
import com.focaplo.myfuse.model.WorkPlanItem;

public interface ProjectManager extends UniversalManager {



	void deleteProjects(List<Long> toBeDeleted);


	List<ManagedProject>  getProjectAccessibleTo(Long userId);
	
	void saveWorkPlan(WorkPlan wp);
	
	public void saveWorkPlanItem(WorkPlanItem wpi);

	public void saveMonthlyWorkPlan(WorkMonthlyPlan wmp);
	
	public void saveProtocol(ExperimentProtocol protocol, boolean realDirty, String dirtyUser);
	
	public void saveExperimentNote(ExperimentNote note);


	void deleteProtocols(List<Long> toBeDeleted);


	void deleteNotes(List<Long> toBeDeleted);


	void deleteWorkPlans(List<Long> toBeDeleted);


	List<WorkPlanItem> getPlanItemsOfWorkPlan(Long planId);


	void deleteWorkPlanItems(List<Long> toBeDeleted);


	List<WorkPlan> getWorkPlanOfProject(Long id);
	
	public List<ExperimentNote> getNotesOfProject(Long projectId);


	List<ToDo> getToDoOfProject(Long projectId);


	void deleteToDos(List<Long> toBeDeleted);


	List<WorkLog> getWorkLogsOfToDo(Long toDoId);


	void uploadExperimentImageFile(Lab lab, ExperimentImage imageFile,
			ByteArrayInputStream stream);
	
	public void removeExperimentImageFile(Lab lab, ExperimentImage imageFile);


	List<ExperimentImage> getExperimentImagesOfNote(Long noteId);


	void saveProject(ManagedProject project);


	void saveProjectParticipants(ManagedProject project);
}
