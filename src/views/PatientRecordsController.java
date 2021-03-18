/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;


import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.Patient;
import static utils.DBConnection.getConnection;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import main.User;
import static utils.DBConnection.accessDB;

/**
 * FXML Controller class
 *
 * @author Darker
 */
public class PatientRecordsController implements Initializable {    
    
    @FXML private TableView<Patient> table;
    @FXML private TableColumn<Patient, String> colId;
    @FXML private TableColumn<Patient, String> colName;
    @FXML private TableColumn<Patient, String> colPhone;
    @FXML private TableColumn<Patient, String> colState;
    @FXML private TableColumn<Patient, String> colInsurance;
    @FXML private Button btnUpdate;
    @FXML private TextField patientId;
    @FXML private TextField patientName;
    @FXML private TextField patientInsurance;
    @FXML private TextField patientAddressLine1;
    @FXML private TextField patientAddressLine2;
    @FXML private TextField patientCity;
    @FXML private TextField patientState;
    @FXML private TextField patientPostalCode;
    @FXML private TextField patientPhone;
    @FXML private Text loggedUser;

    Patient selected;
    ObservableList<Patient> patientList = FXCollections.observableArrayList();
    
    
    @FXML private void updatePatientAction(ActionEvent event) {
        selected = table.getSelectionModel().getSelectedItem();
        
        if (selected != null) {
          Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Update Patient?");
            alert.setHeaderText("Are you sure you want to update" + "" + "?");
            alert.showAndWait()
            .filter(ok -> ok == ButtonType.OK)
            .ifPresent((ButtonType ok) -> {
                    updatePatient();
                    getPatientList();
                }
            );
        } else { //throw error if no patient selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Please select a patient you want to update!");
            alert.showAndWait()
            .filter(ok -> ok == ButtonType.OK)
            .ifPresent((ButtonType ok) -> {
                
                }
            );
        }
    }
    
    
    @FXML private void deletePatientAction(ActionEvent event) {
        
        selected = table.getSelectionModel().getSelectedItem();
        
        //check to make sure patient is selected before we begin
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Patient");
            alert.setHeaderText("Are you sure you want to delete " + selected.getName().getValue() + "?");
            alert.showAndWait()
            .filter(ok -> ok == ButtonType.OK)
            .ifPresent((ButtonType ok) -> {
                deletePatient();
                getPatientList();
                }
            );
        } else { //throw error if no patient selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Please select a patient you want to delete!");
            alert.show();
        }
    }
    
    @FXML private void addPatientAction(ActionEvent event) {
        
        ArrayList<String> popup = Popup.patientPopup();
        if (Popup.valid) {
            addPatient(popup);
            getPatientList();
        }

       
    }

    @FXML void handleRowAction(MouseEvent event) {
        selected = table.getSelectionModel().getSelectedItem();
        String currentSelection = selected.getId().getValue();
        
        ResultSet rs = null;

            try {

                Statement stmt = getConnection().createStatement();
                rs = stmt.executeQuery("SELECT * FROM patient, address WHERE patient.pt_id = '" + currentSelection + "' AND patient.address_id = address.address_id");
                while (rs.next()) {
                    patientId.setText(rs.getString("pt_id"));
                    patientName.setText(rs.getString("pt_name"));
                    patientInsurance.setText(rs.getString("INS_PR"));
                    patientAddressLine1.setText(rs.getString("addressline_1"));
                    patientAddressLine2.setText(rs.getString("addressline_2"));
                    patientCity.setText(rs.getString("city"));
                    patientState.setText(rs.getString("state"));
                    patientPostalCode.setText(rs.getString("postal_code"));
                    patientPhone.setText(rs.getString("phone"));
                    
                }
            } catch (NullPointerException npex) {
                npex.printStackTrace();
            } catch (SQLException ex) {
            Logger.getLogger(PatientRecordsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        getPatientList();
        
    }
    
    public void getPatientList() {
        ObservableList<Patient> patientList = FXCollections.observableArrayList();
       
        ResultSet rs = accessDB("SELECT * FROM patient, address WHERE patient.address_id = address.address_id", this.getClass().getName());
        colId.setCellValueFactory(cellData -> {
            return cellData.getValue().getId();
        });
        colName.setCellValueFactory(cellData -> {
            return cellData.getValue().getName();
        });
        colPhone.setCellValueFactory(cellData -> {
            return cellData.getValue().getPhone();
        });
        colState.setCellValueFactory(cellData -> {
            return cellData.getValue().getState();
        });
        colInsurance.setCellValueFactory(cellData -> {
            return cellData.getValue().getInsurance();
        });
        try {
            rs.beforeFirst(); //this is needed because the result set was looped through in accessDB.  We need to reset the cursor! 
            while (rs.next()) {
                String id = rs.getString("pt_id"); //parameter is the column name in the database
                String name = rs.getString("pt_name");
                String phone = rs.getString("phone");
                String state = rs.getString("state");
                String insurance = rs.getString("INS_PR");
                Patient tr = new Patient(
                    new ReadOnlyStringWrapper(id),
                    new ReadOnlyStringWrapper(name),
                    new ReadOnlyStringWrapper(phone),
                    new ReadOnlyStringWrapper(state),
                    new ReadOnlyStringWrapper(insurance)
                );
                patientList.add(tr);
            }
            table.setItems(patientList);
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    void updatePatient() {
        String id = selected.getId().getValue();
        

        String addressSql = "UPDATE address SET addressline_1 = ?, addressline_2 = ?, city = ?, state = ?, postal_code = ?, phone = ?, updated_at = now(), updated_by = ? WHERE address_id = ?";
        String patientSql = "UPDATE patient SET pt_name = ?, INS_PR = ?, updated_at = now(), updated_by = ? WHERE pt_id = ?";
        try {
            PreparedStatement ps = getConnection().prepareStatement(addressSql);
            PreparedStatement ps2 = getConnection().prepareStatement(patientSql);
            ps.setString(1, patientAddressLine1.getText());
            ps.setString(2, patientAddressLine2.getText());
            ps.setString(3, patientCity.getText());
            ps.setString(4, patientState.getText());
            ps.setString(5, patientPostalCode.getText());
            ps.setString(6, patientPhone.getText());
            ps.setString(7, User.currentUser);
            ps.setString(8, id);
            ps.execute();
            ps2.setString(1, patientName.getText());
            ps2.setString(2, patientInsurance.getText());
            ps2.setString(3, User.currentUser);
            ps2.setString(4, id);
            ps2.execute();
        } catch (SQLException ex) {
            Logger.getLogger(PatientRecordsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    void deletePatient() {
        String id = selected.getId().getValue();
        
        String patientSql = "DELETE FROM patient WHERE pt_id = ?";
        String addressSql = "DELETE FROM address WHERE address_id = ?";
        try {
            PreparedStatement ps = getConnection().prepareStatement(patientSql);
            PreparedStatement ps2 = getConnection().prepareStatement(addressSql);
            ps.setString(1, id);
            ps.execute();
            ps2.setString(1, id);
            ps2.execute();
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void addPatient(ArrayList<String> patient) {
        
        String addressSql = "INSERT INTO address (address_id, addressline_1, addressline_2, city, state, postal_code, phone, created_at, created_by, updated_at, updated_by) VALUES (?, ?, ?, ?, ?, ?, ?, now(), ?, now(), ?)";
        String patientSql = "INSERT INTO patient (pt_id, pt_name, address_id, INS_PR, address_address_id, created_at, created_by, updated_at, updated_by) VALUES (?, ?, ?, ?, ?, now(), ?, now(), ?)";
        String addressId, patientId;
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT SELECT MAX(address_id) FROM address"); //retrieve last rows ID so we can increment later
            ResultSet rs = ps.executeQuery();
            rs.next(); //only one record, so no need for a loop.  
            addressId = Integer.toString(Integer.parseInt(rs.getString(1)) + 1);
            
            ps = getConnection().prepareStatement(addressSql);
            ps.setString(1, addressId);
            ps.setString(2, patient.get(2));
            ps.setString(3, patient.get(3));
            ps.setString(4, patient.get(4));
            ps.setString(5, patient.get(5));
            ps.setString(6, patient.get(6));
            ps.setString(7, patient.get(7));
            ps.setString(8, User.currentUser);
            ps.setString(9, User.currentUser);

            ps.execute();
            
            ps = getConnection().prepareStatement("SELECT MAX(pt_id) FROM patient"); //retrieve last rows ID so we can increment later
            rs = ps.executeQuery();
            rs.next(); //only one record, so no need for a loop.  
            patientId = Integer.toString(Integer.parseInt(rs.getString(1)) + 1);
            
            ps = getConnection().prepareStatement(patientSql);
            ps.setString(1, patientId);
            ps.setString(2, patient.get(0));
            ps.setString(3, addressId);
            ps.setString(4, patient.get(1));
            ps.setString(5, addressId);
            ps.setString(6, User.currentUser);
            ps.setString(7, User.currentUser);
            ps.execute();
            
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
