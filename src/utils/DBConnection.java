/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.mysql.jdbc.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.User;
import views.PatientRecordsController;

/**
 *
 * @author Darker
 */
public class DBConnection {
    
    // JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String IPAddress = "//3.227.166.251/U05SwO";
    
    // JDBC URL
    private static final String jdbcURL = protocol + vendorName + IPAddress;
    
    // Driver Interface reference 
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    private static Connection conn = null;
    
    private static final String username = "U05SwO"; // Username
    private static String password = "53688593894"; // Password
    
    public static Connection startConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = (Connection) DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection success!");
        }
        catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return conn;
    }
    
    public static Connection getConnection() {
        return conn;
    }
    
    public static ResultSet accessDB(String statement, String className) {
        
        ResultSet rs = null;
        try {

            Statement stmt = getConnection().createStatement();
            rs = stmt.executeQuery(statement);
            while (rs.next()) {
            }
        } catch (SQLException ex) {
            Logger.getLogger(className).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    public static void closeConnection() throws SQLException {
        conn.close();
        System.out.println("Connection closed.");
    }
    
    public static String patientNameToId(String name) {
        String result = null;
        try { 
            ResultSet rs;
            PreparedStatement ps = getConnection().prepareStatement("SELECT pt_id FROM patient WHERE pt_name = '" + name + "'");
            rs = ps.executeQuery();
            rs.next(); //only one record, so no need for a loop.  
            result = rs.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }

    
    public static boolean validCredentials(String username, String password, String pin) {
        
        boolean result = false;
        
        ArrayList<User> counselors = new ArrayList<>();
        
        try { 
            ResultSet rs;
            PreparedStatement ps = getConnection().prepareStatement("SELECT c_name, c_password, c_pin FROM counselor");
            rs = ps.executeQuery();
            int i = 0;
            while (rs.next()) {
                counselors.add(new User(rs.getString("c_name"), rs.getString("c_password"), rs.getString("c_pin")));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (User c : counselors) {
            if (username.equals(c.username) && password.equals(c.password) && pin.equals(c.pin)) {
                return true;
            }
        }
        
        return result;
    }
    
    
    public static String userNameToId(String username) {
        String result = null;
        try { 
            ResultSet rs;
            PreparedStatement ps = getConnection().prepareStatement("SELECT c_id FROM counselor WHERE c_name = '" + username + "'");
            rs = ps.executeQuery();
            rs.next();
            result = rs.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }

    public static ArrayList<String> getAppointmentTypes() {
        ArrayList<String> result = new ArrayList<>();

        try {
            ResultSet rs;
            PreparedStatement ps = getConnection().prepareStatement("SELECT description, APTtype_id FROM APTtype");
            rs = ps.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("APTtype_id") + ": " + rs.getString("description"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public static ArrayList<String> getPatientNames() {
        ArrayList<String> result = new ArrayList<>();

        try {
            ResultSet rs;
            PreparedStatement ps = getConnection().prepareStatement("SELECT pt_id, pt_name FROM patient");
            rs = ps.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("pt_name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public static String getAppointmentTypeToDescription(String appointmentId) {
        String result = null;

        try {
            ResultSet rs;
            PreparedStatement ps = getConnection().prepareStatement("SELECT description FROM APTtype WHERE APTtype_id = '" + appointmentId + "'");
            rs = ps.executeQuery();
            rs.next();
            result = rs.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public static String getCounselorIdToName(String counselorId) {
        String result = null;

        try {
            ResultSet rs;
            PreparedStatement ps = getConnection().prepareStatement("SELECT c_name FROM counselor WHERE c_id = '" + counselorId + "'");
            rs = ps.executeQuery();
            rs.next();
            result = rs.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public static String getPatientIdToName(String patient) {
        String result = null;

        try {
            ResultSet rs;
            PreparedStatement ps = getConnection().prepareStatement("SELECT pt_name FROM patient WHERE pt_id = '" + patient + "'");
            rs = ps.executeQuery();
            rs.next();
            result = rs.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

}
