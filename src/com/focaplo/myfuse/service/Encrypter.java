package com.focaplo.myfuse.service;

import java.io.InputStream;
import java.io.OutputStream;

public interface Encrypter {

	public OutputStream getEncryptedOutputStream(String password, OutputStream out);

	public OutputStream getDecryptedOutputStream(String password, OutputStream out);
	
	public InputStream getEncryptedInputStream(String password, InputStream in);

	public InputStream getDecryptedInputStream(String password, InputStream in);
	
	public String encrypt(String input, String password);
	
	public String decrypt(String input, String password);
	
}
