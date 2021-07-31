package com.ddx.chiamon.node.harvester.utils;

import com.ddx.chiamon.common.data.LogErrorEntry;
import com.ddx.chiamon.common.data.LogFarmEntry;
import com.ddx.chiamon.node.harvester.Vars;
import com.ddx.chiamon.node.harvester.data.task.ScanLogs;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ddx
 */
public class LogUtils {
    
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat(FORMAT);
    private static final String[] farmSig = new String[]{"chia.harvester.harvester:","INFO","plots were eligible for farming","... Found","proofs","Time:","s.","Total","plots"};
    private static final String[] errorSig = new String[]{"ERROR"};
    
    public static LogFarmEntry getFarmEntry(String s, Date dtAcceptAfter) {

        try {
        
            int[] sigIndex = new int[farmSig.length];

            boolean foundAll = true;

            for (int i = 0; i < farmSig.length; i++) {

                int prevIndex = i==0?0:(sigIndex[i-1] + farmSig[i-1].length());
                if ((sigIndex[i] = s.indexOf(farmSig[i], prevIndex)) == -1) {

                    foundAll = false;
                    break;
                }
            }

            if (!foundAll) return null;
            if (sigIndex[0] < FORMAT.length()) return null;

            LogFarmEntry result = new LogFarmEntry();

            result.setDt(FORMATTER.parse(s.substring(0,FORMAT.length()).replace('T', ' ')));
            if (dtAcceptAfter != null && !result.getDt().after(dtAcceptAfter)) return null;
            result.setPlots(Integer.parseInt(s.substring(sigIndex[1]+farmSig[1].length(), sigIndex[2]).trim()));
            result.setBlock(s.substring(sigIndex[2]+farmSig[2].length(), sigIndex[3]).trim());
            result.setProofs(Integer.parseInt(s.substring(sigIndex[3]+farmSig[3].length(), sigIndex[4]).trim()));
            result.setTime((int)(1000*Double.parseDouble(s.substring(sigIndex[5]+farmSig[5].length(), sigIndex[6]).trim())));
            result.setPlotsTotal(Integer.parseInt(s.substring(sigIndex[7]+farmSig[7].length(), sigIndex[8]).trim()));

            return result;
            
        } catch (Exception ex) { return null; }
    }
    
    public static LogErrorEntry getErrorEntry(String s, Date dtAcceptAfter) {

        try {
        
            int[] sigIndex = new int[errorSig.length];

            boolean foundAll = true;

            for (int i = 0; i < errorSig.length; i++) {

                int prevIndex = i==0?0:(sigIndex[i-1] + errorSig[i-1].length());
                if ((sigIndex[i] = s.indexOf(errorSig[i], prevIndex)) == -1) {

                    foundAll = false;
                    break;
                }
            }

            if (!foundAll) return null;
            if (sigIndex[0] < FORMAT.length()) return null;

            LogErrorEntry result = new LogErrorEntry();

            result.setDt(FORMATTER.parse(s.substring(0,FORMAT.length()).replace('T', ' ')));
            if (dtAcceptAfter != null && !result.getDt().after(dtAcceptAfter)) return null;

            return result;
            
        } catch (Exception ex) { return null; }
    }
    
    public static void scanLogs(ScanLogs result) throws Exception {
        
        long t1 = System.currentTimeMillis();

        File dir = new File(Vars.logsPath);
        if (!dir.exists()) {
        
            result.markAsFinished(false, "Path ["+Vars.logsPath+"] isn't exist!");
            return;
        }
        
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                
                return name.matches(Vars.logsFileFilter);
            }
        };
        
        for (File file : dir.listFiles(filter)) {
            
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    
                    if (line == null || line.length() < FORMAT.length()) continue;
                    LogFarmEntry item = getFarmEntry(line, result.getDtAcceptAfterLogsFarm());
                    if (item != null) result.addLogFarm(item);
                    LogErrorEntry item2 = getErrorEntry(line, result.getDtAcceptAfterLogsError());
                    if (item2 != null) result.addLogError(item2);
                }
            }
        }

        long t2 = System.currentTimeMillis();

        result.setTimeUsed(t2 - t1);
        result.markAsFinished(true);
    }

}