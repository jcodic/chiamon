package com.ddx.chiamon.utils;

/**
 *
 * @author ddx
 */
public class OSValidator {
    
    public static final String OS_UNKNOWN = "unknown";
    public static final String OS_WINDOWS = "win";
    public static final String OS_UNIX = "uni";
    public static final String OS_SOLARIS = "sol";
    public static final String OS_MAC = "osx";
    
    private static String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }    

    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }

    public static boolean isSolaris() {
        return (OS.indexOf("sunos") >= 0);
    }    

    public static String getOS(){
        if (isWindows()) { return OS_WINDOWS; } 
        else if (isMac()) { return OS_MAC; }
        else if (isUnix()) { return OS_UNIX; }
        else if (isSolaris()) { return OS_SOLARIS; } 
        else { return OS_UNKNOWN; }
    }
    
}