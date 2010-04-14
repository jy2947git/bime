package com.focaplo.myfuse.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.LabMeeting;
import com.focaplo.myfuse.model.MeetingFile;

public class LabManagerTest extends BaseManagerTestCase {
	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;
	
	@Test
	@Transactional
	public void uploadFile() throws IOException{
		//create new lab
		Lab lab = new Lab();
		lab.setName("test999");
		this.labManager.saveLab(lab);
		log.info("saved lab id " + lab.getId());
		this.labManager.setupStorageForLab(lab);
		//
		ByteArrayInputStream is = new ByteArrayInputStream(this.getBytesFromFile(new File("/Users/jy2947/Downloads/Document.pdf")));
		//create meeting
		LabMeeting meeting = new LabMeeting();
		meeting.setSubject("test amazon");
		this.labManager.saveMeeting(meeting);
		log.info("saved meeting id:" + meeting.getId());
		//add meeting file
		MeetingFile meetingFile = new MeetingFile();
		meetingFile.setFileName("offer.doc");
		meetingFile.setMeeting(meeting);
		meetingFile.setUniqueStorageIdentifier("junk");
		this.labManager.uploadMeetingFile(lab, meetingFile, is);
		is.close();
		log.info("meeting file id:" + meetingFile.getId());
		//check data
		JdbcTemplate template = new JdbcTemplate(this.dataSource);
		Map<String, Object> map = template.queryForMap("select * from stored_file where id = " + meetingFile.getId());
		System.out.println("map:" + map);
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
