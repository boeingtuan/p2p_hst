/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import CentralPoint.DeXMLlize;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import javax.swing.JTextArea;

/**
 *
 * @author sonng_000
 */
public class ReceiveFile implements Runnable{
    
    private final String saveLocation; // duong dan toi noi save file
    private DeXMLlize xml;
    private FileOutputStream Out;
    private JTextArea txt;
    
    public ReceiveFile(DeXMLlize xml, String saveLocation, JTextArea txt) {
        this.xml = xml;
        this.saveLocation = saveLocation;
        this.txt = txt;
    }
    
    @Override
    public void run() { 
        try {
            byte[] data = xml.doc.getDocumentElement().getChildNodes().item(0).getNodeValue().getBytes(Charset.forName("UTF-8"));
            FileOutputStream fos = new FileOutputStream(saveLocation);
            fos.write(data);
            fos.close();
            txt.append("File downloading done!\n");
            if(Out != null){ Out.close(); }
        } 
        catch (Exception ex) {
            System.out.println("Exception [Download : run(...)]");
        }
        
    }
}
