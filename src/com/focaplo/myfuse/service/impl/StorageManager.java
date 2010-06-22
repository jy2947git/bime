package com.focaplo.myfuse.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.StoredFile;
import com.focaplo.myfuse.service.ConfigurationService;
import com.focaplo.myfuse.service.StorageService;
@Service(value="storageManager")
public class StorageManager implements StorageService {
	@Autowired
	ConfigurationService bimeConfiguration;
	@Autowired
	@Qualifier("localDriveStorageManager")
	private StorageService localService;
	@Autowired
	@Qualifier("amazonStorageManager")
	private StorageService s3Service;
	
	public StorageService getCurrentStorageService(){
		if(bimeConfiguration.isLocalStorage()){
			return this.localService;
		}else if(bimeConfiguration.isAmazonStorage()){
			return this.s3Service;
		}else{
			throw new IllegalArgumentException("Must be either local-storage or amazon-storage in bime configuration!");
		}
	}
	public void setS3Service(StorageService s3Service) {
		this.s3Service = s3Service;
	}

	public void setLocalService(StorageService localService) {
		this.localService = localService;
	}

	public void setBimeConfiguration(ConfigurationService bimeConfiguration) {
		this.bimeConfiguration = bimeConfiguration;
	}
	
	public InputStream downloadFile(Lab lab, StoredFile storedFile) {
		return this.getCurrentStorageService().downloadFile(lab, storedFile);
	}

	public void prepareStorageForLab(Lab lab) {
		this.getCurrentStorageService().prepareStorageForLab(lab);

	}

	public boolean removeFile(Lab lab, StoredFile storedFile) {
		return this.getCurrentStorageService().removeFile(lab, storedFile);
	}

	public void removeStorageForLab(Lab lab) {
		this.getCurrentStorageService().removeStorageForLab(lab);

	}

	public void storeFile(Lab lab, StoredFile storedFile,
			ByteArrayInputStream is) {
		this.getCurrentStorageService().storeFile(lab, storedFile, is);

	}

	public void storeFile(Lab lab, StoredFile storedFile, byte[] contents) {
		this.getCurrentStorageService().storeFile(lab, storedFile, contents);

	}

}
