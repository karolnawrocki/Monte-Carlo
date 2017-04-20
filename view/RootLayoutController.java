package MonteCarlo.view;

import MonteCarlo.MainApp;
import MonteCarlo.model.CalculatedPointEvent;
import MonteCarlo.model.CalculatedPointListener;
import MonteCarlo.model.DrawingTask;

import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;

public class RootLayoutController {
    private MainApp mainApp;
    @FXML
    private Canvas canvas;
    @FXML
    private TextField numberOfPointsField;
    @FXML
    private Label calculatedAreaLabel;
    @FXML
    private ProgressBar progressBar;
    private DrawingTask task;
    private final int canvasWidth = 750;
    private final int canvasHeight = 750;
    private final int MIN = -8;
    private final int MAX = 8;
    private BufferedImage bi = new BufferedImage(canvasWidth,canvasHeight,BufferedImage.TYPE_INT_RGB);
    private int counterOfPointsBelongingToDiagram;
    private int counterOfTotalPoints;
    GraphicsContext gc;
    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();
    }

    private CalculatedPointListener calculatedPointListener = new CalculatedPointListener() {
        @Override
        public void handleCalculatedPoint(CalculatedPointEvent event) {
            if(event.getCalculatedPoint().isBelongsToDiagram()){
                bi.setRGB((int)event.getCalculatedPoint().getX(),(int)event.getCalculatedPoint().getY(),java.awt.Color.GREEN.getRGB());//65280
                counterOfPointsBelongingToDiagram++;
            }
            else{
                bi.setRGB((int)event.getCalculatedPoint().getX(),(int)event.getCalculatedPoint().getY(),java.awt.Color.RED.getRGB());
            }
            counterOfTotalPoints++;
            if(counterOfTotalPoints % 50000 == 0){
                gc.drawImage(SwingFXUtils.toFXImage(bi, null), 0,0 );
                gc.strokeLine(0,canvasHeight/2,canvasWidth,canvasHeight/2);
                gc.strokeLine(canvasWidth/2,0,canvasWidth/2,canvasHeight);
                for(int i = 1; i <= 7; i++){
                    gc.strokeLine(canvasWidth/2 + (double)canvasHeight / (MAX - MIN)*i,canvasHeight/2+10,canvasWidth/2 + (double)canvasHeight / (MAX - MIN)*i,canvasHeight/2-10);
                    gc.strokeLine(canvasWidth/2 - (double)canvasHeight / (MAX - MIN)*i,canvasHeight/2+10,canvasWidth/2 - (double)canvasHeight / (MAX - MIN)*i,canvasHeight/2-10);
                    gc.strokeLine(canvasWidth/2+10,canvasHeight/2 + (double)canvasHeight / (MAX - MIN)*i,canvasWidth/2-10,canvasHeight/2 + (double)canvasHeight / (MAX - MIN)*i);
                    gc.strokeLine(canvasWidth/2+10,canvasHeight/2 - (double)canvasHeight / (MAX - MIN)*i,canvasWidth/2-10,canvasHeight/2 - (double)canvasHeight / (MAX - MIN)*i);
                }
            }
        }
    };

    @FXML
    private void handleRunButtonAction(){
        calculatedAreaLabel.setText("Calculating...");
        for (int i = 0; i < canvasHeight; i++)
            for (int j = 0; j < canvasWidth; j++)
                bi.setRGB(i,j, java.awt.Color.WHITE.getRGB());
        this.counterOfTotalPoints = 0;
        this.counterOfPointsBelongingToDiagram = 0;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(1);
        gc.setStroke(Color.WHITE);
        task = new DrawingTask(Integer.parseInt(numberOfPointsField.getText()), canvasWidth,canvasHeight,MIN,MAX);
        task.addListener(calculatedPointListener);
        progressBar.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                //task.addListener(calculatedPointListener);
                calculatedAreaLabel.setText("Done! Calculated area: " + Double.toString(calculateArea(counterOfPointsBelongingToDiagram,counterOfTotalPoints)));
            }
        });
        new Thread(task).start();
    }

    @FXML
    private void handleStopButtonAction(){
        task.cancel(true);
        calculatedAreaLabel.setText("Calculation cancelled, calculated area: " + Double.toString(calculateArea(counterOfPointsBelongingToDiagram,counterOfTotalPoints)));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private double calculateArea(int counterOfPointsBelongingToDiagram, int counterOfTotalPoints){
        return counterOfPointsBelongingToDiagram/((double)counterOfTotalPoints) * Math.pow((MAX - MIN),2);
    }
}