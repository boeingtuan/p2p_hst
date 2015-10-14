package Client;

import Server.ListPeerManager;
import java.net.Socket;

public class ClientToServer implements Runnable{

    private ClientFrame peerChat;
    private String hostIP;
    private ListPeerManager lstOnline;
    private int hostPort;
    private Socket server;
    
    public ClientToServer(ClientFrame peerChat, ListPeerManager lstOnline, String hostIP, int hostPort) {
        this.peerChat = peerChat;
        this.lstOnline = lstOnline;
        this.hostIP = hostIP;
        this.hostPort = hostPort;
    }
    
    @Override
    public void run() {
        try {
            server = new Socket(hostIP, hostPort);
            //TODO: implement communicate between client and server
        }
        catch (Exception e) {
            System.out.println("Exception ClientToServer: run()");
        }
    }
    
}
