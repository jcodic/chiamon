package com.ddx.chiamon.common.data;

import java.util.Date;

/**
 *
 * @author ddx
 */
public class LogFarmEntry {

    private Date dt;
    private int plots;
    private String block;
    private int proofs;
    private int time;
    private int plotsTotal;

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public int getPlots() {
        return plots;
    }

    public void setPlots(int plots) {
        this.plots = plots;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public int getProofs() {
        return proofs;
    }

    public void setProofs(int proofs) {
        this.proofs = proofs;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPlotsTotal() {
        return plotsTotal;
    }

    public void setPlotsTotal(int plotsTotal) {
        this.plotsTotal = plotsTotal;
    }
    
}