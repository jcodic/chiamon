package com.ddx.chiamon.node;

import com.ddx.chiamon.utils.MessageX;

/**
 *
 * @author ddx
 */
public class ConvertPassword {

    private static void printInfo() {
        
        System.out.println("Usage: convert_password <password>");
    }

    public static void main(String[] args) {
        
        try {

            if (args == null || args.length < 1) {
                
                printInfo();
                return;
            }

            String pwd = args[0];
            System.out.println("Password ["+pwd+"] -> ["+MessageX.encryptString(pwd)+"]");
            
        } catch (Exception ex) { ex.printStackTrace(); }
    }
    
}