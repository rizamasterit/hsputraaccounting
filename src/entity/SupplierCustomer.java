/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author KANCILZ
 */
public class SupplierCustomer {

    public enum SupplierCustomerType {
        SUPPLIER, CUSTOMER
    }
    private String id;
    private String name;
    private String address;
    private int phone;
    private int price;
    private String description;
    private String regionId;
    private SupplierCustomerType supplierCustomerType;
    private LogInformation log = new LogInformation();
    private String code;
    
    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public int getPhone() {
        return this.phone;
    }
    
    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return this.price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
    
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionId() {
        return this.regionId;
    }
    
    public void setLog(LogInformation log) {
        this.log = log;
    }

    public LogInformation getLog() {
        return this.log;
    }

    public void setSupplierCustomerType(SupplierCustomerType supplierCustomerType) {
        this.supplierCustomerType = supplierCustomerType;
    }

    public SupplierCustomerType getSupplierCustomerType() {
        return this.supplierCustomerType;
    }
}
