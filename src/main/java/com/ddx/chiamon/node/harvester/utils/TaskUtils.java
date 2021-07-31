package com.ddx.chiamon.node.harvester.utils;

import com.ddx.chiamon.node.harvester.data.task.ReadAccess;
import com.ddx.chiamon.node.harvester.data.task.ScanAccess;
import com.ddx.chiamon.node.harvester.data.task.SendToMainNode;
import com.ddx.chiamon.common.data.task.Task;
import com.ddx.chiamon.node.harvester.data.task.ScanLogs;
import com.ddx.chiamon.node.harvester.data.task.SmartAccess;
import com.ddx.chiamon.node.harvester.thread.ThReadAccess;
import com.ddx.chiamon.node.harvester.thread.ThScanAccess;
import com.ddx.chiamon.node.harvester.thread.ThScanLogs;
import com.ddx.chiamon.node.harvester.thread.ThSendToMainNode;
import com.ddx.chiamon.node.harvester.thread.ThSmartAccess;

/**
 *
 * @author ddx
 */
public class TaskUtils extends com.ddx.chiamon.utils.TaskUtils {

    public static void executeTask(Task task) throws Exception {

        if (task instanceof ReadAccess) executeTask(task, new ThReadAccess((ReadAccess)task)); else
        if (task instanceof ScanAccess) executeTask(task, new ThScanAccess((ScanAccess)task)); else
        if (task instanceof SmartAccess) executeTask(task, new ThSmartAccess((SmartAccess)task)); else
        if (task instanceof ScanLogs) executeTask(task, new ThScanLogs((ScanLogs)task)); else
        if (task instanceof SendToMainNode) executeTask(task, new ThSendToMainNode((SendToMainNode)task));
    }

}