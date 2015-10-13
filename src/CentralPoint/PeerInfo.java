package CentralPoint;

public class PeerInfo {
    private String username;
    private String IP;
    private int portNum;
    
    PeerInfo(String username, String IP, int portNum) {
        this.username = username;
        this.IP = IP;
        this.portNum = portNum;
    }
    
    public void setUsername(String username) {
        this.username = username;     
    }
    
    public void setIP(String IP) {
        this.IP = IP;
    }
    
    public void setPortNum(int portNum) {
        this.portNum = portNum;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getIP() {
        return this.IP;
    }
    
    public int getPortNum() {
        return this.portNum;
    }
}
