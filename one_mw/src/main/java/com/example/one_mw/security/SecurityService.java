package com.example.one_mw.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class SecurityService {

	private static final String algorithm = "AES/CBC/PKCS5Padding";
	private static final String initVector = "yakosais@thebest";
	
	public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
	    
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
	    keyGenerator.init(n);
	    SecretKey key = keyGenerator.generateKey();
	    return key;
	}
	
	private static SecretKey getKeyFromPassword()
		    throws NoSuchAlgorithmException, InvalidKeySpecException {
		    
		String password = "x@Ytomakeitsafe";
	    String salt = "100967244336688514";
	    
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
		SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
		        .getEncoded(), "AES");
		    
		return secret;
		
	}
	
	private static IvParameterSpec generateIv() throws UnsupportedEncodingException {
	    
	    return new IvParameterSpec(initVector.getBytes("UTF-8"));
	}
	
	public static String encrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException,
		    InvalidAlgorithmParameterException, InvalidKeyException,
		    BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, UnsupportedEncodingException {
		    
		    
		Cipher cipher = Cipher.getInstance(algorithm);	    
		cipher.init(Cipher.ENCRYPT_MODE, getKeyFromPassword(), generateIv());
		    
		byte[] cipherText = cipher.doFinal(input.getBytes());
		    
		return Base64.getEncoder().encodeToString(cipherText);
		
	}
	
	public static String decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException,
		    InvalidAlgorithmParameterException, InvalidKeyException,
		    BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, UnsupportedEncodingException {
		    
		    
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassword(), generateIv());
		    
		byte[] plainText = cipher.doFinal(Base64.getDecoder()
		        .decode(cipherText));
		    
		return new String(plainText);
		
	}
}
