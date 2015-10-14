package CentralPoint;

public class UserInfo {
    public static int LOGIN = 1;
    public static int REGISTER = 2;
    
    private String username;
    private String password;
    private int portNum;
    private int type;
    
    UserInfo(String username, String password, int portNum, int type) {
        this.username = username;
        this.password = password;
        this.portNum = portNum;
        this.type = type;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setPortNum(int portNum) {
        this.portNum = portNum;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public int getPortNum() {
        return this.portNum;
    }
    
    public int getType() {
        return this.type;
    }
}
