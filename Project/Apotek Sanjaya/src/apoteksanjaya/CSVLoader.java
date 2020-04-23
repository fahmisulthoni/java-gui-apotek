/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apoteksanjaya;

import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 
 */
public class CSVLoader {
    // inisialisasi variable
    private Connection connection;
    
    // konstruktor (fungsi yang namanya sama dengan nama class, dijalankan paling awal)
    public CSVLoader(Connection connection){
        this.connection = connection;
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
            this.connection.setAutoCommit(false);
            // set prepare statement sql
            sql_statement = this.connection.prepareStatement(query);
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
                sql_statement.setString(4, nextLine[3]);
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
            this.connection.commit();
            /* Close connection */
            this.connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(CSVLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CSVLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
