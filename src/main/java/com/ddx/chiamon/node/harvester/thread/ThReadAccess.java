package com.ddx.chiamon.node.harvester.thread;

import com.ddx.chiamon.node.harvester.data.task.ReadAccess;
import com.ddx.chiamon.node.harvester.utils.DiskUtils;
import java.util.concurrent.Callable;

/**
 *
 * @author ddx
 */
public class ThReadAccess<Task> implements Callable<Task> {
    
    private final Task task;
    
    @Override
    public Task call() throws Exception {
        
        DiskUtils.readDisk((ReadAccess)task);
        return task;
    }

    public ThReadAccess(Task task) {
        this.task = task;
    }

}