import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.security.Security;


public class ClientMain {
    private DirectoryTxr transmitter = null;
    Socket clientSocket = null;
    private boolean connectedStatus = false;
    private String ipAddress;
    String srcPath = null;
    String dstPath = "";
    public ClientMain() {

    }

    public void setIpAddress(String ip) {
        this.ipAddress = ip;
    }

    public void setSrcPath(String path) {
        this.srcPath = path;
    }

    public void setDstPath(String path) {
        this.dstPath = path;
    }

    private void createConnection() {
        Runnable connectRunnable = new Runnable() {
  
            public void run() {
              	//specifing the trustStore file which contains the certificate & public of the server
                System.setProperty("javax.net.ssl.trustStore","myTrustStore.jts");
                //specifing the password of the trustStore file
                System.setProperty("javax.net.ssl.trustStorePassword","akashakash");
                //This optional and it is just to show the dump of the details of the handshake process 
                System.setProperty("javax.net.debug","all");
                while (!connectedStatus) {
                    try {
                    	//SSLSSocketFactory establishes the ssl context and and creates SSLSocket 
                        SSLSocketFactory sslsocketfactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
                        //Create SSLSocket using SSLServerFactory already established ssl context and connect to server
                        SSLSocket clientSocket = (SSLSocket)sslsocketfactory.createSocket(ipAddress, 4001);
                        //Create OutputStream to send message to server
                        connectedStatus = true;
                        transmitter = new DirectoryTxr(clientSocket, srcPath, dstPath);
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }

            }
        };
        Thread connectionThread = new Thread(connectRunnable);
        connectionThread.start();
    }

    public static void main(String[] args) {
        ClientMain main = new ClientMain();
        main.setIpAddress("127.0.0.1");
        main.setSrcPath("C:/test/abc");
        main.setDstPath("C:/test/xyz");
        main.createConnection();


    }
}
