import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FileED {

// static int i = 0;
// static  IvParameterSpec ivspec;
// static SecureRandom srandom;
// static Cipher cipher;
	
   public static byte[] byteProcessor(int cipherMode,String key,byte[] inputBytes){
		 try {
			 Key secretKey = new SecretKeySpec(key.getBytes(),"AES");
	            Cipher cipher = Cipher.getInstance("AES");
	            cipher.init(cipherMode, secretKey);
	          
	            
	          //// FileInputStream inputStream = new FileInputStream(inputFile);
	           // byte[] inputBytes = new byte[(int) inputFile.length()];
	          //  inputStream.read(inputBytes);
	             
	         //   byte[] outputBytes = cipher.doFinal(inputBytes);
	             
	         //   FileOutputStream outputStream = new FileOutputStream(outputfile);
	           //  outputStream.write(outputBytes);
	             
	         //   inputStream.close();
	         //   outputStream.close();
	           
	            
	            return  cipher.doFinal(inputBytes);
	            

		    } catch (NoSuchPaddingException | NoSuchAlgorithmException 
	                     | InvalidKeyException | BadPaddingException
		             | IllegalBlockSizeException e) {
			e.printStackTrace();
			return null;
	            }
	     }
   
   public static File fileed(int cipherMode,String key,File inputFile,File outputfile){
		 try {
			  Key secretKey = new SecretKeySpec(key.getBytes(),"AES");
	            Cipher cipher = Cipher.getInstance("AES");
	            cipher.init(cipherMode, secretKey);
	             
	            FileInputStream inputStream = new FileInputStream(inputFile);
	            byte[] inputBytes = new byte[(int) inputFile.length()];
	            inputStream.read(inputBytes);
	             
	            byte[] outputBytes = cipher.doFinal(inputBytes);
	             
	            FileOutputStream outputStream = new FileOutputStream(outputfile,false);
	            outputStream.write(outputBytes);

	           // byte[] buf = new byte[1024];

//	            int count = inputStream.read(buf);
//	            while (count >= 0) {
//	            	outputStream.write(cipher.update(buf, 0, count)); // HERE I WAS DOING doFinal() method
//	                count = inputStream.read(buf);
//	            }
//	            outputStream.write(cipher.doFinal()); // AND I DID NOT HAD THIS LINE BEFORE
//	            outputStream.flush();
	            
	            
	            inputStream.close();
	            outputStream.close();
	            
	            return outputfile;
	            

		    } catch (NoSuchPaddingException | NoSuchAlgorithmException 
	                     | InvalidKeyException | BadPaddingException
		             | IllegalBlockSizeException | IOException e) {
			e.printStackTrace();
			return null;
	            }
	     }
   
   public static void processFile(int ciphermode,String key,File inFile,File outFile)
		    {
	   
	   try {
			 	Key secretKey = new SecretKeySpec(key.getBytes(),"AES");
			 	  Cipher cipher = Cipher.getInstance("AES");
			 	cipher.init(ciphermode,secretKey);
		        try (FileInputStream in = new FileInputStream(inFile);
		             FileOutputStream out = new FileOutputStream(outFile)) {

		            byte[] ibuf = new byte[1024];
		            int len;
		            while ((len = in.read(ibuf)) != -1) {
		                byte[] obuf = cipher.update(ibuf, 0, len);
		                if ( obuf != null ) out.write(obuf);
		            }
		            byte[] obuf = cipher.doFinal();
		            if ( obuf != null ) out.write(obuf);
		        }
	   		}
		    catch(IllegalBlockSizeException | BadPaddingException |IOException | InvalidKeyException| 
		    		NoSuchAlgorithmException | NoSuchPaddingException e) {
		    	e.printStackTrace();
		    }
		    
		    }
   
//	   public static  void construct() {
//		   
//		   if(i==0) {
//			 try {  
//			   byte[] iv = new byte[128/8];
//			   srandom.nextBytes(iv);
//			   ivspec = new IvParameterSpec(iv);
//			  
////			   String ivFile = "IVfile.txt";
////			   try (FileOutputStream out = new FileOutputStream(ivFile)) {
////			       out.write(iv);
////			   }
//			 }
//			 catch(Exception e) {e.getMessage();}
//
//		   }
//		   
//		   i++;
//	   }
	   
//	   public static void entry(int i,String key,String infile,String outfile) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
//		   
//		   cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//  			SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(),"AES");
//        	cipher.init(Cipher.DECRYPT_MODE,secretKey,ivspec);
//		   
//	   }



	
}