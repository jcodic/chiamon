package com.ddx.chiamon.node.harvester.data;

/**
 *
 * @author ddx
 */
public class Plot {

    private String fileName;
    private long fileSize;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public Plot() {
    }
    
    public Plot(String fileName, long fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

}