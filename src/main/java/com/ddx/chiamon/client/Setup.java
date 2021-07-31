package com.ddx.chiamon.client;

import com.ddx.chiamon.utils.TimeConv;
import com.ddx.chiamon.client.utils.UI;
import com.ddx.chiamon.utils.StringConv;
import java.awt.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ddx
 */
public class Setup extends com.ddx.chiamon.common.data.Setup {

    protected static Logger log = LogManager.getLogger(Setup.class);

    private String clientUid;
    private String[] autoScript;
    
    public String getClientUid() {
        return clientUid;
    }

    public String[] getAutoScript() {
        return autoScript;
    }

    @Override
    public boolean parsePrefs() throws Exception {

        String tmp;
        
        clientUid = prefs.node("init").get("client_id", null); if (clientUid == null) return false;
        mainNodeURL = prefs.node("init").get("main_node_url", null); if (mainNodeURL == null) return false;
        mainNodeUser = prefs.node("init").get("main_node_user", null); if (mainNodeUser == null) return false;
        mainNodePassword = prefs.node("init").get("main_node_pwd", null); if (mainNodePassword == null) return false;
        tmp = prefs.node("init").get("main_node_get_stats_interval", null); if (tmp != null) Vars.mainNodeGetStatsInterval = TimeConv.strToTime(tmp);
        tmp = prefs.node("vars").get("stats_collect_time", null); if (tmp != null) Vars.statsCollectTime = TimeConv.strToTime(tmp);
        tmp = prefs.node("vars").get("http_time_max_get", null); if (tmp != null) Vars.httpTimeMaxGet = TimeConv.strToTime(tmp);
        tmp = prefs.node("vars").get("main_thread_sleep_time", null); if (tmp != null) Vars.mainThreadSleepTime = TimeConv.strToTime(tmp);
        
        
        tmp = prefs.node("ui").get("color_ok", null); if (tmp != null) Vars.okColor = Color.decode("#"+tmp);
        tmp = prefs.node("ui").get("color_warn", null); if (tmp != null) Vars.warnColor = Color.decode("#"+tmp);
        Vars.windowWidth = prefs.node("ui").getInt("window_width", UI.getScreenWidth() / 2);
        Vars.windowHeight = prefs.node("ui").getInt("window_height", UI.getScreenHeight()/ 2);
        Vars.windowMaximized = prefs.node("ui").getBoolean("window_maximized", Vars.windowMaximized);
        Vars.windowChartMaximized = prefs.node("ui").getBoolean("window_chart_maximized", Vars.windowChartMaximized);
        tmp = prefs.node("ui").get("autoscript", null); if (tmp != null) autoScript = StringConv.String2Array(tmp);
        
        tmp = prefs.node("smart").get("hdd_temp_norm", null); Vars.smartDiskTempNorm = StringConv.String2IntRange(tmp);
        
        Vars.chartShowProofLine = prefs.node("ui").getBoolean("chart_show_proof", Vars.chartShowProofLine);
        
        return true;
    }
    
}