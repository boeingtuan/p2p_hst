package CentralPoint;

public class FileNameInfo {
    private String fileName;
    private String fileSize;

    public FileNameInfo(String fileName, String fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }  
    
    public String getFileName() {
        return this.fileName;
    }
    
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }  
    
    public String getFileSize() {
        return this.fileSize;
    }
}
