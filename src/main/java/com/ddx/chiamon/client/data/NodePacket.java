package com.ddx.chiamon.client.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ddx
 */
public class NodePacket {
    
    private Date dtFrom;
    private List<Node> nodes;

    public Date getDtFrom() {
        return dtFrom;
    }

    public void setDtFrom(Date dtFrom) {
        this.dtFrom = dtFrom;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    @JsonIgnore
    public boolean isHasNodes() {
        return nodes != null && !nodes.isEmpty();
    }

    public NodePacket() {
    }

    public NodePacket(Date dtFrom, List<Node> nodes) {
        this.dtFrom = dtFrom;
        this.nodes = nodes;
    }
    
}