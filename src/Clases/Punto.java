package clases;

import java.text.DecimalFormat;

public class Punto {

    private double x;
    private double y;

    public Punto(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    // Metodo que calcula la distancia de un punto a otro
    public double getDistancia(Punto a) {
        double aux1 = Math.pow(this.getX() - a.getX(), 2);
        double aux2 = Math.pow(this.getY() - a.getY(), 2);
        double distancia = Math.sqrt(aux1 + aux2);
        return distancia;
    }

    @Override
    public String toString() {
        double a = this.getX() - 544;
        double b = this.getY() - 100;
        return "[" + a + " , " + b + "]";
    }

    public String toString(int i) {
        DecimalFormat dfCorto = new DecimalFormat("#.0");
        double a = this.getX() - 544;
        double b = this.getY() - 100;
        return dfCorto.format(a) + ", " + dfCorto.format(b);
    }

}
