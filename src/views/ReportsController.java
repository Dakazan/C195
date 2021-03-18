package views;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.*;
import javafx.scene.control.*;
import main.Appointment;
import utils.DBConnection;
import utils.TimeHelper;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.DBConnection.accessDB;

public class ReportsController implements Initializable {

    @FXML private TableView<Appointment> appointmentByType;
    @FXML private TableView<Appointment> appointmentByCounselor;
    @FXML private TableView<Appointment> appointmentByState;
    @FXML private TableColumn<Appointment, String> byType;
    @FXML private TableColumn<Appointment, String> byTypeCount;
    @FXML private TableColumn<Appointment, String> byCounselor;
    @FXML private TableColumn<Appointment, String> byCounselorCount;
    @FXML private TableColumn<Appointment, String> byState;
    @FXML private TableColumn<Appointment, String> byStateCount;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        byType.setStyle( "-fx-alignment: CENTER;");
        byTypeCount.setStyle( "-fx-alignment: CENTER;");
        byCounselor.setStyle( "-fx-alignment: CENTER;");
        byCounselorCount.setStyle( "-fx-alignment: CENTER;");
        byState.setStyle( "-fx-alignment: CENTER;");
        byStateCount.setStyle( "-fx-alignment: CENTER;");
    }

    public void sortByType(Event event) {
        ObservableList<Appointment> list = FXCollections.observableArrayList();

        ResultSet rs = accessDB("SELECT apt_type_id , COUNT(apt_type_id) types FROM appointment GROUP BY apt_type_id", this.getClass().getName());

        byType.setCellValueFactory(cellData -> cellData.getValue().getAppointmentByType());
        byTypeCount.setCellValueFactory(cellData -> cellData.getValue().getAppointmentByCount());

        try {
            rs.beforeFirst(); //this is needed because the result set was looped through in accessDB.  We need to reset the cursor!
            while (rs.next()) {
                String type = rs.getString("apt_type_id"); //parameter is the column name in the database
                String count = rs.getString("types");

                Appointment tr = new Appointment(
                    new ReadOnlyStringWrapper(DBConnection.getAppointmentTypeToDescription(type)),
                    new ReadOnlyStringWrapper(count)
                );
                list.add(tr);
            }
            appointmentByType.setItems(list);
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sortByCounselor(Event event) {
        ObservableList<Appointment> list = FXCollections.observableArrayList();

        ResultSet rs = accessDB("SELECT cr_id , COUNT(cr_id) types FROM appointment GROUP BY cr_id", this.getClass().getName());

        byCounselor.setCellValueFactory(cellData -> cellData.getValue().getAppointmentByType());
        byCounselorCount.setCellValueFactory(cellData -> cellData.getValue().getAppointmentByCount());

        try {
            rs.beforeFirst(); //this is needed because the result set was looped through in accessDB.  We need to reset the cursor!
            while (rs.next()) {
                String type = rs.getString("cr_id"); //parameter is the column name in the database
                String count = rs.getString("types");

                Appointment tr = new Appointment(
                    new ReadOnlyStringWrapper(DBConnection.getCounselorIdToName(type)),
                    new ReadOnlyStringWrapper(count)
                );
                list.add(tr);
            }
            appointmentByCounselor.setItems(list);
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sortByState(Event event) {
        ObservableList<Appointment> list = FXCollections.observableArrayList();

        ResultSet rs = accessDB("SELECT state, COUNT(state) types FROM appointment, patient, address WHERE patient.address_id = address.address_id AND appointment.pt_id = patient.pt_id GROUP BY state", this.getClass().getName());

        byState.setCellValueFactory(cellData -> cellData.getValue().getAppointmentByType());
        byStateCount.setCellValueFactory(cellData -> cellData.getValue().getAppointmentByCount());

        try {
            rs.beforeFirst(); //this is needed because the result set was looped through in accessDB.  We need to reset the cursor!
            while (rs.next()) {
                String type = rs.getString("state"); //parameter is the column name in the database
                String count = rs.getString("types");

                Appointment tr = new Appointment(
                    new ReadOnlyStringWrapper(type),
                    new ReadOnlyStringWrapper(count)
                );
                list.add(tr);
            }
            appointmentByState.setItems(list);
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}
