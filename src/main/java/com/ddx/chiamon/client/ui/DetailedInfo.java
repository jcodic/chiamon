package com.ddx.chiamon.client.ui;

import com.ddx.chiamon.client.Vars;
import com.ddx.chiamon.client.data.Disk;
import com.ddx.chiamon.client.data.Node;
import com.ddx.chiamon.client.data.NodePacket;
import com.ddx.chiamon.client.utils.Utils;
import com.ddx.chiamon.utils.SizeConv;
import com.ddx.chiamon.utils.TimeConv;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author ddx
 */
public class DetailedInfo extends WindowTemplate {
    
    private static final SimpleDateFormat DTF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private class ComponentLine {
        
        private final DecimalFormat df;
        
        public JButton node;
        public JButton disk;
        public JButton size;
        public JButton diskTemp;
        public JButton plots;
        public JButton plotsSize;
        public JButton scanAge;
        public JButton raAge;
        public JButton raTime;
        public JButton raTimeAvg;
        public JButton raSucceed;
        public JButton raFailed;
        public JButton lfAge;
        public JButton lfPlotsFarmed;
        public JButton lfPlotsTotal;
        public JButton lfPlotsFT;
        public JButton lfProofsTotal;
        public JButton lfTimeAvg;
        public JButton leAge;
        public JButton leErrorsTotal;
        
        public void setNode(int v) {
            node.setText(String.valueOf(v));
        }

        public void setDisk(int v) {
            disk.setText(String.valueOf(v));
        }

        public void setSize(long v) {
            try {
                size.setText(SizeConv.sizeToStrSimple(v));
            } catch (Exception ex) { size.setText("?"); }
        }
        
        public void setDiskTemp(int v) {
            diskTemp.setText(v>=1?String.valueOf(v):"-");
            if (v >= 1) diskTemp.setBackground(v >= Vars.smartDiskTempNorm[0] && v <= Vars.smartDiskTempNorm[1] ? Vars.okColor : Vars.warnColor);
        }

        public void setPlots(int v) {
            plots.setText(df.format(v));
        }

        public void setPlotsSize(long v) {
            try {
                plotsSize.setText(SizeConv.sizeToStrSimple(v));
            } catch (Exception ex) { plotsSize.setText("?"); }
        }
        
        public void setScanAge(Date dt) {
            try {
                scanAge.setText(dt==null?"-":TimeConv.timeToStrSimple(System.currentTimeMillis() - dt.getTime()));
            } catch (Exception ex) { scanAge.setText("?"); }
        }
        
        public void setRaAge(Date dt) {
            try {
                raAge.setText(dt==null?"-":TimeConv.timeToStrSimple(System.currentTimeMillis() - dt.getTime()));
            } catch (Exception ex) { scanAge.setText("?"); }
        }

        public void setRaTime(long v) {
            try {
                raTime.setText(v==-1?"-":TimeConv.timeToStr(v));
            } catch (Exception ex) { scanAge.setText("?"); }
        }

        public void setRaTimeAvg(long v) {
            try {
                raTimeAvg.setText(v==-1?"-":TimeConv.timeToStr(v));
            } catch (Exception ex) { scanAge.setText("?"); }
        }

        public void setRaSucceed(int v) {
            raSucceed.setText(df.format(v));
        }

        public void setRaFailed(int v) {
            raFailed.setText(df.format(v));
        }

        public void setLfAge(Date dt) {
            try {
                lfAge.setText(dt==null?"-":TimeConv.timeToStrSimple(System.currentTimeMillis() - dt.getTime()));
            } catch (Exception ex) { scanAge.setText("?"); }
        }

        public void setLfPlotsFarmed(int v) {
            lfPlotsFarmed.setText(df.format(v));
        }
        
        public void setLfPlotsTotal(int v) {
            lfPlotsTotal.setText(df.format(v));
        }

        public void setLfPlotsFT(double v) {
            lfPlotsFT.setText(String.valueOf(com.ddx.chiamon.utils.Utils.RoundResult(v, 1)));
        }

        public void setLfProofsTotal(int v) {
            lfProofsTotal.setText(df.format(v));
        }
        
        public void setLfTimeAvg(int v) {
            try {
                lfTimeAvg.setText(v==-1?"-":TimeConv.timeToStrSimple(v));
            } catch (Exception ex) { scanAge.setText("?"); }
        }

        public void setLeAge(Date dt) {
            try {
                leAge.setText(dt==null?"-":TimeConv.timeToStrSimple(System.currentTimeMillis() - dt.getTime()));
            } catch (Exception ex) { scanAge.setText("?"); }
        }

        public void setLeErrorsTotal(int v) {
            leErrorsTotal.setText(df.format(v));
        }

        public ComponentLine() {
            
            df = (DecimalFormat)NumberFormat.getInstance(Locale.US);
            DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            df.setDecimalFormatSymbols(symbols);
        }
        
    }
    
