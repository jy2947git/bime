package com.focaplo.myfuse.service;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.model.ExperimentImage;
import com.focaplo.myfuse.model.ExperimentNote;
import com.focaplo.myfuse.model.ExperimentProtocol;
import com.focaplo.myfuse.model.ManagedProject;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.impl.ProjectManager;

@Transactional
@TransactionConfiguration(defaultRollback=false)
public class ProjectManagerTestCase extends BaseManagerTestCase {

	@Test
	@Transactional
	public void testCreateNewProject(){
		this.createNewProject();
	}
	
	private ManagedProject createNewProject(){
		ManagedProject p = new ManagedProject();
		p.setName("project " + new Date());
//		UserManager u = (UserManager)this.applicationContext.getBean("userManager");
		List<User> superUsers = userManager.getUsersWithRole(Constants.SUPER_USER_ROLE);
		List<User> regularUsers = userManager.getUsersWithRole(Constants.USER_ROLE);		
		p.getOwners().add(superUsers.get(0));
		p.getParticipants().add(superUsers.get(0));
		p.getParticipants().add(regularUsers.get(0));
//		ProjectManager pm = (ProjectManager)this.applicationContext.getBean("projectManager");
		projectManager.save(p);
		log.info("saved " + p.getId());
		return p;
	}
	@Test
	@Transactional
	public void testRemoveUserFromProject(){
//		ProjectManager pm = (ProjectManager)this.applicationContext.getBean("projectManager");
		ManagedProject mp = (ManagedProject) projectManager.get(ManagedProject.class, new Long("1"));
		Iterator<User> ite = mp.getOwners().iterator();
		while(ite.hasNext()){
			log.info("owner " + ite.next().getFullName());
		}
		Iterator<User> itf = mp.getParticipants().iterator();
		while(itf.hasNext()){
			log.info("participants:" + itf.next().getFullName());
		}
	}

	@Test
	@Transactional
	public void testSaveProtocol(){
		ExperimentProtocol protocol = new ExperimentProtocol();
		protocol.setCreatedByName("super");
		protocol.setName("protocl " + new Date());
	
		protocol.setExperimentProcedure("first do this the do that");
		protocol.setManagedProject(null);
		{
			ExperimentNote note = new ExperimentNote();
			Calendar startDate = Calendar.getInstance();
			startDate.add(Calendar.DAY_OF_YEAR, -19);
			Calendar endDate = Calendar.getInstance();
			endDate.add(Calendar.DAY_OF_YEAR, -9);
			note.setStartDate(startDate.getTime());
			note.setEndDate(endDate.getTime());
			note.setResearcher(this.userManager.getUser("1"));
			note.setExperimentProtocol(protocol);
			note.setNotes("this is good");
			ExperimentImage image = new ExperimentImage();
			image.setFileName("s://images/my-image.dat");
			note.getExperimentImages().add(image);
			protocol.getExperimentNotes().add(note);
		}
		this.projectManager.saveProtocol(protocol, true, "super");
	}
	
	@Test
	public void testSaveNote(){
		ManagedProject wp = createNewProject();
		
		ExperimentNote note = new ExperimentNote();
		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.DAY_OF_YEAR, -19);
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_YEAR, -9);
		note.setStartDate(startDate.getTime());
		note.setEndDate(endDate.getTime());
		note.setResearcher(this.userManager.getUser("1"));
//		note.setExperimentProtocol(protocol);
		note.setManagedProject(wp);
		note.setNotes("this is good");
		
		note.getAccessedBy().add(this.userManager.getUser("2"));
		note.getAccessedBy().add(this.userManager.getUser("3"));
		ExperimentImage image = new ExperimentImage();
		image.setFileName("s://images/my-image.dat");
		note.getExperimentImages().add(image);
		this.projectManager.saveExperimentNote(note);
		ExperimentNote en = (ExperimentNote)this.projectManager.get(ExperimentNote.class, note.getId());
		en.getExperimentImages();
		en.getAccessedBy();
	}
	
	@Test
	public void testChangeNote(){
		ExperimentNote note = (ExperimentNote) this.projectManager.get(ExperimentNote.class, new Long("19"));
		note.getExperimentImages().clear();
		ExperimentImage image = new ExperimentImage();
		image.setFileName("s://images/my-image333.dat");
		note.getExperimentImages().add(image);
		note.getAccessedBy().add(this.userManager.getUser("2"));
		this.projectManager.saveExperimentNote(note);
	}
	
	@Test
	public void testGetNoteImages(){
		ExperimentNote note = (ExperimentNote) this.projectManager.get(ExperimentNote.class, new Long("1"));
		System.out.println("note name:" + note.getName());
		System.out.println(note.getExperimentImages().size());
	}
}
