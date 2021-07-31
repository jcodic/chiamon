package com.ddx.chiamon.client.data;

import java.util.Date;

/**
 *
 * @author ddx
 */
public class ReadAccess {

    private long diskId;
    private Date dt; // actual information timestamp
    private long readTime;
    private boolean success;

    public long getDiskId() {
        return diskId;
    }

    public void setDiskId(long diskId) {
        this.diskId = diskId;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
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
    
}