package com.ddx.chiamon.node.harvester.thread;

import com.ddx.chiamon.node.harvester.data.task.ScanLogs;
import com.ddx.chiamon.node.harvester.utils.LogUtils;
import java.util.concurrent.Callable;

/**
 *
 * @author ddx
 */
public class ThScanLogs<Task> implements Callable<Task> {
    
    private final Task task;
    
    @Override
    public Task call() throws Exception {
        
        LogUtils.scanLogs((ScanLogs)task);
        return task;
    }

    public ThScanLogs(Task task) {
        this.task = task;
    }

}