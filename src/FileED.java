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
   
	
}