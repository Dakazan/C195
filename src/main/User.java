/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Darker
 */
public class User {
    
    public String username;
    public String password;
    public String pin;
    
    public static String currentUser;
    public static String currentPassword;
    public static String currentPin;
    
    public User(String username, String password, String pin) {
        this.username = username;
        this.password = password;
        this.pin = pin;
        currentUser = username;
        currentPassword = password;
        currentPin = pin;
    }
    
    


}
