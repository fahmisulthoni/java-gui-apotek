/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apoteksanjaya;

import com.opencsv.CSVReader;
import db.Connection;
import db.Parameter;
import db.SetTable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;



/**
 * @author Fahmi Sulthoni
 */
public class Supplier extends javax.swing.JInternalFrame{
    ResultSet st;
    Connection con;
    // file CSV
    File file;
    // inisialisasi data model
    private DefaultTableModel tabel;
    
    // driver dan url database
    private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static String JDBC_URL_CONNECTION = "jdbc:mysql://localhost/apotek";
    
    // user password database
    static final String USER = "root";
    static final String PASS = "";
    

    
    public Supplier() {
        con = new Connection(Parameter.HOST_DB, Parameter.USERNAME_DB,Parameter.PASSWORD_DB, Parameter.IPHOST, Parameter.PORT);
        initComponents();
        getTable();
    }
         
    public void getTable(){
        String namaKolom[] = {"kode_supplier","nama_supplier","no_telepon","alamat"};
        st = con.querySelect(namaKolom, "supplier");
        TableSupplier.setModel(new SetTable(st));
    }
    
    private void getAddSupplier() {
        //java.util.Date tgl=(java.util.Date)this.TanggalMasuk.getDate()
        if (KodeSupplier.getText().equals("")||
            NamaSupplier.getText().equals("")||
            NoTelepon.getText().equals("")||
            Alamat.getText().equals("")){
            //||new java.sql.Date(ex.getTime()).equals("")
            JOptionPane.showMessageDialog(this, "Lengkapi data anda");
        } else {
            String[] colom = {"kode_supplier","nama_supplier","no_telepon","alamat"};
            String[] value = {KodeSupplier.getText(),NamaSupplier.getText(),NoTelepon.getText(),Alamat.getText()};
            System.out.println(con.queryInsert("supplier", colom, value));
            getTable();            
            JOptionPane.showMessageDialog(this, "Data berhasil di simpan");
            getRefresh();
        }
}
    
    private void getEditSupplier(){
        if (KodeSupplier.getText().equals("")||
            NamaSupplier.getText().equals("")||
            NoTelepon.getText().equals("")||
            Alamat.getText().equals("")){
            //||new java.sql.Date(ex.getTime()).equals("")
            JOptionPane.showMessageDialog(this, "Lengkapi data anda");
        } else {
            String[] column = {"kode_supplier","nama_supplier","no_telepon","alamat"};
            String[] value = {KodeSupplier.getText(),NamaSupplier.getText(),NoTelepon.getText(),Alamat.getText()};
            String id = String.valueOf(TableSupplier.getValueAt(TableSupplier.getSelectedRow(), 0));
            System.out.println(con.queryUpdate("supplier", column, value, "kode_supplier='" + id + "'"));
            getTable();
            JOptionPane.showMessageDialog(this, "Data berhasil dirubah");
            getRefresh();
        }
    }
    
