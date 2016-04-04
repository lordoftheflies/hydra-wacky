/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.hydra.dspmicro;

import java.io.FilePermission;
import java.net.SocketPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Policy;
import java.util.PropertyPermission;
import java.util.logging.Logger;
import org.osgi.framework.AdminPermission;
import org.osgi.framework.FrameworkUtil;

/**
 *
 * @author lordoftheflies
 */
public class EdgeDriverPolicy extends Policy {
    
    private static final Logger LOG = Logger.getLogger(EdgeDriverPolicy.class.getName());

//    static {
//        LOG.info("Setup edge policy.");
//        Policy.setPolicy(new EdgeDriverPolicy());
//    }
    private static PermissionCollection perms;
    
    public EdgeDriverPolicy() {
        super();
        if (perms == null) {
            perms = new FieldTierPermissionCollection();
            addPermissions();
        }
    }
    
    @Override
    public PermissionCollection getPermissions(CodeSource codesource) {
        return perms;
    }
    
    private void addPermissions() {
        SocketPermission socketPermission = new SocketPermission("*", "connect, resolve");
        PropertyPermission propertyPermission = new PropertyPermission("*", "read, write");
        FilePermission filePermission = new FilePermission("<<ALL FILES>>", "read");
        AdminPermission adminPermission = new AdminPermission(FrameworkUtil.getBundle(WebSocketServerConnector.class), "context");
        
        perms.add(socketPermission);
        perms.add(propertyPermission);
        perms.add(filePermission);
        perms.add(adminPermission);
    }
    
}
