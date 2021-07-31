package com.ddx.chiamon.client.utils;

import com.ddx.chiamon.client.data.task.GetFromMainNode;
import com.ddx.chiamon.client.thread.ThGetFromMainNode;
import com.ddx.chiamon.common.data.task.Task;

/**
 *
 * @author ddx
 */
public class TaskUtils extends com.ddx.chiamon.utils.TaskUtils {

    public static void executeTask(Task task) throws Exception {

        if (task instanceof GetFromMainNode) {
            
            executeTask(task, new ThGetFromMainNode(task));
        }
    }
    
}