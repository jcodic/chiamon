package com.ddx.chiamon.node.harvester.thread;

import com.ddx.chiamon.node.harvester.data.task.ScanAccess;
import com.ddx.chiamon.node.harvester.utils.DiskUtils;
import java.util.concurrent.Callable;

/**
 *
 * @author ddx
 */
public class ThScanAccess<Task> implements Callable<Task> {
    
    private final Task task;
    
    @Override
    public Task call() throws Exception {
        
        DiskUtils.scanDisk((ScanAccess)task);
        return task;
    }

    public ThScanAccess(Task task) {
        this.task = task;
    }

}