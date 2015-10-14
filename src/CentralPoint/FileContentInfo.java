package CentralPoint;

public class FileContentInfo {
    private boolean isBeginTransfer;
    private byte[] content;
    private boolean isEndTransfer;

    public FileContentInfo(boolean isBeginTransfer, byte[] content, boolean isEndTransfer) {
        this.isBeginTransfer = isBeginTransfer;
        this.content = content;
        this.isEndTransfer = isEndTransfer;
    }    
    
    public void setIsBeginTransfer(boolean isBeginTransfer) {
        this.isBeginTransfer = isBeginTransfer;
    }
    
    public void setContent(byte[] content) {
        if (this.isBeginTransfer)
            this.content = content;
    }
    
    public void setIsEndTransfer(boolean isEndTransfer) {
        if (isBeginTransfer)
            this.isEndTransfer = isEndTransfer;
    }   
    
    public boolean getIsBeginTransfer() {
        return this.isBeginTransfer;
    }
    
    public byte[] getContent() {
        return this.content;
    }
    
    public boolean getIsEndTransfer() {
        return this.isEndTransfer;
    }
}
