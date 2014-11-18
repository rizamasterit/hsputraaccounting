/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author gudangHS
 */
public class PrintToPDF{

    public Connection con;
    public Document doc;
    public Font font;

    public PrintToPDF(){
        databaseConnection();
    } 
    
    public void databaseConnection() {
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

    public void PDFTemplate(String pdfName, float marginLeft, float marginRight, float marginTop, float marginBottom) {
        try {
            Rectangle pagesize = new Rectangle(615f, 930f);
            doc = new Document(pagesize.rotate(), marginLeft, marginRight, marginTop, marginBottom);
            PdfWriter.getInstance(doc, new FileOutputStream(pdfName));
            font = FontFactory.getFont("verdana", 12, Font.BOLD);
        } catch (Exception ex) {
        }
    }

    public void printTransaction(int type, String titleDateFrom, String titleDateTo) {
        try {
            PDFTemplate("Transaksion.pdf", 0, 0, 0, 0);

            doc.open();
            Paragraph paragraph = new Paragraph();
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setFont(font);
            paragraph.add("PENJUALAN");
            doc.add(paragraph);
            paragraph.clear();
            paragraph.add("PER JUNI 2013");
            doc.add(paragraph);
            paragraph.add(Chunk.NEWLINE);

            PdfPTable pdfTable = new PdfPTable(10);
            pdfTable.setWidthPercentage(100);
            pdfTable.setWidths(new int[]{2, 2, 2, 1, 1, 2, 3, 3, 1, 2});

            //table header
            pdfTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfTable.addCell(new Phrase("TGL", font));
            pdfTable.addCell(new Phrase("PETANI", font));
            pdfTable.addCell(new Phrase("WILAYAH", font));
            pdfTable.addCell(new Phrase("ITEM", font));
            pdfTable.addCell(new Phrase("QTY", font));
            pdfTable.addCell(new Phrase("HARGA", font));
            pdfTable.addCell(new Phrase("TOTAL HARGA", font));
            pdfTable.addCell(new Phrase("BAYAR", font));
            pdfTable.addCell(new Phrase("STATUS", font));
            pdfTable.addCell(new Phrase("KET", font));

            String query = "SELECT * FROM transaction ";
            if (type != 2) {
                query += "WHERE transaction_type LIKE ? ";
            }

            query += "ORDER BY create_date";
            PreparedStatement pre = con.prepareStatement(query);
            if (type != 2) {
                pre.setObject(1, type);
            }
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                String supplierCustomerName = getSupplierCustomerById(rs.getString("supplier_customer_id"));
                String regionName = getRegionIdBySupplierCustomer(rs.getString("supplier_customer_id"));
                String categoryName = getCategoryById(rs.getString("inventory_category_id"));

                //table content
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTable.addCell(rs.getString("update_date"));
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfTable.addCell(supplierCustomerName);
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTable.addCell(regionName);
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfTable.addCell(categoryName);
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTable.addCell(rs.getString("quantity"));
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                pdfTable.addCell(rs.getString("price"));
                pdfTable.addCell(rs.getString("total_prices"));
                pdfTable.addCell(rs.getString("payment"));
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTable.addCell((rs.getInt("payment_type") == 0 ? "LUNAS" : "HUTANG"));
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfTable.addCell(rs.getString("description"));
            }
            doc.add(pdfTable);
            doc.close();
        } catch (Exception ex) {
        }
    }

    public String getSupplierCustomerById(String id) {
        try {
            String query = "SELECT * FROM supplier_customer WHERE id = ? ORDER BY name";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, id);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public String getRegionIdBySupplierCustomer(String supplierCustomerId) {
        try {
            String query = "SELECT r.name AS name FROM supplier_customer sc, region r "
                    + "WHERE sc.id like ? AND sc.region = r.id ORDER BY name";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, supplierCustomerId);

            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public String getCategoryById(String id) {
        try {
            String query = "SELECT * FROM inventory_category WHERE id = ? ORDER BY name";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, id);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                return rs.getString("item_code");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
