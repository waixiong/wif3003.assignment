/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wif3003;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Controller {
    @FXML
    // The reference of inputText will be injected by the FXML loader
    private TextField inputText;

    // The reference of outputText will be injected by the FXML loader
    @FXML
    private TextArea outputText;

    @FXML
    private GridPane insideBox;
    private Canvas canvas1;
    private Canvas canvas2;

    // location and resources will be automatically injected by the FXML loader
    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    // Add a public no-args constructor
    public Controller()
    {
        canvas1 = new Canvas(360, 300);
        canvas2 = new Canvas(360, 300);
    }

    @FXML
    private void initialize()
    {
        insideBox.getChildren().add(canvas1);
        GraphicsContext gc = canvas1.getGraphicsContext2D();
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(3);
        gc.moveTo(0, 0);
        gc.lineTo(300, 0);
        gc.stroke();
        gc.moveTo(0, 0);
        gc.lineTo(0, 300);
        gc.stroke();
        insideBox.getChildren().add(canvas2);
    }

    @FXML
    private void printOutput()
    {
        outputText.setText(inputText.getText());

        GraphicsContext gc = canvas2.getGraphicsContext2D();
        gc.setStroke(Color.GREEN);
        gc.setLineWidth(2);
        gc.moveTo(Math.random() * 300, Math.random() * 300);
        gc.lineTo(Math.random() * 300, Math.random() * 300);
        gc.stroke();
    }

    @FXML
    private void resetCanvas() {
        GraphicsContext gc = canvas2.getGraphicsContext2D();
        gc.clearRect(0, 0, 300, 300);

        // reset n add
        canvas2 = new Canvas(360, 300);
        insideBox.getChildren().add(canvas2);
    }
}
