package CentralPoint;

import java.util.ArrayList;

public class OnlinePeerInfo {
    private ArrayList<PeerInfo> lstOnlinePeer;

    public OnlinePeerInfo(ArrayList<PeerInfo> lstOnlinePeer) {
        this.lstOnlinePeer = lstOnlinePeer;
    }
    
    public void setOnlinePeer(ArrayList<PeerInfo> lstOnlinePeer) {
        this.lstOnlinePeer = lstOnlinePeer;
    }
    
    public ArrayList<PeerInfo> getOnlinePeer() {
        return this.lstOnlinePeer;
    }
}
