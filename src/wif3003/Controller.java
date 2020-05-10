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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Controller {
    
    CPGame game;
    
    @FXML
    // The reference of inputText will be injected by the FXML loader
    private TextField inputText_n;
    
    @FXML
    private TextField inputText_t;
    
    @FXML
    private TextField inputText_m;

    // The reference of outputText will be injected by the FXML loader
//    @FXML
//    private TextArea outputText;
    @FXML
    private Label outputLbl;

    @FXML
    private GridPane insideBox;
    private Canvas canvas;
    private Canvas canvasPoint;
    private Canvas canvasLine;

    // location and resources will be automatically injected by the FXML loader
    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    // Add a public no-args constructor
    public Controller()
    {
        canvas = new Canvas(800, 800);
//        canvas2 = new Canvas(360, 300);
    }

    @FXML
    private void initialize()
    {
        insideBox.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLUE);
//        gc.setLineWidth(3);
//        gc.moveTo(0, 0);
//        gc.lineTo(0, 0);
//        gc.stroke();
        gc.setFill(Color.BLUE);
//        gc.fillOval(600, 600, 200, 200);
//        gc.fillOval(400, 400, 200, 200);
//        gc.fillOval(200, 200, 200, 200);
//        gc.fillOval(00, 00, 200, 200);
//        gc.moveTo(0, 0);
//        gc.lineTo(0, 800);
//        gc.stroke();
//        insideBox.getChildren().add(canvas2);
        System.out.printf("Resize : %b\n", canvas.isResizable());
    }

    @FXML
    private void printOutput()
    {
//        outputText.setText(inputText_n.getText());

        GraphicsContext gc = canvas.getGraphicsContext2D();
//        gc.setStroke(Color.GREEN);
        gc.setLineWidth(2);
        gc.moveTo(Math.random() * 300, Math.random() * 300);
        gc.lineTo(Math.random() * 300, Math.random() * 300);
        gc.stroke();
    }
    
    @FXML
    private void drawPoint(double x, double y)
    {
        GraphicsContext gc = canvasPoint.getGraphicsContext2D();
        x = x/1000 * canvasPoint.getWidth();
        y = y/1000 * canvasPoint.getHeight();
        gc.setFill(Color.RED);
        gc.fillOval(x-2, y-2, 4, 4);
    }
    
    @FXML
    private void drawLine(double x1, double y1, double x2, double y2)
    {
        GraphicsContext gc = canvasLine.getGraphicsContext2D();
        x1 = x1/1000 * canvasPoint.getWidth();
        y1 = y1/1000 * canvasPoint.getHeight();
        x2 = x2/1000 * canvasPoint.getWidth();
        y2 = y2/1000 * canvasPoint.getHeight();
        gc.setStroke(Color.GREEN);
        gc.setLineWidth(2);
        gc.moveTo(x1, y1);
        gc.lineTo(x2, y2);
        gc.stroke();
    }
    
    private DrawPointInterface _drawPoint = (x, y) -> { drawPoint(x, y); };
    private DrawLineInterface _drawLine = (x1, y1, x2, y2) -> { drawLine(x1, y1, x2, y2); };
    
    @FXML
    private void setParams()
    {
        int n, t, m;
        try {
            n = Integer.parseInt(inputText_n.getText());
            t = Integer.parseInt(inputText_t.getText());
            m = Integer.parseInt(inputText_m.getText());
        } catch(Exception e) {
            outputLbl.setText("Please enter Integer");
            return;
        }
        
        if (n <= t){
            System.out.println("Number of points(n) must be greater than number of threads(t)");
            outputLbl.setText("Number of points(n) must be greater than number of threads(t)\nSet n = t + 1");
            n = t + 1;
        }
        
        resetCanvas();
        game = new CPGame(n, t, m, _drawPoint, _drawLine);
        outputLbl.setText("Okay, game is created");

//        GraphicsContext gc = canvas2.getGraphicsContext2D();
//        gc.setStroke(Color.GREEN);
//        gc.setLineWidth(2);
//        gc.moveTo(Math.random() * 300, Math.random() * 300);
//        gc.lineTo(Math.random() * 300, Math.random() * 300);
//        gc.stroke();
    }

    @FXML
    private void resetCanvas() {
        game = null;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, 300, 300);
        
        if(canvasPoint != null) {
            GraphicsContext gcp = canvasPoint.getGraphicsContext2D();
            gcp.clearRect(0, 0, canvasPoint.getWidth(), canvasPoint.getHeight());
        }
        if(canvasLine != null) {
            GraphicsContext gcl = canvasLine.getGraphicsContext2D();
            gcl.clearRect(0, 0, canvasLine.getWidth(), canvasLine.getHeight());
        }

        // reset n add
//        canvas = new Canvas(360, 300);
//        insideBox.getChildren().add(canvas);
        canvasPoint = new Canvas(800, 800);
        insideBox.getChildren().add(canvasPoint);
        canvasLine = new Canvas(800, 800);
        insideBox.getChildren().add(canvasLine);
    }
    
    @FXML
    private void runGame() {
        if(game == null) {
            outputLbl.setText("Please init game first");
            return;
        }
        game.runSingleThreadGame();
    }
}
