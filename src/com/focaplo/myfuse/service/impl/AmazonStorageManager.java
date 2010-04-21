package com.focaplo.myfuse.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;
import org.jets3t.service.utils.ServiceUtils;

import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.StoredFile;
import com.focaplo.myfuse.service.EncryptionService;
import com.focaplo.myfuse.service.StorageService;

public class AmazonStorageManager implements StorageService{
	protected final Log log = LogFactory.getLog(getClass());
	private String awsAccessKey;
	private String awsSecretKey;
	private AWSCredentials awsCredentials;
	private S3Service s3Service;
	private String bucketNameUniqueSuffix;
	/*
	 * 
	 */
	private EncryptionService encrypter;
	
	public void setEncrypter(EncryptionService encrypter) {
		this.encrypter = encrypter;
	}
	private void initialize(){
		if(this.awsCredentials==null){
			//
			this.awsCredentials = new AWSCredentials(this.awsAccessKey, this.awsSecretKey);
		}
		if(s3Service==null){
			try {
				this.s3Service = new RestS3Service(this.awsCredentials);
			} catch (S3ServiceException e) {
				throw new RuntimeException(e);
			}
		}
	}
	public boolean removeFile(Lab lab, StoredFile storedFile) {
		this.initialize();
		String bucketName = lab.getStorageIdentity(); //lab2.labia.com
		String s3ObjectKey = storedFile.getUniqueStorageIdentifier();
		try {
			log.info("removing file from S3 bucket " + bucketName + " key:" + s3ObjectKey);
			this.s3Service.deleteObject(bucketName, s3ObjectKey);
		} catch (S3ServiceException e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	public boolean saveFile(Lab lab, StoredFile storedFile,
			byte[] contents) {
		this.initialize();
		String bucketName = lab.getStorageIdentity(); //lab2.labia.com
		//populdate the storage specific identifier
		String storageId = "/" + storedFile.getFileType() + "/" + storedFile.getId();
		storedFile.setUniqueStorageIdentifier(storageId);
		storedFile.setFullPath(storageId);
		storedFile.setFullUrl(storageId);
		String s3ObjectKey = storedFile.getUniqueStorageIdentifier();
		log.info("trying to upload file to bucket " + bucketName + " key:" + s3ObjectKey);
		try{
			ByteArrayInputStream dataIS = new ByteArrayInputStream(contents);
			byte[] md5Hash = ServiceUtils.computeMD5Hash(dataIS);
			dataIS.reset();   
			// Create an object containing a greeting string as input stream data.
			S3Bucket bucket = this.s3Service.getBucket(bucketName);
			S3Object fileObject = new S3Object(s3ObjectKey);
			
			fileObject.setDataInputStream(dataIS);
			fileObject.setContentLength(dataIS.available());
			//		helloWorldObject.setContentType("text/plain");

			fileObject.setMd5Hash(md5Hash);  

			// Upload the data objects.
			S3Object result = s3Service.putObject(bucket, fileObject);
			log.info("result:" + result);
		return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	public InputStream downloadFile(Lab lab, StoredFile storedFile){
		this.initialize();
		try{
			S3Bucket bucket = this.s3Service.getBucket(lab.getStorageIdentity());
			S3Object s3Obj = this.s3Service.getObject(bucket, storedFile.getUniqueStorageIdentifier());
			if(s3Obj==null){
				throw new RuntimeException("Could not locate the s3object with " + lab.getStorageIdentity() + " and " + storedFile.getUniqueStorageIdentifier());
			}
			log.info("downloading from bucket:" + bucket.getName() + " key:" + s3Obj.getKey());
			return s3Obj.getDataInputStream();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	public void listBuckets(Lab lab){
		boolean existing=false;
		S3Bucket[] buckets;
		this.initialize();
		try {
			buckets = this.s3Service.listAllBuckets();
			if(buckets!=null && buckets.length>0){
				for(S3Bucket b:buckets){
					System.out.println("name:"+ b.getName() + " location:" + b.getLocation() + " " + b.getOwner().getDisplayName());
				}
			}else{
				
			}
		} catch (S3ServiceException e) {
			e.printStackTrace();
		}
		
	}
	public boolean createBucket(Lab lab) {
		this.initialize();
		String bucketName = lab.getName() + "." + this.bucketNameUniqueSuffix;
		try {
			boolean existing=false;
			S3Bucket[] buckets = this.s3Service.listAllBuckets();
			if(buckets!=null && buckets.length>0){
				for(S3Bucket b:buckets){
					if(b.getName().equalsIgnoreCase(bucketName)){
						existing=true;
						break;
					}
				}
			}
			if(!existing){
				this.s3Service.createBucket(bucketName);
			}
		} catch (S3ServiceException e) {
			throw new RuntimeException(e);
		}
		lab.setStorageIdentity(bucketName);
		return true;
	}

	public void removeBucket(Lab lab) {
		this.initialize();
		try {
			this.s3Service.deleteBucket(lab.getStorageIdentity());
		} catch (S3ServiceException e) {
			throw new RuntimeException(e);
		}
	}

	public void setAwsAccessKey(String awsKey) {
		this.awsAccessKey = awsKey;
	}

	public void setAwsSecretKey(String awsSecretKey) {
		this.awsSecretKey = awsSecretKey;
	}
	public void setBucketNameUniqueSuffix(String bucketNameUniqueSuffix) {
		this.bucketNameUniqueSuffix = bucketNameUniqueSuffix;
	}
	
	public boolean saveFile(Lab lab, StoredFile storedFile, ByteArrayInputStream is) {
		this.initialize();
		String bucketName = lab.getStorageIdentity(); //lab2.labia.com
		//populdate the storage specific identifier
		String storageId = "/" + storedFile.getFileType() + "/" + storedFile.getId();
		storedFile.setUniqueStorageIdentifier(storageId);
		storedFile.setFullPath(storageId);
		storedFile.setFullUrl(storageId);
		String s3ObjectKey = storedFile.getUniqueStorageIdentifier();
		try{
			byte[] md5Hash = ServiceUtils.computeMD5Hash(is);
			
			is.reset();   
			
			// Create an object containing a greeting string as input stream data.
			S3Bucket bucket = this.s3Service.getBucket(bucketName);
			S3Object fileObject = new S3Object(s3ObjectKey);
			fileObject.setDataInputStream(is);
			fileObject.setContentLength(is.available());
			fileObject.setMd5Hash(md5Hash);  
			//		helloWorldObject.setContentType("text/plain");

			// Upload the data objects.
			s3Service.putObject(bucket, fileObject);
		return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	

	public void storeFile(Lab lab, StoredFile storedFile, byte[] contents) {
		//first try to save
		if(!this.saveFile(lab, storedFile, contents)){
			throw new RuntimeException("failed to save file " + storedFile.getFileName() + " for lab " +lab.getName());
		}
	}

	public void prepareStorageForLab(Lab lab) {
		this.createBucket(lab);
	}

	public void removeStorageForLab(Lab lab) {
		this.removeBucket(lab);
	}

	public void storeFile(Lab lab, StoredFile storedFile, ByteArrayInputStream is) {
		//first try to save
		if(!this.saveFile(lab, storedFile, is)){
			throw new RuntimeException("failed to save file " + storedFile.getFileName() + " for lab " +lab.getName());
		}
	}


}
