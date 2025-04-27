package com.mycompany.sistema_de_trafico.flow;

import com.mycompany.sistema_de_trafico.edd.AVLTree;
import com.mycompany.sistema_de_trafico.edd.HashTable;
import com.mycompany.sistema_de_trafico.edd.OrthogonalMatrix;
import com.mycompany.sistema_de_trafico.edd.Stack;
import com.mycompany.sistema_de_trafico.enums.TipoInterseccion;
import com.mycompany.sistema_de_trafico.objects.Interseccion;

public class Simulador {

    private int columnas;
    private int filas;
    // tablero de la ciudad
    private OrthogonalMatrix<Interseccion> ciudad;
    // arbol para buscar la ruta mas compleja
    private AVLTree arbolIntersecciones;
    // pila donde se guardaran los eventos
    private Stack<String> registroEventos;
    // tabla donde se almacenan los vehiculos
    private HashTable tablaVehiculos;

    public Simulador(HashTable tablaVehiculos, int ancho, int alto) {
        this.columnas = ancho;
        this.filas = alto;
        this.tablaVehiculos = tablaVehiculos;
        ciudad = new OrthogonalMatrix<>(ancho, alto);
        arbolIntersecciones = new AVLTree();
        registroEventos = new Stack<>();

        generarMapaCiudad();
        ciudad.imprimir();
    }

    public void iniciarSimulador() {

    }

    private void generarMapaCiudad() {
        if (filas > 26) {
            System.out.println("El numero de filas no debe ser mayor a 26");
            return;
        }

        char[] letrasFilas = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        for (int y = 0; y < filas; y++) {
            char letraDeFila = letrasFilas[y];

            for (int x = 0; x < columnas; x++) {
                TipoInterseccion tipoInterseccion;
                String nombreInterseccion = letraDeFila + String.valueOf(x + 1);

                if (x == 0 && y == 0) {
                    // tipoInterseccion en esquina superior izquierda
                    tipoInterseccion = TipoInterseccion.CRUCELVOLTEADAIZQUIERDA;
                } else if (x == columnas - 1 && y == 0) {
                    // tipoInterseccion en esquina superior derecha
                    tipoInterseccion = TipoInterseccion.CRUCELOPUESTA;
                } else if (x == 0 && y == filas - 1) {
                    // tipoInterseccion en esquina inferior izquierda
                    tipoInterseccion = TipoInterseccion.CRUCEL;
                } else if (x == columnas - 1 && y == filas - 1) {
                    // tipoInterseccion en esquina inferior derecha
                    tipoInterseccion = TipoInterseccion.CRUCELVOLTEADADERECHA;
                } else if (x == 0) {
                    // tipoInterseccion en columna afuera izquierda
                    tipoInterseccion = TipoInterseccion.CRUCETVOLTEADAIZQUIERDA;
                } else if (x == columnas - 1) {
                    // tipoInterseccion en columna afuera derecha
                    tipoInterseccion = TipoInterseccion.CRUCETVOLTEADADERECHA;
                } else if (y == 0) {
                    // tipoInterseccion en fila arriba
                    tipoInterseccion = TipoInterseccion.CRUCET;
                } else if (y == filas - 1) {
                    // tipoInterseccion en fila abajo
                    tipoInterseccion = TipoInterseccion.CRUCETOPUESTA;
                } else {
                    // tipoInterseccion en centro de matriz
                    tipoInterseccion = TipoInterseccion.CRUCEMAS;
                }

                Interseccion interseccion = new Interseccion(nombreInterseccion, tipoInterseccion);
                ciudad.insertarDato(x, y, interseccion);
            }
        }
    }

}
