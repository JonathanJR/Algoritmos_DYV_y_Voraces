package clases;

import java.util.ArrayList;

public final class Grafica {

	private ArrayList<Punto> puntos = new ArrayList<>();
	private int numPuntos;

	public Grafica() {
		int s = 2;
		generarPuntos(numPuntos, s);
	}

	// Metodo que genera los puntos aleatorios entre los margenes de la zona de la
	// grafica, con un tamaño de ancho x alto de 408
	public void generarPuntos(int numPuntos, int s) {
		double x, y;
		int nx = 408; // Tamaño del cuadrado a representar eje x
		int ny = 408; // Tamaño del cuadrado a representar eje y
		for (int i = 0; i < numPuntos; i++) {
			switch (s) {
			case 0: // Caso peor, genero puntos sobre la vertical del punto X que coincide con el
					// centro de la grafica
				x = 761;
				y = (Math.random() * ny) + 180;
				puntos.add(new Punto(x, y));
				break;
			case 1: // Caso mejor, genero puntos sobre la horizontal del punto Y que coincide con la
					// mitad de la grafica
				x = (int) (Math.random() * nx) + 557;
				y = 384;
				puntos.add(new Punto(x, y));
				break;
			default: // Caso medio, genero puntos aleatorios que esten comprendidos entre los
						// margenes de la zona dibujable de la grafica
				x = (Math.random() * nx) + 557;
				y = (Math.random() * ny) + 180;
				puntos.add(new Punto(x, y));
				break;
			}
		}
	}

	public ArrayList<Punto> getPuntos() {
		return puntos;
	}

	public void setPuntos(ArrayList<Punto> puntos) {
		this.puntos = puntos;
	}

	public int getNumPuntos() {
		return numPuntos;
	}

	public void setNumPuntos(int numPuntos, int s) {
		this.numPuntos = numPuntos;
		generarPuntos(numPuntos, s);
	}

}
