package com.ddx.chiamon.node.harvester.thread;

import com.ddx.chiamon.node.harvester.data.task.SendToMainNode;
import com.ddx.chiamon.node.harvester.utils.HttpUtils;
import java.util.concurrent.Callable;

/**
 *
 * @author ddx
 */
public class ThSendToMainNode<Task> implements Callable<Task> {
    
    private final Task task;
    
    @Override
    public Task call() throws Exception {
        
        HttpUtils.sendMainNode((SendToMainNode)task);
        return task;
    }

    public ThSendToMainNode(Task task) {
        this.task = task;
    }

}