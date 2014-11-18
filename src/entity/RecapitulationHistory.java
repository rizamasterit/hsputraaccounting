/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.Date;

/**
 *
 * @author KANCILZ
 */
public class RecapitulationHistory {

    private String id;
    private Date fromDate;
    private Date toDate;
    private int saldoAwal;
    private int totalPengeluaran;
    private int totalPemasukan;
    private int employeeSalaries;
    private int saldoAkhir;
    private LogInformation log = new LogInformation();

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getFromDate() {
        return this.fromDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getToDate() {
        return this.toDate;
    }

    public void setLog(LogInformation log) {
        this.log = log;
    }

    public LogInformation getLog() {
        return this.log;
    }

    public void setEmployeeSalaries(int employeeSalaries) {
        this.employeeSalaries = employeeSalaries;
    }

    public int getEmployeeSalaries() {
        return this.employeeSalaries;
    }

    public void setSaldoAwal(int saldoAwal) {
        this.saldoAwal = saldoAwal;
    }

    public int getSaldoAwal() {
        return this.saldoAwal;
    }

    public void setTotalPengeluaran(int totalPengeluaran) {
        this.totalPengeluaran = totalPengeluaran;
    }

    public int getTotalPengeluaran() {
        return this.totalPengeluaran;
    }

    public void setTotalPemasukan(int totalPemasukan) {
        this.totalPemasukan = totalPemasukan;
    }

    public int getTotalPemasukan() {
        return this.totalPemasukan;
    }

    public void setSaldoAkhir(int saldoAkhir) {
        this.saldoAkhir = saldoAkhir;
    }

    public int getSaldoAkhir() {
        return this.saldoAkhir;
    }
}
