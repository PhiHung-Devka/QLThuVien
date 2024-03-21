/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author DIEUMY
 */
public class JDBC {
    private static Connection conn = null;
    
    public static Connection getConnection(String user, String pass, String database) {
        try {
            String url = "jdbc:sqlserver://localhost:1433;database=" +database +";encrypt=false;";
            
            // kết nối
            conn = DriverManager.getConnection(url, user, pass);
//            System.out.println("Ket noi thanh cong");
        } catch (SQLException ex) {
            System.out.println("Loi ket noi " + ex.getMessage());
        }
        return conn;
    }
}
