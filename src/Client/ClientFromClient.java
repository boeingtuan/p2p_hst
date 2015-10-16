/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import CentralPoint.ConstantTags;
import CentralPoint.DeXMLlize;
import CentralPoint.FileNameInfo;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;

/**
 *
 * @author sonng_000
 */
public class ClientFromClient implements Runnable {

    private DataInputStream in;
    private String peerName;
    private ArrayList<Entry> lstTabChat;
    private ClientFrame peerChat;
    private JTabbedPane tabPanel;
    
    public ClientFromClient(ClientFrame peerChat, DataInputStream in, String peerName, ArrayList<Entry> lstTabChat, JTabbedPane tabPanel) {
        this.in = in;
        this.peerName = peerName;
        this.lstTabChat = lstTabChat;
        this.peerChat = peerChat;
        this.tabPanel = tabPanel;
    }
    
    @Override
    public void run() {
        try {
        String msg = "";
            DeXMLlize xml;
            while (true) {
            
                msg = in.readUTF();
                xml = new DeXMLlize(msg);
                switch (xml.firstTag()) {
                    case ConstantTags.TEXT_TAG: {
                        retrieveTxt(lstTabChat.get(tabPanel.getSelectedIndex()).jp).append(xml.getText());                        
                        break;
                    }
                    case ConstantTags.CHAT_MSG_TAG: {
                        try {
                            retrieveTxt(findTab(peerName)).append(peerName + ": " + xml.getMessage().getMessage() + "\n");
                        } catch (Exception ex) {
                            Logger.getLogger(ClientFromClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                    case ConstantTags.CHAT_CLOSE_TAG:
                        for (Entry tmp : lstTabChat) {
                            if (tmp.username == peerName) {
                                tmp.availableToChat = false;
                                break;
                            }
                        }
                        if (lstTabChat.get(tabPanel.getSelectedIndex()).username == peerName) {
                            peerChat.btnSend.setEnabled(false);
                            peerChat.btnTransfer.setEnabled(false);
                        }
                        JOptionPane.showMessageDialog(peerChat, peerName + " has just closed chat to you\n");
                        break;
                    case ConstantTags.FILE_REQ_TAG:
                        FileNameInfo info = null;
                        try {
                            info = xml.getFileName();
                        } catch (Exception ex) {
                            Logger.getLogger(ClientFromClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        int answer = JOptionPane.showConfirmDialog(peerChat, peerName + " want to send you a file \"" + info.getFileName() + "\". Accept?", "Confirm Request", JOptionPane.YES_NO_OPTION);
                        DataOutputStream pOut = findEntry(peerName).out;
                        switch (answer) {
                            case JOptionPane.YES_OPTION:
                                try {
                                    pOut.writeUTF("<" + ConstantTags.FILE_REQ_ACK_TAG + "/>");
                                    pOut.flush();
                                    retrieveTxt(findTab(peerName)).append("Accepting tranferring file\n");
                                } catch (IOException ex) {
                                    Logger.getLogger(ClientFromClient.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                break;
                            case JOptionPane.NO_OPTION:
                                try {
                                    pOut.writeUTF("<" + ConstantTags.FILE_REQ_NOACK_TAG + "/>");
                                    pOut.flush();
                                } catch (IOException ex) {
                                    Logger.getLogger(ClientFromClient.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                break;
                        }
                        break;
                    case ConstantTags.FILE_REQ_NOACK_TAG:
                        peerChat.isSharingFile = false;
                        retrieveTxt(findTab(peerName)).append("Request failed!\n");
                        break;
                    case ConstantTags.FILE_REQ_ACK_TAG:
                        pOut = findEntry(peerName).out;
                        try {
                            Thread t = new Thread(new SendFile(pOut, peerChat.filepath, retrieveTxt(findTab(peerName))));
                            t.start();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        break;
                    case ConstantTags.FILE_DATA_TAG:
                        DataInputStream pIn = findEntry(peerName).in;
                        JFileChooser file = new JFileChooser();
                        file.showSaveDialog(peerChat);

                        try {
                            Thread t = new Thread(new ReceiveFile(xml, file.getSelectedFile().getPath(), retrieveTxt(findTab(peerName))));
                            t.start();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                }
        }
            } catch (Exception ex) {
                System.out.println("Exception ClientFromClient run()");
                for (Entry tmp : lstTabChat) {
                    if (tmp.username == peerName) {
                        tmp.availableToChat = false;
                        if (lstTabChat.get(tabPanel.getSelectedIndex()).username == peerName) {
                            peerChat.btnSend.setEnabled(false);
                            peerChat.btnTransfer.setEnabled(false);
                        }
                        retrieveTxt(findTab(peerName)).append(peerName + " has just closed chat to you\n");                        
                        break;
                    }
                }
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
    
    private Entry findEntry(String peername) {
        for (Entry tmp : lstTabChat) {
            if (tmp.username.equals(peername)) {
                return tmp;
            }
        }
        return null;
    }
}
