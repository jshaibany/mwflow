package com.example.one_mw.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncryptionService {

	
	public static String encrypt(String input) {
		
		
	 
	    try {
	    
			String cipherText = SecurityService.encrypt(input);
			
			return cipherText;
			
		} catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException
				| InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
			
			e.printStackTrace();
			
			return null;
		} 
	}
}
