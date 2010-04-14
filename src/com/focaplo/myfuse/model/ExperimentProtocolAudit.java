package com.focaplo.myfuse.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name="experiment_protocol_audit")
public class ExperimentProtocolAudit extends BaseObject implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false,length=500)
	private String message;
	@ManyToOne
	@JoinColumn(name="experiment_protocol_id")
	private ExperimentProtocol experimentProtocol;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ExperimentProtocol getExperimentProtocol() {
		return experimentProtocol;
	}

	public void setExperimentProtocol(ExperimentProtocol experimentProtocol) {
		this.experimentProtocol = experimentProtocol;
	}

}