    private JPanel gridPanel;
    private JPanel infoPanel;
    private JPanel mainPanel;
    private JLabel infoLabelRefreshed;
    private Map<Node,ComponentLine> nodesMap;
    private Map<Disk,ComponentLine> disksMap;
    private ComponentLine footer;
    private long lastNodeFingerprint = -1;
    
    private JPanel createEmptyGrid(String msg) {
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        _cBoth();
        panel.add(createLabel(msg), c);
        
        return panel;
    }

    private void createGridLineV(JPanel panel, int allColumnsCount) {
        
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = allColumnsCount;
        _cBoth();
        panel.add(createEmptyVPanel(), c);
        c.gridy++;
        c.gridwidth = 1;
    }

    private void createGridLineV1(JPanel panel, int allColumnsCount) {
        
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 1;
        _cBoth();
        panel.add(createEmptyVPanel(), c);

        c.gridx = 2;
        c.gridwidth = allColumnsCount-2;
        _cBoth();
        panel.add(createEmptyVPanel(), c);
        c.gridy++;
        c.gridwidth = 1;
    }

    private void createGridLine(JPanel panel, boolean footer, boolean highlight, JComponent... items) {
        
        _cBoth();
        c.gridx = 0;
        c.gridwidth = 1;
        panel.add(createEmptyHPanel(), c);
        
        for (JComponent item : items) {
            
            JComponent cmp = item==null?createLabel(highlight?" ":""):item;
            if (highlight) cmp.setBackground(Vars.highLightColor);
            
            _cBoth();
            c.gridx++;
            panel.add(cmp, c);
            _cBoth();
            c.gridx++;
            panel.add(createEmptyHPanel(), c);
        }

        if (footer) {
            
            int allColumnsCount = c.gridx+1;
            createGridLineV(panel, allColumnsCount);
        }
    }

    private void createGridLineS(JPanel panel, String... items) {
        
        JComponent[] cmps = new JComponent[items.length];
        for (int i = 0; i < items.length; i++) cmps[i] = createLabelHeader(items[i]);
        
        createGridLine(panel, true, false, cmps);
    }
    
