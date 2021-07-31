package com.ddx.chiamon.common.data;

import java.util.Random;
import java.util.prefs.Preferences;

/**
 *
 * @author ddx
 */
public abstract class Setup {

    public static final Random RND = new Random(System.currentTimeMillis());
    
    protected Preferences prefs;
    protected String mainNodeURL;
    protected String mainNodeUser;
    protected String mainNodePassword;

    public Preferences getPrefs() {
        return prefs;
    }

    public void setPrefs(Preferences prefs) {
        this.prefs = prefs;
    }

    public String getMainNodeURL() {
        return mainNodeURL;
    }

    public void setMainNodeURL(String mainNodeURL) {
        this.mainNodeURL = mainNodeURL;
    }

    public String getMainNodeUser() {
        return mainNodeUser;
    }

    public void setMainNodeUser(String mainNodeUser) {
        this.mainNodeUser = mainNodeUser;
    }

    public String getMainNodePassword() {
        return mainNodePassword;
    }

    public void setMainNodePassword(String mainNodePassword) {
        this.mainNodePassword = mainNodePassword;
    }

    public abstract boolean parsePrefs() throws Exception;
    
}