package com.focaplo.myfuse.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="stored_file")
@Inheritance(strategy=InheritanceType.JOINED)
public class StoredFile extends BaseObject {
	public static final String EXPERIMENT_NOTE = "experiment_note";
	public static final String MEETING_FILE = "meeting_file";
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Basic
	private String uniqueStorageIdentifier;
	
	@Column(nullable=false, length=100)
	private String fileName;
	@Basic
	private String fileType;
	@ManyToOne
	@JoinColumn(name="uploaded_by_user_id")
	private User uploadedBy;
	@Basic
	private String fullPath;
	@Basic
	private String fullUrl;
	@Basic
	private String backupStorageIdentifier;
	@Basic
	private boolean encrypted;
	@Transient
	private String password;
	
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
		return "id:" + id + " name:" + fileName + " type:" + fileType + " storage:" + this.uniqueStorageIdentifier + " url:" + this.fullUrl + " path:"+ this.fullPath;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public User getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(User uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getUniqueStorageIdentifier() {
		return uniqueStorageIdentifier;
	}

	public void setUniqueStorageIdentifier(String uniqueStorageIdentifier) {
		this.uniqueStorageIdentifier = uniqueStorageIdentifier;
	}

	public String getBackupStorageIdentifier() {
		return backupStorageIdentifier;
	}

	public void setBackupStorageIdentifier(String backupStorageIdentifier) {
		this.backupStorageIdentifier = backupStorageIdentifier;
	}

	public boolean isEncrypted() {
		return encrypted;
	}

	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
