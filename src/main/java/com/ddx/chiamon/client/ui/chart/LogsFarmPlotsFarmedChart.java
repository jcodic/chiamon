package com.ddx.chiamon.client.ui.chart;

import com.ddx.chiamon.client.Vars;
import com.ddx.chiamon.client.data.LogFarmEntry;
import com.ddx.chiamon.client.data.Node;
import com.ddx.chiamon.client.data.task.GetFromMainNode;
import com.ddx.chiamon.client.utils.HttpUtils;
import com.ddx.chiamon.common.data.json.DateLong;
import com.fasterxml.jackson.core.type.TypeReference;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.data.time.Millisecond;

/**
 *
 * @author ddx
 */
public class LogsFarmPlotsFarmedChart extends CommonChart {
    
    private List<Node> nodes;
    private List<Boolean> nodesSel;
    private Date dtFrom;
    private Date dtTo;
    private List<LogFarmEntry> items;
    private Set<Date> proofsDt;

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public void setNodesSel(List<Boolean> nodesSel) {
        this.nodesSel = nodesSel;
    }

    public void setDtFrom(Date dtFrom) {
        this.dtFrom = dtFrom;
    }

    public void setDtTo(Date dtTo) {
        this.dtTo = dtTo;
    }


    @Override
    protected GetFromMainNode createTask() throws Exception {
        
        String ids = "";
        boolean allSel = nodesSel.get(0);
        
        for (int i = 1; i < nodesSel.size(); i++) {

            Node node = nodes.get(i-1);
            if (allSel || nodesSel.get(i)) ids += (ids.length()>0?",":"")+node.getId();
        }

        GetFromMainNode task = new GetFromMainNode(GetFromMainNode.GET_LOGSFARM, Vars.httpTimeMaxGet, setup);
        task.addSimpleParam("ids", ids);
        task.addSimpleParam("dtFrom", DateLong.getFormatter().format(dtFrom));
        if (dtTo != null) task.addSimpleParam("dtTo", DateLong.getFormatter().format(dtTo));
        
        return task;
    }
    
    @Override
    protected void parseLoadedData(GetFromMainNode task) throws Exception {
        
        items = HttpUtils.getMapper().readValue(task.getResponse(), new TypeReference<List<LogFarmEntry>>(){});
        int itemsLoaded = (items!=null?items.size():0);
        infoLabel.setText(itemsLoaded+" items loaded.");
        if (itemsLoaded > 0) createChart();
    }
    
    private List<XYItemDateDouble> createDataset(long id) throws Exception {

	List<XYItemDateDouble> ds = new ArrayList<>();
        
        if (items.isEmpty()) return ds;
        
        Date dtc = items.get(0).getDt();
        double summ = 0.0d;
        
        for (LogFarmEntry item : items) {

            if (id != -1 && item.getIdNode() != id) continue;

            Date dt = item.getDt();

            if (dt.after(dtc)) {
            
                ds.add(new XYItemDateDouble(dtc, summ));
                dtc = dt;
            }
            
            summ += item.getPlots();
            
            if (item.getProofs() > 0) proofFound(dt);
        }
        
        ds.add(new XYItemDateDouble(dtc, summ));
        
	return ds;
    }
    
    private void proofFound(Date dt) {
        
        if (proofsDt == null) proofsDt = new HashSet<>();
        proofsDt.add(dt);
    }
    
    @Override
    protected void createChart() throws Exception {
        
        initDataset();
        
        if (nodesSel.get(0)) {
            
            addToDataset("all nodes", createDataset(-1));
        }
        
        for (int i = 1; i < nodesSel.size(); i++) {
            
            if (nodesSel.get(i)) {
                
                Node node = nodes.get(i-1);
                addToDataset(node.getUid(), createDataset(node.getId()));
            }
        }
        
        JFreeChart chart = createChart(dataset, "total plots farmed", "date", "plots farmed");
        
	if (proofsDt != null && Vars.chartShowProofLine) {
	    
	    for (Date dt : proofsDt) {
		
		ValueMarker marker = new ValueMarker(new Millisecond(dt).getFirstMillisecond(), Color.LIGHT_GRAY, new java.awt.BasicStroke(1.0f));
		chart.getXYPlot().addDomainMarker(marker);
	    }
	}
	
        ChartPanel panel = new ChartPanel(chart);
        
        chartPanel.add(panel);
        
        mainPanel.revalidate();
    }

}
