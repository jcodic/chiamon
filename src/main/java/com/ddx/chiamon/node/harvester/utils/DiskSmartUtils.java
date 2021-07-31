package com.ddx.chiamon.node.harvester.utils;

import com.ddx.chiamon.common.data.DiskSmart;
import com.ddx.chiamon.node.harvester.data.Disk;
import com.ddx.chiamon.node.harvester.data.task.SmartAccess;
import com.ddx.chiamon.utils.OSValidator;
import com.ddx.chiamon.utils.Str;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author ddx
 */
public class DiskSmartUtils {

    private static int parseFirstInt(String line) throws Exception {
        
        if (Str.isEmpty(line)) return -1;
        String[] values = line.trim().replaceAll(" +", " ").split(" "); 
        return parseInt(values[0]);
    }

    private static int parseInt(String line) {
        
        try {
            return Integer.parseInt(line);
        } catch (Exception ex) { return DiskSmart.NO_DATA; }
    }

    private static long parseLong(String line) {
        
        try {
            return Long.parseLong(line);
        } catch (Exception ex) { return (long)DiskSmart.NO_DATA; }
    }
    
    private static void parseSmartLine(String line, DiskSmart smart) throws Exception {
    
        if (line == null || line.length() < 10) return;
        
        final int VALUE_IND = 3;
        final int RAW_VALUE_IND = 9;
        
        String[] values = line.trim().replaceAll(" +", " ").split(" "); 
        if (values.length < RAW_VALUE_IND + 1) return;
        
        int attr = parseInt(values[0]);
        if (attr == DiskSmart.NO_DATA) return;
        
        switch (attr) {

            case 1      : smart.Raw_Read_Error_Rate_VALUE = parseInt(values[VALUE_IND]);
                          smart.Raw_Read_Error_Rate_RAW_VALUE = parseLong(values[RAW_VALUE_IND]);
                          break;
            case 5      : smart.Reallocated_Sector_Count_VALUE = parseInt(values[VALUE_IND]);
                          smart.Reallocated_Sector_Count_RAW_VALUE = parseLong(values[RAW_VALUE_IND]);
                          break;
            case 7      : smart.Seek_Error_Rate_VALUE = parseInt(values[VALUE_IND]);
                          smart.Seek_Error_Rate_RAW_VALUE = parseLong(values[RAW_VALUE_IND]);
                          break;
            case 9      : smart.Power_On_Hours_RAW_VALUE = parseInt(values[RAW_VALUE_IND]);
                          break;
            case 10     : smart.Spin_Retry_Count_VALUE = parseInt(values[VALUE_IND]);
                          smart.Spin_Retry_Count_RAW_VALUE = parseLong(values[RAW_VALUE_IND]);
                          break;
            case 194    : smart.Temperature_Celsius_VALUE = parseInt(values[VALUE_IND]);
                          smart.Temperature_Celsius_RAW_VALUE = parseFirstInt(values[RAW_VALUE_IND]);
                          break;
            case 196    : smart.Reallocated_Event_Count_VALUE = parseInt(values[VALUE_IND]);
                          smart.Reallocated_Event_Count_RAW_VALUE = parseLong(values[RAW_VALUE_IND]);
                          break;
            case 197    : smart.Current_Pending_Sector_VALUE = parseInt(values[VALUE_IND]);
                          smart.Current_Pending_Sector_RAW_VALUE = parseLong(values[RAW_VALUE_IND]);
                          break;
            case 198    : smart.Offline_Uncorrectable_VALUE = parseInt(values[VALUE_IND]);
                          smart.Offline_Uncorrectable_RAW_VALUE = parseLong(values[RAW_VALUE_IND]);
                          break;
        }
    }
    
    public static void readDiskSmart(SmartAccess result) throws Exception {

        long t1 = System.currentTimeMillis();

        Disk disk = result.getDisk();
        
        if (Str.isEmpty(disk.getSmartsCmd())) {
        
            result.markAsFinished(false, "Disk smart read command isn't defined!");
            return;
        }

        DiskSmart smart = new DiskSmart();
        
        try {
        
            String[] pars = null;
            
            switch (OSValidator.getOS()) {
                
                case OSValidator.OS_WINDOWS : 
                    pars = new String[]{"cmd", "/c", disk.getSmartsCmd()};
                    break;
                case OSValidator.OS_UNIX : 
                    pars = new String[]{"bash", "-c", disk.getSmartsCmd()};
                    break;
            }

            if (pars == null) {

                result.markAsFinished(false, "OS is not recognized!");
                return;
            }
            
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(pars);
            pr.waitFor(10, TimeUnit.SECONDS);
            
            InputStream iss = pr.getErrorStream();
            int errStreamLen = iss.available();
            if (errStreamLen > 0) {
                
                byte bf[] = new byte[errStreamLen];
                iss.read(bf);
                result.markAsFinished(false, new String(bf)); return;
            }
            
            BufferedReader bf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;

            while ((line = bf.readLine()) != null) {

                parseSmartLine(line, smart);
            }
            bf.close();
        
        } catch (Exception ex) { result.markAsFinished(false, ex.getMessage()); return; }
        
        long t2 = System.currentTimeMillis();

        result.setSmart(smart);
        result.setTimeUsed(t2 - t1);
        result.markAsFinished(true);
    }
    
}