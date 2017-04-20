package MonteCarlo.model;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Random;

public class DrawingTask extends Task {
    private int numberOfPoints;
    private int canvasWidth;
    private int canvasHeight;
    private int MIN;
    private int MAX;
    private ArrayList<CalculatedPointListener> calculatedPointListeners = new ArrayList<>();

    private void processCalculatedPointEvent(CalculatedPointEvent event){
        if(calculatedPointListeners.size()==0)
            return;

        for(CalculatedPointListener listener : calculatedPointListeners){
            listener.handleCalculatedPoint(event);
        }
    }

    public void addListener(CalculatedPointListener listener){
        if(!calculatedPointListeners.contains(listener))
            calculatedPointListeners.add(listener);
    }

    public void removeListener(CalculatedPointListener listener){
        if(calculatedPointListeners.contains(listener))
            calculatedPointListeners.remove(listener);
    }

    public DrawingTask(int numberOfPoints, int canvasWidth, int canvasHeight, int MIN, int MAX) {
        this.numberOfPoints = numberOfPoints;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.MIN = MIN;
        this.MAX = MAX;
    }

    @Override
    protected Object call() throws Exception {
        Random random = new Random();
        for (int i = 0; i < numberOfPoints; i++) {
            double x = MIN + (MAX - MIN) * random.nextDouble();
            double y = MIN + (MAX - MIN) * random.nextDouble();
            int x2 = (int)(x * ((double)canvasWidth / (MAX - MIN)));
            int y2 = (int)(y * ((double)canvasHeight / (MAX - MIN)));
            processCalculatedPointEvent(new CalculatedPointEvent(this, new Point(canvasWidth / 2 + x2,canvasHeight / 2 - y2,Equation.calc(x, y) )));
            if(i%(100000) == 0){
                updateProgress(i, numberOfPoints);
            }
            if(isCancelled()){
                return null;
            }
        }
        updateProgress(1,1);
        return null;
    }
}