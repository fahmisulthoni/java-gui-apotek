/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apoteksanjaya;

import db.Connection;
import db.Parameter;
import db.SetTable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * @author Fahmi Sulthoni
 */
public class Transaction extends javax.swing.JInternalFrame{

    ResultSet st;
    Connection con;
    int old, dec, now;
    String newstock, sql;

    /** Creates new form Transaction */
    public Transaction() {
        con = new db.Connection(new Parameter().HOST_DB, new Parameter().USERNAME_DB, new Parameter().PASSWORD_DB, new Parameter().IPHOST, new Parameter().PORT);
        initComponents();
        getTable();
        long noTransaksi = System.currentTimeMillis();
        KodeTransaksi.setText(String.valueOf(noTransaksi));
    }
    
    
    public void getTable() {
        String column[] = {"kode_obat", "nama_obat", "kategori_obat", "jenis_obat", "merek_obat", "harga_beli_obat", "harga_jual_obat", "jumlah_obat", "tanggal_masuk", "expired"};
        st = con.querySelect(column, "medicine");
        TableMedicine.setModel(new SetTable(st));
    }

    public void getView() {
        String column[] = {"id_jual", "kode_transaksi", "kode_obat", "nama_obat", "merek_obat", "harga_jual", "jumlah_beli", "total_harga"};
        st = con.fcSelectCommand(column, "penjualan", "kode_transaksi='" + KodeTransaksi.getText() + "'");
        TableTransaksi.setModel(new SetTable(st));
    }

