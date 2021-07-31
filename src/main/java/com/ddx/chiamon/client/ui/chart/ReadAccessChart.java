package com.ddx.chiamon.client.ui.chart;

import com.ddx.chiamon.client.Vars;
import com.ddx.chiamon.client.data.Disk;
import com.ddx.chiamon.client.data.Node;
import com.ddx.chiamon.client.data.ReadAccess;
import com.ddx.chiamon.client.data.task.GetFromMainNode;
import com.ddx.chiamon.client.utils.HttpUtils;
import com.ddx.chiamon.common.data.json.DateLong;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author ddx
 */
public class ReadAccessChart extends CommonChart {
    
    private List<Node> nodes;
    private Map<Disk, Boolean> disksSel;
    private Date dtFrom;
    private Date dtTo;
    private List<ReadAccess> items;

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public void setDisksSel(Map<Disk, Boolean> disksSel) {
        this.disksSel = disksSel;
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
        
        for (Map.Entry<Disk, Boolean> entry : disksSel.entrySet()) {

            if (!entry.getValue()) continue;
            ids += (ids.length()>0?",":"")+entry.getKey().getId();
        }

        GetFromMainNode task = new GetFromMainNode(GetFromMainNode.GET_READ_ACCESS, Vars.httpTimeMaxGet, setup);
        task.addSimpleParam("ids", ids);
        task.addSimpleParam("dtFrom", DateLong.getFormatter().format(dtFrom));
        if (dtTo != null) task.addSimpleParam("dtTo", DateLong.getFormatter().format(dtTo));
        
        return task;
    }
    
    @Override
    protected void parseLoadedData(GetFromMainNode task) throws Exception {
        
        items = HttpUtils.getMapper().readValue(task.getResponse(), new TypeReference<List<ReadAccess>>(){});
        int itemsLoaded = (items!=null?items.size():0);
        infoLabel.setText(itemsLoaded+" items loaded.");
        if (itemsLoaded > 0) createChart();
    }
    
    private List<XYItemDateDouble> createDataset(long id) throws Exception {

	List<XYItemDateDouble> ds = new ArrayList<>();
        
        if (items.isEmpty()) return ds;
        
        for (ReadAccess item : items) {

            if (id != -1 && item.getDiskId() != id) continue;
            ds.add(new XYItemDateDouble(item.getDt(), (double)item.getReadTime() / 1000.0d));
        }
        
	return ds;
    }
    
    @Override
    protected void createChart() throws Exception {
        
        initDataset();
        
        for (Map.Entry<Disk, Boolean> entry : disksSel.entrySet()) {

            if (!entry.getValue()) continue;
            Disk disk = entry.getKey();
            addToDataset(disk.getUid(), createDataset(disk.getId()));
        }
        
        JFreeChart chart = createChart(dataset, "disk read access", "date", "time, seconds");
        ChartPanel panel = new ChartPanel(chart);
        
        chartPanel.add(panel);
        
        mainPanel.revalidate();
    }

}