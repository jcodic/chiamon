package com.ddx.chiamon.client.thread;

import com.ddx.chiamon.client.data.task.GetFromMainNode;
import com.ddx.chiamon.client.utils.HttpUtils;
import com.ddx.chiamon.common.data.task.Task;
import java.util.concurrent.Callable;

/**
 *
 * @author ddx
 */
public class ThGetFromMainNode implements Callable<Task> {

    private final Task task;
    
    @Override
    public Task call() throws Exception {
     
        HttpUtils.getMainNodeResponse((GetFromMainNode)task);
        return task;
    }

    public ThGetFromMainNode(Task task) {
        this.task = task;
    }

}