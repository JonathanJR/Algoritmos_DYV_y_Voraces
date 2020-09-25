package clases;

public final class Arista {

    private Punto a, b;
    private Double peso;

    public Arista(Punto a, Punto b) {
        this.a = a;
        this.b = b;
        setPeso(a, b);
    }

    public double getPeso() {
        return peso;
    }

    public Punto getA() {
        return a;
    }

    public void setA(Punto a) {
        this.a = a;
    }

    public Punto getB() {
        return b;
    }

    public void setB(Punto b) {
        this.b = b;
    }

    // Metodo que nos indica el peso de la arista (distancia entre extremos)
    public void setPeso(Punto a, Punto b) {
        double aux1 = Math.pow(a.getX() - b.getX(), 2);
        double aux2 = Math.pow(a.getY() - b.getY(), 2);
        double distancia = Math.sqrt(aux1 + aux2);
        this.peso = distancia;
    }
}
