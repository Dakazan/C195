/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import main.Appointment;
import main.User;
import utils.DBConnection;
import utils.TimeHelper;

import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.DBConnection.accessDB;
import static utils.DBConnection.getConnection;

/**
 * FXML Controller class
 *
 * @author Darker
 */
public class AppointmentRecordsController implements Initializable {
    
    Appointment selected;
    ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    
    @FXML private TableView<Appointment> table;
    @FXML private TableColumn<Appointment, String> colName;
    @FXML private TableColumn<Appointment, String> colType;
    @FXML private TableColumn<Appointment, String> colNotes;
    @FXML private TableColumn<Appointment, String> colStartTime;
    
    @FXML private TextField patientName;
    @FXML private ComboBox<String> appointmentType;
    @FXML private TextArea appointmentNotes;

    @FXML private DatePicker appointmentStartTime;
    @FXML private ComboBox<String> appointmentHour;
    @FXML private ComboBox<String> appointmentMinute;

    ObservableList<String> appointmentTypePicker = FXCollections.observableArrayList();
    ObservableList<String> hours = FXCollections.observableArrayList();
    ObservableList<String> minutes = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {


        hours.addAll( "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21");
        minutes.addAll("00", "15", "30", "45");
        appointmentTypePicker.addAll(DBConnection.getAppointmentTypes());
        appointmentHour.setItems(hours);
        appointmentMinute.setItems(minutes);
        appointmentType.setItems(appointmentTypePicker);
        getAppointmentList();

    }
    
