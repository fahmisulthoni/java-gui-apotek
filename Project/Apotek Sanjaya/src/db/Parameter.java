/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;

/**
 * @author Fahmi Sulthoni
 */
public class Parameter {
    public static String IPHOST = "127.0.0.1";
    public static String HOST_DB = "jdbc:mysql://" + IPHOST + ":3306/apotek";
    public static String USERNAME_DB = "root";
    public static String PASSWORD_DB = "";
    public static int PORT = 8080;
    public static String USER;

    public static Connection getConnection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
