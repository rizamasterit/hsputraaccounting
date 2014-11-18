/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author KANCILZ
 */
public class Company {

    private String id;
    private String name;
    private String address;
    private int phone;
    private String description;
    private LogInformation log = new LogInformation();

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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setLog(LogInformation log) {
        this.log = log;
    }

    public LogInformation getLog() {
        return this.log;
    }
}
