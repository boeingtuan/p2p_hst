/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import CentralPoint.ConstantTags;
import CentralPoint.XML;
import Cryptography.CryptographyModel;
import Cryptography.PeerKey;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author Tuân
 */
public class SendFile implements Runnable {

    private String filepath; // filepath: duong dan toi file can gui. VD: C:\Users\sonng_000\Documents\abc.txt
    private DataOutputStream Out;
    private File file;
    private FileInputStream In;
    private BufferedInputStream Bin;
    private ClientFrame peerChat;
    private PeerKey.Public pKey;

    public SendFile(ClientFrame peerChat, DataOutputStream Out, PeerKey.Public pKey, String filepath) {
        try {
            this.filepath = filepath;
            this.Out = Out;
            this.peerChat = peerChat;
            this.pKey = pKey;
        } catch (Exception e) {
            System.out.println("Exception in [Sendfile: Sendfile()] ! ");
        }
    }

    @Override
    public void run() {
        /*try {       
         Out.writeUTF(XML.createFileXML(filepath));
         txt.append("File sending done!\n");
         peerChat.isSharingFile = false;
         In.close();
         }
         catch (Exception ex) {
         System.out.println("Exception [Sendfile : run()]");
         ex.printStackTrace();
         }*/
        try {
            String fileEncrypt = CryptographyModel.cryptoRSAFile(CryptographyModel.ModeCrypto.ENCRYPT,
                    CryptographyModel.ModeBlockCipher.ECB, CryptographyModel.ModePadding.PKCS5, pKey.getN(),
                    pKey.getE(), pKey.getE(), filepath, new JTextArea());
            this.file = new File(fileEncrypt);
            this.In = new FileInputStream(file);
            this.Bin = new BufferedInputStream(In);           
            byte[] bytearray = new byte[(int) file.length()];
            Bin.read(bytearray, 0, bytearray.length);
            Out.write(bytearray, 0, bytearray.length);
            Out.flush();
            peerChat.isSharingFile = false;
            JOptionPane.showMessageDialog(peerChat, "File sending done!");
            Bin.close();
            In.close();
            //Out.close();
        } catch (Exception ex) {
            System.out.println("Exception [Sendfile : run()]");
            ex.printStackTrace();
        }
    }
}