    public void getDeleteSupplier(){
        String id = String.valueOf(TableSupplier.getValueAt(TableSupplier.getSelectedRow(), 0));
        if (JOptionPane.showConfirmDialog(this, "Anda yakin ingin menghapus ?", "Peringatan!!!", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            con.queryDelete("supplier", "kode_supplier='" + id+"'");
        } else {
            return;
        }
        getTable();
        JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
        getRefresh();
    }
    
    public void getSearchSupplier(){
        if (TextSearch.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Masukkan yang anda cari !!!");
        } else {
            if (ComboBoxSearch.getSelectedItem().equals("Kode Supplier")) {
                st = con.querySelectAll("supplier", "kode_supplier LIKE '%" + TextSearch.getText() + "%' ");
                TableSupplier.setModel(new SetTable(st));
            } else if (ComboBoxSearch.getSelectedItem().equals("Nama Supplier")) {
                st = con.querySelectAll("supplier", "nama_supplier LIKE '%" + TextSearch.getText() + "%' ");
                TableSupplier.setModel(new SetTable(st));
            } 
        }
    } 
     
    public void getMouseClick(){
        //java.util.Date ex=(java.util.Date)this.Expired.getDate();
        KodeSupplier.setText(String.valueOf(TableSupplier.getValueAt(TableSupplier.getSelectedRow(), 0)));
        NamaSupplier.setText(String.valueOf(TableSupplier.getValueAt(TableSupplier.getSelectedRow(), 1)));
        NoTelepon.setText(String.valueOf(TableSupplier.getValueAt(TableSupplier.getSelectedRow(), 2)));
        Alamat.setText(String.valueOf(TableSupplier.getValueAt(TableSupplier.getSelectedRow(), 3)));
    }
    
    public void getRefresh(){
        KodeSupplier.setText(null);
        NamaSupplier.setText(null);
        NoTelepon.setText(null);
        Alamat.setText(null);
    }
    
    // melakukan koneksi database
    public static java.sql.Connection getConnect(){
        // inisialisasi variable
        java.sql.Connection con = null;
        
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(JDBC_URL_CONNECTION, USER, PASS);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return con;
    }
   
    // load CSV file
    public void loadCSV(String csvFile, String tableName){
        // inisialisasi prepared statement
        PreparedStatement sql_statement = null;
        // query sql
        String query = "INSERT INTO "+ tableName
                                + " (kode_supplier, nama_supplier, no_telepon, alamat) VALUES"
                                + "(?,?,?,?)";
        try {
            // set auto commit (false)
            this.con.setAutoCommit(false);
            // set prepare statement sql
            sql_statement = this.con.prepareStatement(query);
            // membaca file CSV
            CSVReader reader = new CSVReader(new FileReader(csvFile));  
            // inisialisasi variable
            String [] nextLine; 
            int lnNum = 0; 
            // parsing data dari CSV
            while ((nextLine = reader.readNext()) != null) {
                lnNum++;
                // binding data kode_supplier
                sql_statement.setString(1, nextLine[0]);
                // binding data nama_supplier
                sql_statement.setString(2, nextLine[1]);
                // binding data no_telepon
                sql_statement.setString(3, nextLine[2]);
                // binding data alamat
                sql_statement.setString(4,nextLine[3]);
                // menambahkan record ke batch
                sql_statement.addBatch();
            }                       
            // inisialisasi total record             
            int[] totalRecords = new int[2];
            try {
                totalRecords = sql_statement.executeBatch();
            } catch(BatchUpdateException e) {
                totalRecords = e.getUpdateCounts();
            }
            System.out.println ("Total data yang diinputkan " + totalRecords.length);                
            /* Close prepared statement */
            sql_statement.close();
            /* COMMIT transaction */
            this.con.commit();
            /* Close connection */
            this.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getData(){
        Object [] Baris = {"kode_supplier","nama_supplier","no_telepon","alamat"};
        tabel = new DefaultTableModel(null, Baris);
        TableSupplier.setModel(tabel);
        
        try{
            Statement stat = (Statement) Supplier.getConnect().createStatement();
            String sql = "select * from supplier";
            ResultSet rs = stat.executeQuery(sql);
            
            while(rs.next()){
                String a = rs.getString("kode_supplier");
                String b = rs.getString("nama_supplier");
                String c = rs.getString("no_telepon");
                String d = rs.getString("alamat");                String[] data= {a,b,c,d};
                tabel.addRow(data);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Supplier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eksportexcel(){
        FileWriter fileWriter;
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("[B]export_output/excel[/B]"));
        int retrival = chooser.showSaveDialog(null);
        if (retrival == JFileChooser.APPROVE_OPTION) {
            try{
                TableModel tModel = TableSupplier.getModel();
                fileWriter = new FileWriter(new File(chooser.getSelectedFile() + ".xls"));           
            // write header
                for(int i = 0; i < tModel.getColumnCount(); i++){
                fileWriter.write(tModel.getColumnName(i).toUpperCase() + "\t");
            }
                fileWriter.write("\n");
            // write record
                for(int i=0; i < tModel.getRowCount(); i++) {
                for(int j=0; j < tModel.getColumnCount(); j++) {
                fileWriter.write(tModel.getValueAt(i,j).toString() + "\t");
            }
                fileWriter.write("\n");
            }
                fileWriter.close();
                JOptionPane.showMessageDialog(this, "Data berhasil diexport");
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, e);
            }
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        KodeSupplier = new javax.swing.JTextField();
        LKodeObat = new javax.swing.JLabel();
        LNamaObat = new javax.swing.JLabel();
        LJenisObat = new javax.swing.JLabel();
        LKategoriObat = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableSupplier = new javax.swing.JTable();
        Refresh = new usu.widget.ButtonGlass();
        Add = new usu.widget.ButtonGlass();
        Edit = new usu.widget.ButtonGlass();
        NamaSupplier = new javax.swing.JTextField();
        Delete = new usu.widget.ButtonGlass();
        jLabel10 = new javax.swing.JLabel();
        ComboBoxSearch = new javax.swing.JComboBox();
        TextSearch = new javax.swing.JTextField();
        Search = new usu.widget.ButtonGlass();
        NoTelepon = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        Alamat = new javax.swing.JTextArea();
        txtFile = new javax.swing.JTextField();
        LExpired1 = new javax.swing.JLabel();
        Browse = new usu.widget.ButtonGlass();
        btnImport = new usu.widget.ButtonGlass();
        btnImport1 = new usu.widget.ButtonGlass();
        Add1 = new usu.widget.ButtonGlass();

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Supplier");

        LKodeObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LKodeObat.setForeground(new java.awt.Color(255, 255, 255));
        LKodeObat.setText("Kode Supplier");

        LNamaObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LNamaObat.setForeground(new java.awt.Color(255, 255, 255));
        LNamaObat.setText("Nama Supplier");

        LJenisObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LJenisObat.setForeground(new java.awt.Color(255, 255, 255));
        LJenisObat.setText("Alamat");

        LKategoriObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LKategoriObat.setForeground(new java.awt.Color(255, 255, 255));
        LKategoriObat.setText("No Telepon");

        TableSupplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Kode Supplier", "Nama Supplier", "No Telepon", "Alamat"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        TableSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableSupplierMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TableSupplier);

        Refresh.setForeground(new java.awt.Color(255, 255, 255));
        Refresh.setText("Refresh");
        Refresh.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshActionPerformed(evt);
            }
        });

        Add.setForeground(new java.awt.Color(255, 255, 255));
        Add.setText("Add");
        Add.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddActionPerformed(evt);
            }
        });

