package com.ddx.chiamon.client.ui.chart;

import com.ddx.chiamon.client.Vars;
import com.ddx.chiamon.client.data.Disk;
import com.ddx.chiamon.client.data.DiskSmart;
import com.ddx.chiamon.client.data.Node;
import com.ddx.chiamon.client.data.task.GetFromMainNode;
import com.ddx.chiamon.client.utils.HttpUtils;
import com.ddx.chiamon.common.data.json.DateLong;
import com.fasterxml.jackson.core.type.TypeReference;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
import javax.swing.JComboBox;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author ddx
 */
public class SmartChart extends CommonOptionsChart {
    
    private List<Node> nodes;
    private Map<Disk, Boolean> disksSel;
    private Date dtFrom;
    private Date dtTo;
    private List<DiskSmart> items;
    private JComboBox optionSmart;
    private String optionSmartCurrent;

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
    protected void initOptionsPanel() {

        int selectedIndex = 0;
        
        for (int i = 0; i < DiskSmart.SMART_ATTRS.length; i++)  {
            
            if (DiskSmart.SMART_ATTRS[i].equals("Temperature_Celsius_RAW_VALUE")) selectedIndex = i;
        }
        
        optionSmart = createCombobox(DiskSmart.SMART_ATTRS);
        optionSmart.setSelectedIndex(selectedIndex);
        optionSmart.addItemListener(new ItemListener () {
                
            @Override
            public void itemStateChanged(ItemEvent event) {
               if (event.getStateChange() == ItemEvent.SELECTED) {
                   
                    try {
                        
                        forceRefresh();
                    } catch (Exception ex) { log.error(ex); }
               }
            }       
        });
        
        optionsPanel.add(Box.createHorizontalStrut(20));
        optionsPanel.add(createLabel("Smart attribute"));
        optionsPanel.add(optionSmart);
    }

    public void forceRefresh() throws Exception {
        
        createChart();
    }
    
    @Override
    protected GetFromMainNode createTask() throws Exception {
        
        String ids = "";
        
        for (Map.Entry<Disk, Boolean> entry : disksSel.entrySet()) {

            if (!entry.getValue()) continue;
            ids += (ids.length()>0?",":"")+entry.getKey().getId();
        }

        GetFromMainNode task = new GetFromMainNode(GetFromMainNode.GET_SMART_ACCESS, Vars.httpTimeMaxGet, setup);
        task.addSimpleParam("ids", ids);
        task.addSimpleParam("dtFrom", DateLong.getFormatter().format(dtFrom));
        if (dtTo != null) task.addSimpleParam("dtTo", DateLong.getFormatter().format(dtTo));
        
        return task;
    }
    
    @Override
    protected void parseLoadedData(GetFromMainNode task) throws Exception {
        
        items = HttpUtils.getMapper().readValue(task.getResponse(), new TypeReference<List<DiskSmart>>(){});
        int itemsLoaded = (items!=null?items.size():0);
        infoLabel.setText(itemsLoaded+" items loaded.");
        if (itemsLoaded > 0) createChart();
    }
    
    private List<XYItemDateDouble> createDataset(long id) throws Exception {

	List<XYItemDateDouble> ds = new ArrayList<>();
        
        if (items.isEmpty()) return ds;
        
        for (DiskSmart item : items) {

            if (id != -1 && item.getDiskId() != id) continue;
            ds.add(new XYItemDateDouble(item.getDt(), Double.valueOf(String.valueOf(item.getValueByName(optionSmartCurrent)))));
        }
        
	return ds;
    }
    
    @Override
    protected void createChart() throws Exception {
        
        optionSmartCurrent = DiskSmart.SMART_ATTRS[optionSmart.getSelectedIndex()];
        
        initDataset();
        
        for (Map.Entry<Disk, Boolean> entry : disksSel.entrySet()) {

            if (!entry.getValue()) continue;
            Disk disk = entry.getKey();
            addToDataset(disk.getUid(), createDataset(disk.getId()));
        }
        
        JFreeChart chart = createChart(dataset, optionSmartCurrent, "date", "value");
        ChartPanel panel = new ChartPanel(chart);
        
        chartPanel.removeAll();
        chartPanel.add(panel);
        
        mainPanel.revalidate();
    }

}