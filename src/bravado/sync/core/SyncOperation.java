package bravado.sync.core;

/**
 * SyncOperation
 * bob
 * 6/2/12 5:59 PM
 */
public class SyncOperation {
    
    public static String PUT = "PUT";
    public static String GET = "GET";
    public static String DELETE = "DELETE";
    
    private FileEntry fileEntry;
    private String operation;

    public SyncOperation(FileEntry fileEntry, String operation) {
        this.fileEntry = fileEntry;
        this.operation = operation;
    }

    public FileEntry getFileEntry() {
        return fileEntry;
    }

    public void setFileEntry(FileEntry fileEntry) {
        this.fileEntry = fileEntry;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
