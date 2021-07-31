package com.ddx.chiamon.client.ui;

import com.ddx.chiamon.client.Vars;
import com.ddx.chiamon.client.data.Node;
import com.ddx.chiamon.client.data.task.GetFromMainNode;
import com.ddx.chiamon.client.utils.HttpUtils;
import com.ddx.chiamon.client.utils.TaskUtils;
import com.ddx.chiamon.common.data.task.Task;
import com.ddx.chiamon.utils.Str;
import com.ddx.chiamon.utils.TimeConv;
import com.fasterxml.jackson.core.type.TypeReference;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class ChooseNodes extends WindowTemplate {

    private JPanel gridPanel;
    private JPanel infoPanel;
    private JPanel mainPanel;
    private JLabel infoLabel;

    private List<Node> nodes;
    private List<JCheckBox> nodesSel;
    private JCheckBox newForEach;
    private JTextField dtFrom;
    private JTextField dtTo;

    private Date selDtFrom;
    private Date selDtTo;
    private List<Boolean> selNodes;
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
            
            selNodes = new ArrayList<>(nodesSel.size());
            for (JCheckBox item : nodesSel) selNodes.add(item.isSelected());

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

    public List<Boolean> getSelNodes() {
        return selNodes;
    }

    public boolean isSelNewForEach() {
        return selNewForEach;
    }

    public boolean isSelectionParsed() {
        return selectionParsed;
    }

    public JPanel createGrid() throws Exception {

        nodesSel = new ArrayList<>(nodes.size() + 1);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        
        int gy = 0;
        
        _cLeft();
        c.gridx = 0;
        c.gridy = gy++;
        panel.add(createLabel("All nodes (combined)"), c);
        c.gridx = 1;
        JCheckBox check = createCheckbox("");
        check.setSelected(true);
        nodesSel.add(check);
        panel.add(check, c);
        
        for (Node node : nodes) {
            
            c.gridx = 0;
            c.gridy = gy++;
            panel.add(createLabel(node.getUid()), c);
            c.gridx = 1;
            check = createCheckbox("");
            check.setSelected(true);
            nodesSel.add(check);
            panel.add(check, c);
        }
        
        _cBoth();
        c.gridx = 0;
        c.gridy = gy++;
        c.gridwidth = 3;
        panel.add(createEmptyVPanelSpaced(5), c);
        c.gridwidth = 1;

        _cLeft();
        c.gridx = 0;
        c.gridy = gy++;
        panel.add(createLabel("Open new window for each selected"), c);
        c.gridx = 1;
        _cMiddle();
        newForEach = createCheckbox("");
        panel.add(newForEach, c);
        c.gridwidth = 1;

        dtFrom = createTextField(Vars.DTF.format(new Date(System.currentTimeMillis() - Vars.statsCollectTime)), 200);
        dtTo = createTextField("", 200);

        _cLeft();
        c.gridx = 0;
        c.gridy = gy++;
        panel.add(createLabel("Date from"), c);
        c.gridx = 1;
        panel.add(dtFrom, c);
        c.gridx = 2;
        panel.add(createLabel("* leave blank if last "+TimeConv.timeToStr(Vars.statsCollectTime)), c);
        
        _cLeft();
        c.gridx = 0;
        c.gridy = gy++;
        panel.add(createLabel("Date to"), c);
        c.gridx = 1;
        panel.add(dtTo, c);
        c.gridx = 2;
        panel.add(createLabel("* leave blank if now()"), c);
        
        JPanel xpanel = new JPanel(new FlowLayout());
        xpanel.add(createLabel("* date may use t+period, like: t2h30m, t1w1d, t30s ..."));

        _cLeft();
        c.gridx = 0;
        c.gridy = gy++;
        c.gridwidth = 3;
        panel.add(xpanel, c);
        c.gridwidth = 1;
        
        _cBoth();
        c.gridx = 0;
        c.gridy = gy++;
        c.gridwidth = 3;
        panel.add(createEmptyVPanelSpaced(5), c);
        c.gridwidth = 1;

        _cLeft();
        c.gridx = 0;
        c.gridy = gy++;
        c.gridwidth = 3;

        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout());
        
        JButton button = new JButton("Select");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parseSelection();
                select();
            }
        });

        panel2.add(button);
        
        button = new JButton("Cancel");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });

        panel2.add(button);
        
        panel.add(panel2, c);
        
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
    
    private void getNodesList() {
        
        try {
            
            infoLabel.setText("Loading nodesList...");
            GetFromMainNode task = new GetFromMainNode(GetFromMainNode.GET_HARVESTER_NODES_LIST, Vars.httpTimeMaxGet, setup);
            TaskUtils.executeTask(task);
            Task taskf = task.getFutures().get();
            
            if (taskf.isFinished() && taskf.isSuccess()) {

                if (task.isHasResponse()) {

                    nodes = HttpUtils.getMapper().readValue(task.getResponse(), new TypeReference<List<Node>>(){});
                    int nodesLoaded = (nodes!=null?nodes.size():0);
                    infoLabel.setText(nodesLoaded+" nodes loaded.");
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