package Client;

import CentralPoint.ConstantTags;
import CentralPoint.DeXMLlize;
import CentralPoint.PeerInfo;
import Server.ListPeerManager;
import com.sun.corba.se.impl.orbutil.closure.Constant;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.DefaultListModel;

public class ClientToServer implements Runnable{

    private ClientFrame peerChat;
    private String hostIP;
    private int hostPort;
    private Socket server;
    private DataInputStream streamIn  =  null;
    private DataOutputStream streamOut = null;
    DefaultListModel model;
    
    public ClientToServer(ClientFrame peerChat, String hostIP, int hostPort) {
        this.peerChat = peerChat;
        this.hostIP = hostIP;
        this.hostPort = hostPort;
        model = new DefaultListModel();
    }
    
    @Override
    public void run() {
        try {
            server = new Socket(hostIP, hostPort);
            //TODO: implement communicate between client and server
            streamIn = new DataInputStream(server.getInputStream());
            streamOut = new DataOutputStream(server.getOutputStream());
            
            peerChat.txtHostAddress.setEnabled(false);
            peerChat.txtHostPort.setEnabled(false);
            peerChat.btnConnect.setEnabled(false);
            
            peerChat.txtUsername.setEnabled(true);
            peerChat.txtPassword.setEnabled(true);
            peerChat.txtPort.setEnabled(true);
            
            peerChat.btnLogin.setEnabled(true);
            peerChat.btnSignUp.setEnabled(true);
                        
            while (true) {
                String msg = streamIn.readUTF();
                System.out.println(msg);
                processRespone(msg);
            }
            
        }
        catch (Exception e) {
            System.out.println("Exception ClientToServer: run()");
        }
    }
    
    public void send(String msg) throws IOException {
        streamOut.writeUTF(msg);
    }

    private void processRespone(String msg) throws Exception {
        DeXMLlize xml = new DeXMLlize(msg);
        switch (xml.firstTag()) {
            case ConstantTags.SESSION_DENY_TAG: {
                peerChat.txtAreaChatServer.append("Unsuccessfully! Please try again!\n");
                break;
            }
            case ConstantTags.SESSION_ACCEPT_TAG: {
                peerChat.txtAreaChatServer.append("Successfully! Looking a friend and start to chat\n");
                
                peerChat.txtUsername.setEnabled(false);
                peerChat.txtPassword.setEnabled(false);
                peerChat.txtPort.setEnabled(false);

                peerChat.btnLogin.setEnabled(false);
                peerChat.btnSignUp.setEnabled(false);
            }
            case ConstantTags.ONLINE_PEER_TAG: {                
                for (PeerInfo peer : xml.getOnlinePeer().getOnlinePeer()) {
                    model.addElement(peer.getUsername());
                }
                peerChat.lstOnline.setModel(model);  
                break;
            }
        }
    }
    
}
