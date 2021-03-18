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
public class AppointmentType {
    
    private ObservableValue<String> id;
    private ObservableValue<String> description;
    
    public AppointmentType(ObservableValue<String> id, ObservableValue<String> description) {
        this.id = id;
        this.description = description;
    }
    
    public ObservableValue<String> getId() {
        return id;
    }

    public void setId(ObservableValue<String> id) {
        this.id = id;
    }
    
    public ObservableValue<String> getDescription() {
        return description;
    }

    public void setDescription(ObservableValue<String> description) {
        this.description = description;
    }
    
}
