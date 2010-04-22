package com.focaplo.myfuse.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name="experiment_note")
public class ExperimentNote extends BaseObject implements Serializable, Securable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column
	private String name;
	@ManyToOne
	@JoinColumn(name="experiment_protocol_id")
	private ExperimentProtocol experimentProtocol;
	
	@ManyToOne
	@JoinColumn(name="managed_project_id")
	private ManagedProject managedProject;
	
	@Column
	private Date startDate;
	@Column
	private Date endDate;
	@Column(length=500)
	private String notes;
	@ManyToOne
	@JoinColumn(name="user_id")
	private User researcher;
	
	@OneToMany(mappedBy="experimentNote",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private Set<ExperimentImage> experimentImages=new HashSet<ExperimentImage>();
	
	@ManyToMany(targetEntity=User.class,fetch=FetchType.LAZY)
	@JoinTable(name="note_access",joinColumns=@JoinColumn(name="note_id"),inverseJoinColumns=@JoinColumn(name="user_id"))
	private Set<User> accessedBy = new HashSet<User>();
	@Transient
	public List<LabelValue> getAccessedUsers(){
		List<LabelValue> accessUserLabels = new ArrayList<LabelValue>();
		for(User user:this.accessedBy){
			accessUserLabels.add(new LabelValue(user.getFullName(), user.getId().toString()));
		}
		return accessUserLabels;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ExperimentProtocol getExperimentProtocol() {
		return experimentProtocol;
	}

	public void setExperimentProtocol(ExperimentProtocol experimentProtocol) {
		this.experimentProtocol = experimentProtocol;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}



	public User getResearcher() {
		return researcher;
	}

	public void setResearcher(User researcher) {
		this.researcher = researcher;
	}

	public Set<ExperimentImage> getExperimentImages() {
		return experimentImages;
	}

	public void setExperimentImages(Set<ExperimentImage> experimentImages) {
		this.experimentImages = experimentImages;
	}

	public ManagedProject getManagedProject() {
		return managedProject;
	}

	public void setManagedProject(ManagedProject managedProject) {
		this.managedProject = managedProject;
	}

	public Set<User> getAccessedBy() {
		return accessedBy;
	}

	public void setAccessedBy(Set<User> accessedBy) {
		this.accessedBy = accessedBy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	
	@Transient
	boolean canAuthorize=false;
	@Transient
	boolean canDelete=false;
	@Transient
	boolean canEdit=false;

	public boolean getCanAuthorize() {
		return canAuthorize;
	}
	public void setCanAuthorize(boolean canAuthorize) {
		this.canAuthorize = canAuthorize;
	}

	public boolean getCanEdit() {
		return canEdit;
	}
	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	public boolean getCanDelete() {
		return canDelete;
	}
	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}
	public boolean getIsLocked() {
		return (!this.canEdit) && (!this.canDelete) && (!this.canAuthorize);
	}
}
