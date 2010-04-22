package com.focaplo.myfuse.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name="experiment_image")
@PrimaryKeyJoinColumn(name="stored_file_id")
public class ExperimentImage extends StoredFile implements Serializable{
	
	@ManyToOne
	@JoinColumn(name="experiment_note_id")
	private ExperimentNote experimentNote;
	
	public ExperimentImage() {
		super();
		this.setFileType(StoredFile.EXPERIMENT_NOTE);
	}
 

	public ExperimentNote getExperimentNote() {
		return experimentNote;
	}

	public void setExperimentNote(ExperimentNote experimentNote) {
		this.experimentNote = experimentNote;
	}


}
