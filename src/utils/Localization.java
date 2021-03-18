/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Locale;

/**
 *
 * @author Darker
 */
public class Localization {
    
    
    // Convert locale statement to readable language code
    String locale = Locale.getDefault().toString().substring(0,2);
    
    public String getLoginText() {
        String result;
        switch (locale) {
            case "es" : 
                result = "Iniciar sesión"; 
                break;
            case "de" : 
                result = "Login";
                break;
            default:
                result = "Log In";                
        }
        return result;
    }
    
    public String getTitleText() {
        String result;
        switch (locale) {
            case "es" : 
                result = "Aplicación de programación"; 
                break;
            case "de" : 
                result = "Progframmieranwendung";
                break;
            default:
                result = "Scheduling App";                
        }
        return result;
    }
    
    public String getUsernameText() {
        String result;
        switch (locale) {
            case "es" : 
                result = "Usuario"; 
                break;
            case "de" : 
                result = "Benutzer";
                break;
            default:
                result = "Username";                
        }
        return result;
    }
    
    public String getPasswordText() {
        String result;
        switch (locale) {
            case "es" : 
                result = "Contraseña"; 
                break;
            case "de" : 
                result = "Passwort";
                break;
            default:
                result = "Password";                
        }
        return result;
    }
    
    public String getPinText() {
        String result;
        switch (locale) {
            case "es" : 
                result = "Alfiler"; 
                break;
            case "de" : 
                result = "PIN";
                break;
            default:
                result = "PIN";                
        }
        return result;
    }
    
    public String getWarningMessageText() {
        String result;
        switch (locale) {
            case "es" : 
                result = "Las credenciales proporcionadas no son válidas."; 
                break;
            case "de" : 
                result = "Die angegebenen Anmeldeinformationen sind ungültig.";
                break;
            default:
                result = "The credentials provided are invalid.";                
        }
        return result;
    }
    
    public String getWarningTitleText() {
        String result;
        switch (locale) {
            case "es" : 
                result = "Credenciales no válidas"; 
                break;
            case "de" : 
                result = "Ungültige Anmeldeinformationen";
                break;
            default:
                result = "Invalid Credentials";                
        }
        return result;
    }
    
    public String getOkText() {
        String result;
        switch (locale) {
            case "es" : 
                result = "Aprobar"; 
                break;
            case "de" : 
                result = "Genehmigen";
                break;
            default:
                result = "OK";                
        }
        return result;
    }
}
