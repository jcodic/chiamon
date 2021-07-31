package com.ddx.chiamon.client.ui;

import com.ddx.chiamon.client.Vars;
import com.ddx.chiamon.client.data.Disk;
import com.ddx.chiamon.client.data.Node;
import com.ddx.chiamon.client.data.task.GetFromMainNode;
import com.ddx.chiamon.client.utils.HttpUtils;
import com.ddx.chiamon.client.utils.TaskUtils;
import com.ddx.chiamon.common.data.task.Task;
import com.ddx.chiamon.utils.SizeConv;
import com.ddx.chiamon.utils.Str;
import com.ddx.chiamon.utils.TimeConv;
import com.fasterxml.jackson.core.type.TypeReference;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author ddx
 */
public class ChooseDisks extends WindowTemplate {

    private JPanel gridPanel;
    private JPanel infoPanel;
    private JPanel mainPanel;
    private JLabel infoLabel;

    private List<Node> nodes;
    private Map<Disk, JCheckBox> disksSel;
    private JCheckBox newForEach;
    private JTextField dtFrom;
    private JTextField dtTo;

    private Date selDtFrom;
    private Date selDtTo;
    private Map<Disk, Boolean> selDisks;
    private boolean selNewForEach;
    
    private boolean selectionParsed = false;

    public void select() {
    }
    
    public void cancel() {
    }

    private void parseSelection() {
        
        try {
            
            String tmp = Str.getNotEmpty(dtFrom.getText());
            if (tmp != null && tmp.toLowerCase().charAt(0) == 't') {
                selDtFrom = new Date(System.currentTimeMillis() - TimeConv.strToTime(tmp.substring(1)));
            } else {
                selDtFrom = tmp!=null?Vars.DTF.parse(tmp):new Date(System.currentTimeMillis()-Vars.statsCollectTime);
            }
            tmp = Str.getNotEmpty(dtTo.getText());
            if (tmp != null && tmp.toLowerCase().charAt(0) == 't') {
                selDtTo = new Date(System.currentTimeMillis() - TimeConv.strToTime(tmp.substring(1)));
            } else {
                selDtTo = tmp!=null?Vars.DTF.parse(tmp):null;
            }
            
            selDisks = new HashMap<>();
            for (Map.Entry<Disk, JCheckBox> item : disksSel.entrySet()) selDisks.put(item.getKey(), item.getValue().isSelected());

            selNewForEach = newForEach.isSelected();
            
            selectionParsed = true;
            
        } catch (Exception ex) { }
    }

    public List<Node> getNodes() {
        return nodes;
    }
    
    public Date getSelDtFrom() {
        return selDtFrom;
    }

    public Date getSelDtTo() {
        return selDtTo;
    }

    public Map<Disk, Boolean> getSelDisks() {
        return selDisks;
    }

    public boolean isSelNewForEach() {
        return selNewForEach;
    }

    public boolean isSelectionParsed() {
        return selectionParsed;
    }

