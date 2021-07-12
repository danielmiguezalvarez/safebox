package com.safeish.item.entity;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class ItemListener {
	
	private static final String KEY = "Bar12345Bar12345";
	private static final String algorithm  = "AES"; 
	
    @PrePersist
    @PreUpdate
	public void encodingItem(Item item) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {   	
    	item.setName(encrypt(item.getName()));    	
    }
    
    @PostLoad
	public void decodingItem(Item item) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {   	
    	item.setName(decrypt(item.getName()));
    }

	private String encrypt(String string) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		var cipher = getCipher(Cipher.ENCRYPT_MODE);
		return Base64.getEncoder().encodeToString(cipher.doFinal(string.getBytes()));
	}
	
	private String decrypt(String string) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		var cipher = getCipher(Cipher.DECRYPT_MODE);
		return new String(cipher.doFinal(Base64.getDecoder().decode(string)));
	}

	private Cipher getCipher(int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Key aesKey = new SecretKeySpec(KEY.getBytes(), algorithm);
		var cipher=Cipher.getInstance(algorithm);		
		cipher.init(mode, aesKey);
		
		return cipher;
	}
		
	
}
