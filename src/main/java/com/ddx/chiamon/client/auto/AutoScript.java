package com.ddx.chiamon.client.auto;

import com.ddx.chiamon.client.Setup;
import com.ddx.chiamon.client.Vars;
import com.ddx.chiamon.client.data.Disk;
import com.ddx.chiamon.client.data.Node;
import com.ddx.chiamon.client.ui.chart.LogsFarmPlotsFarmedChart;
import com.ddx.chiamon.client.ui.chart.LogsFarmPlotsTotalChart;
import com.ddx.chiamon.client.ui.chart.ReadAccessChart;
import com.ddx.chiamon.client.utils.UI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ddx
 */
public class AutoScript {
    
    private static Logger log = LogManager.getLogger(AutoScript.class);

    public static final String AUTO_CHART_LOGSFARM_PLOTS_FARMED = "logsfarm_plots_farmed_chart";
    public static final String AUTO_CHART_LOGSFARM_PLOTS_TOTAL = "logsfarm_plots_total_chart";
    public static final String AUTO_CHART_READACCESS = "readaccess_chart";
    
    private final Setup setup;
    private final JFrame parent;
    
    private void runLogsFarmPlotsFarmedChart(List<Node> nodes) {
    
        int len = nodes.size() + 1;
        List<Boolean> selNodes = new ArrayList<>(len);
        for (int i = 0; i < len; i++) selNodes.add(true);
        
        LogsFarmPlotsFarmedChart chart = new LogsFarmPlotsFarmedChart();
        chart.setSetup(setup);
        chart.setNodes(nodes);
        chart.setDtFrom(new Date(System.currentTimeMillis() - Vars.statsCollectTime));
        chart.setNodesSel(selNodes);
        chart.run();

        UI.showDialog(UI.getFrame(parent), chart, "Plots farmed", new int[]{UI.OPTION_NOT_MODAL, Vars.windowChartMaximized?UI.OPTION_MAXIMIZED:UI.OPTION_BLANK});
    }
    
    private void runLogsFarmPlotsTotalChart(List<Node> nodes) {
    
        int len = nodes.size() + 1;
        List<Boolean> selNodes = new ArrayList<>(len);
        for (int i = 0; i < len; i++) selNodes.add(true);
        
        LogsFarmPlotsTotalChart chart = new LogsFarmPlotsTotalChart();
        chart.setSetup(setup);
        chart.setNodes(nodes);
        chart.setDtFrom(new Date(System.currentTimeMillis() - Vars.statsCollectTime));
        chart.setNodesSel(selNodes);
        chart.run();

        UI.showDialog(UI.getFrame(parent), chart, "Plots total", new int[]{UI.OPTION_NOT_MODAL, Vars.windowChartMaximized?UI.OPTION_MAXIMIZED:UI.OPTION_BLANK});
    }
    
    private void runReadAccessChart(List<Node> nodes) {
    
        Map<Disk, Boolean> selDisks = new HashMap<>();
        for (Node node : nodes)
            if (node.isNodeHasDisks())
                for (Disk disk : node.getDisks())
                    selDisks.put(disk, true);
        
        ReadAccessChart chart = new ReadAccessChart();
        chart.setSetup(setup);
        chart.setNodes(nodes);
        chart.setDtFrom(new Date(System.currentTimeMillis() - Vars.statsCollectTime));
        chart.setDisksSel(selDisks);
        chart.run();

        UI.showDialog(UI.getFrame(parent), chart, "Disks read access", new int[]{UI.OPTION_NOT_MODAL, Vars.windowChartMaximized?UI.OPTION_MAXIMIZED:UI.OPTION_BLANK});
    }
    
    public void executeSingle(String script) {
        
        switch (script) {
            
            case AUTO_CHART_LOGSFARM_PLOTS_FARMED : 

                log.info("Executing autoscript: " + script);
                
                new Fetcher(setup){
                    @Override
                    public void nodesLoaded(List<Node> items) {
                        runLogsFarmPlotsFarmedChart(items);
                    }
                }.getNodesList();
                break;
                
            case AUTO_CHART_LOGSFARM_PLOTS_TOTAL : 

                log.info("Executing autoscript: " + script);
                
                new Fetcher(setup){
                    @Override
                    public void nodesLoaded(List<Node> items) {
                        runLogsFarmPlotsTotalChart(items);
                    }
                }.getNodesList();
                break;
                
            case AUTO_CHART_READACCESS : 

                log.info("Executing autoscript: " + script);
                
                new Fetcher(setup){
                    @Override
                    public void nodesLoaded(List<Node> items) {
                        runReadAccessChart(items);
                    }
                }.getNodesAndDisksList();
                break;
                
            default :
                
                log.info("Unknown autoscript: " + script);
        }
    }

    public void execute() {
        
        for (String script : setup.getAutoScript()) {
            
            new Thread() {
                @Override
                public void run() {
                    executeSingle(script);
                }
            }.start();
        }
    }
    
    public AutoScript(Setup setup, JFrame parent) {
        
        this.setup = setup;
        this.parent = parent;
    }

}