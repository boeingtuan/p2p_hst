/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import CentralPoint.XML;
import Cryptography.CryptographyModel;
import Cryptography.PeerKey;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author sonng_000
 */
public class ReceiveFile implements Runnable{
    
    private String saveLocation = ""; // duong dan toi noi save file
    private DataInputStream In;
    private FileOutputStream Out;
    private int fileSize = 0;
    //private BufferedOutputStream Bout;
    private ClientFrame peerChat;
    private PeerKey.Private pKey;
    
    public ReceiveFile(ClientFrame peerChat, DataInputStream In, String saveLocation, int fileSize, PeerKey.Private pKey) {
        try {
            this.In = In;
            this.saveLocation = saveLocation;
            this.peerChat = peerChat;
            this.Out = new FileOutputStream(saveLocation + ".encrypt");
            this.fileSize = fileSize;
            this.pKey = pKey;
            //this.Bout = new BufferedOutputStream(Out);
        } catch (IOException ex) {
            Logger.getLogger(ReceiveFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() { 
        /*try {
            byte[] data = xml.doc.getDocumentElement().getChildNodes().item(0).getNodeValue().getBytes(Charset.forName("UTF-8"));
            FileOutputStream fos = new FileOutputStream(saveLocation);
            fos.write(data);
            fos.close();
            txt.append("File downloading done!\n");
            if(Out != null){ Out.close(); }
        } 
        catch (Exception ex) {
            System.out.println("Exception [Download : run(...)]");
        }*/
        int filesize = 5242880; 
        int bytesRead;
        try {
            //byte [] bytearray  = new byte [filesize];
            //bytesRead = In.read(bytearray,0,bytearray.length);
            //int a = In.read(bytearray,0,bytearray.length);
            int count;
            int countSize = 0;
            byte[] buffer = new byte[8192];
            while (countSize < fileSize && (count = In.read(buffer, 0, 8192)) > 0)
            {
                Out.write(buffer, 0, count);
                countSize += count;
            }
            peerChat.isReceivingFile = false;
            /*Bout.write(bytearray, 0 , bytesRead);
	    	Bout.flush();*/
            Out.flush();
            //Bout.close();
            Out.close();
            
            String cryptoRSAFile = Cryptography.CryptographyModel.cryptoRSAFile(CryptographyModel.ModeCrypto.DECRYPT, 
                    CryptographyModel.ModeBlockCipher.ECB, CryptographyModel.ModePadding.PKCS5, 
                    pKey.getN(), pKey.getD(), pKey.getD(), saveLocation + ".encrypt", new JTextArea());
            File file = new File(cryptoRSAFile);
            File file2 = new File(saveLocation);
            file.renameTo(file2);
            /*if (file.renameTo(file2))
            	JOptionPane.showMessageDialog(peerChat, "Rename ok");
            else
            	JOptionPane.showMessageDialog(peerChat, "Rename fail");*/
            
            JOptionPane.showMessageDialog(peerChat, "File downloading done!");
            //In.close();
        } 
        catch (Exception ex) {
            System.out.println("Exception [Download : run(...)]");
        }
    }
}
