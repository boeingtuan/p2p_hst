package Server;

import java.io.IOException;
import java.net.Socket;

import CentralPoint.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class PeerSocketHandler implements Runnable {
    
    private Socket peer;
    private ListPeerManager lstPeerOnline;
    private ServerFrame serverLog;
    private DataInputStream streamIn  =  null;
    private DataOutputStream streamOut = null;
    private PeerInfo userPeer;
    
    public PeerSocketHandler(Socket peer, ListPeerManager lstPeerOnline, ServerFrame serverLog) throws IOException {
        this.peer = peer;
        this.lstPeerOnline = lstPeerOnline;
        this.serverLog = serverLog;
        streamOut = new DataOutputStream(peer.getOutputStream());
        streamIn = new DataInputStream(peer.getInputStream());        
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                String msg =  streamIn.readUTF();
                System.out.println(msg);
                processRequest(msg);
            }
        } 
        catch (Exception ex) {
            System.out.println("Exception at PeerSocketHandler: run()");
        }        
    }

    public void close() throws IOException {  
    	if (peer != null) peer.close();
        if (streamIn != null)  streamIn.close();
        if (streamOut != null) streamOut.close();
    }        

    private void processRequest(String msg) throws Exception {
        DeXMLlize xml = new DeXMLlize(msg);
        switch (xml.firstTag()) {
            case ConstantTags.REGISTER_TAG: {
                PeerInfo user = xml.getRegister();
                user.setIP(peer.getRemoteSocketAddress().toString());
                if (lstPeerOnline.register(user)) {
                    serverLog.setLog("Register successfully\n");
                }
                else {
                    serverLog.setLog("Register failed\n");
                }
            }
            
            case ConstantTags.LOGIN_TAG: {
                PeerInfo user = xml.getLogin();
                user.setIP(peer.getRemoteSocketAddress().toString());
                if (lstPeerOnline.login(user)) {
                    serverLog.setLog("Login successfully\n");
                    userPeer = user;
                    String listXML = lstPeerOnline.createPeerListXML();
                    streamOut.writeUTF(listXML);
                    streamOut.flush();
                }
                else {
                    serverLog.setLog("Login failed\n");
                }
                break;
            }
            
            case ConstantTags.STATUS_TAG: {
                StatusInfo status = xml.getClientStatus();
                if (status.isAlive()) {
                    streamOut.writeUTF(lstPeerOnline.createPeerListXML());
                    streamOut.flush();
                }
                else {
                    lstPeerOnline.logout(userPeer);
                }
            }
            
            default: System.out.println("Wrong format XML");
        }
    }
}