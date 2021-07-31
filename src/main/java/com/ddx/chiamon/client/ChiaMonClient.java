package com.ddx.chiamon.client;

import com.ddx.chiamon.client.auto.AutoScript;
import com.ddx.chiamon.client.data.task.GetFromMainNode;
import com.ddx.chiamon.client.ui.DetailedInfo;
import com.ddx.chiamon.client.ui.WindowMain;
import com.ddx.chiamon.client.ui.WindowTemplate;
import com.ddx.chiamon.client.utils.HttpUtils;
import com.ddx.chiamon.client.utils.TaskUtils;
import com.ddx.chiamon.client.data.NodePacket;
import com.ddx.chiamon.common.data.task.Task;
import com.ddx.chiamon.utils.Refresh;
import com.ddx.chiamon.utils.TimeConv;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.prefs.Preferences;
import javax.swing.UIManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;

/**
 *
 * @author ddx
 */
public class ChiaMonClient {

    protected static Logger log = LogManager.getLogger(ChiaMonClient.class);

    private final Setup setup = new Setup();

    private Refresh mainNodeGetStatsRef;
    private final Set<Task> tasks = new HashSet<>();
    
    private volatile boolean stopRequested;
    private DetailedInfo detailedInfoWindow;
    
    public boolean init() throws Exception {
        
        if (!setup.parsePrefs()) return false;
        
        HttpUtils.init(setup);

        mainNodeGetStatsRef = new Refresh(Vars.mainNodeGetStatsInterval);
        
        return true;
    }

    private void nodesInfoRefreshed(NodePacket packet, String info) throws Exception {
        
        log.info("New node info received: "+(packet!=null && packet.isHasNodes()?packet.getNodes().size():"empty"));
        detailedInfoWindow.dataRefreshed(packet, info);
    }

    private void requestStop() {
        
        stopRequested = true;
    }
    
    private boolean stopRequested() {
        
        return stopRequested;
    }
    
    private Task getPreviousTask(Task current) {
        
        for (Task task : tasks) if (task.equals(current)) return task;
        return null;
    }        
    
    private void refreshTimers() throws Exception {
        
        if (Refresh.timePassed(mainNodeGetStatsRef)) {

            GetFromMainNode task = new GetFromMainNode(GetFromMainNode.GET_HARVESTER_NODES, Vars.httpTimeMaxGet, setup);
            Task prevTask = getPreviousTask(task);
            if (prevTask == null) {

                task.addSimpleParam("period", Vars.statsCollectTime);
                tasks.add(task);
                log.info("Task "+task.getClass().getSimpleName()+" ["+task.getId()+"] added.");
            } else log.warn("Task "+task.getClass().getSimpleName()+" requested, but previous task ["+prevTask.getId()+"] not finished!");
        }
    }

    private void processTasks() throws Exception {
        
        List<Task> finishedTasks = new LinkedList<>();
        
        for (Task task : tasks) {
            
            Future<Task> futures = task.getFutures();
            
            if (futures == null) { // not started yet, start
                
                TaskUtils.executeTask(task);
                continue;
            }
            
            if (!futures.isDone()) continue; // processing now
            
            if (!task.isFinished()) task.markAsFinished(false, "Task timed out."); // timed out
            
            finishedTasks.add(task);
            log.info("Finished task "+task.getClass().getSimpleName()+" ["+task.getId()+"] success ["+task.isSuccess()+
                    "] time used ["+TimeConv.timeToStr(task.getTimeUsed())+"]"+
                    (task.getInfo()!=null?(" info ["+task.getInfo()+"]"):"")
                    );

            if (task instanceof GetFromMainNode) {

                GetFromMainNode xtask = (GetFromMainNode)task;
                if (xtask.getPacketUid().equals(GetFromMainNode.GET_HARVESTER_NODES)) 
                    
                    nodesInfoRefreshed(xtask.isSuccess() && xtask.isHasResponse()?HttpUtils.getMapper().readValue(xtask.getResponse(), NodePacket.class):null, xtask.getInfo());
            }
        }
        
        tasks.removeAll(finishedTasks);
    }
    
    public void runUI() throws Exception {
        
        UIManager.put("Menu.font", WindowTemplate.createFont());
        UIManager.put("MenuItem.font", WindowTemplate.createFont());
        
        WindowMain windowMain = new WindowMain() {
            @Override
            public void closeAppRequest() {
                requestStop();
                dispose();
            }
        };
        windowMain.setSize(new Dimension(Vars.windowWidth, Vars.windowHeight));
        windowMain.setSetup(setup);
        windowMain.run();
        //windowMain.setUndecorated(true);
        //windowMain.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        windowMain.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                requestStop();
            }
        });
        
        detailedInfoWindow = new DetailedInfo();
        detailedInfoWindow.composePanels("Requesting data from "+setup.getMainNodeURL()+" ...");
        windowMain.getObjectsPanel().add(detailedInfoWindow, WindowTemplate.getGridBagConstraintsMax());
        
        windowMain.setVisible(true);
        
        if (setup.getAutoScript() != null) new AutoScript(setup, windowMain).execute();
    }
    
    public void run() throws Exception {

        runUI();
        
        while (!stopRequested()) {
	    
            refreshTimers();
            processTasks();
            
            Thread.sleep(Vars.mainThreadSleepTime);
        }
	
        log.info("Client shut down.");
    }
    
    public Preferences getPrefs() {
        return setup.getPrefs();
    }

    public void setPrefs(Preferences prefs) {
        setup.setPrefs(prefs);
    }

    public static void main(String[] args) {

        try {

            String logConfig = System.getProperty("log4j.configurationFile");
            if (logConfig != null) log.info("Using log configuration file: " + logConfig);
                
            ChiaMonClient client = new ChiaMonClient();
            
            if (args != null && args.length > 0) {
                
                String cmdIniPath = "ini=";
                for (String arg : args) {
                    if (arg.startsWith(cmdIniPath)) client.setPrefs(new IniPreferences(new Ini(new File(arg.substring(cmdIniPath.length())))));
		    else log.warn("unknown command : "+arg);
                }
            }
        
            if (client.getPrefs() == null) {
                log.error("No INI file, can't init client.");
                return;
            }
            
            if (!client.init()) {
                
                log.error("Can't init client.");
                return;
            }
            
            client.run();
            
        } catch (Exception ex) { log.error(ex.getMessage(), ex); }
    
    }
    
}