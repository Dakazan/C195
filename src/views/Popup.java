/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import main.User;
import utils.DBConnection;
import utils.TimeHelper;

import static utils.DBConnection.accessDB;
import static utils.DBConnection.getConnection;

/**
 *
 * @author Darker
 */
public class Popup {
    
    public static boolean valid = false;
    
    public static boolean processForm(boolean isValid) {
        return valid = isValid;
    }
        
    
    @FXML public static ArrayList patientPopup() {
        
        ArrayList<String> result = new ArrayList<>();
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add Patient");
        alert.setHeaderText("Add a new patient:");
            

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(30));

        TextField name = new TextField();
        name.setPromptText("Patient Name");
        TextField insurance = new TextField();
        insurance.setPromptText("Insurance");
        TextField addressLine1 = new TextField();
        addressLine1.setPromptText("Address Line 1");
        TextField addressLine2 = new TextField();
        addressLine2.setPromptText("Address Line 2");
        TextField state = new TextField();
        state.setPromptText("State");
        TextField city = new TextField();
        city.setPromptText("City");
        TextField postalCode = new TextField();
        postalCode.setPromptText("Postal Code");
        TextField phone = new TextField();
        phone.setPromptText("Phone");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Insurance:"), 0, 1);
        grid.add(insurance, 1, 1);
        grid.add(new Label("Address Line 1:"), 0, 2);
        grid.add(addressLine1, 1, 2);
        grid.add(new Label("Address Line 2:"), 0, 3);
        grid.add(addressLine2, 1, 3);
        grid.add(new Label("State:"), 0, 4);
        grid.add(state, 1, 4);
        grid.add(new Label("City:"), 0, 5);
        grid.add(city, 1, 5);
        grid.add(new Label("Postal Code:"), 0, 6);
        grid.add(postalCode, 1, 6);
        grid.add(new Label("Phone:"), 0, 7);
        grid.add(phone, 1, 7);

