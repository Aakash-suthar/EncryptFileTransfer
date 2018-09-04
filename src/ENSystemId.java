import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ENSystemId {
  private ENSystemId() {  }

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
public static String getSerialNumber(String drive) {
  String result = "";
    try {
      File file = File.createTempFile("realhowto",".vbs");
      file.deleteOnExit();
      FileWriter fw = new java.io.FileWriter(file);

      String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                  +"Set colDrives = objFSO.Drives\n"
                  +"Set objDrive = colDrives.item(\"" + drive + "\")\n"
                  +"Wscript.Echo objDrive.SerialNumber";  // see note
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
  public static String MD5(String md5) 
    {
  try 
     {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        byte[] array = md.digest(md5.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) 
      {
        sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
      }
        return sb.toString();
      } 
   catch (java.security.NoSuchAlgorithmException e) 
    {
    }
    return null;
   }
  public static void main(String[] args){
    String cpuId = ENSystemId.getMotherboardSN();
    String sn = ENSystemId.getSerialNumber("C");
    String sysId=cpuId+sn;
    String ensysid=ENSystemId.MD5(sysId);
    System.out.println("Motherboard serial number:"+cpuId);
    System.out.println("Serial Number of C:"+sn);
    System.out.println("System Identification number:"+sysId);
    System.out.println("Encrypted System Identification number:"+ensysid);
    javax.swing.JOptionPane.showConfirmDialog((java.awt.Component)
         null, cpuId, "Motherboard serial number",
         javax.swing.JOptionPane.DEFAULT_OPTION);
    javax.swing.JOptionPane.showConfirmDialog((java.awt.Component)
         null, sn, "Serial Number of C:",
         javax.swing.JOptionPane.DEFAULT_OPTION);
    javax.swing.JOptionPane.showConfirmDialog((java.awt.Component)
         null, sysId, "System Identification number:",
         javax.swing.JOptionPane.DEFAULT_OPTION);
    javax.swing.JOptionPane.showConfirmDialog((java.awt.Component)
         null, ensysid, "Encrypted System Identification number:",
         javax.swing.JOptionPane.DEFAULT_OPTION);
  }
}