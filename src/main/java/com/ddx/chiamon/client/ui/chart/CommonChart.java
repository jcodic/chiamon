package com.ddx.chiamon.client.ui.chart;

import com.ddx.chiamon.client.Vars;
import com.ddx.chiamon.client.data.task.GetFromMainNode;
import com.ddx.chiamon.client.ui.WindowTemplate;
import com.ddx.chiamon.client.utils.TaskUtils;
import com.ddx.chiamon.common.data.task.Task;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author ddx
 */
public abstract class CommonChart extends WindowTemplate {

    protected static Logger log = LogManager.getLogger(CommonChart.class);
    
    protected JPanel chartPanel;
    protected JPanel infoPanel;
    protected JPanel mainPanel;
    protected JLabel infoLabel;

    protected TimePeriodValuesCollection dataset;
    protected TimePeriodValues series;
    protected List<String> seriesNames;
    
    protected abstract GetFromMainNode createTask() throws Exception;
    protected abstract void parseLoadedData(GetFromMainNode task) throws Exception;
    protected abstract void createChart() throws Exception;
    
    protected void getChartData() {
        
        try {
            
            infoLabel.setText("Loading chart data...");
            GetFromMainNode task = createTask();
            TaskUtils.executeTask(task);
            Task taskf = task.getFutures().get();
            
            if (taskf.isFinished() && taskf.isSuccess()) {

                if (task.isHasResponse()) {

                    parseLoadedData(task);
                } else infoLabel.setText("Emptry response");

            } else {

                infoLabel.setText("Can't load chart data."+(task.getInfo()!=null?(" Info: "+task.getInfo()):""));
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }
    
    protected void initDataset() {
	
        dataset = new TimePeriodValuesCollection();
        seriesNames = new ArrayList<>();
    }

    protected void addToDataset(String name, List<XYItemDateDouble> items) {
	
        series = new TimePeriodValues(name);
	
        for (XYItemDateDouble item : items) {
	    
	    series.add(new Millisecond(item.getDt()), item.getValue());
            //(new TimeSeriesDataItem(new Day(item.getDt()), item.getValue()));
	}
        
        dataset.addSeries(series);
        seriesNames.add(name);
    }

    private void setAxisFontColor(Axis axis, Color fontColor) {
        if (!fontColor.equals(axis.getLabelPaint())) {
            axis.setLabelPaint(fontColor);
            axis.setLabelFont(createFont());
        }
        if (!fontColor.equals(axis.getTickLabelPaint())) {
            axis.setTickLabelPaint(fontColor);
            axis.setTickLabelFont(createFont());
        }
    }    
    
    protected JFreeChart createChart(XYDataset dataset, String chartTitle, String xAxisLabel, String yAxisLabel) {
	
        boolean showLegend = true;
        boolean createURL = false;
        boolean createTooltip = false;

	
        JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle,
                xAxisLabel, yAxisLabel, dataset, showLegend, createTooltip, createURL);
        
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.DARK_GRAY);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);
        
        chart.setBorderPaint(Color.DARK_GRAY);
        chart.setBackgroundPaint(Color.DARK_GRAY);
        chart.getTitle().setPaint(Color.WHITE);
        chart.getTitle().setFont(new Font(Vars.tableFont, Font.BOLD, 12));
        
        setAxisFontColor(plot.getDomainAxis(), Color.WHITE);
        setAxisFontColor(plot.getRangeAxis(), Color.WHITE);
        
        DateAxis domainAxis = (DateAxis)plot.getDomainAxis(); 
        domainAxis.setDateFormatOverride(new SimpleDateFormat("ddMMM HH:mm")); 
        
        XYItemRenderer renderer = plot.getRenderer();
        
        renderer.setSeriesPaint(0, Color.ORANGE);
        renderer.setSeriesPaint(1, Color.GREEN);
        renderer.setSeriesPaint(2, Color.CYAN);
        renderer.setSeriesPaint(3, Color.RED);
        renderer.setSeriesPaint(4, Color.PINK);
        
        chart.getLegend().setBackgroundPaint(Color.DARK_GRAY);
        chart.getLegend().setItemPaint(Color.WHITE);
        chart.getLegend().setItemFont(chart.getLegend().getItemFont().deriveFont(11.0f));
        
	ValueMarker zeroMarker = new ValueMarker(0.0d, Color.LIGHT_GRAY, new java.awt.BasicStroke(2.0f));
	plot.addRangeMarker(zeroMarker);
	
	return chart;
    }

    protected void createInfoPanel() {

        infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout());
        infoPanel.add(infoLabel = createLabel());
    }
    
    public void composePanels() {
    
        GridBagConstraints fillBag = new GridBagConstraints();
        fillBag.weightx = 1.0;
        fillBag.weighty = 1.0;
        fillBag.gridx = 0;
        fillBag.gridy = 0;
        fillBag.gridheight = 1;
        fillBag.gridwidth = 1;
        fillBag.fill = GridBagConstraints.BOTH;
        fillBag.anchor = GridBagConstraints.CENTER;

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        chartPanel = new JPanel();
        chartPanel.setLayout(new GridLayout(1, 1));

        mainPanel.add(chartPanel, fillBag);

        createInfoPanel();
        
        fillBag.weightx = 1.0;
        fillBag.weighty = 0.02;
        fillBag.gridy++;
        fillBag.fill = GridBagConstraints.NONE;
        fillBag.anchor = GridBagConstraints.EAST;

        mainPanel.add(infoPanel, fillBag);
        
        _cBoth();
        c.gridx = 0;
        c.gridy = 0;

        setLayout(new GridLayout(1, 1));
        add(mainPanel);
    }

    public void run() {
        
        composePanels();
        
        new Thread() {
            @Override
            public void run() {
                getChartData();
            }
        }.start();
    }
    
}