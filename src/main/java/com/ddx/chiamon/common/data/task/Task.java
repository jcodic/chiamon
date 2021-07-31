package com.ddx.chiamon.common.data.task;

import com.ddx.chiamon.common.data.Setup;
import java.util.concurrent.Future;

/**
 *
 * @author ddx
 */
public class Task {

    private static long idCounter = 1;
    
    protected long id;
    protected long dtStart;
    protected long dtEnd;
    protected long timeMax;
    protected Long timeUsed;
    protected Boolean success; // null for unfinished
    protected String info;
    protected Future<Task> futures;
    protected Setup setup;

    public long getId() {
        return id;
    }

    public long getDtStart() {
        return dtStart;
    }

    public void setDtStart(long dtStart) {
        this.dtStart = dtStart;
    }

    public long getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(long dtEnd) {
        this.dtEnd = dtEnd;
    }

    public long getTimeMax() {
        return timeMax;
    }

    public void setTimeMax(long timeMax) {
        this.timeMax = timeMax;
    }

    public long getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(long timeUsed) {
        this.timeUsed = timeUsed;
    }

    public Boolean isSuccess() {
        return success;
    }

    public Boolean isFinished() {
        return success != null;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Future<Task> getFutures() {
        return futures;
    }

    public void setFutures(Future<Task> futures) {
        this.futures = futures;
    }

    public Setup getSetup() {
        return setup;
    }

    public void setSetup(Setup setup) {
        this.setup = setup;
    }

    public void markAsFinished(boolean success) {
        
        markAsFinished(success, null);
    }
    
    public void markAsFinished(boolean success, String info) {
        
        this.success = success;
        this.info = info;
        dtEnd = System.currentTimeMillis();
        if (timeUsed == null) timeUsed = dtEnd - dtStart;
    }

    public Task() {
        
        id = idCounter++;
        dtStart = System.currentTimeMillis();
    }

}