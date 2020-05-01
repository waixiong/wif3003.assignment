/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wif3003;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author thechee
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Concurrent Programming");
        primaryStage.setScene(new Scene(root, 400, 550));
        primaryStage.show();
    }


    public static void main(String[] args) {
        System.out.println("Java again");
        launch(args);
    }
    
}
