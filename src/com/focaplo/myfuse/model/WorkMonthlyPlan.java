package com.focaplo.myfuse.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.focaplo.myfuse.Constants;
@Entity
public class WorkMonthlyPlan extends WorkPlan implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public WorkMonthlyPlan() {
		super();
		this.setPlanType(Constants.WORKPLAN_MONTHLY);
	}

	@OneToMany(mappedBy="workMonthlyPlan",cascade=CascadeType.REMOVE)
	private Set<WorkWeeklyPlan> weeklyPlans = new HashSet<WorkWeeklyPlan>();

	public Set<WorkWeeklyPlan> getWeeklyPlans() {
		return weeklyPlans;
	}

	public void setWeeklyPlans(Set<WorkWeeklyPlan> weeklyPlans) {
		this.weeklyPlans = weeklyPlans;
	}
	
}