    private JPanel createGrid(List<Node> nodes) {

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        
        nodesMap = new HashMap<>();
        disksMap = new HashMap<>();

        JLabel label;
        
        String[] header = new String[]{
            "uid",
            "uid","size","scan age","\u00B0C",
            "count","size",
            "age","time.last","time.avg","succeed","failed",
            "age","plots farmed","plots total","f/t","proofs","time.avg",
            "age","total"    
            };
        int allColumnsCount = 1 + header.length * 2;

        c.gridy = 0;
        
        createGridLineV(panel, allColumnsCount);
        
        _cBoth();
        c.gridx = 0;
        c.gridwidth = 1;
        panel.add(createEmptyHPanel(), c);
        
        _cMiddle();
        c.gridx++;
        panel.add(createLabelHeader("node"), c);
        _cBoth();
        c.gridx++;
        c.gridwidth = 1;
        panel.add(createEmptyHPanel(), c);

        _cMiddle();
        c.gridx++;
        c.gridwidth = 7;
        panel.add(createLabelHeader("disk"), c);
        _cBoth();
        c.gridx += 7;
        c.gridwidth = 1;
        panel.add(createEmptyHPanel(), c);

        _cMiddle();
        c.gridx++;
        c.gridwidth = 3;
        panel.add(createLabelHeader("plots"), c);
        _cBoth();
        c.gridx += 3;
        c.gridwidth = 1;
        panel.add(createEmptyHPanel(), c);

        _cMiddle();
        c.gridx++;
        c.gridwidth = 9;
        panel.add(createLabelHeader("disk read access"), c);
        _cBoth();
        c.gridx += 9;
        c.gridwidth = 1;
        panel.add(createEmptyHPanel(), c);

        _cMiddle();
        c.gridx++;
        c.gridwidth = 11;
        panel.add(createLabelHeader("farming logs"), c);
        _cBoth();
        c.gridx += 11;
        c.gridwidth = 1;
        panel.add(createEmptyHPanel(), c);

        _cMiddle();
        c.gridx++;
        c.gridwidth = 3;
        panel.add(createLabelHeader("error logs"), c);
        _cBoth();
        c.gridx += 3;
        c.gridwidth = 1;
        panel.add(createEmptyHPanel(), c);

        createGridLineV(panel, allColumnsCount);

        createGridLineS(panel,header);
        
        int footerNodesTotal = 0;
        int footerDisksTotal = 0;
        long footerDisksSizeTotal = 0;
        int footerPlotsTotal = 0;
        long footerPlotsSizeTotal = 0;
        int footerLfPlotsFarmed = 0;
        int footerLfPlotsTotal = 0;
        int footerLfProofsTotal = 0;
        int footerLeErrorsTotal = 0;

        for (Node node : nodes) {
            
            ComponentLine cmp = new ComponentLine();
            nodesMap.put(node, cmp);

            cmp.disk = createButtonC();
            cmp.disk.setForeground(cmp.disk.getForeground().brighter());
            cmp.size = createButtonR();
            cmp.diskTemp = createButtonC();
            cmp.plots = createButtonR();
            cmp.plotsSize = createButtonR();
            cmp.scanAge = createButtonC();
            cmp.scanAge.setToolTipText("<html><font face=\""+Vars.tableFont+"\">"+DTF.format(node.getDt())+"</html>");
            cmp.lfAge = createButtonC(); 
            cmp.lfPlotsTotal = createButtonR();
            cmp.lfPlotsFarmed = createButtonR();
            cmp.lfPlotsFT = createButtonC();
            cmp.lfProofsTotal = createButtonR();
            cmp.lfTimeAvg = createButtonC(); 
            cmp.leAge = createButtonC();
            cmp.leErrorsTotal = createButtonR();
        
            label = createLabelHeader(node.getUid());
            label.setToolTipText("<html><font face=\""+Vars.tableFont+"\">"+node.getIp()+"</html>");
            
            createGridLine(panel, true, true, label, cmp.disk, cmp.size, cmp.scanAge, cmp.diskTemp,
                    cmp.plots,cmp.plotsSize,
                    cmp.raAge,cmp.raTime,cmp.raTimeAvg,cmp.raSucceed,cmp.raFailed,
                    cmp.lfAge,cmp.lfPlotsFarmed,cmp.lfPlotsTotal,cmp.lfPlotsFT,cmp.lfProofsTotal,cmp.lfTimeAvg,
                    cmp.leAge,cmp.leErrorsTotal
                    );
            
            int disksTotal = 0;
            long disksSizeTotal = 0;
            int plotsTotal = 0;
            long plotsSizeTotal = 0;
            
            if (node.isNodeHasDisks()) {

                int i = 0;
                int j = node.getDisks().size();
                
                for (Disk disk : node.getDisks()) {

                    ComponentLine cmp2 = new ComponentLine();
                    disksMap.put(disk, cmp2);
                    
                    cmp2.size = createButtonR();
                    cmp2.diskTemp = createButtonC();
                    cmp2.plots = createButtonR();
                    cmp2.plotsSize = createButtonR();
                    cmp2.scanAge = createButtonC();
                    cmp2.scanAge.setToolTipText("<html><font face=\""+Vars.tableFont+"\">"+DTF.format(disk.getDt())+"</html>");
                    cmp2.raAge = createButtonC();
                    cmp2.raTime = createButtonC();
                    cmp2.raTimeAvg = createButtonC();
                    cmp2.raSucceed = createButtonC();
                    cmp2.raFailed = createButtonC();
                    
                    cmp2.setPlots(disk.getPlotsCount());
                    cmp2.setPlotsSize(disk.getPlotsSize());
                    cmp2.setSize(disk.getSize());
                    cmp2.setScanAge(disk.getDt());
                    cmp2.setDiskTemp(disk.getSmartsTemp());
                    cmp2.setRaAge(disk.getRaDt());
                    cmp2.setRaTime(disk.getRaTime());
                    cmp2.setRaTimeAvg(disk.getRaTimeAvg());
                    cmp2.setRaSucceed(disk.getRaSucceed());
                    cmp2.setRaFailed(disk.getRaFailed());

                    disksTotal++;
                    disksSizeTotal += disk.getSize();
                    plotsTotal += disk.getPlotsCount();
                    plotsSizeTotal += disk.getPlotsSize();
                    
                    label = createLabelHeader(disk.getUid());
                    label.setToolTipText("<html><font face=\""+Vars.tableFont+"\"><b>"+disk.getName()+"</b><br>"+disk.getPath()+"</html>");
                    
                    boolean lastDisk = ++i==j;
                    createGridLine(panel, lastDisk, false, createLabel(), label, cmp2.size, cmp2.scanAge,  cmp2.diskTemp,
                            cmp2.plots, cmp2.plotsSize,
                            cmp2.raAge,cmp2.raTime,cmp2.raTimeAvg,cmp2.raSucceed,cmp2.raFailed,
                            cmp2.lfAge,cmp2.lfPlotsFarmed,cmp2.lfPlotsTotal,cmp2.lfPlotsFT,cmp2.lfProofsTotal,cmp2.lfTimeAvg,
                            cmp2.leAge,cmp2.leErrorsTotal);
                    if (!lastDisk) createGridLineV1(panel, allColumnsCount);
                }
            }
            
            cmp.setPlots(plotsTotal);
            cmp.setPlotsSize(plotsSizeTotal);
            cmp.setDisk(disksTotal);
            cmp.setSize(disksSizeTotal);
            cmp.setScanAge(node.getDt());
            cmp.setLfAge(node.getLfDt());
            cmp.setLfPlotsFarmed(node.getLfPlotsFarmed());
            cmp.setLfPlotsTotal(node.getLfPlotsTotal());
            cmp.setLfProofsTotal(node.getLfProofsTotal());
            if (node.getLfPlotsTotal() > 0) cmp.setLfPlotsFT((double)node.getLfPlotsFarmed()/(double)node.getLfPlotsTotal());
            cmp.setLfTimeAvg(node.getLfTimeAvg());
            cmp.setLeAge(node.getLeDt());
            cmp.setLeErrorsTotal(node.getLeErrorsTotal());
            
            footerNodesTotal++;
            footerDisksTotal += disksTotal;
            footerDisksSizeTotal += disksSizeTotal;
            footerPlotsTotal += plotsTotal;
            footerPlotsSizeTotal += plotsSizeTotal;
            footerLfPlotsFarmed += node.getLfPlotsFarmed();
            footerLfPlotsTotal += node.getLfPlotsTotal();
            footerLfProofsTotal += node.getLfProofsTotal();
            footerLeErrorsTotal += node.getLeErrorsTotal();
        }
        
        footer = new ComponentLine();
        footer.node = createButtonC();
        footer.disk = createButtonC();
        footer.size = createButtonR();
        footer.diskTemp = createButtonC();
        footer.plots = createButtonR();
        footer.plotsSize = createButtonR();
        footer.scanAge = createButtonC();
        footer.lfPlotsFarmed = createButtonR();
        footer.lfPlotsTotal = createButtonR();
        footer.lfProofsTotal = createButtonR();
        footer.leErrorsTotal = createButtonR();
        footer.setNode(footerNodesTotal);
        footer.setPlots(footerPlotsTotal);
        footer.setPlotsSize(footerPlotsSizeTotal);
        footer.setDisk(footerDisksTotal);
        footer.setSize(footerDisksSizeTotal);
        footer.setLfPlotsFarmed(footerLfPlotsFarmed);
        footer.setLfPlotsTotal(footerLfPlotsTotal);
        footer.setLfProofsTotal(footerLfProofsTotal);
        footer.setLeErrorsTotal(footerLeErrorsTotal);

        _cBoth();
        c.gridx = 0;
        c.gridwidth = 1;
        panel.add(createEmptyHPanel(), c);
        
        _cMiddle();
        c.gridx++;
        c.gridwidth = allColumnsCount - 2;
        panel.add(createLabelHeader("summary"), c);
        _cBoth();
        c.gridx++;
        panel.add(createEmptyHPanel(), c);

        createGridLineV(panel, allColumnsCount);
        
        createGridLine(panel, true, false, footer.node, footer.disk, footer.size, createLabel(), createLabel(),
                footer.plots, footer.plotsSize,
                footer.raAge,footer.raTime,footer.raTimeAvg,footer.raSucceed,footer.raFailed,
                footer.lfAge,footer.lfPlotsFarmed,footer.lfPlotsTotal,footer.lfPlotsFT,footer.lfProofsTotal,footer.lfTimeAvg,
                footer.leAge,footer.leErrorsTotal);
        
        return panel;
    }
    
