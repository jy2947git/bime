package com.focaplo.myfuse.dao;

import java.util.Set;

import org.junit.Test;

import com.focaplo.myfuse.model.ExperimentImage;
import com.focaplo.myfuse.model.ExperimentNote;

public class ProjectDaoTest extends BaseDaoTestCase {
	@Test
	public void testSaveNote(){
		ExperimentNote note = new ExperimentNote();
		ExperimentImage image = new ExperimentImage();
		image.setFileName("what1");
		note.setNotes("test ");
		note.getExperimentImages().add(image);
		this.universalDao.saveOrUpdate(note);
	}
	
	@Test
	public void testGetNote(){
		ExperimentNote note = (ExperimentNote)this.projectDao.get(ExperimentNote.class, new Long("1"));
		Set<ExperimentImage> images = note.getExperimentImages();
		System.out.println("found " + images.size());
		for(ExperimentImage img:images){
			System.out.println(img.getUniqueStorageIdentifier());
		}
	}
}
