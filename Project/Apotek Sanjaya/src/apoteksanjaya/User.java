/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apoteksanjaya;

import db.Connection;
import db.Parameter;
import db.SetTable;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.ResultSet;
import javax.swing.ButtonGroup;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

/**
 * @author Fahmi Sulthoni
 */
public class User extends javax.swing.JInternalFrame {
    ResultSet st;
    Connection con;

    /**
     * Creates new form User
     */
    public User(){
        con = new Connection(new db.Parameter().HOST_DB, new db.Parameter().USERNAME_DB, new db.Parameter().PASSWORD_DB, new db.Parameter().IPHOST, new db.Parameter().PORT);
        initComponents();
        getButtonGroup();
        getTable();
        Lakilaki.setSelected(true);
    }
    
    public void getTable() {
        String namaKolom[] = {"id_user","nama","jenis_kelamin","username","password","akses","alamat","email","no_hp"};
        st = con.querySelect(namaKolom, "user");
        TableUser.setModel(new SetTable(st));
    }
    
    private void getButtonGroup(){
        ButtonGroup bg = new ButtonGroup();
        bg.add(Lakilaki);
        bg.add(Perempuan);
    }
    
    private void getAddUser() {
        String jk = (Lakilaki.isSelected() ? "Laki-Laki" : "Perempuan"); 
        if (Nama.getText().equals("")||
                Username.getText().equals("")||Password.getText().equals("")
                || ComboBoxAkses.getSelectedItem().equals("Akses")) {
            JOptionPane.showMessageDialog(this, "Lengkapi data anda");
        } else {
            String[] column = {"nama","jenis_kelamin","username","password","akses","alamat","email","no_hp"};
            String[] value = {Nama.getText(),jk,Username.getText(), Password.getText(), 
                ComboBoxAkses.getSelectedItem().toString(),Alamat.getText(),Email.getText(),NoHp.getText()};
            System.out.println(con.queryInsert("user", column, value));
            getTable();
            //JOptionPane.showMessageDialog(this, "Data berhasil disimpan");
            getRefresh();
        }  
    }
      
    public void getEditUser(){
        String jk = (Lakilaki.isSelected() ? "Laki-Laki" : "Perempuan");
        if (Nama.getText().equals("")||Username.getText().equals("") || Password.getText().equals("")
                || ComboBoxAkses.getSelectedItem().equals("Akses")) {
            JOptionPane.showMessageDialog(this, "Lengkapi data anda");
        } else {
            String[] column = {"nama","jenis_kelamin","username","password","akses","alamat","email","no_hp"};
            String[] value = {Nama.getText(),jk,Username.getText(), Password.getText(), 
                ComboBoxAkses.getSelectedItem().toString(),Alamat.getText(),Email.getText(),NoHp.getText()};
            String id = String.valueOf(TableUser.getValueAt(TableUser.getSelectedRow(), 0));
            System.out.println(con.queryUpdate("user", column, value, "id_user='" + id + "'"));
            getTable();
            JOptionPane.showMessageDialog(this, "Data berhasil dirubah");
            getRefresh();
        }
    }
    
    public void getDeleteUser(){
        String id = String.valueOf(TableUser.getValueAt(TableUser.getSelectedRow(), 0));
        if (JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data", "Peringatan!!!", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            con.queryDelete("user", "id_user=" + id);
        } else {
            return;
        }
        getTable();
        JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
        getRefresh();
    }
    
    public void getSearchUser(){
        if (Search.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Masukkan yang anda cari !!!");
        } else {
            if (ComboBoxSearch.getSelectedItem().equals("Nama")) {
                st = con.querySelectAll("user", "nama LIKE '%" + Search.getText() + "%' ");
                TableUser.setModel(new SetTable(st));
            } else if (ComboBoxSearch.getSelectedItem().equals("Username")) {
                st = con.querySelectAll("user", "username LIKE '%" + Search.getText() + "%' ");
                TableUser.setModel(new SetTable(st));
            } else if (ComboBoxSearch.getSelectedItem().equals("Akses")) {
                st = con.querySelectAll("user", "akses LIKE '%" + Search.getText() + "%' ");
                TableUser.setModel(new SetTable(st));
            } 
        }
    }
    
