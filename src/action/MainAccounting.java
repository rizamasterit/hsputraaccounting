/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entity.InventoryCategory;
import entity.Pelunasan;
import entity.RecapitulationHistory;
import entity.Region;
import entity.SupplierCustomer;
import entity.Transaction;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author KANCILZ
 */
public class MainAccounting extends javax.swing.JFrame {

    public Connection con;
    public Document doc;
    public com.itextpdf.text.Font font;
    public DateFormat df;
    public NumberFormat nf;
    public double max = 5;
    public double totalRows = 0;
    public double rowPerPage = 0;
    public double page = 0;
    public int paymentType = 0;
    long count;
    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();

    public MainAccounting() {
        initComponents();
        databaseConnection();
        df = new SimpleDateFormat("dd-MMM-yy");
        nf = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);
        setVisibleElement();
        setStyle();
    }

    private void setStyle() {
        setTitle("PT. HS. PUTRA");
        setIconImage(new ImageIcon(getClass().getResource("/resource/logo.PNG")).getImage());
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        setDefaultLookAndFeelDecorated(true);
        setVisible(true);
        setLayout(new BorderLayout());
        setContentPane(new JLabel(new ImageIcon(getClass().getResource("/resource/frame2.png"))));
        setLayout(new FlowLayout());
        addElement();
        setTableForNumericCellType();
    }

    private void setTableForNumericCellType() {
        setTableAlignment(jTable1, 4);
        setTableAlignment(jTable1, 5);
        setTableAlignment(jTable1, 6);
        setTableAlignment(jTable2, 3);
        setTableAlignment(jTable2, 4);
        setTableAlignment(jTable2, 5);
    }

    private void setTableAlignment(JTable table, int cellIndex) {
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(cellIndex).setCellRenderer(rightRenderer);
    }

    private void addElement() {
        add(frameTambahPemasokPelanggan);
        add(frameCari);
        add(frameStokBarang);
        add(frameTambahKategori);
        add(framekategori);
        add(frameWilayah);
        add(frameCariWilayah);
        add(frameRiwayatTransaksiJual);
        add(frameRekap);
        add(frameTambahStok);
        add(frameRiwayatTransaksiBeli);
        add(framePelunasanTransaksiBeli);
        add(framePelunasan);
        add(framePelunasanTransaksiJual);
        add(frameJual);
        add(frameBeli);
        add(framePengiriman);
        add(frameKomisi);
        add(frameGaji);
        add(frameTransaksiLain);
        add(frameHargaTransaksi);
    }

    private void setVisibleElement() {
        frameTambahPemasokPelanggan.setVisible(false);
        frameCari.setVisible(false);
        frameStokBarang.setVisible(false);
        frameTambahKategori.setVisible(false);
        framekategori.setVisible(false);
        frameWilayah.setVisible(false);
        frameCariWilayah.setVisible(false);
        frameRiwayatTransaksiJual.setVisible(false);
        frameRekap.setVisible(false);
        frameTambahStok.setVisible(false);
        frameRiwayatTransaksiBeli.setVisible(false);
        framePelunasanTransaksiBeli.setVisible(false);
        framePelunasan.setVisible(false);
        fldPelunasanTransactionId.setVisible(false);
        framePelunasanTransaksiJual.setVisible(false);
        frameJual.setVisible(false);
        frameBeli.setVisible(false);
        framePengiriman.setVisible(false);
        frameKomisi.setVisible(false);
        frameGaji.setVisible(false);
        frameTransaksiLain.setVisible(false);
        panelEditWilayah.setVisible(false);
        panelEditKategori.setVisible(false);
        jPanel4.setVisible(false);
        jButton27.setEnabled(false);
        jButton35.setEnabled(false);
        jPanel9.setVisible(false);
        jPanel10.setVisible(false);
        frameHargaTransaksi.setVisible(false);
        jInternalFrame1.setVisible(false);
    }

    public final void databaseConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/hsputra_accounting";
            String username = "root";
            String password = "";
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Koneksi Database Gagal");
            System.exit(WIDTH);
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
        buttonGroup2 = new javax.swing.ButtonGroup();
        jScrollBar1 = new javax.swing.JScrollBar();
        frameTambahPemasokPelanggan = new javax.swing.JInternalFrame();
        lblName = new javax.swing.JLabel();
        lblTelp = new javax.swing.JLabel();
        lblWilayah = new javax.swing.JLabel();
        lblAlamat = new javax.swing.JLabel();
        fldName = new javax.swing.JTextField();
        fldTelp = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        fldAlamat = new javax.swing.JTextArea();
        btnSimpan = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        lblJudul = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        fldKeterangan = new javax.swing.JTextArea();
        lblKeterangan = new javax.swing.JLabel();
        fldWilayah = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        frameCari = new javax.swing.JInternalFrame();
        jdlCari = new javax.swing.JLabel();
        fldCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblResult = new javax.swing.JTable();
        jButton31 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jTextField21 = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jTextField22 = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jButton33 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        frameStokBarang = new javax.swing.JInternalFrame();
        jdlStokBarang = new javax.swing.JLabel();
        btnCariStokBarang = new javax.swing.JButton();
        fldCariStokBarang = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblStokBarangResult = new javax.swing.JTable();
        frameTambahKategori = new javax.swing.JInternalFrame();
        jdlKategori1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        fldNamaKategori = new javax.swing.JTextField();
        fldKodeBarang = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnSimpanKategori = new javax.swing.JButton();
        btnBatalKategori = new javax.swing.JButton();
        framekategori = new javax.swing.JInternalFrame();
        jdlKategori = new javax.swing.JLabel();
        btnCariKategori = new javax.swing.JButton();
        fldCariKategori = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblKategoriResult = new javax.swing.JTable();
        jButton25 = new javax.swing.JButton();
        panelEditKategori = new javax.swing.JPanel();
        jTextField18 = new javax.swing.JTextField();
        jTextField19 = new javax.swing.JTextField();
        jButton29 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        frameWilayah = new javax.swing.JInternalFrame();
        jdlWilayah = new javax.swing.JLabel();
        lblNamaWilayah = new javax.swing.JLabel();
        fldWilayahBaru = new javax.swing.JTextField();
        btnSimpanWilayah = new javax.swing.JButton();
        btnBatalWilayah = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        frameCariWilayah = new javax.swing.JInternalFrame();
        jdlCariWilayah = new javax.swing.JLabel();
        fldCariWilayah = new javax.swing.JTextField();
        btnCariWilayah = new javax.swing.JButton();
        panelEditWilayah = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblWilayahResult = new javax.swing.JTable();
        jButton21 = new javax.swing.JButton();
        frameRiwayatTransaksiJual = new javax.swing.JInternalFrame();
        lblJudul1 = new javax.swing.JLabel();
        scrollTabelTransaksiJual = new javax.swing.JScrollPane();
        tblTransaksiJual = new javax.swing.JTable();
        fldTransaksiJualDateTo = new org.jdesktop.swingx.JXDatePicker();
        jLabel4 = new javax.swing.JLabel();
        fldTransaksiJualDateFrom = new org.jdesktop.swingx.JXDatePicker();
        jScrollPane14 = new javax.swing.JScrollPane();
        tblDetailJual = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jButton35 = new javax.swing.JButton();
        btnCetakTransaksiJual = new javax.swing.JButton();
        jButton38 = new javax.swing.JButton();
        jComboBox7 = new javax.swing.JComboBox();
        jComboBox8 = new javax.swing.JComboBox();
        frameRekap = new javax.swing.JInternalFrame();
        lblJudul2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        fldDateFrom = new org.jdesktop.swingx.JXDatePicker();
        fldDateTo = new org.jdesktop.swingx.JXDatePicker();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblSisaItem = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblTotalPengeluaran = new javax.swing.JLabel();
        lblTotalPemasukan = new javax.swing.JLabel();
        lblTotalSaldo = new javax.swing.JLabel();
        btnRekap = new javax.swing.JButton();
        btnCetakLabaRugi = new javax.swing.JButton();
        btnSaveRekap = new javax.swing.JButton();
        btnCetakStockKardus = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        frameTambahStok = new javax.swing.JInternalFrame();
        jdlTambahStok = new javax.swing.JLabel();
        lblItem = new javax.swing.JLabel();
        fldItemTambahStok = new javax.swing.JComboBox();
        btnBatalTambahStok = new javax.swing.JButton();
        btnSimpanTambahStok = new javax.swing.JButton();
        lblStok = new javax.swing.JLabel();
        fldStok = new javax.swing.JTextField();
        fldJumlahTambah = new javax.swing.JTextField();
        lblJumlahTambah = new javax.swing.JLabel();
        lblTotalStok = new javax.swing.JLabel();
        fldTotalStok = new javax.swing.JTextField();
        lblStokCampur = new javax.swing.JLabel();
        fldSisaStok = new javax.swing.JTextField();
        fldFromItemStok = new javax.swing.JComboBox();
        lblItem1 = new javax.swing.JLabel();
        frameRiwayatTransaksiBeli = new javax.swing.JInternalFrame();
        lblJudul3 = new javax.swing.JLabel();
        fldTransaksiBeliDateTo = new org.jdesktop.swingx.JXDatePicker();
        jLabel11 = new javax.swing.JLabel();
        fldTransaksiBeliDateFrom = new org.jdesktop.swingx.JXDatePicker();
        scrollTabelTransaksiBeli = new javax.swing.JScrollPane();
        tblTransaksiBeli = new javax.swing.JTable();
        jScrollPane13 = new javax.swing.JScrollPane();
        tblDetailBeli = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jButton27 = new javax.swing.JButton();
        btnCetakTransaksiBeli = new javax.swing.JButton();
        jButton39 = new javax.swing.JButton();
        jComboBox5 = new javax.swing.JComboBox();
        jComboBox6 = new javax.swing.JComboBox();
        framePelunasanTransaksiBeli = new javax.swing.JInternalFrame();
        lblJudul4 = new javax.swing.JLabel();
        fldPelunasanBeliDateTo = new org.jdesktop.swingx.JXDatePicker();
        jLabel12 = new javax.swing.JLabel();
        fldPelunasanBeliDateFrom = new org.jdesktop.swingx.JXDatePicker();
        scrollTabelTransaksiBeli1 = new javax.swing.JScrollPane();
        tblPelunasanBeli = new javax.swing.JTable();
        btnPilihTransaksiBeli = new javax.swing.JButton();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblDetailPelunasanBeli = new javax.swing.JTable();
        framePelunasan = new javax.swing.JInternalFrame();
        jdlPelunasan = new javax.swing.JLabel();
        lblPelunasanNama = new javax.swing.JLabel();
        fldPelunasanNama = new javax.swing.JTextField();
        lblPelunasanTotalHarga = new javax.swing.JLabel();
        fldPelunasanTotalHarga = new javax.swing.JTextField();
        lblPelunasanBayar = new javax.swing.JLabel();
        fldPelunasanBayar = new javax.swing.JTextField();
        btnBatalPelunasan = new javax.swing.JButton();
        fldPelunasanTotalPembayaran = new javax.swing.JTextField();
        lblPelunasanTotalPembayaran = new javax.swing.JLabel();
        fldPelunasanSisaHutang = new javax.swing.JTextField();
        lblPelunasanSisaHutang = new javax.swing.JLabel();
        fldPelunasanTransactionId = new javax.swing.JTextField();
        lblPelunasanNama1 = new javax.swing.JLabel();
        btnSimpanPelunasan = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblPelunasan = new javax.swing.JTable();
        fldPelunasanDate = new org.jdesktop.swingx.JXDatePicker();
        lblPelunasanNota = new javax.swing.JLabel();
        lblPelunasanFaktur = new javax.swing.JLabel();
        fldPelunasanNota = new javax.swing.JTextField();
        fldPelunasanFaktur = new javax.swing.JTextField();
        framePelunasanTransaksiJual = new javax.swing.JInternalFrame();
        lblJudul5 = new javax.swing.JLabel();
        scrollTabelTransaksiJual1 = new javax.swing.JScrollPane();
        tblPelunasanJual = new javax.swing.JTable();
        fldTransaksiJualDateTo1 = new org.jdesktop.swingx.JXDatePicker();
        jLabel14 = new javax.swing.JLabel();
        fldTransaksiJualDateFrom1 = new org.jdesktop.swingx.JXDatePicker();
        btnPilihTransaksiJual = new javax.swing.JButton();
        jScrollPane15 = new javax.swing.JScrollPane();
        tblDetailPelunasanJual = new javax.swing.JTable();
        frameJual = new javax.swing.JInternalFrame();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton3 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        lblJudul6 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jXDatePicker2 = new org.jdesktop.swingx.JXDatePicker();
        jLabel33 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jButton22 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        frameBeli = new javax.swing.JInternalFrame();
        lblJudul7 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jTextField9 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        jLabel34 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jButton26 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jScrollPane19 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel54 = new javax.swing.JLabel();
        framePengiriman = new javax.swing.JInternalFrame();
        lblJudul8 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        fldDatePengiriman = new org.jdesktop.swingx.JXDatePicker();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton8 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        frameGaji = new javax.swing.JInternalFrame();
        lblJudul9 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane17 = new javax.swing.JScrollPane();
        tblGaji = new javax.swing.JTable();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        fldDateGaji = new org.jdesktop.swingx.JXDatePicker();
        frameTransaksiLain = new javax.swing.JInternalFrame();
        lblJudul11 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        fldTransaksiJualDateFrom4 = new org.jdesktop.swingx.JXDatePicker();
        jScrollPane18 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        fldTotalBiaya = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jButton20 = new javax.swing.JButton();
        frameKomisi = new javax.swing.JInternalFrame();
        lblJudul10 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        fldTransaksiJualDateFrom3 = new org.jdesktop.swingx.JXDatePicker();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        frameHargaTransaksi = new javax.swing.JInternalFrame();
        jPanel11 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jTextField23 = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jTextField31 = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jTextField26 = new javax.swing.JTextField();
        jTextField27 = new javax.swing.JTextField();
        jTextField30 = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jButton40 = new javax.swing.JButton();
        jButton42 = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jTextField25 = new javax.swing.JTextField();
        jTextField29 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        lblJudul12 = new javax.swing.JLabel();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        lblJudul13 = new javax.swing.JLabel();
        jScrollPane20 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        listMenu = new javax.swing.JMenuBar();
        menuPembelian = new javax.swing.JMenu();
        menuCariSupplier = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        menuTambahPemasok = new javax.swing.JMenuItem();
        menuPenjualan = new javax.swing.JMenu();
        menuCariCustomer = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        menuTambahPelanggan = new javax.swing.JMenuItem();
        menuRekap = new javax.swing.JMenu();
        menuInventori = new javax.swing.JMenu();
        menuCariKategori = new javax.swing.JMenuItem();
        menuStokBarang = new javax.swing.JMenuItem();
        menuTambahKategori = new javax.swing.JMenuItem();
        menTambahStok = new javax.swing.JMenuItem();
        menuRiwayatTransaksi = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        menuBeli = new javax.swing.JMenuItem();
        menuCariPembelian = new javax.swing.JMenuItem();
        menuPelunasanBeli = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        menuJual = new javax.swing.JMenuItem();
        menuCariPenjualan = new javax.swing.JMenuItem();
        menuPelunasanJual = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        menuWilayah = new javax.swing.JMenu();
        menuCariWilayah = new javax.swing.JMenuItem();
        menuTambahWilayah = new javax.swing.JMenuItem();
        menuKeluar = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        frameTambahPemasokPelanggan.setClosable(true);
        frameTambahPemasokPelanggan.setVisible(true);

        lblName.setForeground(new java.awt.Color(255, 0, 0));
        lblName.setText("Nama Supplier");

        lblTelp.setForeground(new java.awt.Color(255, 0, 0));
        lblTelp.setText("No. Telp.");

        lblWilayah.setForeground(new java.awt.Color(255, 0, 0));
        lblWilayah.setText("Wilayah");

        lblAlamat.setText("Alamat");

        fldAlamat.setColumns(20);
        fldAlamat.setRows(5);
        jScrollPane1.setViewportView(fldAlamat);

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/save.png"))); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/delete.png"))); // NOI18N
        btnBatal.setText("Batal");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        lblJudul.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblJudul.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudul.setText("Tambah Supplier");

        fldKeterangan.setColumns(20);
        fldKeterangan.setRows(5);
        jScrollPane2.setViewportView(fldKeterangan);

        lblKeterangan.setText("Keterangan");

        jLabel24.setText("Kode");

        jTextField10.setEditable(false);

        javax.swing.GroupLayout frameTambahPemasokPelangganLayout = new javax.swing.GroupLayout(frameTambahPemasokPelanggan.getContentPane());
        frameTambahPemasokPelanggan.getContentPane().setLayout(frameTambahPemasokPelangganLayout);
        frameTambahPemasokPelangganLayout.setHorizontalGroup(
            frameTambahPemasokPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameTambahPemasokPelangganLayout.createSequentialGroup()
                .addGroup(frameTambahPemasokPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frameTambahPemasokPelangganLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblJudul, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(frameTambahPemasokPelangganLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(frameTambahPemasokPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(frameTambahPemasokPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblTelp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblWilayah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblKeterangan, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel24))
                        .addGap(28, 28, 28)
                        .addGroup(frameTambahPemasokPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fldName)
                            .addComponent(fldTelp)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(frameTambahPemasokPelangganLayout.createSequentialGroup()
                                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 124, Short.MAX_VALUE))
                            .addComponent(jTextField10)
                            .addComponent(jScrollPane1)
                            .addComponent(fldWilayah, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        frameTambahPemasokPelangganLayout.setVerticalGroup(
            frameTambahPemasokPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameTambahPemasokPelangganLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJudul)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(frameTambahPemasokPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameTambahPemasokPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(fldName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameTambahPemasokPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fldTelp, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTelp))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameTambahPemasokPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblWilayah)
                    .addComponent(fldWilayah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameTambahPemasokPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAlamat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameTambahPemasokPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblKeterangan)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(frameTambahPemasokPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(17, 17, 17))
        );

        frameCari.setClosable(true);
        frameCari.setMaximumSize(new java.awt.Dimension(484, 543));
        frameCari.setMinimumSize(new java.awt.Dimension(484, 543));
        frameCari.setVisible(true);

        jdlCari.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jdlCari.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jdlCari.setText("Supplier");

        fldCari.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fldCariFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fldCariFocusLost(evt);
            }
        });

        btnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/search.png"))); // NOI18N
        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        tblResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode", "Nama", "No. Telp", "Alamat", "Wilayah"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblResult);

        jButton31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/edit.png"))); // NOI18N
        jButton31.setText("Ubah");
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel41.setText("Kode");

        jTextField20.setEditable(false);

        jLabel42.setText("Nama");

        jLabel43.setText("No. Telp");

        jLabel45.setText("Wilayah");

        jButton33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/delete.png"))); // NOI18N
        jButton33.setText("Batal");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        jButton34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/save.png"))); // NOI18N
        jButton34.setText("Simpan");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField21, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                            .addComponent(jTextField22)))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                            .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                    .addComponent(jButton34)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton33)
                    .addComponent(jButton34))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout frameCariLayout = new javax.swing.GroupLayout(frameCari.getContentPane());
        frameCari.getContentPane().setLayout(frameCariLayout);
        frameCariLayout.setHorizontalGroup(
            frameCariLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frameCariLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameCariLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jdlCari, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, frameCariLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(fldCari, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCari)))
                .addContainerGap())
        );
        frameCariLayout.setVerticalGroup(
            frameCariLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameCariLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jdlCari)
                .addGap(27, 27, 27)
                .addGroup(frameCariLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fldCari))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        frameStokBarang.setClosable(true);
        frameStokBarang.setMaximumSize(new java.awt.Dimension(484, 327));
        frameStokBarang.setMinimumSize(new java.awt.Dimension(484, 327));
        frameStokBarang.setVisible(true);

        jdlStokBarang.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jdlStokBarang.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jdlStokBarang.setText("Stok Barang");

        btnCariStokBarang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/search.png"))); // NOI18N
        btnCariStokBarang.setText("Cari");
        btnCariStokBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariStokBarangActionPerformed(evt);
            }
        });

        fldCariStokBarang.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fldCariStokBarangFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fldCariStokBarangFocusLost(evt);
            }
        });

        tblStokBarangResult.setAutoCreateRowSorter(true);
        tblStokBarangResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nama", "Kode", "Jumlah"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblStokBarangResult.setEnabled(false);
        jScrollPane4.setViewportView(tblStokBarangResult);

        javax.swing.GroupLayout frameStokBarangLayout = new javax.swing.GroupLayout(frameStokBarang.getContentPane());
        frameStokBarang.getContentPane().setLayout(frameStokBarangLayout);
        frameStokBarangLayout.setHorizontalGroup(
            frameStokBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameStokBarangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameStokBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jdlStokBarang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(frameStokBarangLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(fldCariStokBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCariStokBarang))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        frameStokBarangLayout.setVerticalGroup(
            frameStokBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameStokBarangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jdlStokBarang)
                .addGap(27, 27, 27)
                .addGroup(frameStokBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCariStokBarang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fldCariStokBarang))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        frameTambahKategori.setClosable(true);
        frameTambahKategori.setVisible(true);

        jdlKategori1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jdlKategori1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jdlKategori1.setText("Tambah Kategori");

        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("Name");

        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("Kode Barang");

        btnSimpanKategori.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/save.png"))); // NOI18N
        btnSimpanKategori.setText("Simpan");
        btnSimpanKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanKategoriActionPerformed(evt);
            }
        });

        btnBatalKategori.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/delete.png"))); // NOI18N
        btnBatalKategori.setText("Batal");
        btnBatalKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalKategoriActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout frameTambahKategoriLayout = new javax.swing.GroupLayout(frameTambahKategori.getContentPane());
        frameTambahKategori.getContentPane().setLayout(frameTambahKategoriLayout);
        frameTambahKategoriLayout.setHorizontalGroup(
            frameTambahKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameTambahKategoriLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(frameTambahKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(frameTambahKategoriLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(fldNamaKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(frameTambahKategoriLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(frameTambahKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(frameTambahKategoriLayout.createSequentialGroup()
                                .addComponent(btnSimpanKategori)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBatalKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(fldKodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frameTambahKategoriLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jdlKategori1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        frameTambahKategoriLayout.setVerticalGroup(
            frameTambahKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameTambahKategoriLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jdlKategori1)
                .addGap(30, 30, 30)
                .addGroup(frameTambahKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(fldNamaKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameTambahKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(fldKodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(frameTambahKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnBatalKategori)
                    .addComponent(btnSimpanKategori))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        framekategori.setClosable(true);
        framekategori.setMaximumSize(new java.awt.Dimension(498, 482));
        framekategori.setMinimumSize(new java.awt.Dimension(498, 390));
        framekategori.setVisible(true);

        jdlKategori.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jdlKategori.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jdlKategori.setText("Kategori");

        btnCariKategori.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/search.png"))); // NOI18N
        btnCariKategori.setText("Cari");
        btnCariKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariKategoriActionPerformed(evt);
            }
        });

        fldCariKategori.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fldCariKategoriFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fldCariKategoriFocusLost(evt);
            }
        });

        tblKategoriResult.setAutoCreateRowSorter(true);
        tblKategoriResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nama", "Kode"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tblKategoriResult);

        jButton25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/edit.png"))); // NOI18N
        jButton25.setText("Ubah");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton25)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton25)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextField18.setEditable(false);

        jButton29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/save.png"))); // NOI18N
        jButton29.setText("Simpan");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jButton30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/delete.png"))); // NOI18N
        jButton30.setText("Batal");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        jLabel39.setText("Wilayah");

        jLabel40.setText("Kode");

        javax.swing.GroupLayout panelEditKategoriLayout = new javax.swing.GroupLayout(panelEditKategori);
        panelEditKategori.setLayout(panelEditKategoriLayout);
        panelEditKategoriLayout.setHorizontalGroup(
            panelEditKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditKategoriLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelEditKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelEditKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelEditKategoriLayout.createSequentialGroup()
                        .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextField18)
                    .addComponent(jTextField19))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelEditKategoriLayout.setVerticalGroup(
            panelEditKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditKategoriLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelEditKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelEditKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39))
                .addGap(18, 18, 18)
                .addGroup(panelEditKategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton29)
                    .addComponent(jButton30))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout framekategoriLayout = new javax.swing.GroupLayout(framekategori.getContentPane());
        framekategori.getContentPane().setLayout(framekategoriLayout);
        framekategoriLayout.setHorizontalGroup(
            framekategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(framekategoriLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(framekategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jdlKategori, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(framekategoriLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(fldCariKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCariKategori))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelEditKategori, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        framekategoriLayout.setVerticalGroup(
            framekategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(framekategoriLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jdlKategori)
                .addGap(30, 30, 30)
                .addGroup(framekategoriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCariKategori, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fldCariKategori))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelEditKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        frameWilayah.setClosable(true);
        frameWilayah.setVisible(true);

        jdlWilayah.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jdlWilayah.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jdlWilayah.setText("Tambah Wilayah");

        lblNamaWilayah.setForeground(new java.awt.Color(255, 0, 0));
        lblNamaWilayah.setText("Wilayah");

        btnSimpanWilayah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/save.png"))); // NOI18N
        btnSimpanWilayah.setText("Simpan");
        btnSimpanWilayah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanWilayahActionPerformed(evt);
            }
        });

        btnBatalWilayah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/delete.png"))); // NOI18N
        btnBatalWilayah.setText("Batal");
        btnBatalWilayah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalWilayahActionPerformed(evt);
            }
        });

        jLabel32.setText("Kode");

        jTextField11.setEditable(false);

        javax.swing.GroupLayout frameWilayahLayout = new javax.swing.GroupLayout(frameWilayah.getContentPane());
        frameWilayah.getContentPane().setLayout(frameWilayahLayout);
        frameWilayahLayout.setHorizontalGroup(
            frameWilayahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameWilayahLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jdlWilayah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(frameWilayahLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(frameWilayahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNamaWilayah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(frameWilayahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(fldWilayahBaru)
                    .addGroup(frameWilayahLayout.createSequentialGroup()
                        .addComponent(btnSimpanWilayah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBatalWilayah, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextField11))
                .addGap(31, 31, 31))
        );
        frameWilayahLayout.setVerticalGroup(
            frameWilayahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameWilayahLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jdlWilayah)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(frameWilayahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameWilayahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNamaWilayah)
                    .addComponent(fldWilayahBaru, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(frameWilayahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBatalWilayah)
                    .addComponent(btnSimpanWilayah))
                .addContainerGap())
        );

        frameCariWilayah.setClosable(true);
        frameCariWilayah.setMaximumSize(new java.awt.Dimension(488, 499));
        frameCariWilayah.setMinimumSize(new java.awt.Dimension(488, 390));
        frameCariWilayah.setName(""); // NOI18N
        frameCariWilayah.setVisible(true);

        jdlCariWilayah.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jdlCariWilayah.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jdlCariWilayah.setText("Wilayah");

        fldCariWilayah.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fldCariWilayahFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fldCariWilayahFocusLost(evt);
            }
        });

        btnCariWilayah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/search.png"))); // NOI18N
        btnCariWilayah.setText("Cari");
        btnCariWilayah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariWilayahActionPerformed(evt);
            }
        });

        jLabel35.setText("Kode");

        jLabel36.setText("Wilayah");

        jTextField14.setEditable(false);

        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/save.png"))); // NOI18N
        jButton23.setText("Simpan");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/delete.png"))); // NOI18N
        jButton24.setText("Batal");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelEditWilayahLayout = new javax.swing.GroupLayout(panelEditWilayah);
        panelEditWilayah.setLayout(panelEditWilayahLayout);
        panelEditWilayahLayout.setHorizontalGroup(
            panelEditWilayahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditWilayahLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelEditWilayahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelEditWilayahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelEditWilayahLayout.createSequentialGroup()
                        .addComponent(jButton23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextField15)
                    .addComponent(jTextField14))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelEditWilayahLayout.setVerticalGroup(
            panelEditWilayahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEditWilayahLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelEditWilayahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelEditWilayahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36))
                .addGap(18, 18, 18)
                .addGroup(panelEditWilayahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton23)
                    .addComponent(jButton24))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblWilayahResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode", "Wilayah"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(tblWilayahResult);

        jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/edit.png"))); // NOI18N
        jButton21.setText("Ubah");
        jButton21.setMaximumSize(new java.awt.Dimension(488, 499));
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout frameCariWilayahLayout = new javax.swing.GroupLayout(frameCariWilayah.getContentPane());
        frameCariWilayah.getContentPane().setLayout(frameCariWilayahLayout);
        frameCariWilayahLayout.setHorizontalGroup(
            frameCariWilayahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameCariWilayahLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameCariWilayahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelEditWilayah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jdlCariWilayah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frameCariWilayahLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(fldCariWilayah, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCariWilayah)
                .addContainerGap())
        );
        frameCariWilayahLayout.setVerticalGroup(
            frameCariWilayahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameCariWilayahLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jdlCariWilayah)
                .addGap(26, 26, 26)
                .addGroup(frameCariWilayahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCariWilayah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fldCariWilayah))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(panelEditWilayah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        frameRiwayatTransaksiJual.setClosable(true);
        frameRiwayatTransaksiJual.setVisible(true);

        lblJudul1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblJudul1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudul1.setText("Riwayat Transaksi");

        tblTransaksiJual.setAutoCreateRowSorter(true);
        tblTransaksiJual.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tanggal", "Kode", "Nota", "Customer", "No Fkt", "Total Harga", "Total Bersih"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTransaksiJual.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTransaksiJualMouseClicked(evt);
            }
        });
        scrollTabelTransaksiJual.setViewportView(tblTransaksiJual);
        tblTransaksiJual.getColumnModel().getColumn(1).setResizable(false);

        fldTransaksiJualDateTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fldTransaksiJualDateToActionPerformed(evt);
            }
        });

        jLabel4.setText("s/d");

        fldTransaksiJualDateFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fldTransaksiJualDateFromActionPerformed(evt);
            }
        });

        tblDetailJual.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Item", "Qty", "Harga", "Total Harga", "Wilayah"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDetailJual.setName("pencarian"); // NOI18N
        jScrollPane14.setViewportView(tblDetailJual);
        tblDetailJual.getColumnModel().getColumn(0).setResizable(false);
        tblDetailJual.getColumnModel().getColumn(0).setPreferredWidth(25);

        jButton35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/edit.png"))); // NOI18N
        jButton35.setText("Ubah");
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });

        btnCetakTransaksiJual.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/print.png"))); // NOI18N
        btnCetakTransaksiJual.setText("Cetak");
        btnCetakTransaksiJual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakTransaksiJualActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCetakTransaksiJual, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCetakTransaksiJual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jButton35)
                .addContainerGap())
        );

        jButton38.setText("Cari");
        jButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton38ActionPerformed(evt);
            }
        });

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Customer" }));

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Wilayah" }));

        javax.swing.GroupLayout frameRiwayatTransaksiJualLayout = new javax.swing.GroupLayout(frameRiwayatTransaksiJual.getContentPane());
        frameRiwayatTransaksiJual.getContentPane().setLayout(frameRiwayatTransaksiJualLayout);
        frameRiwayatTransaksiJualLayout.setHorizontalGroup(
            frameRiwayatTransaksiJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameRiwayatTransaksiJualLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameRiwayatTransaksiJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollTabelTransaksiJual)
                    .addComponent(lblJudul1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(frameRiwayatTransaksiJualLayout.createSequentialGroup()
                        .addComponent(jScrollPane14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frameRiwayatTransaksiJualLayout.createSequentialGroup()
                        .addComponent(fldTransaksiJualDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fldTransaksiJualDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton38)))
                .addContainerGap())
        );
        frameRiwayatTransaksiJualLayout.setVerticalGroup(
            frameRiwayatTransaksiJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameRiwayatTransaksiJualLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJudul1)
                .addGap(27, 27, 27)
                .addGroup(frameRiwayatTransaksiJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(frameRiwayatTransaksiJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(frameRiwayatTransaksiJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(fldTransaksiJualDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fldTransaksiJualDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton38)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollTabelTransaksiJual, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameRiwayatTransaksiJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        frameRekap.setClosable(true);
        frameRekap.setVisible(true);

        lblJudul2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblJudul2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudul2.setText("Rekap Data");

        jLabel3.setText("s/d");

        tblSisaItem.setAutoCreateRowSorter(true);
        tblSisaItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Kode", "Stock"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(tblSisaItem);

        jLabel5.setText("Total Pengeluaran");

        jLabel6.setText("Total Pemasukan");

        jLabel7.setText("Total");

        jLabel8.setText("=   Rp");

        jLabel9.setText("=   Rp");

        jLabel10.setText("=   Rp");

        lblTotalPengeluaran.setText("0");

        lblTotalPemasukan.setText("0");

        lblTotalSaldo.setText("0");

        btnRekap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/ok.png"))); // NOI18N
        btnRekap.setText("Ok");
        btnRekap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRekapActionPerformed(evt);
            }
        });

        btnCetakLabaRugi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/print.png"))); // NOI18N
        btnCetakLabaRugi.setText("Laba/Rugi");
        btnCetakLabaRugi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCetakLabaRugi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakLabaRugiActionPerformed(evt);
            }
        });

        btnSaveRekap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/save.png"))); // NOI18N
        btnSaveRekap.setText("Simpan");
        btnSaveRekap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveRekapActionPerformed(evt);
            }
        });

        btnCetakStockKardus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/print.png"))); // NOI18N
        btnCetakStockKardus.setText("Stock Kardus");
        btnCetakStockKardus.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnCetakStockKardus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakStockKardusActionPerformed(evt);
            }
        });

        jButton32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/print.png"))); // NOI18N
        jButton32.setText("Stock Apel");
        jButton32.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        jButton36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/print.png"))); // NOI18N
        jButton36.setText("Jual/Beli");
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout frameRekapLayout = new javax.swing.GroupLayout(frameRekap.getContentPane());
        frameRekap.getContentPane().setLayout(frameRekapLayout);
        frameRekapLayout.setHorizontalGroup(
            frameRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frameRekapLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRekap)
                .addGap(207, 207, 207))
            .addGroup(frameRekapLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblJudul2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane8)
                    .addGroup(frameRekapLayout.createSequentialGroup()
                        .addGroup(frameRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(frameRekapLayout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(fldDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fldDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(frameRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, frameRekapLayout.createSequentialGroup()
                                    .addGroup(frameRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(frameRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(frameRekapLayout.createSequentialGroup()
                                            .addComponent(jLabel9)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(lblTotalPemasukan, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(frameRekapLayout.createSequentialGroup()
                                            .addComponent(jLabel10)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(lblTotalSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, frameRekapLayout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lblTotalPengeluaran, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(frameRekapLayout.createSequentialGroup()
                        .addGroup(frameRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(frameRekapLayout.createSequentialGroup()
                                .addComponent(jButton36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(frameRekapLayout.createSequentialGroup()
                                .addComponent(btnCetakLabaRugi, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCetakStockKardus)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSaveRekap, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        frameRekapLayout.setVerticalGroup(
            frameRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameRekapLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJudul2)
                .addGap(28, 28, 28)
                .addGroup(frameRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(fldDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fldDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRekap)
                .addGap(30, 30, 30)
                .addGroup(frameRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8)
                    .addComponent(lblTotalPengeluaran))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(frameRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel6)
                    .addComponent(lblTotalPemasukan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(frameRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel7)
                    .addComponent(lblTotalSaldo))
                .addGap(32, 32, 32)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(frameRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCetakLabaRugi)
                    .addComponent(btnCetakStockKardus)
                    .addComponent(btnSaveRekap))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameRekapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton32)
                    .addComponent(jButton36))
                .addGap(18, 18, 18))
        );

        frameTambahStok.setClosable(true);
        frameTambahStok.setVisible(true);

        jdlTambahStok.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jdlTambahStok.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jdlTambahStok.setText("Tambah Stok Barang");

        lblItem.setForeground(new java.awt.Color(255, 0, 0));
        lblItem.setText("Item");

        fldItemTambahStok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fldItemTambahStokActionPerformed(evt);
            }
        });

        btnBatalTambahStok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/delete.png"))); // NOI18N
        btnBatalTambahStok.setText("Batal");
        btnBatalTambahStok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalTambahStokActionPerformed(evt);
            }
        });

        btnSimpanTambahStok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/save.png"))); // NOI18N
        btnSimpanTambahStok.setText("Simpan");
        btnSimpanTambahStok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanTambahStokActionPerformed(evt);
            }
        });

        lblStok.setText("Stok");

        fldStok.setEditable(false);
        fldStok.setText("0");

        fldJumlahTambah.setText("0");
        fldJumlahTambah.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fldJumlahTambahPropertyChange(evt);
            }
        });

        lblJumlahTambah.setText("Jumlah");

        lblTotalStok.setText("Total");

        fldTotalStok.setEditable(false);
        fldTotalStok.setText("0");

        lblStokCampur.setText("Sisa Stok");

        fldSisaStok.setEditable(false);
        fldSisaStok.setText("0");

        fldFromItemStok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fldFromItemStokActionPerformed(evt);
            }
        });

        lblItem1.setForeground(new java.awt.Color(255, 0, 0));
        lblItem1.setText("Item");

        javax.swing.GroupLayout frameTambahStokLayout = new javax.swing.GroupLayout(frameTambahStok.getContentPane());
        frameTambahStok.getContentPane().setLayout(frameTambahStokLayout);
        frameTambahStokLayout.setHorizontalGroup(
            frameTambahStokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameTambahStokLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameTambahStokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jdlTambahStok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(frameTambahStokLayout.createSequentialGroup()
                        .addGroup(frameTambahStokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(frameTambahStokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblItem, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblStok, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblJumlahTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblTotalStok, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblStokCampur, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(frameTambahStokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fldSisaStok)
                            .addComponent(fldTotalStok)
                            .addComponent(fldStok)
                            .addComponent(fldJumlahTambah)
                            .addGroup(frameTambahStokLayout.createSequentialGroup()
                                .addComponent(btnSimpanTambahStok)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnBatalTambahStok, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(fldItemTambahStok, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(frameTambahStokLayout.createSequentialGroup()
                        .addComponent(lblItem1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fldFromItemStok, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        frameTambahStokLayout.setVerticalGroup(
            frameTambahStokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameTambahStokLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jdlTambahStok)
                .addGap(18, 18, 18)
                .addGroup(frameTambahStokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblItem1)
                    .addComponent(fldFromItemStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameTambahStokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fldSisaStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStokCampur))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameTambahStokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblItem)
                    .addComponent(fldItemTambahStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameTambahStokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fldStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStok))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameTambahStokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fldJumlahTambah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblJumlahTambah))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameTambahStokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fldTotalStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalStok))
                .addGap(18, 18, 18)
                .addGroup(frameTambahStokLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpanTambahStok)
                    .addComponent(btnBatalTambahStok))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        frameRiwayatTransaksiBeli.setClosable(true);
        frameRiwayatTransaksiBeli.setVisible(true);

        lblJudul3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblJudul3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudul3.setText("Riwayat Transaksi");

        fldTransaksiBeliDateTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fldTransaksiBeliDateToActionPerformed(evt);
            }
        });

        jLabel11.setText("s/d");

        fldTransaksiBeliDateFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fldTransaksiBeliDateFromActionPerformed(evt);
            }
        });

        tblTransaksiBeli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tanggal", "Kode", "Tgl Bayar", "Petani", "Total Harga", "Total Bayar"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTransaksiBeli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTransaksiBeliMouseClicked(evt);
            }
        });
        scrollTabelTransaksiBeli.setViewportView(tblTransaksiBeli);

        tblDetailBeli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Item", "Qty", "Harga", "Total Harga", "Wilayah"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDetailBeli.setMaximumSize(new java.awt.Dimension(375, 0));
        tblDetailBeli.setName(""); // NOI18N
        jScrollPane13.setViewportView(tblDetailBeli);
        tblDetailBeli.getColumnModel().getColumn(0).setResizable(false);
        tblDetailBeli.getColumnModel().getColumn(0).setPreferredWidth(25);

        jButton27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/edit.png"))); // NOI18N
        jButton27.setText("Ubah");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        btnCetakTransaksiBeli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/print.png"))); // NOI18N
        btnCetakTransaksiBeli.setText("Cetak");
        btnCetakTransaksiBeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakTransaksiBeliActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCetakTransaksiBeli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCetakTransaksiBeli)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton27)
                .addGap(16, 16, 16))
        );

        jButton39.setText("Cari");
        jButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton39ActionPerformed(evt);
            }
        });

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Supplier" }));

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Wilayah" }));

        javax.swing.GroupLayout frameRiwayatTransaksiBeliLayout = new javax.swing.GroupLayout(frameRiwayatTransaksiBeli.getContentPane());
        frameRiwayatTransaksiBeli.getContentPane().setLayout(frameRiwayatTransaksiBeliLayout);
        frameRiwayatTransaksiBeliLayout.setHorizontalGroup(
            frameRiwayatTransaksiBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameRiwayatTransaksiBeliLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameRiwayatTransaksiBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblJudul3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scrollTabelTransaksiBeli)
                    .addGroup(frameRiwayatTransaksiBeliLayout.createSequentialGroup()
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 535, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(frameRiwayatTransaksiBeliLayout.createSequentialGroup()
                        .addComponent(fldTransaksiBeliDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fldTransaksiBeliDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton39)))
                .addContainerGap())
        );
        frameRiwayatTransaksiBeliLayout.setVerticalGroup(
            frameRiwayatTransaksiBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameRiwayatTransaksiBeliLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJudul3)
                .addGap(29, 29, 29)
                .addGroup(frameRiwayatTransaksiBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton39)
                    .addGroup(frameRiwayatTransaksiBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(fldTransaksiBeliDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fldTransaksiBeliDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollTabelTransaksiBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(frameRiwayatTransaksiBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        framePelunasanTransaksiBeli.setClosable(true);
        framePelunasanTransaksiBeli.setVisible(true);

        lblJudul4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblJudul4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudul4.setText("Pelunasan Pembelian");

        fldPelunasanBeliDateTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fldPelunasanBeliDateToActionPerformed(evt);
            }
        });

        jLabel12.setText("s/d");

        fldPelunasanBeliDateFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fldPelunasanBeliDateFromActionPerformed(evt);
            }
        });

        tblPelunasanBeli.setAutoCreateRowSorter(true);
        tblPelunasanBeli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tanggal", "Kode", "Tgl Bayar", "Petani", "Total Bayar"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPelunasanBeli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPelunasanBeliMouseClicked(evt);
            }
        });
        scrollTabelTransaksiBeli1.setViewportView(tblPelunasanBeli);

        btnPilihTransaksiBeli.setText("Lanjut");
        btnPilihTransaksiBeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihTransaksiBeliActionPerformed(evt);
            }
        });

        tblDetailPelunasanBeli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Qty", "Harga", "Total Harga", "Wilayah"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane12.setViewportView(tblDetailPelunasanBeli);

        javax.swing.GroupLayout framePelunasanTransaksiBeliLayout = new javax.swing.GroupLayout(framePelunasanTransaksiBeli.getContentPane());
        framePelunasanTransaksiBeli.getContentPane().setLayout(framePelunasanTransaksiBeliLayout);
        framePelunasanTransaksiBeliLayout.setHorizontalGroup(
            framePelunasanTransaksiBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(framePelunasanTransaksiBeliLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(framePelunasanTransaksiBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblJudul4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scrollTabelTransaksiBeli1)
                    .addGroup(framePelunasanTransaksiBeliLayout.createSequentialGroup()
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPilihTransaksiBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(framePelunasanTransaksiBeliLayout.createSequentialGroup()
                .addGap(201, 201, 201)
                .addComponent(fldPelunasanBeliDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fldPelunasanBeliDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        framePelunasanTransaksiBeliLayout.setVerticalGroup(
            framePelunasanTransaksiBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(framePelunasanTransaksiBeliLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJudul4)
                .addGap(18, 18, 18)
                .addGroup(framePelunasanTransaksiBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(fldPelunasanBeliDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fldPelunasanBeliDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addComponent(scrollTabelTransaksiBeli1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(framePelunasanTransaksiBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(framePelunasanTransaksiBeliLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(btnPilihTransaksiBeli)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, framePelunasanTransaksiBeliLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        framePelunasan.setClosable(true);
        framePelunasan.setVisible(true);

        jdlPelunasan.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jdlPelunasan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jdlPelunasan.setText("Pelunasan Pembelian");

        lblPelunasanNama.setText("Petani");

        fldPelunasanNama.setEditable(false);
        fldPelunasanNama.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fldPelunasanNamaPropertyChange(evt);
            }
        });

        lblPelunasanTotalHarga.setText("Total Harga");

        fldPelunasanTotalHarga.setEditable(false);
        fldPelunasanTotalHarga.setText("0");
        fldPelunasanTotalHarga.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fldPelunasanTotalHargaPropertyChange(evt);
            }
        });

        lblPelunasanBayar.setText("Jumlah Bayar");

        fldPelunasanBayar.setText("0");
        fldPelunasanBayar.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fldPelunasanBayarPropertyChange(evt);
            }
        });

        btnBatalPelunasan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/delete.png"))); // NOI18N
        btnBatalPelunasan.setText("Batal");
        btnBatalPelunasan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalPelunasanActionPerformed(evt);
            }
        });

        fldPelunasanTotalPembayaran.setEditable(false);
        fldPelunasanTotalPembayaran.setText("0");
        fldPelunasanTotalPembayaran.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fldPelunasanTotalPembayaranPropertyChange(evt);
            }
        });

        lblPelunasanTotalPembayaran.setText("Total Pembayaran");

        fldPelunasanSisaHutang.setEditable(false);
        fldPelunasanSisaHutang.setText("0");
        fldPelunasanSisaHutang.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fldPelunasanSisaHutangPropertyChange(evt);
            }
        });

        lblPelunasanSisaHutang.setText("Sisa Hutang");

        lblPelunasanNama1.setText("Tanggal");

        btnSimpanPelunasan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/save.png"))); // NOI18N
        btnSimpanPelunasan.setText("Simpan");
        btnSimpanPelunasan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanPelunasanActionPerformed(evt);
            }
        });

        tblPelunasan.setAutoCreateRowSorter(true);
        tblPelunasan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tgl Bayar", "Jumlah Bayar"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane9.setViewportView(tblPelunasan);

        lblPelunasanNota.setText("Nota");

        lblPelunasanFaktur.setText("Faktur");

        javax.swing.GroupLayout framePelunasanLayout = new javax.swing.GroupLayout(framePelunasan.getContentPane());
        framePelunasan.getContentPane().setLayout(framePelunasanLayout);
        framePelunasanLayout.setHorizontalGroup(
            framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jdlPelunasan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(framePelunasanLayout.createSequentialGroup()
                .addGroup(framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(framePelunasanLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, framePelunasanLayout.createSequentialGroup()
                                    .addComponent(lblPelunasanNama1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(258, 258, 258))
                                .addGroup(framePelunasanLayout.createSequentialGroup()
                                    .addComponent(lblPelunasanNota, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(20, 20, 20)))
                            .addGroup(framePelunasanLayout.createSequentialGroup()
                                .addComponent(lblPelunasanFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, framePelunasanLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, framePelunasanLayout.createSequentialGroup()
                                .addComponent(lblPelunasanTotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fldPelunasanTotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(framePelunasanLayout.createSequentialGroup()
                                .addComponent(lblPelunasanNama, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fldPelunasanNama, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(fldPelunasanFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fldPelunasanNota, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fldPelunasanDate, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, framePelunasanLayout.createSequentialGroup()
                                .addComponent(lblPelunasanBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(framePelunasanLayout.createSequentialGroup()
                                        .addComponent(btnSimpanPelunasan)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnBatalPelunasan, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(fldPelunasanBayar)))
                            .addComponent(fldPelunasanTransactionId, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addGroup(framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(framePelunasanLayout.createSequentialGroup()
                        .addGroup(framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(framePelunasanLayout.createSequentialGroup()
                                .addComponent(lblPelunasanSisaHutang, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(102, 102, 102))
                            .addGroup(framePelunasanLayout.createSequentialGroup()
                                .addComponent(lblPelunasanTotalPembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(64, 64, 64)))
                        .addGroup(framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(fldPelunasanTotalPembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fldPelunasanSisaHutang, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        framePelunasanLayout.setVerticalGroup(
            framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(framePelunasanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jdlPelunasan)
                .addGroup(framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(framePelunasanLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fldPelunasanTotalPembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPelunasanTotalPembayaran))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fldPelunasanSisaHutang, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPelunasanSisaHutang)))
                    .addGroup(framePelunasanLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fldPelunasanDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPelunasanNama1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPelunasanNota)
                            .addComponent(fldPelunasanNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPelunasanFaktur)
                            .addComponent(fldPelunasanFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPelunasanNama)
                            .addComponent(fldPelunasanNama, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPelunasanTotalHarga)
                            .addComponent(fldPelunasanTotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPelunasanBayar)
                            .addComponent(fldPelunasanBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(framePelunasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnBatalPelunasan)
                            .addComponent(btnSimpanPelunasan))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fldPelunasanTransactionId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        framePelunasanTransaksiJual.setClosable(true);
        framePelunasanTransaksiJual.setVisible(true);

        lblJudul5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblJudul5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudul5.setText("Pelunasan Penjualan");

        tblPelunasanJual.setAutoCreateRowSorter(true);
        tblPelunasanJual.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tanggal", "Kode", "Nota", "Customer", "No Fkt", "Total Harga"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPelunasanJual.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPelunasanJualMouseClicked(evt);
            }
        });
        scrollTabelTransaksiJual1.setViewportView(tblPelunasanJual);

        fldTransaksiJualDateTo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fldTransaksiJualDateTo1ActionPerformed(evt);
            }
        });

        jLabel14.setText("s/d");

        fldTransaksiJualDateFrom1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fldTransaksiJualDateFrom1ActionPerformed(evt);
            }
        });

        btnPilihTransaksiJual.setText("Lanjut");
        btnPilihTransaksiJual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPilihTransaksiJualActionPerformed(evt);
            }
        });

        tblDetailPelunasanJual.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Qty", "Harga", "Total Harga", "Wilayah"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane15.setViewportView(tblDetailPelunasanJual);

        javax.swing.GroupLayout framePelunasanTransaksiJualLayout = new javax.swing.GroupLayout(framePelunasanTransaksiJual.getContentPane());
        framePelunasanTransaksiJual.getContentPane().setLayout(framePelunasanTransaksiJualLayout);
        framePelunasanTransaksiJualLayout.setHorizontalGroup(
            framePelunasanTransaksiJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(framePelunasanTransaksiJualLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(framePelunasanTransaksiJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollTabelTransaksiJual1)
                    .addComponent(lblJudul5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, framePelunasanTransaksiJualLayout.createSequentialGroup()
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPilihTransaksiJual, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, framePelunasanTransaksiJualLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(fldTransaksiJualDateFrom1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fldTransaksiJualDateTo1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(194, 194, 194))
        );
        framePelunasanTransaksiJualLayout.setVerticalGroup(
            framePelunasanTransaksiJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(framePelunasanTransaksiJualLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJudul5)
                .addGap(20, 20, 20)
                .addGroup(framePelunasanTransaksiJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(fldTransaksiJualDateFrom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fldTransaksiJualDateTo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(scrollTabelTransaksiJual1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(framePelunasanTransaksiJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPilihTransaksiJual)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        frameJual.setClosable(true);
        frameJual.setVisible(true);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Wilayah", "Jenis", "Item", "Harga", "Qty", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane10.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setResizable(false);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(1);
        jTable1.getColumnModel().getColumn(4).setResizable(false);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(30);
        jTable1.getColumnModel().getColumn(5).setResizable(false);
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(30);
        jTable1.getColumnModel().getColumn(6).setResizable(false);
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(70);

        jLabel17.setText("Nota");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel18.setText("Faktur");

        jLabel19.setText("Customer");

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/add.png"))); // NOI18N
        jButton3.setText("Tambah");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel21.setText("Total Pembayaran");

        jTextField3.setEditable(false);
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField3.setText("0");

        lblJudul6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblJudul6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudul6.setText("Transaksi Penjualan");

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/calculator.png"))); // NOI18N
        jButton9.setText("Hitung");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel16.setText("Tanggal");

        jLabel33.setText("Kode");

        jTextField12.setEditable(false);

        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/remove.png"))); // NOI18N
        jButton22.setText("Hapus");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/delete.png"))); // NOI18N
        jButton2.setText("Batal");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/save.png"))); // NOI18N
        jButton1.setText("Simpan");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        javax.swing.GroupLayout frameJualLayout = new javax.swing.GroupLayout(frameJual.getContentPane());
        frameJual.getContentPane().setLayout(frameJualLayout);
        frameJualLayout.setHorizontalGroup(
            frameJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frameJualLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblJudul6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(frameJualLayout.createSequentialGroup()
                        .addGroup(frameJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, frameJualLayout.createSequentialGroup()
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, frameJualLayout.createSequentialGroup()
                                .addGroup(frameJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(frameJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTextField12)
                                    .addComponent(jXDatePicker2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, frameJualLayout.createSequentialGroup()
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane10))
                    .addGroup(frameJualLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(frameJualLayout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9)))
                .addContainerGap())
        );
        frameJualLayout.setVerticalGroup(
            frameJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameJualLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJudul6)
                .addGroup(frameJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(frameJualLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(frameJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3)
                            .addComponent(jButton9)
                            .addComponent(jButton22)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frameJualLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(frameJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33)
                            .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(frameJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(frameJualLayout.createSequentialGroup()
                        .addGroup(frameJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jXDatePicker2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(frameJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(frameJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(frameJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(frameJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        frameBeli.setClosable(true);
        frameBeli.setMaximumSize(new java.awt.Dimension(820, 461));
        frameBeli.setMinimumSize(new java.awt.Dimension(820, 461));
        frameBeli.setPreferredSize(new java.awt.Dimension(820, 461));
        frameBeli.setVisible(true);

        lblJudul7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblJudul7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudul7.setText("Transaksi Pembelian");

        jTable2.setAutoCreateRowSorter(true);
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Wilayah", "Item", "Harga", "Qty", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane11.setViewportView(jTable2);
        jTable2.getColumnModel().getColumn(0).setResizable(false);
        jTable2.getColumnModel().getColumn(0).setPreferredWidth(1);
        jTable2.getColumnModel().getColumn(3).setResizable(false);
        jTable2.getColumnModel().getColumn(3).setPreferredWidth(30);
        jTable2.getColumnModel().getColumn(4).setResizable(false);
        jTable2.getColumnModel().getColumn(4).setPreferredWidth(30);
        jTable2.getColumnModel().getColumn(5).setResizable(false);
        jTable2.getColumnModel().getColumn(5).setPreferredWidth(70);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/add.png"))); // NOI18N
        jButton4.setText("Tambah");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel26.setText("Petani");

        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jTextField9.setEditable(false);
        jTextField9.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField9.setText("0");

        jLabel27.setText("Total Pembayaran");

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/calculator.png"))); // NOI18N
        jButton7.setText("Hitung");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel15.setText("Tanggal");

        jLabel34.setText("Kode");

        jTextField13.setEditable(false);

        jButton26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/remove.png"))); // NOI18N
        jButton26.setText("Hapus");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/delete.png"))); // NOI18N
        jButton6.setText("Batal");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/save.png"))); // NOI18N
        jButton5.setText("Simpan");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton5)
                .addComponent(jButton6))
        );

        jLabel46.setText("Tebasan");

        jCheckBox1.setText(" ");
        jCheckBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBox1MouseClicked(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane19.setViewportView(jTextArea1);

        jLabel54.setText("Keterangan");

        javax.swing.GroupLayout frameBeliLayout = new javax.swing.GroupLayout(frameBeli.getContentPane());
        frameBeli.getContentPane().setLayout(frameBeliLayout);
        frameBeliLayout.setHorizontalGroup(
            frameBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameBeliLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(frameBeliLayout.createSequentialGroup()
                        .addComponent(lblJudul7, javax.swing.GroupLayout.PREFERRED_SIZE, 784, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frameBeliLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frameBeliLayout.createSequentialGroup()
                        .addGroup(frameBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(frameBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel46)
                            .addComponent(jLabel54))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, Short.MAX_VALUE)
                        .addGroup(frameBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox1)
                            .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(frameBeliLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(frameBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(46, 46, 46)
                        .addGroup(frameBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(frameBeliLayout.createSequentialGroup()
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton7))
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        frameBeliLayout.setVerticalGroup(
            frameBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameBeliLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJudul7)
                .addGap(33, 33, 33)
                .addGroup(frameBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(frameBeliLayout.createSequentialGroup()
                        .addGroup(frameBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4)
                            .addComponent(jButton7)
                            .addComponent(jButton26))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(frameBeliLayout.createSequentialGroup()
                        .addGroup(frameBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(frameBeliLayout.createSequentialGroup()
                                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(frameBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel26))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(frameBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jCheckBox1)
                                    .addComponent(jLabel46)))
                            .addGroup(frameBeliLayout.createSequentialGroup()
                                .addComponent(jLabel34)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel15)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(frameBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel54)
                            .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameBeliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        framePengiriman.setClosable(true);
        framePengiriman.setVisible(true);

        lblJudul8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblJudul8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudul8.setText("Biaya Pengiriman");

        jLabel20.setText("Tanggal");

        jLabel22.setText("Total Biaya");

        jTextField4.setText("0");

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tujuan"
            }
        ));
        jScrollPane16.setViewportView(jTable3);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/add.png"))); // NOI18N
        jButton8.setText("Tambah");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/delete.png"))); // NOI18N
        jButton10.setText("Batal");

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/save.png"))); // NOI18N
        jButton11.setText("Simpan");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout framePengirimanLayout = new javax.swing.GroupLayout(framePengiriman.getContentPane());
        framePengiriman.getContentPane().setLayout(framePengirimanLayout);
        framePengirimanLayout.setHorizontalGroup(
            framePengirimanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(framePengirimanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(framePengirimanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblJudul8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(framePengirimanLayout.createSequentialGroup()
                        .addGroup(framePengirimanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(jLabel20))
                        .addGap(18, 18, 18)
                        .addGroup(framePengirimanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField4)
                            .addComponent(fldDatePengiriman, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane16)
                    .addGroup(framePengirimanLayout.createSequentialGroup()
                        .addComponent(jButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        framePengirimanLayout.setVerticalGroup(
            framePengirimanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(framePengirimanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJudul8)
                .addGap(18, 18, 18)
                .addGroup(framePengirimanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(fldDatePengiriman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(framePengirimanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(framePengirimanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton8)
                    .addComponent(jButton10)
                    .addComponent(jButton11))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        frameGaji.setClosable(true);
        frameGaji.setVisible(true);

        lblJudul9.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblJudul9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudul9.setText("Gaji Karyawan");

        jLabel23.setText("Tanggal");

        tblGaji.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nama", "Gaji"
            }
        ));
        jScrollPane17.setViewportView(tblGaji);

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/add.png"))); // NOI18N
        jButton12.setText("Tambah");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/delete.png"))); // NOI18N
        jButton13.setText("Batal");

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/save.png"))); // NOI18N
        jButton14.setText("Simpan");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        fldDateGaji.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fldDateGajiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout frameGajiLayout = new javax.swing.GroupLayout(frameGaji.getContentPane());
        frameGaji.getContentPane().setLayout(frameGajiLayout);
        frameGajiLayout.setHorizontalGroup(
            frameGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameGajiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblJudul9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(frameGajiLayout.createSequentialGroup()
                        .addGroup(frameGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(frameGajiLayout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addGap(18, 18, 18)
                                .addComponent(fldDateGaji, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(frameGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(frameGajiLayout.createSequentialGroup()
                                    .addComponent(jButton12)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton14)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        frameGajiLayout.setVerticalGroup(
            frameGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameGajiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJudul9)
                .addGap(23, 23, 23)
                .addGroup(frameGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fldDateGaji, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(frameGajiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton12)
                    .addComponent(jButton13)
                    .addComponent(jButton14))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        frameTransaksiLain.setClosable(true);
        frameTransaksiLain.setVisible(true);

        lblJudul11.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblJudul11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudul11.setText("Biaya Packing");

        jLabel31.setText("Tanggal");

        fldTransaksiJualDateFrom4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fldTransaksiJualDateFrom4ActionPerformed(evt);
            }
        });

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Barang", "Qty", "Harga", "Total Harga"
            }
        ));
        jScrollPane18.setViewportView(jTable5);

        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/remove.png"))); // NOI18N
        jButton17.setText("Hapus");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/save.png"))); // NOI18N
        jButton18.setText("Simpan");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/add.png"))); // NOI18N
        jButton19.setText("Tambah");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        fldTotalBiaya.setEditable(false);
        fldTotalBiaya.setText("0");

        jLabel13.setText("Total");

        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/calculator.png"))); // NOI18N
        jButton20.setText("Hitung");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout frameTransaksiLainLayout = new javax.swing.GroupLayout(frameTransaksiLain.getContentPane());
        frameTransaksiLain.getContentPane().setLayout(frameTransaksiLainLayout);
        frameTransaksiLainLayout.setHorizontalGroup(
            frameTransaksiLainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameTransaksiLainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameTransaksiLainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(frameTransaksiLainLayout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addGap(18, 18, 18)
                        .addComponent(fldTransaksiJualDateFrom4, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(frameTransaksiLainLayout.createSequentialGroup()
                        .addGroup(frameTransaksiLainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(frameTransaksiLainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(107, 107, 107)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(fldTotalBiaya, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblJudul11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane18, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        frameTransaksiLainLayout.setVerticalGroup(
            frameTransaksiLainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameTransaksiLainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJudul11)
                .addGap(27, 27, 27)
                .addGroup(frameTransaksiLainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(fldTransaksiJualDateFrom4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameTransaksiLainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(frameTransaksiLainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(fldTotalBiaya, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13))
                    .addGroup(frameTransaksiLainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton19)
                        .addComponent(jButton20)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(frameTransaksiLainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton18)
                    .addComponent(jButton17))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        frameKomisi.setClosable(true);
        frameKomisi.setVisible(true);

        lblJudul10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblJudul10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudul10.setText("Komisi");

        jLabel25.setText("Tanggal");

        jLabel28.setText("Nama");

        jLabel29.setText("Jumlah");

        fldTransaksiJualDateFrom3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fldTransaksiJualDateFrom3ActionPerformed(evt);
            }
        });

        jTextField7.setText("0");

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/delete.png"))); // NOI18N
        jButton15.setText("Batal");

        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/save.png"))); // NOI18N
        jButton16.setText("Simpan");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jLabel30.setText("Wilayah");

        javax.swing.GroupLayout frameKomisiLayout = new javax.swing.GroupLayout(frameKomisi.getContentPane());
        frameKomisi.getContentPane().setLayout(frameKomisiLayout);
        frameKomisiLayout.setHorizontalGroup(
            frameKomisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameKomisiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameKomisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblJudul10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(frameKomisiLayout.createSequentialGroup()
                        .addGroup(frameKomisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(frameKomisiLayout.createSequentialGroup()
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField6))
                            .addGroup(frameKomisiLayout.createSequentialGroup()
                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(fldTransaksiJualDateFrom3, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(frameKomisiLayout.createSequentialGroup()
                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField7))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frameKomisiLayout.createSequentialGroup()
                                .addComponent(jButton16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(frameKomisiLayout.createSequentialGroup()
                                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        frameKomisiLayout.setVerticalGroup(
            frameKomisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameKomisiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJudul10)
                .addGap(18, 18, 18)
                .addGroup(frameKomisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(fldTransaksiJualDateFrom3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameKomisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameKomisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(frameKomisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addGap(18, 18, 18)
                .addGroup(frameKomisiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton15)
                    .addComponent(jButton16))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        frameHargaTransaksi.setClosable(true);
        frameHargaTransaksi.setVisible(true);

        jLabel37.setText("Nota");

        jLabel38.setText("Tanggal");

        jLabel44.setText("Customer");

        jTextField16.setEditable(false);

        jTextField17.setEditable(false);

        jTextField23.setEditable(false);

        jLabel53.setText("Kode");

        jTextField31.setEditable(false);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel11Layout.createSequentialGroup()
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(10, 10, 10)
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextField17)
                                .addComponent(jTextField23))))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(103, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44))
                .addGap(37, 37, 37))
        );

        jLabel48.setText("Tanggal");

        jLabel49.setText("Supplier");

        jTextField26.setEditable(false);

        jTextField27.setEditable(false);

        jTextField30.setEditable(false);

        jLabel52.setText("Kode");

        jLabel50.setText("Tebasan");

        jCheckBox2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBox2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(10, 10, 10)
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField26, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                                .addComponent(jTextField27)))
                        .addGroup(jPanel12Layout.createSequentialGroup()
                            .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(10, 10, 10)
                            .addComponent(jTextField30)))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel50)
                    .addComponent(jCheckBox2))
                .addContainerGap())
        );

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Item", "Qty", "Harga", "Jumlah", "Wilayah"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.setName("ubah"); // NOI18N
        jScrollPane5.setViewportView(jTable4);

        jButton40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/calculator.png"))); // NOI18N
        jButton40.setText("Hitung");
        jButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton40ActionPerformed(evt);
            }
        });

        jButton42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/save.png"))); // NOI18N
        jButton42.setText("Simpan");
        jButton42.setEnabled(false);
        jButton42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton42ActionPerformed(evt);
            }
        });

        jButton41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/delete.png"))); // NOI18N
        jButton41.setText("Batal");
        jButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton41ActionPerformed(evt);
            }
        });

        jButton28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/recyclebin_full_1.png"))); // NOI18N
        jButton28.setText("Hapus");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton40)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton42)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton41)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextField25.setEditable(false);

        jLabel47.setText("Total");

        jLabel51.setText("Total Pasar");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(252, 252, 252)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblJudul12.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblJudul12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudul12.setText("Pembelian");

        javax.swing.GroupLayout frameHargaTransaksiLayout = new javax.swing.GroupLayout(frameHargaTransaksi.getContentPane());
        frameHargaTransaksi.getContentPane().setLayout(frameHargaTransaksiLayout);
        frameHargaTransaksiLayout.setHorizontalGroup(
            frameHargaTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameHargaTransaksiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameHargaTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(frameHargaTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblJudul12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(frameHargaTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        frameHargaTransaksiLayout.setVerticalGroup(
            frameHargaTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frameHargaTransaksiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJudul12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jInternalFrame1.setClosable(true);
        jInternalFrame1.setAutoscrolls(true);
        jInternalFrame1.setVisible(true);

        lblJudul13.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblJudul13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudul13.setText("Cari Biaya Packing");

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tangal", "Total", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane20.setViewportView(jTable6);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblJudul13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane20))
                .addContainerGap())
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJudul13)
                .addGap(73, 73, 73)
                .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        listMenu.setPreferredSize(new java.awt.Dimension(373, 25));

        menuPembelian.setText("Supplier");

        menuCariSupplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/search.png"))); // NOI18N
        menuCariSupplier.setText("Cari Data");
        menuCariSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCariSupplierActionPerformed(evt);
            }
        });
        menuPembelian.add(menuCariSupplier);

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/user.png"))); // NOI18N
        jMenu4.setText("Tambah");

        menuTambahPemasok.setText("Supplier");
        menuTambahPemasok.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuTambahPemasokMouseClicked(evt);
            }
        });
        menuTambahPemasok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTambahPemasokActionPerformed(evt);
            }
        });
        jMenu4.add(menuTambahPemasok);

        menuPembelian.add(jMenu4);

        listMenu.add(menuPembelian);

        menuPenjualan.setText("Customer");

        menuCariCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/search.png"))); // NOI18N
        menuCariCustomer.setText("Cari Data");
        menuCariCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCariCustomerActionPerformed(evt);
            }
        });
        menuPenjualan.add(menuCariCustomer);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/user.png"))); // NOI18N
        jMenu2.setText("Tambah");

        menuTambahPelanggan.setText("Customer");
        menuTambahPelanggan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuTambahPelangganMouseClicked(evt);
            }
        });
        menuTambahPelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTambahPelangganActionPerformed(evt);
            }
        });
        jMenu2.add(menuTambahPelanggan);

        menuPenjualan.add(jMenu2);

        listMenu.add(menuPenjualan);

        menuRekap.setText("Rekap");
        menuRekap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuRekapMouseClicked(evt);
            }
        });
        menuRekap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRekapActionPerformed(evt);
            }
        });
        listMenu.add(menuRekap);

        menuInventori.setText("Stok Barang");
        menuInventori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuInventoriActionPerformed(evt);
            }
        });

        menuCariKategori.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/search.png"))); // NOI18N
        menuCariKategori.setText("Cari Kategori");
        menuCariKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCariKategoriActionPerformed(evt);
            }
        });
        menuInventori.add(menuCariKategori);

        menuStokBarang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/view.png"))); // NOI18N
        menuStokBarang.setText("Lihat Stok Barang");
        menuStokBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuStokBarangActionPerformed(evt);
            }
        });
        menuInventori.add(menuStokBarang);

        menuTambahKategori.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/new.png"))); // NOI18N
        menuTambahKategori.setText("Tambah Kategori");
        menuTambahKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTambahKategoriActionPerformed(evt);
            }
        });
        menuInventori.add(menuTambahKategori);

        menTambahStok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/add.png"))); // NOI18N
        menTambahStok.setText("Tambah Stok Barang");
        menTambahStok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menTambahStokActionPerformed(evt);
            }
        });
        menuInventori.add(menTambahStok);

        listMenu.add(menuInventori);

        menuRiwayatTransaksi.setText("Transaksi");
        menuRiwayatTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRiwayatTransaksiActionPerformed(evt);
            }
        });

        jMenu3.setText("Beli");

        menuBeli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/new.png"))); // NOI18N
        menuBeli.setText("Tambah Data");
        menuBeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBeliActionPerformed(evt);
            }
        });
        jMenu3.add(menuBeli);

        menuCariPembelian.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/search.png"))); // NOI18N
        menuCariPembelian.setText("Cari Data");
        menuCariPembelian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCariPembelianActionPerformed(evt);
            }
        });
        jMenu3.add(menuCariPembelian);

        menuPelunasanBeli.setText("Pelunasan");
        menuPelunasanBeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPelunasanBeliActionPerformed(evt);
            }
        });
        jMenu3.add(menuPelunasanBeli);

        menuRiwayatTransaksi.add(jMenu3);

        jMenu5.setText("Jual");

        menuJual.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_J, java.awt.event.InputEvent.CTRL_MASK));
        menuJual.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/new.png"))); // NOI18N
        menuJual.setText("Tambah Data");
        menuJual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuJualActionPerformed(evt);
            }
        });
        jMenu5.add(menuJual);

        menuCariPenjualan.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        menuCariPenjualan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/search.png"))); // NOI18N
        menuCariPenjualan.setText("Cari Data");
        menuCariPenjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCariPenjualanActionPerformed(evt);
            }
        });
        jMenu5.add(menuCariPenjualan);

        menuPelunasanJual.setText("Pelunasan");
        menuPelunasanJual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPelunasanJualActionPerformed(evt);
            }
        });
        jMenu5.add(menuPelunasanJual);

        menuRiwayatTransaksi.add(jMenu5);

        jMenu1.setText("Gaji");

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/new.png"))); // NOI18N
        jMenuItem1.setText("Tambah Baru");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/search.png"))); // NOI18N
        jMenuItem2.setText("Cari");
        jMenu1.add(jMenuItem2);

        menuRiwayatTransaksi.add(jMenu1);

        jMenu6.setText("Komisi");

        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/new.png"))); // NOI18N
        jMenuItem7.setText("Tambah Baru");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem7);

        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/search.png"))); // NOI18N
        jMenuItem8.setText("Cari");
        jMenu6.add(jMenuItem8);

        menuRiwayatTransaksi.add(jMenu6);

        jMenu7.setText("Lain - Lain");

        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/new.png"))); // NOI18N
        jMenuItem5.setText("Tambah Baru");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem5);

        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/search.png"))); // NOI18N
        jMenuItem6.setText("Cari");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem6);

        menuRiwayatTransaksi.add(jMenu7);

        jMenu8.setText("Pengiriman");

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/new.png"))); // NOI18N
        jMenuItem3.setText("Tambah Baru");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem3);

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/search.png"))); // NOI18N
        jMenuItem4.setText("Cari");
        jMenu8.add(jMenuItem4);

        menuRiwayatTransaksi.add(jMenu8);

        listMenu.add(menuRiwayatTransaksi);

        menuWilayah.setText("Wilayah");

        menuCariWilayah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/search.png"))); // NOI18N
        menuCariWilayah.setText("Cari Wilayah");
        menuCariWilayah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCariWilayahActionPerformed(evt);
            }
        });
        menuWilayah.add(menuCariWilayah);

        menuTambahWilayah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/new.png"))); // NOI18N
        menuTambahWilayah.setText("Tambah Wilayah");
        menuTambahWilayah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuTambahWilayahActionPerformed(evt);
            }
        });
        menuWilayah.add(menuTambahWilayah);

        listMenu.add(menuWilayah);

        menuKeluar.setText("Tutup");
        menuKeluar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuKeluarMouseClicked(evt);
            }
        });
        listMenu.add(menuKeluar);

        setJMenuBar(listMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(framekategori, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(frameTambahPemasokPelanggan)
                                    .addComponent(frameTambahKategori))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(frameCari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(frameWilayah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(frameCariWilayah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(frameStokBarang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(framePelunasan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(frameRekap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(frameTambahStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(frameJual, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(frameBeli, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(framePengiriman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(15, 15, 15))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(frameKomisi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(frameGaji, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(frameTransaksiLain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(framePelunasanTransaksiBeli, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(frameRiwayatTransaksiBeli, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(frameRiwayatTransaksiJual, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(framePelunasanTransaksiJual, javax.swing.GroupLayout.Alignment.LEADING))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(frameHargaTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(frameTambahPemasokPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(frameTambahKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(framekategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(frameCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(frameStokBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(frameWilayah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frameCariWilayah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frameRiwayatTransaksiJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(framePelunasanTransaksiJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frameRiwayatTransaksiBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(framePelunasanTransaksiBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(frameTambahStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frameRekap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(framePelunasan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(frameJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frameBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(framePengiriman, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frameKomisi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(frameGaji, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frameTransaksiLain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frameHargaTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        try {
            frameCari.setMaximum(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        try {
            frameStokBarang.setMaximum(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        try {
            frameBeli.setMaximum(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuKeluarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuKeluarMouseClicked
        // TODO add your handling code here:
        System.exit(DISPOSE_ON_CLOSE);
    }//GEN-LAST:event_menuKeluarMouseClicked

    private void menuTambahPemasokMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuTambahPemasokMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_menuTambahPemasokMouseClicked

    private void menuTambahPelangganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuTambahPelangganMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_menuTambahPelangganMouseClicked

    private void menuTambahPemasokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTambahPemasokActionPerformed
        // TODO add your handling code here:
        count = rowCount("supplier_customer");
        jTextField10.setText("SC" + (count + 1));
        lblJudul.setText("Tambah Supplier");
        lblName.setText("Nama Supplier");
        resetField(1);
        setRegions(fldWilayah);
        frameTambahPemasokPelanggan.setVisible(true);
    }//GEN-LAST:event_menuTambahPemasokActionPerformed

    private void menuTambahPelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTambahPelangganActionPerformed
        // TODO add your handling code here:
        count = rowCount("supplier_customer");
        jTextField10.setText("SC" + (count + 1));
        lblJudul.setText("Tambah Customer");
        lblName.setText("Nama Customer");
        resetField(1);
        setRegions(fldWilayah);
        frameTambahPemasokPelanggan.setVisible(true);
    }//GEN-LAST:event_menuTambahPelangganActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        // TODO add your handling code here:
        frameTambahPemasokPelanggan.setVisible(false);
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        fieldValidation(1);
        SupplierCustomer supplierCustomer = new SupplierCustomer();
        supplierCustomer.setCode(jTextField10.getText().replace("SC", ""));
        supplierCustomer.setName(fldName.getText());
        supplierCustomer.setPhone(fldTelp.getText().equalsIgnoreCase("") ? 0 : Integer.parseInt(fldTelp.getText()));
        supplierCustomer.setRegionId(getRegionByName(fldWilayah.getSelectedItem().toString()));
        supplierCustomer.setAddress(fldAlamat.getText());
        supplierCustomer.setDescription(fldKeterangan.getText());
        supplierCustomer.setPrice(0);
        supplierCustomer.setSupplierCustomerType(lblName.getText().equalsIgnoreCase("Nama Supplier")
                ? SupplierCustomer.SupplierCustomerType.SUPPLIER
                : SupplierCustomer.SupplierCustomerType.CUSTOMER);
        supplierCustomer.getLog().setCreateDate(new Date(System.currentTimeMillis()));
        supplierCustomer.getLog().setUpdateDate(new Date(System.currentTimeMillis()));
        boolean success = saveSupplierCustomer(supplierCustomer);
        if (success) {
            JOptionPane.showMessageDialog(null, "Data Telah Tersimpan");
            frameTambahPemasokPelanggan.setVisible(false);
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void fldCariFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fldCariFocusGained
        // TODO add your handling code here:
        fldCari.setText(null);
    }//GEN-LAST:event_fldCariFocusGained

    private void fldCariFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fldCariFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_fldCariFocusLost

    private void menuCariSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCariSupplierActionPerformed
        // TODO add your handling code here:
        resetField(1);
        jdlCari.setText("Supplier");
        fldCari.setText("Cari Supplier");
        findSupplierOrCustomer(SupplierCustomer.SupplierCustomerType.SUPPLIER, "");
        frameCari.setVisible(true);
    }//GEN-LAST:event_menuCariSupplierActionPerformed

    private void menuCariCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCariCustomerActionPerformed
        // TODO add your handling code here:
        resetField(1);
        jdlCari.setText("Customer");
        fldCari.setText("Cari Customer");
        findSupplierOrCustomer(SupplierCustomer.SupplierCustomerType.CUSTOMER, "");
        frameCari.setVisible(true);
    }//GEN-LAST:event_menuCariCustomerActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
        if (jdlCari.getText().equalsIgnoreCase("Supplier")) {
            findSupplierOrCustomer(SupplierCustomer.SupplierCustomerType.SUPPLIER, fldCari.getText());
        } else {
            findSupplierOrCustomer(SupplierCustomer.SupplierCustomerType.CUSTOMER, fldCari.getText());
        }
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnCariStokBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariStokBarangActionPerformed
        // TODO add your handling code here:
        findInventories(fldCariStokBarang.getText());
    }//GEN-LAST:event_btnCariStokBarangActionPerformed

    private void fldCariStokBarangFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fldCariStokBarangFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_fldCariStokBarangFocusGained

    private void fldCariStokBarangFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fldCariStokBarangFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_fldCariStokBarangFocusLost

    private void btnSimpanKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanKategoriActionPerformed
        // TODO add your handling code here:
        fieldValidation(2);
        InventoryCategory category = new InventoryCategory();
        category.setName(fldNamaKategori.getText());
        category.setItemCode(fldKodeBarang.getText());
        category.setTotalItems(0);
        category.getLog().setCreateDate(new Date(System.currentTimeMillis()));
        category.getLog().setUpdateDate(new Date(System.currentTimeMillis()));
        boolean success = saveCategory(category);
        if (success) {
            JOptionPane.showMessageDialog(null, "Data Telah Tersimpan");
            frameTambahKategori.setVisible(false);
        }
    }//GEN-LAST:event_btnSimpanKategoriActionPerformed

    private void btnBatalKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalKategoriActionPerformed
        // TODO add your handling code here:
        frameTambahKategori.setVisible(false);
    }//GEN-LAST:event_btnBatalKategoriActionPerformed

    private void menuTambahKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTambahKategoriActionPerformed
        resetField(2);
        frameTambahKategori.setVisible(true);
    }//GEN-LAST:event_menuTambahKategoriActionPerformed

    private void menuCariKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCariKategoriActionPerformed
        // TODO add your handling code here:
        resetField(2);
        findCategories("");
        framekategori.setVisible(true);
    }//GEN-LAST:event_menuCariKategoriActionPerformed

    private void menuInventoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuInventoriActionPerformed
        // TODO add your handling code here:
        findInventories("");
    }//GEN-LAST:event_menuInventoriActionPerformed

    private void menuBeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBeliActionPerformed
        // TODO add your handling code here:
//        resetField(5);
        count = rowCount("transaction");
        jTextField13.setText("T" + (count + 1));
        jXDatePicker1.setDate(new Date(System.currentTimeMillis()));
        setSuppliersCustomers(SupplierCustomer.SupplierCustomerType.SUPPLIER, jComboBox3);
        setCategories(jTable2, 2);
        frameBeli.setVisible(true);
    }//GEN-LAST:event_menuBeliActionPerformed

    private void menuJualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuJualActionPerformed
        // TODO add your handling code here:
        count = rowCount("transaction");
        jTextField12.setText("T" + (count + 1));
        jXDatePicker2.setDate(new Date(System.currentTimeMillis()));
        setSuppliersCustomers(SupplierCustomer.SupplierCustomerType.CUSTOMER, jComboBox1);
        setCategories(jTable1, 2);
        frameJual.setVisible(true);
    }//GEN-LAST:event_menuJualActionPerformed

    private void btnCariKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariKategoriActionPerformed
        // TODO add your handling code here:
        findCategories(fldCariKategori.getText());
    }//GEN-LAST:event_btnCariKategoriActionPerformed

    private void fldCariKategoriFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fldCariKategoriFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_fldCariKategoriFocusGained

    private void fldCariKategoriFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fldCariKategoriFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_fldCariKategoriFocusLost

    private void menuStokBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuStokBarangActionPerformed
        // TODO add your handling code here:
        resetField(3);
        findInventories("");
        frameStokBarang.setVisible(true);
    }//GEN-LAST:event_menuStokBarangActionPerformed

    private void btnSimpanWilayahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanWilayahActionPerformed
        // TODO add your handling code here:
        fieldValidation(3);
        Region region = new Region();
        region.setCode(jTextField11.getText().replace("R", ""));
        region.setName(fldWilayahBaru.getText());
        region.getLog().setCreateDate(new Date(System.currentTimeMillis()));
        region.getLog().setUpdateDate(new Date(System.currentTimeMillis()));
        boolean success = saveRegion(region);
        if (success) {
            JOptionPane.showMessageDialog(null, "Data Telah Tersimpan");
            frameWilayah.setVisible(false);
        }
    }//GEN-LAST:event_btnSimpanWilayahActionPerformed

    private void btnBatalWilayahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalWilayahActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBatalWilayahActionPerformed

    private void fldCariWilayahFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fldCariWilayahFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_fldCariWilayahFocusGained

    private void fldCariWilayahFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fldCariWilayahFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_fldCariWilayahFocusLost

    private void btnCariWilayahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariWilayahActionPerformed
        // TODO add your handling code here:
        setRegions(fldCariWilayah.getText());
    }//GEN-LAST:event_btnCariWilayahActionPerformed

    private void menuTambahWilayahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuTambahWilayahActionPerformed
        // TODO add your handling code here:
        resetField(4);
        count = rowCount("region");
        jTextField11.setText("R" + (count + 1));
        frameWilayah.setVisible(true);
    }//GEN-LAST:event_menuTambahWilayahActionPerformed

    private void menuCariWilayahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCariWilayahActionPerformed
        // TODO add your handling code here:
        resetField(4);
        setRegions("");
        frameCariWilayah.setVisible(true);
    }//GEN-LAST:event_menuCariWilayahActionPerformed

    private void menuRiwayatTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRiwayatTransaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuRiwayatTransaksiActionPerformed

    private void menTambahStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menTambahStokActionPerformed
        // TODO add your handling code here:
        resetField(3);
        setCategories(fldFromItemStok);
        fldFromItemStok.removeItem("");
        frameTambahStok.setVisible(true);
    }//GEN-LAST:event_menTambahStokActionPerformed

    private void btnBatalTambahStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalTambahStokActionPerformed
        // TODO add your handling code here:
        frameTambahStok.setVisible(false);
    }//GEN-LAST:event_btnBatalTambahStokActionPerformed

    private void btnSimpanTambahStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanTambahStokActionPerformed
        // TODO add your handling code here:
        InventoryCategory ic = findCategoryIdByName(fldItemTambahStok.getSelectedItem().toString());
        int stokSisa = Integer.parseInt(fldSisaStok.getText());
        int jumlahTambah = fldJumlahTambah.getText().equalsIgnoreCase("") ? 0
                : Integer.parseInt(fldJumlahTambah.getText());
        stokSisa = stokSisa - jumlahTambah;
        updateInventoryById(ic.getId(), Integer.parseInt(fldTotalStok.getText()));

        Transaction transaction = new Transaction();
        transaction.setQuantity(jumlahTambah);
        transaction.setTotalPrice(0);
        transaction.setPrice(0);
        transaction.getLog().setCreateDate(new Date(System.currentTimeMillis()));
        transaction.getLog().setUpdateDate(new Date(System.currentTimeMillis()));
        transaction.setPayment(0);
        transaction.setInventoryCategoryId(ic.getId());
        transaction.setTransactionType(Transaction.TransactionType.STOCK);
        transaction.setPaymentStatus(Transaction.PaymentStatus.FULL);

        String success = saveTransaksi(transaction);
        transaction.setId(success);
        saveTransaksiDetail(transaction);
        if (!success.equalsIgnoreCase("")) {
            ic = findCategoryIdByName((String) fldFromItemStok.getSelectedItem());
            updateInventoryById(ic.getId(), (ic.getTotalItems() + transaction.getQuantity()));
            success = updateInventoryById(ic.getId(), stokSisa);
            if (!success.equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(null, "Data Telah Tersimpan");
                frameTambahStok.setVisible(false);
            }
        }
    }//GEN-LAST:event_btnSimpanTambahStokActionPerformed

    private void fldItemTambahStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fldItemTambahStokActionPerformed
        // TODO add your handling code here:
        if (fldItemTambahStok.getItemCount() != 0) {
            int stok = getStokItemByName(fldItemTambahStok.getSelectedItem().toString());
            fldStok.setText(String.valueOf(stok));
            stok = stok + (fldJumlahTambah.getText().equalsIgnoreCase("") ? 0 : Integer.parseInt(fldJumlahTambah.getText()));
            fldTotalStok.setText(String.valueOf(stok));
        }
    }//GEN-LAST:event_fldItemTambahStokActionPerformed

    private void fldJumlahTambahPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fldJumlahTambahPropertyChange
        // TODO add your handling code here:
        fldJumlahTambah.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                // ketika teks dimasukkan
                fldTotalStok.setText("" + ((fldStok.getText().equalsIgnoreCase("") ? 0 : Integer.parseInt(fldStok.getText()))
                        + (fldJumlahTambah.getText().equalsIgnoreCase("") ? 0 : Integer.parseInt(fldJumlahTambah.getText()))));
            }

            public void removeUpdate(DocumentEvent e) {
                // ketika teks diubah
                fldTotalStok.setText("" + ((fldStok.getText().equalsIgnoreCase("") ? 0 : Integer.parseInt(fldStok.getText()))
                        + (fldJumlahTambah.getText().equalsIgnoreCase("") ? 0 : Integer.parseInt(fldJumlahTambah.getText()))));
            }

            public void changedUpdate(DocumentEvent e) {
                // ketika teks diubah
                fldTotalStok.setText("" + ((fldStok.getText().equalsIgnoreCase("") ? 0 : Integer.parseInt(fldStok.getText()))
                        + (fldJumlahTambah.getText().equalsIgnoreCase("") ? 0 : Integer.parseInt(fldJumlahTambah.getText()))));
            }
        });
    }//GEN-LAST:event_fldJumlahTambahPropertyChange

    private void btnRekapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRekapActionPerformed
        try {
            Font font = new Font("Tahoma", Font.BOLD, 11);
            // TODO add your handling code here:
            if (fldDateFrom.getDate().compareTo(fldDateTo.getDate()) < 1) {
                String date = df.format(fldDateFrom.getDate());
                Date dateFrom = df.parse(date);
                date = df.format(fldDateTo.getDate());
                Date dateTo = df.parse(date);

                findAllCategorieStock(dateFrom, dateTo);

                long totalPengeluaran = getTotalPengeluaranPemasukan(dateFrom, dateTo, Transaction.TransactionType.BUY.ordinal());
                totalPengeluaran = totalPengeluaran + getTotalGaji(dateFrom, dateTo) + getTotalKirim(dateFrom, dateTo)
                        + getTotalKomisi(dateFrom, dateTo) + getTotalTransaksiLain(dateFrom, dateTo);
                lblTotalPengeluaran.setText(nf.format(totalPengeluaran));

                long totalPemasukan = getTotalPengeluaranPemasukan(dateFrom, dateTo, Transaction.TransactionType.SELL.ordinal());
                lblTotalPemasukan.setText(nf.format(totalPemasukan));
                long total = totalPemasukan - totalPengeluaran;
//                DateFormat dateFormat = new SimpleDateFormat("yy");
//                String tgl = "01-Jan-"+dateFormat.format(dateTo);
//                dateFrom = new Date(df.parse(tgl).getTime());
//                dateTo = new Date(dateTo.getTime() - 7 * 24 * 60 * 60 * 1000);
//                totalPengeluaran = getTotalPengeluaranPemasukan(dateFrom, dateTo, Transaction.TransactionType.BUY.ordinal());
//                totalPemasukan = getTotalPengeluaranPemasukan(dateFrom, dateTo, Transaction.TransactionType.SELL.ordinal());
//                long totalSaldoAwal = totalPemasukan - totalPengeluaran;
//                lblSaldoAwal.setFont(font);
//                if (totalSaldoAwal < 0) {
//                    lblSaldoAwal.setForeground(Color.RED);
//                } else if (totalSaldoAwal > 0) {
//                    lblSaldoAwal.setForeground(Color.GREEN);
//                }
//                lblSaldoAwal.setText(nf.format(totalSaldoAwal));
//
                long totalSaldo = total;
                lblTotalSaldo.setFont(font);
                if (totalSaldo < 0) {
                    lblTotalSaldo.setForeground(Color.RED);
                } else if (totalSaldo > 0) {
                    lblTotalSaldo.setForeground(Color.GREEN);
                }
                lblTotalSaldo.setText(nf.format(totalSaldo));
            }
            btnSaveRekap.setVisible(true);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnRekapActionPerformed

    private void menuRekapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRekapActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuRekapActionPerformed

    private void menuRekapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuRekapMouseClicked
        // TODO add your handling code here:
        btnSaveRekap.setVisible(true);
        frameRekap.setVisible(true);
    }//GEN-LAST:event_menuRekapMouseClicked

    private void btnCetakTransaksiJualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakTransaksiJualActionPerformed
        String name = JOptionPane.showInputDialog("Isi Nama File", JOptionPane.YES_OPTION);
        btnCetakTransaksiJual.disable();
        printTransactionJual(Transaction.TransactionType.SELL.ordinal(), fldTransaksiJualDateFrom.getDate(),
                fldTransaksiJualDateTo.getDate(), name);
        JOptionPane.showMessageDialog(null, "Data Telah Tercetak");
        btnCetakTransaksiJual.enable();
    }//GEN-LAST:event_btnCetakTransaksiJualActionPerformed

    private void btnCetakLabaRugiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakLabaRugiActionPerformed
        // TODO add your handling code here:
        printLabaRugi(fldDateFrom.getDate(), fldDateTo.getDate(), 0);
    }//GEN-LAST:event_btnCetakLabaRugiActionPerformed

    private void fldTransaksiJualDateFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fldTransaksiJualDateFromActionPerformed
        // TODO add your handling code here:
        findAllTransaction(Transaction.TransactionType.SELL.ordinal(), fldTransaksiJualDateFrom.getDate(),
                fldTransaksiJualDateTo.getDate(), tblTransaksiJual, paymentType,
                (jComboBox5.getSelectedIndex() > 0 ? "" : jComboBox5.getSelectedItem().toString()),
                (jComboBox6.getSelectedIndex() > 0 ? "" : jComboBox6.getSelectedItem().toString()));
    }//GEN-LAST:event_fldTransaksiJualDateFromActionPerformed

    private void fldTransaksiJualDateToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fldTransaksiJualDateToActionPerformed
        // TODO add your handling code here:
        findAllTransaction(Transaction.TransactionType.SELL.ordinal(), fldTransaksiJualDateFrom.getDate(),
                fldTransaksiJualDateTo.getDate(), tblTransaksiJual, paymentType,
                (jComboBox5.getSelectedIndex() > 0 ? "" : jComboBox5.getSelectedItem().toString()),
                (jComboBox6.getSelectedIndex() > 0 ? "" : jComboBox6.getSelectedItem().toString()));
    }//GEN-LAST:event_fldTransaksiJualDateToActionPerformed

    private void btnCetakTransaksiBeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakTransaksiBeliActionPerformed
        // TODO add your handling code here:
        btnCetakTransaksiJual.disable();
        printTransactionBeli(Transaction.TransactionType.BUY.ordinal(), fldTransaksiBeliDateFrom.getDate(),
                fldTransaksiBeliDateTo.getDate());
        JOptionPane.showMessageDialog(null, "Data Telah Tercetak");
        btnCetakTransaksiJual.enable();
    }//GEN-LAST:event_btnCetakTransaksiBeliActionPerformed

    private void fldTransaksiBeliDateToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fldTransaksiBeliDateToActionPerformed
        // TODO add your handling code here:
        findAllTransaction(Transaction.TransactionType.BUY.ordinal(), fldTransaksiBeliDateFrom.getDate(),
                fldTransaksiBeliDateTo.getDate(), tblTransaksiBeli, paymentType,
                (jComboBox5.getSelectedIndex() > 0 ? "" : jComboBox5.getSelectedItem().toString()),
                (jComboBox6.getSelectedIndex() > 0 ? "" : jComboBox6.getSelectedItem().toString()));
    }//GEN-LAST:event_fldTransaksiBeliDateToActionPerformed

    private void fldTransaksiBeliDateFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fldTransaksiBeliDateFromActionPerformed
        // TODO add your handling code here:
        findAllTransaction(Transaction.TransactionType.BUY.ordinal(), fldTransaksiBeliDateFrom.getDate(),
                fldTransaksiBeliDateTo.getDate(), tblTransaksiBeli, paymentType,
                (jComboBox5.getSelectedIndex() > 0 ? "" : jComboBox5.getSelectedItem().toString()),
                (jComboBox6.getSelectedIndex() > 0 ? "" : jComboBox6.getSelectedItem().toString()));
    }//GEN-LAST:event_fldTransaksiBeliDateFromActionPerformed

    private void menuCariPembelianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCariPembelianActionPerformed
        // TODO add your handling code here:
        paymentType = 0;
        findAllTransaction(Transaction.TransactionType.BUY.ordinal(), fldTransaksiBeliDateFrom.getDate(),
                fldTransaksiBeliDateFrom.getDate(), tblTransaksiBeli, paymentType, jComboBox5.getSelectedItem().toString(),
                jComboBox6.getSelectedItem().toString());
        frameRiwayatTransaksiBeli.setVisible(true);
        setSuppliersCustomers(SupplierCustomer.SupplierCustomerType.SUPPLIER, jComboBox5);
        setRegions(jComboBox6);
        jButton27.setEnabled(false);
    }//GEN-LAST:event_menuCariPembelianActionPerformed

    private void menuCariPenjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCariPenjualanActionPerformed
        // TODO add your handling code here:
        paymentType = 0;
        findAllTransaction(Transaction.TransactionType.SELL.ordinal(), fldTransaksiJualDateFrom.getDate(),
                fldTransaksiJualDateTo.getDate(), tblTransaksiJual, paymentType, jComboBox7.getSelectedItem().toString(),
                jComboBox8.getSelectedItem().toString());
        frameRiwayatTransaksiJual.setVisible(true);
        setSuppliersCustomers(SupplierCustomer.SupplierCustomerType.CUSTOMER, jComboBox7);
        setRegions(jComboBox8);
        jButton35.setEnabled(false);
    }//GEN-LAST:event_menuCariPenjualanActionPerformed

    private void btnSaveRekapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveRekapActionPerformed
        try {
            // TODO add your handling code here:
            RecapitulationHistory rh = new RecapitulationHistory();
            rh.setFromDate(new Date(fldDateFrom.getDate().getTime()));
            rh.setToDate(new Date(fldDateTo.getDate().getTime()));
//            rh.setSaldoAwal((int) nf.parseObject(lblSaldoAwal.getText()));
            rh.setTotalPengeluaran((int) nf.parseObject(lblTotalPengeluaran.getText()));
            rh.setTotalPemasukan((int) nf.parseObject(lblTotalPemasukan.getText()));
            rh.getLog().setCreateDate(new Date());
            rh.getLog().setUpdateDate(new Date());
            boolean success = saveRekap(rh);
            if (success) {
                btnSaveRekap.setVisible(false);
            }
        } catch (ParseException ex) {
            Logger.getLogger(MainAccounting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSaveRekapActionPerformed

    private void menuPelunasanBeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPelunasanBeliActionPerformed
        // TODO add your handling code here:
        paymentType = Transaction.PaymentStatus.DEBT.ordinal();
        findAllTransactionDebt(Transaction.TransactionType.BUY.ordinal(), fldTransaksiBeliDateFrom.getDate(),
                fldTransaksiBeliDateFrom.getDate(), tblPelunasanBeli, paymentType);
        framePelunasanTransaksiBeli.setVisible(true);
    }//GEN-LAST:event_menuPelunasanBeliActionPerformed

    private void menuPelunasanJualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPelunasanJualActionPerformed
        // TODO add your handling code here:
        paymentType = Transaction.PaymentStatus.DEBT.ordinal();
        findAllTransaction(Transaction.TransactionType.SELL.ordinal(), fldTransaksiJualDateFrom.getDate(),
                fldTransaksiJualDateTo.getDate(), tblPelunasanJual, paymentType, null, null);
        framePelunasanTransaksiJual.setVisible(true);
    }//GEN-LAST:event_menuPelunasanJualActionPerformed

    private void fldPelunasanBeliDateToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fldPelunasanBeliDateToActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fldPelunasanBeliDateToActionPerformed

    private void fldPelunasanBeliDateFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fldPelunasanBeliDateFromActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fldPelunasanBeliDateFromActionPerformed

    private void btnPilihTransaksiBeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihTransaksiBeliActionPerformed
        try {
            // TODO add your handling code here:
            DefaultTableModel model = (DefaultTableModel) tblPelunasanBeli.getModel();
            String date = (String) model.getValueAt(tblPelunasanBeli.getSelectedRow(), 0);
            String name = (String) model.getValueAt(tblPelunasanBeli.getSelectedRow(), 2);
            String total_prices = (String) model.getValueAt(tblPelunasanBeli.getSelectedRow(), 3);

            fldPelunasanDate.setDate(new Date(System.currentTimeMillis()));
            fldPelunasanNama.setText(name);
            fldPelunasanTotalHarga.setText(total_prices);
            jdlPelunasan.setText("Pelunasan Pembelian");
            fldPelunasanNota.setVisible(false);
            fldPelunasanFaktur.setVisible(false);
            lblPelunasanNota.setVisible(false);
            lblPelunasanFaktur.setVisible(false);

            String transactionId = getTransactionIdForPelunasan(Transaction.TransactionType.BUY.ordinal(),
                    new java.sql.Date(df.parse(date).getTime()), name);
            fldPelunasanTransactionId.setText(transactionId);
            findAllPelunasanByTransactionId(transactionId);

            double totalPayments = getTotalpayment(transactionId);
            total_prices = total_prices.replace(",00", "").replace(".", "");
            double debt = Double.parseDouble(total_prices) - totalPayments;
            fldPelunasanTotalPembayaran.setText(nf.format(totalPayments));
            fldPelunasanSisaHutang.setText(nf.format(debt));
            jdlPelunasan.setText("Pelunasan Pembelian");
            lblPelunasanNama.setText("Petani");
            framePelunasanTransaksiBeli.setVisible(false);
            framePelunasan.setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(MainAccounting.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnPilihTransaksiBeliActionPerformed

    private void fldPelunasanNamaPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fldPelunasanNamaPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_fldPelunasanNamaPropertyChange

    private void fldPelunasanTotalHargaPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fldPelunasanTotalHargaPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_fldPelunasanTotalHargaPropertyChange

    private void fldPelunasanBayarPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fldPelunasanBayarPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_fldPelunasanBayarPropertyChange

    private void fldPelunasanTotalPembayaranPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fldPelunasanTotalPembayaranPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_fldPelunasanTotalPembayaranPropertyChange

    private void fldPelunasanSisaHutangPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fldPelunasanSisaHutangPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_fldPelunasanSisaHutangPropertyChange

    private void btnBatalPelunasanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalPelunasanActionPerformed
        // TODO add your handling code here:
        framePelunasan.setVisible(false);
    }//GEN-LAST:event_btnBatalPelunasanActionPerformed

    private void btnSimpanPelunasanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanPelunasanActionPerformed
        // TODO add your handling code here:
        DecimalFormat decf = new DecimalFormat("0");
        Pelunasan p = new Pelunasan();
        try {
            p.setPayment(Long.parseLong(decf.parse(fldPelunasanBayar.getText()) + ""));
            p.setTransactionId(fldPelunasanTransactionId.getText());
            p.getLog().setCreateDate(fldPelunasanDate.getDate());
            p.getLog().setUpdateDate(fldPelunasanDate.getDate());
            boolean success = savePayment(p);
            if (success) {
                fldPelunasanBayar.setText("0");
                double totalPayments = getTotalpayment(fldPelunasanTransactionId.getText());
                double totalPrice = Double.parseDouble(fldPelunasanTotalHarga.getText().replace(",00", "").replace(".", ""));
                double debt = totalPrice - totalPayments;
                findAllPelunasanByTransactionId(fldPelunasanTransactionId.getText());
                fldPelunasanTotalPembayaran.setText(nf.format(totalPayments));
                fldPelunasanSisaHutang.setText(nf.format(debt));
                if (debt <= 0) {
                    updateTransaksiById(fldPelunasanTransactionId.getText(), Transaction.PaymentStatus.FULL.ordinal(), Long.parseLong(fldPelunasanTotalHarga.getText().replace(",00", "").replace(".", "")));
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(MainAccounting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSimpanPelunasanActionPerformed

    private void fldTransaksiJualDateTo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fldTransaksiJualDateTo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fldTransaksiJualDateTo1ActionPerformed

    private void fldTransaksiJualDateFrom1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fldTransaksiJualDateFrom1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fldTransaksiJualDateFrom1ActionPerformed

    private void btnPilihTransaksiJualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihTransaksiJualActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            DefaultTableModel model = (DefaultTableModel) tblPelunasanJual.getModel();
            String date = (String) model.getValueAt(tblPelunasanJual.getSelectedRow(), 0);
            String nota = (String) model.getValueAt(tblPelunasanJual.getSelectedRow(), 1);
            String name = (String) model.getValueAt(tblPelunasanJual.getSelectedRow(), 2);
            String faktur = (String) model.getValueAt(tblPelunasanJual.getSelectedRow(), 3);
            String total_prices = (String) model.getValueAt(tblPelunasanJual.getSelectedRow(), 4);

            fldPelunasanNama.setText(name);
            fldPelunasanTotalHarga.setText(total_prices);
            fldPelunasanNota.setText(nota);
            fldPelunasanFaktur.setText(faktur);
            fldPelunasanDate.setDate(new Date(System.currentTimeMillis()));
            jdlPelunasan.setText("Pelunasan Penjualan");
            fldPelunasanNota.setVisible(true);
            fldPelunasanFaktur.setVisible(true);
            lblPelunasanNota.setVisible(true);
            lblPelunasanFaktur.setVisible(true);

            String transactionId = getTransactionIdForPelunasan(Transaction.TransactionType.SELL.ordinal(),
                    new java.sql.Date(df.parse(date).getTime()), name);
            fldPelunasanTransactionId.setText(transactionId);
            findAllPelunasanByTransactionId(transactionId);

            double totalPayments = getTotalpayment(transactionId);
            total_prices = total_prices.replace(",00", "").replace(".", "");
            double debt = Double.parseDouble(total_prices) - totalPayments;
            fldPelunasanTotalPembayaran.setText(nf.format(totalPayments));
            fldPelunasanSisaHutang.setText(nf.format(debt));
            framePelunasanTransaksiJual.setVisible(false);
            jdlPelunasan.setText("Pelunasan Penjualan");
            lblPelunasanNama.setText("Customer");
            framePelunasan.setVisible(true);
        } catch (ParseException ex) {
            Logger.getLogger(MainAccounting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPilihTransaksiJualActionPerformed

    private void fldFromItemStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fldFromItemStokActionPerformed
        // TODO add your handling code here:
        fldSisaStok.setText(getStokItemByName((String) fldFromItemStok.getSelectedItem()) + "");
        setCategories(fldItemTambahStok);
        fldItemTambahStok.removeItem(fldFromItemStok.getSelectedItem());
        fldItemTambahStok.removeItem("");
    }//GEN-LAST:event_fldFromItemStokActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void tblPelunasanBeliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPelunasanBeliMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblPelunasanBeli.getModel();
        String code = (String) model.getValueAt(tblPelunasanBeli.getSelectedRow(), 1);
        findAllTransactionDetail(tblDetailPelunasanBeli, code, 0);
    }//GEN-LAST:event_tblPelunasanBeliMouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.addRow(new Object[]{model.getRowCount() + 1, null, null, 0, 0, 0});
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(null, i, 1);
        }
        if (jComboBox3.getSelectedItem() != null) {
            setRegionBySupplierCustomer(jComboBox3.getSelectedItem().toString(), jTable2, 1);
        }
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void tblTransaksiBeliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTransaksiBeliMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblTransaksiBeli.getModel();
        String code = (String) model.getValueAt(tblTransaksiBeli.getSelectedRow(), 1);
        findAllTransactionDetail(tblDetailBeli, code.replace("T", ""), 0);
        jButton27.setEnabled(true);
    }//GEN-LAST:event_tblTransaksiBeliMouseClicked

    private void tblTransaksiJualMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTransaksiJualMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblTransaksiJual.getModel();
        String code = (String) model.getValueAt(tblTransaksiJual.getSelectedRow(), 1);
        findAllTransactionDetail(tblDetailJual, code.replace("T", ""), 1);
        jButton35.setEnabled(true);
    }//GEN-LAST:event_tblTransaksiJualMouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        frameBeli.setVisible(false);
        jPanel10.setVisible(false);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        try {
            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            String name = jComboBox3.getSelectedItem().toString();

            Transaction t = new Transaction();
            t.setCode(jTextField13.getText().replace("T", ""));
            t.getLog().setCreateDate(jXDatePicker1.getDate());
            t.getLog().setUpdateDate(jXDatePicker1.getDate());
            boolean valid = findTransactionByCode(t.getLog().getCreateDate(), t.getCode());
            if (valid) {
                if (model.getRowCount() != 0) {
                    t.setTotalPrice(Double.parseDouble(nf.parse(jTextField9.getText()) + ""));
                    t.setTransactionType(Transaction.TransactionType.BUY);
                    t.setPaymentStatus(Transaction.PaymentStatus.DEBT);
                    t.setTebasan(jCheckBox1.isSelected());
                    t.setDescription(jTextArea1.getText());
                    t.setId(saveTransaksi(t));

                    for (int row = 0; row < model.getRowCount(); row++) {
                        InventoryCategory ic = findCategoryIdByName(model.getValueAt(row, 2).toString());
                        t.setNo((int) model.getValueAt(row, 0));
                        t.setSupplierCustomerId(findSupplierCustomerByName(SupplierCustomer.SupplierCustomerType.SUPPLIER, name, model.getValueAt(row, 1).toString()));
                        t.setInventoryCategoryId(ic.getId());
                        t.setPrice(Double.parseDouble(model.getValueAt(row, 3).toString()));
                        t.setQuantity(Double.parseDouble(model.getValueAt(row, 4).toString()));
                        t.setTotalPrice(Double.parseDouble(model.getValueAt(row, 5).toString()));
                        t.setsisaStock(ic.getTotalItems() + t.getQuantity());
                        saveTransaksiDetail(t);
                        updateInventoryById(ic.getId(), t.getsisaStock());
                    }

                    model.setRowCount(0);
                    jTextField9.setText("0");
                    jTextField13.setText("T" + (Integer.parseInt(t.getCode()) + 1));
                    jPanel10.setVisible(false);
                    jTextArea1.setText("");
                    jCheckBox1.setSelected(false);
                    jTextField9.setEditable(false);
                    JOptionPane.showMessageDialog(null, "Data Telah Tersimpan");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        Transaction t = new Transaction();
        double total = 0;
        if (jCheckBox1.isSelected()) {
            total = Double.parseDouble(jTextField9.getText());
        } else {
            for (int row = 0; row < model.getRowCount(); row++) {
                t.setQuantity(Double.parseDouble(model.getValueAt(row, 4).toString()));
                t.setPrice(Double.parseDouble(model.getValueAt(row, 3).toString()));
                t.setTotalPrice(t.getQuantity() * t.getPrice());
                model.setValueAt(t.getTotalPrices(), row, 5);
                total = total + t.getTotalPrices();
            }
        }
        jTextField9.setText(nf.format(total));
        jPanel10.setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.addRow(new Object[]{model.getRowCount() + 1, null, null, null, 0, 0, 0});
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(null, i, 1);
        }
        if (jComboBox1.getSelectedItem() != null) {
            setRegionBySupplierCustomer(jComboBox1.getSelectedItem().toString(), jTable1, 1);
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        Transaction t = new Transaction();
        double total = 0;
        for (int row = 0; row < model.getRowCount(); row++) {
            t.setQuantity(Double.parseDouble(model.getValueAt(row, 5).toString()));
            t.setPrice(Double.parseDouble(model.getValueAt(row, 4).toString()));
            t.setTotalPrice(t.getQuantity() * t.getPrice());
            model.setValueAt(t.getTotalPrices(), row, 6);
            total = total + t.getTotalPrices();
        }
        jTextField3.setText(nf.format(total));
        jPanel9.setVisible(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        frameJual.setVisible(false);
        jPanel9.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            String name = jComboBox1.getSelectedItem().toString();

            Transaction t = new Transaction();
            t.setCode(jTextField12.getText().replace("T", ""));
            t.getLog().setCreateDate(jXDatePicker2.getDate());
            t.getLog().setUpdateDate(jXDatePicker2.getDate());
            boolean valid = findTransactionByCode(t.getLog().getCreateDate(), t.getCode());
            if (valid) {
                if (model.getRowCount() != 0) {
                    t.setTotalPrice(Double.parseDouble(nf.parse(jTextField3.getText()) + ""));
                    t.setTransactionType(Transaction.TransactionType.SELL);
                    t.setPaymentStatus(Transaction.PaymentStatus.DEBT);
                    t.setInvoices(jTextField2.getText());
                    t.setNota(jTextField1.getText());
                    t.setId(saveTransaksi(t));

                    for (int row = 0; row < model.getRowCount(); row++) {
                        InventoryCategory ic = findCategoryIdByName(model.getValueAt(row, 2).toString());
                        t.setNo((int) model.getValueAt(row, 0));
                        t.setSupplierCustomerId(findSupplierCustomerByName(SupplierCustomer.SupplierCustomerType.CUSTOMER, name, model.getValueAt(row, 1).toString()));
                        t.setInventoryCategoryId(ic.getId());
                        t.setItemPartai(model.getValueAt(row, 3).toString());
                        t.setPrice(Double.parseDouble(model.getValueAt(row, 4).toString()));
                        t.setQuantity(Double.parseDouble(model.getValueAt(row, 5).toString()));
                        t.setTotalPrice(Double.parseDouble(model.getValueAt(row, 6).toString()));
                        t.setsisaStock(ic.getTotalItems() - t.getQuantity());
                        saveTransaksiDetail(t);
                        updateInventoryById(ic.getId(), t.getsisaStock());
                    }

                    model.setRowCount(0);
                    jTextField3.setText("0");
                    jTextField12.setText("T" + (Integer.parseInt(t.getCode()) + 1));
                    JOptionPane.showMessageDialog(null, "Data Telah Tersimpan");
                    jPanel9.setVisible(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tblPelunasanJualMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPelunasanJualMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblPelunasanJual.getModel();
        String code = (String) model.getValueAt(tblPelunasanJual.getSelectedRow(), 1);
        findAllTransactionDetail(tblDetailPelunasanJual, code, 1);
    }//GEN-LAST:event_tblPelunasanJualMouseClicked

    private void fldDateGajiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fldDateGajiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fldDateGajiActionPerformed

    private void fldTransaksiJualDateFrom3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fldTransaksiJualDateFrom3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fldTransaksiJualDateFrom3ActionPerformed

    private void fldTransaksiJualDateFrom4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fldTransaksiJualDateFrom4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fldTransaksiJualDateFrom4ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        fldDateGaji.setDate(new Date(System.currentTimeMillis()));
        frameGaji.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblGaji.getModel();
        model.addRow(new Object[]{"", 0});
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblGaji.getModel();
        for (int row = 0; row < model.getRowCount(); row++) {
            saveGaji(fldDateGaji.getDate(), model.getValueAt(row, 0).toString(),
                    Integer.parseInt(model.getValueAt(row, 1).toString()));
        }
        frameGaji.setVisible(false);
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
        fldTransaksiJualDateFrom3.setDate(new Date(System.currentTimeMillis()));
        setRegions(jComboBox2);
        frameKomisi.setVisible(true);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
        saveKomisi(fldTransaksiJualDateFrom3.getDate(), jTextField6.getText(), Integer.parseInt(jTextField7.getText()),
                jComboBox2.getSelectedItem().toString());
        frameKomisi.setVisible(false);
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        fldTransaksiJualDateFrom4.setDate(new Date(System.currentTimeMillis()));
        frameTransaksiLain.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable5.getModel();
        model.addRow(new Object[]{"", 0, 0, 0});
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable5.getModel();
        Transaction t = new Transaction();
        t.setId(saveTransaksiLain(fldTransaksiJualDateFrom4.getDate(), Transaction.TransactionType.OTHER.ordinal(),
                Integer.parseInt(fldTotalBiaya.getText().replace(",00", "").replace(".", ""))));
        for (int row = 0; row < model.getRowCount(); row++) {
            t.setItemPartai(model.getValueAt(row, 0).toString());
            t.setQuantity(Double.parseDouble(model.getValueAt(row, 1).toString()));
            t.setPrice(Double.parseDouble(model.getValueAt(row, 2).toString()));
            t.setTotalPrice(Double.parseDouble(model.getValueAt(row, 3).toString()));
            t.setNo((row + 1));
            saveTransaksiDetail(t);
        }
        JOptionPane.showMessageDialog(null, "Data Telah Tersimpan");
        frameTransaksiLain.setVisible(false);
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        fldDatePengiriman.setDate(new Date(System.currentTimeMillis()));
        framePengiriman.setVisible(true);
        setRegionCell(jTable3, 0);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
        model.addRow(new Object[]{});
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
        String id = savePengiriman(fldDatePengiriman.getDate(), Integer.parseInt(jTextField4.getText()));
        for (int row = 0; row < model.getRowCount(); row++) {
            savePengirimanDetail(fldDatePengiriman.getDate(), model.getValueAt(row, 0).toString(), id);
        }
        framePengiriman.setVisible(false);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable5.getModel();
        Transaction t = new Transaction();
        double total = 0;
        for (int row = 0; row < model.getRowCount(); row++) {
            t.setQuantity(Double.parseDouble(model.getValueAt(row, 1).toString()));
            t.setPrice(Double.parseDouble(model.getValueAt(row, 2).toString()));
            if (model.getValueAt(row, 3).toString().equalsIgnoreCase("0")) {
                t.setTotalPrice(t.getPrice() * t.getQuantity());
                model.setValueAt(t.getTotalPrices(), row, 3);
            } else {
                t.setTotalPrice(Double.parseDouble(model.getValueAt(row, 3).toString()));
            }
            total = total + t.getTotalPrices();
        }
        fldTotalBiaya.setText(nf.format(total));
    }//GEN-LAST:event_jButton20ActionPerformed

    private void btnCetakStockKardusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakStockKardusActionPerformed
        // TODO add your handling code here:
        printStockKardus(fldDateFrom.getDate(), fldDateTo.getDate());
    }//GEN-LAST:event_btnCetakStockKardusActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblWilayahResult.getModel();
        jTextField14.setText(model.getValueAt(tblWilayahResult.getSelectedRow(), 0).toString());
        jTextField15.setText(model.getValueAt(tblWilayahResult.getSelectedRow(), 1).toString());
        panelEditWilayah.setVisible(true);
        frameCariWilayah.setSize(488, 499);
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        // TODO add your handling code here:
        panelEditWilayah.setVisible(false);
        frameCariWilayah.setSize(488, 390);
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        // TODO add your handling code here:
        Region r = new Region();
        r.setCode(jTextField14.getText().replace("R", ""));
        r.setName(jTextField15.getText());
        if (!r.getName().equalsIgnoreCase("")) {
            updateRegionByCode(r);
            setRegions("");
            frameCariWilayah.setSize(488, 390);
            JOptionPane.showMessageDialog(null, "Data Telah Tersimpan");
            panelEditWilayah.setVisible(false);
        }
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        // TODO add your handling code here:
        InventoryCategory ic = new InventoryCategory();
        ic.setItemCode(jTextField18.getText());
        ic.setName(jTextField19.getText());
        if (!ic.getName().equalsIgnoreCase("")) {
            updateItemByCode(ic);
            findCategories("");
            framekategori.setSize(498, 390);
            JOptionPane.showMessageDialog(null, "Data Telah Tersimpan");
            panelEditKategori.setVisible(false);
        }
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        // TODO add your handling code here:
        panelEditKategori.setVisible(false);
        framekategori.setSize(498, 390);
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblKategoriResult.getModel();
        jTextField19.setText(model.getValueAt(tblKategoriResult.getSelectedRow(), 0).toString());
        jTextField18.setText(model.getValueAt(tblKategoriResult.getSelectedRow(), 1).toString());
        panelEditKategori.setVisible(true);
        framekategori.setSize(498, 482);
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblResult.getModel();
        jTextField20.setText(model.getValueAt(tblResult.getSelectedRow(), 0).toString());
        jTextField21.setText(model.getValueAt(tblResult.getSelectedRow(), 1).toString());
        jTextField22.setText(model.getValueAt(tblResult.getSelectedRow(), 2).toString());
        setRegions(jComboBox4);
        jComboBox4.setSelectedItem(model.getValueAt(tblResult.getSelectedRow(), 4));
        frameCari.setSize(498, 550);
        jPanel4.setVisible(true);
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        // TODO add your handling code here:
        frameCari.setSize(498, 360);
        jPanel4.setVisible(false);
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        // TODO add your handling code here:
        SupplierCustomer sc = new SupplierCustomer();
        sc.setCode(jTextField20.getText().replace("SC", ""));
        sc.setName(jTextField21.getText());
        sc.setAddress(jComboBox4.getSelectedItem().toString());
        sc.setPhone(jTextField22.getText().equalsIgnoreCase("") ? 0 : Integer.parseInt(jTextField22.getText()));
        sc.setRegionId(getRegionByName(jComboBox4.getSelectedItem().toString()));
        if (!sc.getName().equalsIgnoreCase("")) {
            updateSupplierCustomerByCode(sc);
            findSupplierOrCustomer(sc.getCode().startsWith("S") ? SupplierCustomer.SupplierCustomerType.SUPPLIER
                    : SupplierCustomer.SupplierCustomerType.CUSTOMER, "");
            frameCari.setSize(498, 360);
            JOptionPane.showMessageDialog(null, "Data Telah Tersimpan");
            jPanel4.setVisible(false);
        }
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int count = jTable1.getSelectedRowCount();
        for (int i = 0; i < count; i++) {
            model.removeRow(jTable1.getSelectedRow());
        }
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i + 1, i, 0);
        }
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        int count = jTable2.getSelectedRowCount();
        for (int i = 0; i < count; i++) {
            model.removeRow(jTable2.getSelectedRow());
        }
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i + 1, i, 0);
        }
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        // TODO add your handling code here:
        lblJudul12.setText("Pembelian");
        jPanel11.setVisible(false);
        jPanel12.setVisible(true);
        jTextField29.setVisible(false);
        jLabel51.setVisible(false);
        frameRiwayatTransaksiBeli.setVisible(false);
        frameHargaTransaksi.setVisible(true);

        DefaultTableModel model = (DefaultTableModel) tblTransaksiBeli.getModel();
        jTextField30.setText(model.getValueAt(tblTransaksiBeli.getSelectedRow(), 1).toString());
        jTextField26.setText(model.getValueAt(tblTransaksiBeli.getSelectedRow(), 0).toString());
        jTextField27.setText(model.getValueAt(tblTransaksiBeli.getSelectedRow(), 3).toString());
        jTextField25.setText(model.getValueAt(tblTransaksiBeli.getSelectedRow(), 4).toString());
        jButton42.setEnabled(false);

        jCheckBox2.setSelected(isTebasan(jTextField30.getText().replace("T", "")));
        if (jCheckBox2.isSelected()) {
            jTextField25.setEditable(true);
        } else {
            jTextField25.setEditable(false);
        }

        findAllTransactionDetail(jTable4, jTextField30.getText().replace("T", ""), 0);
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        // TODO add your handling code here:
        lblJudul12.setText("Penjualan");
        jPanel11.setVisible(true);
        jPanel12.setVisible(false);
        jTextField29.setVisible(true);
        jLabel51.setVisible(true);
        frameRiwayatTransaksiJual.setVisible(false);
        frameHargaTransaksi.setVisible(true);

        DefaultTableModel model = (DefaultTableModel) tblTransaksiJual.getModel();
        jTextField31.setText(model.getValueAt(tblTransaksiJual.getSelectedRow(), 1).toString());
        jTextField16.setText(model.getValueAt(tblTransaksiJual.getSelectedRow(), 2).toString());
        jTextField17.setText(model.getValueAt(tblTransaksiJual.getSelectedRow(), 0).toString());
        jTextField23.setText(model.getValueAt(tblTransaksiJual.getSelectedRow(), 3).toString());
        jTextField25.setText(model.getValueAt(tblTransaksiJual.getSelectedRow(), 5).toString());
        jTextField29.setText(nf.format(getTransactionByCode(jTextField31.getText().replace("T", "")).getTotalMarketPrice()));
        jButton42.setEnabled(false);

        findAllTransactionDetail(jTable4, jTextField31.getText().replace("T", ""), 1);
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
        // TODO add your handling code here:
        findAllTransaction(Transaction.TransactionType.SELL.ordinal(), fldTransaksiJualDateFrom.getDate(),
                fldTransaksiJualDateTo.getDate(), tblTransaksiJual, paymentType,
                jComboBox7.getSelectedItem().toString(), jComboBox8.getSelectedItem().toString());
    }//GEN-LAST:event_jButton38ActionPerformed

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
        Transaction t = new Transaction();
        double total = 0;
        if (jCheckBox2.isSelected()) {
            try {
                for (int row = 0; row < model.getRowCount(); row++) {
                    t.setQuantity(Double.parseDouble(model.getValueAt(row, 2).toString()));
                    t.setPrice(Double.parseDouble(model.getValueAt(row, 3).toString()));
                    t.setTotalPrice(t.getQuantity() * t.getPrice());
                    model.setValueAt(t.getTotalPrices(), row, 4);
                    total = total + t.getTotalPrices();
                }
                jTextField25.setText(nf.format(nf.parseObject(jTextField25.getText())));
            } catch (ParseException ex) {
                Logger.getLogger(MainAccounting.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            for (int row = 0; row < model.getRowCount(); row++) {
                t.setQuantity(Double.parseDouble(model.getValueAt(row, 2).toString()));
                t.setPrice(Double.parseDouble(model.getValueAt(row, 3).toString()));
                t.setTotalPrice(t.getQuantity() * t.getPrice());
                model.setValueAt(t.getTotalPrices(), row, 4);
                total = total + t.getTotalPrices();
            }
            jTextField25.setText(nf.format(total));
        }
        jPanel10.setVisible(true);
        jButton42.setEnabled(true);
    }//GEN-LAST:event_jButton40ActionPerformed

    private void jButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton42ActionPerformed
        // TODO add your handling code here:
        try {
            Transaction t = new Transaction();
            DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
            t.setCode((lblJudul12.getText().equalsIgnoreCase("Pembelian") ? jTextField30.getText() : jTextField31.getText()).replace("T", ""));
            t.setTotalPrice(Double.parseDouble(nf.parse(jTextField25.getText()) + ""));
            t.getLog().setUpdateDate(new Timestamp(System.currentTimeMillis()));
            if (!lblJudul12.getText().equalsIgnoreCase("Pembelian")) {
                t.setTotalMarketPrice(Double.parseDouble(jTextField29.getText()));
            }
            for (int i = 0; i < model.getRowCount(); i++) {
                Transaction td = new Transaction();
                td.setCode(model.getValueAt(i, 0).toString());
                td.setQuantity(Double.parseDouble(model.getValueAt(i, 2).toString()));
                td.setPrice(Double.parseDouble(model.getValueAt(i, 3).toString()));
                td.setTotalPrice((!model.getValueAt(i, 4).toString().equalsIgnoreCase("0,00") ? Double.parseDouble(model.getValueAt(i, 4).toString()) : 0));
                td.getLog().setUpdateDate(t.getLog().getUpdateDate());
                updateTransactionDetail(t, td);
            }
            updateTransaction(t);
            JOptionPane.showMessageDialog(null, "Data Telah Tersimpan");
            if (lblJudul12.getText().equalsIgnoreCase("Pembelian")) {
//                jButton39.doClick();
                frameRiwayatTransaksiBeli.setVisible(true);
            } else {
                jButton38.doClick();
                frameRiwayatTransaksiJual.setVisible(true);
            }
            frameHargaTransaksi.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton42ActionPerformed

    private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
        // TODO add your handling code here:
        if (lblJudul12.getText().equalsIgnoreCase("Pembelian")) {
            frameRiwayatTransaksiBeli.setVisible(true);
        } else {
            frameRiwayatTransaksiJual.setVisible(true);
        }

        frameHargaTransaksi.setVisible(false);
    }//GEN-LAST:event_jButton41ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        // TODO add your handling code here:
        Transaction t = new Transaction();
        List<Transaction> tdList = new ArrayList<Transaction>();
        DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
        int selectedItem = jTable4.getSelectedRowCount();
        int reply = JOptionPane.showConfirmDialog(null, "Anda yakin mau menghapus (" + selectedItem
                + ") data yang telah dipilih ?", "Anda yakin ?", JOptionPane.YES_NO_OPTION);
        if (reply == 0) {
            t.setCode((lblJudul12.getText().equalsIgnoreCase("Pembelian") ? jTextField30.getText() : jTextField31.getText()).replace("T", ""));
            int[] selection = jTable4.getSelectedRows();
            for (int i = 0; i < selectedItem; i++) {
                Transaction td = new Transaction();
                td.setCode(model.getValueAt(selection[i], 0).toString().replace("T", ""));
                System.out.println(td.getCode());
                tdList.add(td);
            }
        }
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton39ActionPerformed
        // TODO add your handling code here:
        findAllTransaction(Transaction.TransactionType.BUY.ordinal(), fldTransaksiBeliDateFrom.getDate(),
                fldTransaksiBeliDateTo.getDate(), tblTransaksiBeli, paymentType, jComboBox5.getSelectedItem().toString(),
                jComboBox6.getSelectedItem().toString());
    }//GEN-LAST:event_jButton39ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        // TODO add your handling code here:
        printStockApel(fldDateFrom.getDate(), fldDateTo.getDate());
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable5.getModel();
        int count = jTable5.getSelectedRowCount();
        for (int i = 0; i < count; i++) {
            model.removeRow(jTable5.getSelectedRow());
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jCheckBox1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox1MouseClicked
        // TODO add your handling code here:
        if (jCheckBox1.isSelected()) {
            jTextField9.setEditable(true);
        } else {
            jTextField9.setEditable(false);
            jTextField9.setText("0");
        }
    }//GEN-LAST:event_jCheckBox1MouseClicked

    private void jCheckBox2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox2MouseClicked
        // TODO add your handling code here:
        if (jCheckBox2.isSelected()) {
            jTextField25.setEditable(true);
        } else {
            jTextField25.setEditable(false);
            jTextField25.setText(jTextField25.getText());
        }
    }//GEN-LAST:event_jCheckBox2MouseClicked

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        // TODO add your handling code here:
        printLabaRugi(fldDateFrom.getDate(), fldDateTo.getDate(), 1);
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
        jInternalFrame1.setVisible(true);
