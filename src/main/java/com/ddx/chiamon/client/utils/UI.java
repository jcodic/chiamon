package com.ddx.chiamon.client.utils;

import com.ddx.chiamon.client.Vars;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author ddx
 */
public class UI {

    public static final int OPTION_BLANK        = -1;
    public static final int OPTION_MAXIMIZED    = 10;
    public static final int OPTION_NOT_MODAL    = 20; // modal is default
    
    private static boolean hasOption(int[] options, int option) {
        
        if (options == null || options.length == 0) return false;
        for (int i : options) if (i == option) return true;
        return false;
    }
    
    public static JFrame getFrame(Component c) {
        
        return (JFrame)SwingUtilities.getAncestorOfClass(JFrame.class, c);
    }
    
    public static JDialog getDialog(Component c) {
        
        return (JDialog)SwingUtilities.getAncestorOfClass(JDialog.class, c);
    }
    
    public static JDialog showDialog(Frame owner, JPanel panel, String title, int[] options) {
        
        JDialog dialog = new JDialog(owner, title, !hasOption(options, OPTION_NOT_MODAL));
        dialog.getContentPane().add(panel);
    
        return showDialog(owner, dialog, title, options);
    }

    public static JDialog showDialog(Component relative, JDialog dialog, String title, int[] options) {
        
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                
                dialog.setModal(!hasOption(options, OPTION_NOT_MODAL));
                if (title != null) dialog.setTitle(title);
                dialog.setResizable(true);
                boolean maximized = hasOption(options, OPTION_MAXIMIZED);
                if (maximized) 
                    dialog.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds()); 
                else {
                    dialog.setPreferredSize(new Dimension(Vars.windowWidth, Vars.windowHeight));
                    dialog.pack();
                }
                dialog.setLocationRelativeTo(relative);
                dialog.setVisible(true);
            }
        });
        
        return dialog;
    }
    
    public static int getScreenWidth() {
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();
        if (gd.length > 0) return gd[0].getDefaultConfiguration().getBounds().width;
        return 0;
    }
    
    public static int getScreenHeight() {
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();
        if (gd.length > 0) return gd[0].getDefaultConfiguration().getBounds().height;
        return 0;
    }
    
    public static Color stripAlpha(Color color) {
        
        return new Color(color.getRed(),color.getGreen(),color.getBlue());
    }

}