        // Enable/Disable login button depending on whether a username was entered.
        alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);

        // Lambda to validate to make sure all required text fields are filled.
        
        alert.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(
            Bindings.createBooleanBinding( () -> 
                name.getText().trim().isEmpty(), name.textProperty()
            )
        .or(  
            Bindings.createBooleanBinding( () ->
                insurance.getText().trim().isEmpty(), insurance.textProperty()
            )
        )
        .or(  
            Bindings.createBooleanBinding( () ->
                addressLine1.getText().trim().isEmpty(), addressLine1.textProperty()
            )
        )
        .or(  
            Bindings.createBooleanBinding( () ->
                state.getText().trim().isEmpty(), state.textProperty()
            )
        )
        .or(  
            Bindings.createBooleanBinding( () ->
                city.getText().trim().isEmpty(), city.textProperty()
            )
        )
        .or(  
            Bindings.createBooleanBinding( () ->
                postalCode.getText().trim().isEmpty(), postalCode.textProperty()
            )
        )
        .or(  
            Bindings.createBooleanBinding( () ->
                phone.getText().trim().isEmpty(), phone.textProperty()
            )
        )
                

        );
        
        alert.getDialogPane().setContent(grid);
        
        // Request focus on the username field by default.
        Platform.runLater(() -> name.requestFocus());
        
        alert.showAndWait()
                .filter(ok -> ok == ButtonType.OK)
                .ifPresent((ButtonType ok) -> {
                    processForm(true);
                    result.add(name.getText());
                    result.add(insurance.getText());
                    result.add(addressLine1.getText());
                    result.add(addressLine2.getText());
                    result.add(state.getText());
                    result.add(city.getText());
                    result.add(postalCode.getText());
                    result.add(phone.getText());
                    }
                );
        
        return result;
    }

    @FXML public static ArrayList appointmentPopup(ArrayList<String> appointmentTypes) throws RuntimeException{
        
        ArrayList<String> result = new ArrayList<>();
        ObservableList<String> appointmentPicker = FXCollections.observableArrayList();
        ObservableList<String> patientNamePicker = FXCollections.observableArrayList();
        ObservableList<String> hours = FXCollections.observableArrayList();
        ObservableList<String> minutes = FXCollections.observableArrayList();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add Appointment");
        alert.setHeaderText("Schedule a new appointment:");
            
        // set up area
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(30));

        // create form
        ComboBox<String> name = new ComboBox<>();
        patientNamePicker.addAll(DBConnection.getPatientNames());
        name.setItems(patientNamePicker);
        name.setPromptText("Patient Name");
        ComboBox<String> appointmentType = new ComboBox<>();
        appointmentPicker.addAll(appointmentTypes);
        appointmentType.setItems(appointmentPicker);
        TextField notes = new TextField();
        notes.setPromptText("Notes");
        DatePicker startTime = new DatePicker();
        startTime.setPromptText("Start Time");
        ComboBox<String> appointmentHour = new ComboBox<>();
        ComboBox<String> appointmentMinute = new ComboBox<>();

        // populate form
        grid.add(new Label("Name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Appointment Type:"), 0, 1);
        grid.add(appointmentType, 1, 1);
        grid.add(new Label("Notes:"), 0, 2);
        grid.add(notes, 1, 2);
        grid.add(new Label("Start Time:"), 0, 3);
        grid.add(startTime, 1, 3);
        grid.add(new Label("Hour:"), 0, 4);
        grid.add(appointmentHour, 1, 4);
        grid.add(new Label("Minute:"), 0, 5);
        grid.add(appointmentMinute, 1, 5);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");
        // Find 9PM EST and convert to local time so we can add GUI controls to know when to forbid new appointments.
        ZonedDateTime officeTime = ZonedDateTime.ofLocal(LocalDateTime.of(2020, 1, 1, 21, 00), ZoneId.of("America/New_York"), null);
        // Closing time in local hours
        int closingTime = Integer.parseInt(officeTime.withZoneSameInstant(ZoneId.systemDefault()).format(formatter)) - 1;
        int openingTime = closingTime - 11;

        for (int i = openingTime; i <= closingTime; i++) {
            if (i < 10) {
                hours.add("0" + String.valueOf(i));
            } else hours.add(String.valueOf(i));
        }

        minutes.addAll("00", "15", "30", "45");
        // populate time picker
        appointmentHour.setItems(hours);
        appointmentMinute.setItems(minutes);
        // set default value shown to current time
        appointmentHour.setValue(String.valueOf(LocalDateTime.now().getHour()));

        // Check remainder of current time, if it is not in our specified timeslot eg: 00, 15, 30, 45.
        // and round up shown time to reflect next available slot
        if (LocalDateTime.now().getMinute() % 15 == 0) {
            appointmentMinute.setValue(String.valueOf(LocalDateTime.now().getMinute()));
        } else {
            appointmentMinute.setValue(String.valueOf(LocalDateTime.now().getMinute() + (15 - (LocalDateTime.now().getMinute() % 15))));
            if (appointmentMinute.getValue().equals("60")) {
                appointmentMinute.setValue("00");
            }
        }

        // set default value shown to current date
        startTime.setValue(LocalDate.now());


        // If business is currently closed, default to opening time tomorrow (converted to local time).
        if (LocalDateTime.now().getHour() > Integer.parseInt(hours.get(11))) {
            startTime.setValue(LocalDate.now().plusDays(1));
            appointmentHour.setValue(hours.get(0));
            appointmentMinute.setValue("00");
        }
        if (LocalDateTime.now().getHour() < Integer.parseInt(hours.get(0))) {
            startTime.setValue(LocalDate.now());
            appointmentHour.setValue(hours.get(0));
            appointmentMinute.setValue("00");
        }

        // retrieve first appointment type and display it
        appointmentType.setValue(appointmentTypes.get(0));
        name.setValue(patientNamePicker.get(0));

        // Disable button to validate later
        alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);

        // Lambda to validate to make sure all required text fields are filled.
        alert.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(

            Bindings.createBooleanBinding(() -> name.getValue().trim().isEmpty(), name.valueProperty())
            .or(Bindings.createBooleanBinding(() -> appointmentType.getValue().trim().isEmpty(), appointmentType.valueProperty()))
            .or(Bindings.createBooleanBinding(() -> notes.getText().trim().isEmpty(), notes.textProperty()))
            .or(Bindings.createBooleanBinding(() -> startTime.getValue().toString().trim().isEmpty(), startTime.valueProperty()))
            .or(Bindings.createBooleanBinding(() -> appointmentHour.getValue().trim().isEmpty(), startTime.valueProperty()))
            .or(Bindings.createBooleanBinding(() -> appointmentMinute.getValue().trim().isEmpty(), startTime.valueProperty()))

        );
        
        alert.getDialogPane().setContent(grid);
        
        // Request focus on the username field by default.
        Platform.runLater(() -> name.requestFocus());
        alert.showAndWait()
            .filter(ok -> ok == ButtonType.OK)
            .ifPresent((ButtonType ok) -> {
                String addZeroBeforeHour = appointmentHour.getValue();
                if (Integer.parseInt(appointmentHour.getValue()) < 10) {
                    addZeroBeforeHour = "0" + addZeroBeforeHour;
                }
                if (
                        checkForException(startTime.getValue().toString(),
                        Date.from(startTime.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        startTime.getValue() + " " + addZeroBeforeHour + ":" + appointmentMinute.getValue() + ":00.0"
                )) {
                    processForm(true);
                    result.add(name.getValue());
                    result.add(appointmentType.getValue());
                    result.add(notes.getText());
                    result.add(startTime.getValue() + " " + addZeroBeforeHour + ":" + appointmentMinute.getValue() + ":00.0");
                } else {
                    alert.close();
                    valid = false;
                }
            });

        
        return result;
    }

    // Displays popup with exceptioon message
    @FXML public static void exceptionPopup(String exception) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("ERROR!");
        error.setHeaderText("There was an error adding this appointment:");
        error.setContentText(exception);

        error.showAndWait();
    }

    // Make sure appointment date doesn't fall on restricted dates or times
    public static boolean checkForException(String dateStart, Date date, String startTime) {

        startTime = TimeHelper.convertTime(startTime, "utc").toString();
        boolean valid = true;

        String month = dateStart.substring(5, 7);
        String year = dateStart.substring(0, 4);
        dateStart = dateStart.substring(5);

        boolean counselorOverlap = false;
        boolean patientOverlap = false;

        // Check for holiday
        ArrayList<String> holidays = new ArrayList<>();

        holidays.add(TimeHelper.nthWeekdayOfMonth(1, 0, Calendar.getInstance().get(Calendar.YEAR), 3, TimeZone.getTimeZone("America/New_York")));
        holidays.add(TimeHelper.getLastWeekday(date, Calendar.MONDAY));
        holidays.add("11-11");
        holidays.add(TimeHelper.nthWeekdayOfMonth(4, 10, Calendar.getInstance().get(Calendar.YEAR), 4, TimeZone.getTimeZone("America/New_York")));

        if (holidays.contains(dateStart)) {
            exceptionPopup("Appointment can't be scheduled during a holiday!");
            valid = false;
        }

        // Check for counselor overlap
        ResultSet rs = null;

        try {
            Statement stmt = getConnection().createStatement();
            rs = stmt.executeQuery("SELECT pt_id, start_datetime FROM appointment WHERE cr_id = '" + User.currentUser + "' AND start_datetime = '" + startTime + "'");
            if (rs.next()) counselorOverlap = true;
            rs.close();
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(PatientRecordsController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (counselorOverlap) {
            exceptionPopup("You already have an appointment scheduled for this timeslot!");
            valid = false;
        }

        // Check for patient overlap
        try {
            Statement stmt = getConnection().createStatement();
            rs = stmt.executeQuery("SELECT pt_id, start_datetime FROM appointment WHERE start_datetime = '" + startTime + "'");
            if (rs.next()) patientOverlap = true;
            rs.close();
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(PatientRecordsController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (patientOverlap) {
            exceptionPopup("Patient already has an appointment in this timeslot!");
            valid = false;
        }

        return valid;
    }


    // Retrieve list of upcoming appointments and populate popup.
    @FXML public static void upcomingAppointmentsPopup() throws SQLException, InterruptedException {
        ResultSet rs = accessDB("SELECT pt_id, apt_id, notes, start_datetime , COUNT(cr_id) amount FROM appointment WHERE start_datetime BETWEEN NOW() and NOW() + INTERVAL 4 HOUR GROUP BY cr_id", null);
        rs.beforeFirst();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(30));


        int i = 0;
        while (rs.next()) {
            String time = TimeHelper.convertTime(rs.getString(4), "local").format(DateTimeFormatter.ofPattern("HH:mm")).toString();
            grid.add(new Label(time), 0, i);
            grid.add(new Text("Name: " + DBConnection.getPatientIdToName(rs.getString(1))), 1, i);
            grid.add(new Text("Type: " + DBConnection.getAppointmentTypeToDescription(rs.getString(2))), 2, i);
            grid.add(new Text("Notes: " + rs.getString(3)), 3, i);
            i++;
        }
        // Ff grid was populated in while loop, we know there is at least one appointment to display
        if (grid.getChildren().size() >= 3) {
            alert.setTitle("Upcoming Appointments");
            alert.setHeaderText("Here is a list of appointments you have in the next 4 hours:");
            alert.getDialogPane().setContent(grid);
            alert.showAndWait();
        // Otherwise, grid wasn't populated, therefore no appointments
        } else {
            alert.setTitle("Upcoming Appointments");
            alert.setHeaderText("You have no appointments scheduled for the next 4 hours!");
            alert.showAndWait();
        }


    }
    
}
