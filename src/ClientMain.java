import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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
                while (!connectedStatus) {
                    try {
                        clientSocket = new Socket(ipAddress, 4001);
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
