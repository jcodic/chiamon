package com.ddx.chiamon.node.main.servlet;

import com.ddx.chiamon.common.data.json.DateLongDeserializer;
import com.ddx.chiamon.common.data.json.DateLongSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

/**
 *
 * @author ddx
 */
public class CommonServlet extends HttpServlet {

    protected ObjectMapper mapper;
    
    @Override
    public void init(ServletConfig conf) throws ServletException {

	super.init(conf);
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(java.util.Date.class, new DateLongSerializer());
        module.addDeserializer(java.util.Date.class, new DateLongDeserializer());
        mapper.registerModule(module);            
    }

}