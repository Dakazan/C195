/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.Localization;
import utils.LoginManager;

/**
 *
 * @author Darker
 */
public class SchedulingApp2 extends Application {
    
    private Stage primaryStage;

    @Override 
    public void start(Stage primaryStage) throws IOException {
       this.primaryStage = primaryStage;
       Scene scene = new Scene(new StackPane());
       Localization locale = new Localization();
       LoginManager loginManager = new LoginManager(scene);
       loginManager.showLoginForm();
       this.primaryStage.setTitle(locale.getTitleText());
       this.primaryStage.setScene(scene);
       //primaryStage.setResizable(false);
       this.primaryStage.show();
    }

    public static void main(String[] args) throws SQLException {
        
        DBConnection.startConnection();
        launch(args);
        DBConnection.closeConnection();
    }
    
    
    public void displayLoginForm(Stage stage) throws IOException {
        Scene scene = new Scene(new StackPane());
    
        LoginManager loginManager = new LoginManager(scene);
        loginManager.showLoginForm();
        stage.setScene(scene);
        stage.show();
    }
    
    
}
