package com.ddx.chiamon.db;

import com.ddx.chiamon.client.data.Node;
import com.ddx.chiamon.client.data.Disk;
import com.ddx.chiamon.client.data.DiskSmart;
import com.ddx.chiamon.client.data.NodePacket;
import com.ddx.chiamon.client.data.LogFarmEntry;
import com.ddx.chiamon.client.data.ReadAccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author ddx
 */
public class FetcherClient {

    public static NodePacket getHarvesterNodes(Date dtFrom) throws Exception {

        java.sql.Timestamp dtFromT = new java.sql.Timestamp(dtFrom.getTime());
        List<Node> items = new LinkedList<>();
        int c;
        
        NodePacket packet = new NodePacket(dtFrom, items);
        
        try (Connection connection = DB.getConnection()) {
            
            ////////////////////////////
            // Nodes
            ////////////////////////////

            String SQL = "SELECT id,uid,dt,ip FROM HarvesterNode  " +
                         "WHERE dt>=? " +
                         "ORDER BY dt DESC";

            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setTimestamp(1, dtFromT);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                Node item = new Node();
                items.add(item);
                c = 1;
                item.setId(rs.getLong(c++));
                item.setUid(rs.getString(c++));
                item.setDt(rs.getTimestamp(c++));
                item.setIp(rs.getString(c++));
            }
            rs.close();
            ps.close();
            
            if (!items.isEmpty()) {
                
                ////////////////////////////
                // Disks
                ////////////////////////////

                for (Node node : items) {
                
                    SQL = "SELECT id,uid,dt,name,path,size,plots_count,plots_size "+
                          "FROM Disk "+
                          "WHERE id_harvester_node=? "+
                          "AND dt>=? " +
                          "ORDER BY dt DESC";

                    ps = connection.prepareStatement(SQL);
                    c = 1;
                    ps.setLong(c++, node.getId());
                    ps.setTimestamp(c++, dtFromT);
                    rs = ps.executeQuery();
                    while (rs.next()) {

                        Disk disk = new Disk();
                        node.addDisk(disk);
                        c = 1;
                        disk.setId(rs.getLong(c++));
                        disk.setUid(rs.getString(c++));
                        disk.setDt(rs.getTimestamp(c++));
                        disk.setName(rs.getString(c++));
                        disk.setPath(rs.getString(c++));
                        disk.setSize(rs.getLong(c++));
                        disk.setPlotsCount(rs.getInt(c++));
                        disk.setPlotsSize(rs.getLong(c++));
                    }
                    rs.close();
                    ps.close();
                    
                } // for :: nodes
                
                ////////////////////////////
                // ReadAccess
                ////////////////////////////

                for (Node node : items) {
                    
                    if (node.isNodeHasDisks()) {
                        
                        for (Disk disk : node.getDisks()) {
                            
                            SQL = "SELECT read_time,success FROM ReadAccess WHERE id_disk=? AND dt>=?";

                            long readTimeSum = 0;
                            int succeed = 0;
                            int failed = 0;
                            int count = 0;
                            
                            ps = connection.prepareStatement(SQL);
                            c = 1;
                            ps.setLong(c++, disk.getId());
                            ps.setTimestamp(c++, dtFromT);
                            rs = ps.executeQuery();
                            while (rs.next()) {

                                count++;
                                c = 1;
                                readTimeSum += rs.getLong(c++);
                                boolean success = rs.getBoolean(c++);
                                if (success) succeed++; else failed++;
                            }
                            rs.close();
                            ps.close();
                            
                            disk.setRaSucceed(succeed);
                            disk.setRaFailed(failed);
                            disk.setRaTimeAvg(count>0?readTimeSum/count:-1);
                        
                            SQL = "SELECT dt,read_time FROM ReadAccess WHERE id_disk=? ORDER BY dt DESC LIMIT 1";
                            
                            ps = connection.prepareStatement(SQL);
                            ps.setLong(1, disk.getId());
                            rs = ps.executeQuery();
                            if (rs.next()) {

                                c = 1;
                                disk.setRaDt(rs.getTimestamp(c++));
                                disk.setRaTime(rs.getLong(c++));
                            }
                            rs.close();
                            ps.close();
                            
                        } // for :: disks
                        
                    } // if :: has disks
                    
                } // for :: nodes
                

                ////////////////////////////
                // Smarts
                ////////////////////////////

                for (Node node : items) {
                    
                    if (node.isNodeHasDisks()) {
                        
                        for (Disk disk : node.getDisks()) {
                            
                            SQL = "SELECT dt,Temperature_Celsius_RAW_VALUE FROM DiskSmart WHERE id_disk=? AND dt>=? ORDER BY dt DESC LIMIT 1";
                            
                            ps = connection.prepareStatement(SQL);
                            c = 1;
                            ps.setLong(c++, disk.getId());
                            ps.setTimestamp(c++, dtFromT);
                            rs = ps.executeQuery();
                            if (rs.next()) {

                                c = 1;
                                disk.setSmartsDt(rs.getTimestamp(c++));
                                disk.setSmartsTemp(rs.getInt(c++));
                            }
                            rs.close();
                            ps.close();
                            
                        } // for :: disks
                        
                    } // if :: has disks
                    
                } // for :: nodes
                

                ////////////////////////////
                // LogsFarm
                ////////////////////////////

                for (Node node : items) {
                
                    SQL = "SELECT COUNT(*),SUM(plots),SUM(proofs),AVG(time) FROM LogFarm WHERE id_harvester_node=? AND dt>=?";

                    ps = connection.prepareStatement(SQL);
                    c = 1;
                    ps.setLong(c++, node.getId());
                    ps.setTimestamp(c++, dtFromT);
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        c = 1;
                        int count = rs.getInt(c++);
                        if (count > 0) {
                            
                            node.setLfPlotsFarmed(rs.getInt(c++));
                            node.setLfProofsTotal(rs.getInt(c++));
                            node.setLfTimeAvg(rs.getInt(c++));
                        }
                    }
                    rs.close();
                    ps.close();
                    
                    SQL = "SELECT dt,plots_total FROM LogFarm WHERE id_harvester_node=? ORDER BY dt DESC LIMIT 1";

                    ps = connection.prepareStatement(SQL);
                    c = 1;
                    ps.setLong(c++, node.getId());
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        c = 1;
                        node.setLfDt(rs.getTimestamp(c++));
                        node.setLfPlotsTotal(rs.getInt(c++));
                    }
                    rs.close();
                    ps.close();
                    
                } // for :: nodes
                
