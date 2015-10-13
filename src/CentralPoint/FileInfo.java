package CentralPoint;

public class FileInfo {
    private String fileName;
    private boolean isAck;
    private int port;
    private boolean isBeginTransfer;
    private byte[] content;
    private boolean isEndTransfer;

    public FileInfo() {
        this.isAck = false;
        this.isBeginTransfer = false;
        this.isEndTransfer = false;
        content = null;
        fileName = "";
        port = 0;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }  
    
    public void setIsAck(boolean isAck) {
        this.isAck = isAck;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public void setIsBeginTransfer(boolean isBeginTransfer) {
        if (this.isAck)
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
    
    public String getFileName() {
        return this.fileName;
    }  
    
    public boolean getIsAck() {
        return this.isAck;
    }
    
    public int getPort() {
        return this.port;
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
