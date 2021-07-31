package com.ddx.chiamon.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author ddx
 */
public class DB {

    public static Connection getConnection() throws Exception {
        
        InitialContext cxt = new InitialContext();
        DataSource ds = (DataSource)cxt.lookup("java:/comp/env/jdbc/postgres");
        return ds.getConnection();
    }
    
    /*
    public static Connection getConnection() throws Exception {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(jdbcPath,jdbcUser,jdbcPwd);
    }
    */

    public static String checkConnection() {

	String version = null;

	try {
	    Connection conn = getConnection();
	    Statement stm = conn.createStatement();
	    ResultSet rs = stm.executeQuery("SELECT version()");
	    if (rs.next()) version = rs.getString(1);
	    rs.close();
	    stm.close();
	    conn.close();
        
	} catch (Exception ex) { ; }

	return version;
    }
    
    public static double[] toDouble(Double[] values) throws Exception {
        if (values == null) return null;
        double[] valuesM = new double[values.length];
        for (int i = 0; i < values.length; i++) valuesM[i] = values[i];
        return valuesM;
    }
    
    public static int[] toInt(Integer[] values) throws Exception {
        if (values == null) return null;
        int[] valuesM = new int[values.length];
        for (int i = 0; i < values.length; i++) valuesM[i] = values[i];
        return valuesM;
    }

}