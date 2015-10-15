package Client;

import CentralPoint.PeerInfo;
import Server.ListPeerManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;

public class ClientToClient implements Runnable{

    private ClientFrame peerChat;
    private String hostIP;
    private int hostPort;
    private Socket peer;    
    private ListPeerManager lstOnline;

    public ClientToClient(ClientFrame peerChat, String hostIP, int hostPort) {
        this.peerChat = peerChat;
        this.hostIP = hostIP;
        this.hostPort = hostPort;
    }

    public ClientToClient(ClientFrame peerChat, Socket peer, ListPeerManager lstOnline) {
        this.peerChat = peerChat;
        this.peer = peer;
        this.lstOnline = lstOnline;
    }
    
    @Override
    public void run() {
        try {
            if (peer == null) 
                peer = new Socket(hostIP, hostPort);
            
            //TODO: implement communicate between client and client
            int answer = JOptionPane.showConfirmDialog(peerChat, "... want to chat with you. Accept?", "Confirm Request", JOptionPane.YES_NO_OPTION);
            switch (answer) {
                case JOptionPane.YES_OPTION:
                    startChat();
                    break;
                case JOptionPane.NO_OPTION:
                    peer.close();
                    break;
            }
        }
        catch (Exception e) {
            System.out.println("Exception ClientToClient: run()");
        }        
    }
    
    private void startChat() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(peer.getInputStream()));
            PrintWriter writer = new PrintWriter(peer.getOutputStream());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
