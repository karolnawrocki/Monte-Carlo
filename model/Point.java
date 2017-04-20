package MonteCarlo.model;


public class Point extends Object{
    private double x;
    private double y;
    private boolean belongsToDiagram;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isBelongsToDiagram() {
        return belongsToDiagram;
    }

    public Point(double x, double y, boolean belongsToDiagram) {
        this.x = x;
        this.y = y;
        this.belongsToDiagram = belongsToDiagram;

    }
}