    public void getMouseClick(){
        Nama.setText(String.valueOf(TableUser.getValueAt(TableUser.getSelectedRow(), 1)));
        //ComboBoxJenisKelamin.setSelectedItem(String.valueOf(TableUser.getValueAt(TableUser.getSelectedRow(),2)));
        Username.setText(String.valueOf(TableUser.getValueAt(TableUser.getSelectedRow(), 3)));
        Password.setText(String.valueOf(TableUser.getValueAt(TableUser.getSelectedRow(), 4)));
        ComboBoxAkses.setSelectedItem(String.valueOf(TableUser.getValueAt(TableUser.getSelectedRow(), 5)));
        Alamat.setText(String.valueOf(TableUser.getValueAt(TableUser.getSelectedRow(), 6))); 
        Email.setText(String.valueOf(TableUser.getValueAt(TableUser.getSelectedRow(), 7)));
        NoHp.setText(String.valueOf(TableUser.getValueAt(TableUser.getSelectedRow(), 8)));
        int baris = TableUser.getSelectedRow();
        int kolom = TableUser.getSelectedColumn();
        String data = TableUser.getValueAt(baris, kolom).toString();
        String kolom2 = TableUser.getValueAt(baris, 2).toString();
           try {
               if (kolom2.equals("Laki-Laki")) {
                Lakilaki.setSelected(true);
                Perempuan.setSelected(false);
               }if (kolom2.equals("Perempuan")) {
                Perempuan.setSelected(true);
                Lakilaki.setSelected(false);
               }
           } catch (Exception e) {
        }
    }
    
    public void getRefresh() {
        Nama.setText(null);
        Lakilaki.setSelected(true);
        Username.setText(null);
        Password.setText(null);
        ComboBoxAkses.setSelectedItem("Akses");
        Alamat.setText(null);
        Email.setText(null);
        NoHp.setText(null);
    }
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelSearch = new javax.swing.JPanel();
        Nama = new javax.swing.JTextField();
        jLabelNama = new javax.swing.JLabel();
        Username = new javax.swing.JTextField();
        jLabelUsername = new javax.swing.JLabel();
        jLabelJenisKelamin = new javax.swing.JLabel();
        jLabelPassword = new javax.swing.JLabel();
        Password = new javax.swing.JPasswordField();
        jLabelAkses = new javax.swing.JLabel();
        jLabelAlamat = new javax.swing.JLabel();
        jLabelEmail = new javax.swing.JLabel();
        Email = new javax.swing.JTextField();
        jLabelNoHp = new javax.swing.JLabel();
        NoHp = new javax.swing.JTextField();
        TambahUser = new usu.widget.ButtonGlass();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableUser = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        Alamat = new javax.swing.JTextArea();
        Delete = new usu.widget.ButtonGlass();
        Edit = new usu.widget.ButtonGlass();
        Refresh = new usu.widget.ButtonGlass();
        Search = new javax.swing.JTextField();
        ComboBoxSearch = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        buttonGlassSearch = new usu.widget.ButtonGlass();
        ComboBoxAkses = new javax.swing.JComboBox();
        Lakilaki = new javax.swing.JRadioButton();
        Perempuan = new javax.swing.JRadioButton();

        setClosable(true);

        jPanelSearch.setBackground(new java.awt.Color(0, 153, 153));

        jLabelNama.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelNama.setForeground(new java.awt.Color(255, 255, 255));
        jLabelNama.setText("Nama");

        jLabelUsername.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelUsername.setForeground(new java.awt.Color(255, 255, 255));
        jLabelUsername.setText("Username");

        jLabelJenisKelamin.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelJenisKelamin.setForeground(new java.awt.Color(255, 255, 255));
        jLabelJenisKelamin.setText("Jenis Kelamin");

        jLabelPassword.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelPassword.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPassword.setText("Password");

        jLabelAkses.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAkses.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAkses.setText("Akses");

        jLabelAlamat.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelAlamat.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAlamat.setText("Alamat");

        jLabelEmail.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelEmail.setForeground(new java.awt.Color(255, 255, 255));
        jLabelEmail.setText("Email");

        jLabelNoHp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelNoHp.setForeground(new java.awt.Color(255, 255, 255));
        jLabelNoHp.setText("No HP");