    public void getAppointmentList() {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        ResultSet rs = accessDB("SELECT * FROM appointment, patient, counselor, APTtype WHERE patient.pt_id = appointment.pt_id AND counselor.c_id = appointment.cr_id AND appointment.apt_type_id = APTtype.APTtype_id", this.getClass().getName());
        colName.setCellValueFactory(cellData -> cellData.getValue().getPatientId());
        colType.setCellValueFactory(cellData -> cellData.getValue().getAppointmentTypeId());
        colNotes.setCellValueFactory(cellData -> cellData.getValue().getAppointmentNotes());
        colStartTime.setCellValueFactory(cellData -> cellData.getValue().getAppointmentStartTime());
        
        try {
            rs.beforeFirst(); //this is needed because the result set was looped through in accessDB.  We need to reset the cursor! 
            while (rs.next()) {
                String id = rs.getString("apt_id"); //parameter is the column name in the database
                String name = rs.getString("pt_name");
                String type = rs.getString("description");
                String notes = rs.getString("notes");

                LocalDateTime timeToLocal = TimeHelper.convertTime(rs.getString("start_datetime"), "local");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d HH:mm");
                String startTime = timeToLocal.format(formatter);

                Appointment tr = new Appointment(
                    new ReadOnlyStringWrapper(id),
                    new ReadOnlyStringWrapper(name),
                    new ReadOnlyStringWrapper(type),
                    new ReadOnlyStringWrapper(notes),
                    new ReadOnlyStringWrapper(startTime)
                );
                appointmentList.add(tr);
            }
            table.setItems(appointmentList);
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML private void updateAppointmentAction(ActionEvent event) {
        selected = table.getSelectionModel().getSelectedItem();
        
        if (selected != null) {
          Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Update Appointment?");
            alert.setHeaderText("Are you sure you want to update this appointment?");
            alert.showAndWait()
            .filter(ok -> ok == ButtonType.OK)
            .ifPresent((ButtonType ok) -> {
                    updateAppointment();
                    getAppointmentList();
                }
            );
        } else { //throw error if no patient selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Please select the appointment you want to update!");
            alert.showAndWait()
            .filter(ok -> ok == ButtonType.OK)
            .ifPresent((ButtonType ok) -> {
                
                }
            );
        }
    }
    
    @FXML private void deleteAppointmentAction(ActionEvent event) {
        selected = table.getSelectionModel().getSelectedItem();
        
        //check to make sure patient is selected before we begin
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Patient");
            alert.setHeaderText("Are you sure you want to delete this appointment?");
            alert.showAndWait()
            .filter(ok -> ok == ButtonType.OK)
            .ifPresent((ButtonType ok) -> {
                deleteAppointment();
                getAppointmentList();
                }
            );
        } else { //throw error if no patient selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Please select an appointment you want to delete!");
            alert.show();
        }
    }
    
    @FXML private void addAppointmentAction(ActionEvent event) {
        ArrayList<String> popup = Popup.appointmentPopup(DBConnection.getAppointmentTypes());
        if (Popup.valid) {
            addAppointment(popup);
            getAppointmentList();
        }
    }
    
    @FXML void handleRowAction(MouseEvent event) throws ParseException {
        selected = table.getSelectionModel().getSelectedItem();
        String currentSelection = selected.getAppointmentId().getValue();
        
        ResultSet rs = null;
        String date;
        String time;
        String hour;
        String minute;
        LocalDate ld;


            try {

                Statement stmt = getConnection().createStatement();
                rs = stmt.executeQuery("SELECT * FROM appointment, patient, APTtype WHERE appointment.apt_id = '" + currentSelection + "' AND appointment.pt_id = patient.pt_id AND appointment.apt_type_id = APTtype.APTtype_id");
                while (rs.next()) {

                    time = rs.getString("start_datetime").substring(0, rs.getString("start_datetime").lastIndexOf(":"));
                    hour = time.substring(time.lastIndexOf(" ") + 1, time.lastIndexOf(":"));
                    minute = time.substring(time.lastIndexOf(":") + 1);
                    date = rs.getString("start_datetime");

                    LocalDateTime timeToLocal = TimeHelper.convertTime(rs.getString("start_datetime"), "local");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d HH:mm");
                    String startTime = timeToLocal.format(formatter);

                    patientName.setText(rs.getString("pt_name"));
                    appointmentType.setValue(rs.getString("apt_type_id") + ": " + rs.getString("description"));
                    appointmentNotes.setText(rs.getString("notes"));

                    appointmentStartTime.setValue(timeToLocal.toLocalDate());
                    formatter = DateTimeFormatter.ofPattern("HH");
                    startTime = timeToLocal.format(formatter);
                    appointmentHour.setValue(startTime);
                    formatter = DateTimeFormatter.ofPattern("mm");
                    startTime = timeToLocal.format(formatter);
                    appointmentMinute.setValue(startTime);
                }
            } catch (SQLException | NullPointerException ex) {
                ex.printStackTrace();
            }

    }
    
    void deleteAppointment() {
        String id = selected.getAppointmentId().getValue();
        
        String sql = "DELETE FROM appointment WHERE apt_id = ?";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    void updateAppointment() {

        String id = selected.getAppointmentId().getValue();

        String sql = "UPDATE appointment SET apt_type_id = ?, notes = ?, start_datetime = ?, updated_at = now(), updated_by = ? WHERE apt_id = ?";

        try {
            LocalDate date = appointmentStartTime.getValue();
            String hour = appointmentHour.getValue();
            String minute = appointmentMinute.getValue();

            if (date == null || hour == null || minute == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Date and time not completely filled in!");
                alert.setContentText("Please use the calendar date picker to select a date AND use the Hour and Minute dropdown to set hours and minutes before clicking this button");
                alert.setGraphic(null);
                alert.showAndWait();
            }

            LocalDateTime ldt = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), Integer.parseInt(hour), Integer.parseInt(minute));
            ZonedDateTime locZdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
            ZonedDateTime utcZdt = locZdt.withZoneSameInstant(ZoneOffset.UTC);
            DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setString(1, appointmentType.getValue().substring(0, appointmentType.getValue().lastIndexOf(":")));
            ps.setString(2, appointmentNotes.getText());
            ps.setString(3, customFormatter.format(utcZdt));
            ps.setString(4, User.currentUser);
            ps.setString(5, id);

            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(PatientRecordsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    void addAppointment(ArrayList<String> appointment) {
        
        String sql = "INSERT INTO appointment (apt_id, pt_id, cr_id, apt_type_id, notes, start_datetime, patient_pt_id, counselor_c_id, APTtype_APTtype_id, created_at, created_by, updated_at, updated_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, now(), ?)";
 
        String appointmentId;
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT MAX(apt_id) FROM appointment"); //retrieve last rows ID so we can increment later
            ResultSet rs = ps.executeQuery();
            rs.next(); //only one record, so no need for a loop.  
            appointmentId = Integer.toString(Integer.parseInt(rs.getString(1)) + 1);

            ps = getConnection().prepareStatement(sql);
            ps.setString(1, appointmentId);      // apt_id
            ps.setString(2, DBConnection.patientNameToId(appointment.get(0))); // pt_id
            ps.setString(3, DBConnection.userNameToId(User.currentUser)); // cr_id
            ps.setString(4, appointment.get(1).substring(0, appointment.get(1).lastIndexOf(":"))); // apt_type_id
            ps.setString(5, appointment.get(2)); // notes
            ps.setString(6, TimeHelper.convertTime(appointment.get(3), "utc").toString()); // start_datetime
            ps.setString(7, DBConnection.patientNameToId(appointment.get(0))); // patient_pt_id
            ps.setString(8, DBConnection.userNameToId(User.currentUser)); // counselor_c_id
            ps.setString(9, appointment.get(1).substring(0, appointment.get(1).lastIndexOf(":"))); // APTtype_APTtype_id
            ps.setString(10, User.currentUser);
            ps.setString(11, User.currentUser);
            

            ps.execute();
            
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

}
