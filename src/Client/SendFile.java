/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Tuân
 */
public class SendFile  implements Runnable{
    
    private Socket socket;
    private String filepath; // filepath: duong dan toi file can gui. VD: C:\Users\sonng_000\Documents\abc.txt
    private OutputStream Out;
    private FileInputStream In;
    
    public SendFile(Socket socket, String filepath) {
        try {
            this.socket = socket;
            this.filepath = filepath;
            this.Out = socket.getOutputStream();
            this.In = new FileInputStream(filepath);
        } catch (Exception e) {
            System.out.println("Exception in [Sendfile: Sendfile()] ! ");
        }   
    }
    
    @Override
    public void run() { 
         try {       
            byte[] buffer = new byte[1024];
            int count;
            while((count = In.read(buffer)) >= 0) {
                Out.write(buffer, 0, count);
            }
            
            Out.flush();
            if(In != null){ In.close(); }
            if(Out != null){ Out.close(); }
        }
        catch (Exception ex) {
            System.out.println("Exception [Sendfile : run()]");
            ex.printStackTrace();
        }
        
    }
}
