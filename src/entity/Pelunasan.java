/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author gudangHS
 */
public class Pelunasan {

    private String id;
    private String transactionId;
    private long payment;
    private LogInformation log = new LogInformation();

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public void setPayment(long payment) {
        this.payment = payment;
    }

    public long getPayment() {
        return this.payment;
    }

    public void setLog(LogInformation log) {
        this.log = log;
    }

    public LogInformation getLog() {
        return this.log;
    }
}