//        findBiayaPacking();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void findBiayaPacking(){
        try {
            DefaultTableModel model = (DefaultTableModel) jTable6.getModel();
            String query = "SELECT create_date, total_prices, active_flag from transaction where transaction_type = 2";
            PreparedStatement pre = con.prepareStatement(query);
            ResultSet rs = pre.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{df.format(rs.getDate("create_date")), nf.format(rs.getLong("total_prices")), ""});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    } 
    
    private boolean isTebasan(String code) {
        try {
            String query = "select tebasan from transaction where code = ?";

            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, code);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                return rs.getBoolean("tebasan");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getLastCodeTransactionDetail(String transactionId) {
        try {
            String query = "select code from transaction_detail where transaction_id = ? order by code DESC LIMIT 1";

            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, transactionId);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                return rs.getInt("code");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Transaction getTransactionByCode(String code) {
        Transaction t = new Transaction();
        try {
            String query = "select * from transaction where code = ?";

            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, code);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                t.setTotalMarketPrice(rs.getDouble("total_market_prices"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    private void deleteTransaction(Transaction t) {
        try {
            String query = "delete from transaction where code = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, t.getCode());
            pre.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteTransactionDetail(Transaction t, List<Transaction> tdList) {
        try {
            String query = "DELETE td.* FROM transaction_detail td "
                    + "INNER JOIN transaction t ON td.transaction_id = t.id "
                    + "WHERE t.code = ? ";
            int i = 0;
            if (tdList.size() > 0) {
                query += "and (";
                for (Transaction td : tdList) {
                    query += "td.code = ? " + (i < tdList.size() ? " OR " : "");
                    i++;
                }
                query += ")";
            }
            i = 1;
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, t.getCode());
            for (Transaction td : tdList) {
                i += 1;
                pre.setObject(i, td.getCode());
            }
            pre.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateTransaction(Transaction t) {
        try {
            String query = "update transaction set total_prices = ?, update_date = ?, total_market_prices = ? where code = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, t.getTotalPrices());
            pre.setObject(2, t.getLog().getUpdateDate());
            pre.setObject(3, t.getTotalMarketPrice());
            pre.setObject(4, t.getCode());
            pre.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateTransactionDetail(Transaction t, Transaction td) {
        try {
            String query = "update transaction_detail td inner join transaction t on(td.transaction_id = t.id) "
                    + "set td.price = ?, td.total_prices = ?, td.update_date = ?, td.quantity = ? where t.code = ? and td.code = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, td.getPrice());
            pre.setObject(2, td.getTotalPrices());
            pre.setObject(3, t.getLog().getUpdateDate());
            pre.setObject(4, td.getQuantity());
            pre.setObject(5, t.getCode());
            pre.setObject(6, td.getCode());
            pre.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int rowCount(String table) {
        try {
            String query = "select code from " + table + " order by code desc limit 1";

            PreparedStatement pre = con.prepareStatement(query);
            System.out.println(pre.toString());
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                return rs.getInt("code");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public void updateRegionByCode(Region r) {
        try {
            String query = "UPDATE region SET name = ? WHERE code = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, r.getName());
            pre.setObject(2, r.getCode());
            pre.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateSupplierCustomerByCode(SupplierCustomer sc) {
        try {
            String query = "UPDATE supplier_customer SET name = ?, address = ?, region = ?, phone = ? WHERE code = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, sc.getName());
            pre.setObject(2, sc.getAddress());
            pre.setObject(3, sc.getRegionId());
            pre.setObject(4, sc.getPhone());
            pre.setObject(5, sc.getCode());
            pre.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateItemByCode(InventoryCategory ic) {
        try {
            String query = "UPDATE inventory_category SET name = ? WHERE item_code = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, ic.getName());
            pre.setObject(2, ic.getItemCode());
            pre.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public long getTotalGaji(Date dateFrom, Date dateTo) {
        try {
            String query = "select sum(gk.gaji) as gaji from gaji_karyawan gk "
                    + "where (gk.create_date between ? and ?)";

            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, dateFrom);
            pre.setObject(2, dateTo);
            System.out.println(pre.toString());
            ResultSet rs = pre.executeQuery();
            long[] total = new long[3];
            while (rs.next()) {
                return rs.getLong("gaji");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public long getTotalKomisi(Date dateFrom, Date dateTo) {
        try {
            String query = "select sum(k.jumlah) as komisi from komisi k "
                    + "where (k.create_date between ? and ?)";

            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, dateFrom);
            pre.setObject(2, dateTo);
            System.out.println(pre.toString());
            ResultSet rs = pre.executeQuery();
            long[] total = new long[3];
            while (rs.next()) {
                return rs.getLong("komisi");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public long getTotalKirim(Date dateFrom, Date dateTo) {
        try {
            String query = "select sum(p.total_biaya) as pengiriman from pengiriman p "
                    + "where (p.create_date between ? and ?)";

            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, dateFrom);
            pre.setObject(2, dateTo);
            System.out.println(pre.toString());
            ResultSet rs = pre.executeQuery();
            long[] total = new long[3];
            while (rs.next()) {
                return rs.getLong("pengiriman");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public long getTotalTransaksiLain(Date dateFrom, Date dateTo) {
        try {
            String query = "select sum(t.total_prices) as payment from transaction t "
                    + "where t.transaction_type = 2 and (t.create_date between ? and ?) ";

            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, dateFrom);
            pre.setObject(2, dateTo);
            System.out.println(pre.toString());
            ResultSet rs = pre.executeQuery();
            long[] total = new long[3];
            while (rs.next()) {
                return rs.getLong("payment");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private String savePengirimanDetail(Date date, String wilayah, String pengirimanId) {
        try {
            String query = "INSERT INTO pengiriman_detail (id, create_date, update_date, region_id, pengiriman_id) "
                    + "VALUES (?, ?, ?, ?, ?)";
            String id = randomId();
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, id);
            pre.setObject(2, date);
            pre.setObject(3, date);
            pre.setObject(4, getRegionByName(wilayah));
            pre.setObject(5, pengirimanId);
            pre.execute();
            return id;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private String savePengiriman(Date date, int total) {
        try {
            String query = "INSERT INTO pengiriman (id, create_date, update_date, total_biaya) "
                    + "VALUES (?, ?, ?, ?)";
            String id = randomId();
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, id);
            pre.setObject(2, date);
            pre.setObject(3, date);
            pre.setObject(4, total);
            pre.execute();
            return id;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private String saveTransaksiLain(Date date, int type, int total) {
        try {
            String query = "INSERT INTO transaction (id, create_date, update_date, transaction_type, total_prices) "
                    + "VALUES (?, ?, ?, ?, ?)";
            String id = randomId();
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, id);
            pre.setObject(2, date);
            pre.setObject(3, date);
            pre.setObject(4, type);
            pre.setObject(5, total);
            pre.execute();
            return id;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private String saveKomisi(Date date, String name, int jumlah, String wilayah) {
        try {
            String query = "INSERT INTO komisi (id, create_date, update_date, name, jumlah, region_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            String id = randomId();
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, id);
            pre.setObject(2, date);
            pre.setObject(3, date);
            pre.setObject(4, name);
            pre.setObject(5, jumlah);
            pre.setObject(6, getRegionByName(wilayah));
            pre.execute();
            return id;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private String saveGaji(Date date, String name, int gaji) {
        try {
            String query = "INSERT INTO gaji_karyawan (id, create_date, update_date, name, gaji)"
                    + "VALUES (?, ?, ?, ?, ?)";
            String id = randomId();
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, id);
            pre.setObject(2, date);
            pre.setObject(3, date);
            pre.setObject(4, name);
            pre.setObject(5, gaji);
            pre.execute();
            return id;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private boolean savePayment(Pelunasan p) {
        try {
            String query = "INSERT INTO pelunasan (id, create_date, update_date, transaction_id, payment)"
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, randomId());
            pre.setObject(2, p.getLog().getCreateDate());
            pre.setObject(3, p.getLog().getUpdateDate());
            pre.setObject(4, p.getTransactionId());
            pre.setObject(5, p.getPayment());
            pre.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private double getTotalpayment(String transactionId) {
        try {
            String query = "SELECT SUM(payment) AS payments FROM pelunasan "
                    + "WHERE transaction_id = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, transactionId);

            ResultSet rs = pre.executeQuery();
            double totalPayment = 0;
            while (rs.next()) {
                totalPayment = rs.getDouble("payments");
            }
            return totalPayment;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public void findAllPelunasanByTransactionId(String transactionId) {
        try {
            DefaultTableModel model = (DefaultTableModel) tblPelunasan.getModel();
            String query = "SELECT create_date, payment FROM pelunasan "
                    + "WHERE transaction_id = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, transactionId);

            ResultSet rs = pre.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{df.format(rs.getDate("create_date")), nf.format(rs.getLong("payment"))});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean saveRekap(RecapitulationHistory rh) {
        try {
            String query = "INSERT INTO region (id, from_date, to_date, saldo_awal, total_pengeluaran, total_pemasukan, "
                    + "employee_salaries, saldo_akhir, create_date, update_date)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, randomId());
            pre.setObject(2, rh.getFromDate());
            pre.setObject(3, rh.getToDate());
            pre.setObject(4, rh.getSaldoAwal());
            pre.setObject(5, rh.getTotalPengeluaran());
            pre.setObject(6, rh.getTotalPemasukan());
            pre.setObject(7, rh.getEmployeeSalaries());
            pre.setObject(8, rh.getSaldoAkhir());
            pre.setObject(9, rh.getLog().getCreateDate());
            pre.setObject(10, rh.getLog().getUpdateDate());
            pre.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public void setRegionCell(JTable table, int column) {
        try {
            String query = "SELECT r.name AS region from region r ORDER BY r.name";
            PreparedStatement pre = con.prepareStatement(query);

            ResultSet rs = pre.executeQuery();
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            JComboBox comboBox = new JComboBox();
            while (rs.next()) {
                comboBox.addItem(rs.getString("region"));
            }
            tableColumn.setCellEditor(new DefaultCellEditor(comboBox));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void setRegionBySupplierCustomer(String name, JTable table, int column) {
        try {
            String query = "SELECT r.name AS region from region r, supplier_customer sc WHERE sc.region = r.id "
                    + "AND sc.name LIKE ? ORDER BY r.name";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, name);

            ResultSet rs = pre.executeQuery();
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            JComboBox comboBox = new JComboBox();
            while (rs.next()) {
                comboBox.addItem(rs.getString("region"));
            }
            tableColumn.setCellEditor(new DefaultCellEditor(comboBox));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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

    public String getRegionById(String id) {
        try {
            String query = "SELECT * FROM region WHERE id like ? ORDER BY name";
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

    public String getRegionByName(String name) {
        try {
            String query = "SELECT * FROM region WHERE name like ? ORDER BY name";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, name);

            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                return rs.getString("id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public void setRegions(JComboBox comboBox) {
        try {
            String query = "SELECT * FROM region ORDER BY name";
            PreparedStatement pre = con.prepareStatement(query);

            ResultSet rs = pre.executeQuery();
            comboBox.removeAllItems();
            comboBox.addItem("Wilayah");
            while (rs.next()) {
                comboBox.addItem(rs.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void setRegions(String wilayah) {
        try {
            DefaultTableModel model = (DefaultTableModel) tblWilayahResult.getModel();
            String query = "SELECT * FROM region ";
            if (!wilayah.equalsIgnoreCase("")) {
                query += "WHERE name like ? ";
            }

            query += "ORDER BY name";
            PreparedStatement pre = con.prepareStatement(query);
            if (!wilayah.equalsIgnoreCase("")) {
                pre.setObject(1, wilayah);
            }

            ResultSet rs = pre.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{"R" + rs.getInt("code"), rs.getString("name")});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean saveRegion(Region region) {
        try {
            String query = "INSERT INTO region (id, name, create_date, update_date, code)"
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, randomId());
            pre.setObject(2, region.getName());
            pre.setObject(3, region.getLog().getCreateDate());
            pre.setObject(4, region.getLog().getUpdateDate());
            pre.setObject(5, region.getCode());
            pre.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public long getTotalPengeluaranPemasukan(Date dateFrom, Date dateTo, int type) {
        try {
            String query = "select sum(p.payment) as payment "
                    + "from transaction t, pelunasan p "
                    + "where t.id=p.transaction_id and t.transaction_type = ? "
                    + "and (p.create_date between ? and ?)";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, type);
            pre.setObject(2, dateFrom);
            pre.setObject(3, dateTo);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                return rs.getInt("payment");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public long findStocksNotCampurByDate(Date dateFrom, Date dateTo, int type) {
        try {
            String query = "SELECT SUM(td.quantity) AS total_stocks FROM transaction t, transaction_detail td "
                    + "WHERE t.id = td.transaction_id AND t.transaction_type = ? "
                    + "AND (t.create_date BETWEEN ? AND ?)";

            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, type);
            pre.setObject(2, dateFrom);
            pre.setObject(3, dateTo);
            System.out.println(pre.toString());
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                return rs.getLong("total_stocks");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public long findItemStocksByDate(Date dateFrom, Date dateTo, int type, String itemId) {
        try {
            String query = "SELECT SUM(td.quantity) AS total_stocks FROM transaction t, transaction_detail td "
                    + "WHERE t.id = td.transaction_id AND t.transaction_type = ? "
                    + "AND td.inventory_category_id = ? AND (t.create_date BETWEEN ? AND ?)";

            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, type);
            pre.setObject(2, itemId);
            pre.setObject(3, dateFrom);
            pre.setObject(4, dateTo);

            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                return rs.getLong("total_stocks");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public String getTransactionIdForPelunasan(int type, Date createDate, String name) {
        try {

            String query = "SELECT t.id AS id FROM transaction t, transaction_detail td, supplier_customer sc "
                    + "WHERE td.supplier_customer_id = sc.id AND td.transaction_id = t.id "
                    + "AND t.create_date LIKE ? AND sc.name LIKE ? AND t.transaction_type = ? ";

            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, createDate);
            pre.setObject(2, name);
            pre.setObject(3, type);
            System.out.println(pre.toString());
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                return rs.getString("id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public boolean findTransactionByCode(Date date, String code) {
        try {
            String query = "select * from transaction where code like ? and create_date = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, code);
            pre.setObject(2, date);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void findAllTransaction(int type, Date dateFrom, Date dateTo, JTable table, int paymentType,
            String name, String wilayah) {
        try {

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            String query = "SELECT t.create_date AS create_date, t.update_date AS update_date, sc.name AS name, t.nota AS nota, "
                    + "t.invoices AS invoices, t.total_prices AS totals, t.total_market_prices as total_market_prices, t.code as code "
                    + "FROM transaction t, supplier_customer sc, transaction_detail td, region r "
                    + "WHERE sc.id = td.supplier_customer_id AND t.id = td.transaction_id AND sc.region = r.id ";

            if (type != 2) {
                query += "AND t.transaction_type = ? ";
            }

            if ((dateFrom != null) && dateTo != null) {
                query += "AND (t.create_date BETWEEN ? AND ?) ";
            }

            if (paymentType == Transaction.PaymentStatus.DEBT.ordinal()) {
                query += "AND t.payment_type LIKE ? ";
            }

            if ((!name.equalsIgnoreCase("Supplier"))
                    && (!name.equalsIgnoreCase("Customer"))) {
                query += "AND sc.name LIKE ? ";
            }

            if (!wilayah.equalsIgnoreCase("Wilayah")) {
                query += "AND r.name LIKE ? ";
            }

            query += "GROUP BY t.code ORDER BY t.create_date ASC";
            PreparedStatement pre = con.prepareStatement(query);
            int i = 0;
            if (type != 2) {
                i += 1;
                pre.setObject(i, type);
            }
            if ((dateFrom != null) && dateTo != null) {
                i += 1;
                pre.setObject(i, dateFrom);
                i += 1;
                pre.setObject(i, dateTo);
            }
            if (paymentType == Transaction.PaymentStatus.DEBT.ordinal()) {
                i += 1;
                pre.setObject(i, paymentType);
            }
            if ((!name.equalsIgnoreCase("Supplier"))
                    && (!name.equalsIgnoreCase("Customer"))) {
                i += 1;
                pre.setObject(i, name);
            }

            if (!wilayah.equalsIgnoreCase("Wilayah")) {
                i += 1;
                pre.setObject(i, wilayah);
            }

            ResultSet rs = pre.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                if (type == 0) {
                    model.addRow(new Object[]{(rs.getDate("create_date") == null ? "" : df.format(rs.getDate("create_date"))),
                        "T" + rs.getString("code"), (rs.getDate("update_date") == null ? "" : df.format(rs.getDate("update_date"))),
                        rs.getString("name"), nf.format(rs.getLong("totals"))});
                } else {
                    model.addRow(new Object[]{(rs.getDate("create_Date") == null ? "" : df.format(rs.getDate("create_date"))),
                        "T" + rs.getString("code"), rs.getString("nota"), rs.getString("name"), rs.getString("invoices"), nf.format(rs.getLong("totals")), nf.format(rs.getLong("total_market_prices"))});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void findAllTransactionDebt(int type, Date dateFrom, Date dateTo, JTable table, int paymentType) {
        try {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            String query = "SELECT t.create_date AS create_date, t.update_date AS update_date, sc.name AS name, "
                    + "t.total_prices AS totals, t.code "
                    + "FROM transaction t, supplier_customer sc, transaction_detail td "
                    + "WHERE sc.id = td.supplier_customer_id AND t.id = td.transaction_id ";
            if (type != 2) {
                query += "AND t.transaction_type LIKE ? ";
            }

            if ((dateFrom != null) && dateTo != null) {
                query += "AND (t.create_date BETWEEN ? AND ?) ";
            }

            if (paymentType == Transaction.PaymentStatus.DEBT.ordinal()) {
                query += "AND t.payment_type LIKE ? ";
            }

            query += "GROUP BY t.create_date, sc.name ORDER BY t.create_date;";
            PreparedStatement pre = con.prepareStatement(query);
            int i = 0;
            if (type != 2) {
                i += 1;
                pre.setObject(i, type);
            }
            if ((dateFrom != null) && dateTo != null) {
                i += 1;
                pre.setObject(i, dateFrom);
                i += 1;
                pre.setObject(i, dateTo);
            }
            if (paymentType == Transaction.PaymentStatus.DEBT.ordinal()) {
                i += 1;
                pre.setObject(i, paymentType);
            }

            ResultSet rs = pre.executeQuery();
            System.out.println(pre.toString());
            model.setRowCount(0);
            while (rs.next()) {
                if (type == 0) {
                    model.addRow(new Object[]{(rs.getDate("create_Date") == null ? "" : df.format(rs.getDate("create_date"))),
                        rs.getString("code"), (rs.getDate("update_Date") == null ? "" : df.format(rs.getDate("update_date"))),
                        rs.getString("name"), nf.format(rs.getLong("totals"))});
                } else {
                    String region = getRegionIdBySupplierCustomer(rs.getString("supplier_customer_id"));
                    model.addRow(new Object[]{(rs.getDate("create_Date") == null ? "" : df.format(rs.getDate("create_date"))),
                        rs.getString("code"), rs.getString("name"), region,
                        rs.getString("invoices"), nf.format(rs.getLong("payment")), rs.getLong("plu"),
                        (rs.getInt("payment_type") == 0 ? "Lunas" : "Hutang")
                        + (rs.getObject("description") == null ? "" : ", " + rs.getObject("description"))});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void findAllTransactionDetail(JTable table, String code, int type) {
        try {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            String query = "SELECT td.code as tdcode, td.item_partai AS partai, ic.name as item, td.quantity AS qty, td.price AS price, "
                    + "td.total_prices AS prices, r.name AS region, t.tebasan as istebasan "
                    + "FROM transaction t, transaction_detail td, inventory_category ic, supplier_customer sc, region r "
                    + "WHERE sc.region = r.id AND td.supplier_customer_id = sc.id AND t.id = td.transaction_id "
                    + "AND td.inventory_category_id = ic.id AND t.code = ? order by td.code asc";

            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, code);

            ResultSet rs = pre.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("tdcode"), (type == 0 ? rs.getString("item") : rs.getString("partai")), rs.getString("qty"),
                    (table.getName().equalsIgnoreCase("ubah") ? rs.getLong("price") : nf.format(rs.getLong("price"))),
                    nf.format(rs.getLong("prices")), rs.getString("region")});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String saveTransaksi(Transaction transaction) {
        try {
            String query = "INSERT INTO transaction (id, create_date, update_date, transaction_type,"
                    + " invoices, payment_type, total_prices, description, nota, code, tebasan)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            String id = randomId();
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, id);
            pre.setObject(2, transaction.getLog().getCreateDate());
            pre.setObject(3, transaction.getLog().getUpdateDate());
            pre.setObject(4, transaction.getTransactionType().ordinal());
            pre.setObject(5, transaction.getInvoices());
            pre.setObject(6, transaction.getPaymentStatus().ordinal());
            pre.setObject(7, transaction.getTotalPrices());
            pre.setObject(8, transaction.getDescription());
            pre.setObject(9, transaction.getNota());
            pre.setObject(10, transaction.getCode());
            pre.setObject(11, transaction.isTebasan());
            pre.execute();
            return id;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public boolean saveTransaksiDetail(Transaction transaction) {
        try {
            String query = "INSERT INTO transaction_detail (id, create_date, update_date, quantity, price, total_prices,"
                    + " description, supplier_customer_id, inventory_category_id, plu, item_partai, transaction_id, sisa_stock, code)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, randomId());
            pre.setObject(2, transaction.getLog().getCreateDate());
            pre.setObject(3, transaction.getLog().getUpdateDate());
            pre.setObject(4, transaction.getQuantity());
            pre.setObject(5, transaction.getPrice());
            pre.setObject(6, transaction.getTotalPrices());
            pre.setObject(7, transaction.getDescription());
            pre.setObject(8, transaction.getSupplierCustomerId());
            pre.setObject(9, transaction.getInventoryCategoryId());
            pre.setObject(10, transaction.getPlu());
            pre.setObject(11, transaction.getItemPartai());
            pre.setObject(12, transaction.getId());
            pre.setObject(13, transaction.getsisaStock());
            pre.setObject(14, transaction.getNo());
            pre.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateTransaksiById(String id, int type, long totals) {
        try {
            String query = "UPDATE transaction SET payment_type = ?, total_prices = ? WHERE id = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, type);
            pre.setObject(2, totals);
            pre.setObject(3, id);
            System.out.println(pre.toString());
            pre.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public int getStokItemByName(String name) {
        try {
            String query = "SELECT * FROM inventory_category WHERE name = ? ORDER BY name";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, name);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                return rs.getInt("total_items");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public void setCategories(JTable table, int column) {
        try {
            String query = "SELECT name FROM inventory_category ORDER BY name";
            PreparedStatement pre = con.prepareStatement(query);
            ResultSet rs = pre.executeQuery();
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            JComboBox comboBox = new JComboBox();
            while (rs.next()) {
                comboBox.addItem(rs.getString("name"));
            }
            tableColumn.setCellEditor(new DefaultCellEditor(comboBox));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void setCategories(JComboBox jComboBox) {
        try {
            String query = "SELECT * FROM inventory_category ORDER BY name";
            PreparedStatement pre = con.prepareStatement(query);
            ResultSet rs = pre.executeQuery();
            jComboBox.removeAllItems();
            while (rs.next()) {
                jComboBox.addItem(rs.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void findAllCategorieStock(Date dateFrom, Date dateTo) {
        try {
            DefaultTableModel model = (DefaultTableModel) tblSisaItem.getModel();
            String query = "SELECT * FROM inventory_category ORDER BY name";
            PreparedStatement pre = con.prepareStatement(query);
            ResultSet rs = pre.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                if (rs.getString("name").equalsIgnoreCase("CAMPUR")) {
                    model.addRow(new Object[]{rs.getString("name"), rs.getString("item_code"),
                        findItemStocksByDate(dateFrom, dateTo, 0, rs.getString("id"))
                        - findItemStocksByDate(dateFrom, dateTo, 1, rs.getString("id"))
                        - findStocksNotCampurByDate(dateFrom, dateTo, 3)});
                } else {
                    model.addRow(new Object[]{rs.getString("name"), rs.getString("item_code"),
                        findItemStocksByDate(dateFrom, dateTo, 0, rs.getString("id"))
                        - findItemStocksByDate(dateFrom, dateTo, 1, rs.getString("id"))
                        + findItemStocksByDate(dateFrom, dateTo, 3, rs.getString("id"))});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void findCategories(String name) {
        try {
            DefaultTableModel model = (DefaultTableModel) tblKategoriResult.getModel();
            String query = "SELECT * FROM inventory_category ";
            if (!name.equalsIgnoreCase("")) {
                query += "WHERE name like ? ";
            }

            query += "ORDER BY name";
            PreparedStatement pre = con.prepareStatement(query);
            if (!name.equalsIgnoreCase("")) {
                pre.setObject(1, name);
            }

            ResultSet rs = pre.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("name"),
                    rs.getString("item_code")});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getCategoryById(String id) {
        try {
            String query = "SELECT * FROM inventory_category WHERE id = ? ORDER BY name";
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

    public InventoryCategory findCategoryIdByName(String name) {
        try {
            String query = "SELECT * FROM inventory_category WHERE name = ? ORDER BY name";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, name);

            ResultSet rs = pre.executeQuery();
            InventoryCategory ic = new InventoryCategory();
            while (rs.next()) {
                ic.setId(rs.getString("id"));
                ic.setTotalItems(rs.getInt("total_items"));
                return ic;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean saveCategory(InventoryCategory category) {
        try {
            String query = "INSERT INTO inventory_category (id, create_date, update_date, name, item_code, total_items, category_parent_id)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, randomId());
            pre.setObject(2, category.getLog().getCreateDate());
            pre.setObject(3, category.getLog().getUpdateDate());
            pre.setObject(4, category.getName());
            pre.setObject(5, category.getItemCode());
            pre.setObject(6, category.getTotalItems());
            pre.setObject(7, category.getParrentId());
            pre.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public String updateInventoryById(String id, double total) {
        try {
            String query = "UPDATE inventory_category SET total_items = ? WHERE id = ?";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, total);
            pre.setObject(2, id);
            pre.execute();
            return id;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public int findInventoriesByName(String id) {
        try {
            String query = "SELECT * FROM inventory_category WHERE id like ? ORDER BY name";

            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, id);

            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                return rs.getInt("total_items");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public void findInventories(String name) {
        try {
            DefaultTableModel model = (DefaultTableModel) tblStokBarangResult.getModel();
            String query = "SELECT * FROM inventory_category ";
            if (!name.equalsIgnoreCase("")) {
                query += "WHERE name like ? ";
            }

            query += "ORDER BY name";
            PreparedStatement pre = con.prepareStatement(query);
            if (!name.equalsIgnoreCase("")) {
                pre.setObject(1, name);
            }

            ResultSet rs = pre.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("name"),
                    rs.getString("item_code"), rs.getString("total_items")});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
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

    public void setSuppliersCustomers(SupplierCustomer.SupplierCustomerType type, JComboBox comboBox) {
        try {
            String query = "SELECT DISTINCT(name) FROM supplier_customer WHERE type = ? ORDER BY name";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, type.ordinal());
            ResultSet rs = pre.executeQuery();
            comboBox.removeAllItems();
            if (type == SupplierCustomer.SupplierCustomerType.SUPPLIER) {
                comboBox.addItem("Supplier");
            } else {
                comboBox.addItem("Customer");
            }
            while (rs.next()) {
                comboBox.addItem(rs.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String findSupplierCustomerByName(SupplierCustomer.SupplierCustomerType type, String name, String region) {
        try {
            String query = "SELECT sc.id AS id FROM supplier_customer sc, region r WHERE sc.region = r.id "
                    + "AND sc.type = ? AND sc.name LIKE ? AND r.name LIKE ? ORDER BY sc.name";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, type.ordinal());
            pre.setObject(2, name);
            pre.setObject(3, region);
            System.out.println(pre.toString());
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                return rs.getString("id").toString();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean saveSupplierCustomer(SupplierCustomer supplierCustomer) {
        try {
            String query = "INSERT INTO supplier_customer (id, name, address, create_date, update_date, phone, price, description, type, region, code)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, randomId());
            pre.setObject(2, supplierCustomer.getName());
            pre.setObject(3, supplierCustomer.getAddress());
            pre.setObject(4, supplierCustomer.getLog().getCreateDate());
            pre.setObject(5, supplierCustomer.getLog().getUpdateDate());
            pre.setObject(6, supplierCustomer.getPhone());
            pre.setObject(7, supplierCustomer.getPrice());
            pre.setObject(8, supplierCustomer.getDescription());
            pre.setObject(9, supplierCustomer.getSupplierCustomerType().ordinal());
            pre.setObject(10, supplierCustomer.getRegionId());
            pre.setObject(11, supplierCustomer.getCode());
            pre.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public void findSupplierOrCustomer(SupplierCustomer.SupplierCustomerType type, String name) {
        try {
            DefaultTableModel model = (DefaultTableModel) tblResult.getModel();
            String query = "SELECT * FROM supplier_customer WHERE type = ? ";
            if (!name.equalsIgnoreCase("")) {
                query += "AND name like ? ";
            }
            query += "ORDER BY name";

            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, type.ordinal());
            if (!name.equalsIgnoreCase("")) {
                pre.setObject(2, "%" + name + "%");
            }

            ResultSet rs = pre.executeQuery();
            model.setRowCount(0);
            int row = 0;
            while (rs.next()) {
                model.addRow(new Object[]{"SC" + rs.getString("code"), rs.getString("name"),
                    rs.getString("phone"), rs.getString("address"),
                    getRegionById(rs.getString("region"))
                });
                row++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String randomId() {
        String id = UUID.randomUUID().toString().replace("-", "");
        return id;
    }

    public void resetField(int frame) {
        switch (frame) {
            case 1:
                fldName.setText(null);
                fldAlamat.setText(null);
                fldKeterangan.setText(null);
                fldTelp.setText(null);
                fldCari.setText(null);
                break;
            case 2:
                fldNamaKategori.setText(null);
                fldKodeBarang.setText(null);
                fldCariKategori.setText(null);
                break;
            case 3:
                fldJumlahTambah.setText("0");
                fldCariStokBarang.setText(null);
                break;
            case 4:
                fldWilayahBaru.setText(null);
                fldCariWilayah.setText(null);
                break;
        }
    }

    public boolean fieldValidation(int frame) {
        List<String> errors = new ArrayList<String>();
        switch (frame) {
            case 1:
                //supplier/customer
                if (fldName.getText().equalsIgnoreCase("")) {
                    errors.add("Nama harus diisi");
                }
                if (fldTelp.getText().equalsIgnoreCase("")) {
                    errors.add("No Telp harus diisi");
                }
                if (fldAlamat.getText().equalsIgnoreCase("")) {
                    errors.add("Alamat harus diisi");
                }
            case 2:
                //kategori
                if (fldNamaKategori.getText().equalsIgnoreCase("")) {
                    errors.add("Nama harus diisi");
                }
                if (fldKodeBarang.getText().equalsIgnoreCase("")) {
                    errors.add("Kode harus diisi");
                }
            case 3:
                //wilayah
                if (fldWilayahBaru.getText().equalsIgnoreCase("")) {
                    errors.add("Nama wilayah harus diisi");
                }
        }
        return errors.isEmpty();
    }

    public List<Region> listRegion(int type) {
        try {
            Region r = new Region();
            List<Region> list = new ArrayList<Region>();
            String query = "select r.id as id, r.name as name from region r, supplier_customer sc where r.id = sc.region "
                    + "and sc.type = ? group by r.name order by r.name";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, type);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                r.setId(rs.getString("id"));
                r.setName(rs.getString("name"));
                list.add(r);
            }
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<SupplierCustomer> listSupplier(int type) {
        try {
            List<SupplierCustomer> list = new ArrayList<SupplierCustomer>();
            String query = "select id, name, region from supplier_customer where type = ? order by name";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, type);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                SupplierCustomer sc = new SupplierCustomer();
                sc.setId(rs.getString("id"));
                sc.setName(rs.getString("name"));
                sc.setRegionId(rs.getString("region"));
                list.add(sc);
            }
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Transaction> listTransaction(int type, SupplierCustomer sc, Date dateFrom, Date dateTo) {
        try {
            List<Transaction> list = new ArrayList<Transaction>();
            String query = "select t.create_date as create_date, sc.name as name, ic.id as itemId, td.item_partai as partai, "
                    + "td.price as price, td.quantity as qty, td.total_prices as total "
                    + "from supplier_customer sc, region r, transaction t, transaction_detail td, inventory_category ic "
                    + "where sc.region = r.id and t.id = td.transaction_id and td.supplier_customer_id = sc.id "
                    + "and td.inventory_category_id = ic.id and t.transaction_type = ?  and r.id like ? and sc.id like ? "
                    + "and (t.create_date between ? and ?) "
                    + "order by t.create_date, ic.name asc";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, type);
            pre.setObject(2, sc.getRegionId());
            pre.setObject(3, sc.getId());
            pre.setObject(4, dateFrom);
            pre.setObject(5, dateTo);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setPrice(rs.getDouble("price"));
                t.setQuantity(rs.getDouble("qty"));
                t.setTotalPrice(rs.getDouble("total"));
                t.setItemPartai(rs.getString("partai"));
                t.getLog().setCreateDate(rs.getDate("create_date"));
                t.setInventoryCategoryId(rs.getString("itemId"));
                list.add(t);
            }
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Long> totalTransactionPerRegion(int type, SupplierCustomer sc, Date dateFrom, Date dateTo) {
        try {
            String query = "select sum(if(total_market_prices is null, total_prices, "
                    + "if(total_market_prices = 0, total_prices, total_market_prices))) as total_prices, "
                    + "(select sum(td.quantity) from transaction t "
                    + "join transaction_detail td on (td.transaction_id = t.id) "
                    + (sc != null ? "join supplier_customer sc on (td.supplier_customer_id = sc.id)" : "")
                    + "where t.transaction_type = ? "
                    + (sc != null ? "and sc.region like ? and sc.id like ? " : "")
                    + "and (t.create_date between ? and ?)) as total_qty "
                    + "from transaction "
                    + "where id in (select distinct t.id "
                    + "from transaction t "
                    + "join transaction_detail td on (td.transaction_id = t.id) "
                    + (sc != null ? "join supplier_customer sc on (td.supplier_customer_id = sc.id)" : "")
                    + "where t.transaction_type = ? "
                    + (sc != null ? "and sc.region like ? and sc.id like ? " : "")
                    + "and (t.create_date between ? and ?))";
            PreparedStatement pre = con.prepareStatement(query);
            if (sc != null) {
                pre.setObject(1, type);
                pre.setObject(2, sc.getRegionId());
                pre.setObject(3, sc.getId());
                pre.setObject(4, dateFrom);
                pre.setObject(5, dateTo);
                pre.setObject(6, type);
                pre.setObject(7, sc.getRegionId());
                pre.setObject(8, sc.getId());
                pre.setObject(9, dateFrom);
                pre.setObject(10, dateTo);
            } else if (sc == null) {
                pre.setObject(1, type);
                pre.setObject(2, dateFrom);
                pre.setObject(3, dateTo);
                pre.setObject(4, type);
                pre.setObject(5, dateFrom);
                pre.setObject(6, dateTo);
            }
            ResultSet rs = pre.executeQuery();
            List<Long> jList = new ArrayList<Long>();
            while (rs.next()) {
                jList.add(rs.getLong("total_qty"));
                jList.add(rs.getLong("total_prices"));
                return jList;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public double totalTransactionBuyOrSell(int type, Date dateFrom, Date dateTo) {
        try {
            String query = "select sum(if(total_market_prices is null, total_prices, "
                    + "if(total_market_prices = 0, total_prices, total_market_prices) "
                    + ")) as total_prices from transaction "
                    + "where id in (select distinct t.id "
                    + "from transaction t "
                    + "join transaction_detail td on (td.transaction_id = t.id) "
                    + "join supplier_customer sc on (td.supplier_customer_id = sc.id) "
                    + "where t.transaction_type = ? and (t.create_date between ? and ?))";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, type);
            pre.setObject(2, dateFrom);
            pre.setObject(3, dateTo);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                return rs.getDouble("total_prices");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public List<Transaction> biayaPacking(int type) {
        try {
            List<Transaction> list = new ArrayList<Transaction>();
            String query = "select td.price as price, td.item_partai as partai, td.quantity as qty, td.total_prices as total_prices "
                    + "from transaction t, transaction_detail td "
                    + "where t.id = td.transaction_id and t.transaction_type = ? "
                    + "order by t.create_date limit 1";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, type);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setItemPartai(rs.getString("partai"));
                t.setQuantity(rs.getInt("qty"));
                t.setPrice(rs.getInt("price"));
                t.setTotalPrice(rs.getInt("total_prices"));
                list.add(t);
            }
            return list;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Transaction getBiayaPacking(int transactionType) {
        Transaction t = new Transaction();
        try {
            String query = "select id, total_prices from transaction where transaction_type = ? "
                    + "order by create_date desc limit 1";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, transactionType);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                t.setId(rs.getString("id"));
                t.setTotalPrice(rs.getDouble("total_prices"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return t;
    }

    public List<Transaction> getDetailPacking(String transactionId) {
        try {
            String query = "select quantity, total_prices, price, item_partai "
                    + "from transaction_detail "
                    + "where transaction_id like ? "
                    + "order by code asc";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, transactionId);
            ResultSet rs = pre.executeQuery();
            List<Transaction> list = new ArrayList<Transaction>();
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setItemPartai(rs.getString("item_partai"));
                t.setPrice(rs.getDouble("price"));
                t.setQuantity(rs.getDouble("quantity"));
                t.setTotalPrice(rs.getDouble("total_prices"));
                list.add(t);
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<InventoryCategory> getItemFromTransaction(Date dateFrom, Date dateTo) {
        try {
            String query = "select ic.name as item, ic.id as id "
                    + "from transaction_detail td, inventory_category ic "
                    + "where td.inventory_category_id = ic.id and (td.create_date between ? and ?) "
                    + "group by ic.name order by ic.name";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, dateFrom);
            pre.setObject(2, dateTo);
            ResultSet rs = pre.executeQuery();
            List<InventoryCategory> list = new ArrayList<InventoryCategory>();
            while (rs.next()) {
                InventoryCategory ic = new InventoryCategory();
                ic.setId(rs.getString("id"));
                ic.setName(rs.getString("item"));
                list.add(ic);
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Transaction> getItemTransaction(Date dateFrom, Date dateTo, String itemId) {
        try {
            String query = "select t.code as code, td.create_date as create_date, t.transaction_type as type, td.total_prices as total_prices, "
                    + "td.supplier_customer_id as supplier_customer_id, td.quantity as qty "
                    + "from transaction t, transaction_detail td, inventory_category ic "
                    + "where t.id = td.transaction_id and td.inventory_category_id = ic.id and ic.id like ? "
                    + "and (td.create_date between ? and ?) "
                    + "order by td.create_date";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, itemId);
            pre.setObject(2, dateFrom);
            pre.setObject(3, dateTo);
            ResultSet rs = pre.executeQuery();
            List<Transaction> list = new ArrayList<Transaction>();
            while (rs.next()) {
                Transaction td = new Transaction();
                td.getLog().setCreateDate(rs.getDate("create_date"));
                td.setTransactionType(rs.getInt("type") == 0 ? Transaction.TransactionType.BUY : Transaction.TransactionType.SELL);
                td.setQuantity(rs.getDouble("qty"));
                td.setSupplierCustomerId(rs.getString("supplier_customer_id"));
                td.setTotalPrice(rs.getDouble("total_prices"));
                td.setCode(rs.getString("code"));
                list.add(td);
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<InventoryCategory> getStockGudang() {
        try {
            String query = "select name, total_items from inventory_category";
            PreparedStatement pre = con.prepareStatement(query);
            ResultSet rs = pre.executeQuery();
            List<InventoryCategory> list = new ArrayList<InventoryCategory>();
            while (rs.next()) {
                InventoryCategory ic = new InventoryCategory();
                ic.setName(rs.getString("name"));
                ic.setTotalItems(rs.getDouble("total_items"));
                list.add(ic);
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Transaction> getTotalPricesPerSupplierCustomer(Date dateFrom, Date dateTo, int type) {
        try {
            String query = "select td.supplier_customer_id as sc_id, sum(td.total_prices) as total "
                    + "from transaction t, transaction_detail td "
                    + "where t.id = td.transaction_id and t.transaction_type = ? and (t.create_date between ? and ?) "
                    + "group by td.supplier_customer_id";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, type);
            pre.setObject(2, dateFrom);
            pre.setObject(3, dateTo);
            ResultSet rs = pre.executeQuery();
            List<Transaction> list = new ArrayList<Transaction>();
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setSupplierCustomerId(rs.getString("sc_id"));
                t.setTotalPrice(rs.getDouble("total"));
                list.add(t);
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Transaction> getListTransaction(int transactionType, String scId, Date dateFrom, Date dateTo) {
        try {
            String query = "";
            if (transactionType == 0) {
                query = "select t.create_date as create_date, sc.name as name, t.nota as nota, sum(td.quantity) as qty, "
                        + "if (t.total_market_prices is null, t.total_prices, t.total_market_prices ) as total "
                        + "from transaction t inner join transaction_detail td on (t.id=td.transaction_id) "
                        + "inner join supplier_customer sc on (sc.id=td.supplier_customer_id) "
                        + "inner join inventory_category ic on (ic.id=td.inventory_category_id) "
                        + "where t.transaction_type = ? and sc.id = ? and (t.create_date between ? and ?)"
                        + "group by sc.name, t.create_date, t.code"
                        + "order by sc.name, t.create_date, ic.name;";
            }
            if (transactionType == 1) {
                query = "select t.create_date as create_date, sc.name as name, t.nota as nota, sum(td.quantity) as qty, "
                        + " t.total_prices AS totals, t.total_market_prices as total_market_prices "
                        + "from transaction t inner join transaction_detail td on (t.id=td.transaction_id) "
                        + "inner join supplier_customer sc on (sc.id=td.supplier_customer_id) "
                        + "inner join inventory_category ic on (ic.id=td.inventory_category_id) "
                        + "where t.transaction_type = ? and sc.id = ? and (t.create_date between ? and ?)"
                        + "group by sc.name, t.create_date, t.nota, t.code "
                        + "order by sc.name, t.create_date, t.nota, ic.name;";
            }
            
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, transactionType);
            pre.setObject(2, scId);
            pre.setObject(3, dateFrom);
            pre.setObject(4, dateTo);
            ResultSet rs = pre.executeQuery();
            List<Transaction> list = new ArrayList<Transaction>();
            while (rs.next()) {
                Transaction t = new Transaction();
                t.getLog().setCreateDate(rs.getDate("create_date"));
                t.setSupplierCustomerId(rs.getString("name"));
                t.setNota(rs.getString("nota"));
                t.setQuantity(rs.getDouble("qty"));
                if ((rs.getObject("total_market_prices") == null) || (rs.getDouble("total_market_prices") == 0)) {
                    t.setTotalPrice(rs.getDouble("totals"));
                } else {
                    t.setTotalPrice(rs.getDouble("total_market_prices"));
                }
                list.add(t);
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public PdfPTable printBuying(int transactionType, int scType, Date dateFrom, Date dateTo) {
        PdfPTable pdfTable = new PdfPTable(7);
        try {
            font = FontFactory.getFont("verdana", 13, com.itextpdf.text.Font.BOLD);
            pdfTable.setWidthPercentage(100);
            pdfTable.setWidths(new int[]{2, 2, 2, 1, 2, 1, 2});
            pdfTable.getDefaultCell().setPadding(2);
            pdfTable.getDefaultCell().setBorder(0);
            PdfPCell cell = new PdfPCell(new Phrase(Chunk.NEWLINE + (scType == 0 ? "PEMBELIAN" : "PENJUALAN") + " :", font));
            cell.setColspan(7);
            cell.setBorder(0);
            pdfTable.addCell(cell);
            cell.setPhrase(new Phrase(Chunk.NEWLINE));
            pdfTable.addCell(cell);
            List<SupplierCustomer> scList = listSupplier(scType);
            long totalPrices = 0;
            long totalQty = 0;
            for (SupplierCustomer sc : scList) {
                List<Transaction> tList = listTransaction(transactionType, sc, dateFrom, dateTo);
                font = FontFactory.getFont("verdana", 13, com.itextpdf.text.Font.BOLD);
                pdfTable.getDefaultCell().setPadding(2);
                if (!tList.isEmpty()) {
                    String region = getRegionById(sc.getRegionId());
                    cell.setPhrase(new Phrase(sc.getName() + (!region.equalsIgnoreCase("NONE") ? " / " + region : ""), font));
                    cell.setColspan(7);
                    cell.setPadding(2);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    pdfTable.addCell(cell);
                }

                font = FontFactory.getFont("verdana", 11, com.itextpdf.text.Font.NORMAL);
                int i = 0;
                String date = "";
                for (Transaction t : tList) {
                    String createDate = df.format(t.getLog().getCreateDate());
                    String item = getCategoryById(t.getInventoryCategoryId());
                    if (i == 0) {
                        pdfTable.getDefaultCell().setBorder(1);
                    } else {
                        pdfTable.getDefaultCell().setBorder(0);
                    }
                    pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                    pdfTable.addCell(new Phrase(createDate.equalsIgnoreCase(date) ? " " : createDate, font));
                    if (transactionType == 0) {
                        pdfTable.addCell(new Phrase(item));
                    } else {
                        pdfTable.addCell(new Phrase(t.getItemPartai(), font));
                    }
                    pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                    pdfTable.addCell(new Phrase(nf.format(t.getQuantity()).replace(",00", "") + " Kg", font));
                    pdfTable.addCell(new Phrase("Rp", font));
                    pdfTable.addCell(new Phrase((nf.format(t.getPrice())).replace(",00", ""), font));
                    pdfTable.addCell(new Phrase("Rp", font));
                    pdfTable.addCell(new Phrase((nf.format(t.getTotalPrices()).replace(",00", "")), font));
                    date = createDate;
                    i++;
                }
                font = FontFactory.getFont("verdana", 11, com.itextpdf.text.Font.BOLD);
                if (!tList.isEmpty()) {
                    List<Long> jList = totalTransactionPerRegion(transactionType, sc, dateFrom, dateTo);
                    cell.setPhrase(new Phrase("TOTAL", font));
                    cell.setColspan(2);
                    cell.setBorder(1);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    pdfTable.addCell(cell);

                    cell.setPhrase(new Phrase(nf.format(jList.get(0)).replace(",00", "") + " Kg", font));
                    cell.setColspan(1);
                    pdfTable.addCell(cell);

                    cell.setPhrase(new Phrase("Rp", font));
                    cell.setColspan(3);
                    pdfTable.addCell(cell);

                    cell.setPhrase(new Phrase(nf.format(jList.get(1)).replace(",00", ""), font));
                    pdfTable.addCell(cell);

                    cell.setPhrase(new Phrase(Chunk.NEWLINE));
                    cell.setColspan(7);
                    cell.setBorder(0);
                    pdfTable.addCell(cell);
                    cell.setPhrase(new Phrase(Chunk.NEWLINE));
                    pdfTable.addCell(cell);

                    totalQty += jList.get(0);
                    totalPrices += jList.get(1);
                }
            }

            //TotaL Keseluruhan Pembelian
            font = FontFactory.getFont("verdana", 14, com.itextpdf.text.Font.BOLD);
            List<Long> jList = totalTransactionPerRegion(transactionType, null, dateFrom, dateTo);
            cell.setPhrase(new Phrase("JUMLAH TOTAL", font));
            cell.setColspan(2);
            cell.setBorder(1);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pdfTable.addCell(cell);

            cell.setPhrase(new Phrase(nf.format(jList.get(0)).replace(",00", "") + " Kg", font));
            cell.setColspan(1);
            pdfTable.addCell(cell);

            cell.setPhrase(new Phrase("Rp", font));
            cell.setColspan(3);
            pdfTable.addCell(cell);

            cell.setPhrase(new Phrase(nf.format(jList.get(1)).replace(",00", ""), font));
            pdfTable.addCell(cell);

            cell.setColspan(7);
            cell.setBorder(0);
            cell.setPhrase(new Phrase(Chunk.NEWLINE));
            pdfTable.addCell(cell);

            totalQty += jList.get(0);
            totalPrices += jList.get(1);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pdfTable;
    }

    public PdfPTable printSelling(int transactionType, int scType, Date dateFrom, Date dateTo) {
        PdfPTable pdfTable = new PdfPTable(6);
        try {
            font = FontFactory.getFont("verdana", 13, com.itextpdf.text.Font.BOLD);
            pdfTable.setWidthPercentage(100);
            pdfTable.setWidths(new int[]{2, 3, 2, 2, 2, 2});
            pdfTable.getDefaultCell().setPadding(2);
            pdfTable.getDefaultCell().setBorder(0);
            PdfPCell cell = new PdfPCell(new Phrase(Chunk.NEWLINE + "PENJUALAN :", font));
            cell.setColspan(6);
            cell.setBorder(0);
            pdfTable.addCell(cell);
            cell.setPhrase(new Phrase(Chunk.NEWLINE));
            pdfTable.addCell(cell);

            List<SupplierCustomer> scList = listSupplier(scType);
            for (SupplierCustomer sc : scList) {
                List<Transaction> tList = getListTransaction(transactionType, sc.getId(), dateFrom, dateTo);
                font = FontFactory.getFont("verdana", 13, com.itextpdf.text.Font.BOLD);
                pdfTable.getDefaultCell().setPadding(2);
                if (!tList.isEmpty()) {
                    String region = getRegionById(sc.getRegionId());
                    cell.setPhrase(new Phrase(sc.getName() + (!region.equalsIgnoreCase("NONE") ? " / " + region : ""), font));
                    cell.setColspan(6);
                    cell.setPadding(2);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    pdfTable.addCell(cell);
                }

                font = FontFactory.getFont("verdana", 11, com.itextpdf.text.Font.NORMAL);
                int i = 0;
                String date = "";
                for (Transaction t : tList) {
                    String createDate = df.format(t.getLog().getCreateDate());
                    if (i == 0) {
                        pdfTable.getDefaultCell().setBorder(1);
                    } else {
                        pdfTable.getDefaultCell().setBorder(0);
                    }
                    pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfTable.addCell(new Phrase(createDate, font));
                    pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                    pdfTable.addCell(new Phrase(t.getSupplierCustomerId(), font));
                    pdfTable.addCell(new Phrase((!t.getNota().equalsIgnoreCase("") ? "NOTA " + t.getNota() : ""), font));
                    pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                    pdfTable.addCell(new Phrase(nf.format(t.getQuantity()).replace(",00", "") + "  Kg", font));
                    pdfTable.addCell(new Phrase("Rp ", font));
                    pdfTable.addCell(new Phrase(nf.format(t.getTotalPrices()).replace(",00", ""), font));
                    i++;
                }
                font = FontFactory.getFont("verdana", 11, com.itextpdf.text.Font.BOLD);
                if (!tList.isEmpty()) {
                    List<Long> jList = totalTransactionPerRegion(transactionType, sc, dateFrom, dateTo);
                    cell.setPhrase(new Phrase("TOTAL", font));
                    cell.setColspan(3);
                    cell.setBorder(1);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    pdfTable.addCell(cell);

                    cell.setPhrase(new Phrase(nf.format(jList.get(0)).replace(",00", "") + " Kg", font));
                    cell.setColspan(1);
                    pdfTable.addCell(cell);

                    cell.setPhrase(new Phrase("Rp", font));
                    cell.setColspan(1);
                    pdfTable.addCell(cell);

                    cell.setPhrase(new Phrase(nf.format(jList.get(1)).replace(",00", ""), font));
                    pdfTable.addCell(cell);

                    cell.setPhrase(new Phrase(Chunk.NEWLINE));
                    cell.setColspan(6);
                    cell.setBorder(0);
                    pdfTable.addCell(cell);
                    cell.setPhrase(new Phrase(Chunk.NEWLINE));
                    pdfTable.addCell(cell);
                }
            }
            //Total Keseluruhan Penjualan
            font = FontFactory.getFont("verdana", 14, com.itextpdf.text.Font.BOLD);
            List<Long> jList = totalTransactionPerRegion(transactionType, null, dateFrom, dateTo);
            cell.setPhrase(new Phrase("JUMLAH TOTAL", font));
            cell.setColspan(3);
            cell.setBorder(1);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pdfTable.addCell(cell);

            cell.setPhrase(new Phrase(nf.format(jList.get(0)).replace(",00", "") + " Kg", font));
            cell.setColspan(1);
            pdfTable.addCell(cell);

            cell.setPhrase(new Phrase("Rp", font));
            cell.setColspan(1);
            pdfTable.addCell(cell);

            cell.setPhrase(new Phrase(nf.format(jList.get(1)).replace(",00", ""), font));
            pdfTable.addCell(cell);

            cell.setColspan(6);
            cell.setBorder(0);
            cell.setPhrase(new Phrase(Chunk.NEWLINE));
            pdfTable.addCell(cell);

            return pdfTable;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pdfTable;
    }

    public PdfPTable printBiayaPacking(Transaction t) {
        PdfPTable pdfTable = new PdfPTable(6);
        try {
            double totalBiayaPacking = t.getTotalPrices();
            font = FontFactory.getFont("verdana", 13, com.itextpdf.text.Font.BOLD);
            pdfTable.setWidthPercentage(100);
            pdfTable.setWidths(new int[]{3, 1, 1, 2, 1, 2});
            pdfTable.getDefaultCell().setPadding(2);
            pdfTable.getDefaultCell().setBorder(0);
            PdfPCell cell = new PdfPCell(new Phrase(Chunk.NEWLINE + "BIAYA PACKING", font));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(6);
            cell.setBorder(0);
            pdfTable.addCell(cell);
            List<Transaction> tList = getDetailPacking(t.getId());
            int i = 0;
            for (Transaction td : tList) {
                font = FontFactory.getFont("verdana", 11, com.itextpdf.text.Font.NORMAL);
                if (i == 0) {
                    pdfTable.getDefaultCell().setBorder(1);
                } else {
                    pdfTable.getDefaultCell().setBorder(0);
                }
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfTable.addCell(new Phrase(td.getItemPartai(), font));
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                pdfTable.addCell(new Phrase((td.getQuantity() > 0 ? nf.format(td.getQuantity()) : "").replace(",00", ""), font));
                pdfTable.addCell(new Phrase(td.getPrice() > 0 ? "X       Rp" : "", font));
                pdfTable.addCell(new Phrase((td.getPrice() > 0 ? nf.format(td.getPrice()).replace(",00", "") : ""), font));
                pdfTable.addCell(new Phrase(td.getTotalPrices() > 0 ? "=       Rp" : "", font));
                pdfTable.addCell(new Phrase((td.getTotalPrices() > 0 ? nf.format(td.getTotalPrices()).replace(",00", "") : ""), font));
                i++;
            }
            if (!tList.isEmpty()) {
                font = FontFactory.getFont("verdana", 11, com.itextpdf.text.Font.BOLD);
                cell.setPhrase(new Phrase("Rp", font));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setColspan(5);
                cell.setBorder(1);
                pdfTable.addCell(cell);

                cell.setPhrase(new Phrase(nf.format(totalBiayaPacking).replace(",00", ""), font));
                cell.setColspan(1);
                pdfTable.addCell(cell);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pdfTable;
    }

    public PdfPTable printKeluarMasukKardus(PdfPTable pdfTable, Date dateFrom, Date dateTo) {
        try {
            List<InventoryCategory> icList = getItemFromTransaction(dateFrom, dateTo);
            PdfPCell cell = new PdfPCell();
            int no = 0;
            for (InventoryCategory ic : icList) {
                font = FontFactory.getFont("verdana", 11, com.itextpdf.text.Font.BOLD);
                cell.setPhrase(new Phrase(""));
                cell.setColspan(1);
                if (no == 0) {
                    cell.setBorder(1);
                } else {
                    cell.setBorder(0);
                }
                pdfTable.addCell(cell);
                cell.setPhrase(new Phrase(Chunk.NEWLINE + ic.getName(), font));
                cell.setColspan(5);
                pdfTable.addCell(cell);

                font = FontFactory.getFont("verdana", 11, com.itextpdf.text.Font.NORMAL);
                List<Transaction> tList = getItemTransaction(dateFrom, dateTo, ic.getId());
                for (Transaction t : tList) {
                    no++;
                    pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfTable.addCell(new Phrase(t.getCode(), font));
                    pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                    pdfTable.addCell(new Phrase(df.format(t.getLog().getCreateDate()), font));
                    pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                    if (t.getTransactionType().ordinal() == 0) {
                        pdfTable.addCell(new Phrase(t.getQuantity() + "", font));
                        pdfTable.addCell("-");
                    } else if (t.getTransactionType().ordinal() == 1) {
                        pdfTable.addCell("-");
                        pdfTable.addCell(new Phrase(t.getQuantity() + "", font));
                    }
                    pdfTable.addCell(new Phrase(nf.format(t.getTotalPrices()), font));
                    pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                    pdfTable.addCell(new Phrase(getSupplierCustomerById(t.getSupplierCustomerId()), font));
                }
            }
            for (int i = 0; i < 2; i++) {
                if (no != 0) {
                    cell.setBorder(1);
                } else {
                    cell.setBorder(0);
                }
                cell.setPhrase(new Phrase(""));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfTable.addCell(cell);
                cell.setPhrase(new Phrase(""));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfTable.addCell(cell);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pdfTable;
    }

    public PdfPTable printStockGudang(PdfPTable pdfTable) {
        try {
            List<InventoryCategory> icList = getStockGudang();
            font = FontFactory.getFont("verdana", 11, com.itextpdf.text.Font.NORMAL);
            int no = 0;
            PdfPCell cell = new PdfPCell();
            cell.setPadding(2);
            for (InventoryCategory ic : icList) {
                if (no == 0) {
                    cell.setBorder(1);
                } else {
                    cell.setBorder(0);
                }
                no++;
                cell.setPhrase(new Phrase(no + "", font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTable.addCell(cell);
                cell.setPhrase(new Phrase(ic.getName(), font));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfTable.addCell(cell);
                cell.setPhrase(new Phrase(nf.format(ic.getTotalItems()).replace(",00", ""), font));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                pdfTable.addCell(cell);
                cell.setPhrase(new Phrase("", font));
                pdfTable.addCell(cell);
            }
            for (int i = 0; i < 2; i++) {
                if (no != 0) {
                    cell.setBorder(1);
                } else {
                    cell.setBorder(0);
                }
                cell.setPhrase(new Phrase(""));
                cell.setColspan(4);
                pdfTable.addCell(cell);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pdfTable;
    }

    public PdfPTable printTotalPricesTransactionPerSupplierCustomer(PdfPTable pdfTable, Date dateFrom, Date dateTo, int type) {
        try {
            List<Transaction> tList = getTotalPricesPerSupplierCustomer(dateFrom, dateTo, type);
            font = FontFactory.getFont("verdana", 11, com.itextpdf.text.Font.NORMAL);
            int no = 0;
            PdfPCell cell = new PdfPCell();
            cell.setPadding(2);
            for (Transaction t : tList) {
                if (no == 0) {
                    cell.setBorder(1);
                } else {
                    cell.setBorder(0);
                }
                no++;
                cell.setPhrase(new Phrase(no + "", font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTable.addCell(cell);
                cell.setPhrase(new Phrase(getSupplierCustomerById(t.getSupplierCustomerId()), font));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfTable.addCell(cell);
                cell.setPhrase(new Phrase(nf.format(t.getTotalPrices()) + "", font));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                pdfTable.addCell(cell);
            }
            for (int i = 0; i < 2; i++) {
                if (no != 0) {
                    cell.setBorder(1);
                } else {
                    cell.setBorder(0);
                }
                cell.setPhrase(new Phrase(""));
                cell.setColspan(3);
                pdfTable.addCell(cell);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pdfTable;
    }

    public void PDFTemplate(String pdfName, float marginLeft, float marginRight, float marginTop, float marginBottom, int type) {
        try {
            Rectangle pagesize = new Rectangle(615f, 930f);
            doc = new Document((type == 0 ? pagesize.rotate() : pagesize), marginLeft, marginRight, marginTop, marginBottom);
            PdfWriter.getInstance(doc, new FileOutputStream(pdfName));
            font = FontFactory.getFont("verdana", 13, com.itextpdf.text.Font.BOLD);
        } catch (Exception ex) {
        }
    }

    public Paragraph setHeaderPage(Date dateFrom, Date dateTo, String title) {
        Paragraph paragraph = new Paragraph();
        paragraph.setAlignment(Element.ALIGN_LEFT);
        font = FontFactory.getFont("verdana", 13, com.itextpdf.text.Font.BOLD);
        paragraph.add(new Phrase("PT. HS. PUTRA" + Chunk.NEWLINE, font));
        font = FontFactory.getFont("verdana", 12, com.itextpdf.text.Font.NORMAL);
        paragraph.add(new Phrase(title + Chunk.NEWLINE, font));
        paragraph.add(Chunk.NEWLINE);
        paragraph.add(new Phrase("Dari Tanggal " + df.format(dateFrom) + " s/d " + df.format(dateTo) + Chunk.NEWLINE, font));
        paragraph.add(Chunk.NEWLINE);
        return paragraph;
    }

    public void printTransactionBeli(int type, Date dateFrom, Date dateTo) {
        try {
            String name = "PEMBELIAN";
//            PDFTemplate(name + ".pdf", 0, 0, 0, 0);
            Rectangle pagesize = new Rectangle(615f, 930f);
            doc = new Document(pagesize.rotate(), 10, 10, 0, 0);
            PdfWriter.getInstance(doc, new FileOutputStream(name + ".pdf"));
            font = FontFactory.getFont("verdana", 12, com.itextpdf.text.Font.BOLD);

            doc.open();
            Paragraph paragraph = new Paragraph();
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setFont(font);
            paragraph.add(name.toUpperCase());
            paragraph.add(Chunk.NEWLINE);
            if ((dateFrom != null) && (dateTo != null)) {
                paragraph.add(df.format(dateFrom) + " s/d " + df.format(dateTo));
                paragraph.add(Chunk.NEWLINE);
            }
            paragraph.add(Chunk.NEWLINE);
            doc.add(paragraph);

            PdfPTable pdfTable = new PdfPTable(7);
            pdfTable.setWidthPercentage(100);
            pdfTable.setWidths(new int[]{1, 1, 1, 1, 1, 1, 1});
            pdfTable.getDefaultCell().setPadding(5);
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

            String query = "select t.create_date as create_date, sc.name as name, r.name as region, t.invoices as invoices, "
                    + "ic.name as item, td.quantity as qty, td.price as price, td.total_prices as total_prices, "
                    + "t.total_prices as totals "
                    + "from transaction t, transaction_detail td, supplier_customer sc, "
                    + "inventory_category ic, region r "
                    + "where t.id = td.transaction_id and sc.id = td.supplier_customer_id and td.inventory_category_id = ic.id "
                    + "and r.id = sc.region ";
            if (type != 2) {
                query += "AND t.transaction_type = ? ";
            }

            if ((dateFrom != null) && (dateTo != null)) {
                query += "AND (t.create_date BETWEEN ? AND ?) ";
            }

            query += "ORDER BY t.create_date";
            PreparedStatement pre = con.prepareStatement(query);

            int i = 0;
            if (type != 2) {
                i += 1;
                pre.setObject(i, type);
            }
            if ((dateFrom != null) && (dateTo != null)) {
                i += 1;
                pre.setObject(i, dateFrom);
                i += 1;
                pre.setObject(i, dateTo);
            }

            String date = "", costumer = "", region = "", nota = "";

            ResultSet rs = pre.executeQuery();
            pdfTable.getDefaultCell().setPadding(2);
            font = FontFactory.getFont("verdana", 10, com.itextpdf.text.Font.NORMAL);

            while (rs.next()) {
                //table content
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                if (date.equalsIgnoreCase(df.format(rs.getDate("create_date")))) {
                    pdfTable.addCell("");
                    pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                    pdfTable.addCell(new Phrase(costumer.equalsIgnoreCase(rs.getString("name")) ? "" : rs.getString("name"), font));
                    pdfTable.addCell(new Phrase(region.equalsIgnoreCase(rs.getString("region")) ? ""
                            : (rs.getString("region").toLowerCase().equalsIgnoreCase("none") ? "" : rs.getString("region")), font));
                    costumer = rs.getString("name");
                    region = rs.getString("region");
                } else {
                    pdfTable.addCell(new Phrase(df.format(rs.getDate("create_date")), font));
                    pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                    pdfTable.addCell(new Phrase(rs.getString("name"), font));
                    pdfTable.addCell(new Phrase(rs.getString("region").toLowerCase().equalsIgnoreCase("none") ? "" : rs.getString("region"), font));
                    costumer = rs.getString("name");
                    region = rs.getString("region");
                }
                pdfTable.addCell(new Phrase(rs.getString("item"), font));
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTable.addCell(new Phrase(rs.getString("qty"), font));
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                pdfTable.addCell(new Phrase(nf.format(rs.getLong("price")), font));
                pdfTable.addCell(new Phrase(nf.format(rs.getLong("total_prices")), font));
                date = df.format(rs.getDate("create_date"));
            }
            doc.add(pdfTable);
            doc.close();
        } catch (Exception ex) {
        }
    }

    public void printTransactionJual(int type, Date dateFrom, Date dateTo, String name) {
        try {
//            PDFTemplate(name + ".pdf", 0, 0, 0, 0);
            Rectangle pagesize = new Rectangle(615f, 930f);
            doc = new Document(pagesize.rotate(), 10, 2, 0, 0);
            PdfWriter.getInstance(doc, new FileOutputStream("Penjualan " + name + ".pdf"));
            font = FontFactory.getFont("verdana", 12, com.itextpdf.text.Font.BOLD);

            doc.open();
            Paragraph paragraph = new Paragraph();
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setFont(font);
            paragraph.add("PENJUALAN");
            paragraph.add(Chunk.NEWLINE);
            if ((dateFrom != null) && (dateTo != null)) {
                paragraph.add(df.format(dateFrom) + " s/d " + df.format(dateTo));
                paragraph.add(Chunk.NEWLINE);
            }
            paragraph.add(Chunk.NEWLINE);
            doc.add(paragraph);

            PdfPTable pdfTable = new PdfPTable(11);
            pdfTable.setWidthPercentage(100);
            pdfTable.setWidths(new int[]{1, 3, 1, 2, 1, 1, 1, 2, 2, 1, 2});
            pdfTable.getDefaultCell().setPadding(5);
            //table header
            pdfTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfTable.addCell(new Phrase("TGL", font));
            pdfTable.addCell(new Phrase("CUSTOMER", font));
            pdfTable.addCell(new Phrase("NOTA", font));
            pdfTable.addCell(new Phrase("ITEM", font));
            pdfTable.addCell(new Phrase("QTY", font));
            pdfTable.addCell(new Phrase("TOTAL QTY", font));
            pdfTable.addCell(new Phrase("HARGA", font));
            pdfTable.addCell(new Phrase("TOTAL HARGA", font));
            pdfTable.addCell(new Phrase("TOTAL", font));
            pdfTable.addCell(new Phrase("PLU", font));
            pdfTable.addCell(new Phrase("KET", font));

            String query = "select t.id as transactionId, t.create_date as create_date, sc.name as name, r.name as region, t.invoices as invoices, "
                    + "td.item_partai as item, td.quantity as qty, td.price as price, td.total_prices as total_prices, "
                    + "t.total_prices as totals, td.plu as plu, t.nota as nota, td.code as code "
                    + "from transaction t, transaction_detail td, supplier_customer sc, inventory_category ic, region r "
                    + "where t.id = td.transaction_id and sc.id = td.supplier_customer_id and td.inventory_category_id = ic.id "
                    + "and r.id = sc.region ";
            if (type != 2) {
                query += "AND t.transaction_type = ? ";
            }

            if ((dateFrom != null) && (dateTo != null)) {
                query += "AND (t.create_date BETWEEN ? AND ?) ";
            }

            query += "ORDER BY t.code, t.create_date, sc.name, td.code ASC";
            PreparedStatement pre = con.prepareStatement(query);
            int i = 0;
            if (type != 2) {
                i += 1;
                pre.setObject(i, type);
            }
            if ((dateFrom != null) && (dateTo != null)) {
                i += 1;
                pre.setObject(i, dateFrom);
                i += 1;
                pre.setObject(i, dateTo);
            }

            ResultSet rs = pre.executeQuery();
            String date = "", costumer = "", costumer2 = "", nota = "";
            String invoices = "", invoices2 = "";
            pdfTable.getDefaultCell().setPadding(2);
            font = FontFactory.getFont("verdana", 10, com.itextpdf.text.Font.NORMAL);
            int code = 0;
//            int switchColor = 0;
            while (rs.next()) {
                //table content
//                BaseColor custom = new BaseColor(229, 255, 255);
//                pdfTable.getDefaultCell().setBackgroundColor(switchColor == 0?BaseColor.WHITE:custom);
                costumer = (rs.getString("region").toLowerCase().equalsIgnoreCase("none") ? "" : rs.getString("region") + " / ")
                        + rs.getString("name");

                if (date.equalsIgnoreCase(df.format(rs.getDate("create_date")))) {
                    pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfTable.addCell(new Phrase(rs.getInt("code") > 1 ? " "
                            : df.format(rs.getDate("create_date")), font));
                    pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                    pdfTable.addCell(new Phrase(rs.getInt("code") > 1 ? " " : costumer, font));
                    code = (costumer2.equalsIgnoreCase(costumer) ? code : getLastCodeTransactionDetail(rs.getString("transactionId")));
                } else {
                    pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfTable.addCell(new Phrase(df.format(rs.getDate("create_date")), font));
                    pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                    pdfTable.addCell(new Phrase(costumer, font));
                    code = getLastCodeTransactionDetail(rs.getString("transactionId"));
                }
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTable.addCell(new Phrase((nota.equalsIgnoreCase(rs.getString("nota")) ? ""
                        : (rs.getString("nota").equalsIgnoreCase("") ? "" : "NOTA " + rs.getString("nota"))), font));
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfTable.addCell(new Phrase(rs.getString("item"), font));
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                pdfTable.addCell(new Phrase(nf.format(rs.getLong("qty")).replace(",00", ""), font));
                if (1 == rs.getInt("code")) {
                    font = FontFactory.getFont("verdana", 10, com.itextpdf.text.Font.BOLD);
                    PdfPCell cell = new PdfPCell(new Phrase(" ", font));
                    cell.setRowspan(code);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfTable.addCell(cell);
                }
                font = FontFactory.getFont("verdana", 10, com.itextpdf.text.Font.NORMAL);
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                pdfTable.addCell(new Phrase(nf.format(rs.getLong("price")).replace(",00", ""), font));
                pdfTable.addCell(new Phrase(nf.format(rs.getLong("total_prices")).replace(",00", ""), font));
                if (1 == rs.getInt("code")) {
                    font = FontFactory.getFont("verdana", 10, com.itextpdf.text.Font.BOLD);
                    PdfPCell cell = new PdfPCell(new Phrase(nf.format(rs.getLong("totals")).replace(",00", ""), font));
                    cell.setRowspan(code);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfTable.addCell(cell);
                }
                font = FontFactory.getFont("verdana", 10, com.itextpdf.text.Font.NORMAL);
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTable.addCell(new Phrase((rs.getLong("plu") == 0 ? "" : rs.getLong("plu") + ""), font));
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                invoices = (rs.getLong("invoices") == 0 ? "" : rs.getString("invoices"));
                pdfTable.addCell(new Phrase(invoices2.equalsIgnoreCase(invoices) ? "" : invoices, font));
                invoices2 = invoices;
                nota = rs.getString("nota");
                date = df.format(rs.getDate("create_date"));
//                costumer2 = costumer;
//                switchColor = switchColor == 0 ? 1 : 0;
            }
            doc.add(pdfTable);
            doc.close();
        } catch (Exception ex) {
        }
    }

    public void printLabaRugi(Date dateFrom, Date dateTo, int printType) {
        try {
            PDFTemplate("LabaRugi (" + df.format(new Date(System.currentTimeMillis())) + ").pdf", 20, 15, 5, 5, 0);
            doc.open();
            //Title
            DateFormat df = new SimpleDateFormat("MMMM");
            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(dateFrom);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(dateTo);
            int lama = startCalendar.get(Calendar.MONTH) - endCalendar.get(Calendar.MONTH);
            Paragraph paragraph = new Paragraph();
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setFont(font);
            paragraph.add(printType == 0?"LABA - RUGI (":"PEMBELIAN - PENJUALAN (");
            for (int i = 0; i >= lama; i--) {
                paragraph.add(df.format(startCalendar.getTime()).toUpperCase() + (i == lama ? " " : "-"));
                startCalendar.add(startCalendar.MONTH, 1);
            }
            paragraph.add(startCalendar.get(Calendar.YEAR) + ")" + Chunk.NEWLINE);
            paragraph.add(Chunk.NEWLINE);
            doc.add(paragraph);
            paragraph.clear();

            //Pembelian
            int type = Transaction.TransactionType.BUY.ordinal();
            doc.add(printBuying(type, SupplierCustomer.SupplierCustomerType.SUPPLIER.ordinal(), dateFrom, dateTo));
            double totalBuy = totalTransactionBuyOrSell(type, dateFrom, dateTo);

            //Penjualan
            doc.newPage();
            type = Transaction.TransactionType.SELL.ordinal();
            doc.add(printSelling(type, SupplierCustomer.SupplierCustomerType.CUSTOMER.ordinal(), dateFrom, dateTo));
            double totalSell = totalTransactionBuyOrSell(type, dateFrom, dateTo);

            if (printType == 0) {

                //Biaya Packing
                doc.newPage();
                type = Transaction.TransactionType.OTHER.ordinal();
                Transaction t = getBiayaPacking(type);
                double totalBiayaPacking = t.getTotalPrices();
                doc.add(printBiayaPacking(t));

                //Laba Rugi
                double labaRugi = totalSell - (totalBuy + totalBiayaPacking);
                font = FontFactory.getFont("verdana", 16, com.itextpdf.text.Font.BOLD);
                doc.newPage();
                paragraph.add(Chunk.NEWLINE);
                doc.add(paragraph);
                PdfPTable pdfTable = new PdfPTable(4);
                pdfTable.setWidthPercentage(100);
                pdfTable.setWidths(new int[]{2, 1, 3, 4});
                pdfTable.getDefaultCell().setBorder(0);
                pdfTable.getDefaultCell().setPadding(5);
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfTable.addCell(new Phrase("PEMBELIAN ", font));
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                pdfTable.addCell(new Phrase("Rp", font));
                pdfTable.addCell(new Phrase(nf.format(totalBuy).replace(",00", ""), font));
                pdfTable.addCell(new Phrase(""));
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfTable.addCell(new Phrase("BIAYA PACKING ", font));
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                pdfTable.addCell(new Phrase("Rp", font));
                pdfTable.addCell(new Phrase(nf.format(totalBiayaPacking).replace(",00", ""), font));
                pdfTable.addCell(new Phrase(""));
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfTable.addCell(new Phrase("PENJUALAN ", font));
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                pdfTable.addCell(new Phrase("Rp", font));
                pdfTable.addCell(new Phrase(nf.format(totalSell).replace(",00", ""), font));
                pdfTable.addCell(new Phrase(""));
                pdfTable.getDefaultCell().setBorder(1);
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfTable.addCell(new Phrase((labaRugi > 0 ? "LABA " : "RUGI "), font));
                pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                pdfTable.addCell(new Phrase("Rp", font));
                pdfTable.addCell(new Phrase(nf.format(labaRugi).replace(",00", ""), font));
                pdfTable.addCell(new Phrase(""));
                doc.add(pdfTable);

            }

            doc.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printStockKardus(Date dateFrom, Date dateTo) {
        try {
            PDFTemplate("LaporanStockKardus (" + df.format(new Date(System.currentTimeMillis())) + ").pdf", 20, 15, 5, 5, 1);
            doc.open();

            //Table Header
            doc.add(setHeaderPage(dateFrom, dateTo, "Laporan Penjualan / Pembelian"));
            font = FontFactory.getFont("verdana", 11, com.itextpdf.text.Font.NORMAL);
            PdfPTable pdfTable = new PdfPTable(6);
            pdfTable.setWidthPercentage(100);
            pdfTable.setWidths(new int[]{1, 2, 2, 2, 2, 3});
            pdfTable.getDefaultCell().setBorder(1);
            pdfTable.getDefaultCell().setPadding(4);
            pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfTable.addCell(new Phrase("No", font));
            pdfTable.addCell(new Phrase("Tanggal", font));
            pdfTable.addCell(new Phrase("Masuk", font));
            pdfTable.addCell(new Phrase("Keluar", font));
            pdfTable.addCell(new Phrase("Total Harga", font));
            pdfTable.addCell(new Phrase("Keterangan", font));

            //Table Content
            pdfTable.getDefaultCell().setBorder(0);
            pdfTable.getDefaultCell().setPadding(2);
            doc.add(printKeluarMasukKardus(pdfTable, dateFrom, dateTo));

            //Total prices Pembelian
            font = FontFactory.getFont("verdana", 12, com.itextpdf.text.Font.NORMAL);
            doc.newPage();
            doc.add(setHeaderPage(dateFrom, dateTo, "Laporan Pembelian / Penjualan(Summary)"));
            Paragraph paragraph = new Paragraph();
            paragraph.add(new Phrase("Pembelian :" + Chunk.NEWLINE, font));
            paragraph.add(Chunk.NEWLINE);
            doc.add(paragraph);
            paragraph.clear();
            pdfTable = new PdfPTable(3);
            pdfTable.setWidthPercentage(100);
            pdfTable.setWidths(new int[]{1, 3, 3});
            pdfTable.getDefaultCell().setBorder(1);
            pdfTable.getDefaultCell().setPadding(4);
            pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfTable.addCell(new Phrase("No", font));
            pdfTable.addCell(new Phrase("Nama", font));
            pdfTable.addCell(new Phrase("Jumlah", font));
            doc.add(printTotalPricesTransactionPerSupplierCustomer(pdfTable, dateFrom, dateTo, 0));

            //Total prices Penjualan
            font = FontFactory.getFont("verdana", 12, com.itextpdf.text.Font.NORMAL);
            paragraph.add(Chunk.NEWLINE);
            paragraph.add(Chunk.NEWLINE);
            paragraph.add(new Phrase("Penjualan :" + Chunk.NEWLINE, font));
            paragraph.add(Chunk.NEWLINE);
            doc.add(paragraph);
            paragraph.clear();
            pdfTable = new PdfPTable(3);
            pdfTable.setWidthPercentage(100);
            pdfTable.setWidths(new int[]{1, 3, 3});
            pdfTable.getDefaultCell().setBorder(1);
            pdfTable.getDefaultCell().setPadding(4);
            pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfTable.addCell(new Phrase("No", font));
            pdfTable.addCell(new Phrase("Nama", font));
            pdfTable.addCell(new Phrase("Jumlah", font));
            doc.add(printTotalPricesTransactionPerSupplierCustomer(pdfTable, dateFrom, dateTo, 1));

            //Stock Kardus
            doc.newPage();
            doc.add(setHeaderPage(dateFrom, dateTo, "Stock Gudang"));
            pdfTable = new PdfPTable(4);
            pdfTable.setWidthPercentage(100);
            pdfTable.setWidths(new int[]{1, 3, 3, 3});
            pdfTable.getDefaultCell().setBorder(1);
            pdfTable.getDefaultCell().setPadding(4);
            pdfTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfTable.addCell(new Phrase("No", font));
            pdfTable.addCell(new Phrase("Nama Barang", font));
            pdfTable.addCell(new Phrase("Sisa Stock", font));
            pdfTable.addCell(new Phrase("", font));
            doc.add(printStockGudang(pdfTable));
            doc.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printStockApel(Date dateFrom, Date dateTo) {
        try {
            PDFTemplate("Laporan Stock Apel.pdf", 20, 15, 5, 5, 1);
            doc.open();

            //Table Header
            doc.add(setHeaderPage(dateFrom, dateTo, "Laporan Pembelian / Penjualan"));
            font = FontFactory.getFont("verdana", 11, com.itextpdf.text.Font.NORMAL);
            PdfPTable pdfTableOuter = new PdfPTable(2);
            pdfTableOuter.setWidthPercentage(100);
            pdfTableOuter.setWidths(new int[]{2, 2});

            //HEADER LEFT
            PdfPTable pdfTable = new PdfPTable(4);
            pdfTable.setWidthPercentage(100);
            pdfTable.setWidths(new int[]{1, 1, 1, 2});
            PdfPCell cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(4);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setPhrase(new Phrase("Tgl", font));
            pdfTable.addCell(cell);
            cell.setPhrase(new Phrase("Jenis", font));
            pdfTable.addCell(cell);
            cell.setPhrase(new Phrase("Qty", font));
            pdfTable.addCell(cell);
            cell.setPhrase(new Phrase("Supplier", font));
            pdfTable.addCell(cell);

            //CONTENT LEFT
            cell.setBorder(Rectangle.NO_BORDER);
            pdfTableOuter.addCell(printJualBeliApel(pdfTable, cell, dateFrom, dateTo, Transaction.TransactionType.BUY.ordinal()));

            //HEADER RIGHT
            pdfTable = new PdfPTable(4);
            pdfTable.setWidthPercentage(100);
            pdfTable.setWidths(new int[]{1, 1, 1, 2});
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setPhrase(new Phrase("Tgl", font));
            pdfTable.addCell(cell);
            cell.setPhrase(new Phrase("Jenis", font));
            pdfTable.addCell(cell);
            cell.setPhrase(new Phrase("Qty", font));
            pdfTable.addCell(cell);
            cell.setPhrase(new Phrase("Customer", font));
            pdfTable.addCell(cell);

            //CONTENT RIGHT
            cell.setBorder(Rectangle.NO_BORDER);
            pdfTableOuter.addCell(printJualBeliApel(pdfTable, cell, dateFrom, dateTo, Transaction.TransactionType.SELL.ordinal()));

            pdfTable = new PdfPTable(4);
            pdfTable.setWidthPercentage(100);
            pdfTable.setWidths(new int[]{1, 1, 1, 2});
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(2);
            cell.setBorder(Rectangle.BOX);
            cell.setPhrase(new Phrase("Total", font));
            pdfTable.addCell(cell);
            cell.setPhrase(new Phrase(getTotalQuantity(dateFrom, dateTo, Transaction.TransactionType.BUY.ordinal()) + " Kg", font));
            pdfTable.addCell(cell);
            pdfTableOuter.addCell(pdfTable);

            pdfTable = new PdfPTable(4);
            pdfTable.setWidthPercentage(100);
            pdfTable.setWidths(new int[]{1, 1, 1, 2});
            cell.setPhrase(new Phrase("Total", font));
            pdfTable.addCell(cell);
            cell.setPhrase(new Phrase(getTotalQuantity(dateFrom, dateTo, Transaction.TransactionType.SELL.ordinal()) + " Kg", font));
            pdfTable.addCell(cell);
            pdfTableOuter.addCell(pdfTable);
            doc.add(pdfTableOuter);
            doc.add(Chunk.NEWLINE);

//            //Table Total Pembelian/Penjualan
//            cell.setColspan(2);
//            pdfTable = new PdfPTable(7);
//            pdfTable.setWidthPercentage(100);
//            pdfTable.setWidths(new int[]{2, 2, 2, 2, 1, 2, 2});
//            cell.setPhrase(new Phrase("Total Pembelian", font));
//            pdfTable.addCell(cell);
//            cell.setPhrase(new Phrase("Total Penjualan", font));
//            pdfTable.addCell(cell);
//            
//            pdfTable.addCell(blank(cell));
//            
//            //Table Sisa Stock
//            cell.setColspan(2);
//            cell.setBorder(Rectangle.BOX);
//            cell.setPhrase(new Phrase("Sisa Stock", font));
//            pdfTable.addCell(cell);
//            
//            //Table Total Pembelian/Penjualan
//            cell.setColspan(1);
//            cell.setPhrase(new Phrase("Jenis", font));
//            pdfTable.addCell(cell);
//            cell.setPhrase(new Phrase("Total Qty", font));
//            pdfTable.addCell(cell);
//            cell.setPhrase(new Phrase("Jenis", font));
//            pdfTable.addCell(cell);
//            cell.setPhrase(new Phrase("Total Qty", font));
//            pdfTable.addCell(cell);
//            
//            pdfTable.addCell(blank(cell));
//            
//            //Table Sisa Stock
//            cell.setColspan(1);
//            cell.setBorder(Rectangle.BOX);
//            cell.setPhrase(new Phrase("Jenis", font));
//            pdfTable.addCell(cell);
//            cell.setPhrase(new Phrase("Total Qty", font));
//            pdfTable.addCell(cell);
//            
//            doc.add(pdfTable);

            doc.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Transaction> getAllTransaction(Date dateFrom, Date dateTo, int type) {
        try {
            String query = "select t.create_date as create_date, ic.name as item, sum(td.quantity) as quantity, sc.name as supllier_customer "
                    + "from transaction t "
                    + "inner join transaction_detail td on (t.id = td.transaction_id) "
                    + "inner join supplier_customer sc on (sc.id = td.supplier_customer_id) "
                    + "inner join inventory_category ic on (ic.id = td.inventory_category_id) "
                    + "where t.transaction_type = ? and (t.create_date between ? and ?) "
                    + "group by t.create_date, ic.id, sc.id "
                    + "order by t.create_date, sc.name, ic.name asc";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, type);
            pre.setObject(2, dateFrom);
            pre.setObject(3, dateTo);
            ResultSet rs = pre.executeQuery();
            List<Transaction> list = new ArrayList<Transaction>();
            while (rs.next()) {
                Transaction t = new Transaction();
                t.getLog().setCreateDate(rs.getDate("create_date"));
                t.setQuantity(rs.getInt("quantity"));
                t.setInventoryCategoryId(rs.getString("item"));
                t.setSupplierCustomerId(rs.getString("supllier_customer"));
                list.add(t);
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getTotalQuantity(Date dateFrom, Date dateTo, int type) {
        try {
            String query = "select sum(td.quantity) as quantities "
                    + "from transaction t inner join transaction_detail td on (t.id = td.transaction_id) "
                    + "where t.transaction_type = ? and (t.create_date between ? and ?) "
                    + "order by t.create_date asc";
            PreparedStatement pre = con.prepareStatement(query);
            pre.setObject(1, type);
            pre.setObject(2, dateFrom);
            pre.setObject(3, dateTo);
            ResultSet rs = pre.executeQuery();
            Transaction t = new Transaction();
            while (rs.next()) {
                return nf.format(rs.getLong("quantities")).replace(",00", "");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public PdfPTable printJualBeliApel(PdfPTable pdfTable, PdfPCell cell, Date dateFrom, Date dateTo, int type) {
        try {
            DateFormat df = new SimpleDateFormat("dd-MMM");
            List<Transaction> list = new ArrayList<Transaction>();
            list = getAllTransaction(dateFrom, dateTo, type);
            for (Transaction t : list) {
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                cell.setPhrase(new Phrase(df.format(t.getLog().getCreateDate()), font));
                pdfTable.addCell(cell);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPhrase(new Phrase(t.getInventoryCategoryId(), font));
                pdfTable.addCell(cell);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPhrase(new Phrase(nf.format(t.getQuantity()).replace(",00", "") + " Kg", font));
                pdfTable.addCell(cell);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPhrase(new Phrase(t.getSupplierCustomerId(), font));
                pdfTable.addCell(cell);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pdfTable;
    }

    public PdfPCell blank(PdfPCell cell) {
        cell.setColspan(1);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPhrase(new Phrase(" ", font));
        return cell;
    }

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
            java.util.logging.Logger.getLogger(MainAccounting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainAccounting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainAccounting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainAccounting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainAccounting().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnBatalKategori;
    private javax.swing.JButton btnBatalPelunasan;
    private javax.swing.JButton btnBatalTambahStok;
    private javax.swing.JButton btnBatalWilayah;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnCariKategori;
    private javax.swing.JButton btnCariStokBarang;
    private javax.swing.JButton btnCariWilayah;
    private javax.swing.JButton btnCetakLabaRugi;
    private javax.swing.JButton btnCetakStockKardus;
    private javax.swing.JButton btnCetakTransaksiBeli;
    private javax.swing.JButton btnCetakTransaksiJual;
    private javax.swing.JButton btnPilihTransaksiBeli;
    private javax.swing.JButton btnPilihTransaksiJual;
    private javax.swing.JButton btnRekap;
    private javax.swing.JButton btnSaveRekap;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnSimpanKategori;
    private javax.swing.JButton btnSimpanPelunasan;
    private javax.swing.JButton btnSimpanTambahStok;
    private javax.swing.JButton btnSimpanWilayah;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JTextArea fldAlamat;
    private javax.swing.JTextField fldCari;
    private javax.swing.JTextField fldCariKategori;
    private javax.swing.JTextField fldCariStokBarang;
    private javax.swing.JTextField fldCariWilayah;
    private org.jdesktop.swingx.JXDatePicker fldDateFrom;
    private org.jdesktop.swingx.JXDatePicker fldDateGaji;
    private org.jdesktop.swingx.JXDatePicker fldDatePengiriman;
    private org.jdesktop.swingx.JXDatePicker fldDateTo;
    private javax.swing.JComboBox fldFromItemStok;
    private javax.swing.JComboBox fldItemTambahStok;
    private javax.swing.JTextField fldJumlahTambah;
    private javax.swing.JTextArea fldKeterangan;
    private javax.swing.JTextField fldKodeBarang;
    private javax.swing.JTextField fldNamaKategori;
    private javax.swing.JTextField fldName;
    private javax.swing.JTextField fldPelunasanBayar;
    private org.jdesktop.swingx.JXDatePicker fldPelunasanBeliDateFrom;
    private org.jdesktop.swingx.JXDatePicker fldPelunasanBeliDateTo;
    private org.jdesktop.swingx.JXDatePicker fldPelunasanDate;
    private javax.swing.JTextField fldPelunasanFaktur;
    private javax.swing.JTextField fldPelunasanNama;
    private javax.swing.JTextField fldPelunasanNota;
    private javax.swing.JTextField fldPelunasanSisaHutang;
    private javax.swing.JTextField fldPelunasanTotalHarga;
    private javax.swing.JTextField fldPelunasanTotalPembayaran;
    private javax.swing.JTextField fldPelunasanTransactionId;
    private javax.swing.JTextField fldSisaStok;
    private javax.swing.JTextField fldStok;
    private javax.swing.JTextField fldTelp;
    private javax.swing.JTextField fldTotalBiaya;
    private javax.swing.JTextField fldTotalStok;
    private org.jdesktop.swingx.JXDatePicker fldTransaksiBeliDateFrom;
    private org.jdesktop.swingx.JXDatePicker fldTransaksiBeliDateTo;
    private org.jdesktop.swingx.JXDatePicker fldTransaksiJualDateFrom;
    private org.jdesktop.swingx.JXDatePicker fldTransaksiJualDateFrom1;
    private org.jdesktop.swingx.JXDatePicker fldTransaksiJualDateFrom3;
    private org.jdesktop.swingx.JXDatePicker fldTransaksiJualDateFrom4;
    private org.jdesktop.swingx.JXDatePicker fldTransaksiJualDateTo;
    private org.jdesktop.swingx.JXDatePicker fldTransaksiJualDateTo1;
    private javax.swing.JComboBox fldWilayah;
    private javax.swing.JTextField fldWilayahBaru;
    private javax.swing.JInternalFrame frameBeli;
    private javax.swing.JInternalFrame frameCari;
    private javax.swing.JInternalFrame frameCariWilayah;
    private javax.swing.JInternalFrame frameGaji;
    private javax.swing.JInternalFrame frameHargaTransaksi;
    private javax.swing.JInternalFrame frameJual;
    private javax.swing.JInternalFrame frameKomisi;
    private javax.swing.JInternalFrame framePelunasan;
    private javax.swing.JInternalFrame framePelunasanTransaksiBeli;
    private javax.swing.JInternalFrame framePelunasanTransaksiJual;
    private javax.swing.JInternalFrame framePengiriman;
    private javax.swing.JInternalFrame frameRekap;
    private javax.swing.JInternalFrame frameRiwayatTransaksiBeli;
    private javax.swing.JInternalFrame frameRiwayatTransaksiJual;
    private javax.swing.JInternalFrame frameStokBarang;
    private javax.swing.JInternalFrame frameTambahKategori;
    private javax.swing.JInternalFrame frameTambahPemasokPelanggan;
    private javax.swing.JInternalFrame frameTambahStok;
    private javax.swing.JInternalFrame frameTransaksiLain;
    private javax.swing.JInternalFrame frameWilayah;
    private javax.swing.JInternalFrame framekategori;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField31;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField9;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker2;
    private javax.swing.JLabel jdlCari;
    private javax.swing.JLabel jdlCariWilayah;
    private javax.swing.JLabel jdlKategori;
    private javax.swing.JLabel jdlKategori1;
    private javax.swing.JLabel jdlPelunasan;
    private javax.swing.JLabel jdlStokBarang;
    private javax.swing.JLabel jdlTambahStok;
    private javax.swing.JLabel jdlWilayah;
    private javax.swing.JLabel lblAlamat;
    private javax.swing.JLabel lblItem;
    private javax.swing.JLabel lblItem1;
    private javax.swing.JLabel lblJudul;
    private javax.swing.JLabel lblJudul1;
    private javax.swing.JLabel lblJudul10;
    private javax.swing.JLabel lblJudul11;
    private javax.swing.JLabel lblJudul12;
    private javax.swing.JLabel lblJudul13;
    private javax.swing.JLabel lblJudul2;
    private javax.swing.JLabel lblJudul3;
    private javax.swing.JLabel lblJudul4;
    private javax.swing.JLabel lblJudul5;
    private javax.swing.JLabel lblJudul6;
    private javax.swing.JLabel lblJudul7;
    private javax.swing.JLabel lblJudul8;
    private javax.swing.JLabel lblJudul9;
    private javax.swing.JLabel lblJumlahTambah;
    private javax.swing.JLabel lblKeterangan;
    private javax.swing.JLabel lblNamaWilayah;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPelunasanBayar;
    private javax.swing.JLabel lblPelunasanFaktur;
    private javax.swing.JLabel lblPelunasanNama;
    private javax.swing.JLabel lblPelunasanNama1;
    private javax.swing.JLabel lblPelunasanNota;
    private javax.swing.JLabel lblPelunasanSisaHutang;
    private javax.swing.JLabel lblPelunasanTotalHarga;
    private javax.swing.JLabel lblPelunasanTotalPembayaran;
    private javax.swing.JLabel lblStok;
    private javax.swing.JLabel lblStokCampur;
    private javax.swing.JLabel lblTelp;
    private javax.swing.JLabel lblTotalPemasukan;
    private javax.swing.JLabel lblTotalPengeluaran;
    private javax.swing.JLabel lblTotalSaldo;
    private javax.swing.JLabel lblTotalStok;
    private javax.swing.JLabel lblWilayah;
    private javax.swing.JMenuBar listMenu;
    private javax.swing.JMenuItem menTambahStok;
    private javax.swing.JMenuItem menuBeli;
    private javax.swing.JMenuItem menuCariCustomer;
    private javax.swing.JMenuItem menuCariKategori;
    private javax.swing.JMenuItem menuCariPembelian;
    private javax.swing.JMenuItem menuCariPenjualan;
    private javax.swing.JMenuItem menuCariSupplier;
    private javax.swing.JMenuItem menuCariWilayah;
    private javax.swing.JMenu menuInventori;
    private javax.swing.JMenuItem menuJual;
    private javax.swing.JMenu menuKeluar;
    private javax.swing.JMenuItem menuPelunasanBeli;
    private javax.swing.JMenuItem menuPelunasanJual;
    private javax.swing.JMenu menuPembelian;
    private javax.swing.JMenu menuPenjualan;
    private javax.swing.JMenu menuRekap;
    private javax.swing.JMenu menuRiwayatTransaksi;
    private javax.swing.JMenuItem menuStokBarang;
    private javax.swing.JMenuItem menuTambahKategori;
    private javax.swing.JMenuItem menuTambahPelanggan;
    private javax.swing.JMenuItem menuTambahPemasok;
    private javax.swing.JMenuItem menuTambahWilayah;
    private javax.swing.JMenu menuWilayah;
    private javax.swing.JPanel panelEditKategori;
    private javax.swing.JPanel panelEditWilayah;
    private javax.swing.JScrollPane scrollTabelTransaksiBeli;
    private javax.swing.JScrollPane scrollTabelTransaksiBeli1;
    private javax.swing.JScrollPane scrollTabelTransaksiJual;
    private javax.swing.JScrollPane scrollTabelTransaksiJual1;
    private javax.swing.JTable tblDetailBeli;
    private javax.swing.JTable tblDetailJual;
    private javax.swing.JTable tblDetailPelunasanBeli;
    private javax.swing.JTable tblDetailPelunasanJual;
    private javax.swing.JTable tblGaji;
    private javax.swing.JTable tblKategoriResult;
    private javax.swing.JTable tblPelunasan;
    private javax.swing.JTable tblPelunasanBeli;
    private javax.swing.JTable tblPelunasanJual;
    private javax.swing.JTable tblResult;
    private javax.swing.JTable tblSisaItem;
    private javax.swing.JTable tblStokBarangResult;
    private javax.swing.JTable tblTransaksiBeli;
    private javax.swing.JTable tblTransaksiJual;
    private javax.swing.JTable tblWilayahResult;
    // End of variables declaration//GEN-END:variables
}
