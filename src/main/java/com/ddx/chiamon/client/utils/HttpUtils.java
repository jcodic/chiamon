package com.ddx.chiamon.client.utils;

import com.ddx.chiamon.client.data.task.GetFromMainNode;
import com.ddx.chiamon.common.data.json.DateLongDeserializer;
import com.ddx.chiamon.common.data.task.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author ddx
 */
public class HttpUtils extends com.ddx.chiamon.utils.HttpUtils {

    private static ObjectMapper mapper;
    
    public static synchronized ObjectMapper getMapper() {
        
        if (mapper == null) {
            
            mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(java.util.Date.class, new DateLongDeserializer());
            mapper.registerModule(module);            
        }
        
        return mapper;
    }
    
    public static Task getMainNodeResponse(GetFromMainNode task) throws Exception {

        try {
        
            task.addSimpleParam("client_id", ((com.ddx.chiamon.client.Setup)task.getSetup()).getClientUid());
            task.addSimpleParam("cmd", task.getPacketUid());
            URL url = new URL(task.getSetup().getMainNodeURL()+"?"+encodeParams(task.getParams()));
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.setAllowUserInteraction(true);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout((int)task.getTimeMax());
            conn.setDoInput(true);
            conn.setRequestProperty("User-Agent", "JavaClient");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.connect();
            int resp_code = conn.getResponseCode();
            String resp_msg = conn.getResponseMessage();
            task.setResponse(downloadInputStream(conn.getInputStream()));
            conn.disconnect();

            task.markAsFinished(resp_code==HttpServletResponse.SC_OK, resp_msg);
            
        } catch (Exception ex) { task.markAsFinished(false, ex.getMessage()); }
        
        return task;
    }

}