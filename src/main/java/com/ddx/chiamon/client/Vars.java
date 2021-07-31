package com.ddx.chiamon.client;

import com.ddx.chiamon.client.utils.UI;
import java.awt.Color;
import java.text.SimpleDateFormat;
import javax.swing.UIManager;

/**
 *
 * @author ddx
 */
public class Vars extends com.ddx.chiamon.common.data.Vars {

    public static final SimpleDateFormat DTF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static long httpTimeMaxGet = 2 * MINUTE;
    public static long mainThreadSleepTime = 500;
    public static long mainNodeGetStatsInterval = 30 * 1000; // ms
    public static long statsCollectTime = 2 * HOUR;
    
    public static int windowWidth;
    public static int windowHeight;
    public static boolean windowMaximized = true;
    public static boolean windowChartMaximized = true;
 
    public static String tableFont = "Lucida Console";
    public static int tableFontSize = 11;
    public static Color EmptyColor = UI.stripAlpha(UIManager.getColor("Panel.background"));
    public static Color highLightColor = new Color(255, 252, 187);
    
    public static Color okColor = Color.decode("#ccffcc");
    public static Color warnColor = Color.decode("#ffcc99");
    
    public static int[] smartDiskTempNorm = new int[]{15, 50};
    
    public static boolean chartShowProofLine = false;

}