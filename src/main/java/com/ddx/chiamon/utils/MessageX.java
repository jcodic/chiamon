package com.ddx.chiamon.utils;

import java.security.MessageDigest;
import java.util.*;
import java.io.*;
import java.util.zip.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author SergeyZYX
 */
public class MessageX {

    private static final String BASE_SK = "lakjdaljd143yJ1hH";
    private static final String BASE_IV = "1ttg736F";
    
    public static byte[] compress(byte[] data) throws IOException {
	
	Deflater deflater = new Deflater();
	deflater.setInput(data);
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
	deflater.finish();
	byte[] buffer = new byte[1024];
	while (!deflater.finished()) {
	    int count = deflater.deflate(buffer);
	    outputStream.write(buffer, 0, count);
	}
	outputStream.close();
	byte[] output = outputStream.toByteArray();
	return output;
    }

    public static byte[] decompress(byte[] data) throws IOException, DataFormatException {
	
	Inflater inflater = new Inflater();
	inflater.setInput(data);
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
	byte[] buffer = new byte[1024];
	while (!inflater.finished()) {
	    int count = inflater.inflate(buffer);
	    outputStream.write(buffer, 0, count);
	}
	outputStream.close();
	byte[] output = outputStream.toByteArray();
	return output;
    }
    
    private static SecretKeySpec getSecretKeySpec(String secretKey) throws Exception {
	
	MessageDigest sha = MessageDigest.getInstance("SHA-256");
	byte[] key = sha.digest(secretKey.getBytes("UTF-8"));
	key = Arrays.copyOf(key, 24);
	return new SecretKeySpec(key, "DESede");
    }

    public static String encrypt(byte[] input, String secretKey, String initVector) throws Exception {
	
	IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
	Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
	SecretKeySpec keySpec = getSecretKeySpec(secretKey);
	cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
	return Base64.getEncoder().encodeToString(cipher.doFinal(input));
    }

    public static String encryptString(String strToEncrypt) throws Exception {
        
        return encryptString(strToEncrypt, BASE_SK, BASE_IV);
    }
    
    public static String encryptString(String strToEncrypt, String secretKey, String initVector) throws Exception {
	
	return encryptString(strToEncrypt, secretKey, initVector, "UTF-8");
    }
    
    public static String encryptString(String strToEncrypt, String secretKey, String initVector, String charset) throws Exception {
	
	return encrypt(strToEncrypt.getBytes(charset), secretKey, initVector);
    }    
    
    public static String encryptFile(String filePath, String secretKey, String initVector) throws Exception {
	
	File file = new File(filePath);
	if (!file.exists()) throw new RuntimeException("File ["+filePath+"] not found.");
	
	int maxSizeKb = 256;
	
	int len = (int)file.length();
	if (len > maxSizeKb*1024) throw new RuntimeException("File size exceed "+maxSizeKb+"Kb, too large.");
	
	byte[] bf = new byte[len];
	    
	InputStream stream = new FileInputStream(file);
	int loaded = stream.read(bf);
	stream.close();
	
	if (loaded != len) throw new RuntimeException("File is not loaded properly.");
	
	return encrypt(compress(bf), secretKey, initVector);
    }    

    public static byte[] decrypt(String strToDecrypt, String secretKey, String initVector) throws Exception {
	
	IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
	Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
	SecretKeySpec keySpec = getSecretKeySpec(secretKey);
	cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
	return cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));
    }

    public static String decryptString(String strToDecrypt) throws Exception {
        
        return decryptString(strToDecrypt, BASE_SK, BASE_IV);
    }
    
    public static String decryptString(String strToDecrypt, String secretKey, String initVector) throws Exception {

	return decryptString(strToDecrypt, secretKey, initVector, "UTF-8");
    }
    
    public static String decryptString(String strToDecrypt, String secretKey, String initVector, String charset) throws Exception {
	
	return new String(decrypt(strToDecrypt, secretKey, initVector), charset);
    }

    public static void decryptFile(String filePath, String strToDecrypt, String secretKey, String initVector) throws Exception {
	
	File file = new File(filePath);
	if (file.exists() && !file.delete()) throw new RuntimeException("File exists & can't be rewriten.");
	
	byte[] bf = decrypt(strToDecrypt, secretKey, initVector);
	
	OutputStream stream = new FileOutputStream(file);
	
	stream.write(decompress(bf));
	stream.flush();
	stream.close();
    }    

}