/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Socket;

/**
 *
 * @author sonng_000
 */
public class ReceiveFile implements Runnable{
    
    private final Socket socket;
    private final String saveLocation; // duong dan toi noi save file
    private InputStream In;
    private FileOutputStream Out;
    
    public ReceiveFile(Socket socket, String saveLocation) {
        this.socket = socket;
        this.saveLocation = saveLocation;
    }
    
    @Override
    public void run() { 
        try {
         
            In = socket.getInputStream();
            Out = new FileOutputStream(saveLocation);
            
            byte[] buffer = new byte[1024];
            int count;
            
            while((count = In.read(buffer)) >= 0){
                Out.write(buffer, 0, count);
            }
            
            Out.flush();
            
            if(Out != null){ Out.close(); }
            if(In != null){ In.close(); }
        } 
        catch (Exception ex) {
            System.out.println("Exception [Download : run(...)]");
        }
        
    }
}
