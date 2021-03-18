/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import views.*;

/** Manages control flow for logins */
public class LoginManager {
  private final Scene scene;

  public LoginManager(Scene scene) {
    this.scene = scene;
  }

  /**
   * Callback method invoked to notify that a user has been authenticated.
   * Will show the main application screen.
     * @param counselor
   */ 
  public void authenticated(String counselor) {
    showMainMenu(counselor);
  }

  /**
   * Callback method invoked to notify that a user has logged out of the main application.
   * Will show the login application screen.
   */ 
  public void logout() {
    showLoginForm();
  }
  
  public void showLoginForm() {
    try {
      FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/views/LoginForm.fxml")
      );
      scene.setRoot(loader.load());
      LoginFormController controller = loader.getController();
      controller.initManager(this);
    } catch (IOException ex) {
      Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void showMainMenu(String counselor) {
    try {
      FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/views/MainMenu.fxml")
      );
      scene.setRoot(loader.load());
      MainMenuController controller = loader.getController();
      controller.initSessionID(this, counselor);
    } catch (IOException | SQLException | InterruptedException ex) {
      Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static void processLog(boolean valid, String user) {
      if (valid) {
        // Append new logins to respective file.
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(new File("success.txt"),true));
            pw.append("["+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))+"]"
                    + " Successful login for user: " + user + "\n");
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
      } else {
          try {
              PrintWriter pw = new PrintWriter(new FileOutputStream(new File("failure.txt"),true));
              pw.append("["+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))+"]"
                      + " Failed login for user: " + user + "\n");
              pw.close();
          } catch (FileNotFoundException ex) {
              Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
  }
}