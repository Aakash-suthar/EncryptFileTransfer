public class ServerMain {

    public ServerMain() {

    }

    public static void main(String[] args) {

    	ENSystemId.getId();
        DirectoryRcr dirRcr = new DirectoryRcr(ENSystemId.getId());

    }
}
