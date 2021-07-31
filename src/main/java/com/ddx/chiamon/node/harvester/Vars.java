package com.ddx.chiamon.node.harvester;

/**
 *
 * @author ddx
 */
public class Vars extends com.ddx.chiamon.common.data.Vars {

    public static long ioTimeMaxScan = 1 * MINUTE;
    public static long ioTimeMaxRead = 1 * MINUTE;
    public static long httpTimeMaxSend = 2 * MINUTE;
    public static long mainThreadSleepTime = 500;
    public static long mainNodeSendStatsInterval = 30 * 1000; // ms
    public static String disksFileFilter = ".*plot$";
    
    // logs
    public static String logsPath;
    public static long logsRescanInterval = 0; // ms
    public static long logsTimeMaxScan = 1 * MINUTE;
    public static String logsFileFilter = ".*log$";

}