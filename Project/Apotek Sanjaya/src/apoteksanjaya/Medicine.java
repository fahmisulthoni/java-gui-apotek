/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apoteksanjaya;

import db.Connection;
import db.Parameter;
import db.SetTable;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

/**
 * @author Fahmi Sulthoni
 */
public class Medicine extends javax.swing.JInternalFrame{
    ResultSet st;
    Connection con;
     
    public Medicine() {
        con = new Connection(Parameter.HOST_DB, Parameter.USERNAME_DB,Parameter.PASSWORD_DB, Parameter.IPHOST, Parameter.PORT);
        initComponents();
        getTable();
        
    }
    
    public void getTable(){
        String namaKolom[] = {"kode_obat","nama_obat","kode_supplier","kategori_obat","jenis_obat","merek_obat","harga_beli_obat","harga_jual_obat","jumlah_obat","tanggal_masuk","expired"};
        st = con.querySelect(namaKolom, "medicine");
        TableMedicine.setModel(new SetTable(st));
    }
    
    private void getAddMedicine() {
        //java.util.Date tgl=(java.util.Date)this.TanggalMasuk.getDate();
        java.util.Date ex=(java.util.Date)this.Expired.getDate();
        if (KodeObat.getText().equals("")||
                NamaObat.getText().equals("")||
                ComboBoxKodeSupplier.getSelectedItem().equals("Pilih Supplier")||
                ComboBoxKategoriObat.getSelectedItem().equals("Kategori Obat")||
                ComboBoxJenisObat.getSelectedItem().equals("Jenis Obat")||
                MerekObat.getText().equals("")||
                HargaBeliObat.getText().equals("")||
                HargaJualObat.getText().equals("")||
                JumlahObat.getText().equals("")){
            //||new java.sql.Date(ex.getTime()).equals("")
            JOptionPane.showMessageDialog(this, "Lengkapi data obat");
        } else {
            String[] colom = {"kode_obat","nama_obat","kode_supplier","kategori_obat","jenis_obat","merek_obat","harga_beli_Obat","harga_jual_obat","jumlah_obat","expired"};
            String[] value = {KodeObat.getText(),NamaObat.getText(),ComboBoxKodeSupplier.getSelectedItem().toString(),ComboBoxKategoriObat.getSelectedItem().toString(),ComboBoxJenisObat.getSelectedItem().toString(),
                MerekObat.getText(),HargaBeliObat.getText(),HargaJualObat.getText(),JumlahObat.getText(),new java.sql.Date(ex.getTime()).toString()};
            System.out.println(con.queryInsert("medicine", colom, value));
            getTable();            
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan");
            getRefresh();
        }
}
    
    private void getEditMedicine(){
        java.util.Date ex=(java.util.Date)this.Expired.getDate();
        if (KodeObat.getText().equals("")||NamaObat.getText().equals("")||ComboBoxKodeSupplier.getSelectedItem().equals("Kode Supplier")||ComboBoxKategoriObat.getSelectedItem().equals("Kategori Obat") 
                ||ComboBoxJenisObat.getSelectedItem().equals("Jenis Obat")||MerekObat.getText().equals("")||HargaBeliObat.getText().equals("")
                ||HargaJualObat.getText().equals("")|| JumlahObat.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Lengkapi data anda");
        } else {
            String[] column = {"kode_obat","nama_obat","kode_supplier","kategori_obat","jenis_obat","merek_obat","harga_beli_obat","harga_jual_obat","jumlah_obat","expired"};
            String[] value = {KodeObat.getText(),NamaObat.getText(),ComboBoxKodeSupplier.getSelectedItem().toString(),ComboBoxKategoriObat.getSelectedItem().toString(),ComboBoxJenisObat.getSelectedItem().toString(),
                MerekObat.getText(),HargaBeliObat.getText(), HargaJualObat.getText(),JumlahObat.getText(),new java.sql.Date(ex.getTime()).toString()};
            String id = String.valueOf(TableMedicine.getValueAt(TableMedicine.getSelectedRow(), 0));
            System.out.println(con.queryUpdate("medicine", column, value, "kode_obat='" + id + "'"));
            getTable();
            JOptionPane.showMessageDialog(this, "Data berhasil dirubah");
            getRefresh();
        }
    }
    
