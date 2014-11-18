/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import entity.InventoryCategory;
import entity.SupplierCustomer;
import entity.Transaction;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author gudangHS
 */
public class ImportExcel extends javax.swing.JFrame {

    public Connection con;
    public DateFormat df;
    public Date date;
    public Number qty;
    public String item = "null";
    public String jenis = "null";
    public String supplierCustomer = "null";
    public String region = "null";
    public Number price;
    public Number status;
    public Number totalPrices;
    public double totalPrices2 = 0;
    public Number grup = 0;
    public Number grup2 = 0;
    public String transactionId = "";
    public Number nota;
    public long count;

    /**
     * Creates new form ImportExcel
     */
    public ImportExcel() {
        initComponents();
        databaseConnection();
        df = new SimpleDateFormat("dd/MM/yyyy");
        layerApel.setVisible(false);
        layerKardus.setVisible(false);
    }

    public final void databaseConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/hsputra_accounting";
            String username = "root";
            String password = "";
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String randomId() {
        String id = UUID.randomUUID().toString().replace("-", "");
        return id;
    }

    public String saveCategory(String name, String code, Number total, Date createDate) {
        try {
            String id = randomId();
            String query = "INSERT INTO inventory_category (id, create_date, update_date, name, item_code, total_items)"
                    + " VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, id);
            pre.setObject(2, createDate);
            pre.setObject(3, new Date(System.currentTimeMillis()));
            pre.setObject(4, name);
            pre.setObject(5, code);
            pre.setObject(6, total);
            pre.execute();

            return id;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public boolean updateCategoryById(String id, double total) {
        try {
            String query = "UPDATE inventory_category SET total_items = ? WHERE id = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, total);
            pre.setObject(2, id);
            pre.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public InventoryCategory checkCategori(String name) {
        InventoryCategory ic;
        try {
            String query = "SELECT * FROM inventory_category WHERE name LIKE ? ORDER BY name";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, name);

            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                ic = new InventoryCategory();
                ic.setId(rs.getString("id"));
                ic.setName(rs.getString("name"));
                ic.setTotalItems(rs.getInt("total_items"));
                return ic;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String saveRegion(String name) {
        try {
            String id = randomId();
            String query = "INSERT INTO region (id, name, create_date, update_date, code)"
                    + "VALUES (?, ?, ?, ?, ?)";
            count = rowCount("region");
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, id);
            pre.setObject(2, name);
            pre.setObject(3, new Date(System.currentTimeMillis()));
            pre.setObject(4, new Date(System.currentTimeMillis()));
            pre.setObject(5, "R" + count++);
            pre.execute();

            return id;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public String checkRegion(String wilayah) {
        try {
            String query = "SELECT * FROM region WHERE name LIKE ? ORDER BY name";

            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, wilayah);

            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                return rs.getString("id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public String saveSupplierCustomer(String name, String address, Number phone, Number price, String description, Number type,
            String regionId) {
        try {
            String id = randomId();
            String query = "INSERT INTO supplier_customer (id, name, address, create_date, update_date, phone, price, description, type, region, code)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            count = rowCount("supplier_customer");
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, id);
            pre.setObject(2, name);
            pre.setObject(3, address);
            pre.setObject(4, new Date(System.currentTimeMillis()));
            pre.setObject(5, new Date(System.currentTimeMillis()));
            pre.setObject(6, phone);
            pre.setObject(7, price);
            pre.setObject(8, description);
            pre.setObject(9, type);
            pre.setObject(10, regionId);
            pre.setObject(11, "SC" + (count++));
            pre.execute();
            return id;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public String checkSupplierOrCustomer(String name, String region, int type) {
        try {
            String query = "SELECT * FROM supplier_customer WHERE type = ? AND name LIKE ? AND region LIKE ? ORDER BY name";

            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, type);
            pre.setObject(2, name);
            pre.setObject(3, region);

            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                return rs.getString("id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public String saveTransaksi(Date createDate, Number transactionType, Number invoices, int paymentStatus,
            double totalPrice, Number nota) {
        try {
            String id = randomId();
            String query = "INSERT INTO transaction (id, create_date, transaction_type, invoices, payment_type, total_prices, nota, code)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            count = rowCount("transaction");;
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, id);
            pre.setObject(2, createDate);
            pre.setObject(3, transactionType);
            pre.setObject(4, invoices);
            pre.setObject(5, paymentStatus);
            pre.setObject(6, totalPrice);
            pre.setObject(7, ((nota + "").replace(".0", "")));
            pre.setObject(8, "T" + (count++));

            pre.execute();
            return id;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public String updateTransaksi(String id, double totalPrice) {
        try {
            String query = "UPDATE transaction SET total_prices = ? WHERE id = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, totalPrice);
            pre.setObject(2, id);
            pre.execute();
            return id;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public String saveTransaksiDetail(Date createDate, Number quantity, Number price, Number totalPrice, String itemId,
            Number plu, String description, String transaction_id, String supplierCustomerId, String itemPartai, Number sisaStock) {
        try {
            String id = randomId();
            String query = "INSERT INTO transaction_detail (id, create_date, quantity, price, total_prices, "
                    + "supplier_customer_id, inventory_category_id, plu, transaction_id, item_partai, description, sisa_stock)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, id);
            pre.setObject(2, createDate);
            pre.setObject(3, quantity);
            pre.setObject(4, price);
            pre.setObject(5, totalPrice);
            pre.setObject(6, supplierCustomerId);
            pre.setObject(7, itemId);
            pre.setObject(8, plu);
            pre.setObject(9, transaction_id);
            pre.setObject(10, itemPartai);
            pre.setObject(11, description);
            pre.setObject(12, sisaStock);
            pre.execute();
            return id;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public void saveBeli(Date date, Number qty, String item, String supplier, String region, Number price, Number status, Number totalPrices, Number grup) {
        String regionId = checkRegion(region);
        InventoryCategory ic = checkCategori(item);
        String supplierId = checkSupplierOrCustomer(supplier, regionId, SupplierCustomer.SupplierCustomerType.SUPPLIER.ordinal());
        DecimalFormat df = new DecimalFormat("0");

        if (ic == null) {
            ic = new InventoryCategory();
            ic.setId(saveCategory(item, item, qty, date));
        } else {
            double total = Double.parseDouble(ic.getTotalItems() + "") + Double.parseDouble(qty + "");
            ic.setTotalItems(Integer.parseInt(df.format(total)));
            updateCategoryById(ic.getId(), ic.getTotalItems());
        }
        if (regionId.equalsIgnoreCase("")) {
            regionId = saveRegion(region);
        }
        if (supplierId.equalsIgnoreCase("")) {
            supplierId = saveSupplierCustomer(supplier, region, 0, price, region, SupplierCustomer.SupplierCustomerType.SUPPLIER.ordinal(), regionId);
        }

        int payment_status = Integer.parseInt((status + "").replace(".0", ""));

        if (grup2.equals(grup)) {
            saveTransaksiDetail(date, qty, price, totalPrices, ic.getId(), 0, "", transactionId, supplierId, "", ic.getTotalItems());
            totalPrices2 = totalPrices2 + Double.parseDouble(totalPrices.toString());
        } else {
            updateTransaksi(transactionId, totalPrices2);
            totalPrices2 = Double.parseDouble(totalPrices.toString());
            transactionId = saveTransaksi(date, Transaction.TransactionType.BUY.ordinal(), 0, payment_status, totalPrices2, 0);
            saveTransaksiDetail(date, qty, price, totalPrices, ic.getId(), 0, "", transactionId, supplierId, "", ic.getTotalItems());
            grup2 = grup;
        }
    }

    public void saveJual(Date date, Number qty, String item, String customer, String region, Number price, Number nota, Number totalPrices, String jenis, Number status) {
        String regionId = checkRegion(region);
        InventoryCategory ic = checkCategori(jenis);
        String customerId = checkSupplierOrCustomer(customer, regionId, SupplierCustomer.SupplierCustomerType.CUSTOMER.ordinal());
        DecimalFormat df = new DecimalFormat("0");

        double total = Double.parseDouble(ic.getTotalItems() + "") - Double.parseDouble(qty + "");
        ic.setTotalItems(Integer.parseInt(df.format(total)));
        updateCategoryById(ic.getId(), ic.getTotalItems());

        if (regionId.equalsIgnoreCase("")) {
            regionId = saveRegion(region);
        }
        if (customerId.equalsIgnoreCase("")) {
            customerId = saveSupplierCustomer(customer, region, 0, price, region, SupplierCustomer.SupplierCustomerType.CUSTOMER.ordinal(), regionId);
        }

        int payment_status = Integer.parseInt((status + "").replace(".0", ""));

        grup = nota;
        if (grup2.equals(grup)) {
            saveTransaksiDetail(date, qty, price, totalPrices, ic.getId(), 0, ((nota + "").replace(".0", "")), transactionId, customerId, item, ic.getTotalItems());
            totalPrices2 = totalPrices2 + Double.parseDouble(totalPrices.toString());
        } else {
            updateTransaksi(transactionId, totalPrices2);
            totalPrices2 = Double.parseDouble(totalPrices.toString());
            transactionId = saveTransaksi(date, Transaction.TransactionType.SELL.ordinal(), 0, payment_status, totalPrices2, nota);
            saveTransaksiDetail(date, qty, price, totalPrices, ic.getId(), 0, ((nota + "").replace(".0", "")), transactionId, customerId, item, ic.getTotalItems());
            grup2 = grup;
        }

    }

    public void readXLSXFilePembelianApel() throws Exception {

        InputStream ExcelFileToRead = new FileInputStream("ImportBeli.xlsx");
        XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);

        XSSFWorkbook test = new XSSFWorkbook();

        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;

        Iterator rows = sheet.rowIterator();
        int i = 0;
        while (rows.hasNext()) {
            System.out.print((i++) + " >> ");
            row = (XSSFRow) rows.next();
            Iterator cells = row.cellIterator();
            while (cells.hasNext()) {
                cell = (XSSFCell) cells.next();
                if (cell.getRowIndex() > 0) {

                    if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {

                        if ((cell.getColumnIndex() == 0)) {
                            String longDate = cell.getStringCellValue();
                            longDate = longDate.replace("z", "");
                            date = df.parse(longDate);
                            System.out.print(df.format(date) + " ");
                        } else if (cell.getColumnIndex() == 2) {
                            item = cell.getStringCellValue();
                            System.out.print(item + " ");
                        } else if (cell.getColumnIndex() == 3) {
                            supplierCustomer = cell.getStringCellValue();
                            System.out.print(supplierCustomer + " ");
                        } else if (cell.getColumnIndex() == 4) {
                            region = cell.getStringCellValue();
                            System.out.print(region + " ");
                        }

                    } else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                        if (cell.getColumnIndex() == 1) {
                            qty = cell.getNumericCellValue();
                            System.out.print(cell.getNumericCellValue() + " ");
                        } else if (cell.getColumnIndex() == 5) {
                            price = cell.getNumericCellValue();
                            System.out.print(cell.getNumericCellValue() + " ");
                        } else if (cell.getColumnIndex() == 6) {
                            status = cell.getNumericCellValue();
                            System.out.print(cell.getNumericCellValue() + " ");
                        } else if (cell.getColumnIndex() == 8) {
                            grup = cell.getNumericCellValue();
                            System.out.print(cell.getNumericCellValue() + " ");
                        }

                    }

                    if (cell.getColumnIndex() == 7) {
                        totalPrices = cell.getNumericCellValue();
                        System.out.print(cell.getNumericCellValue() + " ");
                    }
                }
            }
            if (supplierCustomer.equalsIgnoreCase("null")) {
                System.out.print(" >> SUPPLIER/CUSTOMER");
                System.out.print(" >> NOT SAVED");
            } else if (region.equalsIgnoreCase("null")) {
                System.out.print(" >> REGION");
                System.out.print(" >> NOT SAVED");
            } else if (item.equalsIgnoreCase("null")) {
                System.out.print(" >> ITEM");
                System.out.print(" >> NOT SAVED");
            } else if ((item.matches("(?i).*BRUTO"))) {
                System.out.print(" >> NOT SAVED");
            } else {
                saveBeli(date, qty, item, supplierCustomer, region, price, status, totalPrices, grup);
                System.out.print(" >> SAVED");
            }
            System.out.println();
        }
    }

    public void readXLSXFilePenjualanApel() throws Exception {

        InputStream ExcelFileToRead = new FileInputStream("ImportJual.xlsx");
        XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);

        XSSFWorkbook test = new XSSFWorkbook();

        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;

        Iterator rows = sheet.rowIterator();
        while (rows.hasNext()) {
            row = (XSSFRow) rows.next();
            Iterator cells = row.cellIterator();
            while (cells.hasNext()) {
                cell = (XSSFCell) cells.next();
                if (cell.getRowIndex() > 0) {

                    if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {

                        if ((cell.getColumnIndex() == 0)) {
                            String longDate = cell.getStringCellValue();
                            longDate = longDate.replace("z", "");
                            date = df.parse(longDate);
                            System.out.print(df.format(date) + " ");
                        } else if (cell.getColumnIndex() == 2) {
                            item = cell.getStringCellValue();
                            System.out.print(cell.getStringCellValue() + " ");
                        } else if (cell.getColumnIndex() == 8) {
                            supplierCustomer = cell.getStringCellValue();
                            System.out.print(cell.getStringCellValue() + " ");
                        } else if (cell.getColumnIndex() == 9) {
                            region = cell.getStringCellValue();
                            System.out.print(cell.getStringCellValue() + " ");
                        } else if (cell.getColumnIndex() == 10) {
                            jenis = cell.getStringCellValue();
                            System.out.print(cell.getStringCellValue() + " ");
                        }

                    } else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
                        if (cell.getColumnIndex() == 1) {
                            nota = cell.getNumericCellValue();
                            System.out.print(cell.getNumericCellValue() + " ");
                        } else if (cell.getColumnIndex() == 3) {
                            qty = cell.getNumericCellValue();
                            System.out.print(cell.getNumericCellValue() + " ");
                        } else if (cell.getColumnIndex() == 4) {
                            price = cell.getNumericCellValue();
                            System.out.print(cell.getNumericCellValue() + " ");
                        } else if (cell.getColumnIndex() == 11) {
                            status = cell.getNumericCellValue();
                            System.out.print(cell.getNumericCellValue() + " ");
                        }

                    }

                    if (cell.getColumnIndex() == 5) {
                        totalPrices = cell.getNumericCellValue();
                        System.out.print(cell.getNumericCellValue() + " ");
                    }

                }
            }
            if (supplierCustomer.equalsIgnoreCase("null")) {
                System.out.print(" >> SUPPLIER/CUSTOMER");
                System.out.print(" >> NOT SAVED");
            } else if (region.equalsIgnoreCase("null")) {
                System.out.print(" >> REGION");
                System.out.print(" >> NOT SAVED");
            } else if (item.equalsIgnoreCase("null")) {
                System.out.print(" >> ITEM");
                System.out.print(" >> NOT SAVED");
            } else if (jenis.equalsIgnoreCase("null")) {
                System.out.print(" >> JENIS");
                System.out.print(" >> NOT SAVED");
            } else {
                saveJual(date, qty, item, supplierCustomer, region, price, nota, totalPrices, jenis, status);
                System.out.print(" >> SAVED");
            }
            System.out.println();
        }

    }

    public void readXLSXFilePacking() throws Exception {

        InputStream ExcelFileToRead = new FileInputStream("Import.xlsx");
        XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);

        XSSFWorkbook test = new XSSFWorkbook();

        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;

        Iterator rows = sheet.rowIterator();
        while (rows.hasNext()) {
            row = (XSSFRow) rows.next();
            Iterator cells = row.cellIterator();
            while (cells.hasNext()) {
                cell = (XSSFCell) cells.next();
                if (cell.getRowIndex() > 0) {
                    if ((cell.getColumnIndex() == 0)) {
                        String longDate = cell.getStringCellValue();
                        longDate = longDate.replace("z", "");
                        date = df.parse(longDate);
                        System.out.print(df.format(date) + " ");
                    } else if (cell.getColumnIndex() == 1) {
                        supplierCustomer = cell.getStringCellValue();
                        System.out.print(cell.getStringCellValue() + " ");
                    } else if (cell.getColumnIndex() == 2) {
                        region = cell.getStringCellValue();
                        System.out.print(cell.getStringCellValue() + " ");
                    } else if (cell.getColumnIndex() == 3) {
                        item = cell.getStringCellValue();
                        System.out.print(cell.getStringCellValue() + " ");
                    } else if (cell.getColumnIndex() == 4) {
                        qty = cell.getNumericCellValue();
                        System.out.print(cell.getNumericCellValue() + " ");
                    } else if (cell.getColumnIndex() == 5) {
                        price = cell.getNumericCellValue();
                        System.out.print(cell.getNumericCellValue() + " ");
                    } else if (cell.getColumnIndex() == 6) {
                        totalPrices = cell.getNumericCellValue();
                        System.out.print(cell.getNumericCellValue() + " ");
                    } else if (cell.getColumnIndex() == 7) {
                        status = cell.getNumericCellValue();
                        System.out.print(cell.getNumericCellValue() + " ");
                    } else if (cell.getColumnIndex() == 8) {
                        grup = cell.getNumericCellValue();
                        System.out.print(cell.getNumericCellValue() + " ");
                    }
                }
            }
            System.out.println();
            if (status != null) {
                if (Integer.parseInt(status.toString().replace(".0", "")) == 0) {
                    saveBeli(date, qty, item, supplierCustomer, region, price, status, totalPrices, grup);
                }
                if (Integer.parseInt(status.toString().replace(".0", "")) == 1) {
                    saveJual(date, qty, item, supplierCustomer, region, price, grup, totalPrices, item, status);
                }
            }

        }

    }

    public int rowCount(String table) {
        try {
            String query = "select code from " + table + " order by code desc limit 1";

            PreparedStatement pre = con.prepareStatement(query);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                return rs.getInt("code");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public void updateRegionById(String id, int code) {
        try {
            String query = "UPDATE region SET code = ? WHERE id = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, code);
            pre.setObject(2, id);
            pre.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateTransactionById(String id, int code) {
        try {
            String query = "UPDATE transaction SET code = ? WHERE id = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, code);
            pre.setObject(2, id);
            pre.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateSupplierCustomerById(String id, int code) {
        try {
            String query = "UPDATE supplier_customer SET code = ? WHERE id = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, code);
            pre.setObject(2, id);
            pre.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void getDataSupplierCustomer() {
        try {
            String query = "select id, type from supplier_customer order by create_date asc";
            PreparedStatement pre = con.prepareStatement(query);
            pre.execute();
            ResultSet rs = pre.executeQuery();
            System.out.println("SUPPLIER CUSTOMER");
            int code = 0;
            while (rs.next()) {
                updateSupplierCustomerById(rs.getString("id"), (code++));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void getDataRegion() {
        try {
            String query = "select id from region order by create_date asc";
            PreparedStatement pre = con.prepareStatement(query);
            pre.execute();
            ResultSet rs = pre.executeQuery();
            System.out.println("REGION");
            int code = 1;
            while (rs.next()) {
                updateRegionById(rs.getString("id"), (code++));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void getDataTransaction() {
        try {
            String query = "select id, transaction_type from transaction order by create_date asc";
            PreparedStatement pre = con.prepareStatement(query);
            pre.execute();
            ResultSet rs = pre.executeQuery();
            System.out.println("TRANSACTION");
            int code = 1;
            while (rs.next()) {
                updateTransactionById(rs.getString("id"), (code++));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        btnApel = new javax.swing.JRadioButton();
        btnPacking = new javax.swing.JRadioButton();
        layerApel = new javax.swing.JLayeredPane();
        btnJual = new javax.swing.JButton();
        btnBeli = new javax.swing.JButton();
        layerKardus = new javax.swing.JLayeredPane();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        buttonGroup1.add(btnApel);
        btnApel.setText("APEL");
        btnApel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApelActionPerformed(evt);
            }
        });

        buttonGroup1.add(btnPacking);
        btnPacking.setText("PACKING");
        btnPacking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPackingActionPerformed(evt);
            }
        });

        btnJual.setText("Import Penjualan");
        btnJual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJualActionPerformed(evt);
            }
        });
        btnJual.setBounds(20, 73, 170, 40);
        layerApel.add(btnJual, javax.swing.JLayeredPane.DEFAULT_LAYER);

        btnBeli.setText("Import Pembelian");
        btnBeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBeliActionPerformed(evt);
            }
        });
        btnBeli.setBounds(20, 20, 170, 40);
        layerApel.add(btnBeli, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jButton1.setText("Import");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.setBounds(20, 20, 170, 40);
        layerKardus.add(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jButton2.setText("Generate");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(layerApel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(btnApel)
                        .addGap(18, 18, 18)
                        .addComponent(btnPacking)
                        .addGap(0, 56, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(layerKardus)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnApel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPacking, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(layerApel, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(layerKardus, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBeliActionPerformed
        try {
            // TODO add your handling code here:
            readXLSXFilePembelianApel();
        } catch (Exception ex) {
            Logger.getLogger(ImportExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBeliActionPerformed

    private void btnJualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJualActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            readXLSXFilePenjualanApel();
        } catch (Exception ex) {
            Logger.getLogger(ImportExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnJualActionPerformed

    private void btnApelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApelActionPerformed
        // TODO add your handling code here:
        layerApel.setVisible(true);
        layerKardus.setVisible(false);
    }//GEN-LAST:event_btnApelActionPerformed

    private void btnPackingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPackingActionPerformed
        // TODO add your handling code here:
        layerApel.setVisible(false);
        layerKardus.setVisible(true);
    }//GEN-LAST:event_btnPackingActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // TODO add your handling code here:
            readXLSXFilePacking();
        } catch (Exception ex) {
            Logger.getLogger(ImportExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        getDataRegion();
        getDataSupplierCustomer();
        getDataTransaction();
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ImportExcel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ImportExcel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ImportExcel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ImportExcel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ImportExcel().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton btnApel;
    private javax.swing.JButton btnBeli;
    private javax.swing.JButton btnJual;
    private javax.swing.JRadioButton btnPacking;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLayeredPane layerApel;
    private javax.swing.JLayeredPane layerKardus;
    // End of variables declaration//GEN-END:variables
}
