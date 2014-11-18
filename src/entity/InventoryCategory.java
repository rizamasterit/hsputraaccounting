/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author KANCILZ
 */
public class InventoryCategory {

    private String id;
    private LogInformation log = new LogInformation();
    private String name;
    private String itemCode;
    private double totalItems;
    private String parrentId;
    
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setLog(LogInformation log) {
        this.log = log;
    }

    public LogInformation getLog() {
        return this.log;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
    
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemCode() {
        return this.itemCode;
    }
    
    public void setTotalItems(double totalItems) {
        this.totalItems = totalItems;
    }

    public double getTotalItems() {
        return this.totalItems;
    }
    
    public void setParrentId(String parrentId) {
        this.parrentId = parrentId;
    }

    public String getParrentId() {
        return this.parrentId;
    }
}
