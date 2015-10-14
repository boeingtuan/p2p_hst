package Server;

import java.net.*;
import java.util.Enumeration;

public class ServerListener implements Runnable {
    
    private ServerFrame serverLog;
    private int serverPort;
    private ServerSocket sSocket;
    private ListPeerManager lstOnline;
    private boolean open_port = true;
    
    public ServerListener(ServerFrame serverLog) {
        this.serverLog = serverLog;
        this.serverPort = 4508;
        lstOnline = new ListPeerManager(serverLog.filepath);
    }
    
    @Override
    public void run() {
        try {
            sSocket = new ServerSocket(serverPort);
            serverLog.setLog("Server started \n");
            serverLog.setLog("Current IP: " + getCurrentIP() + "\n");
            serverLog.setLog("Current port: " + serverPort + "\n");
            serverLog.setLog("Waiting for client to connect ... \n");
            while (open_port) {
                Socket peer = sSocket.accept();
                serverLog.setLog("Just connect to " + peer.getRemoteSocketAddress() + "\n");
                PeerSocketHandler peerHandle = new PeerSocketHandler(peer, lstOnline, serverLog);
                Thread thread = new Thread(peerHandle);
                thread.start();
            }
        }
        catch (Exception e) {
            System.out.println("Exception at ServerListener: run()");
        }
    }
    
    public void stopServer() {
        try {
            open_port = false;
            sSocket.close();
        }
        catch (Exception e) {
            System.out.println("Exception ServerListener: stopServer()");
        }
    }
    
    // This function is on the Internet :3
    public static String getCurrentIP() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String ipAddress = inetAddress.getHostAddress().toString();
                        return ipAddress;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Socket exception in GetIP Address of Utilities");
        }
        return null;        
    }
}
