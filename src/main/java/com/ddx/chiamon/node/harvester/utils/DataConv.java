package com.ddx.chiamon.node.harvester.utils;

import com.ddx.chiamon.common.data.DiskSmart;
import com.ddx.chiamon.common.data.Node;
import com.ddx.chiamon.node.harvester.Setup;
import com.ddx.chiamon.node.harvester.data.Disk;
import com.ddx.chiamon.node.harvester.data.Plot;
import com.ddx.chiamon.node.harvester.data.task.ReadAccess;
import com.ddx.chiamon.node.harvester.data.task.SmartAccess;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author ddx
 */
public class DataConv {

    public static Node convSetupToCommonNode(Setup setup, List<ReadAccess> queueReadAccess, List<SmartAccess> queueSmartAccess) {
        
        Node node = new Node();
        List<com.ddx.chiamon.common.data.Disk> disks = new LinkedList<>();

        node.setDt(new Date());
        node.setUid(setup.getNodeUid());
        node.setDisks(disks);
        
        for (Disk disk : setup.getDisks()) {
            
            com.ddx.chiamon.common.data.Disk diskA = new com.ddx.chiamon.common.data.Disk();
            diskA.setDt(disk.getDtLastModified());
            diskA.setUid(disk.getUid());
            diskA.setName(disk.getName());
            diskA.setPath(disk.getPath());
            diskA.setSize(disk.getSize());
            
            List<Plot> plots = disk.getPlots();
            
            int count = 0;
            long size = 0;
            
            if (plots != null && !plots.isEmpty()) {
                
                for (Plot plot : plots) {
                    
                    if (plot.getFileSize() > 0) {
                        
                        count++;
                        size += plot.getFileSize();
                    }
                }
            }
            
            diskA.setPlotsCount(count);
            diskA.setPlotsSize(size);
            disks.add(diskA);
            
            for (ReadAccess item : queueReadAccess) {
                
                if (item.getDisk() == disk) {
                    
                    com.ddx.chiamon.common.data.ReadAccess ra = new com.ddx.chiamon.common.data.ReadAccess();

                    ra.setDt(new Date(item.getDtStart()));
                    ra.setFileName(item.getFileName());
                    ra.setSeekAt(item.getSeekAt());
                    ra.setReadSize(item.getReadSize());
                    ra.setReadTime(item.getReadTime());
                    ra.setSuccess(item.isSuccess());
                    ra.setInfo(item.getInfo());
                    
                    diskA.addReadAccess(ra);
                }
            }
            
            for (SmartAccess item : queueSmartAccess) {
                
                if (item.getDisk() == disk) {

                    DiskSmart ds = item.getSmart();
                    ds.setDt(new Date(item.getDtStart()));
                    diskA.addSmart(ds);
                }
            }

        }
        
        return node;
    }
    
}