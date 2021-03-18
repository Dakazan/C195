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

public class Patient {
    
    private ObservableValue<String> id;
    private ObservableValue<String> name;
    private ObservableValue<String> phone;
    private ObservableValue<String> state;
    private ObservableValue<String> insurance;
    private ObservableValue<String> addressLine1;
    private ObservableValue<String> addressLine2;
    private ObservableValue<String> city;
    private ObservableValue<String> postalCode;

    public Patient(ObservableValue<String> id, ObservableValue<String> name, ObservableValue<String> phone, ObservableValue<String> state, ObservableValue<String> insurance) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.state = state;
        this.insurance = insurance;
    }
    
    public Patient(ObservableValue<String> id, ObservableValue<String> name, ObservableValue<String> phone, ObservableValue<String> state, ObservableValue<String> insurance, ObservableValue<String> addressLine1, ObservableValue<String> addressLine2, ObservableValue<String> city, ObservableValue<String> postalCode) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.state = state;
        this.insurance = insurance;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.postalCode = postalCode;
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

    public ObservableValue<String> getPhone() {
        return phone;
    }

    public void setPhone(ObservableValue<String> phone) {
        this.phone = phone;
    }

    public ObservableValue<String> getState() {
        return state;
    }

    public void setState(ObservableValue<String> state) {
        this.state = state;
    }

    public ObservableValue<String> getInsurance() {
        return insurance;
    }

    public void setInsurance(ObservableValue<String> insurance) {
        this.insurance = insurance;
    }
    
    public ObservableValue<String> getAddressLine1() {
        return addressLine1;
    }
    
    public void setAddressLine1(ObservableValue<String> addressLine1) {
        this.addressLine1 = addressLine1;
    }
    
    public ObservableValue<String> getAddressLine2() {
        return addressLine2;
    }
    
    public void setAddressLine2(ObservableValue<String> addressLine2) {
        this.addressLine2 = addressLine2;
    }
    
    public ObservableValue<String> getCity() {
        return city;
    }
    
    public void setCity(ObservableValue<String> city) {
        this.city = city;
    }
    
    public ObservableValue<String> getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(ObservableValue<String> postalCode) {
        this.postalCode = postalCode;
    }
}