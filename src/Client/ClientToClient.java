package Client;

import CentralPoint.ChatRequestInfo;
import CentralPoint.ConstantTags;
import CentralPoint.XML;
import CentralPoint.PeerInfo;
import Server.ListPeerManager;
import Server.PeerSocketHandler;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;

public class ClientToClient implements Runnable{

    private ClientFrame peerChat;
    private String hostIP;
    private int hostPort;
    private Socket peer;    
    private DataInputStream in;
    private DataOutputStream out;
    private ArrayList<Entry> lstTabChat;
    private JTabbedPane tabPanel;
    private String peerName;

    public ClientToClient(ClientFrame peerChat, String hostIP, int hostPort) {
        this.peerChat = peerChat;
        this.hostIP = hostIP;
        this.hostPort = hostPort;
    }

    public ClientToClient(ClientFrame peerChat, Socket peer, ArrayList<Entry> lstTabChat, JTabbedPane tabPanel) {
        this.peerChat = peerChat;
        this.peer = peer;
        this.lstTabChat = lstTabChat;
        this.tabPanel = tabPanel;
    }
    
    @Override
    public void run() {
        try {
            if (peer == null) 
                peer = new Socket(hostIP, hostPort);
            
            //TODO: implement communicate between client and client
            in = new DataInputStream(peer.getInputStream());
            out = new DataOutputStream(peer.getOutputStream());
            String request = in.readUTF();
            XML xml = new XML(request);
            switch (xml.firstTag())
            {
                case ConstantTags.CHAT_REQ_TAG:
                    ChatRequestInfo info = xml.getChatRequest();
                    peerName = info.getUsername();
                    int answer = JOptionPane.showConfirmDialog(peerChat, peerName + " want to chat with you. Accept?", "Confirm Request", JOptionPane.YES_NO_OPTION);
                    switch (answer) {
                        case JOptionPane.YES_OPTION:
                            startChat();
                            break;
                        case JOptionPane.NO_OPTION:
                            out.writeUTF("<" + ConstantTags.CHAT_DENY_TAG + "/>");
                            in.close();
                            out.close();
                            peer.close();
                            break;
                    }
                    break;
            }
        }
        catch (Exception e) {
            System.out.println("Exception ClientToClient: run()");
        }        
    }
    
    private void startChat() {
        try {
            out.writeUTF("<" + ConstantTags.CHAT_ACCEPT_TAG + "/>");
            JPanel jp = ClientFrame.createTab();
            tabPanel.add(peerName, jp);
            lstTabChat.add(new Entry(peerName, jp));
            if (lstTabChat.size() == 1) tabPanel.remove(peerChat.jPanel1);
            tabPanel.setSelectedIndex(lstTabChat.size() - 1);
            lstTabChat.get(tabPanel.getSelectedIndex()).availableToChat = true;
            lstTabChat.get(tabPanel.getSelectedIndex()).in = in;
            lstTabChat.get(tabPanel.getSelectedIndex()).out = out;
            lstTabChat.get(tabPanel.getSelectedIndex()).socket = peer;
            lstTabChat.get(tabPanel.getSelectedIndex()).pKey = peerChat.findPeerInfo(peerChat.lstPeerOnline, peerName).getPKey();
            peerChat.btnSend.setEnabled(true);
            peerChat.btnTransfer.setEnabled(false);
            Thread t = new Thread(new ClientFromClient(peerChat, in, peerName, lstTabChat, tabPanel));
            t.start();
            // to do next
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private JPanel findTab(String peername) {
        for (Entry tmp : lstTabChat) {
            if (tmp.username.equals(peername)) {
                return tmp.jp;
            }
        }
        return null;
    }
    
    private JTextArea retrieveTxt(JPanel jp) {
        JScrollPane j = (JScrollPane) jp.getComponent(0);
        JViewport vp = (JViewport) j.getComponent(0);
        JTextArea txtArea = (JTextArea) vp.getComponent(0);
        return txtArea;
    }
     
    public static JTextArea getTextArea(JComponent com) {
        return ((JTextArea)(((JViewport)(((JScrollPane)(com.getComponent(0))).getComponent(0))).getComponent(0)));
    }
}
