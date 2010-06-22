package com.focaplo.myfuse.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
@Entity
@Table(name="managed_order")
public class ManagedOrder extends BaseObject implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id;
	@Column(nullable=true,length=50)
	private String accountNumber;

	@Column(nullable=true)
	private BigDecimal totalPrice = BigDecimal.ZERO;
	@Column(nullable=true)
	private String fundName;
	@Column(nullable=true)
	private Date submitDate;
	@Column
	private Long submittedByPerson;
	public Long getSubmittedByPerson() {
		return submittedByPerson;
	}

	public void setSubmittedByPerson(Long submittedByPerson) {
		this.submittedByPerson = submittedByPerson;
	}

	@Column
	private Date approvalDate;
	@Column(nullable=true)
	private Date orderDate;
	@Column(nullable=true)
	private Long orderByPerson;
	@Column(nullable=true)
	private Long approvedByPerson;
	@Column(nullable=true)
	private String status;
	@Column(nullable=true)
	private String salesFirstName;
	@Column(nullable=true)
	private String salesLastName;
	@Column(nullable=true)
	private String salesPhone;
	@Column(nullable=true)
	private String salesEmail;
	@Column(nullable=true)
	@OneToMany(mappedBy="order",cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<OrderItem> orderItems = new HashSet<OrderItem>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}


	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}


	public Long getOrderByPerson() {
		return orderByPerson;
	}

	public void setOrderByPerson(Long orderByPerson) {
		this.orderByPerson = orderByPerson;
	}

	public Long getApprovedByPerson() {
		return approvedByPerson;
	}

	public void setApprovedByPerson(Long approvedByPerson) {
		this.approvedByPerson = approvedByPerson;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



	public String getSalesFirstName() {
		return salesFirstName;
	}

	public void setSalesFirstName(String salesFirstName) {
		this.salesFirstName = salesFirstName;
	}

	public String getSalesLastName() {
		return salesLastName;
	}

	public void setSalesLastName(String salesLastName) {
		this.salesLastName = salesLastName;
	}

	public String getSalesPhone() {
		return salesPhone;
	}

	public void setSalesPhone(String salesPhone) {
		this.salesPhone = salesPhone;
	}

	public String getSalesEmail() {
		return salesEmail;
	}

	public void setSalesEmail(String salesEmail) {
		this.salesEmail = salesEmail;
	}

	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}


}
