/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import utils.LoginManager;

import java.sql.SQLException;

/** Controls the main application screen */
public class MainMenuController {
  @FXML private Button logoutButton;
  @FXML private Text loggedUser;
  
  public void initialize() {}
  
  public void initSessionID(final LoginManager loginManager, String counselor) throws SQLException, InterruptedException {
    loggedUser.setText("Hello, " + counselor);
    logoutButton.setOnAction((ActionEvent event) -> {
        loginManager.logout();
    });
    Popup.upcomingAppointmentsPopup();
  }
  
  public String getUser() {

    return loggedUser.getText();

  }
  
}