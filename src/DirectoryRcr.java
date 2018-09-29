import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.security.Security;
import javax.crypto.Cipher;

public class DirectoryRcr {

    String request = "May I send?";
    String respServer = "Yes,You can";
    String dirResponse = "Directory created...Please send files";
    String dirFailedResponse = "Failed";
    String fileHeaderRecvd = "File header received ...Send File";
    String fileReceived = "File Received";
    SSLSocket socket = null;
    OutputStream ioStream = null;
    InputStream inStream = null;
    boolean isLive = false;
    int state = 0;
    final int initialState = 0;
    final int dirHeaderWait = 1;
    final int dirWaitState = 2;
    final int fileHeaderWaitState = 3;
    final int fileContentWaitState = 4;
    final int fileReceiveState = 5;
    final int fileReceivedState = 6;
    final int finalState = 7;
    byte[] readBuffer = new byte[1024 * 100000];
    long fileSize = 0;
    String dir = "";
    FileOutputStream foStream = null;
    int fileCount = 0;
    File dstFile = null;
    
  //start
    String key =null;
    //end

    public DirectoryRcr(String key1) {
    	key = key1;
        acceptConnection();
    }

    private void acceptConnection() {
    	 System.setProperty("javax.net.ssl.keyStore","myKeyStore.jks");
         //specifing the password of the keystore file
         System.setProperty("javax.net.ssl.keyStorePassword","akashakash");
         //This optional and it is just to show the dump of the details of the handshake process 
         System.setProperty("javax.net.debug","all");
        try {
        	 //SSLServerSocketFactory establishes the ssl context and and creates SSLServerSocket 
            SSLServerSocketFactory sslServerSocketfactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
            //Create SSLServerSocket using SSLServerSocketFactory established ssl context
            SSLServerSocket sslServerSocket = (SSLServerSocket)sslServerSocketfactory.createServerSocket(4001);
            System.out.println("Echo Server Started & Ready to accept Client Connection");
            //Wait for the SSL client to connect to this server
             socket = (SSLSocket)sslServerSocket.accept();
            //Create InputStream to recive messages send by the client
            isLive = true;
            ioStream = socket.getOutputStream();
            inStream = socket.getInputStream();
            state = initialState;
            sendResponse(key);
            startReadThread();

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private void startReadThread() {
        Thread readRunnable = new Thread() {
            public void run() {
                while (isLive) {
                    try {
                        int num = inStream.read(readBuffer);
                        if (num > 0) {
                            byte[] tempArray = new byte[num];
                            System.arraycopy(readBuffer, 0, tempArray, 0, num);
                            processBytes(tempArray);
                        }
                        sleep(100);

                    } catch (SocketException s) {

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException i) {
                        i.printStackTrace();
                    }
                }
            }
        };
        Thread readThread = new Thread(readRunnable);
        readThread.start();
    }

    private void processBytes(byte[] buff) throws InterruptedException {
        if (state == fileReceiveState || state == fileContentWaitState) {
            //write to file
            if (state == fileContentWaitState)
                state = fileReceiveState;
            fileSize = fileSize - buff.length;
            writeToFile(buff);


            if (fileSize == 0) {
                state = fileReceivedState;
                try {
                    foStream.close();
                } catch (IOException io) {
                    io.printStackTrace();
                }
                System.out.println("Received " + dstFile.getName());
                sendResponse(fileReceived);
                fileCount--;
                if (fileCount != 0) {
                    state = fileHeaderWaitState;
                } 
                else {
                    System.out.println("Finished");
                    state = finalState;
                    sendResponse("Thanks");
                    Thread.sleep(2000);
                    System.exit(0);
                }

                System.out.println("Received");
            }
        }
       
        else {
            parseToUTF(buff);
        }

    }

    private void parseToUTF(byte[] data) {
        try {
            String parsedMessage = new String(data, "UTF-8");
            System.out.println(parsedMessage);
            setResponse(parsedMessage);
        } catch (UnsupportedEncodingException u) {
            u.printStackTrace();
        }

    }

    private void setResponse(String message) {
        if (message.trim().equalsIgnoreCase(request) && state == initialState) {
            sendResponse(respServer);
            state = dirHeaderWait;

        } else if (state == dirHeaderWait) {
            if (createDirectory(message)) {
                sendResponse(dirResponse);
                state = fileHeaderWaitState;
            } else {
                sendResponse(dirFailedResponse);
                System.out.println("Error occurred...Going to exit");
                System.exit(0);
            }


        } else if (state == fileHeaderWaitState) {
            createFile(message);
            state = fileContentWaitState;
            sendResponse(fileHeaderRecvd);

        } else if (message.trim().equalsIgnoreCase(dirFailedResponse)) {
            System.out.println("Error occurred ....");
            System.exit(0);
        }

    }

    private void sendResponse(String resp) {
        try {
            sendBytes(resp.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private boolean createDirectory(String dirName) {
        boolean status = false;
        dir = dirName.substring(dirName.indexOf("$") + 1, dirName.indexOf("#"));
        fileCount = Integer.parseInt(dirName.substring(dirName.indexOf("#") + 1, dirName.indexOf("&")));
        if (new File(dir).mkdir()) {
            status = true;
            System.out.println("Successfully created directory  " + dirName);
        } else if (new File(dir).mkdirs()) {
            status = true;
            System.out.println("Directories were created " + dirName);

        } else if (new File(dir).exists()) {
            status = true;
            System.out.println("Directory exists" + dirName);
        } else {
            System.out.println("Could not create directory " + dirName);
            status = false;
        }

        return status;
    }

    private void createFile(String fileName) {

        String file = fileName.substring(fileName.indexOf("&") + 1, fileName.indexOf("#"));
        String lengthFile = fileName.substring(fileName.indexOf("#") + 1, fileName.indexOf("*"));
        fileSize = Integer.parseInt(lengthFile);
        dstFile = new File(dir + "/" + file);
        try {
            foStream = new FileOutputStream(dstFile);
            System.out.println("Starting to receive " + dstFile.getName());
        } catch (FileNotFoundException fn) {
            fn.printStackTrace();
        }

    }

    private void writeToFile(byte[] buff) {
        try {
            foStream.write(buff);
            
            //start
        	
        	//foStream.write(FileED.byteProcessor(Cipher.DECRYPT_MODE,key,buff));
            FileED.fileed(Cipher.DECRYPT_MODE, key, dstFile,dstFile );
            
           // FileED.processFile(Cipher.DECRYPT_MODE,key, dstFile,dstFile);
           // new File(destpath+"decrpyted"+dstFile.getName())
            //end

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private void sendBytes(byte[] dataBytes) {
        synchronized (socket) {
            if (ioStream != null) {
                try {
                    ioStream.write(dataBytes);
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        }

    }

}
