/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apoteksanjaya;

import aksesoris.Loading;
import java.awt.Dialog;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * @author Fahmi Sulthoni
 */
public class Main {

    //konfigurasi JDBC
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/";

    // user dan password database
    static final String USER = "root";
    static final String PASS = "";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException {
        //inisialisasi variable dan koneksi dan statement
        Connection connection = null;
        Statement statement = null;
        String sql = "";

        try {
            // mendaftar drive ke JDBC
            Class.forName(JDBC_DRIVER);
            // melakukan koneksi ke database
            System.out.println("Menghubungkan ke MYSQL");
            connection = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
            statement = (Statement) connection.createStatement();
            System.out.println("Berhasil Menghubungkan ke MYSQL...");

            boolean ketemu = false;
            ResultSet result = connection.getMetaData().getCatalogs();
            while (result.next()) {
                String databaseName = result.getString(1);
                if (databaseName.equals("apotek")) {
                    ketemu = true;
                    System.err.print("Database " + databaseName + " Sudah ada\n");
                }
            }

            if (ketemu == false) {
//             eksekusi query (Create DB)
                System.out.println("Membuat database apotek");
                sql = "CREATE DATABASE apotek";
                statement.executeUpdate(sql);
                System.out.println("Berhasil membuat DB apotek");

                System.out.println("Mengakses database apotek");
                sql = "USE apotek";
                statement.execute(sql);
                System.out.println("Berhasil tersambung dengan apotek");

//        membuat tabel medicine
                System.out.println("Membuat medicine..");
                sql = "CREATE TABLE medicine (kode_obat VARCHAR (20) NOT NULL, "
                        + "nama_obat VARCHAR (100), kode_supplier VARCHAR (20), "
                        + "kategori_obat VARCHAR (15), jenis_obat VARCHAR (20), "
                        + "merek_obat VARCHAR (100), harga_beli_obat DECIMAL (10,0), "
                        + "harga_jual_obat DECIMAL (10,0), jumlah_obat INT (3), "
                        + "tanggal_masuk TIMESTAMP, "
                        + "expired DATE, PRIMARY KEY (kode_obat))";
                statement.executeUpdate(sql);
                System.out.println("Berhasil membuat apotek..");

//        membuat tabel penjualan
                System.out.println("Membuat penjualan..");
                sql = "CREATE TABLE penjualan (id_jual INT(5) NOT NULL, "
                        + "kode_transaksi VARCHAR(50), "
                        + "kode_obat VARCHAR (10), "
                        + "nama_obat VARCHAR (100), merek_obat VARCHAR (100), "
                        + "harga_jual INT (11), jumlah_beli INT (11), "
                        + "total_harga INT (11), tanggal_transaksi TIMESTAMP)";
                statement.executeUpdate(sql);
                System.out.println("Berhasil membuat penjualan..");

//        membuat tabel supplier
                System.out.println("Membuat supplier..");
                sql = "CREATE TABLE supplier (kode_supplier VARCHAR(5) NOT NULL, "
                        + "nama_supplier VARCHAR (100), no_telepon VARCHAR (15), "
                        + "alamat TEXT, PRIMARY KEY (kode_supplier))";
                statement.executeUpdate(sql);
                System.out.println("Berhasil membuat supplier..");

//        membuat tabel user
                System.out.println("Membuat user..");
                sql = "CREATE TABLE user (id_user INT(11) NOT NULL, "
                        + "nama VARCHAR (100), jenis_kelamin VARCHAR (15), "
                        + "username VARCHAR (20), password VARCHAR (20), "
                        + "akses VARCHAR (15), alamat TEXT, email VARCHAR (50), "
                        + "no_hp VARCHAR(12), PRIMARY KEY (id_user))";
                statement.executeUpdate(sql);
                System.out.println("Berhasil membuat user..");

                // memasukan data kedalam tabel
                System.out.println("Memasukan data ke medicine");
                sql = "INSERT INTO medicine VALUES('FS01','Amoxilin Generik 100g','KS01',"
                        + "'Obat Dalam','Tablet','Amoxcilin 100g',8000,10000,57,'2019-04-29 21:48:09','2020-01-26')";
                statement.executeUpdate(sql);
                sql = "INSERT INTO medicine VALUES('FS02','Farsien 50g','KS04',"
                        + "'Obat Luar','Kapsul','Farsien 50g',6000,8000,76,'2019-04-29 21:52:11','2020-02-29')";
                statement.executeUpdate(sql);
                sql = "INSERT INTO medicine VALUES('FS03','Broadamx 500g','KS03',"
                        + "'Obat Dalam','Kaplet','Amoksisilina Trihidrat 500mg',12500,15000,90,'2019-04-29 22:02:21','2020-03-12')";
                statement.executeUpdate(sql);
                sql = "INSERT INTO medicine VALUES('FS04','Flutamol 50g','KS02',"
                        + "'Obat Dalam','Kaplet','Flutamol',4000,5000,66,'2019-04-29 22:19:23','2020-04-17')";
                statement.executeUpdate(sql);
                sql = "INSERT INTO medicine VALUES('FS05','Paracetamol','KS05',"
                        + "'Obat Dalam','Kaplet','Paracetamol',7000,10000,34,'2019-04-29 22:27:15','2020-05-22')";
                statement.executeUpdate(sql);
                System.out.println("Data berhasil dimasukan..");

                // memasukan data kedalam tabel
                System.out.println("Memasukan data ke supplier");
                sql = "INSERT INTO supplier VALUES('KS01','Fahmi Sulthoni',08980880303,'Jl. Pasir Subur No.10')";
                statement.executeUpdate(sql);
                sql = "INSERT INTO supplier VALUES('KS02','Indriawan',082295975687,'Jl. Leuwi Panjang Gg.Bakti II No.32')";
                statement.executeUpdate(sql);
                sql = "INSERT INTO supplier VALUES('KS03','Reza Ranmark',087729900772,'Jl. Khoer Affandi No.70')";
                statement.executeUpdate(sql);
                sql = "INSERT INTO supplier VALUES('KS04','Nachiro',088210094535,'Jl. Cibaduyut Gg.H.Umar No.23')";
                statement.executeUpdate(sql);
                sql = "INSERT INTO supplier VALUES('KS05','Irfan Fathoni',082323573286,'Jl. Kopo Gg.Maksudi V No.16')";
                statement.executeUpdate(sql);
                System.out.println("Data berhasil dimasukan..");

                // memasukan data kedalam tabel
                System.out.println("Memasukan data ke user");
                sql = "INSERT INTO user VALUES('1','admin','Laki-Laki','admin','admin','Admin','','','')";
                statement.executeUpdate(sql);
                System.out.println("Data berhasil dimasukan..");

                // membuat foreign key
                System.err.println("Menambahkan index pada kode_supplier");
                sql = "ALTER TABLE supplier ADD INDEX(kode_supplier)";
                statement.executeUpdate(sql);
                System.err.println("Berhasil Menambahkan index pada kode_supplier");

                // membuat foreign key
                System.err.println("Menambahkan index pada kode_obat");
                sql = "ALTER TABLE penjualan ADD INDEX(kode_obat)";
                statement.executeUpdate(sql);
                System.err.println("Berhasil Menambahkan index pada kode_obat");

                // menghubungkan medicine dengan supplier pada kode_obat
                System.out.println("Menghubungkan supplier dengan medicine pada kode_supplier");
                sql = "ALTER TABLE `medicine` ADD FOREIGN KEY (`kode_supplier`) REFERENCES `supplier`(`kode_supplier`) ON DELETE CASCADE ON UPDATE CASCADE";
                statement.execute(sql);
                System.out.println("Berhasil Menghubungkan medicine dengan supplier pada kode_obat");

                // menghubungkan medicine dengan penjualan pada kode_obat
                System.out.println("Menghubungkan medicine dengan penjualan pada kode_obat");
                sql = "ALTER TABLE `penjualan` ADD FOREIGN KEY (`kode_obat`) REFERENCES `medicine`(`kode_obat`) ON DELETE CASCADE ON UPDATE CASCADE";
                statement.execute(sql);
                System.out.println("Berhasil Menghubungkan medicine dengan penjualan pada kode_obat");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Loading ld = new Loading();
        ld.setVisible(true);
        for (int i = 0; i <= 100; i++) {
            try {
                ld.getProgressBar().setValue(i);
                Thread.sleep(40);
            } catch (InterruptedException ex) {
                Logger.getLogger(Loading.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ld.dispose();
        Login l = new Login();
        l.setVisible(true);
        // TODO code application logic here
    }
}
