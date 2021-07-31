package com.ddx.chiamon.utils;

import com.ddx.chiamon.common.data.task.Task;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author ddx
 */
public class TaskUtils {

    public static void executeTask(Task task, Callable<Task> clz) throws Exception {
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        task.setFutures(executor.invokeAll(Arrays.asList(clz), task.getTimeMax(), TimeUnit.MILLISECONDS).get(0));
        executor.shutdown();
    }
    
}