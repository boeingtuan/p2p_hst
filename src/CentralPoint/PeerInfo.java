package CentralPoint;

import Cryptography.PeerKey;

public class PeerInfo {
    public static int LOGIN = 1;
    public static int REGISTER = 2;
    
    private String username;
    private String password;
    private String IP;
    private int portNum;
    private int type;
    private PeerKey.Public pKey;
    
    public PeerInfo(String username, String password, int portNum, int type) {
        this.username = username;
        this.password = password;
        this.portNum = portNum;
        this.type = type;
    }
    
    public PeerInfo(String username, String password, int portNum, int type, PeerKey.Public pKey) {
        this.username = username;
        this.password = password;
        this.portNum = portNum;
        this.type = type;
        this.pKey = pKey;
    }

    public PeerInfo(String username, String IP, int portNum) {
        this.username = username;
        this.IP = IP;
        this.portNum = portNum;
    }
    
    public PeerInfo(String username, String password, String IP, int portNum, int type) {
        this.username = username;
        this.password = password;
        this.IP = IP;
        this.portNum = portNum;
        this.type = type;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setIP(String IP) {
        this.IP = IP;
    }
    
    public void setPortNum(int portNum) {
        this.portNum = portNum;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public void setPKey(PeerKey.Public pKey) {
        this.pKey = pKey;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getPassword() {
        return this.password;
    }

    public String getIP() {
        return this.IP;
    }    
    
    public int getPortNum() {
        return this.portNum;
    }
    
    public int getType() {
        return this.type;
    }
    
    public PeerKey.Public getPKey() {
        return this.pKey;
    }
}
