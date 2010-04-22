package com.focaplo.myfuse.dao;

import java.util.List;

import com.focaplo.myfuse.model.ExperimentImage;
import com.focaplo.myfuse.model.ExperimentNote;
import com.focaplo.myfuse.model.ToDo;
import com.focaplo.myfuse.model.WorkLog;

public interface IProjectDao extends IUniversalDao {


	List<ExperimentNote> getNotesOfProject(Long projectId);

	List<ToDo> getToDosOfProject(Long projectId);

	List<WorkLog> getWorkLogsOfToDo(Long toDoId);

	List<ExperimentImage> getExperimentImagesOfNote(Long noteId);
}
