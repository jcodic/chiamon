package com.ddx.chiamon.node.harvester;

import com.ddx.chiamon.common.data.LogErrorEntry;
import com.ddx.chiamon.common.data.LogFarmEntry;
import com.ddx.chiamon.common.data.Node;
import com.ddx.chiamon.node.harvester.data.Disk;
import com.ddx.chiamon.node.harvester.data.task.IOTask;
import com.ddx.chiamon.node.harvester.data.task.ReadAccess;
import com.ddx.chiamon.node.harvester.data.task.ScanAccess;
import com.ddx.chiamon.node.harvester.data.task.SendToMainNode;
import com.ddx.chiamon.common.data.task.Task;
import com.ddx.chiamon.node.harvester.data.task.ScanLogs;
import com.ddx.chiamon.node.harvester.data.task.SmartAccess;
import com.ddx.chiamon.node.harvester.utils.DataConv;
import com.ddx.chiamon.node.harvester.utils.HttpUtils;
import com.ddx.chiamon.node.harvester.utils.TaskUtils;
import com.ddx.chiamon.utils.Refresh;
import com.ddx.chiamon.utils.TimeConv;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.prefs.Preferences;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author ddx
 */
public class HarvesterNode {

    protected static Logger log = LogManager.getLogger(HarvesterNode.class);

    private final Setup setup = new Setup();
    private boolean refLater = false; // delay disks read when service starts
    
    private Refresh mainNodeSendStatsRef;
    private Refresh logsRescanRef;
    private final Map<Disk,Refresh> disksRescanRef = new HashMap<>();
    private final Map<Disk,Refresh> disksReadRef = new HashMap<>();
    private final Map<Disk,Refresh> disksSmartsRef = new HashMap<>();
    private final Set<Task> tasks = new HashSet<>();
    private List<ReadAccess> queueReadAccess = new LinkedList<>(); // finished ReadAccess objects
    private List<SmartAccess> queueSmartAccess = new LinkedList<>(); // finished ReadAccess objects
    private List<LogFarmEntry> queueLogsFarm = new LinkedList<>();
    private List<LogErrorEntry> queueLogsError = new LinkedList<>();
    private Date dtAcceptAfterLogsError;
    private Date dtAcceptAfterLogsFarm;

    public boolean init() throws Exception {
        
        if (!setup.parsePrefs()) return false;
        
        HttpUtils.init(setup);
        
        mainNodeSendStatsRef = new Refresh(Vars.mainNodeSendStatsInterval);
        if (refLater) Refresh.timePassed(mainNodeSendStatsRef);

        logsRescanRef = new Refresh(Vars.logsRescanInterval);
        
        for (Disk disk : setup.getDisks()) {
            
            if (disk.getRescanInterval() > 0) disksRescanRef.put(disk, new Refresh(disk.getRescanInterval()));
            if (disk.getReadInterval() > 0) disksReadRef.put(disk, new Refresh(disk.getReadInterval()));
            if (disk.getSmartsInterval()> 0) disksSmartsRef.put(disk, new Refresh(disk.getSmartsInterval()));
        }

        return true;
    }

    private boolean stopRequested() {
        
        return false;
    }
    
    private Task getPreviousTask(Task current) {
        
        for (Task task : tasks) if (task.equals(current)) return task;
        return null;
    }        
    
