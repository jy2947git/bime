package com.focaplo.myfuse.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.StoredFile;
import com.focaplo.myfuse.service.EncryptionService;
import com.focaplo.myfuse.service.StorageService;

/**
 * @author jy2947
 *
 */
public class LocalDriveStorageManager implements StorageService {
	protected final Log log = LogFactory.getLog(getClass());
	/**
	 * the path of the whole storage for bime application
	 */
	private String storageRootPath="/usr/local/bime-home";
	/*
	 * 
	 */
	@Autowired
	private EncryptionService encrypter;
	
	public void setStorageRootPath(String storageRootPath) {
		this.storageRootPath = storageRootPath;
	}

	public void setEncrypter(EncryptionService encrypter) {
		this.encrypter = encrypter;
	}

	public InputStream downloadFile(Lab lab, StoredFile storedFile) {
		//lab storage identifier is /lab-name
		@SuppressWarnings("unused")
		String labStorageDir = lab.getStorageIdentity();
		//file storage identifier is /meeting-files/file-id
		@SuppressWarnings("unused")
		String fileStorageDir = storedFile.getUniqueStorageIdentifier();
		//full-path is /usr/local/bime-home/lab-name/meeting-file/file-id
		String fullpath = storedFile.getFullPath();
		//read the file from fullpath to input stream
		
		try {
			FileInputStream fis = new FileInputStream(new File(fullpath));
			if(storedFile.isEncrypted()){
				return this.encrypter.getDecryptedInputStream(storedFile.getPassword(), fis);
			}else{
				return fis;
			}
		
		} catch (FileNotFoundException e) {
			log.error("file not found" + storedFile.getFullPath(), e);
			throw new RuntimeException(e);
		}
	}

	public void prepareStorageForLab(Lab lab) {
		lab.setStorageIdentity(File.separator + lab.getName());
		//mkdir this.rootPath + lab.getStorageIdentifier
		try {
			Runtime.getRuntime().exec("mkdir " + lab.getName(), null, new File(this.storageRootPath));
		} catch (IOException e) {
			log.error("failed to create lab directory " + lab.getName() + " in " + this.storageRootPath, e);
			throw new RuntimeException(e);
		}
		
	}

	public boolean removeFile(Lab lab, StoredFile storedFile) {
		//delete file
		String fullpath = storedFile.getFullPath();
		try{
			Runtime.getRuntime().exec("rm -f " + fullpath, null, new File(this.storageRootPath));
		}catch(IOException e){
			log.error("failed to delete file in lab directory " + lab.getName() + " name " + this.storageRootPath, e);
			throw new RuntimeException(e);
		}
		return true;
	}

	public void removeStorageForLab(Lab lab) {
		//delete lab directory
		String fullpath = this.storageRootPath  + lab.getStorageIdentity();
		try{
			Runtime.getRuntime().exec("mv " + lab.getName() + " " + lab.getName()+"-cancelled", null, new File(this.storageRootPath));
		}catch(IOException e){
			log.error("failed to delete rename lab directory " + lab.getName() , e);
			throw new RuntimeException(e);
		}

	}

	public void storeFile(Lab lab, StoredFile storedFile,
			ByteArrayInputStream is) {
		String labPath = this.storageRootPath  + lab.getStorageIdentity();
		///meeting-file/12345
		String fileIdentifier = File.separator + storedFile.getFileType() + File.separator + storedFile.getId();
		String fullpath = labPath + fileIdentifier;
		String fullurl = fullpath;
		storedFile.setUniqueStorageIdentifier(fileIdentifier);
		storedFile.setFullPath(fullpath);
		storedFile.setFullUrl(fullurl);
		//make sure the full-type directory already exists, otherwise create it
		try {
			Runtime.getRuntime().exec("mkdir -p " + storedFile.getFileType(), null, new File(labPath));
		} catch (IOException e1) {
			log.error("failed to exec " + "mkdir " + storedFile.getFileType(),e1);
			throw new RuntimeException(e1);
		}
		//save file
		log.info("saving file to " + storedFile.getFullPath());
		File targetFile = new File(storedFile.getFullPath());
		if(targetFile.exists()){
			throw new RuntimeException("Failed to save uploaded file, file already exists " + targetFile.getAbsolutePath());
		}
		try {
			//try 3 times
			int retry=0;
			int totalRetry=3;
			while(!targetFile.createNewFile() && retry++<totalRetry){
				log.warn("could not create the file " + targetFile.getAbsolutePath() + " try more...");
				try {
					Thread.sleep(2*1000);//sleep for 2 seconds
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}
			if(!targetFile.exists()){
				throw new RuntimeException("Failed to save uploaded file, file could not be created " + targetFile.getAbsolutePath());
			}
		} catch (IOException e1) {
			log.error("Failed to save uploaded file, file could not be created " + targetFile.getAbsolutePath());
			throw new RuntimeException(e1);
		}
		
		FileOutputStream fos = null;
		InputStream fis = is;
		try{
			
			fos = new FileOutputStream(storedFile.getFullPath());
			if(storedFile.isEncrypted()){
				fis = this.encrypter.getEncryptedInputStream(storedFile.getPassword(), is);
			}
			byte buf[]=new byte[8024];
		    int len;
		    while((len=fis.read(buf))>0){
		    	fos.write(buf,0,len);
		    }
	
		    
		
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
		    try {
		    	if(fos!=null){
		    		fos.close();
		    	}
				if(fis!=null){
					fis.close();
				}
			} catch (IOException e) {
				log.error("failed to close file-output-stream", e);
			}
		}
		
	}

	public void storeFile(Lab lab, StoredFile storedFile, byte[] contents) {
		ByteArrayInputStream is = null;
		try{
			is = new ByteArrayInputStream(contents);
		
			this.storeFile(lab, storedFile, is);
		}catch(Exception e){
			log.error("failed to save bytes ", e);
		}finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					log.error("failed to close byte-array-input-stream", e);
				}
				
			}
		}
	}

}
