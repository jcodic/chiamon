package com.ddx.chiamon.node.harvester.data;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author ddx
 */
public class Disk {

    private Date dtLastModified = new Date();
    
    private String uid; // unique per host
    private String name;
    private String path;
    private long size = -1;
    private long rescanInterval = 30 * 60 * 1000; // ms
    private long readInterval = 30 * 60 * 1000; // ms
    private long smartsInterval = 30 * 60 * 1000; // ms
    private int readFiles = 1;
    private long readBlockSize = 16 * 1024;
    private List<Plot> plots;
    private String smartsCmd;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getRescanInterval() {
        return rescanInterval;
    }

    public void setRescanInterval(long rescanInterval) {
        this.rescanInterval = rescanInterval;
    }

    public long getReadInterval() {
        return readInterval;
    }

    public void setReadInterval(long readInterval) {
        this.readInterval = readInterval;
    }

    public long getSmartsInterval() {
        return smartsInterval;
    }

    public void setSmartsInterval(long smartsInterval) {
        this.smartsInterval = smartsInterval;
    }

    public int getReadFiles() {
        return readFiles;
    }

    public void setReadFiles(int readFiles) {
        this.readFiles = readFiles;
    }

    public long getReadBlockSize() {
        return readBlockSize;
    }

    public void setReadBlockSize(long readBlockSize) {
        this.readBlockSize = readBlockSize;
    }

    public List<Plot> getPlots() {
        return plots;
    }

    public void setPlots(List<Plot> plots) {
        this.plots = plots;
        setDtLastModified(new Date());
    }

    public String getSmartsCmd() {
        return smartsCmd;
    }

    public void setSmartsCmd(String smartsCmd) {
        this.smartsCmd = smartsCmd;
    }

    public Date getDtLastModified() {
        return dtLastModified;
    }

    public void setDtLastModified(Date dtLastModified) {
        this.dtLastModified = dtLastModified;
    }

    public boolean isDiskInfoValid() {
        
        return uid != null && uid.length() > 0 &&
               name != null && name.length() > 0 &&
               path != null && path.length() > 0;
    }

    public boolean isDiskHasPlots() {
        
        return plots != null && !plots.isEmpty();
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.uid);
        return hash;
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
        final Disk other = (Disk) obj;
        if (!Objects.equals(this.uid, other.uid)) {
            return false;
        }
        return true;
    }

}