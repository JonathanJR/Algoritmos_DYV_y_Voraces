package clases;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Voraces {

    private Grafica grafica;
    private double distancia;
    private ArrayList<Punto> aux; // Conjunto de ciudades (modificadas coordenadas para representar)
    private ArrayList<Punto> ciudades; // Conjunto de ciudades (puntos originales del fichero)
    private ArrayList<Punto> usados; // Conjunto de puntos que vamos usando durante el recorrido
    private ArrayList<Punto> no_usados; // Conjunto de puntos que nos quedan por recorrer
    private ArrayList<Arista> solucion; // Conjunto de aristas solucion
    private ArrayList<Arista> aristas; // Conjunto de aristas que vamos generando
    private Comparator<Punto> comparadorEjeX;
    private Comparator<Punto> comparadorEjeY;
    private Comparator<Arista> comparadorPesoAristas;
    private String nombreFichero;
    Graphics2D g;

    private ArrayList<Punto> a; // Array de puntos para Kruskal
    private ArrayList<Integer> b; // Array de codigos para Kruskal
    private double escaladoy, escaladox;

    public Voraces() {
        grafica = new Grafica();
        aux = grafica.getPuntos();
        ciudades = new ArrayList<>();
        usados = new ArrayList<>();
        no_usados = new ArrayList<>();
        solucion = new ArrayList<>();
        aristas = new ArrayList<>();
        a = new ArrayList<>();
        b = new ArrayList<>();
    }

    // Metodo para pintar el mapa de puntos
    public void pintarPuntosGrafica(Graphics2D g, int tamañoPuntos) {
        g.setColor(Color.BLUE);
        for (Punto punto : grafica.getPuntos()) {
            g.fillOval((int) punto.getX(), (int) punto.getY(), tamañoPuntos, tamañoPuntos);
        }
    }

    // Algoritmo de Prim
    public void prim(Graphics2D g, int x, boolean secuencial, int velocidad) {
        int numAleatorio = (int) (Math.random() * aux.size());
        for (Punto p : aux) {
            no_usados.add(p);
        }
        Punto partida = aux.get(numAleatorio);
        generarAristas(partida);

        while (!no_usados.isEmpty()) {
            Arista aristaMin = aristas.get(0); // Cojo arista menor peso
            Punto siguiente = aristaMin.getB();
            solucion.add(aristaMin);
            aristas.remove(0);
            generarAristas(siguiente);
            if (secuencial) {
                pintaSolucion(g, Color.RED, x, secuencial);
                try {
                    TimeUnit.MILLISECONDS.sleep(velocidad);
                } catch (InterruptedException e) {
                }
            }
        }
        if (!secuencial) {
            pintaSolucion(g, Color.RED, x, secuencial);
        } else {
            calcularDistancia(x);
        }
    }

    // Algoritmo de kruskal
    public void kruskal(Graphics2D g, int x, boolean secuencial, int velocidad) {
        generarAristas();
        for (int i = 0; i < aux.size(); i++) {
            a.add(aux.get(i));
        }
        for (int i = 0; i < aux.size(); i++) {
            b.add(i);
        }
        for (Arista arista : aristas) {
            if (comprobar(arista)) {
                solucion.add(arista);
                if (!usados.contains(arista.getA())) {
                    usados.add(arista.getA());
                }
                if (!usados.contains(arista.getB())) {
                    usados.add(arista.getB());
                }
                if (secuencial) {
                    pintaSolucion(g, Color.RED, x, secuencial);
                    try {
                        TimeUnit.MILLISECONDS.sleep(velocidad);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        if (!secuencial) {
            pintaSolucion(g, Color.RED, x, secuencial);
        } else {
            calcularDistancia(x);
        }
    }

    // Comprueba si la arista pasada como parametro forma un ciclo aplicando el
    // algoritmo de kruskal
    private boolean comprobar(Arista arista) {
        int b1 = a.indexOf(arista.getA());
        int b2 = a.indexOf(arista.getB());
        if (b.get(b1) != b.get(b2)) {
            int valorACambiar = b.get(b2);
            for (int i = 0; i < b.size(); i++) {
                if (b.get(i) == valorACambiar) {
                    b.set(i, b.get(b1));
                }
            }
            return true;
        }
        return false;
    }

    // Metodo para dibujar las aristas que forman la solucion
    public void pintaSolucion(Graphics2D g, Color c, int x, boolean secuencial) {
        g.setColor(c);
        g.setStroke(new BasicStroke(1.0f));
        for (Arista arista : solucion) {
            Punto pa = arista.getA();
            Punto pb = arista.getB();
            g.drawLine((int) pa.getX(), (int) pa.getY(), (int) pb.getX(), (int) pb.getY());
            if (!secuencial) {
                if (x == 0) {
                    distancia += arista.getPeso();
                } else {
                    distancia += aplicarFactorEscala(pa).getDistancia(aplicarFactorEscala(pb));
                }
            }
        }
    }

    // Metodo auxiliar para calcular distancia - El parametro es para ver si estamos
    // con puntos aleatorios o puntos de fichero .tsp
    public void calcularDistancia(int x) {
        if (x == 0) {
            for (Arista arista : solucion) {
                distancia += arista.getPeso();
            }
        } else {
            for (Arista arista : solucion) {
                distancia += aplicarFactorEscala(arista.getA()).getDistancia(aplicarFactorEscala(arista.getB()));
            }
        }
    }

    // Genera todas las aristas posibles del conjunto
    private void generarAristas() {
        for (int i = 0; i < aux.size(); i++) {
            Punto p1 = aux.get(i);
            for (int j = i + 1; j < aux.size(); j++) {
                Punto p2 = aux.get(j);
                aristas.add(new Arista(p1, p2));
            }
        }
        ordenarAristas();
    }

    // Metodo que me genera todas las aristas de un punto dado
    private void generarAristas(Punto p1) {
        usados.add(p1);
        no_usados.remove(p1);
        for (Punto p2 : no_usados) {
            aristas.add(new Arista(p1, p2));
        }
        limpiarAristas();
        ordenarAristas();
    }

    // Metodo para limpiar de la lista de aristas todas las que ya no son validas
    private void limpiarAristas() {
        ArrayList<Arista> aristasAux = new ArrayList<>();
        for (Arista arista : aristas) {
            aristasAux.add(arista);
        }

        for (Arista arista : aristasAux) {
            if (usados.contains(arista.getA()) && usados.contains(arista.getB())) {
                aristas.remove(arista);
            }
        }
    }

    // Metodo para ordenar las aristas por su peso
    @SuppressWarnings("unchecked")
    private void ordenarAristas() {
        Collections.sort(aristas, getCompPeso());
    }

    // Comparador de aristas por su peso
    @SuppressWarnings("rawtypes")
    private Comparator getCompPeso() {
        comparadorPesoAristas = (Arista a1, Arista a2) -> {
            Double xa1 = a1.getPeso();
            Double xa2 = a2.getPeso();
            return xa1.compareTo(xa2);
        };
        return comparadorPesoAristas;
    }

    // Metodo para leer puntos de un archivo .tsp
    public ArrayList<Punto> leerFichero(File f) {
        if (f != null) {
            nombreFichero = f.getName();
            FileReader fr;
            BufferedReader br;
            String cadena;
            try {
                fr = new FileReader(f);
                br = new BufferedReader(fr);
                for (int i = 0; i < 6; i++) {
                    br.readLine();
                }
                while ((cadena = br.readLine()) != null && (!cadena.equals("EOF"))) {
                    String[] lectura = cadena.split(" ");
                    double x = Double.valueOf(lectura[1]);
                    double y = Double.valueOf(lectura[2]);
                    aux.add(new Punto(x, y));
                    ciudades.add(new Punto(x + 544, y + 100));
                }

                br.close();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return prepararGraficaCiudades(aux, ciudades);
    }

    // Metodo para exportar la solucion a un fichero en la ruta indicada
    public void guardarFichero(ArrayList<Punto> usados, ArrayList<Punto> ciudadesVoraz, File file, String algoritmo) {
        FileWriter fichero = null;
        PrintWriter pw;
        try {
            fichero = new FileWriter(file.getAbsolutePath() + "/" + nombreFichero + ".opt." + algoritmo);
            pw = new PrintWriter(fichero);

            pw.println("NAME : " + nombreFichero + ".opt." + algoritmo);
            pw.println("TYPE : " + algoritmo);
            pw.println("DIMENSION : " + (ciudadesVoraz.size() - 1));
            pw.println("SOLUTION : " + getDistancia());
            pw.println(algoritmo.toUpperCase() + "_SECTION");

            for (Arista aris : solucion) {
                pw.println(aplicarFactorEscala(aris.getA()).toString(0) + " ---> " + aplicarFactorEscala(aris.getB()).toString(0));
            }

            pw.println("-1");
            pw.println("EOF");

        } catch (IOException e) {
        } finally {
            try {
                // Nuevamente aprovechamos el finally para asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (IOException e2) {
            }
        }
    }

    public Punto aplicarFactorEscala(Punto p) {
        return new Punto((p.getX() - 557) * escaladox + 544, (p.getY() - 180) * escaladoy + 100);
    }

    // Con este metodo redimensiono los puntos leidos del fichero para que se
    // ajusten a la zona de la grafica de nuestra aplicacion
    private ArrayList<Punto> prepararGraficaCiudades(List<Punto> aux, List<Punto> ciudades) {
        Collections.sort(aux, getCompPuntosY());
        double yultimo = aux.get(aux.size() - 1).getY();
        double ypenultimo = aux.get(aux.size() - 2).getY();
        double aux2 = ypenultimo * 408 / yultimo;
        escaladoy = ypenultimo / aux2;

        for (Punto punto : aux) {
            punto.setY((punto.getY() / escaladoy + 180));
        }

        Collections.sort(aux, getCompPuntosX());
        double xultimo = aux.get(aux.size() - 1).getX();
        double xpenultimo = aux.get(aux.size() - 2).getX();
        double aux1 = xpenultimo * 408 / xultimo;
        escaladox = xpenultimo / aux1;

        for (Punto punto : aux) {
            punto.setX((punto.getX() / escaladox + 557));
        }
        Collections.sort(ciudades, getCompPuntosX());

        return (ArrayList<Punto>) aux;
    }

    // Comparador de puntos para ordenar por eje X
    public Comparator<Punto> getCompPuntosX() {
        comparadorEjeX = (Punto p1, Punto p2) -> {
            Double xp1 = p1.getX();
            Double xp2 = p2.getX();
            return xp1.compareTo(xp2);
        };
        return comparadorEjeX;
    }

    // Comparador de puntos para ordenar por eje Y
    public Comparator<Punto> getCompPuntosY() {
        comparadorEjeY = (Punto p1, Punto p2) -> {
            Double yp1 = p1.getY();
            Double yp2 = p2.getY();
            return yp1.compareTo(yp2);
        };
        return comparadorEjeY;
    }

    public Grafica getGrafica() {
        return grafica;
    }

    public void setGrafica(Grafica grafica) {
        this.grafica = grafica;
    }

    public double getDistancia() {
        return distancia;
    }

    public ArrayList<Punto> getAux() {
        return aux;
    }

    public ArrayList<Punto> getCiudades() {
        return ciudades;
    }

    public ArrayList<Punto> getUsados() {
        return usados;
    }

    public ArrayList<Arista> getSolucion() {
        return solucion;
    }

}
