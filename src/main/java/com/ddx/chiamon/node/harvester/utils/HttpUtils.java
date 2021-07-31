package com.ddx.chiamon.node.harvester.utils;

import com.ddx.chiamon.common.data.json.DateLongSerializer;
import com.ddx.chiamon.node.harvester.Setup;
import com.ddx.chiamon.node.harvester.data.task.SendToMainNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author ddx
 */
public class HttpUtils extends com.ddx.chiamon.utils.HttpUtils {

    public static void sendMainNode(SendToMainNode task) throws Exception {

        URL url = new URL(task.getSetup().getMainNodeURL());
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        conn.setAllowUserInteraction(true);
        conn.setRequestMethod("POST");
        conn.setConnectTimeout((int)task.getTimeMax());
        conn.setDoOutput(true);
        conn.setRequestProperty("User-Agent", "HarvesterNode");
        conn.setRequestProperty("Content-Type","application/json");
        
        //conn.setRequestProperty("Content-Type","application/octet-stream");
        //conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        //conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        conn.connect();
        
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(java.util.Date.class, new DateLongSerializer());
        mapper.registerModule(module);            
        mapper.writeValue((OutputStream)dos, task.getNode());
        dos.flush();
        dos.close();
        
        int resp_code = conn.getResponseCode();
        String resp_msg = conn.getResponseMessage();

        conn.disconnect();
        
        task.markAsFinished(resp_code==HttpServletResponse.SC_OK, resp_msg);
    } 
    
}