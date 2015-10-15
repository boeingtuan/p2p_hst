/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;

import CentralPoint.ConstantTags;
import CentralPoint.DeXMLlize;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JTextArea;

/**
 *
 * @author Tuân
 */
public class SendFile  implements Runnable{
    
    private String filepath; // filepath: duong dan toi file can gui. VD: C:\Users\sonng_000\Documents\abc.txt
    private DataOutputStream Out;
    private FileInputStream In;
    private JTextArea txt;
    
    public SendFile(DataOutputStream Out, String filepath, JTextArea txt) {
        try {
            this.filepath = filepath;
            this.Out = Out;
            this.In = new FileInputStream(filepath);
            this.txt = txt;
        } catch (Exception e) {
            System.out.println("Exception in [Sendfile: Sendfile()] ! ");
        }   
    }
    
    @Override
    public void run() { 
        try {       
            Out.writeUTF(DeXMLlize.createFileXML(filepath));
            txt.append("File sending done!\n");
            In.close();
        }
        catch (Exception ex) {
            System.out.println("Exception [Sendfile : run()]");
            ex.printStackTrace();
        }
        
    }
}
