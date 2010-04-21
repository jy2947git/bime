package com.focaplo.myfuse.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.focaplo.myfuse.service.EncryptionService;

public class AESEncrypter implements EncryptionService{
	protected final Log log = LogFactory.getLog(getClass());
	private byte[] salt = new byte[]{
		(byte)0x28,(byte)0x94,(byte)0x95,(byte)0x75,
		(byte)0x16,(byte)0x76,(byte)0x64,(byte)0x82,
		(byte)0x34,(byte)0x54,(byte)0x86,(byte)0x64,
		(byte)0x24,(byte)0x67,(byte)0x93,(byte)0x19};
	
	private SecretKey getSecretKey(String password, byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException{
//		SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");  
//		byte[] salt = new byte[16];  
//		rand.nextBytes(salt);  
		PBEKeySpec pw = new PBEKeySpec(password.toCharArray(), salt, 1000, 128); 
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");  
		PBEKey key = (PBEKey) factory.generateSecret(pw);  
		SecretKey encKey = new SecretKeySpec(key.getEncoded(), "AES");
		return encKey;
	}
	
	private Cipher getEncryptCipher(String password, byte[] salt){
		if(password==null || password.equalsIgnoreCase("")){
			throw new RuntimeException("Secret key is required to encrypt/decrpt");
		}

		try {
			SecretKey key = this.getSecretKey(password, salt); 
			byte[] raw = key.getEncoded();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES"); 
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			return cipher; 
		}catch (Exception e) {
			throw new RuntimeException("failed to encrypt input stream", e);
		} 
	}
	
	private Cipher getDecryptCipher(String password, byte[] salt){
		if(password==null || password.equalsIgnoreCase("")){
			throw new RuntimeException("Secret key is required to encrypt/decrpt");
		}

		try {
			SecretKey key = this.getSecretKey(password, salt); 
			byte[] raw = key.getEncoded();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES"); 
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			return cipher; 
		}catch (Exception e) {
			throw new RuntimeException("failed to encrypt input stream", e);
		} 
	}
	
	public OutputStream getDecryptedOutputStream(String password,
			OutputStream out) {

		try {
			
			Cipher dcipher = this.getDecryptCipher(password, salt);
			return new CipherOutputStream(out, dcipher); 
		}catch (Exception e) {
			throw new RuntimeException("failed to encrypt input stream", e);
		} 
	
	}

	public OutputStream getEncryptedOutputStream(String password, OutputStream out) {
		
		try {
			Cipher ecipher = this.getEncryptCipher(password, salt);
		
			// Bytes written to out will be encrypted
			return  new CipherOutputStream(out, ecipher); 
		}catch (Exception e) {
			throw new RuntimeException("failed to encrypt input stream", e);
		} 
	}

	public InputStream getDecryptedInputStream(String password, InputStream in) {
		
		try {
			Cipher dcipher = this.getDecryptCipher(password, salt);
			return new CipherInputStream(in, dcipher); 
		}catch (Exception e) {
			throw new RuntimeException("failed to encrypt input stream", e);
		} 
	}

	public InputStream getEncryptedInputStream(String password, InputStream in) {
		
		try {
			Cipher ecipher = this.getEncryptCipher(password, salt);
			// Bytes written to out will be encrypted
			return  new CipherInputStream(in, ecipher); 
		}catch (Exception e) {
			throw new RuntimeException("failed to encrypt input stream", e);
		} 
	}
	
	public String encrypt(String input, String password){
		try{
			Cipher ecipher = this.getEncryptCipher(password, salt);
			byte[] encrypted = ecipher.doFinal(input.getBytes());
			return this.asHex(encrypted);
		}catch (Exception e) {
			throw new RuntimeException("failed to encrypt input stream", e);
		} 
	}
	
	public String decrypt(String input, String password){
		try{
			Cipher dcipher = this.getDecryptCipher(password, salt);
			byte[] decrypted = dcipher.doFinal(this.asBytes(input));
			return new String(decrypted);
		}catch (Exception e) {
			throw new RuntimeException("failed to decrypt input stream", e);
		} 
	}
	
    private String asHex (byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
         if (((int) buf[i] & 0xff) < 0x10)
  	    strbuf.append("0");

         strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }

        return strbuf.toString();
       }

    private byte[] asBytes(String hex){
    	byte[] bts = new byte[hex.length()/2];
    	for(int i=0;i<bts.length;i++){
    		bts[i] = ((byte) Integer.parseInt(hex.substring(2*i, 2*i+2), 16));
    	}
    	return bts;
    }
}
