package com.focaplo.myfuse.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name="experiment_protocol")
public class ExperimentProtocol extends BaseObject implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false,length=50)
	private String name;
	@Column(nullable=false)
	private int protocolVersion=1;
	@Column(length=50)
	private String createdByName;
	
	@Column(nullable=true,length=2000)
	private String experimentProcedure;
	@OneToMany(mappedBy="experimentProtocol", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<ExperimentProtocolAudit> protocolAudits=new HashSet<ExperimentProtocolAudit>();
	
	@OneToMany(mappedBy="experimentProtocol", cascade=CascadeType.ALL)
	private Set<ExperimentNote> experimentNotes=new HashSet<ExperimentNote>();
	
	@ManyToOne
	@JoinColumn(name="managed_project_id")
	private ManagedProject managedProject;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Transient
	public String getFullName(){
		return this.getName() + " - " + this.getProtocolVersion();
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



	public String getExperimentProcedure() {
		return experimentProcedure;
	}

	public void setExperimentProcedure(String experimentProcedure) {
		this.experimentProcedure = experimentProcedure;
	}

	public Set<ExperimentProtocolAudit> getProtocolAudits() {
		return protocolAudits;
	}

	public void setProtocolAudits(Set<ExperimentProtocolAudit> protocolAudits) {
		this.protocolAudits = protocolAudits;
	}
	public int getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public Set<ExperimentNote> getExperimentNotes() {
		return experimentNotes;
	}

	public void setExperimentNotes(Set<ExperimentNote> experimentNotes) {
		this.experimentNotes = experimentNotes;
	}

	public ManagedProject getManagedProject() {
		return managedProject;
	}

	public void setManagedProject(ManagedProject managedProject) {
		this.managedProject = managedProject;
	}
	
}
