package com.ddx.chiamon.client.ui.chart;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 *
 * @author ddx
 */
public abstract class CommonOptionsChart extends CommonChart {

    protected JPanel optionsPanel;
    protected JSplitPane mainSplitPane;
    
    protected void initOptionsPanel() {
    }
    
    @Override
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

        optionsPanel = new JPanel();
        optionsPanel.setLayout(new FlowLayout());
        initOptionsPanel();

        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, new JScrollPane(optionsPanel), chartPanel);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setDividerLocation(44);
        mainSplitPane.setResizeWeight(0.0d);
        mainPanel.add(mainSplitPane, fillBag);

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

}