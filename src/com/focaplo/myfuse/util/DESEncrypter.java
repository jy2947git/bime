package com.focaplo.myfuse.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.focaplo.myfuse.service.EncryptionService;

public class DESEncrypter implements EncryptionService{
	protected final Log log = LogFactory.getLog(getClass());


	public OutputStream getDecryptedOutputStream(String password,
			OutputStream out) {
		if(password==null || password.equalsIgnoreCase("")){
			throw new RuntimeException("Secret key is required to encrypt/decrpt");
		}
		byte[] iv = password.getBytes();
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
		try {
			SecretKey key = KeyGenerator.getInstance("DES").generateKey(); 
		
			Cipher dcipher = Cipher.getInstance("DES/CBC/PKCS5Padding"); 
		
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			return new CipherOutputStream(out, dcipher); 
		}catch (Exception e) {
			throw new RuntimeException("failed to encrypt input stream", e);
		} 
	
	}

	public OutputStream getEncryptedOutputStream(String password, OutputStream out) {
		if(password==null || password.equalsIgnoreCase("")){
			throw new RuntimeException("Secret key is required to encrypt/decrpt");
		}
		byte[] iv = password.getBytes();
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
		try {
			SecretKey key = KeyGenerator.getInstance("DES").generateKey(); 
			Cipher ecipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
		
			// Bytes written to out will be encrypted
			return  new CipherOutputStream(out, ecipher); 
		}catch (Exception e) {
			throw new RuntimeException("failed to encrypt input stream", e);
		} 
	}

	public InputStream getDecryptedInputStream(String password, InputStream in) {
		if(password==null || password.equalsIgnoreCase("")){
			throw new RuntimeException("Secret key is required to encrypt/decrpt");
		}
		byte[] iv = password.getBytes();
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
		try {
			SecretKey key = KeyGenerator.getInstance("DES").generateKey(); 
		
			Cipher dcipher = Cipher.getInstance("DES/CBC/PKCS5Padding"); 
		
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			return new CipherInputStream(in, dcipher); 
		}catch (Exception e) {
			throw new RuntimeException("failed to encrypt input stream", e);
		} 
	}

	public InputStream getEncryptedInputStream(String password, InputStream in) {
		if(password==null || password.equalsIgnoreCase("")){
			throw new RuntimeException("Secret key is required to encrypt/decrpt");
		}
		byte[] iv = password.getBytes();
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
		try {
			SecretKey key = KeyGenerator.getInstance("DES").generateKey(); 
			Cipher ecipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
		
			// Bytes written to out will be encrypted
			return  new CipherInputStream(in, ecipher); 
		}catch (Exception e) {
			throw new RuntimeException("failed to encrypt input stream", e);
		} 
	}

	public String decrypt(String input, String password) {
		return null;
	}

	public String encrypt(String input, String password) {
		return null;
	}
}
