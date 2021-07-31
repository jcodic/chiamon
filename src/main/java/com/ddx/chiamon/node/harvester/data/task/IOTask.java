package com.ddx.chiamon.node.harvester.data.task;

import com.ddx.chiamon.common.data.task.Task;
import com.ddx.chiamon.node.harvester.data.Disk;

/**
 *
 * @author ddx
 */
public class IOTask extends Task {

    protected Disk disk;

    public Disk getDisk() {
        return disk;
    }

    public void setDisk(Disk disk) {
        this.disk = disk;
    }

}