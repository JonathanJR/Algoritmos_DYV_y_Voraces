package clases;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DyV {

    private Grafica grafica;
    private Punto a;
    private Punto b;
    private Punto c;
    private double distancia;
    private double resultado;
    private double dMin;
    private double dMinIzq;
    private double dMinDer;
    private double escalay;
    private double escalax;
    private ArrayList<Punto> aux; // Conjunto de ciudades (modificadas coordenadas para representar)
    private ArrayList<Punto> ciudades; // Conjunto de ciudades (puntos originales del fichero)
    //private Comparator<Punto> comparadorEjeX;
    private Comparator<Punto> comparadorEjeY;
    Graphics2D g;
    Punto pivote;
    double distSolucion;

    int tamañoPuntoSolucion = 5;

    public DyV() {
        grafica = new Grafica();
        aux = grafica.getPuntos();
        ciudades = new ArrayList<>();
    }

    // Metodo para pintar el mapa de puntos
    public void pintarPuntosGrafica(Graphics2D g, int tamañoPuntos) {
        g.setColor(Color.BLUE);
        grafica.getPuntos().forEach((punto) -> {
            g.fillOval((int) punto.getX(), (int) punto.getY(), tamañoPuntos, tamañoPuntos);
        });
    }

    // Metodo para asignar los puntos solucion que vamos encontrando
    public void asignarPuntos(Punto p1, Punto p2, Punto p3, Punto pivote, double dMin) {
        this.a = p1;
        this.b = p2;
        this.c = p3;
        this.pivote = pivote;
        distSolucion = dMin;
    }

    // Metodo para dibujar los puntos solucion
    public void pintaSolucion(Graphics2D g, Punto p1, Punto p2, Punto p3, Color c, int x) {
        g.setColor(c);
        g.fillOval((int) p1.getX(), (int) p1.getY(), tamañoPuntoSolucion, tamañoPuntoSolucion);
        g.fillOval((int) p2.getX(), (int) p2.getY(), tamañoPuntoSolucion, tamañoPuntoSolucion);
        g.fillOval((int) p3.getX(), (int) p3.getY(), tamañoPuntoSolucion, tamañoPuntoSolucion);

        // Si la llamada se hace desde el algoritmo dyv pintamos las ultimas lineas
        // usadas como pivotes
        if (x == 0) {
            g.setStroke(new BasicStroke(0.5f));
            g.setColor(Color.RED);
            if ((pivote.getX() - distSolucion) > 556) {
                g.drawLine((int) (pivote.getX() - distSolucion), 592, (int) (pivote.getX() - distSolucion), 180);
            }
            if ((pivote.getX() + distSolucion) < 956) {
                g.drawLine((int) (pivote.getX() + distSolucion), 592, (int) (pivote.getX() + distSolucion), 180);
            }
            g.setColor(Color.MAGENTA);
            g.drawLine((int) pivote.getX(), 592, (int) pivote.getX(), 180);
        }
    }

    // Metodo para pintar las 3 lineas que unen los 3 puntos que conforman la
    // solucion encontrada
    public void pintaLineaSolucion(Graphics2D g, Punto p1, Punto p2, Punto p3, Color c) {
        g.setColor(c);
        g.setStroke(new BasicStroke(1.0f));
        g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
        g.drawLine((int) p2.getX(), (int) p2.getY(), (int) p3.getX(), (int) p3.getY());
        g.drawLine((int) p3.getX(), (int) p3.getY(), (int) p1.getX(), (int) p1.getY());
    }

    // Distancia entre 3 puntos
    public Double calcularDistanciaPuntos(Punto p1, Punto p2, Punto p3) {
        return p1.getDistancia(p2) + p2.getDistancia(p3) + p3.getDistancia(p1);
    }

    int n; // Variable auxiliar del dyv

    // Metodo algoritmo dyv
    public double distanciaMinimaDyV(List<Punto> conjuntoPuntos, Punto pivote) {
        if (conjuntoPuntos.size() >= 3) {
            if (n == 0) { // Solo necesito ordenar el conjunto en la primera ejecucion
                heapSortX(conjuntoPuntos.size(), conjuntoPuntos);
                n++;
            }
            pivote = conjuntoPuntos.get((conjuntoPuntos.size() / 2)); // Cojo el punto medio como pivote

            // Zona izquierda (INCLUSIVE-EXCLUSIVE)
            List<Punto> izquierda = conjuntoPuntos.subList(0, conjuntoPuntos.indexOf(pivote));
            dMinIzq = distanciaMinimaDyV(izquierda, pivote); // Llamada recursiva zona izquierda

            // Zona derecha (INCLUSIVE-EXCLUSIVE)
            List<Punto> derecha = conjuntoPuntos.subList(conjuntoPuntos.indexOf(pivote), conjuntoPuntos.size());
            dMinDer = distanciaMinimaDyV(derecha, pivote); // Llamada recursiva zona derecha

            return centro(izquierda, derecha, minimo(dMinIzq, dMinDer), pivote);
            /*
                * Le paso como parametros las dos sublistas, la distancia minima encontrada
		* para subdividir zona de medios escalay el pivote a partir del cual vamos a elegir
		* cuales puntos del medio se van a tomar en cuenta
             */
        } else {
            return Double.MAX_VALUE; // Devuelvo un valor muy grande para indicar que en el calculo del centro la
            // frontera es infinita
        }
    }

    // Metodo que me calcula la solucion de los puntos que han caido en el centro
    public double centro(List<Punto> izquierda, List<Punto> derecha, double frontera, Punto pivote) {
        ArrayList<Punto> subzonaIzquierda = new ArrayList<>();
        ArrayList<Punto> subzonaDerecha = new ArrayList<>();

        // Puntos que se encuentran en la subzona izquierda (desde el pivote hasta la frontera por la izquierda)
        for (Punto p : izquierda) {
            if (p.getX() >= (pivote.getX() - frontera)) {
                subzonaIzquierda.add(p);
            }
        }
        // Puntos que se encuentran en la subzona derecha (desde el pivote hasta la frontera por la derecha)
        for (Punto p : derecha) {
            if (p.getX() <= (pivote.getX() + frontera)) {
                subzonaDerecha.add(p);
            }
        }

        // Calculamos en las 2 direcciones, de izquierda a derecha escalay de derecha a
        // izquierda para evitar dejarnos alguna posible solucion
        busquedaExaustivaCentro(subzonaIzquierda, subzonaDerecha, pivote);
        busquedaExaustivaCentro(subzonaDerecha, subzonaIzquierda, pivote);

        return dMin; // Devuelvo la distancia minima encontrada
    }

    // Calculo la solucion de forma exhaustiva de los puntos que caen en el centro
    public void busquedaExaustivaCentro(ArrayList<Punto> aux1, ArrayList<Punto> aux2, Punto pivote) {
        for (Punto p1 : aux1) {
            for (Punto p2 : aux1) {
                if (!p1.equals(p2)) {
                    for (Punto p3 : aux2) {
                        distancia = calcularDistanciaPuntos(p1, p2, p3);
                        if (dMin == 0 || distancia < dMin) {
                            dMin = distancia;
                            if (dMin < resultado || resultado == 0) {
                                asignarPuntos(p1, p2, p3, pivote, dMin); // Asigno los puntos que seran la solucion
                                // final escalay las lineas frontera
                                resultado = dMin; // Guardo el valor de la distancia solucion final
                            }
                        }
                    }
                }
            }
        }
    }

    // Metodo que me calcula la solucion del problema de forma exhaustiva
    public void busquedaExaustiva(ArrayList<Punto> aux) {
        for (Punto p1 : aux) {
            for (Punto p2 : aux) {
                if (!p1.equals(p2)) {
                    for (Punto p3 : aux) {
                        if (!p2.equals(p3) && !p1.equals(p3)) {
                            distancia = calcularDistanciaPuntos(p1, p2, p3);
                            if (dMin == 0 || distancia < dMin) {
                                dMin = distancia;
                                if (dMin < resultado || resultado == 0) {
                                    asignarPuntos(p1, p2, p3, pivote, dMin); // Asigno los puntos que seran la solucion
                                    resultado = dMin; // Guardo el valor de la distancia solucion final
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Metodo que nos pinta la solucion (Si escalax==0 pintamos dyv si no, pintamos exhaustiva)
    public void pinta(Graphics2D g, int x) {
        dMin = 0.0;
        resultado = 0.0;
        if (x == 0) {
            distanciaMinimaDyV(aux, pivote);
        } else {
            busquedaExaustiva(aux);
        }
        pintaSolucion(g, a, b, c, Color.GREEN, x);
        pintaLineaSolucion(g, a, b, c, Color.BLACK);
    }

    // Metodo para leer puntos de un fichero .tsp
    public ArrayList<Punto> leerFichero(File f) {
        if (f != null) {
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

    // Con este metodo redimensiono los puntos leidos del fichero para que se
    // ajusten a la zona de la grafica de nuestra aplicacion - APLICO UN FACTOR DE ESCALA escalax e escalay
    private ArrayList<Punto> prepararGraficaCiudades(List<Punto> aux, List<Punto> ciudades) {
        Collections.sort(aux, getCompPuntosY());
        double yultimo = aux.get(aux.size() - 1).getY();
        double ypenultimo = aux.get(aux.size() - 2).getY();
        double aux2 = ypenultimo * 408 / yultimo;
        escalay = ypenultimo / aux2;

        for (Punto punto : aux) {
            punto.setY((punto.getY() / escalay + 180));
        }

        heapSortX(aux.size(), aux);

        double xultimo = aux.get(aux.size() - 1).getX();
        double xpenultimo = aux.get(aux.size() - 2).getX();
        double aux1 = xpenultimo * 408 / xultimo;
        escalax = xpenultimo / aux1;

        for (Punto punto : aux) {
            punto.setX((punto.getX() / escalax + 557));
        }

        heapSortX(ciudades.size(), ciudades);
        return (ArrayList<Punto>) aux;
    }
    
    void heapify(int size, int i, List<Punto> lista) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < size && lista.get(left).getX() > lista.get(largest).getX()) {
            largest = left;
        }

        if (right < size && lista.get(right).getX() > lista.get(largest).getX()) {
            largest = right;
        }

        if (largest != i) {
            double temp = lista.get(i).getX();
            lista.get(i).setX(lista.get(largest).getX());
            lista.get(largest).setX(temp);

            temp = lista.get(i).getY();
            lista.get(i).setY(lista.get(largest).getY());
            lista.get(largest).setY(temp);
            heapify(size, largest, lista);
        }
    }

    public void heapSortX(int size, List<Punto> lista) {
        int i;
        for (i = size / 2 - 1; i >= 0; i--) {
            heapify(size, i, lista);
        }
        for (i = size - 1; i >= 0; i--) {
            double temp = lista.get(0).getX();
            lista.get(0).setX(lista.get(i).getX());
            lista.get(i).setX(temp);

            temp = lista.get(0).getY();
            lista.get(0).setY(lista.get(i).getY());
            lista.get(i).setY(temp);

            heapify(i, 0, lista);
        }
    }

    // Metodo auxliar que me devuelve el minimo de dos numeros
    private double minimo(double dIzq, double dDer) {
        if (dIzq < dDer) {
            return dIzq;
        } else {
            return dDer;
        }
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

    public ArrayList<Punto> getCiudades() {
        return ciudades;
    }

    public ArrayList<Punto> getAux() {
        return aux;
    }

    public Grafica getGrafica() {
        return grafica;
    }

    public void setGrafica(Grafica grafica) {
        this.grafica = grafica;
    }

    public Punto getA() {
        return a;
    }

    public Punto getB() {
        return b;
    }

    public Punto getC() {
        return c;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public double getY() {
        return escalay;
    }

    public double getX() {
        return escalax;
    }
}



    // Comparador de puntos para ordenar por eje X
    /*public Comparator<Punto> getCompPuntosX() {
        comparadorEjeX = new Comparator<Punto>() {
            @Override
            public int compare(Punto p1, Punto p2) {
                Double xp1 = p1.getX();
                Double xp2 = p2.getX();
                //Si dos objetos son iguales en el eje de la X miro su coordenada Y
                if (Objects.equals(xp1, xp2) && p1.getY() > p2.getY()) {
                    return -1;
                }
                if (Objects.equals(xp1, xp2) && p1.getY() < p2.getY()) {
                    return 1;
                }
                return xp1.compareTo(xp2);
            }
        };
        return comparadorEjeX;
    }*/