package com.innon.education.sso;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.util.Base64;


public class CipherDec {
	
	
//	public static void main(String[] args) throws Exception {
//		String KEY = "HCKOLMARKEY";
//
//        String text = "health.user";
//        String encryptStr = "Lmd7g5CAPRI+zTaZ+i/odw==";
//
//        String decStr = decrypt(encryptStr, KEY);
//
//        System.out.println("\nOrigin key: " + KEY);
//        System.out.println("\nOrigin text: " + text);
//
//        System.out.println("\nEncrypted text: " + encryptStr);
//        System.out.println("\nDecrypted text: " + decStr);
//    }
	
	
	/**
    * 복호화
     */
    public static String decrypt(String encStr, String keyValue) throws Exception {
        if (encStr == null || encStr.length() == 0) {
            return "";
        }
        
        final Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, getKey(keyValue));
        


        final byte[] inputBytes = Base64.getDecoder().decode(encStr);
        final byte[] outputBytes = cipher.doFinal(inputBytes);

        String decStr = new String(outputBytes, "UTF8");
        return decStr;
    }


    private static Key getKey(String keyValue) throws Exception {
    	
    	final MessageDigest md = MessageDigest.getInstance("md5");
		final byte[] digestOfPassword = md.digest(keyValue.getBytes("utf-8"));

		byte[] keyBytes = new byte[24];
		System.arraycopy(digestOfPassword, 0, keyBytes, 0, digestOfPassword.length);
		for (int j = 0, k = 16; j < 8; k++, j++) {
			keyBytes[k] = keyBytes[j];
		}
    	
        DESedeKeySpec desKeySpec = new DESedeKeySpec(keyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        
        Key key = keyFactory.generateSecret(desKeySpec);
        return key;
    }
    
        
    private static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return null;

        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }
    
    /**
     * 쿠키 값 가져오기
     * @param request
     * @param name
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie  = getCookie(request,name);
        if( cookie != null ) {
            try {
                return URLDecoder.decode(cookie.getValue(),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }


   
}

