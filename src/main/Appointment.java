/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

/**
 *
 * @author Darker
 */
public class Appointment {
    private ObservableValue<String> appointmentId;
    private ObservableValue<String> patientId;
    private ObservableValue<String> counselorId;
    private ObservableValue<String> appointmentTypeId;
    private ObservableValue<String> appointmentNotes;
    private ObservableValue<String> appointmentStartTime;

    private ObservableValue<String> appointmentByType;
    private ObservableValue<String> appointmentByCount;
    
    public Appointment(ObservableValue<String> appointmentId, ObservableValue<String> patientId, ObservableValue<String> counselorId, ObservableValue<String> appointmentTypeId, ObservableValue<String> appointmentNotes, ObservableValue<String> appointmentStartTime) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.counselorId = counselorId;
        this.appointmentTypeId = appointmentTypeId;
        this.appointmentNotes = appointmentNotes;
        this.appointmentStartTime = appointmentStartTime;
    }

    public Appointment(ObservableValue<String> type, ObservableValue<String> count) {
        this.appointmentByType = type;
        this.appointmentByCount = count;
    }

    public Appointment(ObservableValue<String> patientId, ObservableValue<String> appointmentTypeId, ObservableValue<String> appointmentStartTime) {
        this.patientId = patientId;
        this.appointmentTypeId = appointmentTypeId;
        this.appointmentStartTime = appointmentStartTime;
    }

    
    public Appointment(ObservableValue<String> appointmentId, ObservableValue<String> patientId, ObservableValue<String> appointmentTypeId, ObservableValue<String> appointmentNotes, ObservableValue<String> appointmentStartTime) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.appointmentTypeId = appointmentTypeId;
        this.appointmentNotes = appointmentNotes;
        this.appointmentStartTime = appointmentStartTime;
    }
    
    public ObservableValue<String> getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(ObservableValue<String> appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    public ObservableValue<String> getPatientId() {
        return patientId;
    }
    
    public void setPatientId(ObservableValue<String> patientId) {
        this.patientId = patientId;
    }
    
    public ObservableValue<String> getCounselorId() {
        return counselorId;
    }
    
    public void setCounselorId(ObservableValue<String> counselorId) {
        this.counselorId = counselorId;
    }
    
    public ObservableValue<String> getAppointmentTypeId() {
        return appointmentTypeId;
    }
    
    public void setAppointmentTypeId(ObservableValue<String> appointmentTypeId) {
        this.appointmentTypeId = appointmentTypeId;
    }
    
    public ObservableValue<String> getAppointmentNotes() {
        return appointmentNotes;
    }
    
    public void setAppointmentNotes(ObservableValue<String> appointmentNotes) {
        this.appointmentNotes = appointmentNotes;
    }
    
    public ObservableValue<String> getAppointmentStartTime() {
        return appointmentStartTime;
    }
    
    public void setAppointmentStartTime(ObservableValue<String> appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
    }

    public ObservableValue<String> getAppointmentByType() { return appointmentByType; }

    public void setAppointmentByType(ObservableValue<String> type) {
        this.appointmentByType = type;
    }

    public ObservableValue<String> getAppointmentByCount() { return appointmentByCount; }

    public void setAppointmentByCount(ObservableValue<String> count) {
        this.appointmentByCount = count;
    }
    
}
