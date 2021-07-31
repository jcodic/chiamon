package com.ddx.chiamon.node.harvester.data.task;

import com.ddx.chiamon.common.data.LogErrorEntry;
import com.ddx.chiamon.common.data.LogFarmEntry;
import com.ddx.chiamon.common.data.task.Task;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author ddx
 */
public class ScanLogs extends Task {
    
    private List<LogFarmEntry> logsFarm;
    private List<LogErrorEntry> logsError;
    private final Date dtAcceptAfterLogsFarm;
    private final Date dtAcceptAfterLogsError;
    private Date dtAcceptMaxLogsFarm;
    private Date dtAcceptMaxLogsError;

    public List<LogFarmEntry> getLogsFarm() {
        return logsFarm;
    }

    public List<LogErrorEntry> getLogsError() {
        return logsError;
    }
    
    public void addLogFarm(LogFarmEntry log) {
        if (logsFarm == null) logsFarm = new LinkedList<>(); 
        logsFarm.add(log);
        acceptedDtLogsFarm(log.getDt());
    }
    
    public void addLogError(LogErrorEntry log) {
        if (logsError == null) logsError = new LinkedList<>(); 
        logsError.add(log);
        acceptedDtLogsError(log.getDt());
    }
    
    public boolean isHasLogsFarm() {
        return logsFarm != null && !logsFarm.isEmpty();
    }

    public boolean isHasLogsError() {
        return logsError != null && !logsError.isEmpty();
    }

    public Date getDtAcceptAfterLogsFarm() {
        return dtAcceptAfterLogsFarm;
    }

    public Date getDtAcceptAfterLogsError() {
        return dtAcceptAfterLogsError;
    }

    public Date getDtAcceptMaxLogsFarm() {
        return dtAcceptMaxLogsFarm;
    }

    public Date getDtAcceptMaxLogsError() {
        return dtAcceptMaxLogsError;
    }

    public void acceptedDtLogsFarm(Date dt) {
        if (dtAcceptMaxLogsFarm == null || dtAcceptMaxLogsFarm.before(dt)) dtAcceptMaxLogsFarm = dt;
    }
    
    public void acceptedDtLogsError(Date dt) {
        if (dtAcceptMaxLogsError == null || dtAcceptMaxLogsError.before(dt)) dtAcceptMaxLogsError = dt;
    }

    public ScanLogs(Date dtAcceptAfterLogsFarm, Date dtAcceptAfterLogsError, long timeMax) {
        this.dtAcceptAfterLogsFarm = dtAcceptAfterLogsFarm;
        this.dtAcceptAfterLogsError = dtAcceptAfterLogsError;
        this.timeMax = timeMax;
    }

    @Override
    public int hashCode() {
        return 1;
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
        return true;
    }

}