                ////////////////////////////
                // LogsError
                ////////////////////////////

                for (Node node : items) {
                
                    SQL = "SELECT COUNT(*),MAX(dt) FROM LogError WHERE id_harvester_node=? AND dt>=?";

                    ps = connection.prepareStatement(SQL);
                    c = 1;
                    ps.setLong(c++, node.getId());
                    ps.setTimestamp(c++, dtFromT);
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        c = 1;
                        int count = rs.getInt(c++);
                        node.setLeErrorsTotal(count);
                        if (count > 0) {
                            
                            node.setLeDt(rs.getTimestamp(c++));
                        }
                    }
                    rs.close();
                    ps.close();
                    
                } // for :: nodes
                
                
            } // if :: nodes
            
        } // try
        
	return packet;
    }

    public static List<Node> getHarvesterNodesList() throws Exception {
        
        List<Node> items = new LinkedList<>();
        
        try (Connection connection = DB.getConnection()) {
            
            String SQL = "SELECT id,uid,dt,ip FROM HarvesterNode ORDER BY uid,id";

            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                Node item = new Node();
                items.add(item);
                int c = 1;
                item.setId(rs.getLong(c++));
                item.setUid(rs.getString(c++));
                item.setDt(rs.getTimestamp(c++));
                item.setIp(rs.getString(c++));
            }
            rs.close();
            ps.close();
            
        } // try
        
        return items;
    }
    
    public static List<Node> getHarvesterNodesAndDisksList() throws Exception {
        
        List<Node> items = new LinkedList<>();
        
        try (Connection connection = DB.getConnection()) {
            
            String SQL = "SELECT id,uid,dt,ip FROM HarvesterNode ORDER BY uid,id";

            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                Node item = new Node();
                items.add(item);
                int c = 1;
                item.setId(rs.getLong(c++));
                item.setUid(rs.getString(c++));
                item.setDt(rs.getTimestamp(c++));
                item.setIp(rs.getString(c++));
            }
            rs.close();
            ps.close();
            
            if (!items.isEmpty()) {
                
                SQL = "SELECT id,uid,dt,name,path,size FROM Disk WHERE id_harvester_node=? ORDER BY size DESC,uid,id";
                
                for (Node item : items) {
                    
                    ps = connection.prepareStatement(SQL);
                    ps.setLong(1, item.getId());
                    rs = ps.executeQuery();
                    while (rs.next()) {

                        Disk disk = new Disk();
                        item.addDisk(disk);
                        int c = 1;
                        disk.setId(rs.getLong(c++));
                        disk.setUid(rs.getString(c++));
                        disk.setDt(rs.getTimestamp(c++));
                        disk.setName(rs.getString(c++));
                        disk.setPath(rs.getString(c++));
                        disk.setSize(rs.getLong(c++));
                    }
                    rs.close();
                    ps.close();
                }
            }
            
        } // try
        
        return items;
    }
    
    public static List<LogFarmEntry> getLogsFarm(Date dtFrom, Date dtTo, long[] nodeIds) throws Exception {

        List<LogFarmEntry> items = new LinkedList<>();
        
        try (Connection connection = DB.getConnection()) {
            
            String where1 = "";

            if (nodeIds != null && nodeIds.length > 0) {

                where1 = " AND id_harvester_node IN (";
                for (int i = 0; i < nodeIds.length; i++) 
                    where1 += (i > 0?",":"")+nodeIds[i];
                where1 += ")";
            }
            
            String SQL = 
            "SELECT id_harvester_node,dt,plots,proofs,time,plots_total" +
            "  FROM LogFarm" +
            " WHERE 1=1 " + where1 +
            "   AND dt BETWEEN ? AND ?" +
            " ORDER BY dt";

            PreparedStatement ps = connection.prepareStatement(SQL);
            int c = 1;
            ps.setTimestamp(c++, new java.sql.Timestamp(dtFrom.getTime()));
            ps.setTimestamp(c++, new java.sql.Timestamp(dtTo.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                LogFarmEntry item = new LogFarmEntry();
                items.add(item);
                c = 1;
                item.setIdNode(rs.getLong(c++));
                item.setDt(rs.getTimestamp(c++));
                item.setPlots(rs.getInt(c++));
                item.setProofs(rs.getInt(c++));
                item.setTime(rs.getInt(c++));
                item.setPlotsTotal(rs.getInt(c++));
            }
            rs.close();
            ps.close();
            
        } // try
        
        return items;
    }

    public static List<ReadAccess> getReadAccess(Date dtFrom, Date dtTo, long[] diskIds) throws Exception {

        List<ReadAccess> items = new LinkedList<>();
        
        try (Connection connection = DB.getConnection()) {
            
            String where1 = "";

            if (diskIds != null && diskIds.length > 0) {

                where1 = " AND id_disk IN (";
                for (int i = 0; i < diskIds.length; i++) 
                    where1 += (i > 0?",":"")+diskIds[i];
                where1 += ")";
            }
            
            String SQL = 
            "SELECT id_disk,dt,read_time,success" +
            "  FROM ReadAccess" +
            " WHERE 1=1 " + where1 +
            "   AND dt BETWEEN ? AND ?" +
            " ORDER BY dt";

            PreparedStatement ps = connection.prepareStatement(SQL);
            int c = 1;
            ps.setTimestamp(c++, new java.sql.Timestamp(dtFrom.getTime()));
            ps.setTimestamp(c++, new java.sql.Timestamp(dtTo.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                ReadAccess item = new ReadAccess();
                items.add(item);
                c = 1;
                item.setDiskId(rs.getLong(c++));
                item.setDt(rs.getTimestamp(c++));
                item.setReadTime(rs.getInt(c++));
                item.setSuccess(rs.getBoolean(c++));
            }
            rs.close();
            ps.close();
            
        } // try
        
        return items;
    }

    public static List<DiskSmart> getDiskSmart(Date dtFrom, Date dtTo, long[] diskIds) throws Exception {

        List<DiskSmart> items = new LinkedList<>();
        
        try (Connection connection = DB.getConnection()) {
            
            String where1 = "";

            if (diskIds != null && diskIds.length > 0) {

                where1 = " AND id_disk IN (";
                for (int i = 0; i < diskIds.length; i++) 
                    where1 += (i > 0?",":"")+diskIds[i];
                where1 += ")";
            }
            
            String SQL = 
            "SELECT id_disk,dt," +
                "Raw_Read_Error_Rate_VALUE,"+
                "Raw_Read_Error_Rate_RAW_VALUE,"+
                "Seek_Error_Rate_VALUE,"+
                "Seek_Error_Rate_RAW_VALUE,"+
                "Power_On_Hours_RAW_VALUE,"+
                "Temperature_Celsius_VALUE,"+
                "Temperature_Celsius_RAW_VALUE,"+
                "Reallocated_Sector_Count_VALUE,"+
                "Reallocated_Sector_Count_RAW_VALUE,"+
                "Spin_Retry_Count_VALUE,"+
                "Spin_Retry_Count_RAW_VALUE,"+
                "Reallocated_Event_Count_VALUE,"+
                "Reallocated_Event_Count_RAW_VALUE,"+
                "Current_Pending_Sector_VALUE,"+
                "Current_Pending_Sector_RAW_VALUE,"+
                "Offline_Uncorrectable_VALUE,"+
                "Offline_Uncorrectable_RAW_VALUE"+
            "  FROM DiskSmart" +
            " WHERE 1=1 " + where1 +
            "   AND dt BETWEEN ? AND ?" +
            " ORDER BY dt";

            PreparedStatement ps = connection.prepareStatement(SQL);
            int c = 1;
            ps.setTimestamp(c++, new java.sql.Timestamp(dtFrom.getTime()));
            ps.setTimestamp(c++, new java.sql.Timestamp(dtTo.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                DiskSmart item = new DiskSmart();
                items.add(item);
                c = 1;
                item.setDiskId(rs.getLong(c++));
                item.setDt(rs.getTimestamp(c++));
                item.Raw_Read_Error_Rate_VALUE = rs.getInt(c++);
                item.Raw_Read_Error_Rate_RAW_VALUE = rs.getLong(c++);
                item.Seek_Error_Rate_VALUE = rs.getInt(c++);
                item.Seek_Error_Rate_RAW_VALUE = rs.getLong(c++);
                item.Power_On_Hours_RAW_VALUE = rs.getInt(c++);
                item.Temperature_Celsius_VALUE = rs.getInt(c++);
                item.Temperature_Celsius_RAW_VALUE = rs.getInt(c++);
                item.Reallocated_Sector_Count_VALUE = rs.getInt(c++);
                item.Reallocated_Sector_Count_RAW_VALUE = rs.getLong(c++);
                item.Spin_Retry_Count_VALUE = rs.getInt(c++);
                item.Spin_Retry_Count_RAW_VALUE = rs.getLong(c++);
                item.Reallocated_Event_Count_VALUE = rs.getInt(c++);
                item.Reallocated_Event_Count_RAW_VALUE = rs.getLong(c++);
                item.Current_Pending_Sector_VALUE = rs.getInt(c++);
                item.Current_Pending_Sector_RAW_VALUE = rs.getLong(c++);
                item.Offline_Uncorrectable_VALUE = rs.getInt(c++);
                item.Offline_Uncorrectable_RAW_VALUE = rs.getLong(c++);
            }
            rs.close();
            ps.close();
            
        } // try
        
        return items;
    }
    
}