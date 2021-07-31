package com.ddx.chiamon.common.data;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 *
 * @author ddx
 */
public class NodeAuthenticator extends Authenticator {
    
    private String user;
    private String pwd;
    
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, pwd.toCharArray());
    }

    public NodeAuthenticator(String user, String pwd) {
        this.user = user;
        this.pwd = pwd;
    }

}