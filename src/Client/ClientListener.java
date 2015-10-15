package Client;

import Server.ListPeerManager;
import static Server.ServerListener.getCurrentIP;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JTabbedPane;

public class ClientListener implements Runnable{

    private ClientFrame peerChat;
    private int port;
    private ServerSocket sSocket;
    private boolean open_port = true;  
    private ListPeerManager lstOnline;
    private ArrayList<Entry> lstTabChat;
    private JTabbedPane tabPanel;

    public ClientListener(ClientFrame peerChat, int port, ListPeerManager lstOnline, ArrayList<Entry> lstTabChat, JTabbedPane tabPanel) {
        this.peerChat = peerChat;
        this.port = Integer.parseInt(peerChat.txtPort.getText());
        this.lstOnline = lstOnline;
        this.lstTabChat = lstTabChat;
        this.port = port;
        this.tabPanel = tabPanel;
    }
    
    @Override
    public void run() {
        try {
            sSocket = new ServerSocket(port);
            while (open_port) {
                Socket peer = sSocket.accept();                
                ClientToClient peerHandle = new ClientToClient(peerChat, peer, lstOnline, lstTabChat, tabPanel);
                Thread thread = new Thread(peerHandle);
                thread.start();
            }
        }
        catch (Exception e) {
            System.out.println("Exception at ClientListener: run()");
        }        
    }
    
}
