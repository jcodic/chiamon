package com.ddx.chiamon.common.data;

import java.util.Date;

/**
 *
 * @author ddx
 */
public class ReadAccess {

    private long id;
    private Date dt; // actual information timestamp
    private String fileName;
    private long seekAt;
    private long readSize;
    private long readTime;
    private boolean success;
    private String info;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSeekAt() {
        return seekAt;
    }

    public void setSeekAt(long seekAt) {
        this.seekAt = seekAt;
    }

    public long getReadSize() {
        return readSize;
    }

    public void setReadSize(long readSize) {
        this.readSize = readSize;
    }

    public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}