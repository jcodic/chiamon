package com.ddx.chiamon.node.harvester.thread;

import com.ddx.chiamon.node.harvester.data.task.SmartAccess;
import com.ddx.chiamon.node.harvester.utils.DiskSmartUtils;
import java.util.concurrent.Callable;

/**
 *
 * @author ddx
 */
public class ThSmartAccess<Task> implements Callable<Task> {
    
    private final Task task;
    
    @Override
    public Task call() throws Exception {
        
        DiskSmartUtils.readDiskSmart((SmartAccess)task);
        return task;
    }

    public ThSmartAccess(Task task) {
        this.task = task;
    }

}