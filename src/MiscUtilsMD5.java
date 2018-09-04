import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MiscUtilsMD5 {
  private MiscUtilsMD5() {  }

  public static String getMotherboardSN() {
  String result = "";
    try {
      File file = File.createTempFile("realhowto",".vbs");
      file.deleteOnExit();
      FileWriter fw = new java.io.FileWriter(file);

      String vbs =
         "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
        + "Set colItems = objWMIService.ExecQuery _ \n"
        + "   (\"Select * from Win32_BaseBoard\") \n"
        + "For Each objItem in colItems \n"
        + "    Wscript.Echo objItem.SerialNumber \n"
        + "    exit for  ' do the first cpu only! \n"
        + "Next \n";

      fw.write(vbs);
      fw.close();
      Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
      BufferedReader input =
        new BufferedReader
          (new InputStreamReader(p.getInputStream()));
      String line;
      while ((line = input.readLine()) != null) {
         result += line;
      }
      input.close();
    }
    catch(Exception e){
        e.printStackTrace();
    }
    return result.trim();
  }
public static String MD5(String md5) {
try {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        byte[] array = md.digest(md5.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
        sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
    }
        return sb.toString();
    } catch (java.security.NoSuchAlgorithmException e) {
    }
    return null;
}

  public static void main(String[] args){
    String cpuId = MiscUtilsMD5.getMotherboardSN();
    String encpuid=MiscUtilsMD5.MD5(cpuId);
    System.out.println("Motherboard serial number:"+cpuId);
    System.out.println("Motherboard serial number:"+encpuid);
    javax.swing.JOptionPane.showConfirmDialog((java.awt.Component)
         null, cpuId, "Motherboard serial number",
         javax.swing.JOptionPane.DEFAULT_OPTION);
    javax.swing.JOptionPane.showConfirmDialog((java.awt.Component)
         null, encpuid, "Motherboard serial number",
         javax.swing.JOptionPane.DEFAULT_OPTION);
  }
}