    private void createInfoPanel() {

        infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout());
        infoPanel.add(infoLabelRefreshed = createLabel());
        
    }
    
    private void refreshInfo(NodePacket packet, String info) {
        
        String result = "Refreshed: "+DTF.format(new Date());
        if (packet != null) result += "; Date from: "+DTF.format(packet.getDtFrom());
        if (info != null) result += "; Info: "+info;
        
        infoLabelRefreshed.setText(result);
    }
    
    private void refreshGrid(List<Node> nodes) {
        
        int footerNodesTotal = 0;
        int footerDisksTotal = 0;
        long footerDisksSizeTotal = 0;
        int footerPlotsTotal = 0;
        long footerPlotsSizeTotal = 0;
        
        for (Node node : nodes) {
            
            ComponentLine cmp = nodesMap.get(node);
            
            if (cmp == null) {
                
                rebuildGrid(createGrid(nodes));
                return;
            }

            int disksTotal = 0;
            long disksSizeTotal = 0;
            int plotsTotal = 0;
            long plotsSizeTotal = 0;
            
            if (node.isNodeHasDisks()) {
            
                for (Disk disk : node.getDisks()) {

                    ComponentLine cmp2 = disksMap.get(disk);
                    
                    if (cmp2 == null) {

                        rebuildGrid(createGrid(nodes));
                        return;
                    }
                    
                    cmp2.setPlots(disk.getPlotsCount());
                    cmp2.setPlotsSize(disk.getPlotsSize());
                    cmp2.setSize(disk.getSize());
                    cmp2.setScanAge(disk.getDt());

                    disksTotal++;
                    disksSizeTotal += disk.getSize();
                    plotsTotal += disk.getPlotsCount();
                    plotsSizeTotal += disk.getPlotsSize();
                }
            }
            
            cmp.setPlots(plotsTotal);
            cmp.setPlotsSize(plotsSizeTotal);
            cmp.setDisk(disksTotal);
            cmp.setSize(disksSizeTotal);
            cmp.setScanAge(node.getDt());

            footerNodesTotal++;
            footerDisksTotal += disksTotal;
            footerDisksSizeTotal += disksSizeTotal;
            footerPlotsTotal += plotsTotal;
            footerPlotsSizeTotal += plotsSizeTotal;
        }

        footer.setNode(footerNodesTotal);
        footer.setPlots(footerPlotsTotal);
        footer.setPlotsSize(footerPlotsSizeTotal);
        footer.setDisk(footerDisksTotal);
        footer.setSize(footerDisksSizeTotal);

        mainPanel.revalidate();
    }

    private void rebuildGrid(JPanel grid) {

        _cBoth();
        c.gridx = 0;
        c.gridy = 0;
        JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        gridPanel.removeAll();
        gridPanel.add(scrollPane, c);
        
        mainPanel.revalidate();
    }
   
    public void dataRefreshed(NodePacket packet, String info) throws Exception {
        
        refreshInfo(packet, info);
        
        if (packet != null && packet.isHasNodes()) {

            List<Node> nodes = packet.getNodes();
            Utils.sortNodes(nodes);
            long nodeFingerprint = Utils.getFingerprint(nodes);
            
            if (nodesMap == null || nodeFingerprint != lastNodeFingerprint) {

                lastNodeFingerprint = nodeFingerprint;
                rebuildGrid(createGrid(nodes));
            } else {

                refreshGrid(nodes);
            }
            
        } else {

            nodesMap = null;
            rebuildGrid(createEmptyGrid(info));
        }
    }
    
    public void composePanels(String msg) {
    
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

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(1, 1));

        mainPanel.add(gridPanel, fillBag);

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

        rebuildGrid(createEmptyGrid(msg));
        
        setLayout(new GridLayout(1, 1));
        add(mainPanel);
    }
    
}