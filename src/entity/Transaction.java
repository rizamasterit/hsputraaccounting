/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author KANCILZ
 */
public class Transaction {

    public enum TransactionType {

        BUY, SELL, OTHER, STOCK
    }

    public enum PaymentStatus {

        FULL, DEBT
    }
    private String id;
    private LogInformation log = new LogInformation();
    private double quantity;
    private double price;
    private String invoices;
    private TransactionType transactionType;
    private PaymentStatus paymentStatus;
    private double totalPrice;
    private String description;
    private String supplierCustomerId;
    private String companyId;
    private String inventoryCategoryId;
    private int payment;
    private int plu;
    private int commission;
    private int employeeSalaries;
    private int operatingCosts;
    private int packingCosts;
    private String itemPartai;
    private String nota;
    private double sisaStock;
    private String code;
    private int no;
    private double totalMarketPrice;
    private boolean tebasan;

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

    public void setLog(LogInformation log) {
        this.log = log;
    }

    public LogInformation getLog() {
        return this.log;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getQuantity() {
        return this.quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return this.price;
    }

    public void setInvoices(String invoices) {
        this.invoices = invoices;
    }

    public String getInvoices() {
        return this.invoices;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalPrices() {
        return this.totalPrice;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public PaymentStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    public void setSupplierCustomerId(String supplierCustomerId) {
        this.supplierCustomerId = supplierCustomerId;
    }

    public String getSupplierCustomerId() {
        return this.supplierCustomerId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyId() {
        return this.companyId;
    }

    public void setInventoryCategoryId(String inventoryCategoryId) {
        this.inventoryCategoryId = inventoryCategoryId;
    }

    public String getInventoryCategoryId() {
        return this.inventoryCategoryId;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getPayment() {
        return this.payment;
    }

    public void setPlu(int plu) {
        this.plu = plu;
    }

    public int getPlu() {
        return this.plu;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public int getCommission() {
        return this.commission;
    }

    public void setOperatingCosts(int operatingCosts) {
        this.operatingCosts = operatingCosts;
    }

    public int getOperatingCosts() {
        return this.operatingCosts;
    }

    public void setPackingCosts(int packingCosts) {
        this.packingCosts = packingCosts;
    }

    public int getPackingCosts() {
        return this.packingCosts;
    }

    public void setItemPartai(String itemPartai) {
        this.itemPartai = itemPartai;
    }

    public String getItemPartai() {
        return this.itemPartai;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getNota() {
        return this.nota;
    }

    public void setsisaStock(double sisaStock) {
        this.sisaStock = sisaStock;
    }

    public double getsisaStock() {
        return this.sisaStock;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public double getNo() {
        return this.no;
    }

    public void setTotalMarketPrice(double totalMarketPrice) {
        this.totalMarketPrice = totalMarketPrice;
    }

    public double getTotalMarketPrice() {
        return this.totalMarketPrice;
    }

    public boolean isTebasan() {
        return this.tebasan;
    }

    public void setTebasan(boolean tebasan) {
        this.tebasan = tebasan;
    }
}
