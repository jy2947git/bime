package com.focaplo.myfuse.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.StoredFile;

public interface StorageService {

	/**
	 * store the input byte stream. This method will update the storage-identifier, fullpath
	 * and full-url of the stored-file object after the content is stored.
	 * @param lab
	 * @param storedFile StoredFile object, must be already existing in database.
	 * @param is
	 */
	public void storeFile(Lab lab, StoredFile storedFile, ByteArrayInputStream is);
	
	/**
	 * store the input byte array. This method will update the storage-identifier, full-path
	 * and full-url of the stored-file object after the content is stored
	 * @param lab
	 * @param storedFile StoredFile object, must be already existing in database.
	 * @param contents
	 */
	public void storeFile(Lab lab, StoredFile storedFile, byte[] contents);
	
	/**
	 * remove file physically from the storage
	 * @param lab
	 * @param storedFile
	 * @return
	 */
	public boolean removeFile(Lab lab, StoredFile storedFile);
	
	/**
	 * prepare the root storage for the lab
	 * @param lab
	 */
	public void prepareStorageForLab(Lab lab);
	
	/**
	 * remove all the storage for the lab, including all the files
	 * @param lab
	 */
	public void removeStorageForLab(Lab lab);
	
	/**
	 * Get input stream from the stored file.
	 * @param lab
	 * @param storedFile
	 * @return
	 */
	public InputStream downloadFile(Lab lab, StoredFile storedFile);
}
