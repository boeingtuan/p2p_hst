package CentralPoint;

public class RegisResponeInfo {
    private OnlinePeerInfo lstOnlinePeer;
    private boolean isAccept;

    public RegisResponeInfo(boolean isAccept) {
        this.isAccept = isAccept;
    }

    public RegisResponeInfo(OnlinePeerInfo lstOnlinePeer, boolean isAccept) {
        this.isAccept = isAccept;
        if (isAccept) this.lstOnlinePeer = lstOnlinePeer;        
    }
    
    public void setOnlinePeerInfo(OnlinePeerInfo lstOnlinePeer) {
        this.lstOnlinePeer = lstOnlinePeer;
    }
    
    public void setIsAccept(boolean isAccept) {
        this.isAccept = isAccept;
    }
    
    public OnlinePeerInfo getOnlinePeerInfo() {
        return this.lstOnlinePeer;
    }
    
    public boolean getIsAccept() {
        return this.isAccept;
    }
}
