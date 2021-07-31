package com.ddx.chiamon.client.ui;

import com.ddx.chiamon.client.Setup;
import com.ddx.chiamon.client.Vars;
import com.ddx.chiamon.client.ui.chart.LogsFarmPlotsFarmedChart;
import com.ddx.chiamon.client.ui.chart.LogsFarmPlotsTotalChart;
import com.ddx.chiamon.client.ui.chart.ReadAccessChart;
import com.ddx.chiamon.client.ui.chart.SmartChart;
import com.ddx.chiamon.client.ui.chart.SmartTempChart;
import com.ddx.chiamon.client.utils.UI;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author ddx
 */
public class WindowMain extends JFrame {
 
    private WindowMain c = this;

    private JPanel objectsPanel;
    private JPanel optionsPanel;
    private Setup setup;
    
    public void closeAppRequest() {
    }
    
    private void chartLogsFarmPlotsFarmed() {
        
        final WindowMain z = this;
        
        final ChooseNodes frame = new ChooseNodes() {
            @Override    
            public void select() {

                UI.getDialog(this).dispose();

                if (isSelectionParsed()) {
                    
                    LogsFarmPlotsFarmedChart chart = new LogsFarmPlotsFarmedChart();
                    chart.setSetup(setup);
                    chart.setNodes(getNodes());
                    chart.setDtFrom(getSelDtFrom());
                    chart.setDtTo(getSelDtTo());
                    chart.setNodesSel(getSelNodes());
                    chart.run();
                    
                    UI.showDialog(UI.getFrame(z), chart, "Plots farmed", new int[]{UI.OPTION_NOT_MODAL, Vars.windowChartMaximized?UI.OPTION_MAXIMIZED:UI.OPTION_BLANK});
                }
            }

            @Override    
            public void cancel() {
                
                UI.getDialog(this).dispose();
            }
        };
        frame.setSetup(setup);
        frame.run();

        UI.showDialog(UI.getFrame(z), frame, "Select nodes", null);
    }
    
    private void chartLogsFarmPlotsTotal() {
        
        final WindowMain z = this;
        
        final ChooseNodes frame = new ChooseNodes() {
            @Override    
            public void select() {

                UI.getDialog(this).dispose();

                if (isSelectionParsed()) {
                    
                    LogsFarmPlotsTotalChart chart = new LogsFarmPlotsTotalChart();
                    chart.setSetup(setup);
                    chart.setNodes(getNodes());
                    chart.setDtFrom(getSelDtFrom());
                    chart.setDtTo(getSelDtTo());
                    chart.setNodesSel(getSelNodes());
                    chart.run();
                    
                    UI.showDialog(UI.getFrame(z), chart, "Plots total", new int[]{UI.OPTION_NOT_MODAL, Vars.windowChartMaximized?UI.OPTION_MAXIMIZED:UI.OPTION_BLANK});
                }
            }

            @Override    
            public void cancel() {
                
                UI.getDialog(this).dispose();
            }
        };
        frame.setSetup(setup);
        frame.run();

        UI.showDialog(UI.getFrame(z), frame, "Select nodes", null);
    }
    
    private void chartDisksRead() {
        
        final WindowMain z = this;
        
        final ChooseDisks frame = new ChooseDisks() {
            @Override    
            public void select() {

                UI.getDialog(this).dispose();

                if (isSelectionParsed()) {
                    
                    ReadAccessChart chart = new ReadAccessChart();
                    chart.setSetup(setup);
                    chart.setNodes(getNodes());
                    chart.setDtFrom(getSelDtFrom());
                    chart.setDtTo(getSelDtTo());
                    chart.setDisksSel(getSelDisks());
                    chart.run();
                    
                    UI.showDialog(UI.getFrame(z), chart, "Disks read access", new int[]{UI.OPTION_NOT_MODAL, Vars.windowChartMaximized?UI.OPTION_MAXIMIZED:UI.OPTION_BLANK});
                }
            }

            @Override    
            public void cancel() {
                
                UI.getDialog(this).dispose();
            }
        };
        frame.setSetup(setup);
        frame.run();

        UI.showDialog(UI.getFrame(z), frame, "Select disks", null);
    }
    