    private void refreshTimers() throws Exception {
        
        for (Map.Entry<Disk,Refresh> item : disksRescanRef.entrySet()) {
        
            Disk disk = item.getKey();

            if (Refresh.timePassed(item.getValue())) {

                IOTask task = new ScanAccess(disk, Vars.ioTimeMaxScan);
                Task prevTask = getPreviousTask(task);
                if (prevTask == null) {

                    tasks.add(task);
                    log.info("Disk ["+disk.getUid()+"] task "+task.getClass().getSimpleName()+" ["+task.getId()+"] added.");
                } else log.warn("Disk ["+disk.getUid()+"] task "+task.getClass().getSimpleName()+" requested, but previous task ["+prevTask.getId()+"] not finished!");
            }
        }

        for (Map.Entry<Disk,Refresh> item : disksReadRef.entrySet()) {

            Disk disk = item.getKey();

            if (Refresh.timePassed(item.getValue()) && disk.isDiskHasPlots()) {

                IOTask task = new ReadAccess(disk, Vars.ioTimeMaxRead);
                Task prevTask = getPreviousTask(task);
                if (prevTask == null) {

                    tasks.add(task);
                    log.info("Disk ["+disk.getUid()+"] task "+task.getClass().getSimpleName()+" ["+task.getId()+"] added.");
                } else log.warn("Disk ["+disk.getUid()+"] task "+task.getClass().getSimpleName()+" requested, but previous task ["+prevTask.getId()+"] not finished!");
            }
        }

        for (Map.Entry<Disk,Refresh> item : disksSmartsRef.entrySet()) {

            Disk disk = item.getKey();

            if (Refresh.timePassed(item.getValue())) {

                IOTask task = new SmartAccess(disk, Vars.ioTimeMaxRead);
                Task prevTask = getPreviousTask(task);
                if (prevTask == null) {

                    tasks.add(task);
                    log.info("Disk ["+disk.getUid()+"] task "+task.getClass().getSimpleName()+" ["+task.getId()+"] added.");
                } else log.warn("Disk ["+disk.getUid()+"] task "+task.getClass().getSimpleName()+" requested, but previous task ["+prevTask.getId()+"] not finished!");
            }
        }

        if (Refresh.timePassed(mainNodeSendStatsRef)) {

            SendToMainNode task = new SendToMainNode(SendToMainNode.PACKET_FULL_NODE_INFO, Vars.httpTimeMaxSend);
            Task prevTask = getPreviousTask(task);
            if (prevTask == null) {

                Node node = DataConv.convSetupToCommonNode(setup, queueReadAccess, queueSmartAccess);
                node.setLogsFarm(queueLogsFarm);
                node.setLogsError(queueLogsError);
                task.setNode(node);
                task.setSetup(setup);
                tasks.add(task);
                queueReadAccess = new LinkedList<>(); // reset queue
                queueSmartAccess = new LinkedList<>(); // reset queue
                queueLogsFarm = new LinkedList<>(); // reset queue 
                queueLogsError = new LinkedList<>(); // reset queue 
                log.info("Task "+task.getClass().getSimpleName()+" ["+task.getId()+"] added.");
            } else log.warn("Task "+task.getClass().getSimpleName()+" requested, but previous task ["+prevTask.getId()+"] not finished!");
        }

        if (Refresh.timePassed(logsRescanRef)) {

            ScanLogs task = new ScanLogs(dtAcceptAfterLogsFarm, dtAcceptAfterLogsError, Vars.logsTimeMaxScan);
            Task prevTask = getPreviousTask(task);
            if (prevTask == null) {

                tasks.add(task);
                log.info("Task "+task.getClass().getSimpleName()+" ["+task.getId()+"] added.");
            } else log.warn("Task "+task.getClass().getSimpleName()+" requested, but previous task ["+prevTask.getId()+"] not finished!");
        }
    }

    private void processTasks() throws Exception {
        
        List<Task> finishedTasks = new LinkedList<>();
        
        for (Task task : tasks) {
            
            Future<Task> futures = task.getFutures();
            
            if (futures == null) { // not started yet, start
                
                TaskUtils.executeTask(task);
                continue;
            }
            
            if (!futures.isDone()) continue; // processing now
            
            if (!task.isFinished()) task.markAsFinished(false, "Task timed out."); // timed out
            
            finishedTasks.add(task);
            log.info("Finished task "+task.getClass().getSimpleName()+" ["+task.getId()+"] success ["+task.isSuccess()+
                    "] time used ["+TimeConv.timeToStr(task.getTimeUsed())+"]"+
                    (task.getInfo()!=null?(" info ["+task.getInfo()+"]"):"")
                    );

            if (task instanceof ReadAccess) {

                queueReadAccess.add((ReadAccess)task);
            } else if (task instanceof SmartAccess) {

                queueSmartAccess.add((SmartAccess)task);
            } else if (task instanceof ScanLogs) {
                
                ScanLogs xtask = (ScanLogs)task;
                if (xtask.isHasLogsFarm()) {
                    
                    queueLogsFarm.addAll(xtask.getLogsFarm());
                    dtAcceptAfterLogsFarm = xtask.getDtAcceptMaxLogsFarm();
                }
                if (xtask.isHasLogsError()) {
                    
                    queueLogsError.addAll(xtask.getLogsError());
                    dtAcceptAfterLogsError = xtask.getDtAcceptMaxLogsError();
                }
            }
        }
        
        tasks.removeAll(finishedTasks);
    }
    
    public void run() throws Exception {

        while (!stopRequested()) {
	    
            refreshTimers();
            processTasks();
            
            Thread.sleep(Vars.mainThreadSleepTime);
        }
	
        log.info("Service shut down.");
    }
    
    public Preferences getPrefs() {
        return setup.getPrefs();
    }

    public void setPrefs(Preferences prefs) {
        setup.setPrefs(prefs);
    }

    public void setRefLater(boolean refLater) {
        this.refLater = refLater;
    }

    public static void main(String[] args) {

        try {

            String logConfig = System.getProperty("log4j.configurationFile");
            if (logConfig != null) log.info("Using log configuration file: " + logConfig);
                
            HarvesterNode service = new HarvesterNode();
            
            if (args != null && args.length > 0) {
                
                String cmdIniPath = "ini=";
                String cmdRefLater = "reflater";
                for (String arg : args) {
                    if (arg.startsWith(cmdIniPath)) service.setPrefs(new IniPreferences(new Ini(new File(arg.substring(cmdIniPath.length())))));
                    else if (arg.startsWith(cmdRefLater)) service.setRefLater(true); 
		    else log.warn("unknown command : "+arg);
                }
            }
        
            if (service.getPrefs() == null) {
                log.error("No INI file, can't init service.");
                return;
            }
            
            if (!service.init()) {
                
                log.error("Can't init service.");
                return;
            }
            
            service.run();
            
        } catch (Exception ex) { log.error(ex.getMessage(), ex); }
    }
    
}