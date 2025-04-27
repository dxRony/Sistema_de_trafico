package com.mycompany.sistema_de_trafico.flow;

import com.mycompany.sistema_de_trafico.edd.AVLTree;
import com.mycompany.sistema_de_trafico.edd.HashTable;
import com.mycompany.sistema_de_trafico.edd.LinkedList;
import com.mycompany.sistema_de_trafico.edd.Node;
import com.mycompany.sistema_de_trafico.edd.OrthogonalMatrix;
import com.mycompany.sistema_de_trafico.edd.PriorityQueue;
import com.mycompany.sistema_de_trafico.edd.Stack;
import com.mycompany.sistema_de_trafico.enums.TipoInterseccion;
import com.mycompany.sistema_de_trafico.objects.Interseccion;
import com.mycompany.sistema_de_trafico.objects.Vehiculo;

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

    public Simulador(LinkedList<Vehiculo> vehiculosCargados, int anchoCiudad, int altoCiudad, int tamañoTablaHash) {
        this.columnas = anchoCiudad;
        this.filas = altoCiudad;
        tablaVehiculos = new HashTable(tamañoTablaHash);
        ciudad = new OrthogonalMatrix<>(anchoCiudad, altoCiudad);
        arbolIntersecciones = new AVLTree();
        registroEventos = new Stack<>();

        generarMapaCiudad();
        System.out.println("Mostrando el mapa de la ciudad:");
        ciudad.imprimir();

        System.out.println("Repartiendo a los vehiculos en la ciudad...");
        repartirVehiculosCiudad(vehiculosCargados);

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

    private void repartirVehiculosCiudad(LinkedList<Vehiculo> listaVehiculos) {
        if (listaVehiculos == null || listaVehiculos.getSize() == 0) {
            System.out.println("No hay vehiculos para repartir");
            return;
        }
        int vehiculosRepartidos = 0;
        int vehiculosPerdidos = 0;

        while (!listaVehiculos.isEmpty()) {
            Node<Vehiculo> nodoVehiculo = listaVehiculos.getHead();
            Vehiculo vehiculo = nodoVehiculo.getData();

            Interseccion interseccionOrigen = obtenerInterseccion(vehiculo.getInterseccionOrigen());
            Interseccion interseccionDestino = obtenerInterseccion(vehiculo.getInterseccionDestino());

            if (interseccionOrigen != null && interseccionDestino != null) {
                PriorityQueue cola = obtenerCola(interseccionOrigen);

                if (cola != null) {
                    cola.insertar(vehiculo);
                    vehiculosRepartidos++;
                    tablaVehiculos.insertar(vehiculo);
                }

            } else {
                System.out.println("Vehiculo con placa: " + vehiculo.getPlaca() +
                        ", tiene un origen o destino inexistente");
                vehiculosPerdidos++;
            }
            listaVehiculos.remove(vehiculo);
        }
        System.out.println("Se repartieron: " + vehiculosRepartidos + ".\nSe perdieron: " + vehiculosPerdidos);
    }

    private Interseccion obtenerInterseccion(String nombreInterseccion) {
        if (nombreInterseccion == null) {
            return null;
        }

        char letraDeFila = nombreInterseccion.charAt(0);
        int numeroDeFila = letraDeFila - 'A';

        int numeroDeColumna = Integer.parseInt(nombreInterseccion.substring(1)) - 1;

        if (numeroDeFila >= 0 && numeroDeFila < filas && numeroDeColumna >= 0 && numeroDeColumna < columnas) {
            Node<Interseccion> nodoInterseccion = ciudad.obtenerNodo(numeroDeFila, numeroDeColumna);
            if (nodoInterseccion != null) {
                return nodoInterseccion.getData();
            } else {
                return null;
            }
        } else {
            // excediendo limites del tablero -> nodo inexistente
            return null;
        }

    }

    private PriorityQueue obtenerCola(Interseccion interseccion) {

        if (interseccion.getColaNorte() != null) {
            return interseccion.getColaNorte();
        } else if (interseccion.getColaSur() != null) {
            return interseccion.getColaSur();
        } else if (interseccion.getColaEste() != null) {
            return interseccion.getColaEste();
        } else if (interseccion.getColaOeste() != null) {
            return interseccion.getColaOeste();
        }
        return null;
    }
}
