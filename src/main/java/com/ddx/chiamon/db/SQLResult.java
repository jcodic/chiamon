package com.ddx.chiamon.db;

/**
 *
 * @author ddx
 */
public class SQLResult {
    
    private long returnId = -1; // id of inserted object
    private int inserted;
    private int updated;
    private int deleted;
    private int skipped;

    public long getReturnId() {
        return returnId;
    }

    public void setReturnId(long returnId) {
        this.returnId = returnId;
    }

    public int getInserted() {
        return inserted;
    }

    public void setInserted(int inserted) {
        this.inserted = inserted;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }
    
    public void addInserted() {
        
        inserted++;
    }

    public void addInserted(int n) {
        
        inserted += n;
    }

    public void addUpdated() {
        
        updated++;
    }

    public void addUpdated(int n) {
        
        updated += n;
    }

    public void addDeleted() {
        
        deleted++;
    }

    public void addDeleted(int n) {
        
        deleted += n;
    }

    public void addSkipped() {
        
        skipped++;
    }
    
    public void addSkipped(int n) {
        
        skipped += n;
    }

    @Override
    public String toString() {
        
        return getInfo();
    }
    
    public String getInfo() {
        
        return "added = "+inserted+"; updated = "+updated+"; deleted = "+deleted+"; skipped = "+skipped;
    }
    
    public void append(SQLResult result) {
        
        inserted += result.inserted;
        updated += result.updated;
        deleted += result.deleted;
        skipped += result.skipped;
    }
    
}