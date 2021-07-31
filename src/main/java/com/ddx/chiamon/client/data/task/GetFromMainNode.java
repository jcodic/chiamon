package com.ddx.chiamon.client.data.task;

import com.ddx.chiamon.client.Setup;
import com.ddx.chiamon.common.data.task.HttpTask;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author ddx
 */
public class GetFromMainNode extends HttpTask {
    
    public static final String GET_HARVESTER_NODES = "getHarvesterNodes";
    public static final String GET_HARVESTER_NODES_LIST = "getHarvesterNodesList";
    public static final String GET_HARVESTER_NODES_DISKS_LIST = "getHarvesterNodesAndDisksList";
    public static final String GET_LOGSFARM = "getLogsFarm";
    public static final String GET_READ_ACCESS = "getReadAccess";
    public static final String GET_SMART_ACCESS = "getSmartAccess";

    protected final Map<String, Object> params = new HashMap<>();
    protected byte[] response;
    
    public GetFromMainNode(String packetUid, long timeMax, Setup setup) {
        
        this.packetUid = packetUid;
        this.timeMax = timeMax;
        this.setup = setup;
    }

    public void addSimpleParam(String name, Object value) {
        
        params.put(name, value);
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public byte[] getResponse() {
        return response;
    }

    public void setResponse(byte[] response) {
        this.response = response;
    }

    public boolean isHasResponse() {
        
        return response != null && response.length > 0;
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
        final GetFromMainNode other = (GetFromMainNode) obj;
        if (!Objects.equals(this.packetUid, other.packetUid)) {
            return false;
        }
        return true;
    }

}