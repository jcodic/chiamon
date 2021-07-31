package com.ddx.chiamon.common.data.json;

import java.text.SimpleDateFormat;

/**
 *
 * @author ddx
 */
public class DateLong {

    public static final String FORMATTER_STR = "yyyy-MM-dd HH:mm:ss";

    public static SimpleDateFormat getFormatter() {
        
        return new SimpleDateFormat(FORMATTER_STR);
    }
    
}