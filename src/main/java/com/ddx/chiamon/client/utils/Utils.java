package com.ddx.chiamon.client.utils;

import com.ddx.chiamon.client.data.Disk;
import com.ddx.chiamon.client.data.Node;
import com.ddx.chiamon.client.data.comparator.DiskComparator;
import com.ddx.chiamon.client.data.comparator.NodeComparator;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author ddx
 */
public class Utils {

    public static void sortNodes(List<Node> nodes) {
        
        if (nodes == null || nodes.isEmpty()) return;
        
        Collections.sort(nodes, new NodeComparator());
        
        for (Node node : nodes) {
            
            if (node.isNodeHasDisks()) {
                
                Collections.sort(node.getDisks(), new DiskComparator());
            }
        }
    }

    public static long getFingerprint(List<Node> nodes) {
        
        if (nodes == null || nodes.isEmpty()) return -1;

        long result = 0;

        for (Node node : nodes) {
            
            result += node.getUid().hashCode();
            
            if (node.isNodeHasDisks()) {
                
                for (Disk disk : node.getDisks()) {
                    
                    
                    result += disk.getUid().hashCode() * disk.getId() * node.getId();
                }
            }
        }
        
        return result;
    }
    
}