/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wif3003;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
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
    
    @FXML
    private ToggleButton stoggles;

    // The reference of outputText will be injected by the FXML loader
//    @FXML
//    private TextArea outputText;
    @FXML
    private Label outputLbl;

    @FXML
    private GridPane insideBox;
    private Canvas canvas;
    private Canvas canvasPoint;
//    private Canvas canvasLine;
    private Map<Integer, Canvas> canvasLines;

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
        canvasLines = new HashMap<Integer, Canvas>();
//        Platform.setImplicitExit(false);
    }

    @FXML
    private void initialize()
    {
//        Platform.setImplicitExit(false);
        stoggles.selectedProperty();
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
    private void drawLine(double x1, double y1, double x2, double y2, int colorTag)
    {   
        Platform.runLater(new DrawLineRunnable(x1, y1, x2, y2, colorTag, canvasLines, insideBox, game));
//        Canvas canvasLine = canvasLines.get(colorTag);
//        if(canvasLine == null) {
//            canvasLine = new Canvas(800, 800);
//            insideBox.getChildren().add(canvasLine);
//            canvasLines.put(colorTag, canvasLine);
//        }
//        GraphicsContext gc = canvasLine.getGraphicsContext2D();
//        x1 = x1/1000 * canvasPoint.getWidth();
//        y1 = y1/1000 * canvasPoint.getHeight();
//        x2 = x2/1000 * canvasPoint.getWidth();
//        y2 = y2/1000 * canvasPoint.getHeight();
////        gc.setStroke(Color.GREEN);
//        gc.setStroke(Color.rgb(colorTag * 127 % 256, colorTag * 159 % 256, colorTag * 191 % 256));
//        gc.setLineWidth(2);
//        gc.moveTo(x1, y1);
//        gc.lineTo(x2, y2);
//        gc.stroke();
    }
    
    private DrawPointInterface _drawPoint = (x, y) -> { drawPoint(x, y); };
    private DrawLineInterface _drawLine = (x1, y1, x2, y2, c) -> { drawLine(x1, y1, x2, y2, c); };
    
    @FXML
    private void setParams()
    {
        int n, t, m;
        boolean sleep;
        try {
            n = Integer.parseInt(inputText_n.getText());
            t = Integer.parseInt(inputText_t.getText());
            m = Integer.parseInt(inputText_m.getText());
            sleep = stoggles.selectedProperty().get();
        } catch(Exception e) {
            outputLbl.setText("Please enter Integer");
            return;
        }
        
        if (n <= t){
            System.out.println("Number of points(n) must be greater than number of threads(t)");
            outputLbl.setText("Number of points(n) must be greater than number of threads(t)\nSet n = t + 1");
            n = t + 1;
            return;
        }
        
        resetCanvas();
        game = new CPGame(n, t, m, _drawPoint, _drawLine, sleep);
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
            insideBox.getChildren().remove(canvasPoint);
        }
//        if(canvasLine != null) {
//            GraphicsContext gcl = canvasLine.getGraphicsContext2D();
//            gcl.clearRect(0, 0, canvasLine.getWidth(), canvasLine.getHeight());
//        }
        Iterator iterator = canvasLines.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry cL = (Map.Entry) iterator.next();
            Canvas canvasLine = (Canvas) cL.getValue();
            
            GraphicsContext gcl = canvasLine.getGraphicsContext2D();
            gcl.clearRect(0, 0, canvasLine.getWidth(), canvasLine.getHeight());
            insideBox.getChildren().remove(canvasLine);
        }
        canvasLines = new HashMap<Integer, Canvas>();

        // reset n add
//        canvas = new Canvas(360, 300);
//        insideBox.getChildren().add(canvas);
        canvasPoint = new Canvas(800, 800);
        insideBox.getChildren().add(canvasPoint);
//        canvasLine = new Canvas(800, 800);
//        insideBox.getChildren().add(canvasLine);
        
        game = null;
        outputLbl.setText("Game clear and reset");
    }
    
    @FXML
    private void runGame() {
        if(game == null) {
            outputLbl.setText("Please init game first");
            return;
        }
//        game.runSingleThreadGame();
//        Platform.runLater(() -> {
//            game.runConcurrentGame();
//            outputLbl.setText("Game finish");
//        });
        Thread main = new Thread(() -> {
            game.runConcurrentGame();
            Platform.runLater(() -> {
                outputLbl.setText(game.result);
            });        
        });
        main.start();
    }
}

class DrawLineRunnable implements Runnable {
    double x1;
    double y1;
    double x2;
    double y2;
    int colorTag;
//    DrawLineInterface drawLine;
    Map<Integer, Canvas> canvasLines;
    GridPane insideBox;
    CPGame game; // for file writer
    
    public DrawLineRunnable(double x1, double y1, double x2, double y2, int colorTag, Map<Integer, Canvas> canvasLines, GridPane insideBox, CPGame game) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.colorTag = colorTag;
        this.canvasLines = canvasLines;
        this.insideBox = insideBox;
        this.game = game;
    }

    @Override
    public void run() {
//        drawLine.draw(x1, y1, x2, y2, colorTag);
        Canvas canvasLine = canvasLines.get(colorTag);
        if(canvasLine == null) {
            canvasLine = new Canvas(800, 800);
            insideBox.getChildren().add(canvasLine);
            canvasLines.put(colorTag, canvasLine);
        }
        GraphicsContext gc = canvasLine.getGraphicsContext2D();
//            final double fx1 = x1/1000 * canvasPoint.getWidth();
//            y1 = y1/1000 * canvasPoint.getHeight();
//            x2 = x2/1000 * canvasPoint.getWidth();
//            y2 = y2/1000 * canvasPoint.getHeight();
//            gc.setStroke(Color.GREEN);
        gc.setStroke(Color.rgb(colorTag * 127 % 256, colorTag * 159 % 256, colorTag * 191 % 256));
        gc.setLineWidth(2);
        gc.moveTo(x1, y1);
        gc.lineTo(x2, y2);
        gc.stroke();
        try {
            //        System.out.println("\t\tUI drawed");
            game.fw.write(String.format("\tUI drawed (%.0f, %.0f), (%.0f, %.0f)\n", x1, y1, x2, y2));
        } catch (IOException ex) {
            // ignore error
            // Logger.getLogger(DrawLineRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}