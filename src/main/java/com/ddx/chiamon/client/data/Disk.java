package com.ddx.chiamon.client.data;

import java.util.Date;

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
    private long raTime = -1;
    private long raTimeAvg = -1;
    private Date raDt;
    private int raSucceed;
    private int raFailed;
    private Date smartsDt;
    private int smartsTemp = DiskSmart.NO_DATA;

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

    public long getRaTime() {
        return raTime;
    }

    public void setRaTime(long raTime) {
        this.raTime = raTime;
    }

    public long getRaTimeAvg() {
        return raTimeAvg;
    }

    public void setRaTimeAvg(long raTimeAvg) {
        this.raTimeAvg = raTimeAvg;
    }

    public Date getRaDt() {
        return raDt;
    }

    public void setRaDt(Date raDt) {
        this.raDt = raDt;
    }

    public int getRaSucceed() {
        return raSucceed;
    }

    public void setRaSucceed(int raSucceed) {
        this.raSucceed = raSucceed;
    }

    public int getRaFailed() {
        return raFailed;
    }

    public void setRaFailed(int raFailed) {
        this.raFailed = raFailed;
    }

    public Date getSmartsDt() {
        return smartsDt;
    }

    public void setSmartsDt(Date smartsDt) {
        this.smartsDt = smartsDt;
    }

    public int getSmartsTemp() {
        return smartsTemp;
    }

    public void setSmartsTemp(int smartsTemp) {
        this.smartsTemp = smartsTemp;
    }

}