    public JPanel createGrid() throws Exception {

        disksSel = new HashMap<>();
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        
        int gy = 0;
        
        for (Node node : nodes) {
            
            _cBoth();
            c.gridx = 0;
            c.gridy = gy++;
            JLabel label = createLabelHeader(node.getUid());
            label.setBackground(Vars.highLightColor);
            panel.add(label, c);
            _cBoth();
            c.gridx++;
            JPanel epanel = createEmptyHPanel();
            epanel.setBackground(Vars.highLightColor);
            panel.add(epanel, c);
            c.gridx++;
            epanel = createEmptyHPanel();
            epanel.setBackground(Vars.highLightColor);
            panel.add(epanel, c);
            c.gridx++;
            epanel = createEmptyHPanel();
            epanel.setBackground(Vars.highLightColor);
            panel.add(epanel, c);
            c.gridx++;
            _cLeft();
            JCheckBox check = createCheckbox("");
            check.setBackground(Vars.highLightColor);
            check.setSelected(true);
            check.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    boolean sel = ((JCheckBox)e.getSource()).isSelected();
                    for (Node n : nodes) {
                        if (n == node) {
                            if (n.isNodeHasDisks()) {
                                for (Disk d : n.getDisks()) {
                                    for (Map.Entry<Disk, JCheckBox> item : disksSel.entrySet()) {
                                        if (item.getKey() == d) {
                                            item.getValue().setSelected(sel);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            });
            panel.add(check, c);
            
            
            if (node.isNodeHasDisks()) {
                
                for (Disk disk : node.getDisks()) {
                    
                    _cLeft();
                    c.gridx = 1;
                    c.gridy = gy++;
                    panel.add(createLabel(disk.getUid()), c);
                    c.gridx++;
                    panel.add(createLabel(disk.getName()), c);
                    c.gridx++;
                    _cRight();
                    panel.add(createLabel(SizeConv.sizeToStr(disk.getSize())), c);
                    c.gridx++;
                    check = createCheckbox("");
                    check.setSelected(true);
                    disksSel.put(disk, check);
                    panel.add(check, c);
                }
            }
        }
        
        int maxw = 5;
        
        _cBoth();
        c.gridx = 0;
        c.gridy = gy++;
        c.gridwidth = maxw;
        panel.add(createEmptyVPanelSpaced(5), c);

        newForEach = createCheckbox("Open new window for each selected");
        JPanel xpanel = new JPanel(new FlowLayout());
        xpanel.add(newForEach);

        _cLeft();
        c.gridx = 0;
        c.gridy = gy++;
        c.gridwidth = maxw;
        panel.add(xpanel, c);

        dtFrom = createTextField(Vars.DTF.format(new Date(System.currentTimeMillis() - Vars.statsCollectTime)), 200);
        dtTo = createTextField("", 200);

        xpanel = new JPanel(new FlowLayout());
        xpanel.add(createLabel("Date from"));
        xpanel.add(dtFrom);
        xpanel.add(createLabel("* leave blank if last "+TimeConv.timeToStr(Vars.statsCollectTime)));
        
        _cLeft();
        c.gridx = 0;
        c.gridy = gy++;
        c.gridwidth = maxw;
        panel.add(xpanel, c);
        
        xpanel = new JPanel(new FlowLayout());
        xpanel.add(createLabel("Date to"));
        xpanel.add(dtTo);
        xpanel.add(createLabel("* leave blank if now()"));
        
        _cLeft();
        c.gridx = 0;
        c.gridy = gy++;
        c.gridwidth = maxw;
        panel.add(xpanel, c);
        
        xpanel = new JPanel(new FlowLayout());
        xpanel.add(createLabel("* date may use t+period, like: t2h30m, t1w1d, t30s ..."));

        _cLeft();
        c.gridx = 0;
        c.gridy = gy++;
        c.gridwidth = maxw;
        panel.add(xpanel, c);
        
        _cBoth();
        c.gridx = 0;
        c.gridy = gy++;
        c.gridwidth = maxw;
        panel.add(createEmptyVPanelSpaced(5), c);

        xpanel = new JPanel(new FlowLayout());
        
        JButton button = new JButton("Select");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parseSelection();
                select();
            }
        });

        xpanel.add(button);
        
        button = new JButton("Cancel");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });

        xpanel.add(button);

        button = new JButton("Unselect all");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox cb : disksSel.values()) cb.setSelected(false);
            }
        });

        xpanel.add(button);
        
        button = new JButton("Select all");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox cb : disksSel.values()) cb.setSelected(true);
            }
        });

        xpanel.add(button);
        
        _cLeft();
        c.gridx = 0;
        c.gridy = gy++;
        c.gridwidth = maxw;
        panel.add(xpanel, c);
        
        return panel;
    }


    public void nodesLoaded() throws Exception {

        _cBoth();
        c.gridx = 0;
        c.gridy = 0;
        JScrollPane scrollPane = new JScrollPane(createGrid());
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        gridPanel.add(scrollPane, c);
        
        mainPanel.revalidate();
    }
    
    private int countDisks() {
        
        int result = 0;
        if (nodes == null || nodes.isEmpty()) return result;
        for (Node node : nodes) if (node.isNodeHasDisks()) result += node.getDisks().size();
        return result;
    }
    
    private void getNodesList() {
        
        try {
            
            infoLabel.setText("Loading nodesList...");
            GetFromMainNode task = new GetFromMainNode(GetFromMainNode.GET_HARVESTER_NODES_DISKS_LIST, Vars.httpTimeMaxGet, setup);
            TaskUtils.executeTask(task);
            Task taskf = task.getFutures().get();
            
            if (taskf.isFinished() && taskf.isSuccess()) {

                if (task.isHasResponse()) {

                    nodes = HttpUtils.getMapper().readValue(task.getResponse(), new TypeReference<List<Node>>(){});
                    int nodesLoaded = (nodes!=null?nodes.size():0);
                    infoLabel.setText(nodesLoaded+" nodes, "+countDisks()+" disks loaded");
                    if (nodesLoaded > 0) nodesLoaded();
                } else infoLabel.setText("Emptry response");

            } else {

                infoLabel.setText("Can't load nodesList."+(task.getInfo()!=null?(" Info: "+task.getInfo()):""));
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }
    
    private void createInfoPanel() {

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

        setLayout(new GridLayout(1, 1));
        add(mainPanel);
    }

    public void run() {
        
        composePanels();
        
        new Thread() {
            @Override
            public void run() {
                getNodesList();
            }
        }.start();
    }
    
}