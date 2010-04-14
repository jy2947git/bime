package com.focaplo.myfuse.util;

import java.util.Random;
 
public class RandomPassword {
 
    private static final String charset = "!0123456789abcdefghijklmnopqrstuvwxyz";
 
    public static String getRandomString(int length) {
        Random rand = new Random(System.currentTimeMillis());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int pos = rand.nextInt(charset.length());
            sb.append(charset.charAt(pos));
        }
        return sb.toString();
    }
 
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
 
//            System.out.println(RandomStringUtils.random(10));
 
            try {
                // if you generate more than 1 time, you must
                // put the process to sleep for awhile
                // otherwise it will return the same random string
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
 
}