package com.ddx.chiamon.node.main.servlet;

import com.ddx.chiamon.common.data.ClientAccess;
import com.ddx.chiamon.common.data.json.DateLong;
import com.ddx.chiamon.db.DB;
import com.ddx.chiamon.db.FetcherClient;
import com.ddx.chiamon.db.Storage;
import com.ddx.chiamon.utils.StringConv;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;


/**
 *
 * @author ddx
 */
public class ClientService extends CommonServlet {
    
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

	String cmd = req.getParameter("cmd");

        ClientAccess clientAccess = new ClientAccess();
        clientAccess.setUid(req.getParameter("client_id"));
        clientAccess.setIp(req.getRemoteAddr());
        clientAccess.setDt(new Date());
        clientAccess.setCmd(cmd);
        
        Object exp = null;
        String tmp;
        
        try {
        
            if (cmd == null || cmd.length() == 0) {
                
                // nothing to do
            }
            
            else if (cmd.equals("getHarvesterNodes")) {
                
                long period = Long.parseLong(req.getParameter("period"));
                exp = FetcherClient.getHarvesterNodes(new Date(System.currentTimeMillis()-period));
            }
            
            else if (cmd.equals("getHarvesterNodesList")) {
            
                exp = FetcherClient.getHarvesterNodesList();
                
            }
            
            else if (cmd.equals("getHarvesterNodesAndDisksList")) {
            
                exp = FetcherClient.getHarvesterNodesAndDisksList();
                
            }
            
            else if (cmd.equals("getLogsFarm")) {
                
                long[] ids = StringConv.String2LongArray(req.getParameter("ids"));
                Date dtFrom = DateLong.getFormatter().parse(req.getParameter("dtFrom"));
                tmp = req.getParameter("dtTo");
                Date dtTo = tmp!=null?DateLong.getFormatter().parse(tmp):new Date();
                exp = FetcherClient.getLogsFarm(dtFrom, dtTo, ids);
            }
            
            else if (cmd.equals("getReadAccess")) {
                
                long[] ids = StringConv.String2LongArray(req.getParameter("ids"));
                Date dtFrom = DateLong.getFormatter().parse(req.getParameter("dtFrom"));
                tmp = req.getParameter("dtTo");
                Date dtTo = tmp!=null?DateLong.getFormatter().parse(tmp):new Date();
                exp = FetcherClient.getReadAccess(dtFrom, dtTo, ids);
            }
            
            else if (cmd.equals("getSmartAccess")) {
                
                long[] ids = StringConv.String2LongArray(req.getParameter("ids"));
                Date dtFrom = DateLong.getFormatter().parse(req.getParameter("dtFrom"));
                tmp = req.getParameter("dtTo");
                Date dtTo = tmp!=null?DateLong.getFormatter().parse(tmp):new Date();
                exp = FetcherClient.getDiskSmart(dtFrom, dtTo, ids);
            }
            
            else if (cmd.equals("db")) {
                
                exp = DB.checkConnection();
            }
            
            else {

                // command not found
            }

            if (exp != null) {

                res.setContentType("application/json");
                mapper.writeValue(res.getOutputStream(), exp);
                clientAccess.setSuccess(true);
            }
            
        } catch (Exception ex) { ex.printStackTrace(); getServletContext().log("["+cmd+"] error", ex); res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); }
        finally {
            
            try {
                
                Storage.insertClientAccess(clientAccess);
            } catch (Exception ex) { getServletContext().log("insertClientAccess", ex); }
        }
        
    }
    
}