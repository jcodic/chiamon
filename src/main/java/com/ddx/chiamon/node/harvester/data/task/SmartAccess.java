package com.ddx.chiamon.node.harvester.data.task;

import com.ddx.chiamon.common.data.DiskSmart;
import com.ddx.chiamon.node.harvester.data.Disk;
import java.util.Objects;

/**
 *
 * @author ddx
 */
public class SmartAccess extends IOTask {

    private DiskSmart smart;

    public DiskSmart getSmart() {
        return smart;
    }

    public void setSmart(DiskSmart smart) {
        this.smart = smart;
    }

    public SmartAccess(Disk disk, long timeMax) {
        this.disk = disk;
        this.timeMax = timeMax;
    }

    @Override
    public int hashCode() {
        return disk.hashCode() + 100;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SmartAccess other = (SmartAccess) obj;
        if (!Objects.equals(this.disk, other.disk)) {
            return false;
        }
        return true;
    }

}