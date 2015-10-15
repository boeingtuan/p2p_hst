/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;

import java.net.Socket;

/**
 *
 * @author Tuân
 */
public class SendFile {
    
    private Socket socket;
    private String filepath; // filepath: duong dan toi file can gui. VD: C:\Users\sonng_000\Documents\abc.txt
    
    public SendFile(Socket socket, String filepath) {
        this.socket = socket;
        this.filepath = filepath;
    }
    
    public void sendFile() { 
        // hien thuc gui file qua socket, gia su socket da duoc ket noi
        
    }
}
