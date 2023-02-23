package com.example.one_mw.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class DecryptionService {

	
	
	public static String decrypt(String input) {
		
		
	 
	    try {
	    	
			String plainText = SecurityService.decrypt(input);
			
			return plainText;
			
		} catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException 
				| InvalidKeySpecException | UnsupportedEncodingException e) {
			
			e.printStackTrace();
			
			return null;
		}
	}
}
