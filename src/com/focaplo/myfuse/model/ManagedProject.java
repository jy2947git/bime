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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="managed_project")
public class ManagedProject extends BaseObject  implements Serializable, Securable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false)
	private String name;
	@ManyToMany(targetEntity=com.focaplo.myfuse.model.User.class, fetch=FetchType.LAZY)
	@JoinTable(name="project_owner",joinColumns=@JoinColumn(name="project_id"),inverseJoinColumns=@JoinColumn(name="user_id"))
	private Set<User> owners = new HashSet<User>();
	@ManyToMany(targetEntity=com.focaplo.myfuse.model.User.class, fetch=FetchType.LAZY)
	@JoinTable(name="project_participants",joinColumns=@JoinColumn(name="project_id"),inverseJoinColumns=@JoinColumn(name="user_id"))
	private Set<User> participants = new HashSet<User>();
	@OneToMany(mappedBy="managedProject",cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	private Set<ExperimentProtocol> experimentProtocols=new HashSet<ExperimentProtocol>();
	
//	@OneToMany(mappedBy="managedProject",cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
//	private Set<WorkPlan> workPlans=new HashSet<WorkPlan>();
	@OneToMany(mappedBy="managedProject", cascade=CascadeType.ALL)
	private Set<ToDo> toDos = new HashSet<ToDo>();
	
	@OneToMany(mappedBy="managedProject",cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	private Set<ExperimentNote> experimentNotes=new HashSet<ExperimentNote>();
	
	@Column
	private String status;
	@Column
	private Date startDate;
	@Column
	private Date endDate;
	@Column
	private String description;
	@Column
	private String fundSource;
	
	@Transient
	public User getPrincipal(){
		if(this.getOwners().isEmpty()){
			return null;
		}
		return this.getOwners().iterator().next();
	}
	public void setPrincipal(User user){
		//first clear all the owners since we only allow one
		this.getOwners().clear();
		if(!this.getOwners().contains(user)){
			//
			log.debug("adding principal " + user);
			this.getOwners().add(user);
		}
	}
	
	@Transient
	public List<LabelValue> getParticipantsLabelList(){
		List<LabelValue> accessUserLabels = new ArrayList<LabelValue>();
		for(User user:this.participants){
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



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public Set<User> getOwners() {
		return owners;
	}

	public void setOwners(Set<User> owners) {
		this.owners = owners;
	}

	public Set<User> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<User> participants) {
		this.participants = participants;
	}

	public Set<ExperimentProtocol> getExperimentProtocols() {
		return experimentProtocols;
	}

	public void setExperimentProtocols(Set<ExperimentProtocol> experimentProtocols) {
		this.experimentProtocols = experimentProtocols;
	}



//	public Set<WorkPlan> getWorkPlans() {
//		return workPlans;
//	}
//	public void setWorkPlans(Set<WorkPlan> workPlans) {
//		this.workPlans = workPlans;
//	}
	public Set<ExperimentNote> getExperimentNotes() {
		return experimentNotes;
	}

	public void setExperimentNotes(Set<ExperimentNote> experimentNotes) {
		this.experimentNotes = experimentNotes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFundSource() {
		return fundSource;
	}
	public void setFundSource(String fundSource) {
		this.fundSource = fundSource;
	}
	public Set<ToDo> getToDos() {
		return toDos;
	}
	public void setToDos(Set<ToDo> toDos) {
		this.toDos = toDos;
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
