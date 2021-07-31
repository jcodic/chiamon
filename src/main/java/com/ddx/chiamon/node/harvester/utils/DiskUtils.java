package com.ddx.chiamon.node.harvester.utils;

import com.ddx.chiamon.node.harvester.Vars;
import com.ddx.chiamon.node.harvester.data.Disk;
import com.ddx.chiamon.node.harvester.data.Plot;
import com.ddx.chiamon.node.harvester.data.task.ReadAccess;
import com.ddx.chiamon.node.harvester.data.task.ScanAccess;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ddx
 */
public class DiskUtils {

    protected static Logger log = LogManager.getLogger(DiskUtils.class);
    
    public static String completePath(String path) {
        
        if (path == null || path.length() == 0) return "";
        if (!path.endsWith(java.io.File.separator)) return path + java.io.File.separator;
        return path;
    }

    public static void scanDisk(ScanAccess result) throws Exception {
        
        long t1 = System.currentTimeMillis();

        Disk disk = result.getDisk();
        File dir = new File(disk.getPath());
        if (!dir.exists()) {
        
            result.markAsFinished(false, "Path ["+disk.getPath()+"] isn't exist!");
            return;
        }
        
        List<Plot> plots = new ArrayList<>(500);
        
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                
                return name.matches(Vars.disksFileFilter);
            }
        };
        
        for (File file : dir.listFiles(filter)) plots.add(new Plot(file.getName(), file.length()));

        long t2 = System.currentTimeMillis();

        disk.setPlots(plots);
        result.setTimeUsed(t2 - t1);
        result.markAsFinished(true);
    }
    
    public static void readDisk(ReadAccess result) throws Exception {

        Disk disk = result.getDisk();
        if (!disk.isDiskHasPlots()) {
            
            result.markAsFinished(false, "Disk have no plots.");
            return;
        }
        
        long totalReadSize = 0;
        long totalReadTime = 0;
        List<Plot> plots = disk.getPlots();
        
        for (int n = 0; n < disk.getReadFiles(); n++) {
            
            Plot plot = plots.get(ThreadLocalRandom.current().nextInt(plots.size()));
            
            long maxSeekAt = plot.getFileSize() - disk.getReadBlockSize();
            if (maxSeekAt < 0) {
                
                result.markAsFinished(false, "Plot size is less than read block size!");
                return;
            }
            long seekAt = maxSeekAt>0?ThreadLocalRandom.current().nextLong(maxSeekAt):0;
            
            result.setFileName(plot.getFileName());
            result.setSeekAt(seekAt);
            
            long t1 = System.currentTimeMillis();
            
            FileInputStream fis = new FileInputStream(completePath(disk.getPath()) + plot.getFileName());
            fis.getChannel().position(seekAt);
            BufferedInputStream bis = new BufferedInputStream(fis, (int)disk.getReadBlockSize());
            byte[] bf = new byte[(int)disk.getReadBlockSize()];
            totalReadSize += bis.read(bf);

            bis.close();
            fis.close();
            
            long t2 = System.currentTimeMillis();
            
            totalReadTime += t2 - t1;
        }
            
        result.setReadSize(totalReadSize);
        result.setReadTime(totalReadTime);
        result.setTimeUsed(totalReadTime);
        result.markAsFinished(true);
    }
    
}