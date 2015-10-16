package Server;

import CentralPoint.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ServerListener implements Runnable {
    
    private ServerFrame serverLog;
    private int serverPort;
    private ServerSocket sSocket;
    private ListPeerManager lstOnline;
    private boolean open_port = true;
    
    public HashMap<PairUser, String> lstConversation;
    
    public ServerListener(ServerFrame serverLog) throws Exception {
        this.serverLog = serverLog;
        this.serverPort = 4508;
        lstOnline = new ListPeerManager(serverLog.filepath);
        lstConversation = lstOnline.getConversation();
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
    
    public void writeConversation() {
        String res = "";
        ArrayList<PairUser> check = new ArrayList<>();
        for (Map.Entry<PairUser, String> entry : lstConversation.entrySet()) {
            PairUser pair = entry.getKey();
            String txt = entry.getValue();
            if (!check.contains(pair.swap())) {
                check.add(pair);
                res += "<" + ConstantTags.SAVE_CONVERSATION_TAG + ">";

                res += "<" + ConstantTags.USERNAME_TAG + ">"
                        + pair.getUser1()
                        + "<" + ConstantTags.USERNAME_TAG + ">";

                res += "<" + ConstantTags.USERNAME_TAG + ">"
                        + pair.getUser2()
                        + "<" + ConstantTags.USERNAME_TAG + ">";

                res += "<" + ConstantTags.TEXT_TAG + ">" + txt + "</" + ConstantTags.TEXT_TAG + ">";

                res += "</" + ConstantTags.SAVE_CONVERSATION_TAG + ">";
            }
        }        
        lstOnline.writeConversation(res);
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
