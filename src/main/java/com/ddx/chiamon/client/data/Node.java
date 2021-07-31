package com.ddx.chiamon.client.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
    // LogFarm
    private Date lfDt;
    private int lfPlotsFarmed;
    private int lfPlotsTotal;
    private int lfProofsTotal;
    private int lfTimeAvg = -1;
    // LogError
    private Date leDt;
    private int leErrorsTotal;

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

    public Date getLfDt() {
        return lfDt;
    }

    public void setLfDt(Date lfDt) {
        this.lfDt = lfDt;
    }

    public int getLfPlotsFarmed() {
        return lfPlotsFarmed;
    }

    public void setLfPlotsFarmed(int lfPlotsFarmed) {
        this.lfPlotsFarmed = lfPlotsFarmed;
    }

    public int getLfPlotsTotal() {
        return lfPlotsTotal;
    }

    public void setLfPlotsTotal(int lfPlotsTotal) {
        this.lfPlotsTotal = lfPlotsTotal;
    }

    public int getLfProofsTotal() {
        return lfProofsTotal;
    }

    public void setLfProofsTotal(int lfProofsTotal) {
        this.lfProofsTotal = lfProofsTotal;
    }

    public int getLfTimeAvg() {
        return lfTimeAvg;
    }

    public void setLfTimeAvg(int lfTimeAvg) {
        this.lfTimeAvg = lfTimeAvg;
    }

    public Date getLeDt() {
        return leDt;
    }

    public void setLeDt(Date leDt) {
        this.leDt = leDt;
    }

    public int getLeErrorsTotal() {
        return leErrorsTotal;
    }

    public void setLeErrorsTotal(int leErrorsTotal) {
        this.leErrorsTotal = leErrorsTotal;
    }

    @JsonIgnore
    public boolean isNodeHasDisks() {
        
        return disks != null && !disks.isEmpty();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.uid);
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
        final Node other = (Node) obj;
        if (!Objects.equals(this.uid, other.uid)) {
            return false;
        }
        return true;
    }

}