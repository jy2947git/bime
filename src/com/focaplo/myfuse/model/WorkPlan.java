package com.focaplo.myfuse.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.persistence.CascadeType;
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name="work_plan")
public class WorkPlan extends BaseObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column
	private String planType;
	@Column
	private String planIdentity;
	@OneToMany(mappedBy="workPlan",cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	private Set<WorkPlanItem> workPlanItems = new HashSet<WorkPlanItem>();
	@Column
	private String status;
	@ManyToOne
	@JoinColumn(name="managed_project_id")
	private ManagedProject managedProject;
	
	public ManagedProject getManagedProject() {
		return managedProject;
	}

	public void setManagedProject(ManagedProject managedProject) {
		this.managedProject = managedProject;
	}
	
	@Override
	public boolean equals(Object o) {
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public String toString() {
		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public String getPlanIdentity() {
		return planIdentity;
	}

	public void setPlanIdentity(String planIdentity) {
		this.planIdentity = planIdentity;
	}

	public Set<WorkPlanItem> getWorkPlanItems() {
		return workPlanItems;
	}

	public void setWorkPlanItems(Set<WorkPlanItem> workPlanItems) {
		this.workPlanItems = workPlanItems;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
