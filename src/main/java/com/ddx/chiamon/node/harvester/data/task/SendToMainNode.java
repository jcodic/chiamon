package com.ddx.chiamon.node.harvester.data.task;

import com.ddx.chiamon.common.data.task.HttpTask;
import com.ddx.chiamon.common.data.Node;
import java.util.Objects;

/**
 *
 * @author ddx
 */
public class SendToMainNode extends HttpTask {
    
    public static final String PACKET_FULL_NODE_INFO = "FullNodeInfo";
    public static final String PACKET_READ_ACCESS = "ReadAccess";
    
    private Node node;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public SendToMainNode(String packetUid, long timeMax) {
        
        this.packetUid = packetUid;
        this.timeMax = timeMax;
    }

    @Override
    public int hashCode() {
        return packetUid.hashCode() + 100;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SendToMainNode other = (SendToMainNode) obj;
        if (!Objects.equals(this.packetUid, other.packetUid)) {
            return false;
        }
        return true;
    }
    
}