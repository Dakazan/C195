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
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import utils.*;

/** Controls the login screen */
public class LoginFormController {
  @FXML private TextField user;
  @FXML private TextField password;
  @FXML private TextField pin;
  @FXML private Text timeLocal;
  @FXML private Text mainOfficeTime;
  @FXML private Button loginButton;
  
  
  
  public void initialize() {
      Localization locale = new Localization();
      user.setPromptText(locale.getUsernameText());
      password.setPromptText(locale.getPasswordText());
      pin.setPromptText(locale.getPinText());
      loginButton.setText(locale.getLoginText());
      Platform.runLater(() -> user.requestFocus());
      TimeHelper.loginClock(timeLocal, mainOfficeTime);
  }
  
  
  public void initManager(final LoginManager loginManager) {
    loginButton.setOnAction((ActionEvent event) -> {
        String sessionID = authorize();
        if (sessionID != null) {
            loginManager.authenticated(sessionID);
        }
    });
    
    // When pin textfield is highlighted (aka last field in form), allow submission of form by pressing ENTER key instead of clicking the button.
    pin.setOnKeyPressed(e -> {
    if (e.getCode() == KeyCode.ENTER) {
        String sessionID = authorize();
        if (sessionID != null) {
          loginManager.authenticated(sessionID);
        }
        }
    });
  }
  

  /**
   * Check authorization credentials.
   * 
   * If accepted, return a sessionID for the authorized session
   * otherwise, return null.
   */   
  private String authorize() {
    

    if (DBConnection.validCredentials(user.getText(), password.getText(), pin.getText())) {
        LoginManager.processLog(true, user.getText());
        return generateSessionID(user.getText(), "");
    }
    else {
        LoginManager.processLog(false, user.getText());
        invalidCredentials();
        return null;
    }
    
  }
  
  @FXML void invalidCredentials() {
    Localization locale = new Localization();
    Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(locale.getWarningTitleText());
        alert.setHeaderText(locale.getWarningMessageText());
        alert.showAndWait()
        .filter(response -> response == ButtonType.OK)
        .ifPresent((ButtonType response) -> {
            initialize(); //reinitialize program
            }
        );
  }
  
  
    

  private String generateSessionID(String userName, String userID) {
    return userName;
  }

    
}