    public void getDeleteMedicine(){
        String id = String.valueOf(TableMedicine.getValueAt(TableMedicine.getSelectedRow(), 0));
        if (JOptionPane.showConfirmDialog(this, "Anda yakin ingin menghapus ?", "Peringatan!!!", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            con.queryDelete("medicine", "kode_obat='" + id + "'");
        } else {
            return;
        }
        getTable();
        JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
        getRefresh();
    }
    
    public void getSearchMedicine(){
        if (TextSearch.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Masukkan yang anda cari !!!");
        } else {
            if (ComboBoxSearch.getSelectedItem().equals("Kode Obat")) {
                st = con.querySelectAll("medicine", "kode_obat LIKE '%" + TextSearch.getText() + "%' ");
                TableMedicine.setModel(new SetTable(st));
            } else if (ComboBoxSearch.getSelectedItem().equals("Nama Obat")) {
                st = con.querySelectAll("medicine", "nama_obat LIKE '%" + TextSearch.getText() + "%' ");
                TableMedicine.setModel(new SetTable(st));
            } else if (ComboBoxSearch.getSelectedItem().equals("Kategori Obat")) {
                st = con.querySelectAll("medicine", "kategori_obat LIKE '%" + TextSearch.getText() + "%' ");
                TableMedicine.setModel(new SetTable(st));
            } else if (ComboBoxSearch.getSelectedItem().equals("Jenis Obat")) {
                st = con.querySelectAll("medicine", "jenis_obat LIKE '%" + TextSearch.getText() + "%' ");
                TableMedicine.setModel(new SetTable(st));
            } else if (ComboBoxSearch.getSelectedItem().equals("Merek Obat")) {
                st = con.querySelectAll("medicine", "merek_obat LIKE '%" + TextSearch.getText() + "%' ");
                TableMedicine.setModel(new SetTable(st));
            } 
        }
    } 
     
    public void getMouseClick(){
        //java.util.Date ex=(java.util.Date)this.Expired.getDate();
        KodeObat.setText(String.valueOf(TableMedicine.getValueAt(TableMedicine.getSelectedRow(), 0)));
        NamaObat.setText(String.valueOf(TableMedicine.getValueAt(TableMedicine.getSelectedRow(), 1)));
        ComboBoxKodeSupplier.setSelectedItem(String.valueOf(TableMedicine.getValueAt(TableMedicine.getSelectedRow(),2)));
        ComboBoxKategoriObat.setSelectedItem(String.valueOf(TableMedicine.getValueAt(TableMedicine.getSelectedRow(),3)));
        ComboBoxJenisObat.setSelectedItem(String.valueOf(TableMedicine.getValueAt(TableMedicine.getSelectedRow(),4)));
        MerekObat.setText(String.valueOf(TableMedicine.getValueAt(TableMedicine.getSelectedRow(), 5)));
        HargaBeliObat.setText(String.valueOf(TableMedicine.getValueAt(TableMedicine.getSelectedRow(), 6)));
        HargaJualObat.setText(String.valueOf(TableMedicine.getValueAt(TableMedicine.getSelectedRow(), 7)));
        JumlahObat.setText(String.valueOf(TableMedicine.getValueAt(TableMedicine.getSelectedRow(), 8)));
        String dateValue = String.valueOf(TableMedicine.getValueAt(TableMedicine.getSelectedRow(), 9));
        java.util.Date date=null;
        try{
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateValue);
        }catch(ParseException ex){
            System.out.println(ex);
        }
        Expired.setDate(date);
    }
    
    public void getRefresh(){
        KodeObat.setText(null);
        NamaObat.setText(null);
        ComboBoxKodeSupplier.setSelectedItem("Pilih Supplier");
        ComboBoxKategoriObat.setSelectedItem("Kategori Obat");
        ComboBoxJenisObat.setSelectedItem("Jenis Obat");
        MerekObat.setText(null);
        HargaBeliObat.setText(null);
        HargaJualObat.setText(null);
        JumlahObat.setText(null);
        Expired.setDate(null);
    }
    