    public void getSearchMedicine() {
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

    public void getAddCart() {
        if (KodeObat.getText().equals("") || NamaObat.getText().equals("")
                || MerekObat.getText().equals("") || HargaJualObat.getText().equals("") || JumlahBeli.getText().equals("") || LSubTotal.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Silahkan lengkapi data terlebih dahulu !!! ");
        } else if (KodeTransaksi.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Silahkan Masukan Kode Transaksi Secara Unik !!!");
        } else {
            String[] column = {"kode_transaksi", "kode_obat", "nama_obat", "merek_obat", "harga_jual", "jumlah_beli", "total_harga"};
            String[] value = {KodeTransaksi.getText(), KodeObat.getText(), NamaObat.getText(), MerekObat.getText(), HargaJualObat.getText(), JumlahBeli.getText(), LSubTotal.getText()};
            System.out.println(con.queryInsert("penjualan", column, value));
            try {
                if (!getCheck_stock()) {
                    JOptionPane.showMessageDialog(this, "Stock obat sudah habis");
                } else {
                    getMin();
                    getTable();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex);
            }
            getSubTotal();
            getTotal();
            getView();
            /*getCancel();*/
        }
    }

    public boolean getCheck_stock() throws SQLException {
        boolean checkstock;
        st = con.querySelectAll("medicine", "kode_obat='" + KodeObat.getText() + "'");
        while (st.next()) {
            old = st.getInt("jumlah_obat");
        }
        dec = Integer.parseInt(JumlahBeli.getText());
        if (old < dec) {
            checkstock = false;
        } else {
            checkstock = true;
        }
        return checkstock;
    }

    public void getMin() throws SQLException {
        st = con.querySelectAll("medicine", "kode_obat='" + KodeObat.getText() + "'");
        while (st.next()) {
            old = st.getInt("jumlah_obat");
        }
        dec = Integer.parseInt(JumlahBeli.getText());
        now = old - dec;
        newstock = Integer.toString(now);
        String a = String.valueOf(newstock);
        String[] kolom = {"jumlah_obat"};
        String[] isi = {a};
        System.out.println(con.queryUpdate("medicine", kolom, isi, "kode_obat='" + KodeObat.getText() + "'"));
    }

    public void getMouseClick() {
        //KodeTransaksi.setText(String.valueOf(TableMedicine.getValueAt(TableMedicine.getSelectedRow(), 0)));
        KodeObat.setText(String.valueOf(TableMedicine.getValueAt(TableMedicine.getSelectedRow(), 0)));
        NamaObat.setText(String.valueOf(TableMedicine.getValueAt(TableMedicine.getSelectedRow(), 1)));
        MerekObat.setText(String.valueOf(TableMedicine.getValueAt(TableMedicine.getSelectedRow(), 4)));
        HargaJualObat.setText(String.valueOf(TableMedicine.getValueAt(TableMedicine.getSelectedRow(), 6)));
        JumlahBeli.setText("");
        LSubTotal.setText("");
        String dateValue = String.valueOf(TableMedicine.getValueAt(TableMedicine.getSelectedRow(), 9));
        java.util.Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateValue);
        } catch (ParseException ex) {
            System.out.println(ex);
        }
        Expired.setDate(date);
    }

    public void getSubTotal() {
        Integer a = Integer.parseInt(HargaJualObat.getText());
        Integer b = Integer.parseInt(JumlahBeli.getText());
        Integer c = a * b;
        LSubTotal.setText(String.valueOf(c));
        //LSubTotal.setText(""+c);
    }

    public void getTotal() {
        st = con.eksekusiQuery("SELECT SUM(total_harga) as total_harga FROM penjualan WHERE kode_transaksi = '" + KodeTransaksi.getText() + "'");
        try {
            st.next();
            LSubTotal1.setText(st.getString("total_harga"));
        } catch (SQLException ex) {
            Logger.getLogger(Transaction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getPaymen() {
        int a = Integer.parseInt(LSubTotal1.getText());
        int b = Integer.parseInt(Tunai.getText());
        int c = b - a;
        Kembalian.setText(Integer.toString(c));
    }

    public void getCancel() {
        KodeTransaksi.setText(null);
        KodeObat.setText(null);
        NamaObat.setText(null);
        MerekObat.setText(null);
        HargaJualObat.setText(null);
        JumlahBeli.setText(null);
        Expired.setDate(null);
        LSubTotal.setText(null);
    }

    public void getDelete() {
        String id = String.valueOf(TableTransaksi.getValueAt(TableTransaksi.getSelectedRow(), 0));
        if (JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini", "Peringatan!!!", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            con.queryDelete("penjualan", "id_jual=" + id);
        } else {
            return;
        }
        getTable();
        getView();
        JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
        getTotal();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableMedicine = new javax.swing.JTable();
        TextSearch = new javax.swing.JTextField();
        Search = new usu.widget.ButtonGlass();
        jLabel10 = new javax.swing.JLabel();
        ComboBoxSearch = new javax.swing.JComboBox();
        panel1 = new usu.widget.Panel();
        KodeObat = new javax.swing.JTextField();
        LKodeObat = new javax.swing.JLabel();
        NamaObat = new javax.swing.JTextField();
        LNamaObat = new javax.swing.JLabel();
        MerekObat = new javax.swing.JTextField();
        LMerekObat = new javax.swing.JLabel();
        HargaJualObat = new javax.swing.JTextField();
        LHargaJualObat = new javax.swing.JLabel();
        JumlahBeli = new javax.swing.JTextField();
        LJumlahBeli = new javax.swing.JLabel();
        Expired = new com.toedter.calendar.JDateChooser();
        LExpired1 = new javax.swing.JLabel();
        LKodeTransaksi = new javax.swing.JLabel();
        KodeTransaksi = new javax.swing.JLabel();
        panel2 = new usu.widget.Panel();
        jLabel1 = new javax.swing.JLabel();
        panelGlass4 = new usu.widget.glass.PanelGlass();
        Cancel = new usu.widget.ButtonGlass();
        AddCart = new usu.widget.ButtonGlass();
        panelGlass1 = new usu.widget.glass.PanelGlass();
        LSubTotal = new usu.widget.Label();
        LKoma = new usu.widget.Label();
        label1 = new usu.widget.Label();
        panelGlass5 = new usu.widget.glass.PanelGlass();
        panelGlass3 = new usu.widget.glass.PanelGlass();
        label3 = new usu.widget.Label();
        panelGlass2 = new usu.widget.glass.PanelGlass();
        LKoma1 = new usu.widget.Label();
        label2 = new usu.widget.Label();
        LSubTotal1 = new usu.widget.TextBox();
        panelGlass8 = new usu.widget.glass.PanelGlass();
        LKoma2 = new usu.widget.Label();
        label5 = new usu.widget.Label();
        Tunai = new usu.widget.TextBox();
        panelGlass7 = new usu.widget.glass.PanelGlass();
        label4 = new usu.widget.Label();
        panelGlass10 = new usu.widget.glass.PanelGlass();
        LKoma3 = new usu.widget.Label();
        label7 = new usu.widget.Label();
        Kembalian = new usu.widget.TextBox();
        panelGlass9 = new usu.widget.glass.PanelGlass();
        label6 = new usu.widget.Label();
        Cetak = new usu.widget.ButtonGlass();
        Delete1 = new usu.widget.ButtonGlass();
        panelGlass6 = new usu.widget.glass.PanelGlass();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableTransaksi = new javax.swing.JTable();

        setFocusable(false);

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));

        TableMedicine.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Kode Obat", "Nama Obat", "Kategori Obat", "Jenis Obat", "Merek Obat", "Harga Jual Obat", "Stock Obat", "Expired"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
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

        Search.setForeground(new java.awt.Color(255, 255, 255));
        Search.setText("Search");
        Search.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchActionPerformed(evt);
            }
        });

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Search Categories");

        ComboBoxSearch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kode Obat", "Nama Obat", "Kategori Obat", "Jenis Obat", "Merek Obat" }));

        panel1.setBackground(new java.awt.Color(0, 102, 102));

        LKodeObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LKodeObat.setForeground(new java.awt.Color(255, 255, 255));
        LKodeObat.setText("Kode Obat");

        LNamaObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LNamaObat.setForeground(new java.awt.Color(255, 255, 255));
        LNamaObat.setText("Nama Obat");

        LMerekObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LMerekObat.setForeground(new java.awt.Color(255, 255, 255));
        LMerekObat.setText("Merek Obat");

        LHargaJualObat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LHargaJualObat.setForeground(new java.awt.Color(255, 255, 255));
        LHargaJualObat.setText("Harga Jual");

        JumlahBeli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                JumlahBeliKeyReleased(evt);
            }
        });

        LJumlahBeli.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LJumlahBeli.setForeground(new java.awt.Color(255, 255, 255));
        LJumlahBeli.setText("Jumlah Beli");

        Expired.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                ExpiredPropertyChange(evt);
            }
        });

        LExpired1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LExpired1.setForeground(new java.awt.Color(255, 255, 255));
        LExpired1.setText("Expired");

        LKodeTransaksi.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LKodeTransaksi.setForeground(new java.awt.Color(255, 255, 255));
        LKodeTransaksi.setText("Kode Transaksi");

        KodeTransaksi.setForeground(new java.awt.Color(255, 255, 255));
        KodeTransaksi.setText("-");

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                        .addComponent(LNamaObat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(NamaObat, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                        .addComponent(LMerekObat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                        .addComponent(MerekObat, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                        .addComponent(LHargaJualObat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(HargaJualObat, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LJumlahBeli)
                            .addComponent(LExpired1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(JumlahBeli)
                            .addComponent(Expired, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LKodeObat)
                            .addComponent(LKodeTransaksi))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(KodeObat, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(KodeTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LKodeTransaksi)
                    .addComponent(KodeTransaksi))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(KodeObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LKodeObat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NamaObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LNamaObat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LMerekObat)
                    .addComponent(MerekObat, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LHargaJualObat)
                    .addComponent(HargaJualObat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JumlahBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LJumlahBeli))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Expired, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LExpired1))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        panel2.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setBackground(new java.awt.Color(0, 0, 153));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Keranjang Belanja");

        javax.swing.GroupLayout panel2Layout = new javax.swing.GroupLayout(panel2);
        panel2.setLayout(panel2Layout);
        panel2Layout.setHorizontalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel2Layout.setVerticalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Cancel.setForeground(new java.awt.Color(255, 255, 255));
        Cancel.setText("Batal");
        Cancel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelActionPerformed(evt);
            }
        });

        AddCart.setForeground(new java.awt.Color(255, 255, 255));
        AddCart.setText("Tambah");
        AddCart.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        AddCart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddCartActionPerformed(evt);
            }
        });

        panelGlass1.setBackgroundImageType(usu.widget.constan.BackgroundConstan.BACKGROUND_IMAGE_CENTER_TOP);
        panelGlass1.setOpaqueGradient(true);
        panelGlass1.setOpaqueImage(false);
        panelGlass1.setRound(false);
        panelGlass1.setWarna(new java.awt.Color(0, 204, 0));

        LSubTotal.setForeground(new java.awt.Color(255, 255, 255));
        LSubTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        LSubTotal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        LKoma.setForeground(new java.awt.Color(255, 255, 255));
        LKoma.setText(",-");
        LKoma.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        label1.setForeground(new java.awt.Color(255, 255, 255));
        label1.setText("Rp.");
        label1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        javax.swing.GroupLayout panelGlass1Layout = new javax.swing.GroupLayout(panelGlass1);
        panelGlass1.setLayout(panelGlass1Layout);
        panelGlass1Layout.setHorizontalGroup(
            panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LKoma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelGlass1Layout.setVerticalGroup(
            panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGlass1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LKoma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelGlass4Layout = new javax.swing.GroupLayout(panelGlass4);
        panelGlass4.setLayout(panelGlass4Layout);
        panelGlass4Layout.setHorizontalGroup(
            panelGlass4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass4Layout.createSequentialGroup()
                .addContainerGap(76, Short.MAX_VALUE)
                .addGroup(panelGlass4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGlass4Layout.createSequentialGroup()
                        .addComponent(Cancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(AddCart, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelGlass1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(63, 63, 63))
        );
        panelGlass4Layout.setVerticalGroup(
            panelGlass4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGlass4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelGlass1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelGlass4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AddCart, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panelGlass3.setRound(false);

        label3.setForeground(new java.awt.Color(255, 255, 255));
        label3.setText("Total");
        label3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        javax.swing.GroupLayout panelGlass3Layout = new javax.swing.GroupLayout(panelGlass3);
        panelGlass3.setLayout(panelGlass3Layout);
        panelGlass3Layout.setHorizontalGroup(
            panelGlass3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass3Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelGlass3Layout.setVerticalGroup(
            panelGlass3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelGlass2.setBackgroundImageType(usu.widget.constan.BackgroundConstan.BACKGROUND_IMAGE_CENTER_TOP);
        panelGlass2.setOpaqueGradient(true);
        panelGlass2.setOpaqueImage(false);
        panelGlass2.setRound(false);
        panelGlass2.setWarna(new java.awt.Color(0, 204, 0));

        LKoma1.setForeground(new java.awt.Color(255, 255, 255));
        LKoma1.setText(",-");
        LKoma1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        label2.setForeground(new java.awt.Color(255, 255, 255));
        label2.setText("Rp.");
        label2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        LSubTotal1.setBackground(new java.awt.Color(153, 153, 153));
        LSubTotal1.setForeground(new java.awt.Color(255, 255, 255));
        LSubTotal1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        LSubTotal1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        LSubTotal1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                LSubTotal1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout panelGlass2Layout = new javax.swing.GroupLayout(panelGlass2);
        panelGlass2.setLayout(panelGlass2Layout);
        panelGlass2Layout.setHorizontalGroup(
            panelGlass2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LSubTotal1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LKoma1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelGlass2Layout.setVerticalGroup(
            panelGlass2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGlass2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LKoma1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LSubTotal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelGlass8.setBackgroundImageType(usu.widget.constan.BackgroundConstan.BACKGROUND_IMAGE_CENTER_TOP);
        panelGlass8.setOpaqueGradient(true);
        panelGlass8.setOpaqueImage(false);
        panelGlass8.setRound(false);
        panelGlass8.setWarna(new java.awt.Color(0, 204, 0));

        LKoma2.setForeground(new java.awt.Color(255, 255, 255));
        LKoma2.setText(",-");
        LKoma2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        label5.setForeground(new java.awt.Color(255, 255, 255));
        label5.setText("Rp.");
        label5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        Tunai.setBackground(new java.awt.Color(153, 153, 153));
        Tunai.setForeground(new java.awt.Color(255, 255, 255));
        Tunai.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Tunai.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Tunai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TunaiKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout panelGlass8Layout = new javax.swing.GroupLayout(panelGlass8);
        panelGlass8.setLayout(panelGlass8Layout);
        panelGlass8Layout.setHorizontalGroup(
            panelGlass8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Tunai, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LKoma2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelGlass8Layout.setVerticalGroup(
            panelGlass8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGlass8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LKoma2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Tunai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelGlass7.setRound(false);

        label4.setForeground(new java.awt.Color(255, 255, 255));
        label4.setText("Tunai");
        label4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        javax.swing.GroupLayout panelGlass7Layout = new javax.swing.GroupLayout(panelGlass7);
        panelGlass7.setLayout(panelGlass7Layout);
        panelGlass7Layout.setHorizontalGroup(
            panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass7Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelGlass7Layout.setVerticalGroup(
            panelGlass7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelGlass10.setBackgroundImageType(usu.widget.constan.BackgroundConstan.BACKGROUND_IMAGE_CENTER_TOP);
        panelGlass10.setOpaqueGradient(true);
        panelGlass10.setOpaqueImage(false);
        panelGlass10.setRound(false);
        panelGlass10.setWarna(new java.awt.Color(0, 204, 0));

        LKoma3.setForeground(new java.awt.Color(255, 255, 255));
        LKoma3.setText(",-");
        LKoma3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        label7.setForeground(new java.awt.Color(255, 255, 255));
        label7.setText("Rp.");
        label7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        Kembalian.setBackground(new java.awt.Color(153, 153, 153));
        Kembalian.setForeground(new java.awt.Color(255, 255, 255));
        Kembalian.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Kembalian.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        javax.swing.GroupLayout panelGlass10Layout = new javax.swing.GroupLayout(panelGlass10);
        panelGlass10.setLayout(panelGlass10Layout);
        panelGlass10Layout.setHorizontalGroup(
            panelGlass10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(label7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LKoma3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelGlass10Layout.setVerticalGroup(
            panelGlass10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGlass10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelGlass10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(LKoma3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(label7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelGlass9.setRound(false);

        label6.setForeground(new java.awt.Color(255, 255, 255));
        label6.setText("Kembalian");
        label6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        javax.swing.GroupLayout panelGlass9Layout = new javax.swing.GroupLayout(panelGlass9);
        panelGlass9.setLayout(panelGlass9Layout);
        panelGlass9Layout.setHorizontalGroup(
            panelGlass9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass9Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        panelGlass9Layout.setVerticalGroup(
            panelGlass9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Cetak.setForeground(new java.awt.Color(255, 255, 255));
        Cetak.setText("Cetak");
        Cetak.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Cetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CetakActionPerformed(evt);
            }
        });

        Delete1.setForeground(new java.awt.Color(255, 255, 255));
        Delete1.setText("Hapus");
        Delete1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Delete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Delete1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelGlass5Layout = new javax.swing.GroupLayout(panelGlass5);
        panelGlass5.setLayout(panelGlass5Layout);
        panelGlass5Layout.setHorizontalGroup(
            panelGlass5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelGlass5Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(Delete1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(Cetak, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68)
                .addGroup(panelGlass5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelGlass7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelGlass9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelGlass3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(panelGlass5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelGlass10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelGlass2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelGlass8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38))
        );
        panelGlass5Layout.setVerticalGroup(
            panelGlass5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGlass5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(panelGlass3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelGlass2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGlass5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelGlass7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelGlass8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelGlass5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Cetak, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Delete1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelGlass5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelGlass10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelGlass9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        TableTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "id_jual", "Kode Transaksi", "Kode Obat", "Nama Obat", "Harga Obat", "Jumlah Beli", "Total Harga"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(TableTransaksi);

        javax.swing.GroupLayout panelGlass6Layout = new javax.swing.GroupLayout(panelGlass6);
        panelGlass6.setLayout(panelGlass6Layout);
        panelGlass6Layout.setHorizontalGroup(
            panelGlass6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass6Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 718, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        panelGlass6Layout.setVerticalGroup(
            panelGlass6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGlass6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelGlass4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(panelGlass6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(panelGlass5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(20, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(TextSearch, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ComboBoxSearch, javax.swing.GroupLayout.Alignment.LEADING, 0, 174, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(panel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panelGlass4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(ComboBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TextSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(panel2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelGlass6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelGlass5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(36, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Delete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Delete1ActionPerformed
        getDelete();// TODO add your handling code here:
    }//GEN-LAST:event_Delete1ActionPerformed

    private void CetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CetakActionPerformed
        Nota n = new Nota();
        n.setVisible(true);
        getPaymen();// TODO add your handling code here:
    }//GEN-LAST:event_CetakActionPerformed

    private void TunaiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TunaiKeyReleased
        getPaymen();// TODO add your handling code here:
    }//GEN-LAST:event_TunaiKeyReleased

    private void LSubTotal1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LSubTotal1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_LSubTotal1KeyReleased

    private void AddCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddCartActionPerformed
        getAddCart();// TODO add your handling code here:
        //getCancel();
    }//GEN-LAST:event_AddCartActionPerformed

    private void CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelActionPerformed
        getCancel();// TODO add your handling code here:
        getTable();
    }//GEN-LAST:event_CancelActionPerformed

    private void ExpiredPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_ExpiredPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_ExpiredPropertyChange

    private void JumlahBeliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JumlahBeliKeyReleased
        getSubTotal();// TODO add your handling code here:
    }//GEN-LAST:event_JumlahBeliKeyReleased

    private void SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchActionPerformed
        getSearchMedicine();//getSearchMedicine();// TODO add your handling code here:
    }//GEN-LAST:event_SearchActionPerformed

    private void TableMedicineMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableMedicineMouseClicked
        getMouseClick();//getMouseClick();// TODO add your handling code here:
    }//GEN-LAST:event_TableMedicineMouseClicked

    /**
     * @param args the command line arguments
     */
    
      
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static usu.widget.ButtonGlass AddCart;
    public static usu.widget.ButtonGlass Cancel;
    public static usu.widget.ButtonGlass Cetak;
    public static javax.swing.JComboBox ComboBoxSearch;
    public static usu.widget.ButtonGlass Delete1;
    public static com.toedter.calendar.JDateChooser Expired;
    public static javax.swing.JTextField HargaJualObat;
    public static javax.swing.JTextField JumlahBeli;
    public static usu.widget.TextBox Kembalian;
    public static javax.swing.JTextField KodeObat;
    public static javax.swing.JLabel KodeTransaksi;
    public static javax.swing.JLabel LExpired1;
    public static javax.swing.JLabel LHargaJualObat;
    public static javax.swing.JLabel LJumlahBeli;
    public static javax.swing.JLabel LKodeObat;
    public static javax.swing.JLabel LKodeTransaksi;
    public static usu.widget.Label LKoma;
    public static usu.widget.Label LKoma1;
    public static usu.widget.Label LKoma2;
    public static usu.widget.Label LKoma3;
    public static javax.swing.JLabel LMerekObat;
    public static javax.swing.JLabel LNamaObat;
    public static usu.widget.Label LSubTotal;
    public static usu.widget.TextBox LSubTotal1;
    public static javax.swing.JTextField MerekObat;
    public static javax.swing.JTextField NamaObat;
    public static usu.widget.ButtonGlass Search;
    public static javax.swing.JTable TableMedicine;
    public static javax.swing.JTable TableTransaksi;
    public static javax.swing.JTextField TextSearch;
    public static usu.widget.TextBox Tunai;
    public static javax.swing.JLabel jLabel1;
    public static javax.swing.JLabel jLabel10;
    public static javax.swing.JPanel jPanel1;
    public static javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JScrollPane jScrollPane2;
    public static usu.widget.Label label1;
    public static usu.widget.Label label2;
    public static usu.widget.Label label3;
    public static usu.widget.Label label4;
    public static usu.widget.Label label5;
    public static usu.widget.Label label6;
    public static usu.widget.Label label7;
    public static usu.widget.Panel panel1;
    public static usu.widget.Panel panel2;
    public static usu.widget.glass.PanelGlass panelGlass1;
    public static usu.widget.glass.PanelGlass panelGlass10;
    public static usu.widget.glass.PanelGlass panelGlass2;
    public static usu.widget.glass.PanelGlass panelGlass3;
    public static usu.widget.glass.PanelGlass panelGlass4;
    public static usu.widget.glass.PanelGlass panelGlass5;
    public static usu.widget.glass.PanelGlass panelGlass6;
    public static usu.widget.glass.PanelGlass panelGlass7;
    public static usu.widget.glass.PanelGlass panelGlass8;
    public static usu.widget.glass.PanelGlass panelGlass9;
    // End of variables declaration//GEN-END:variables
}