    private void chartDisksSmartTemp() {
        
        final WindowMain z = this;
        
        final ChooseDisks frame = new ChooseDisks() {
            @Override    
            public void select() {

                UI.getDialog(this).dispose();

                if (isSelectionParsed()) {
                    
                    SmartTempChart chart = new SmartTempChart();
                    chart.setSetup(setup);
                    chart.setNodes(getNodes());
                    chart.setDtFrom(getSelDtFrom());
                    chart.setDtTo(getSelDtTo());
                    chart.setDisksSel(getSelDisks());
                    chart.run();
                    
                    UI.showDialog(UI.getFrame(z), chart, "Disks smart data", new int[]{UI.OPTION_NOT_MODAL, Vars.windowChartMaximized?UI.OPTION_MAXIMIZED:UI.OPTION_BLANK});
                }
            }

            @Override    
            public void cancel() {
                
                UI.getDialog(this).dispose();
            }
        };
        frame.setSetup(setup);
        frame.run();

        UI.showDialog(UI.getFrame(z), frame, "Select disks", null);
    }
    
    private void chartDisksSmart() {
        
        final WindowMain z = this;
        
        final ChooseDisks frame = new ChooseDisks() {
            @Override    
            public void select() {

                UI.getDialog(this).dispose();

                if (isSelectionParsed()) {
                    
                    SmartChart chart = new SmartChart();
                    chart.setSetup(setup);
                    chart.setNodes(getNodes());
                    chart.setDtFrom(getSelDtFrom());
                    chart.setDtTo(getSelDtTo());
                    chart.setDisksSel(getSelDisks());
                    chart.run();
                    
                    UI.showDialog(UI.getFrame(z), chart, "Disks smart data", new int[]{UI.OPTION_NOT_MODAL, Vars.windowChartMaximized?UI.OPTION_MAXIMIZED:UI.OPTION_BLANK});
                }
            }

            @Override    
            public void cancel() {
                
                UI.getDialog(this).dispose();
            }
        };
        frame.setSetup(setup);
        frame.run();

        UI.showDialog(UI.getFrame(z), frame, "Select disks", null);
    }

    private JMenuItem getCloseAllWindowsItem() {
        
        JMenuItem menuItem = new JMenuItem(new AbstractAction("Quit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeAppRequest();
            }
        });
        return menuItem;
    }

    private JMenuItem getChartLogsFarmPlotsFarmed() {
        
        JMenuItem menuItem = new JMenuItem(new AbstractAction("Farmed plots") {
            @Override
            public void actionPerformed(ActionEvent e) {
                chartLogsFarmPlotsFarmed();
            }
        });
        return menuItem;
    }

    private JMenuItem getChartLogsFarmPlotsTotal() {
        
        JMenuItem menuItem = new JMenuItem(new AbstractAction("Total plots") {
            @Override
            public void actionPerformed(ActionEvent e) {
                chartLogsFarmPlotsTotal();
            }
        });
        return menuItem;
    }

    private JMenuItem getChartDisksRead() {
        
        JMenuItem menuItem = new JMenuItem(new AbstractAction("Disks read") {
            @Override
            public void actionPerformed(ActionEvent e) {
                chartDisksRead();
            }
        });
        return menuItem;
    }

    private JMenuItem getChartSmartTemp() {
        
        JMenuItem menuItem = new JMenuItem(new AbstractAction("Disks temperature") {
            @Override
            public void actionPerformed(ActionEvent e) {
                chartDisksSmartTemp();
            }
        });
        return menuItem;
    }

    private JMenuItem getChartSmart() {
        
        JMenuItem menuItem = new JMenuItem(new AbstractAction("Disks S.M.A.R.T.") {
            @Override
            public void actionPerformed(ActionEvent e) {
                chartDisksSmart();
            }
        });
        return menuItem;
    }

    private void addMenu() {
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menu;

        menuBar.add(menu = new JMenu("Main"));
        menu.add(getCloseAllWindowsItem());
        menuBar.add(menu = new JMenu("Charts"));
        menu.add(getChartLogsFarmPlotsFarmed());
        menu.add(getChartLogsFarmPlotsTotal());
        menu.add(getChartDisksRead());
        menu.add(getChartSmartTemp());
        menu.add(getChartSmart());
        
        setJMenuBar(menuBar);
    }
    
    public void run() throws Exception {

        addMenu();
        
        objectsPanel = new JPanel(new GridBagLayout());
        optionsPanel = new JPanel(new FlowLayout());

        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        
        add(objectsPanel, c);
        
        c.weightx = 1.0;
        c.weighty = 0.025;
        c.gridy++;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        
        //add(optionsPanel, c);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        if (Vars.windowMaximized) setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    public JPanel getObjectsPanel() {
        return objectsPanel;
    }

    public JPanel getOptionsPanel() {
        return optionsPanel;
    }

    public void setSetup(Setup setup) {
        this.setup = setup;
    }

}