package com.ddx.chiamon.node.harvester.tools;

import com.ddx.chiamon.common.data.LogErrorEntry;
import com.ddx.chiamon.common.data.LogFarmEntry;
import com.ddx.chiamon.node.harvester.Vars;
import com.ddx.chiamon.node.harvester.data.task.ScanLogs;
import com.ddx.chiamon.node.harvester.utils.LogUtils;
import com.ddx.chiamon.utils.TimeConv;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ddx
 */
public class LogsFarmShow {

    public static final String DPSTR = "yyyy-MM-dd HH:mm:ss";
    public static DateFormat DP = new SimpleDateFormat(DPSTR);
    
    private int proofs = 0;
    private int time = 0;
    private long period = 0;
    private boolean showErrors = true;
    
    public void run() throws Exception {
        
        Date dtFrom = null;
        if (period > 0) dtFrom = new Date(System.currentTimeMillis() - period);
        
        ScanLogs task = new ScanLogs(null, null, 2 * Vars.MINUTE);
        LogUtils.scanLogs(task);
        if (task.isSuccess()) {
            
            if (task.isHasLogsFarm()) {
                
                int total = 0;
                int totalFarmed = 0;
                int totalProofs = 0;
                
                for (LogFarmEntry item : task.getLogsFarm()) {

                    if (item.getProofs() < proofs ||
                        item.getTime() < time ||
                        (dtFrom != null && dtFrom.after(item.getDt()))
                        ) continue;
                    
                    total++;
                    totalFarmed += item.getPlots();
                    totalProofs += item.getProofs();
                    
                    System.out.println(DP.format(item.getDt())+" ["+
                            item.getPlots()+"] plots used within block ["+item.getBlock()+"] "+
                            item.getProofs()+" proofs found within ["+TimeConv.timeToStr(item.getTime())+"] "+
                            "total plots = "+item.getPlotsTotal()
                            );
                }
                
                System.out.println("Farm records: "+total);
                System.out.println("Farmed plots: "+totalFarmed);
                System.out.println("Proofs found: "+totalProofs);
                
            } else {
                
                System.out.println("No farm records.");
            }
            
            if (showErrors) {
                
                if (task.isHasLogsError()) {

                    int total = 0;

                    for (LogErrorEntry item : task.getLogsError()) {

                        if (dtFrom != null && dtFrom.after(item.getDt())) continue;
                        
                        total++;
                        //System.out.println(DP.format(item.getDt()));
                    }

                    System.out.println("Errors: "+total);

                } else {

                    System.out.println("No error records.");
                }
            }
            
        } else {
            
            System.out.println("Task error ["+task.getInfo()+"]");
        }
    }
    
    private static void printInfo() {
        
        System.out.println("Usage: logsfarm_show <logs_path> <logs_file_filter> [options]");
        System.out.println("Possible options:");
        System.out.println(" proofs=N (proofs >= N)");
        System.out.println(" time=N (farm time >= N)");
        System.out.println(" period=N (period used, ex: 24h)");
    }

    public void processArg(String arg) throws Exception {
        
        String argProofs = "proofs=";
        String argTime = "time=";
        String argPeriod = "period=";
        String argErrors = "errors=";
        if (arg.startsWith(argProofs)) proofs = Integer.parseInt(arg.substring(argProofs.length())); else
        if (arg.startsWith(argTime)) time = Integer.parseInt(arg.substring(argTime.length())); else
        if (arg.startsWith(argPeriod)) period = TimeConv.strToTime(arg.substring(argPeriod.length())); else
        if (arg.startsWith(argErrors)) showErrors = Boolean.parseBoolean(arg.substring(argErrors.length())); else
        System.out.println("Unknown argument: "+arg);
    }

    public void processArgs(String[] args, int startIndex) throws Exception {
        
        for (int i = startIndex; i < args.length; i++) processArg(args[i]);
    }

    public void setLogsPath(String logsPath) {
        Vars.logsPath = logsPath;
    }

    public void setLogsFiles(String logsFiles) {
        Vars.logsFileFilter = logsFiles;
    }

    public static void main(String[] args) {
        
        try {

            if (args == null || args.length < 2) {
                
                printInfo();
                return;
            }
            
            LogsFarmShow tool = new LogsFarmShow();
            tool.setLogsPath(args[0]);
            tool.setLogsFiles(args[1]);
            tool.processArgs(args, 2);
            tool.run();
            
        } catch (Exception ex) { ex.printStackTrace(); }
        
    }
    
}