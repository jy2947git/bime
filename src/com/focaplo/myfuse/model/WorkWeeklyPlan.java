package com.focaplo.myfuse.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity
public class WorkWeeklyPlan extends WorkPlan implements Serializable {

	@ManyToOne
	@JoinColumn(name="work_monthly_plan_id")
	private WorkMonthlyPlan workMonthlyPlan;

	public WorkMonthlyPlan getWorkMonthlyPlan() {
		return workMonthlyPlan;
	}

	public void setWorkMonthlyPlan(WorkMonthlyPlan workMonthlyPlan) {
		this.workMonthlyPlan = workMonthlyPlan;
	}
	
}
