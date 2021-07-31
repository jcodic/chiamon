package com.ddx.chiamon.node.main.servlet;

import com.ddx.chiamon.common.data.Node;
import com.ddx.chiamon.db.SQLResult;
import com.ddx.chiamon.db.Storage;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author ddx
 */
public class HarvesterNodeReceiver extends CommonServlet {
    
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("text/html; charset=UTF-8");
        Node node = null;
        
        try {
        
            node = mapper.readValue(req.getInputStream(), Node.class);
            node.setIp(req.getRemoteAddr());
            
        } catch (Exception ex) { 
            
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().print("Error parsing HarvesterNode!");
            getServletContext().log("Error parsing HarvesterNode", ex);
            
            return;
        }
        
        try {
        
            SQLResult result = Storage.insertHarvesterNode(node);
            getServletContext().log("HarvesterNode item ["+result.getReturnId()+"] "+result.getInfo());
            
        } catch (Exception ex) { 
            
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().print("Error storing HarvesterNode!");
            getServletContext().log("Error storing HarvesterNode", ex);
            return;
        }
        
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().print("Accepted");
    }
    
}