package com.ddx.chiamon.common.data;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author ddx
 */
public class Disk {

    private long id;
    private String uid; // unique
    private Date dt; // actual information timestamp
    private String name;
    private String path;
    private long size;
    private int plotsCount;
    private long plotsSize;
    private List<ReadAccess> readAccesses;
    private List<DiskSmart> smarts;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
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

    public int getPlotsCount() {
        return plotsCount;
    }

    public void setPlotsCount(int plotsCount) {
        this.plotsCount = plotsCount;
    }

    public long getPlotsSize() {
        return plotsSize;
    }

    public void setPlotsSize(long plotsSize) {
        this.plotsSize = plotsSize;
    }

    public List<ReadAccess> getReadAccesses() {
        return readAccesses;
    }

    public void setReadAccesses(List<ReadAccess> readAccesses) {
        this.readAccesses = readAccesses;
    }

    public void addReadAccess(ReadAccess readAccess) {
        
        if (readAccesses == null) readAccesses = new LinkedList<>();
        readAccesses.add(readAccess);
    }

    public List<DiskSmart> getSmarts() {
        return smarts;
    }

    public void setSmarts(List<DiskSmart> smarts) {
        this.smarts = smarts;
    }

    public void addSmart(DiskSmart smart) {
        
        if (smarts == null) smarts = new LinkedList<>();
        smarts.add(smart);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.uid);
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