package com.ddx.chiamon.client.data.comparator;

import com.ddx.chiamon.client.data.Node;
import java.util.Comparator;

/**
 *
 * @author ddx
 */
public class NodeComparator implements Comparator<Node> {
    
    @Override
    public int compare(Node item1, Node item2) {

        return item1.getUid().compareTo(item2.getUid());
    }
}