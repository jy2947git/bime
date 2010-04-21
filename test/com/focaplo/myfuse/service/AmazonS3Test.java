package com.focaplo.myfuse.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.MeetingFile;
import com.focaplo.myfuse.model.StoredFile;
import com.focaplo.myfuse.service.impl.AmazonStorageManager;


public class AmazonS3Test extends BaseManagerTestCase{
	private AmazonStorageManager s3controller;
	
	public void setS3controller(AmazonStorageManager s3controller) {
		this.s3controller = s3controller;
	}

	@Test
	public void listExistingBuckets(){
		Lab lab = new Lab();
		lab.setName("testLab");
		this.s3controller.listBuckets(lab);
	}
	
	@Test
	public void createBucket(){
		Lab lab = new Lab();
		lab.setName("testLab");
		this.s3controller.createBucket(lab);
	}
	
	@Test
	public void uploadFile() throws IOException{
		Lab lab = new Lab();
		lab.setName("testLab");
		lab.setStorageIdentity("testLab.focaplo.com");
		StoredFile storedFile = new MeetingFile();
		storedFile.setId(new Long(128));
		this.s3controller.saveFile(lab, storedFile, this.getBytesFromFile(new File("/Users/jy2947/Downloads/Document.pdf")));
	}
	
	@Test
	public void uploadFileStream() throws IOException{
		Lab lab = new Lab();
		lab.setName("testLab");
		lab.setStorageIdentity("testLab.focaplo.com");
		StoredFile storedFile = new MeetingFile();
		storedFile.setId(new Long(130));
		ByteArrayInputStream is = new ByteArrayInputStream(this.getBytesFromFile(new File("/Users/jy2947/Downloads/Document.pdf")));
		this.s3controller.saveFile(lab, storedFile, is);
		is.close();
	}
	@Test
	public void downloadFile(){
		Lab lab = new Lab();
		lab.setName("testLab");
		lab.setStorageIdentity("lab11.focaplo.com");
		StoredFile storedFile = new StoredFile();
		storedFile.setUniqueStorageIdentifier("/meeting_file/16000007");
		InputStream is = this.s3controller.downloadFile(lab, storedFile);
		if(is!=null){
			FileOutputStream fos = null;
			try{
				fos = new FileOutputStream("/tmp/download.doc");
				byte buf[]=new byte[1024];
			    int len;
			    while((len=is.read(buf))>0){
			    	fos.write(buf,0,len);
			    }
		
			    
			
			}catch(Exception e){
				throw new RuntimeException(e);
			}finally{
			    try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			    try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	@Test
	public void removeFile(){
		Lab lab = new Lab();
		lab.setName("testLab");
		lab.setStorageIdentity("testLab.focaplo.com");
		StoredFile storedFile = new StoredFile();
		storedFile.setUniqueStorageIdentifier("/meeting-file/123");
		this.s3controller.removeFile(lab, storedFile);
	}
	
    // Returns the contents of the file in a byte array.
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
    
        // Get the size of the file
        long length = file.length();
    
        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
 
}
