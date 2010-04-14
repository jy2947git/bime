package com.focaplo.myfuse.service;

import org.junit.Test;

public class EncrypterTest extends BaseManagerTestCase {

	@Test
	public void encryptString(){
		String s = "this is a testing 12345";
		System.out.println(this.encrypter.encrypt(s, "gopass202"));
	}
	
	@Test
	public void decryptString(){
		String d = "2e4723e2934a0da034f61e617838ee47aa5802d48528d128ee7a5e533a112438";
		System.out.println(this.encrypter.decrypt(d, "gopass202"));
	}
}
