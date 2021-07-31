package com.ddx.chiamon.client.auto;

import com.ddx.chiamon.client.Setup;
import com.ddx.chiamon.client.Vars;
import com.ddx.chiamon.client.data.Node;
import com.ddx.chiamon.client.data.task.GetFromMainNode;
import com.ddx.chiamon.client.utils.HttpUtils;
import com.ddx.chiamon.client.utils.TaskUtils;
import com.ddx.chiamon.common.data.task.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ddx
 */
public abstract class Fetcher {

    private final static Logger log = LogManager.getLogger(Fetcher.class);

    private final Setup setup;
    
    public abstract void nodesLoaded(List<Node> items);

    public void getNodesList() {
        
        try {
            
            log.info("Loading nodesList...");
            GetFromMainNode task = new GetFromMainNode(GetFromMainNode.GET_HARVESTER_NODES_LIST, Vars.httpTimeMaxGet, setup);
            TaskUtils.executeTask(task);
            Task taskf = task.getFutures().get();
            
            if (taskf.isFinished() && taskf.isSuccess()) {

                if (task.isHasResponse()) {

                    List<Node> nodes = HttpUtils.getMapper().readValue(task.getResponse(), new TypeReference<List<Node>>(){});
                    int nodesLoaded = (nodes!=null?nodes.size():0);
                    log.info(nodesLoaded+" nodes loaded.");
                    if (nodesLoaded > 0) nodesLoaded(nodes);
                } else log.info("Emptry response");

            } else {

                log.info("Can't load nodesList."+(task.getInfo()!=null?(" Info: "+task.getInfo()):""));
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public void getNodesAndDisksList() {
        
        try {
            
            log.info("Loading nodesList...");
            GetFromMainNode task = new GetFromMainNode(GetFromMainNode.GET_HARVESTER_NODES_DISKS_LIST, Vars.httpTimeMaxGet, setup);
            TaskUtils.executeTask(task);
            Task taskf = task.getFutures().get();
            
            if (taskf.isFinished() && taskf.isSuccess()) {

                if (task.isHasResponse()) {

                    List<Node> nodes = HttpUtils.getMapper().readValue(task.getResponse(), new TypeReference<List<Node>>(){});
                    int nodesLoaded = (nodes!=null?nodes.size():0);
                    log.info(nodesLoaded+" nodes");
                    if (nodesLoaded > 0) nodesLoaded(nodes);
                } else log.info("Emptry response");

            } else {

                log.info("Can't load nodesList."+(task.getInfo()!=null?(" Info: "+task.getInfo()):""));
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }
    
    public Fetcher(Setup setup) {
        this.setup = setup;
    }

}