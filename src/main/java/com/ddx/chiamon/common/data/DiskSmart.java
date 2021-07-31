package com.ddx.chiamon.common.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;

/**
 *
 * @author ddx
 */
public class DiskSmart {

    private Date dt;
    
    public static final int NO_DATA = -1;
    
    public static final String[] SMART_ATTRS = new String[]{
        "Raw_Read_Error_Rate_VALUE",
        "Raw_Read_Error_Rate_RAW_VALUE",
        "Seek_Error_Rate_VALUE",
        "Seek_Error_Rate_RAW_VALUE",
        "Power_On_Hours_RAW_VALUE",
        "Temperature_Celsius_VALUE",
        "Temperature_Celsius_RAW_VALUE",
        "Reallocated_Sector_Count_VALUE",
        "Reallocated_Sector_Count_RAW_VALUE",
        "Spin_Retry_Count_VALUE",
        "Spin_Retry_Count_RAW_VALUE",
        "Reallocated_Event_Count_VALUE",
        "Reallocated_Event_Count_RAW_VALUE",
        "Current_Pending_Sector_VALUE",
        "Current_Pending_Sector_RAW_VALUE",
        "Offline_Uncorrectable_VALUE",
        "Offline_Uncorrectable_RAW_VALUE"
    };

    public int Raw_Read_Error_Rate_VALUE = NO_DATA;
    public long Raw_Read_Error_Rate_RAW_VALUE = NO_DATA;
    
    public int Seek_Error_Rate_VALUE = NO_DATA;
    public long Seek_Error_Rate_RAW_VALUE = NO_DATA;
    
    public int Power_On_Hours_RAW_VALUE = NO_DATA;
    
    public int Temperature_Celsius_VALUE = NO_DATA;
    public int Temperature_Celsius_RAW_VALUE = NO_DATA;
    
    public int Reallocated_Sector_Count_VALUE = NO_DATA;
    public long Reallocated_Sector_Count_RAW_VALUE = NO_DATA;
    
    public int Spin_Retry_Count_VALUE = NO_DATA;
    public long Spin_Retry_Count_RAW_VALUE = NO_DATA;
    
    public int Reallocated_Event_Count_VALUE = NO_DATA;
    public long Reallocated_Event_Count_RAW_VALUE = NO_DATA;

    public int Current_Pending_Sector_VALUE = NO_DATA;
    public long Current_Pending_Sector_RAW_VALUE = NO_DATA;
    
    public int Offline_Uncorrectable_VALUE = NO_DATA;
    public long Offline_Uncorrectable_RAW_VALUE = NO_DATA;
    
    @JsonIgnore 
    public Object getValueByName(String name) {
        
        switch (name) {
            
            case "Raw_Read_Error_Rate_VALUE" : return Raw_Read_Error_Rate_VALUE;
            case "Raw_Read_Error_Rate_RAW_VALUE" : return Raw_Read_Error_Rate_RAW_VALUE;
            case "Seek_Error_Rate_VALUE" : return Seek_Error_Rate_VALUE;
            case "Seek_Error_Rate_RAW_VALUE" : return Seek_Error_Rate_RAW_VALUE;
            case "Power_On_Hours_RAW_VALUE" : return Power_On_Hours_RAW_VALUE;
            case "Temperature_Celsius_VALUE" : return Temperature_Celsius_VALUE;
            case "Temperature_Celsius_RAW_VALUE" : return Temperature_Celsius_RAW_VALUE;
            case "Reallocated_Sector_Count_VALUE" : return Reallocated_Sector_Count_VALUE;
            case "Reallocated_Sector_Count_RAW_VALUE" : return Reallocated_Sector_Count_RAW_VALUE;
            case "Spin_Retry_Count_VALUE" : return Spin_Retry_Count_VALUE;
            case "Spin_Retry_Count_RAW_VALUE" : return Spin_Retry_Count_RAW_VALUE;
            case "Reallocated_Event_Count_VALUE" : return Reallocated_Event_Count_VALUE;
            case "Reallocated_Event_Count_RAW_VALUE" : return Reallocated_Event_Count_RAW_VALUE;
            case "Current_Pending_Sector_VALUE" : return Current_Pending_Sector_VALUE;
            case "Current_Pending_Sector_RAW_VALUE" : return Current_Pending_Sector_RAW_VALUE;
            case "Offline_Uncorrectable_VALUE" : return Offline_Uncorrectable_VALUE;
            case "Offline_Uncorrectable_RAW_VALUE" : return Offline_Uncorrectable_RAW_VALUE;
            default : return -1;
        }
    }
    
    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }
    
    
    
}