package test;

import clases.Arista;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.SystemColor;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import clases.DyV;
import clases.Grafica;
import clases.Punto;
import clases.Voraces;
import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class Main extends JFrame {

    private JPanel panelBase;
    private DyV trioPuntos;
    private Voraces voraces;
    private JTextField textNumeroPuntos_parte1;
    private JTextField textNumeroPuntos_parte2;
    private int numPuntos;
    private String temp = "mili";
    private static long tinicio, tfin, tiempo;
    private JTextField tiempo_parte1;
    private JTextField tiempo_parte2;
    private JTextField textPunto1;
    private JTextField textPunto2;
    private JTextField textPunto3;
    private JTextField textDistancia_parte1;
    private JTextField textDistancia_parte2;
    private JTextField textRuta_parte1;
    private JTextField textRuta_parte2;
    private TextArea textArea;
    private Grafica aux;
    DecimalFormat df = new DecimalFormat("#.00000");
    DecimalFormat dfMedio = new DecimalFormat("#.000");
    DecimalFormat dfCorto = new DecimalFormat("#.0");
    private File fichero;
    private ArrayList<Punto> ciudadesMain;
    private String algoritmo;
    int ciudad = 0;
    int tamañoPuntos;
    private JTextField textVelocidad;

    /**
     * Launch the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Main frame = new Main();
                frame.setVisible(true);
            } catch (Exception e) {
            }
        });
    }

    /**
     * Create the frame.
     */
    public Main() {
        setTitle("Practica 2 - Algoritmica Computacional - Jonathan 2019/20"); // Titulo ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1020, 640); // Tamaño ventana

        // Panel base
        panelBase = new JPanel();
        panelBase.setBackground(new Color(240, 240, 240));
        panelBase.setLayout(null);
        setContentPane(panelBase); // Asignamos el panel base a la ventana

        // Label titulo principal
        JLabel label_titulo = new JLabel("Practica 2 : Algoritmos DYV y Voraces");
        label_titulo.setFont(new Font("Microsoft YaHei", Font.BOLD, 23));
        label_titulo.setBounds(10, 11, 440, 34);
        panelBase.add(label_titulo);

        // TabbedPane para crear 2 pestañas
        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
        tabbedPane.setBounds(10, 56, 975, 530);
        panelBase.add(tabbedPane);

        // Modelo para el spinner del tamaño de los puntos
        SpinnerNumberModel model = new SpinnerNumberModel();
        model.setValue(4);
        model.setMaximum(7);
        model.setMinimum(2);

        ////////////////////////////// PESTAñA 1 - Items /////////////////////////////
        // Panel principal de la pestaña 1
        JPanel panel1 = new JPanel();
        panel1.setLayout(null);
        tabbedPane.addTab("Parte 1 - DyV", null, panel1, null);

        // Panel para mostrar grafica
        JPanel panelGrafica_parte1 = new JPanel();
        panelGrafica_parte1.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
        panelGrafica_parte1.setBounds(535, 67, 415, 415);
        panel1.add(panelGrafica_parte1);

        // Etiqueta numero de puntos
        JLabel label_numPuntos_parte1 = new JLabel("Nº de puntos:");
        label_numPuntos_parte1.setEnabled(false);
        label_numPuntos_parte1.setFont(new Font("Tahoma", Font.PLAIN, 12));
        label_numPuntos_parte1.setBounds(20, 59, 95, 14);
        panel1.add(label_numPuntos_parte1);

        // Etiqueta Titulo Resultados
        JLabel label_resultados_parte1 = new JLabel("RESULTADOS DE EJECUCION : ");
        label_resultados_parte1.setFont(new Font("Microsoft YaHei", Font.BOLD, 18));
        label_resultados_parte1.setBounds(30, 226, 282, 34);
        panel1.add(label_resultados_parte1);

        // Cuadro de texto para recoger la cantidad de puntos aleatorios
        textNumeroPuntos_parte1 = new JTextField();
        textNumeroPuntos_parte1.setEnabled(false);
        textNumeroPuntos_parte1.setText("15");
        textNumeroPuntos_parte1.setBounds(107, 57, 41, 20);
        panel1.add(textNumeroPuntos_parte1);
        textNumeroPuntos_parte1.setColumns(10);

        // Boton que nos muestra los puntos en la grafica
        JButton btnMostrarPuntos_parte1 = new JButton("Mostrar puntos");
        btnMostrarPuntos_parte1.setEnabled(false);
        btnMostrarPuntos_parte1.setBounds(215, 110, 136, 23);
        panel1.add(btnMostrarPuntos_parte1);

        // Boton para calcular el dyv
        JButton btnDyV = new JButton("Divide y Venceras");
        btnDyV.setEnabled(false);
        btnDyV.setBounds(338, 174, 143, 23);
        panel1.add(btnDyV);

        // Boton para calcular de forma exhaustiva
        JButton btnExhaustiva = new JButton("Exhaustiva");
        btnExhaustiva.setEnabled(false);
        btnExhaustiva.setBounds(175, 174, 143, 23);
        panel1.add(btnExhaustiva);

        // JPanel para indicar en que unidades mostramos el tiempo de ejecucion
        JPanel panelTiempo_parte1 = new JPanel();
        FlowLayout flowLayout_parte1 = (FlowLayout) panelTiempo_parte1.getLayout();
        flowLayout_parte1.setAlignment(FlowLayout.LEFT);
        panelTiempo_parte1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Mostrar tiempo en:",
                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelTiempo_parte1.setBounds(30, 265, 161, 116);
        panel1.add(panelTiempo_parte1);

        // Checkbox para mostrar en milisegundos
        Checkbox mili_parte1 = new Checkbox("Milisegundos");
        panelTiempo_parte1.add(mili_parte1);
        mili_parte1.setState(true);

        // Checkbox para mostrar en nanosegundos / 10.000 para mayor comodidad
        Checkbox micro_parte1 = new Checkbox("Microsegundos");
        panelTiempo_parte1.add(micro_parte1);

        // Checkbox para mostrar en nanosegundos
        Checkbox nano_parte1 = new Checkbox("Nanosegundos");
        panelTiempo_parte1.add(nano_parte1);

        // Etiqueta tiempo de duracion de la ejecucion
        JLabel label_tiempoEjecucion_parte1 = new JLabel("Tiempo de ejecucion:");
        label_tiempoEjecucion_parte1.setFont(new Font("Tahoma", Font.PLAIN, 12));
        label_tiempoEjecucion_parte1.setBounds(201, 267, 150, 14);
        panel1.add(label_tiempoEjecucion_parte1);

        // Cuadro para mostrar el tiempo de duracion de la ejecucion
        tiempo_parte1 = new JTextField();
        tiempo_parte1.setEditable(false);
        tiempo_parte1.setBounds(289, 292, 116, 20);
        panel1.add(tiempo_parte1);
        tiempo_parte1.setColumns(10);

        // Etiqueta Solucion
        JLabel label_solucion_parte1 = new JLabel("Puntos solucion:");
        label_solucion_parte1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        label_solucion_parte1.setBounds(200, 382, 120, 14);
        panel1.add(label_solucion_parte1);

        // Etiqueta punto 1
        JLabel label_Punto1 = new JLabel("Punto 1:");
        label_Punto1.setFont(new Font("Tahoma", Font.PLAIN, 12));
        label_Punto1.setBounds(200, 409, 71, 14);
        panel1.add(label_Punto1);

        // Etiqueta punto 2
        JLabel label_Punto2 = new JLabel("Punto 2:");
        label_Punto2.setFont(new Font("Tahoma", Font.PLAIN, 12));
        label_Punto2.setBounds(200, 439, 71, 14);
        panel1.add(label_Punto2);

        // Etiqueta punto 3
        JLabel label_Punto3 = new JLabel("Punto 3:");
        label_Punto3.setFont(new Font("Tahoma", Font.PLAIN, 12));
        label_Punto3.setBounds(200, 468, 71, 14);
        panel1.add(label_Punto3);

        // Cuadro para mostrar las coordenadas del punto 1
        textPunto1 = new JTextField();
        textPunto1.setEditable(false);
        textPunto1.setBounds(263, 407, 145, 20);
        panel1.add(textPunto1);
        textPunto1.setColumns(10);

        // Cuadro para mostrar las coordenadas del punto 2
        textPunto2 = new JTextField();
        textPunto2.setEditable(false);
        textPunto2.setColumns(10);
        textPunto2.setBounds(263, 437, 145, 20);
        panel1.add(textPunto2);

        // Cuadro para mostrar las coordenadas del punto 3
        textPunto3 = new JTextField();
        textPunto3.setEditable(false);
        textPunto3.setColumns(10);
        textPunto3.setBounds(263, 467, 145, 20);
        panel1.add(textPunto3);

        // Etiqueta Representacion grafica
        JLabel label_grafica_parte1 = new JLabel("Representacion aproximada:");
        label_grafica_parte1.setFont(new Font("Tahoma", Font.PLAIN, 18));
        label_grafica_parte1.setBounds(535, 33, 282, 23);
        panel1.add(label_grafica_parte1);

        // Etiqueta distancia solucion
        JLabel label_distanciaSolucion_parte1 = new JLabel("Distancia solucion:");
        label_distanciaSolucion_parte1.setFont(new Font("Tahoma", Font.PLAIN, 12));
        label_distanciaSolucion_parte1.setBounds(201, 323, 150, 14);
        panel1.add(label_distanciaSolucion_parte1);

        // Cuadro para mostrar la distancia solucion
        textDistancia_parte1 = new JTextField();
        textDistancia_parte1.setEditable(false);
        textDistancia_parte1.setColumns(10);
        textDistancia_parte1.setBounds(289, 348, 116, 20);
        panel1.add(textDistancia_parte1);

        // JPanel para indicar que tipo de caso vamos a ejecutar
        JPanel panelCasos = new JPanel();
        FlowLayout flowLayout2 = (FlowLayout) panelCasos.getLayout();
        flowLayout2.setAlignment(FlowLayout.LEFT);
        panelCasos.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Mostrar caso:",
                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelCasos.setBounds(20, 99, 103, 116);
        panel1.add(panelCasos);
        panelCasos.setEnabled(false);

        Checkbox medio = new Checkbox("Medio");
        medio.setEnabled(false);
        medio.setState(true);
        panelCasos.add(medio);

        Checkbox peor = new Checkbox("Peor");
        peor.setEnabled(false);
        panelCasos.add(peor);

        Checkbox mejor = new Checkbox("Mejor");
        mejor.setEnabled(false);
        panelCasos.add(mejor);

        // Campo para mostrar la ubicacion del archivo elegido
        textRuta_parte1 = new JTextField();
        textRuta_parte1.setEnabled(false);
        textRuta_parte1.setText("Ruta del archivo");
        textRuta_parte1.setFont(new Font("Microsoft YaHei", Font.PLAIN, 11));
        textRuta_parte1.setToolTipText("Inserta la ruta del fichero");
        textRuta_parte1.setBounds(175, 54, 103, 23);
        panel1.add(textRuta_parte1);
        textRuta_parte1.setColumns(10);

        // Boton seleccionar archivo
        JButton btnBuscar_parte1 = new JButton("Buscar");
        btnBuscar_parte1.setEnabled(false);
        btnBuscar_parte1.setBounds(285, 55, 75, 23);
        panel1.add(btnBuscar_parte1);

        // RadioButtons principales
        JRadioButton rdbtnPuntosAleatorios_parte1 = new JRadioButton("Puntos aleatorios");
        rdbtnPuntosAleatorios_parte1.setFont(new Font("Tahoma", Font.PLAIN, 13));
        rdbtnPuntosAleatorios_parte1.setBounds(20, 17, 128, 23);
        panel1.add(rdbtnPuntosAleatorios_parte1);

        JRadioButton rdbtnPuntosDeArchivo_parte1 = new JRadioButton("Puntos de archivo TSP");
        rdbtnPuntosDeArchivo_parte1.setFont(new Font("Tahoma", Font.PLAIN, 13));
        rdbtnPuntosDeArchivo_parte1.setBounds(176, 17, 184, 23);
        panel1.add(rdbtnPuntosDeArchivo_parte1);

        JRadioButton rdbtnDirectorioLocal_partel = new JRadioButton("Directorio actual");
        rdbtnDirectorioLocal_partel.setBounds(382, 17, 118, 23);
        rdbtnDirectorioLocal_partel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        panel1.add(rdbtnDirectorioLocal_partel);

        // Cajas de adorno
        Box aleatoriosBox_parte1 = Box.createVerticalBox();
        aleatoriosBox_parte1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        aleatoriosBox_parte1.setBounds(10, 11, 150, 210);
        panel1.add(aleatoriosBox_parte1);

        Box archivoBox_parte1 = Box.createHorizontalBox();
        archivoBox_parte1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        archivoBox_parte1.setBounds(170, 11, 201, 80);
        panel1.add(archivoBox_parte1);

        // Etiqueta tecnica a utilizar
        JLabel label_tecnicaAUtilizar = new JLabel("Tecnica a utilizar:");
        label_tecnicaAUtilizar.setFont(new Font("Tahoma", Font.PLAIN, 12));
        label_tecnicaAUtilizar.setBounds(175, 150, 116, 14);
        panel1.add(label_tecnicaAUtilizar);

        // Lista para mostrar archivos .tsp del directorio local de ejecucion
        List list_parte1 = new List();
        list_parte1.setEnabled(false);
        list_parte1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        list_parte1.setBackground(SystemColor.menu);
        list_parte1.setForeground(Color.BLACK);
        list_parte1.setBounds(389, 45, 105, 111);
        panel1.add(list_parte1);

        // Caja de adorno
        Box localBox_parte1 = Box.createHorizontalBox();
        localBox_parte1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        localBox_parte1.setBounds(381, 11, 122, 152);
        panel1.add(localBox_parte1);

        // Tamaño de los puntos
        JSpinner spinner_parte1 = new JSpinner();
        spinner_parte1.setBounds(915, 24, 36, 20);
        panel1.add(spinner_parte1);
        spinner_parte1.setModel(model);

        // Centrar texto del spinner
        JComponent editor = spinner_parte1.getEditor();
        JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) editor;
        spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);

        // Etiqueta tamaño puntos
        JLabel label_tamañoPuntos_parte1 = new JLabel("Tamaño puntos:");
        label_tamañoPuntos_parte1.setBounds(815, 27, 94, 14);
        panel1.add(label_tamañoPuntos_parte1);

        // Defino la ruta del directorio actual de ejecucion
        String path = ".";
        File dir = new File(path);
        String[] files = dir.list();

        // Añado solo el nombre de los archivos que contengan la extension tsp
        for (String string : files) {
            String patternString = "tsp";
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(string);
            if (matcher.find()) {
                list_parte1.add(string);
            }
        }

        ////////////////////////////// PESTAñA 2 - Items /////////////////////////////
        // Panel principal de la pestaña 2
        JPanel panel2 = new JPanel();
        panel2.setLayout(null);
        tabbedPane.addTab("Parte 2 - Voraz", null, panel2, null);

        JPanel panelGrafica_parte2 = new JPanel();
        panelGrafica_parte2.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
        panelGrafica_parte2.setBounds(535, 67, 415, 415);
        panel2.add(panelGrafica_parte2);

        // Etiqueta numero de puntos
        JLabel label_numPuntos_parte2 = new JLabel("Nº de puntos:");
        label_numPuntos_parte2.setEnabled(false);
        label_numPuntos_parte2.setFont(new Font("Tahoma", Font.PLAIN, 12));
        label_numPuntos_parte2.setBounds(20, 59, 95, 14);
        panel2.add(label_numPuntos_parte2);

        // Etiqueta Titulo Resultados
        JLabel label_resultados_parte2 = new JLabel("RESULTADOS DE EJECUCION : ");
        label_resultados_parte2.setFont(new Font("Microsoft YaHei", Font.BOLD, 18));
        label_resultados_parte2.setBounds(10, 162, 282, 34);
        panel2.add(label_resultados_parte2);

        // Cuadro de texto para recoger la cantidad de puntos aleatorios
        textNumeroPuntos_parte2 = new JTextField();
        textNumeroPuntos_parte2.setEnabled(false);
        textNumeroPuntos_parte2.setText("15");
        textNumeroPuntos_parte2.setBounds(107, 57, 41, 20);
        panel2.add(textNumeroPuntos_parte2);
        textNumeroPuntos_parte2.setColumns(10);

        // Boton que nos muestra los puntos de la grafica
        JButton btnMostrarPuntos_parte2 = new JButton("Mostrar puntos");
        btnMostrarPuntos_parte2.setEnabled(false);
        btnMostrarPuntos_parte2.setBounds(20, 128, 134, 23);
        panel2.add(btnMostrarPuntos_parte2);

        // Boton para calcular el algoritmo voraz de Prim
        JButton btnPrim = new JButton("Prim");
        btnPrim.setEnabled(false);
        btnPrim.setBounds(180, 113, 85, 23);
        panel2.add(btnPrim);

        // Boton para calcular el algoritmo voraz de Kruskal
        JButton btnKruskal = new JButton("Kruskal");
        btnKruskal.setEnabled(false);
        btnKruskal.setBounds(284, 113, 85, 23);
        panel2.add(btnKruskal);

        // JPanel para indicar en que unidades mostramos el tiempo de ejecucion
        JPanel panelTiempo2 = new JPanel();
        FlowLayout flowLayout3 = (FlowLayout) panelTiempo2.getLayout();
        flowLayout3.setAlignment(FlowLayout.LEFT);
        panelTiempo2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Mostrar tiempo en:",
                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelTiempo2.setBounds(10, 201, 161, 116);
        panel2.add(panelTiempo2);

        // Checkbox para mostrar en milisegundos
        Checkbox mili_parte2 = new Checkbox("Milisegundos");
        panelTiempo2.add(mili_parte2);
        mili_parte2.setState(true);

        // Checkbox para mostrar en nanosegundos / 10.000 para mayor comodidad
        Checkbox micro_parte2 = new Checkbox("Microsegundos");
        panelTiempo2.add(micro_parte2);

        // Checkbox para mostrar en nanosegundos
        Checkbox nano_parte2 = new Checkbox("Nanosegundos");
        panelTiempo2.add(nano_parte2);

        // Etiqueta tiempo de duracion de la ejecucion
        JLabel label_tiempoEjecucion_parte2 = new JLabel("Tiempo de ejecucion:");
        label_tiempoEjecucion_parte2.setFont(new Font("Tahoma", Font.PLAIN, 12));
        label_tiempoEjecucion_parte2.setBounds(181, 213, 150, 14);
        panel2.add(label_tiempoEjecucion_parte2);

        // Cuadro para mostrar el tiempo de duracion de la ejecucion
        tiempo_parte2 = new JTextField();
        tiempo_parte2.setEditable(false);
        tiempo_parte2.setBounds(269, 238, 116, 20);
        panel2.add(tiempo_parte2);
        tiempo_parte2.setColumns(10);

        // Etiqueta Solucion
        JLabel label_solucion_parte2 = new JLabel("Camino de Puntos Solucion:");
        label_solucion_parte2.setFont(new Font("Tahoma", Font.PLAIN, 15));
        label_solucion_parte2.setBounds(10, 328, 187, 14);
        panel2.add(label_solucion_parte2);

        // Etiqueta Representacion grafica
        JLabel label_grafica_parte2 = new JLabel("Representacion aproximada:");
        label_grafica_parte2.setFont(new Font("Tahoma", Font.PLAIN, 18));
        label_grafica_parte2.setBounds(535, 33, 282, 23);
        panel2.add(label_grafica_parte2);

        // Etiqueta distancia solucion
        JLabel label_DistanciaSolucion_parte2 = new JLabel("Distancia solucion:");
        label_DistanciaSolucion_parte2.setFont(new Font("Tahoma", Font.PLAIN, 12));
        label_DistanciaSolucion_parte2.setBounds(181, 269, 150, 14);
        panel2.add(label_DistanciaSolucion_parte2);

        // Cuadro para mostrar la distancia solucion
        textDistancia_parte2 = new JTextField();
        textDistancia_parte2.setEditable(false);
        textDistancia_parte2.setColumns(10);
        textDistancia_parte2.setBounds(269, 294, 116, 20);
        panel2.add(textDistancia_parte2);

        // Campo para mostrar la ubicacion del archivo elegido
        textRuta_parte2 = new JTextField();
        textRuta_parte2.setEnabled(false);
        textRuta_parte2.setText("Ruta del archivo");
        textRuta_parte2.setFont(new Font("Microsoft YaHei", Font.PLAIN, 11));
        textRuta_parte2.setToolTipText("Inserta la ruta del fichero");
        textRuta_parte2.setBounds(179, 54, 95, 23);
        panel2.add(textRuta_parte2);
        textRuta_parte2.setColumns(10);

        // Boton seleccionar archivo
        JButton btnBuscar_parte2 = new JButton("Buscar");
        btnBuscar_parte2.setEnabled(false);
        btnBuscar_parte2.setBounds(284, 55, 82, 23);
        panel2.add(btnBuscar_parte2);

        // RadioButtons principales
        JRadioButton rdbtnPuntosAleatorios_parte2 = new JRadioButton("Puntos aleatorios");
        rdbtnPuntosAleatorios_parte2.setFont(new Font("Tahoma", Font.PLAIN, 13));
        rdbtnPuntosAleatorios_parte2.setBounds(20, 17, 128, 23);
        panel2.add(rdbtnPuntosAleatorios_parte2);

        JRadioButton rdbtnPuntosDeArchivo_parte2 = new JRadioButton("Puntos de archivo TSP");
        rdbtnPuntosDeArchivo_parte2.setFont(new Font("Tahoma", Font.PLAIN, 13));
        rdbtnPuntosDeArchivo_parte2.setBounds(175, 17, 191, 23);
        panel2.add(rdbtnPuntosDeArchivo_parte2);

        JRadioButton rdbtnDirectorioLocal_parte2 = new JRadioButton("Directorio actual");
        rdbtnDirectorioLocal_parte2.setBounds(389, 17, 118, 23);
        rdbtnDirectorioLocal_parte2.setFont(new Font("Tahoma", Font.PLAIN, 13));
        panel2.add(rdbtnDirectorioLocal_parte2);

        // Cajas de adorno
        Box aleatoriosBox_parte2 = Box.createVerticalBox();
        aleatoriosBox_parte2.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        aleatoriosBox_parte2.setBounds(10, 11, 147, 95);
        panel2.add(aleatoriosBox_parte2);

        Box archivoBox_parte2 = Box.createHorizontalBox();
        archivoBox_parte2.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        archivoBox_parte2.setBounds(170, 11, 206, 95);
        panel2.add(archivoBox_parte2);

        // Tamaño de los puntos
        JSpinner spinner_parte2 = new JSpinner();
        spinner_parte2.setBounds(915, 9, 36, 20);
        panel2.add(spinner_parte2);
        spinner_parte2.setModel(model);

        // Centrar texto del spinner
        JComponent editor2 = spinner_parte2.getEditor();
        JSpinner.DefaultEditor spinnerEditor2 = (JSpinner.DefaultEditor) editor2;
        spinnerEditor2.getTextField().setHorizontalAlignment(JTextField.CENTER);

        // Etiqueta tamaño puntos
        JLabel label_tamañoPuntos_parte2 = new JLabel("Tamaño puntos:");
        label_tamañoPuntos_parte2.setBounds(815, 12, 94, 14);
        panel2.add(label_tamañoPuntos_parte2);

        // Cuadro para representar los puntos del camino solucion
        textArea = new TextArea();
        textArea.setBounds(205, 328, 247, 154);
        panel2.add(textArea);

        // Lista para mostrar archivos .tsp del directorio local de ejecucion
        List list_parte2 = new List();
        list_parte2.setEnabled(false);
        list_parte2.setFont(new Font("Tahoma", Font.PLAIN, 14));
        list_parte2.setBackground(SystemColor.menu);
        list_parte2.setForeground(Color.BLACK);
        list_parte2.setBounds(396, 45, 105, 111);
        panel2.add(list_parte2);

        // Caja de adorno
        Box localBox_parte2 = Box.createHorizontalBox();
        localBox_parte2.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        localBox_parte2.setBounds(387, 11, 122, 152);
        panel2.add(localBox_parte2);

        // Checkbox para mostrar la solucion secuencialmente
        JCheckBox solucionSecuencial = new JCheckBox("Solucion secuencial");
        solucionSecuencial.setEnabled(false);
        solucionSecuencial.setBounds(200, 140, 181, 23);
        panel2.add(solucionSecuencial);

        // Etiqueta Velocidad
        JLabel label_velocidad = new JLabel("Velocidad (ms):");
        label_velocidad.setEnabled(false);
        label_velocidad.setBounds(813, 40, 90, 14);
        panel2.add(label_velocidad);

        // JText para introducir la velocidad en ms de la representacion secuencial
        textVelocidad = new JTextField();
        textVelocidad.setEnabled(false);
        textVelocidad.setText("400");
        textVelocidad.setBounds(909, 37, 43, 20);
        panel2.add(textVelocidad);
        textVelocidad.setColumns(10);
        textVelocidad.setHorizontalAlignment(JTextField.CENTER);

        // Boton para exportar fichero con la solucion
        JButton btnFichero = new JButton("Exportar a fichero");
        btnFichero.setBounds(10, 386, 161, 23);
        btnFichero.setEnabled(false);
        panel2.add(btnFichero);

        // Añado solo el nombre de los archivos que contengan la extension tsp
        for (String string : files) {
            String patternString = "tsp";
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(string);
            if (matcher.find()) {
                list_parte2.add(string);
            }
        }

        /////////////////////// ACCIONES PESTAñA 1 /////////////////////////////////////
        // Funciones de los checkboxs relativos al tiempo de ejecucion
        nano_parte1.addItemListener((ItemEvent e) -> {
            if (nano_parte1.getState()) {
                mili_parte1.setState(false);
                micro_parte1.setState(false);
                temp = "nano";
            }
        });

        mili_parte1.addItemListener((ItemEvent e) -> {
            if (mili_parte1.getState()) {
                nano_parte1.setState(false);
                micro_parte1.setState(false);
                temp = "mili";
            }
        });

        micro_parte1.addItemListener((ItemEvent e) -> {
            if (micro_parte1.getState()) {
                nano_parte1.setState(false);
                mili_parte1.setState(false);
                temp = "micro";
            }
        });

        // Funciones de los checkboxs relativos al tipo de caso
        medio.addItemListener((ItemEvent e) -> {
            if (medio.getState()) {
                peor.setState(false);
                mejor.setState(false);
            }
        });

        peor.addItemListener((ItemEvent e) -> {
            if (peor.getState()) {
                medio.setState(false);
                mejor.setState(false);
            }
        });

        mejor.addItemListener((ItemEvent e) -> {
            if (mejor.getState()) {
                medio.setState(false);
                peor.setState(false);
            }
        });

        // Funcion para elegir el archivo del directorio local de ejecucion
        list_parte1.addItemListener((ItemEvent e) -> {
            fichero = new File(list_parte1.getSelectedItem());
            btnMostrarPuntos_parte1.setEnabled(true);
            btnExhaustiva.setEnabled(false);
            btnDyV.setEnabled(false);
        });

        // Funciones de los radioButtons
        rdbtnPuntosAleatorios_parte1.addActionListener((ActionEvent e) -> {
            if (!rdbtnPuntosAleatorios_parte1.isSelected()) {
                rdbtnPuntosAleatorios_parte1.setSelected(true);
            }
            if (rdbtnPuntosAleatorios_parte1.isSelected()) {
                getGraphics().clearRect(557, 180, 412, 412);
                rdbtnPuntosDeArchivo_parte1.setSelected(false);
                rdbtnDirectorioLocal_partel.setSelected(false);
                textRuta_parte1.setEnabled(false);
                btnBuscar_parte1.setEnabled(false);
                label_numPuntos_parte1.setEnabled(true);
                textNumeroPuntos_parte1.setEnabled(true);
                panelCasos.setEnabled(true);
                medio.setEnabled(true);
                mejor.setEnabled(true);
                peor.setEnabled(true);
                btnMostrarPuntos_parte1.setEnabled(true);
                btnExhaustiva.setEnabled(false);
                btnDyV.setEnabled(false);
                list_parte1.setEnabled(false);
                ciudad = 0;
                vaciarTextsFields();
            }
        });

        rdbtnPuntosDeArchivo_parte1.addActionListener((ActionEvent e) -> {
            if (!rdbtnPuntosDeArchivo_parte1.isSelected()) {
                rdbtnPuntosDeArchivo_parte1.setSelected(true);
            }
            if (rdbtnPuntosDeArchivo_parte1.isSelected()) {
                getGraphics().clearRect(557, 180, 412, 412);
                rdbtnPuntosAleatorios_parte1.setSelected(false);
                rdbtnDirectorioLocal_partel.setSelected(false);
                textRuta_parte1.setEnabled(true);
                btnBuscar_parte1.setEnabled(true);
                label_numPuntos_parte1.setEnabled(false);
                textNumeroPuntos_parte1.setEnabled(false);
                panelCasos.setEnabled(false);
                medio.setEnabled(false);
                mejor.setEnabled(false);
                peor.setEnabled(false);
                btnMostrarPuntos_parte1.setEnabled(false);
                btnExhaustiva.setEnabled(false);
                btnDyV.setEnabled(false);
                list_parte1.setEnabled(false);
                ciudad = 1;
                vaciarTextsFields();
            }
        });

        rdbtnDirectorioLocal_partel.addActionListener((ActionEvent e) -> {
            if (!rdbtnDirectorioLocal_partel.isSelected()) {
                rdbtnDirectorioLocal_partel.setSelected(true);
            }
            if (rdbtnDirectorioLocal_partel.isSelected()) {
                getGraphics().clearRect(557, 180, 412, 412);
                rdbtnPuntosAleatorios_parte1.setSelected(false);
                rdbtnPuntosDeArchivo_parte1.setSelected(false);
                label_numPuntos_parte1.setEnabled(false);
                textNumeroPuntos_parte1.setEnabled(false);
                panelCasos.setEnabled(false);
                medio.setEnabled(false);
                mejor.setEnabled(false);
                peor.setEnabled(false);
                btnMostrarPuntos_parte1.setEnabled(false);
                btnExhaustiva.setEnabled(false);
                btnDyV.setEnabled(false);
                textRuta_parte1.setEnabled(false);
                btnBuscar_parte1.setEnabled(false);
                ciudad = 1;
                list_parte1.setEnabled(true);
                vaciarTextsFields();
            }
        });

        // Funcion del boton mostrar
        btnMostrarPuntos_parte1.addActionListener((ActionEvent e) -> {
            trioPuntos = new DyV();
            if (rdbtnPuntosAleatorios_parte1.isSelected()) { // Comprobamos si se trata de aleatorios
                if (Integer.parseInt(textNumeroPuntos_parte1.getText()) <= 3) {
                    textNumeroPuntos_parte1.setText("3");
                }
                numPuntos = Integer.parseInt(textNumeroPuntos_parte1.getText());
                aux = trioPuntos.getGrafica();

                int s; // Comprobamos el tipo de caso que queremos generar
                if (peor.getState()) {
                    s = 0;
                } else if (mejor.getState()) {
                    s = 1;
                } else {
                    s = 2;
                }
                aux.setNumPuntos(numPuntos, s); // Indicamos el numero de puntos que hemos introducido

            } else { // Tenemos que leer fichero que le hemos indicado a la aplicacion
                ciudadesMain = new ArrayList<>();
                ciudadesMain = trioPuntos.leerFichero(fichero);
                aux = trioPuntos.getGrafica();
                aux.setPuntos(ciudadesMain);
            }
            getGraphics().clearRect(557, 180, 412, 412); // Limpiamos zona grafica
            trioPuntos.setGrafica(aux); // Asignamos la grafica que hemos generado (Bien aleatoria o bien de
            // archivo)
            tamañoPuntos = (int) spinner_parte1.getValue();
            trioPuntos.pintarPuntosGrafica((Graphics2D) getGraphics(), tamañoPuntos); // Dibujamos los puntos
            btnExhaustiva.setEnabled(true);
            btnDyV.setEnabled(true);
            vaciarTextsFields(); // Vaciamos los campos de texto para la nueva ejecucion
        });

        // Funcion busqueda dyv
        btnDyV.addActionListener((ActionEvent e) -> {
            tinicio = currentTime(); // Guardo el tiempo actual
            trioPuntos.pinta((Graphics2D) getGraphics(), 0); // Llamando al metodo pinta, generamos la solucion dyv
            // y la pintamos en la grafica
            tfin = currentTime(); // Guardo el tiempo actual en milisegundos
            tiempo = (tfin - tinicio); // Resto para conseguir tiempo de ejecucion
            tiempo_parte1.setText(" " + String.valueOf(tiempo)); // Asigno el tiempo transcurrido a su etiqueta
            rellenarTextsfieldsSolucion(trioPuntos.getA(), trioPuntos.getB(), trioPuntos.getC()); // Mostramos la
            // solucion
            btnExhaustiva.setEnabled(true);
        });

        // Funcion busqueda exhaustiva
        btnExhaustiva.addActionListener((ActionEvent e) -> {
            btnDyV.setEnabled(false);
            getGraphics().clearRect(557, 180, 412, 412); // Limpiamos la grafica
            trioPuntos.setGrafica(aux); // Asignamos la grafica con la que vamos a trabajar
            tamañoPuntos = (int) spinner_parte1.getValue();
            trioPuntos.pintarPuntosGrafica((Graphics2D) getGraphics(), tamañoPuntos); // Pintamos la solucion
            tinicio = currentTime(); // Guardo el tiempo actual
            trioPuntos.pinta((Graphics2D) getGraphics(), 1);
            tfin = currentTime(); // Guardo el tiempo actual en milisegundos
            tiempo = (tfin - tinicio); // Resto para conseguir tiempo de ejecucion
            tiempo_parte1.setText(" " + String.valueOf(tiempo)); // Asigno el tiempo transcurrido a su etiqueta
            rellenarTextsfieldsSolucion(trioPuntos.getA(), trioPuntos.getB(), trioPuntos.getC()); // Mostramos la
            // solucion
            btnDyV.setEnabled(true);
        });

        // Funciones del boton Buscar
        btnBuscar_parte1.addActionListener((ActionEvent e) -> {
            JFileChooser fc = new JFileChooser(); // Creamos el objeto JFileChooser
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("*.TSP", "tsp"); // Filtro solo tsp
            fc.setFileFilter(filtro); // Le paso el filtro al FileChooser
            fc.setCurrentDirectory(new File(System.getProperty("user.dir")));

            int seleccion = fc.showOpenDialog(panelBase); // Guardo la opcion seleccionada

            btnExhaustiva.setEnabled(false);
            btnDyV.setEnabled(false);

            if (seleccion == JFileChooser.APPROVE_OPTION) { // Si el usuario pincha en aceptar
                fichero = fc.getSelectedFile(); // Seleccionamos el fichero
                textRuta_parte1.setText(fichero.getAbsolutePath()); // Ecribe la ruta del fichero seleccionado en el
                // campo de texto
                btnMostrarPuntos_parte1.setEnabled(true);
                getGraphics().clearRect(557, 180, 412, 412); // Limpiamos zona grafica
                vaciarTextsFields();
            }
        });

        /////////////////////// ACCIONES PESTAñA 2 /////////////////////////////////////
        // Funciones de los checkboxs relativos al tiempo de ejecucion
        nano_parte2.addItemListener((ItemEvent e) -> {
            if (nano_parte2.getState()) {
                mili_parte2.setState(false);
                micro_parte2.setState(false);
                temp = "nano";
            }
        });

        mili_parte2.addItemListener((ItemEvent e) -> {
            if (mili_parte2.getState()) {
                nano_parte2.setState(false);
                micro_parte2.setState(false);
                temp = "mili";
            }
        });

        micro_parte2.addItemListener((ItemEvent e) -> {
            if (micro_parte2.getState()) {
                nano_parte2.setState(false);
                mili_parte2.setState(false);
                temp = "micro";
            }
        });

        solucionSecuencial.addItemListener((ItemEvent e) -> {
            if (solucionSecuencial.isSelected()) {
                label_velocidad.setEnabled(true);
                textVelocidad.setEnabled(true);
            } else {
                label_velocidad.setEnabled(false);
                textVelocidad.setEnabled(false);
            }
        });

        // Funcion para elegir el archivo del directorio local de ejecucion
        list_parte2.addItemListener((ItemEvent e) -> {
            fichero = new File(list_parte2.getSelectedItem());
            btnMostrarPuntos_parte2.setEnabled(true);
            btnPrim.setEnabled(false);
            btnKruskal.setEnabled(false);
            btnFichero.setEnabled(false);
        });

        // Funcion para elegir el directorio al que exportar el fichero solucion
        btnFichero.addActionListener((ActionEvent arg0) -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setCurrentDirectory(new File(path));
            fc.showOpenDialog(panelBase);
            voraces.guardarFichero(voraces.getUsados(), voraces.getCiudades(), fc.getSelectedFile(),
                    algoritmo);
            JOptionPane.showMessageDialog(null,
                    "Archivo guardado correctamente en: " + fc.getSelectedFile() + "\\");
        });

        // Funciones de los radioButtons
        rdbtnPuntosAleatorios_parte2.addActionListener((ActionEvent e) -> {
            if (!rdbtnPuntosAleatorios_parte2.isSelected()) {
                rdbtnPuntosAleatorios_parte2.setSelected(true);
            }
            if (rdbtnPuntosAleatorios_parte2.isSelected()) {
                getGraphics().clearRect(557, 180, 412, 412);
                rdbtnPuntosDeArchivo_parte2.setSelected(false);
                rdbtnDirectorioLocal_parte2.setSelected(false);
                textRuta_parte2.setEnabled(false);
                btnBuscar_parte2.setEnabled(false);
                label_numPuntos_parte2.setEnabled(true);
                textNumeroPuntos_parte2.setEnabled(true);
                btnMostrarPuntos_parte2.setEnabled(true);
                btnPrim.setEnabled(false);
                btnKruskal.setEnabled(false);
                list_parte2.setEnabled(false);
                solucionSecuencial.setEnabled(false);
                solucionSecuencial.setSelected(false);
                btnFichero.setEnabled(false);
                ciudad = 0;
                vaciarTextsFields();
            }
        });

        rdbtnPuntosDeArchivo_parte2.addActionListener((ActionEvent e) -> {
            if (!rdbtnPuntosDeArchivo_parte2.isSelected()) {
                rdbtnPuntosDeArchivo_parte2.setSelected(true);
            }
            if (rdbtnPuntosDeArchivo_parte2.isSelected()) {
                getGraphics().clearRect(557, 180, 412, 412);
                rdbtnPuntosAleatorios_parte2.setSelected(false);
                rdbtnDirectorioLocal_parte2.setSelected(false);
                textRuta_parte2.setEnabled(true);
                btnBuscar_parte2.setEnabled(true);
                label_numPuntos_parte2.setEnabled(false);
                textNumeroPuntos_parte2.setEnabled(false);
                btnMostrarPuntos_parte2.setEnabled(false);
                btnPrim.setEnabled(false);
                btnKruskal.setEnabled(false);
                list_parte2.setEnabled(false);
                solucionSecuencial.setEnabled(false);
                solucionSecuencial.setSelected(false);
                btnFichero.setEnabled(false);
                ciudad = 1;
                vaciarTextsFields();
            }
        });

        rdbtnDirectorioLocal_parte2.addActionListener((ActionEvent e) -> {
            if (!rdbtnDirectorioLocal_parte2.isSelected()) {
                rdbtnDirectorioLocal_parte2.setSelected(true);
            }
            if (rdbtnDirectorioLocal_parte2.isSelected()) {
                getGraphics().clearRect(557, 180, 412, 412);
                rdbtnPuntosAleatorios_parte2.setSelected(false);
                rdbtnPuntosDeArchivo_parte2.setSelected(false);
                label_numPuntos_parte2.setEnabled(false);
                textNumeroPuntos_parte2.setEnabled(false);
                btnMostrarPuntos_parte2.setEnabled(false);
                btnPrim.setEnabled(false);
                btnKruskal.setEnabled(false);
                textRuta_parte2.setEnabled(false);
                btnBuscar_parte2.setEnabled(false);
                ciudad = 1;
                list_parte2.setEnabled(true);
                solucionSecuencial.setEnabled(false);
                solucionSecuencial.setSelected(false);
                btnFichero.setEnabled(false);
                vaciarTextsFields();
            }
        });

        // Funcion del boton mostrar
        btnMostrarPuntos_parte2.addActionListener((ActionEvent e) -> {
            voraces = new Voraces();
            if (rdbtnPuntosAleatorios_parte2.isSelected()) { // Comprobamos si se trata de aleatorios
                if (Integer.parseInt(textNumeroPuntos_parte2.getText()) <= 1) {
                    textNumeroPuntos_parte2.setText("1");
                }
                numPuntos = Integer.parseInt(textNumeroPuntos_parte2.getText());
                aux = voraces.getGrafica();
                aux.setNumPuntos(numPuntos, 2); // Indicamos el numero de puntos que hemos introducido

            } else { // Tenemos que leer fichero que le hemos indicado a la aplicacion
                ciudadesMain = new ArrayList<>();
                ciudadesMain = voraces.leerFichero(fichero);
                aux = voraces.getGrafica();
                aux.setPuntos(ciudadesMain);
            }
            tamañoPuntos = (int) spinner_parte2.getValue();
            getGraphics().clearRect(557, 180, 412, 412); // Limpiamos zona grafica
            voraces.setGrafica(aux); // Asignamos la grafica que hemos generado (Bien aleatoria o bien de archivo)
            voraces.pintarPuntosGrafica((Graphics2D) getGraphics(), tamañoPuntos); // Dibujamos los puntos
            btnPrim.setEnabled(true);
            btnKruskal.setEnabled(true);
            solucionSecuencial.setEnabled(true);
            btnFichero.setEnabled(false);
            vaciarTextsFields(); // Vaciamos los campos de texto para la nueva ejecucion
        });

        // Funciones del boton Buscar
        btnBuscar_parte2.addActionListener((ActionEvent e) -> {
            JFileChooser fc = new JFileChooser(); // Creamos el objeto JFileChooser
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("*.TSP", "tsp"); // Filtro solo tsp
            fc.setFileFilter(filtro); // Le paso el filtro al FileChooser
            fc.setCurrentDirectory(new File(System.getProperty("user.dir")));

            int seleccion = fc.showOpenDialog(panelBase); // Guardo la opcion seleccionada

            btnExhaustiva.setEnabled(false);
            btnDyV.setEnabled(false);

            if (seleccion == JFileChooser.APPROVE_OPTION) { // Si el usuario, pincha en aceptar
                fichero = fc.getSelectedFile(); // Seleccionamos el fichero
                textRuta_parte2.setText(fichero.getAbsolutePath()); // Ecribe la ruta del fichero seleccionado en el
                // campo de texto
                btnMostrarPuntos_parte2.setEnabled(true);
                btnFichero.setEnabled(false);
                btnKruskal.setEnabled(false);
                btnPrim.setEnabled(false);
                getGraphics().clearRect(557, 180, 412, 412); // Limpiamos zona grafica
                vaciarTextsFields(); // Vaciamos los campos de texto para la nueva ejecucion
            }
        });

        btnPrim.addActionListener((ActionEvent e) -> {
            int tipo; // Compruebo si generamos numeros aleatorios o de un archivo .tsp
            boolean secuencial;
            if (rdbtnPuntosAleatorios_parte2.isSelected()) {
                tipo = 0;
            } else {
                tipo = 1;
            }

            secuencial = solucionSecuencial.isSelected();
            tinicio = currentTime(); // Guardo el tiempo actual
            voraces.prim((Graphics2D) getGraphics(), tipo, secuencial, Integer.parseInt(textVelocidad.getText()));
            // de puntos (aleatorios o de archivo)
            tfin = currentTime(); // Guardo el tiempo actual en milisegundos
            tiempo = (tfin - tinicio); // Resto para conseguir tiempo de ejecucion
            tiempo_parte2.setText(String.valueOf(tiempo)); // Asigno el tiempo transcurrido a su etiqueta
            textDistancia_parte2.setText(df.format(voraces.getDistancia())); // Asigno la distancia recorrida

            // Con esta condicional consigo mostrar bien los puntos en el textArea
            // (aleatorios o de archivo)
            if (rdbtnPuntosDeArchivo_parte2.isSelected() || rdbtnDirectorioLocal_parte2.isSelected()) {
                for (Arista aris : voraces.getSolucion()) {
                    textArea.append(voraces.aplicarFactorEscala(aris.getA()).toString(0) + " ---> "
                            + voraces.aplicarFactorEscala(aris.getB()).toString(0) + "\n");
                }
            } else {
                for (Arista aris : voraces.getSolucion()) { // Aqui simplemente resto los margenes del eje X y eje Y
                    // para mostrar la solucion de manera que podamos ver mejor
                    // donde se encuentran los puntos en la grafica
                    textArea.append(aplicarFactorEscalaStringAleatorio(aris.getA()) + " ---> "
                            + aplicarFactorEscalaStringAleatorio(aris.getB()) + "\n");
                }
            }
            btnKruskal.setEnabled(false);
            if (rdbtnPuntosDeArchivo_parte2.isSelected() || rdbtnDirectorioLocal_parte2.isSelected()) {
                btnFichero.setEnabled(true);
                algoritmo = "Prim";
            }
        });

        btnKruskal.addActionListener((ActionEvent e) -> {
            int tipo; // Compruebo si generamos numeros aleatorios o de un archivo .tsp
            boolean secuencial;
            if (rdbtnPuntosAleatorios_parte2.isSelected()) {
                tipo = 0;
            } else {
                tipo = 1;
            }
            secuencial = solucionSecuencial.isSelected();

            tinicio = currentTime(); // Guardo el tiempo actual
            voraces.kruskal((Graphics2D) getGraphics(), tipo, secuencial,
                    Integer.parseInt(textVelocidad.getText())); // Pinto la solucion pasandole como parametro el
            // tipo
            // de puntos (aleatorios o de archivo)
            tfin = currentTime(); // Guardo el tiempo actual en milisegundos
            tiempo = (tfin - tinicio); // Resto para conseguir tiempo de ejecucion
            tiempo_parte2.setText(String.valueOf(tiempo)); // Asigno el tiempo transcurrido a su etiqueta
            textDistancia_parte2.setText(df.format(voraces.getDistancia())); // Asigno la distancia recorrida

            // Con esta condicional consigo mostrar bien los puntos en el textArea
            // (aleatorios o de archivo)
            if (rdbtnPuntosDeArchivo_parte2.isSelected() || rdbtnDirectorioLocal_parte2.isSelected()) {
                for (Arista aris : voraces.getSolucion()) {
                    textArea.append(voraces.aplicarFactorEscala(aris.getA()).toString(0) + " ---> "
                            + voraces.aplicarFactorEscala(aris.getB()).toString(0) + "\n");
                }
            } else {
                for (Arista aris : voraces.getSolucion()) { // Aqui simplemente resto los margenes del eje X y eje Y
                    // para mostrar la solucion de manera que podamos ver mejor
                    // donde se encuentran los puntos en la grafica
                    textArea.append(aplicarFactorEscalaStringAleatorio(aris.getA()) + " ---> "
                            + aplicarFactorEscalaStringAleatorio(aris.getB()) + "\n");
                }
            }
            btnPrim.setEnabled(false);
            if (rdbtnPuntosDeArchivo_parte2.isSelected() || rdbtnDirectorioLocal_parte2.isSelected()) {
                btnFichero.setEnabled(true);
                algoritmo = "Kruskal";
            }
        });
    }

    // Metodos auxiliares
    protected void vaciarTextsFields() {
        textPunto1.setText("");
        textPunto2.setText("");
        textPunto3.setText("");
        tiempo_parte1.setText("");
        tiempo_parte2.setText("");
        textDistancia_parte1.setText("");
        textDistancia_parte2.setText("");
        textArea.setText("");

    }

    public void rellenarTextsfieldsSolucion(Punto a, Punto b, Punto c) {
        if (ciudad == 0) {
            // Le resto la distancia que hay en el eje de la X y de la Y para mostrar la
            // solucion de manera que podamos ver mejor donde se encuentran los puntos en la
            // grafica
            textPunto1.setText(aplicarFactorEscalaStringAleatorio(a));
            textPunto2.setText(aplicarFactorEscalaStringAleatorio(b));
            textPunto3.setText(aplicarFactorEscalaStringAleatorio(c));
            textDistancia_parte1.setText("  " + df.format(trioPuntos.calcularDistanciaPuntos(a, b, c)));
        } else {
            // De este modo consigo el punto del fichero original de ciudades (y no el que
            // genero en la grafica que tiene modificadas sus coordenadas para que se pueda
            // visualizar dentro de los limites de la zona grafica)
            Punto A = aplicarFactorEscala(a);
            Punto B = aplicarFactorEscala(b);
            Punto C = aplicarFactorEscala(c);

            textPunto1.setText(aplicarFactorEscalaStringFichero(A));
            textPunto2.setText(aplicarFactorEscalaStringFichero(B));
            textPunto3.setText(aplicarFactorEscalaStringFichero(C));

            textDistancia_parte1.setText("  " + df.format(trioPuntos.calcularDistanciaPuntos(A, B, C)));
        }
    }

    public Punto aplicarFactorEscala(Punto p) {
        return new Punto((p.getX() - 557) * trioPuntos.getX() + 544, (p.getY() - 180) * trioPuntos.getY() + 100);
    }

    public String aplicarFactorEscalaStringAleatorio(Punto p) {
        return " [" + dfCorto.format(p.getX() - 557) + "  ,  " + dfCorto.format(p.getY() - 180) + "]";
    }

    public String aplicarFactorEscalaStringFichero(Punto p) {
        return "  [" + dfCorto.format(p.getX() - 544) + "  ,  " + dfCorto.format(p.getY() - 100) + "]";
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    /**
     * Metodo para obtener el tiempo actual en milisegundos, nanosegundos o
     * nanosegundos / 10.000
     *
     * @return
     */
    public long currentTime() {
        if ("mili".equals(temp)) {
            return System.currentTimeMillis();
        } else if ("nano".equals(temp)) {
            return System.nanoTime();
        }
        return System.nanoTime() / 10000;
    }
}
