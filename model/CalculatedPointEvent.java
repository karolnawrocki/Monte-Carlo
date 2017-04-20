package MonteCarlo.model;

public class CalculatedPointEvent extends java.util.EventObject {
    private Point calculatedPoint;

    public CalculatedPointEvent(Object source, Point calculatedPoint) {
        super(source);
        this.calculatedPoint = calculatedPoint;
    }

    public Point getCalculatedPoint(){
        return this.calculatedPoint;
    }
}
