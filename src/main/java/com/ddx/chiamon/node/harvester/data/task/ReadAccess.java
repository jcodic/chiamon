package com.ddx.chiamon.node.harvester.data.task;

import com.ddx.chiamon.node.harvester.data.Disk;
import java.util.Objects;

/**
 *
 * @author ddx
 */
public class ReadAccess extends IOTask {
    
    private String fileName;
    private long seekAt;
    private long readSize;
    private long readTime;

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

    public ReadAccess(Disk disk, long timeMax) {
        this.disk = disk;        
        this.timeMax = timeMax;
    }

    @Override
    public int hashCode() {
        return disk.hashCode() + 200;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReadAccess other = (ReadAccess) obj;
        if (!Objects.equals(this.disk, other.disk)) {
            return false;
        }
        return true;
    }

}