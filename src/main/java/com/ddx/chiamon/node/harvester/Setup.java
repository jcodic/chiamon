package com.ddx.chiamon.node.harvester;

import com.ddx.chiamon.node.harvester.data.Disk;
import com.ddx.chiamon.utils.SizeConv;
import com.ddx.chiamon.utils.Str;
import com.ddx.chiamon.utils.TimeConv;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ddx
 */
public class Setup extends com.ddx.chiamon.common.data.Setup {

    protected static Logger log = LogManager.getLogger(Setup.class);
    
    private List<Disk> disks = new ArrayList<>(24);

    private String nodeUid;
    
    public List<Disk> getDisks() {
        return disks;
    }

    public void setDisks(List<Disk> disks) {
        this.disks = disks;
    }

    public String getNodeUid() {
        return nodeUid;
    }

    public void setNodeUid(String nodeUid) {
        this.nodeUid = nodeUid;
    }

    private boolean isDiskIdUnique(String id) {
        
        for (Disk disk : disks) if (id.equals(disk.getUid())) return false; return true;
    }
    
    @Override
    public boolean parsePrefs() throws Exception {

        String tmp;
        
        nodeUid = prefs.node("init").get("node_id", null); if (nodeUid == null) return false;
        mainNodeURL = prefs.node("init").get("main_node_url", null); if (mainNodeURL == null) return false;
        mainNodeUser = prefs.node("init").get("main_node_user", null); if (mainNodeUser == null) return false;
        mainNodePassword = prefs.node("init").get("main_node_pwd", null); if (mainNodePassword == null) return false;
        tmp = prefs.node("init").get("main_node_send_stats_interval", null); if (tmp != null) Vars.mainNodeSendStatsInterval = TimeConv.strToTime(tmp);
        tmp = prefs.node("vars").get("io_time_max_scan", null); if (tmp != null) Vars.ioTimeMaxScan = TimeConv.strToTime(tmp);
        tmp = prefs.node("vars").get("io_time_max_read", null); if (tmp != null) Vars.ioTimeMaxRead = TimeConv.strToTime(tmp);
        tmp = prefs.node("vars").get("http_time_max_send", null); if (tmp != null) Vars.httpTimeMaxSend = TimeConv.strToTime(tmp);
        tmp = prefs.node("vars").get("main_thread_sleep_time", null); if (tmp != null) Vars.mainThreadSleepTime = TimeConv.strToTime(tmp);
        
        Disk commonDisk = new Disk();
        commonDisk.setRescanInterval(TimeConv.strToTime(prefs.node("disks").get("rescan_interval", null)));
        commonDisk.setReadInterval(TimeConv.strToTime(prefs.node("disks").get("read_interval", null)));
        commonDisk.setReadFiles(prefs.node("disks").getInt("read_files", commonDisk.getReadFiles()));
        commonDisk.setReadBlockSize(SizeConv.strToSize(prefs.node("disks").get("read_block_size", null)));
        commonDisk.setSmartsInterval(TimeConv.strToTime(prefs.node("disks").get("smarts_interval", null)));
        Vars.disksFileFilter = prefs.node("disks").get("file_filter", Vars.disksFileFilter);
        commonDisk.setSmartsCmd(prefs.node("disks").get("smarts_cmd", null));

        for (String diskNode : prefs.childrenNames()) if (diskNode.startsWith("disk_")) {
            
            Disk disk = new Disk();
            
            disk.setUid(prefs.node(diskNode).get("id", null));
            if (!isDiskIdUnique(disk.getUid())) {
                
                log.error("Disk id \""+disk.getUid()+"\" is not unique!");
                return false;
            }
            disk.setName(prefs.node(diskNode).get("name", null).replace("{id}", disk.getUid()));
            disk.setPath(prefs.node(diskNode).get("path", null).replace("{id}", disk.getUid()));
            disk.setSize(SizeConv.strToSize(prefs.node(diskNode).get("size", null)));
            tmp = prefs.node(diskNode).get("rescan_interval", null); disk.setRescanInterval(tmp!=null?TimeConv.strToTime(tmp):commonDisk.getRescanInterval());
            tmp = prefs.node(diskNode).get("read_interval", null); disk.setReadInterval(tmp!=null?TimeConv.strToTime(tmp):commonDisk.getReadInterval());
            disk.setReadFiles(prefs.node(diskNode).getInt("read_files", commonDisk.getReadFiles()));
            tmp = prefs.node(diskNode).get("read_block_size", null); disk.setReadBlockSize(tmp!=null?SizeConv.strToSize(tmp):commonDisk.getReadBlockSize());
            tmp = prefs.node(diskNode).get("smarts_interval", null); disk.setSmartsInterval(tmp!=null?TimeConv.strToTime(tmp):commonDisk.getSmartsInterval());
            tmp = prefs.node(diskNode).get("smarts_cmd", null); disk.setSmartsCmd(tmp!=null?tmp:commonDisk.getSmartsCmd());
            tmp = disk.getSmartsCmd(); if (tmp != null) disk.setSmartsCmd(tmp.replace("{path}", disk.getPath()));
            
            if (!disk.isDiskInfoValid()) {
                
                log.error("Disk \""+diskNode+"\" info is not valid!");
                return false;
            }

            disks.add(disk);
        }

        if (disks.isEmpty()) {
            
            log.warn("No disks found in configuration!");
            return false;
        }

        tmp = prefs.node("logs").get("rescan_interval", null); if (tmp != null) Vars.logsRescanInterval = TimeConv.strToTime(tmp);
        tmp = prefs.node("logs").get("rescan_time_max", null); if (tmp != null) Vars.logsTimeMaxScan = TimeConv.strToTime(tmp);
        Vars.logsFileFilter = prefs.node("logs").get("file_filter", Vars.logsFileFilter);
        Vars.logsPath = prefs.node("logs").get("path", Vars.logsPath);
        
        log.info("Node.ID ["+nodeUid+"]");

        String sp = "\n" + Str.getStringWPrefix("", 15, " ");
        
        for (Disk d : disks) {
            
            log.info("Monitoring new disk ["+d.getUid()+"]:" +
                    sp + Str.getStringWPrefix("name: ", 20, " ", true) + d.getName() +
                    sp + Str.getStringWPrefix("path: ", 20, " ", true) + d.getPath() +
                    sp + Str.getStringWPrefix("size: ", 20, " ", true) + SizeConv.sizeToStr(d.getSize()) +
                    sp + Str.getStringWPrefix("rescan interval: ", 20, " ", true) + TimeConv.timeToStr(d.getRescanInterval()) +
                    sp + Str.getStringWPrefix("read interval: ", 20, " ", true) + TimeConv.timeToStr(d.getReadInterval()) +
                    sp + Str.getStringWPrefix("read files: ", 20, " ", true) + d.getReadFiles() +
                    sp + Str.getStringWPrefix("read block size: ", 20, " ", true) + SizeConv.sizeToStr(d.getReadBlockSize()));
        }
        
        return true;
    }
    
}