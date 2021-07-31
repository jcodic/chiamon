package com.ddx.chiamon.client.ui;

import com.ddx.chiamon.client.Setup;
import com.ddx.chiamon.client.Vars;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author ddx
 */
public class WindowTemplate extends JPanel {
    
    protected Setup setup;
    
    protected GridBagConstraints c = new GridBagConstraints();

    protected void _cLeft() {
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
    }

    protected void _cRight() {
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
    }

    protected void _cMiddle() {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
    }
    
    protected void _cBoth() {
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
    }
    
    public static Font createFont() {

        return new Font(Vars.tableFont, Font.PLAIN, Vars.tableFontSize);
    }

    public static Font createFontBold() {

        return new Font(Vars.tableFont, Font.BOLD, Vars.tableFontSize);
    }

    public static Font createFontHeader() {

        Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
        //fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        return new Font(Vars.tableFont, Font.BOLD, Vars.tableFontSize).deriveFont(fontAttributes);
    }
    
    public static JLabel createLabel() {
        
        return createLabel("");
    }
    
    public static JLabel createLabel(String msg) {
        
        JLabel label = new JLabel(msg);
        label.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
        label.setFont(createFont());
        //label.setBackground(bg);
        //label.setForeground(fg);
        label.setOpaque(true);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        return label;
    }

    public static JLabel createLabelHeader(String msg) {
        
        JLabel label = new JLabel(msg);
        label.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
        label.setFont(createFontHeader());
        //label.setBackground(bg);
        //label.setForeground(fg);
        label.setOpaque(true);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        return label;
    }
    
    public static JLabel createTextConst(String msg) {
        
        JLabel label = new JLabel(msg);
        label.setBorder(new EmptyBorder(new Insets(5, 0, 5, 0)));
        label.setFont(createFont());
        //label.setBackground(bg);
        label.setForeground(Color.GRAY);
        label.setOpaque(true);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        return label;
    }

    protected JPanel createEmptyHPanel() {
    
        return createEmptyHPanel(Color.GRAY);
    }
    
    protected JPanel createEmptyHPanel(Color color) {
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(color);
        panel.setBorder(new EmptyBorder(new Insets(0, 1, 0, 0)));

        return panel;
    }
    
    protected JPanel createEmptyVPanel() {
    
        return createEmptyVPanel(Color.GRAY);
    }
    
    protected JPanel createEmptyVPanel(Color color) {
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(color);
        panel.setBorder(new EmptyBorder(new Insets(1, 0, 0, 0)));

        return panel;
    }

    protected JPanel createEmptyVPanelSpaced(int space) {
        
        return createEmptyVPanelSpaced(Color.GRAY, space);
    }
    
    protected JPanel createEmptyVPanelSpaced(Color color, int space) {
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel panel2 = new JPanel();
        panel2.setBorder(new EmptyBorder(new Insets(space, 0, 0, 0)));
        
        JPanel panel3 = new JPanel();
        panel3.setBorder(new EmptyBorder(new Insets(space, 0, 0, 0)));
        
        panel.add(panel2);
        panel.add(createEmptyVPanel(color));
        panel.add(panel3);

        return panel;
    }
    
    protected JButton createButtonC() {
        
        JButton button = createButton(null);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        return button;
    }

    protected JButton createButtonR() {
        
        JButton button = createButton(null);
        button.setHorizontalAlignment(SwingConstants.RIGHT);
        return button;
    }
    
    protected JButton createButton(String msg) {

        JButton button = new JButton(msg);
        button.setFont(createFont());
        button.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        button.setBorderPainted(false);
        //button.setContentAreaFilled(areaFilled);
        button.setFocusPainted(false);
        button.setBackground(Vars.EmptyColor);
        
        return button;
    }
 
    public static GridBagConstraints getGridBagConstraintsMax() {
        
        GridBagConstraints cx = new GridBagConstraints();
        cx.weightx = 1.0;
        cx.weighty = 1.0;
        cx.gridx = 0;
        cx.gridy = 0;
        cx.gridheight = 1;
        cx.gridwidth = 1;
        cx.fill = GridBagConstraints.BOTH;
        cx.anchor = GridBagConstraints.CENTER;
        return cx;
    }

    public static JPanel wrapComponent(JComponent cmp, boolean left) {
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c0 = new GridBagConstraints();
        c0.fill = GridBagConstraints.NONE;
        c0.anchor = left?GridBagConstraints.WEST:GridBagConstraints.EAST;
        c0.gridx = 0;
        c0.gridy = 0;
        c0.weightx = 1;
        c0.weighty = 1;
        panel.add(cmp, c0);
        return panel;
    }

    protected JCheckBox createCheckbox(String msg) {
    
        JCheckBox label = new JCheckBox(msg);
        label.setBorder(new EmptyBorder(new Insets(8, 8, 8, 8)));
        label.setFont(createFont());
        label.setOpaque(true);
        
        return label;
    }
    
    protected JComboBox createCombobox(String[] items) {
    
        JComboBox combo = new JComboBox(items);
        combo.setFont(createFont());
        combo.setMaximumRowCount(30);
        
        return combo;
    }
    
    protected JTextField createTextField(String msg) {
        
        return createTextField(msg, 150);
    }
    
    protected JTextField createTextField(String msg, int width) {
        
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(width, 22));
        //field.setBorder(new EmptyBorder(new Insets(8, 8, 8, 8)));
        field.setFont(createFont());
        if (msg != null) field.setText(msg);
        
        return field;
    }

    protected JTextArea createTextArea(String msg) {
        
        JTextArea field = new JTextArea();
        //field.setPreferredSize(new Dimension(300, 88));
        //field.setBorder(new EmptyBorder(new Insets(8, 8, 8, 8)));
        field.setFont(createFont());
        if (msg != null) field.setText(msg);
        
        return field;
    }

    public void setSetup(Setup setup) {
        this.setup = setup;
    }

}