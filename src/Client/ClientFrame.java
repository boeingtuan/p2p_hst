package Client;

import CentralPoint.ConstantTags;
import CentralPoint.DeXMLlize;
import CentralPoint.PeerInfo;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.UIManager;



public class ClientFrame extends javax.swing.JFrame {    
    
    private ArrayList<Entry> lstTabChat;
    private ArrayList<PeerInfo> lstPeerOnline;
    public String filepath="";
    private JFileChooser fileChooser;
    private ClientToServer serverGate;
    public boolean isSharingFile = false;
    
    public ClientFrame() {      
        initComponents();
        fileChooser = new JFileChooser();
        lstTabChat = new ArrayList<>();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    serverGate.send(DeXMLlize.createStatusXML(ConstantTags.DYING));
                } catch (Exception ex) {
                    Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {}

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtHostAddress = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtHostPort = new javax.swing.JTextField();
        btnConnect = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnLogin = new javax.swing.JButton();
        btnSignUp = new javax.swing.JButton();
        txtPassword = new javax.swing.JPasswordField();
        tabPanel = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaChatServer = new javax.swing.JTextArea();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstOnline = new javax.swing.JList();
        label1 = new java.awt.Label();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtMessage = new javax.swing.JTextArea();
        btnSend = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtDirFile = new javax.swing.JTextField();
        btnTransfer = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtPort = new javax.swing.JTextField();
        btnStartChat = new javax.swing.JButton();
        btnBrowse = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("bChat");
        setLocationByPlatform(true);
        setResizable(false);

        jLabel1.setText("Host Address");

        txtHostAddress.setText("localhost");

        jLabel2.setText("Host port ");

        txtHostPort.setText("4508");

        btnConnect.setText("Connect");
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });

        jLabel3.setText("Username");

        txtUsername.setText("boeing");
        txtUsername.setEnabled(false);

        jLabel4.setText("Password");

        btnLogin.setText("Login");
        btnLogin.setEnabled(false);
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnSignUp.setText("Sign Up");
        btnSignUp.setEnabled(false);
        btnSignUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignUpActionPerformed(evt);
            }
        });

        txtPassword.setText("password");
        txtPassword.setEnabled(false);

        tabPanel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabPanelStateChanged(evt);
            }
        });
        tabPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabPanelMouseClicked(evt);
            }
        });

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setFocusable(false);

        txtAreaChatServer.setColumns(20);
        txtAreaChatServer.setLineWrap(true);
        txtAreaChatServer.setRows(5);
        txtAreaChatServer.setText("Welcome to  HST p2p chat application.\nFirst, you have to register an account to start chatting with everyone. \nIf you already had one, just login with it.\nAfter login with your account, all available users are show in the right box. \nJust choose who you want to talk and chat with him/her.\nYou can send a file (less than 5Mb) to anyone by using the button \"Transfer File\" at the bottom of the windows.\nIf you don't want to talk anymore, just double click to the current tab. The chat tab will close.\nEnjoy your chatting and having a good time!\n");
        txtAreaChatServer.setWrapStyleWord(true);
        txtAreaChatServer.setBorder(null);
        txtAreaChatServer.setFocusable(false);
        jScrollPane2.setViewportView(txtAreaChatServer);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
        );

        tabPanel.addTab("Server", null, jPanel1, "");

        lstOnline.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstOnlineMouseClicked(evt);
            }
        });
        lstOnline.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstOnlineValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lstOnline);

        label1.setAlignment(java.awt.Label.RIGHT);
        label1.setText("Message");

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtMessage.setColumns(20);
        txtMessage.setRows(2);
        txtMessage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMessageKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(txtMessage);

        btnSend.setText("Send Message");
        btnSend.setEnabled(false);
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("File");

        btnTransfer.setText("Transfer File");
        btnTransfer.setEnabled(false);
        btnTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransferActionPerformed(evt);
            }
        });

        jLabel6.setText("Port");

        txtPort.setText("5130");
        txtPort.setToolTipText("");
        txtPort.setEnabled(false);

        btnStartChat.setLabel("Start Chat");
        btnStartChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartChatActionPerformed(evt);
            }
        });

        btnBrowse.setText("Browse");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPort))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtHostAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtUsername)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtHostPort)
                            .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnConnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSignUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDirFile, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBrowse))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tabPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(btnStartChat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnTransfer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtHostAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtHostPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConnect))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(btnLogin)
                    .addComponent(btnSignUp)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnStartChat)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                        .addComponent(label1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtDirFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTransfer)
                    .addComponent(btnBrowse))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnSignUp.getAccessibleContext().setAccessibleName("");
        btnSignUp.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed
        /*DefaultListModel<String> model;
        model = new DefaultListModel();
        model.addElement("All");
        model.addElement("All1");
        lstOnline.setModel(model);*/
        lstPeerOnline = new ArrayList<>();
        serverGate = new ClientToServer(this, txtHostAddress.getText(), Integer.parseInt(txtHostPort.getText()), lstPeerOnline, lstTabChat, tabPanel);
        Thread serverThread = new Thread(serverGate);
        serverThread.start();
        btnConnect.setEnabled(false);
    }//GEN-LAST:event_btnConnectActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        try {
            serverGate.send(DeXMLlize.createUserXML(txtUsername.getText(), txtPassword.getText(), ConstantTags.LOGIN_TAG,Integer.parseInt(txtPort.getText())));
        } catch (Exception ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnSignUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignUpActionPerformed
        try {
            serverGate.send(DeXMLlize.createUserXML(txtUsername.getText(), txtPassword.getText(), ConstantTags.REGISTER_TAG,Integer.parseInt(txtPort.getText())));
        } catch (Exception ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSignUpActionPerformed

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        DataOutputStream pOut = lstTabChat.get(tabPanel.getSelectedIndex()).out;
        try {
            pOut.writeUTF(DeXMLlize.createMessage(txtMessage.getText()));
            pOut.flush();
            retrieveTxt(lstTabChat.get(tabPanel.getSelectedIndex()).jp).append(txtUsername.getText() + ": " + txtMessage.getText() + "\n");
            txtMessage.setText("");
        } catch (Exception ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSendActionPerformed

    private void btnTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransferActionPerformed
        if (isSharingFile) {
            JOptionPane.showMessageDialog(this, "In progress of sharing file...");
        }
        else {
            DataOutputStream pOut = lstTabChat.get(tabPanel.getSelectedIndex()).out;
            isSharingFile = true;
            filepath = txtDirFile.getText();
            try {
                pOut.writeUTF(DeXMLlize.createFileRequest(filepath.substring(filepath.lastIndexOf('\\') + 1)));
                pOut.flush();
                retrieveTxt(lstTabChat.get(tabPanel.getSelectedIndex()).jp).append("Sending file tranferring request to " + lstTabChat.get(tabPanel.getSelectedIndex()).username + "\n");
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
    }//GEN-LAST:event_btnTransferActionPerformed

    private void lstOnlineValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstOnlineValueChanged
        
    }//GEN-LAST:event_lstOnlineValueChanged

    private void tabPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabPanelMouseClicked
        if (evt.getClickCount() == 2) {
            DataOutputStream pOut = lstTabChat.get(tabPanel.getSelectedIndex()).out;
            try {
                pOut.writeUTF("<" + ConstantTags.CHAT_CLOSE_TAG + "/>");
                pOut.flush();
            } catch (Exception ex) {
                System.out.println("Client closed");
            }
            int index = tabPanel.getSelectedIndex();
            lstTabChat.remove(index);
            tabPanel.remove(tabPanel.getSelectedComponent());
            if (lstTabChat.isEmpty()) {
                tabPanel.addTab("Server", jPanel1);
                btnSend.setEnabled(false);
                btnTransfer.setEnabled(false);
            }
        }
    }//GEN-LAST:event_tabPanelMouseClicked

    private void btnStartChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartChatActionPerformed
        String peername = lstOnline.getSelectedValue().toString();
        JPanel jp;
        if ((jp = findTab(peername)) != null) {
            tabPanel.setSelectedComponent(jp);
        }
        else {
            jp = createTab();
            tabPanel.addTab(peername, jp);
            lstTabChat.add(new Entry(peername, jp));
            if (lstTabChat.size() == 1) tabPanel.remove(jPanel1);
            tabPanel.setSelectedIndex(lstTabChat.size() - 1);
            retrieveTxt(jp).append("Waiting for accepting...\n");
            PeerInfo info = findPeerInfo(lstPeerOnline, peername);
            try {
                String IP = info.getIP().substring(1, info.getIP().indexOf(':'));
                Socket socket = new Socket(IP, info.getPortNum());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());
                out.writeUTF(DeXMLlize.createChatRequestXML(txtUsername.getText()));
                String msg = in.readUTF();
                DeXMLlize xml = new DeXMLlize(msg);
                switch (xml.firstTag()) {
                    case ConstantTags.CHAT_ACCEPT_TAG:
                        //retrieveTxt(jp).append("Ready to chat!\n");
                        JOptionPane.showMessageDialog(this, "Ready to chat!");
                        btnSend.setEnabled(true);
                        btnTransfer.setEnabled(true);
                        lstTabChat.get(tabPanel.getSelectedIndex()).availableToChat = true;
                        lstTabChat.get(tabPanel.getSelectedIndex()).in = in;
                        lstTabChat.get(tabPanel.getSelectedIndex()).out = out;
                        Thread t = new Thread(new ClientFromClient(this, in, peername, lstTabChat, tabPanel));
                        t.start();
                        break;
                    case ConstantTags.CHAT_DENY_TAG:
                        //retrieveTxt(jp).append("Failed to chat!\n");
                        JOptionPane.showMessageDialog(this, "Failed to chat!");                        
                        btnSend.setEnabled(false);
                        btnTransfer.setEnabled(false);
                        lstTabChat.get(tabPanel.getSelectedIndex()).availableToChat = false;
                        in.close();
                        out.close();
                        socket.close();
                        break;
                }
                // request chat
                // TODO
            } catch (Exception ex) {
                Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnStartChatActionPerformed

    private PeerInfo findPeerInfo(ArrayList<PeerInfo> lst, String peerName) {
        for (PeerInfo i : lst) {
            if (i.getUsername() == peerName)
                return i;
        }
        return null;
    }
    
    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        // TODO add your handling code here:
        fileChooser.showDialog(this, "Select file you want to send");
        File file = fileChooser.getSelectedFile();
        
        if (file != null) {
            filepath = file.getPath();
            txtDirFile.setText(filepath);
            btnConnect.setEnabled(true);
        }        
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void tabPanelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabPanelStateChanged
        // TODO add your handling code here:
        if (lstTabChat != null && lstTabChat.size() >= 1) {
            if (lstTabChat.get(tabPanel.getSelectedIndex()).availableToChat) {
                btnSend.setEnabled(true);
                btnTransfer.setEnabled(true);
            }
            else {
                btnSend.setEnabled(false);
                btnTransfer.setEnabled(false);
            }
        }
    }//GEN-LAST:event_tabPanelStateChanged

    private void lstOnlineMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstOnlineMouseClicked
        if (evt.getClickCount() == 2 && lstOnline.getSelectedValue() != null) {
            btnStartChat.doClick();
        }
    }//GEN-LAST:event_lstOnlineMouseClicked

    private void txtMessageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMessageKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            evt.consume();
            btnSend.doClick();
        }
    }//GEN-LAST:event_txtMessageKeyPressed
    
    private JPanel findTab(String peername) {
        for (Entry tmp : lstTabChat) {
            if (tmp.username.equals(peername)) {
                return tmp.jp;
            }
        }
        return null;
    }
    
    public static JPanel createTab() {
        JPanel jp = new JPanel();
        JScrollPane jScrollPane = new JScrollPane();
        JTextArea txtArea = new JTextArea();
        
        txtArea.setColumns(20);
        txtArea.setLineWrap(true);
        txtArea.setRows(5);
        txtArea.setWrapStyleWord(true);
        txtArea.setBorder(null);
        txtArea.setFocusable(false);
        
        jScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setFocusable(false);          
        jScrollPane.setViewportView(txtArea);         
        
        GroupLayout jPanelLayout = new GroupLayout(jp);
        jp.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
        );
        jPanelLayout.setVerticalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
        );  
        
        return jp;
    }
    
    private JTextArea retrieveTxt(JPanel jp) {
        JScrollPane j = (JScrollPane) jp.getComponent(0);
        JViewport vp = (JViewport) j.getComponent(0);
        JTextArea txtArea = (JTextArea) vp.getComponent(0);
        return txtArea;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception ex){
            System.out.println("Look & Feel Exception");
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientFrame().setVisible(true);
            }
        });       
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowse;
    public javax.swing.JButton btnConnect;
    public javax.swing.JButton btnLogin;
    public javax.swing.JButton btnSend;
    public javax.swing.JButton btnSignUp;
    private javax.swing.JButton btnStartChat;
    public javax.swing.JButton btnTransfer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    public static javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private java.awt.Label label1;
    public javax.swing.JList lstOnline;
    public javax.swing.JTabbedPane tabPanel;
    public javax.swing.JTextArea txtAreaChatServer;
    private javax.swing.JTextField txtDirFile;
    public javax.swing.JTextField txtHostAddress;
    public javax.swing.JTextField txtHostPort;
    private javax.swing.JTextArea txtMessage;
    public javax.swing.JPasswordField txtPassword;
    public javax.swing.JTextField txtPort;
    public javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}

class Entry {
    public String username;
    public JPanel jp;
    public boolean availableToChat;
    public DataInputStream in;
    public DataOutputStream out;

    public Entry(String username, JPanel jp) {
        this.username = username;
        this.jp = jp;
        availableToChat = false;
    }        
}