package Server;

import java.io.IOException;
import java.net.Socket;

import CentralPoint.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PeerSocketHandler implements Runnable {
    
    private Socket peer;
    private ListPeerManager lstPeerOnline;
    private ServerFrame serverLog;
    private DataInputStream streamIn  =  null;
    private DataOutputStream streamOut = null;
    private PeerInfo userPeer;
    private int time_count = 10;
    private Timer timer;
            
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
                //System.out.println(msg);
                processRequest(msg);
            }
        } 
        catch (Exception ex) {
            System.out.println("Exception at PeerSocketHandler: run()");
        }        
    }

    public void close() throws Exception {  
    	if (peer != null) peer.close();
        if (streamIn != null)  streamIn.close();
        if (streamOut != null) streamOut.close();
    }        

    private void processRequest(String msg) throws Exception {
        XML xml = new XML(msg);
        switch (xml.firstTag()) {
            case ConstantTags.REGISTER_TAG: {
                PeerInfo user = xml.getRegister();
                user.setIP(peer.getRemoteSocketAddress().toString());
                if (lstPeerOnline.register(user)) {
                    serverLog.setLog("User " + user.getUsername() + " just register at IP: " + user.getIP() + "\n");
                }
                else {
                    serverLog.setLog("User " + user.getUsername() + " register failed at IP: " + user.getIP() + "\n");
                    streamOut.writeUTF("<" + ConstantTags.SESSION_DENY_TAG + "/>");
                    streamOut.flush();
                    break;
                }
            }
            
            case ConstantTags.LOGIN_TAG: {
                PeerInfo user = xml.getLogin();
                user.setIP(peer.getRemoteSocketAddress().toString());
                if (lstPeerOnline.login(user)) {
                    serverLog.setLog("User " + user.getUsername() + " login successfully at IP: " + user.getIP()+ "\n");
                    
                    userPeer = user;
                    
                    String listXML = lstPeerOnline.createPeerListXML();
                    streamOut.writeUTF("<" + ConstantTags.SESSION_ACCEPT_TAG + ">" + listXML.substring(listXML.indexOf("?>") + 2) + "</" + ConstantTags.SESSION_ACCEPT_TAG + ">");
                    streamOut.flush();
                    
                    timerKillPeer();
                }
                else {
                    serverLog.setLog("User " + user.getUsername() + " login failed at IP: " + user.getIP() + "\n");
                    streamOut.writeUTF("<" + ConstantTags.SESSION_DENY_TAG + "/>");
                    streamOut.flush();
                }
                break;
            }
            
            case ConstantTags.STATUS_TAG: {
                StatusInfo status = xml.getClientStatus();
                if (status.isAlive()) {
                    time_count = 10;
                    streamOut.writeUTF(lstPeerOnline.createPeerListXML());
                    streamOut.flush();
                }
                else {
                    lstPeerOnline.logout(userPeer);
                    serverLog.setLog("User " + userPeer.getUsername() + " log out at IP: " + userPeer.getIP() + "\n");
                    timer.cancel();
                    close();
                }
                break;
            }                
            
            case ConstantTags.CONVERSATION_TAG: {
                PairUser pairUser = xml.getPairUser();
                streamOut.writeUTF("<" + ConstantTags.TEXT_TAG + ">" + serverLog.serverListener.lstConversation.get(pairUser) + "</" + ConstantTags.TEXT_TAG + ">");                
                break;
            }
            
            case ConstantTags.SAVE_CONVERSATION_TAG: {
                Conversation conversation = xml.getConversation();
                serverLog.serverListener.lstConversation.remove(conversation.getPairUser());
                serverLog.serverListener.lstConversation.remove(conversation.getPairUser().swap());
                
                serverLog.serverListener.lstConversation.put(conversation.getPairUser(), conversation.getText());
                serverLog.serverListener.lstConversation.put(conversation.getPairUser().swap(), conversation.getText());
                System.out.println("Update conversation " + conversation.getPairUser().getUser1() + " " + conversation.getPairUser().getUser2());
                break;
            }
            default: System.out.println("Wrong format XML");
        }
    }
    
    private void timerKillPeer(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {              
                    try {
                        time_count--;
                        if (time_count < 0) {
                            lstPeerOnline.logout(userPeer);
                            serverLog.setLog("User " + userPeer.getUsername() + " terminate at IP: " + userPeer.getIP() + "\n");
                            timer.cancel();                        
                            close();
                        }
                    } catch (Exception ex) {
                        System.out.println("Exception timerKillPeer");
                    }
            }
        }, 0, 1000);
    }
}