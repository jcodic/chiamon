package com.ddx.chiamon.node.harvester.data.task;

import com.ddx.chiamon.node.harvester.data.Disk;
import java.util.Objects;

/**
 *
 * @author ddx
 */
public class ScanAccess extends IOTask {
    
    public ScanAccess(Disk disk, long timeMax) {
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
        final ScanAccess other = (ScanAccess) obj;
        if (!Objects.equals(this.disk, other.disk)) {
            return false;
        }
        return true;
    }

}