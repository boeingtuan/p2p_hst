package Server;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import java.io.File;

public class ServerFrame extends javax.swing.JFrame {
    
    public ServerListener serverListener;
    public String filepath="";
    public JFileChooser fileChooser;
    
    public ServerFrame() {
        initComponents();
        fileChooser = new JFileChooser();        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        txtFilepath = new java.awt.TextField();
        btnBrowse = new javax.swing.JButton();
        btnConnect = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("bServer");
        setLocationByPlatform(true);
        setName("frmServer"); // NOI18N
        setResizable(false);

        txtLog.setColumns(20);
        txtLog.setRows(5);
        jScrollPane1.setViewportView(txtLog);

        jLabel1.setText("Database File:");

        txtFilepath.setName("txtFieldDir"); // NOI18N
        txtFilepath.addTextListener(new java.awt.event.TextListener() {
            public void textValueChanged(java.awt.event.TextEvent evt) {
                txtFilepathTextValueChanged(evt);
            }
        });

        btnBrowse.setLabel("Browse");
        btnBrowse.setName("btnBrowse"); // NOI18N
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        btnConnect.setText("Start server");
        btnConnect.setEnabled(false);
        btnConnect.setName("btnStarServer"); // NOI18N
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFilepath, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnConnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtFilepath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnBrowse)
                        .addComponent(btnConnect)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        fileChooser.showDialog(this, "Select");
        File file = fileChooser.getSelectedFile();
        
        if (file != null) {
            filepath = file.getPath();
            txtFilepath.setText(filepath);
            btnConnect.setEnabled(true);
        }
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void txtFilepathTextValueChanged(java.awt.event.TextEvent evt) {//GEN-FIRST:event_txtFilepathTextValueChanged
        if (txtFilepath == null || txtFilepath.getText().length() == 0) {
            btnConnect.setEnabled(false);            
        }        
    }//GEN-LAST:event_txtFilepathTextValueChanged

    private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed
        serverListener = new ServerListener(this);
        Thread thread = new Thread(serverListener);
        thread.start();
        btnBrowse.setEnabled(false);
        btnConnect.setEnabled(false);
    }//GEN-LAST:event_btnConnectActionPerformed

    public void setLog(String str) {
        txtLog.append(str);
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
                new ServerFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnConnect;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private java.awt.TextField txtFilepath;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables
}
