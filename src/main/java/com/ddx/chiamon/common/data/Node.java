package com.ddx.chiamon.common.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author ddx
 */
public class Node {
    
    private long id;
    private String uid; // unique
    private Date dt; // actual information timestamp
    private String ip;
    private List<Disk> disks;
    private List<LogFarmEntry> logsFarm;
    private List<LogErrorEntry> logsError;

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<Disk> getDisks() {
        return disks;
    }

    public void setDisks(List<Disk> disks) {
        this.disks = disks;
    }

    public void addDisk(Disk disk) {
        
        if (disks == null) disks = new LinkedList<>();
        disks.add(disk);
    }

    public List<LogFarmEntry> getLogsFarm() {
        return logsFarm;
    }

    public void setLogsFarm(List<LogFarmEntry> logsFarm) {
        this.logsFarm = logsFarm;
    }

    public List<LogErrorEntry> getLogsError() {
        return logsError;
    }

    public void setLogsError(List<LogErrorEntry> logsError) {
        this.logsError = logsError;
    }

    @JsonIgnore
    public boolean isNodeHasDisks() {
        
        return disks != null && !disks.isEmpty();
    }
    
}