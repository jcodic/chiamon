package com.ddx.chiamon.client.ui.chart;

import java.util.Date;

/**
 *
 * @author ddx
 */
public class XYItemDateDouble {

    private Date dt;
    private double value;

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public XYItemDateDouble() {
    }
    
    public XYItemDateDouble(Date dt, double value) {
        this.dt = dt;
        this.value = value;
    }
    
}