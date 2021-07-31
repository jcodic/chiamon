package com.ddx.chiamon.db;

import com.ddx.chiamon.common.data.ClientAccess;
import com.ddx.chiamon.common.data.Disk;
import com.ddx.chiamon.common.data.DiskSmart;
import com.ddx.chiamon.common.data.LogErrorEntry;
import com.ddx.chiamon.common.data.LogFarmEntry;
import com.ddx.chiamon.common.data.Node;
import com.ddx.chiamon.common.data.ReadAccess;
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
public class Storage {

    public static synchronized SQLResult insertHarvesterNode(Node node) throws Exception {
	
        SQLResult result = new SQLResult();
        
        Connection connection = DB.getConnection();
        connection.setAutoCommit(false);

        try {
	
        ////////////////////////////
        // Nodes
        ////////////////////////////
        
        node.setId(-1);
            
        String SQL = "SELECT id FROM HarvesterNode WHERE uid=? ORDER BY dt DESC LIMIT 1";
        
        PreparedStatement ps = connection.prepareStatement(SQL);
        int c = 1;
        ps.setString(c++, node.getUid());
	ResultSet rs = ps.executeQuery();
	if (rs.next()) {
            
            long id = rs.getLong(1);
            node.setId(id);
            result.setReturnId(id);
        }
	rs.close();
        ps.close();
        
        if (node.getId() != -1) {

            SQL = "UPDATE HarvesterNode SET dt=?,ip=? WHERE id=?";

            ps = connection.prepareStatement(SQL);
            c = 1;
            ps.setTimestamp(c++, new java.sql.Timestamp(node.getDt().getTime()));
            ps.setString(c++, node.getIp());
            ps.setLong(c++, node.getId());
            int i = ps.executeUpdate();
            if (i > 0) result.addUpdated(i);
            ps.close();

        } else {

            SQL = "INSERT INTO HarvesterNode(uid,dt,ip) VALUES (?,?,?) RETURNING id";

            ps = connection.prepareStatement(SQL);
            c = 1;
            ps.setString(c++, node.getUid());
            ps.setTimestamp(c++, new java.sql.Timestamp(node.getDt().getTime()));
            ps.setString(c++, node.getIp());
            rs = ps.executeQuery();
            if (rs.next()) {

                long id = rs.getLong(1);
                node.setId(id);
                result.setReturnId(id);
                result.addInserted();
            }
            rs.close();
            ps.close();
        }
        
        ////////////////////////////
        // Disks
        ////////////////////////////
        
        if (node.getDisks() != null && !node.getDisks().isEmpty()) {

            for (Disk disk : node.getDisks()) {
                
                disk.setId(-1);
                
                SQL = "SELECT id FROM Disk WHERE uid=? ORDER BY dt DESC LIMIT 1";

                ps = connection.prepareStatement(SQL);

                c = 1;
                ps.setString(c++, disk.getUid());
                rs = ps.executeQuery();
                if (rs.next()) {

                    c = 1;
                    disk.setId(rs.getLong(c++));
                }

                rs.close();
                ps.close();
                
                if (disk.getId() != -1) {
                    
                    SQL = "UPDATE Disk SET id_harvester_node=?,dt=?,name=?,path=?,size=?,plots_count=?,plots_size=? WHERE id=?";

                    ps = connection.prepareStatement(SQL);
                    c = 1;
                    ps.setLong(c++, node.getId());
                    ps.setTimestamp(c++, new java.sql.Timestamp(disk.getDt().getTime()));
                    ps.setString(c++, disk.getName());
                    ps.setString(c++, disk.getPath());
                    ps.setLong(c++, disk.getSize());
                    ps.setInt(c++, disk.getPlotsCount());
                    ps.setLong(c++, disk.getPlotsSize());
                    ps.setLong(c++, disk.getId());
                    int i = ps.executeUpdate();
                    if (i > 0) result.addUpdated(i);
                    ps.close();
                    
                } else {
                    
                    SQL = "INSERT INTO Disk(id_harvester_node,uid,dt,name,path,size,plots_count,plots_size) "+
                          "VALUES (?,?,?,?,?,?,?,?) RETURNING id";

                    ps = connection.prepareStatement(SQL);

                    c = 1;
                    ps.setLong(c++, node.getId());
                    ps.setString(c++, disk.getUid());
                    ps.setTimestamp(c++, new java.sql.Timestamp(disk.getDt().getTime()));
                    ps.setString(c++, disk.getName());
                    ps.setString(c++, disk.getPath());
                    ps.setLong(c++, disk.getSize());
                    ps.setInt(c++, disk.getPlotsCount());
                    ps.setLong(c++, disk.getPlotsSize());
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        disk.setId(rs.getLong(1));
                        result.addInserted();
                    }
                    rs.close();
                    ps.close();
                }
                
            } // for :: disk update or insert
            
                
            ////////////////////////////
            // Disks :: ReadAccess
            ////////////////////////////
        
            SQL = "INSERT INTO ReadAccess(id_disk,dt,filename,seek_at,read_size,read_time,success,info) "+
                  "VALUES (?,?,?,?,?,?,?,?)";
	
            ps = connection.prepareStatement(SQL);
                
            final int batchSize = 25;
            int batchCount = 0;

            for (Disk disk : node.getDisks()) {
                
                if (disk.getReadAccesses() != null && !disk.getReadAccesses().isEmpty()) {
                    
                    for (ReadAccess ra : disk.getReadAccesses()) {
                        
                        c = 1;
                        ps.setLong(c++, disk.getId());
                        ps.setTimestamp(c++, new java.sql.Timestamp(ra.getDt().getTime()));
                        ps.setString(c++, ra.getFileName());
                        ps.setLong(c++, ra.getSeekAt());
                        ps.setLong(c++, ra.getReadSize());
                        ps.setLong(c++, ra.getReadTime());
                        ps.setBoolean(c++, ra.isSuccess());
                        ps.setString(c++, ra.getInfo());
                        ps.addBatch();

                        if(++batchCount % batchSize == 0) {
                            int[] iresults = ps.executeBatch();
                            for (int i : iresults) if (i > 0) result.addInserted(i);
                        }
                    }
                }
            }
            
            if (batchCount > 0) {
                
                int[] iresults = ps.executeBatch();
                for (int i : iresults) if (i > 0) result.addInserted(i);
            }
            
            ps.close();


            ////////////////////////////
            // Disks :: Smart
            ////////////////////////////
        
            SQL = "INSERT INTO DiskSmart(id_disk,dt,"+
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
                    "Offline_Uncorrectable_RAW_VALUE) "+
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
            ps = connection.prepareStatement(SQL);
                
            batchCount = 0;

            for (Disk disk : node.getDisks()) {
                
                if (disk.getSmarts() != null && !disk.getSmarts().isEmpty()) {
                    
                    for (DiskSmart ds : disk.getSmarts()) {
                        
                        c = 1;
                        ps.setLong(c++, disk.getId());
                        ps.setTimestamp(c++, new java.sql.Timestamp(ds.getDt().getTime()));
                        ps.setInt(c++, ds.Raw_Read_Error_Rate_VALUE);
                        ps.setLong(c++, ds.Raw_Read_Error_Rate_RAW_VALUE);
                        ps.setInt(c++, ds.Seek_Error_Rate_VALUE);
                        ps.setLong(c++, ds.Seek_Error_Rate_RAW_VALUE);
                        ps.setInt(c++, ds.Power_On_Hours_RAW_VALUE);
                        ps.setInt(c++, ds.Temperature_Celsius_VALUE);
                        ps.setInt(c++, ds.Temperature_Celsius_RAW_VALUE);
                        ps.setInt(c++, ds.Reallocated_Sector_Count_VALUE);
                        ps.setLong(c++, ds.Reallocated_Sector_Count_RAW_VALUE);
                        ps.setInt(c++, ds.Spin_Retry_Count_VALUE);
                        ps.setLong(c++, ds.Spin_Retry_Count_RAW_VALUE);
                        ps.setInt(c++, ds.Reallocated_Event_Count_VALUE);
                        ps.setLong(c++, ds.Reallocated_Event_Count_RAW_VALUE);
                        ps.setInt(c++, ds.Current_Pending_Sector_VALUE);
                        ps.setLong(c++, ds.Current_Pending_Sector_RAW_VALUE);
                        ps.setInt(c++, ds.Offline_Uncorrectable_VALUE);
                        ps.setLong(c++, ds.Offline_Uncorrectable_RAW_VALUE);
                        ps.addBatch();

                        if(++batchCount % batchSize == 0) {
                            int[] iresults = ps.executeBatch();
                            for (int i : iresults) if (i > 0) result.addInserted(i);
                        }
                    }
                }
            }
            
            if (batchCount > 0) {
                
                int[] iresults = ps.executeBatch();
                for (int i : iresults) if (i > 0) result.addInserted(i);
            }
            
            ps.close();

            
        } // if :: disks

        ////////////////////////////
        // LogsFarm
        ////////////////////////////
        
        if (node.getLogsFarm() != null && !node.getLogsFarm().isEmpty()) {
            
            SQL = "SELECT MAX(dt) FROM LogFarm WHERE id_harvester_node=?";

            ps = connection.prepareStatement(SQL);

            Date dtMax = null;
            
            ps.setLong(1, node.getId());
            rs = ps.executeQuery();
            if (rs.next()) dtMax = rs.getTimestamp(1);
            rs.close();
            ps.close();
            
            List<LogFarmEntry> accepted = new LinkedList<>();
            
            for (LogFarmEntry item : node.getLogsFarm()) if (dtMax == null || dtMax.before(item.getDt())) accepted.add(item);

            if (!accepted.isEmpty()) {

                SQL = "INSERT INTO LogFarm(id_harvester_node,dt,plots,block,proofs,time,plots_total) "+
                      "VALUES (?,?,?,?,?,?,?)";

                ps = connection.prepareStatement(SQL);

                final int batchSize = 25;
                int batchCount = 0;

                for (LogFarmEntry item : accepted) {

                    c = 1;
                    ps.setLong(c++, node.getId());
                    ps.setTimestamp(c++, new java.sql.Timestamp(item.getDt().getTime()));
                    ps.setInt(c++, item.getPlots());
                    ps.setString(c++, item.getBlock());
                    ps.setInt(c++, item.getProofs());
                    ps.setInt(c++, item.getTime());
                    ps.setInt(c++, item.getPlotsTotal());
                    ps.addBatch();

                    if(++batchCount % batchSize == 0) {
                        int[] iresults = ps.executeBatch();
                        for (int i : iresults) if (i > 0) result.addInserted(i);
                    }
                }

                if (batchCount > 0) {

                    int[] iresults = ps.executeBatch();
                    for (int i : iresults) if (i > 0) result.addInserted(i);
                }

                ps.close();
                
            } // if :: new records
            
        } // if :: logs farm

        ////////////////////////////
        // LogsError
        ////////////////////////////
        
        if (node.getLogsError()!= null && !node.getLogsError().isEmpty()) {
            
            SQL = "SELECT MAX(dt) FROM LogError WHERE id_harvester_node=?";

            ps = connection.prepareStatement(SQL);

            Date dtMax = null;
            
            ps.setLong(1, node.getId());
            rs = ps.executeQuery();
            if (rs.next()) dtMax = rs.getTimestamp(1);
            rs.close();
            ps.close();
            
            List<LogErrorEntry> accepted = new LinkedList<>();
            
            for (LogErrorEntry item : node.getLogsError()) if (dtMax == null || dtMax.before(item.getDt())) accepted.add(item);

            if (!accepted.isEmpty()) {

                SQL = "INSERT INTO LogError(id_harvester_node,dt) VALUES (?,?)";

                ps = connection.prepareStatement(SQL);

                final int batchSize = 25;
                int batchCount = 0;

                for (LogErrorEntry item : accepted) {

                    c = 1;
                    ps.setLong(c++, node.getId());
                    ps.setTimestamp(c++, new java.sql.Timestamp(item.getDt().getTime()));
                    ps.addBatch();

                    if(++batchCount % batchSize == 0) {
                        int[] iresults = ps.executeBatch();
                        for (int i : iresults) if (i > 0) result.addInserted(i);
                    }
                }

                if (batchCount > 0) {

                    int[] iresults = ps.executeBatch();
                    for (int i : iresults) if (i > 0) result.addInserted(i);
                }

                ps.close();
                
            } // if :: new records
            
        } // if :: logs error
        
        connection.commit();

        } finally {

            connection.setAutoCommit(true);
            connection.close();
        }

	return result;
    }

    public static void insertClientAccess(ClientAccess item) throws Exception {
	
        try (Connection connection = DB.getConnection()) {
	
            String SQL = "INSERT INTO ClientAccess(uid,dt,ip,cmd,success) VALUES (?,?,?,?,?)";

            PreparedStatement ps = connection.prepareStatement(SQL);
            int c = 1;
            ps.setString(c++, item.getUid());
            ps.setTimestamp(c++, new java.sql.Timestamp(item.getDt().getTime()));
            ps.setString(c++, item.getIp());
            ps.setString(c++, item.getCmd());
            ps.setBoolean(c++, item.isSuccess());
            ps.execute();
            ps.close();

        }
    }
    
}
