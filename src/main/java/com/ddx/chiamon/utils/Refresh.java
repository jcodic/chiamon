package com.ddx.chiamon.utils;

/**
 *
 * @author ddx
 */
public class Refresh {

    public long period;
    public long last = -1;

    public Refresh(long period) {
	this.period = period;
    }

    public static boolean timePassed(Refresh ref) {

        if (ref.period == 0) return false;
        
	long now = System.currentTimeMillis();
	if (ref.last == -1 || now - ref.last >= ref.period) {
	    ref.last = now;
	    return true;
	} else return false;
    }

}