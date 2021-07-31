package com.ddx.chiamon.client.data.comparator;

import com.ddx.chiamon.client.data.Disk;
import java.util.Comparator;

/**
 *
 * @author ddx
 */
public class DiskComparator implements Comparator<Disk> {
    
    @Override
    public int compare(Disk item1, Disk item2) {

        Long v1 = item1.getSize();
        Long v2 = item2.getSize();
        int cmp1 = v2.compareTo(v1);

        return cmp1==0?item1.getUid().compareTo(item2.getUid()):cmp1;
    }
}