        Edit.setForeground(new java.awt.Color(255, 255, 255));
        Edit.setText("Edit");
        Edit.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditActionPerformed(evt);
            }
        });

        Delete.setForeground(new java.awt.Color(255, 255, 255));
        Delete.setText("Delete");
        Delete.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteActionPerformed(evt);
            }
        });

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Search Categories");

        ComboBoxSearch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kode Supplier", "Nama Supplier" }));

        Search.setForeground(new java.awt.Color(255, 255, 255));
        Search.setText("Search");
        Search.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchActionPerformed(evt);
            }
        });

        Alamat.setColumns(20);
        Alamat.setRows(5);
        jScrollPane2.setViewportView(Alamat);

        LExpired1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LExpired1.setForeground(new java.awt.Color(255, 255, 255));
        LExpired1.setText("Import CSV");

        Browse.setForeground(new java.awt.Color(255, 255, 255));
        Browse.setText("Browse");
        Browse.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BrowseActionPerformed(evt);
            }
        });

        btnImport.setForeground(new java.awt.Color(255, 255, 255));
        btnImport.setText("Import");
        btnImport.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportActionPerformed(evt);
            }
        });

        btnImport1.setForeground(new java.awt.Color(255, 255, 255));
        btnImport1.setText("Tampilkan Data");
        btnImport1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnImport1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImport1ActionPerformed(evt);
            }
        });

        Add1.setForeground(new java.awt.Color(255, 255, 255));
        Add1.setText("Export");
        Add1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Add1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Add1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LKategoriObat)
                                    .addComponent(LKodeObat)
                                    .addComponent(LNamaObat)
                                    .addComponent(LJenisObat))
                                .addGap(37, 37, 37)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(KodeSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                    .addComponent(NamaSupplier)
                                    .addComponent(NoTelepon)
                                    .addComponent(jScrollPane2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(LExpired1)
                                .addGap(38, 38, 38)
                                .addComponent(txtFile, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(Browse, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnImport1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(TextSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(ComboBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Add1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(Add, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(Edit, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(Refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(34, 34, 34))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(460, 460, 460)
                .addComponent(jLabel1)
                .addContainerGap(557, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(ComboBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(TextSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(Add, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Edit, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Add1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                        .addComponent(KodeSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LKodeObat)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(NamaSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LNamaObat)
                            .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Browse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LExpired1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LKategoriObat)
                            .addComponent(NoTelepon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnImport1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LJenisObat)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchActionPerformed
        getSearchSupplier();// TODO add your handling code here:
    }//GEN-LAST:event_SearchActionPerformed

    private void DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteActionPerformed
        getDeleteSupplier();// TODO add your handling code here:
    }//GEN-LAST:event_DeleteActionPerformed

    private void EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditActionPerformed
        getEditSupplier();// TODO add your handling code here:
    }//GEN-LAST:event_EditActionPerformed

    private void AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddActionPerformed
        getAddSupplier();// TODO add your handling code here:
    }//GEN-LAST:event_AddActionPerformed

    private void RefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshActionPerformed
        getRefresh();// TODO add your handling code here:
        getTable();
    }//GEN-LAST:event_RefreshActionPerformed

    private void TableSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableSupplierMouseClicked
        getMouseClick();// TODO add your handling code here:
    }//GEN-LAST:event_TableSupplierMouseClicked

    private void BrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BrowseActionPerformed
              // TODO add your handling code here:
        // inisialisasi variable
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        // filter berdasarkan file extension
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV (Comma Delimited) *.csv", "csv");
        fc.setFileFilter(filter);
        fc.setMultiSelectionEnabled(false);
        
        // menampilkan file chooser dialog
        int i = fc.showOpenDialog(this);
        
        // mengecek jika file chooser dialog di cancel
        if(i == fc.CANCEL_OPTION){
            return;
        }
        
        // mengambil file
        file = fc.getSelectedFile();
        
        // mengecek validnya file
        if(file == null || file.getName().equals("")){
            JOptionPane.showMessageDialog(null, "Error : File belum dipilih", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
           
        // set text
        txtFile.setText(file.toString());
    }//GEN-LAST:event_BrowseActionPerformed

    private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportActionPerformed
         // TODO add your handling code here:
         try{
            btnImport.setEnabled(false);
            CSVLoader csvLoader = new CSVLoader(getConnect());
            csvLoader.loadCSV(file.toString(), "supplier");
            JOptionPane.showMessageDialog(null, "Info : File berhasil di import.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnImportActionPerformed

    private void btnImport1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImport1ActionPerformed
        // TODO add your handling code here:
        getData();
    }//GEN-LAST:event_btnImport1ActionPerformed

    private void Add1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Add1ActionPerformed
        // TODO add your handling code here:
        eksportexcel();
    }//GEN-LAST:event_Add1ActionPerformed

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
            java.util.logging.Logger.getLogger(Supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Supplier().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private usu.widget.ButtonGlass Add;
    private usu.widget.ButtonGlass Add1;
    private javax.swing.JTextArea Alamat;
    private usu.widget.ButtonGlass Browse;
    private javax.swing.JComboBox ComboBoxSearch;
    private usu.widget.ButtonGlass Delete;
    private usu.widget.ButtonGlass Edit;
    private javax.swing.JTextField KodeSupplier;
    private javax.swing.JLabel LExpired1;
    private javax.swing.JLabel LJenisObat;
    private javax.swing.JLabel LKategoriObat;
    private javax.swing.JLabel LKodeObat;
    private javax.swing.JLabel LNamaObat;
    private javax.swing.JTextField NamaSupplier;
    private javax.swing.JTextField NoTelepon;
    private usu.widget.ButtonGlass Refresh;
    private usu.widget.ButtonGlass Search;
    private javax.swing.JTable TableSupplier;
    private javax.swing.JTextField TextSearch;
    private usu.widget.ButtonGlass btnImport;
    private usu.widget.ButtonGlass btnImport1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtFile;
    // End of variables declaration//GEN-END:variables

    /*    private void setColumnTable() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/

}