    public void eksportexcel(){
        FileWriter fileWriter;
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("[B]export_output/excel[/B]"));
        int retrival = chooser.showSaveDialog(null);
        if (retrival == JFileChooser.APPROVE_OPTION) {
            try{
                TableModel tModel = TableMedicine.getModel();
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
        KodeObat = new javax.swing.JTextField();
        LKodeObat = new javax.swing.JLabel();
        LNamaObat = new javax.swing.JLabel();
        LJenisObat = new javax.swing.JLabel();
        LMerekObat = new javax.swing.JLabel();
        LJumlahStockObat = new javax.swing.JLabel();
        LExpired = new javax.swing.JLabel();
        LKategoriObat = new javax.swing.JLabel();
        ComboBoxKategoriObat = new javax.swing.JComboBox();
        ComboBoxJenisObat = new javax.swing.JComboBox();
        LHargaJualObat = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableMedicine = new javax.swing.JTable();
        Refresh = new usu.widget.ButtonGlass();
        Add = new usu.widget.ButtonGlass();
        Edit = new usu.widget.ButtonGlass();
        NamaObat = new javax.swing.JTextField();
        HargaJualObat = new javax.swing.JTextField();
        JumlahObat = new javax.swing.JTextField();
        Delete = new usu.widget.ButtonGlass();
        jLabel10 = new javax.swing.JLabel();
        ComboBoxSearch = new javax.swing.JComboBox();
        TextSearch = new javax.swing.JTextField();
        Search = new usu.widget.ButtonGlass();
        Expired = new com.toedter.calendar.JDateChooser();
        LHargaBeliObat = new javax.swing.JLabel();
        HargaBeliObat = new javax.swing.JTextField();
        MerekObat = new javax.swing.JTextField();
        LKategoriObat1 = new javax.swing.JLabel();
        ComboBoxKodeSupplier = new javax.swing.JComboBox();
        Add1 = new usu.widget.ButtonGlass();

        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameActivated(evt);
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Medicine");

        LKodeObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LKodeObat.setForeground(new java.awt.Color(255, 255, 255));
        LKodeObat.setText("Kode Obat");

        LNamaObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LNamaObat.setForeground(new java.awt.Color(255, 255, 255));
        LNamaObat.setText("Nama Obat");

        LJenisObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LJenisObat.setForeground(new java.awt.Color(255, 255, 255));
        LJenisObat.setText("Jenis Obat");

        LMerekObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LMerekObat.setForeground(new java.awt.Color(255, 255, 255));
        LMerekObat.setText("Merek Obat");

        LJumlahStockObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LJumlahStockObat.setForeground(new java.awt.Color(255, 255, 255));
        LJumlahStockObat.setText("Jumlah Obat");

        LExpired.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LExpired.setForeground(new java.awt.Color(255, 255, 255));
        LExpired.setText("Expired");

        LKategoriObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LKategoriObat.setForeground(new java.awt.Color(255, 255, 255));
        LKategoriObat.setText("Kategori Obat");

        ComboBoxKategoriObat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kategori Obat", "Obat Dalam", "Obat Luar" }));

        ComboBoxJenisObat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Jenis Obat", "Kaplet", "Pil", "Kapsul", "Botol", "Sirup", "Salep" }));

        LHargaJualObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LHargaJualObat.setForeground(new java.awt.Color(255, 255, 255));
        LHargaJualObat.setText("Harga Jual Obat");

        TableMedicine.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Kode Obat", "Nama Obat", "Kode Supplier", "Kategori Obat", "Jenis Obat", "Merek Obat", "Harga Beli Obat", "Harga Jual Obat", "Stock Obat", "Tanggal Masuk", "Expired"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        TableMedicine.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableMedicineMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TableMedicine);

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

        ComboBoxSearch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kode Obat", "Nama Obat", "Kategori Obat", "Jenis Obat", "Merek Obat" }));

        Search.setForeground(new java.awt.Color(255, 255, 255));
        Search.setText("Search");
        Search.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchActionPerformed(evt);
            }
        });

        Expired.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                ExpiredPropertyChange(evt);
            }
        });

        LHargaBeliObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LHargaBeliObat.setForeground(new java.awt.Color(255, 255, 255));
        LHargaBeliObat.setText("Harga Beli Obat");

        LKategoriObat1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LKategoriObat1.setForeground(new java.awt.Color(255, 255, 255));
        LKategoriObat1.setText("Supplier");

        ComboBoxKodeSupplier.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pilih Supplier" }));

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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LMerekObat)
                                    .addComponent(LJenisObat)
                                    .addComponent(LKategoriObat)
                                    .addComponent(LKodeObat)
                                    .addComponent(LNamaObat))
                                .addGap(37, 37, 37)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(ComboBoxKategoriObat, 0, 280, Short.MAX_VALUE)
                                    .addComponent(ComboBoxJenisObat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(KodeObat)
                                    .addComponent(NamaObat)
                                    .addComponent(MerekObat))
                                .addGap(66, 66, 66)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LJumlahStockObat)
                                    .addComponent(LHargaJualObat)
                                    .addComponent(LExpired)
                                    .addComponent(LHargaBeliObat)
                                    .addComponent(LKategoriObat1))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ComboBoxSearch, 0, 176, Short.MAX_VALUE)
                            .addComponent(TextSearch))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(274, 274, 274)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(JumlahObat, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                    .addComponent(HargaJualObat)
                                    .addComponent(HargaBeliObat)
                                    .addComponent(Expired, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                                    .addComponent(ComboBoxKodeSupplier, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 161, Short.MAX_VALUE)
                                .addComponent(Add1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Add, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Edit, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30))))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(460, 460, 460)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(Add, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Edit, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Add1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(ComboBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(TextSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(KodeObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LHargaBeliObat)
                            .addComponent(HargaBeliObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LKodeObat)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NamaObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HargaJualObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LHargaJualObat)
                    .addComponent(LNamaObat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LJumlahStockObat)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(ComboBoxKategoriObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(LKategoriObat)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ComboBoxJenisObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LExpired)
                            .addComponent(LJenisObat)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(JumlahObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(Expired, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(MerekObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(LMerekObat))
                    .addComponent(ComboBoxKodeSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LKategoriObat1))
                .addGap(31, 31, 31))
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

    private void AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddActionPerformed
        getAddMedicine();// TODO add your handling code here:
    }//GEN-LAST:event_AddActionPerformed

    private void EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditActionPerformed
        getEditMedicine();// TODO add your handling code here:
    }//GEN-LAST:event_EditActionPerformed

    private void DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteActionPerformed
        getDeleteMedicine();// TODO add your handling code here:
    }//GEN-LAST:event_DeleteActionPerformed

    private void RefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshActionPerformed
        getRefresh();// TODO add your handling code here:
        getTable();
    }//GEN-LAST:event_RefreshActionPerformed

    private void TableMedicineMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableMedicineMouseClicked
       getMouseClick();// TODO add your handling code here:
    }//GEN-LAST:event_TableMedicineMouseClicked

    private void ExpiredPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_ExpiredPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_ExpiredPropertyChange

    private void SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchActionPerformed
        getSearchMedicine();// TODO add your handling code here:
    }//GEN-LAST:event_SearchActionPerformed

    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameActivated
        String namaKolom[] = {"nama_supplier","kode_supplier"};
        st = con.querySelect(namaKolom, "supplier");
        try {
            while(st.next()){
                ComboBoxKodeSupplier.addItem(st.getString("kode_supplier"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Medicine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formInternalFrameActivated

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
            java.util.logging.Logger.getLogger(Medicine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Medicine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Medicine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Medicine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Medicine().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private usu.widget.ButtonGlass Add;
    private usu.widget.ButtonGlass Add1;
    private javax.swing.JComboBox ComboBoxJenisObat;
    private javax.swing.JComboBox ComboBoxKategoriObat;
    private javax.swing.JComboBox ComboBoxKodeSupplier;
    private javax.swing.JComboBox ComboBoxSearch;
    private usu.widget.ButtonGlass Delete;
    private usu.widget.ButtonGlass Edit;
    private com.toedter.calendar.JDateChooser Expired;
    private javax.swing.JTextField HargaBeliObat;
    private javax.swing.JTextField HargaJualObat;
    private javax.swing.JTextField JumlahObat;
    private javax.swing.JTextField KodeObat;
    private javax.swing.JLabel LExpired;
    private javax.swing.JLabel LHargaBeliObat;
    private javax.swing.JLabel LHargaJualObat;
    private javax.swing.JLabel LJenisObat;
    private javax.swing.JLabel LJumlahStockObat;
    private javax.swing.JLabel LKategoriObat;
    private javax.swing.JLabel LKategoriObat1;
    private javax.swing.JLabel LKodeObat;
    private javax.swing.JLabel LMerekObat;
    private javax.swing.JLabel LNamaObat;
    private javax.swing.JTextField MerekObat;
    private javax.swing.JTextField NamaObat;
    private usu.widget.ButtonGlass Refresh;
    private usu.widget.ButtonGlass Search;
    private javax.swing.JTable TableMedicine;
    private javax.swing.JTextField TextSearch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
