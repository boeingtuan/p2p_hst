/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import CentralPoint.ConstantTags;
import CentralPoint.XML;
import CentralPoint.FileNameInfo;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
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
    private File fileToSave;

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
            XML xml;
            while (true) {
                while (peerChat.isReceivingFile);
                msg = in.readUTF();
                xml = new XML(msg);
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
                        boolean isAccept = true;
                        int answer = JOptionPane.showConfirmDialog(peerChat, peerName + " want to send you a file \"" + info.getFileName() + "\". Accept?", "Confirm Request", JOptionPane.YES_NO_OPTION);
                        DataOutputStream pOut = findEntry(peerName).out;
                        switch (answer) {
                            case JOptionPane.YES_OPTION:
                                JFileChooser file = new JFileChooser();
                                file.setSelectedFile(new File(info.getFileName()));
                                switch (file.showSaveDialog(peerChat)) {
                                    case JFileChooser.APPROVE_OPTION:
                                        isAccept = true;
                                        fileToSave = file.getSelectedFile();
                                        break;
                                    case JFileChooser.CANCEL_OPTION:
                                        isAccept = false;
                                        break;
                                }
                                break;
                            case JOptionPane.NO_OPTION:
                                isAccept = false;
                                break;
                        }
                        if (isAccept) {
                            try {
                                pOut.writeUTF("<" + ConstantTags.FILE_REQ_ACK_TAG + "/>");
                                pOut.flush();
                                JOptionPane.showMessageDialog(peerChat, "Accepting tranferring file\n");
                                try {
                                    peerChat.isReceivingFile = true;
                                    Thread t = new Thread(new ReceiveFile(peerChat, findEntry(peerName).
                                            in, fileToSave.getPath(), Integer.parseInt(info.getFileSize()), 
                                            peerChat.privateKey));
                                    t.start();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(ClientFromClient.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            try {
                                pOut.writeUTF("<" + ConstantTags.FILE_REQ_NOACK_TAG + "/>");
                                pOut.flush();
                            } catch (IOException ex) {
                                Logger.getLogger(ClientFromClient.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        break;
                    case ConstantTags.FILE_REQ_NOACK_TAG:
                        peerChat.isSharingFile = false;
                        JOptionPane.showMessageDialog(peerChat, "Request failed!\n");
                        break;
                    case ConstantTags.FILE_REQ_ACK_TAG:
                        try {
                            Thread t = new Thread(new SendFile(peerChat, findEntry(peerName).out, findEntry(peerName).pKey, peerChat.filepath));
                            t.start();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
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
                    JOptionPane.showMessageDialog(peerChat, peerName + " has just closed chat to you\n");
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