        TambahUser.setForeground(new java.awt.Color(255, 255, 255));
        TambahUser.setText("Add");
        TambahUser.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        TambahUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TambahUserActionPerformed(evt);
            }
        });

        TableUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Nama", "Jenis Kelamin", "Username", "Password", "Akses", "Alamat", "Email", "No Hp"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        TableUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableUserMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TableUser);

        Alamat.setColumns(20);
        Alamat.setRows(5);
        jScrollPane3.setViewportView(Alamat);

        Delete.setForeground(new java.awt.Color(255, 255, 255));
        Delete.setText("Delete");
        Delete.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteActionPerformed(evt);
            }
        });

        Edit.setForeground(new java.awt.Color(255, 255, 255));
        Edit.setText("Edit");
        Edit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditActionPerformed(evt);
            }
        });

        Refresh.setForeground(new java.awt.Color(255, 255, 255));
        Refresh.setText("Refresh");
        Refresh.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        Refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshActionPerformed(evt);
            }
        });

        ComboBoxSearch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nama", "Username", "Akses" }));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Search Categories");

        buttonGlassSearch.setForeground(new java.awt.Color(255, 255, 255));
        buttonGlassSearch.setText("Search");
        buttonGlassSearch.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        buttonGlassSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGlassSearchActionPerformed(evt);
            }
        });

        ComboBoxAkses.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Akses", "Admin", "Kasir", "Apoteker" }));

        Lakilaki.setText("Laki-Laki");

        Perempuan.setText("Perempuan");

        javax.swing.GroupLayout jPanelSearchLayout = new javax.swing.GroupLayout(jPanelSearch);
        jPanelSearch.setLayout(jPanelSearchLayout);
        jPanelSearchLayout.setHorizontalGroup(
            jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSearchLayout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanelSearchLayout.createSequentialGroup()
                        .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelNama)
                            .addComponent(jLabelJenisKelamin)
                            .addComponent(jLabelUsername)
                            .addComponent(jLabelPassword)
                            .addComponent(jLabelAkses))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanelSearchLayout.createSequentialGroup()
                                .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelSearchLayout.createSequentialGroup()
                                        .addComponent(Lakilaki)
                                        .addGap(8, 8, 8)
                                        .addComponent(Perempuan))
                                    .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ComboBoxAkses, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Nama, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(107, 107, 107)
                                .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelAlamat)
                                    .addComponent(jLabelEmail)
                                    .addComponent(jLabelNoHp))
                                .addGap(33, 33, 33)
                                .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(NoHp, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Email, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(170, 170, 170))
                            .addGroup(jPanelSearchLayout.createSequentialGroup()
                                .addComponent(TambahUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addComponent(Delete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Refresh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSearchLayout.createSequentialGroup()
                                        .addComponent(buttonGlassSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(59, 59, 59)
                                        .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSearchLayout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(ComboBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 909, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(114, Short.MAX_VALUE))
        );
        jPanelSearchLayout.setVerticalGroup(
            jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(TambahUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Edit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelSearchLayout.createSequentialGroup()
                        .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ComboBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonGlassSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSearchLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(NoHp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelSearchLayout.createSequentialGroup()
                        .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelSearchLayout.createSequentialGroup()
                                .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelNama)
                                    .addComponent(Nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                                .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelJenisKelamin)
                                    .addComponent(Lakilaki)
                                    .addComponent(Perempuan))
                                .addGap(8, 8, 8)
                                .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelUsername)
                                    .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabelAlamat))
                        .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelSearchLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabelEmail)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelNoHp)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanelSearchLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelSearchLayout.createSequentialGroup()
                                        .addComponent(jLabelPassword)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                        .addComponent(jLabelAkses)
                                        .addGap(118, 118, 118))
                                    .addGroup(jPanelSearchLayout.createSequentialGroup()
                                        .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(ComboBoxAkses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 97, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TambahUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TambahUserActionPerformed
        getAddUser();// TODO add your handling code here:
    }//GEN-LAST:event_TambahUserActionPerformed

    private void EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditActionPerformed
        getEditUser();// TODO add your handling code here:
    }//GEN-LAST:event_EditActionPerformed

    private void TableUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableUserMouseClicked
        getMouseClick();
    }//GEN-LAST:event_TableUserMouseClicked

    private void DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteActionPerformed
        getDeleteUser();// TODO add your handling code here:
    }//GEN-LAST:event_DeleteActionPerformed

    private void RefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshActionPerformed
        getRefresh();// TODO add your handling code here:
        getTable();
    }//GEN-LAST:event_RefreshActionPerformed

    private void buttonGlassSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonGlassSearchActionPerformed
        getSearchUser();// TODO add your handling code here:
    }//GEN-LAST:event_buttonGlassSearchActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
       
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new User().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea Alamat;
    private javax.swing.JComboBox ComboBoxAkses;
    private javax.swing.JComboBox ComboBoxSearch;
    private usu.widget.ButtonGlass Delete;
    private usu.widget.ButtonGlass Edit;
    private javax.swing.JTextField Email;
    private javax.swing.JRadioButton Lakilaki;
    private javax.swing.JTextField Nama;
    private javax.swing.JTextField NoHp;
    private javax.swing.JPasswordField Password;
    private javax.swing.JRadioButton Perempuan;
    private usu.widget.ButtonGlass Refresh;
    private javax.swing.JTextField Search;
    private javax.swing.JTable TableUser;
    private usu.widget.ButtonGlass TambahUser;
    private javax.swing.JTextField Username;
    private usu.widget.ButtonGlass buttonGlassSearch;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelAkses;
    private javax.swing.JLabel jLabelAlamat;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelJenisKelamin;
    private javax.swing.JLabel jLabelNama;
    private javax.swing.JLabel jLabelNoHp;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JLabel jLabelUsername;
    private javax.swing.JPanel jPanelSearch;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}
