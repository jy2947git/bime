package com.focaplo.myfuse.model;

import java.util.Date;
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

@Entity
@Table(name="to_do")
public class ToDo extends BaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column
	private Date startDate;
	@Column
	private Date endDate;
	@Column
	private String status;
	@OneToMany(mappedBy="toDo",cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<WorkLog> workLogs=new HashSet<WorkLog>();
	@Column
	private String subject;
	@Column
	private String message;
	@ManyToOne
	@JoinColumn(name="project_id")
	private ManagedProject managedProject;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<WorkLog> getWorkLogs() {
		return workLogs;
	}

	public void setWorkLogs(Set<WorkLog> workLogs) {
		this.workLogs = workLogs;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ManagedProject getManagedProject() {
		return managedProject;
	}

	public void setManagedProject(ManagedProject managedProject) {
		this.managedProject = managedProject;
	}

}
