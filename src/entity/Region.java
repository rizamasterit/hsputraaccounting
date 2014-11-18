/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author gudangHS
 */
public class Region {

    private String id;
    private String name;
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

    public void setLog(LogInformation log) {
        this.log = log;
    }

    public LogInformation getLog() {
        return this.log;
    }
}
