/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.beans.value.ObservableValue;

/**
 *
 * @author Darker
 */
public class Counselor {
    
    private ObservableValue<String> id;
    private ObservableValue<String> name;
    private ObservableValue<String> password;
    private ObservableValue<String> pin;
    
    
    public Counselor(ObservableValue<String> id, ObservableValue<String> name, ObservableValue<String> password, ObservableValue<String> pin) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.pin = pin;
    }
    
    public ObservableValue<String> getId() {
        return id;
    }

    public void setId(ObservableValue<String> id) {
        this.id = id;
    }
    
    public ObservableValue<String> getName() {
        return name;
    }

    public void setName(ObservableValue<String> name) {
        this.name = name;
    }
    
    public ObservableValue<String> getPassword() {
        return password;
    }

    public void setPassword(ObservableValue<String> password) {
        this.password = password;
    }
    
    public ObservableValue<String> getPIN() {
        return pin;
    }

    public void setPIN(ObservableValue<String> pin) {
        this.pin = pin;
    }
    
}
