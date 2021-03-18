/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.Appointment;
import main.User;
import utils.DBConnection;
import utils.TimeHelper;

import static utils.DBConnection.accessDB;

/**
 * FXML Controller class
 *
 * @author Darker
 */
public class CalendarController implements Initializable {

    @FXML private TableView<Appointment> monthTable;
    @FXML private TableColumn<Appointment, String> monthColPatient;
    @FXML private TableColumn<Appointment, String> monthColAppointmentType;
    @FXML private TableColumn<Appointment, String> monthColDate;

    @FXML private TableView<Appointment> weekTable;
    @FXML private TableColumn<Appointment, String> weekColPatient;
    @FXML private TableColumn<Appointment, String> weekColAppointmentType;
    @FXML private TableColumn<Appointment, String> weekColDate;

    @FXML private TableView<Appointment> biWeekTable;
    @FXML private TableColumn<Appointment, String> biWeekColPatient;
    @FXML private TableColumn<Appointment, String> biWeekColAppointmentType;
    @FXML private TableColumn<Appointment, String> biWeekColDate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void sortByMonth(Event event) {

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        ResultSet rs = accessDB("SELECT * FROM appointment, patient, counselor, APTtype WHERE patient.pt_id = appointment.pt_id AND counselor.c_id = '" + User.currentUser + "' AND appointment.apt_type_id = APTtype.APTtype_id AND start_datetime BETWEEN NOW() and NOW() + INTERVAL 1 MONTH", this.getClass().getName());
        monthColPatient.setCellValueFactory(cellData -> cellData.getValue().getPatientId());
        monthColAppointmentType.setCellValueFactory(cellData -> cellData.getValue().getAppointmentTypeId());
        monthColDate.setCellValueFactory(cellData -> cellData.getValue().getAppointmentStartTime());

        try {
            rs.beforeFirst(); //this is needed because the result set was looped through in accessDB.  We need to reset the cursor!
            while (rs.next()) {
                String name = rs.getString("pt_name");
                String type = DBConnection.getAppointmentTypeToDescription(rs.getString("apt_type_id"));

                LocalDateTime timeToLocal = TimeHelper.convertTime(rs.getString("start_datetime"), "local");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d HH:mm");
                String startTime = timeToLocal.format(formatter);

                Appointment tr = new Appointment(
                    new ReadOnlyStringWrapper(name),
                    new ReadOnlyStringWrapper(type),
                    new ReadOnlyStringWrapper(startTime)
                );
                appointmentList.add(tr);
            }
            monthTable.setItems(appointmentList);
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void sortByWeek(Event event) {

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        ResultSet rs = accessDB("SELECT * FROM appointment, patient, counselor, APTtype WHERE patient.pt_id = appointment.pt_id AND counselor.c_id = '" + User.currentUser + "' AND appointment.apt_type_id = APTtype.APTtype_id AND start_datetime BETWEEN NOW() and NOW() + INTERVAL 1 WEEK", this.getClass().getName());
        weekColPatient.setCellValueFactory(cellData -> cellData.getValue().getPatientId());
        weekColAppointmentType.setCellValueFactory(cellData -> cellData.getValue().getAppointmentTypeId());
        weekColDate.setCellValueFactory(cellData -> cellData.getValue().getAppointmentStartTime());

        try {
            rs.beforeFirst(); //this is needed because the result set was looped through in accessDB.  We need to reset the cursor!
            while (rs.next()) {
                String name = rs.getString("pt_name");
                String type = DBConnection.getAppointmentTypeToDescription(rs.getString("apt_type_id"));

                LocalDateTime timeToLocal = TimeHelper.convertTime(rs.getString("start_datetime"), "local");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d HH:mm");
                String startTime = timeToLocal.format(formatter);

                Appointment tr = new Appointment(
                    new ReadOnlyStringWrapper(name),
                    new ReadOnlyStringWrapper(type),
                    new ReadOnlyStringWrapper(startTime)
                );
                appointmentList.add(tr);
            }
            weekTable.setItems(appointmentList);
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void sortByBiWeek(Event event) {

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        ResultSet rs = accessDB("SELECT * FROM appointment, patient, counselor, APTtype WHERE patient.pt_id = appointment.pt_id AND counselor.c_id = '" + User.currentUser + "' AND appointment.apt_type_id = APTtype.APTtype_id AND start_datetime BETWEEN NOW() and NOW() + INTERVAL 2 WEEK", this.getClass().getName());
        biWeekColPatient.setCellValueFactory(cellData -> cellData.getValue().getPatientId());
        biWeekColAppointmentType.setCellValueFactory(cellData -> cellData.getValue().getAppointmentTypeId());
        biWeekColDate.setCellValueFactory(cellData -> cellData.getValue().getAppointmentStartTime());

        try {
            rs.beforeFirst(); //this is needed because the result set was looped through in accessDB.  We need to reset the cursor!
            while (rs.next()) {
                String name = rs.getString("pt_name");
                String type = DBConnection.getAppointmentTypeToDescription(rs.getString("apt_type_id"));

                LocalDateTime timeToLocal = TimeHelper.convertTime(rs.getString("start_datetime"), "local");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d HH:mm");
                String startTime = timeToLocal.format(formatter);

                Appointment tr = new Appointment(
                    new ReadOnlyStringWrapper(name),
                    new ReadOnlyStringWrapper(type),
                    new ReadOnlyStringWrapper(startTime)
                );
                appointmentList.add(tr);
            }
            biWeekTable.setItems(appointmentList);
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

    }
}
