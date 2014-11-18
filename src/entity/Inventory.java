/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author KANCILZ
 */
public class Inventory {

    private String id;
    private String name;
    private Company companyId;
    private LogInformation log = new LogInformation();

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

    public void setCompanyId(Company companyId) {
        this.companyId = companyId;
    }

    public Company getCompanyId() {
        return this.companyId;
    }
}
