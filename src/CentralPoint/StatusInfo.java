package CentralPoint;

public class StatusInfo {
    public static int ALIVE = 1;
    public static int DYING = 2;
    
    private int status;

    public StatusInfo(int status) {
        this.status = status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public int getStatus() {
        return this.status;
    }
    
    public boolean isAlive() {
        return status == ALIVE;
    }
}
