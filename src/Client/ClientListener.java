package Client;

import static Server.ServerListener.getCurrentIP;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientListener implements Runnable{

    private ClientFrame peerChat;
    private int port;
    private ServerSocket sSocket;
    private boolean open_port = true;    

    public ClientListener(ClientFrame peerChat) {
        this.peerChat = peerChat;
        this.port = Integer.parseInt(peerChat.txtPort.getText());
    }
    
    @Override
    public void run() {
        try {
            sSocket = new ServerSocket(port);
            while (open_port) {
                Socket peer = sSocket.accept();                
                ClientToClient peerHandle = new ClientToClient(peerChat, peer);
                Thread thread = new Thread(peerHandle);
                thread.start();
            }
        }
        catch (Exception e) {
            System.out.println("Exception at ClientListener: run()");
        }        
    }
    
}
