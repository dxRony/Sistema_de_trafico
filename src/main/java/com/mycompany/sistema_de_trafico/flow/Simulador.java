package com.mycompany.sistema_de_trafico.flow;

import java.util.Scanner;

import com.mycompany.sistema_de_trafico.edd.HashTable;
import com.mycompany.sistema_de_trafico.edd.LinkedList;
import com.mycompany.sistema_de_trafico.edd.Node;
import com.mycompany.sistema_de_trafico.edd.OrthogonalMatrix;
import com.mycompany.sistema_de_trafico.edd.PriorityQueueI;
import com.mycompany.sistema_de_trafico.edd.PriorityQueueV;
import com.mycompany.sistema_de_trafico.edd.Stack;
import com.mycompany.sistema_de_trafico.enums.Direccion;
import com.mycompany.sistema_de_trafico.enums.TipoInterseccion;
import com.mycompany.sistema_de_trafico.objects.Interseccion;
import com.mycompany.sistema_de_trafico.objects.Vehiculo;

public class Simulador {

    private int columnas;
    private int filas;
    // tablero de la ciudad
    private OrthogonalMatrix<Interseccion> ciudad;
    // arbol para buscar la ruta mas compleja
    private PriorityQueueI arbolIntersecciones;
    // pila donde se guardaran los eventos
    private Stack<String> registroEventos;
    // tabla donde se almacenan los vehiculos
    private HashTable tablaVehiculos;
    private boolean simulacionTerminada;
    private char[] letrasFilas = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
            'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };;

    private Scanner sn;

    public Simulador(LinkedList<Vehiculo> vehiculosCargados, int anchoCiudad, int altoCiudad, int tamañoTablaHash) {
        this.columnas = anchoCiudad;
        this.filas = altoCiudad;
        simulacionTerminada = false;

        sn = new Scanner(System.in);

        tablaVehiculos = new HashTable(tamañoTablaHash);
        ciudad = new OrthogonalMatrix<>(anchoCiudad, altoCiudad);
        arbolIntersecciones = new PriorityQueueI();
        registroEventos = new Stack<>();

        System.out.println("Generando el mapa de la ciudad:");
        generarMapaCiudad();
        System.out.println("Repartiendo a los vehiculos en la ciudad...");
        repartirVehiculosCiudad(vehiculosCargados);
        calcularComplejidades();

    }

    public void iniciarSimulador() {
        System.out.println("En simulador, todo bien a este punto");
        while (!simulacionTerminada) {
            ciudad.imprimir();
            realizarAccion(mostrarAcciones());
        }
    }

    private void generarMapaCiudad() {
        if (filas > 26) {
            System.out.println("El numero de filas no debe ser mayor a 26");
            return;
        }

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
                PriorityQueueV cola = obtenerCola(interseccionOrigen);

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

        int numeroDeFila = obtenerNumeroDeFila(nombreInterseccion);
        int numeroDeColumna = obtenerNumeroColumna(nombreInterseccion);

        if (numeroDeFila >= 0 && numeroDeFila < filas && numeroDeColumna >= 0 && numeroDeColumna < columnas) {
            Node<Interseccion> nodoInterseccion = ciudad.obtenerNodo(numeroDeColumna, numeroDeFila);
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

    private PriorityQueueV obtenerCola(Interseccion interseccion) {

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

    private void calcularComplejidades() {
        for (int y = 0; y < filas; y++) {
            for (int x = 0; x < columnas; x++) {
                Interseccion interseccion = ciudad.obtenerDato(x, y);
                interseccion.calcularComplejidad();
                arbolIntersecciones.insertar(interseccion);
                System.out.println("Complejidad: " + interseccion.getComplejidad());
            }
        }
    }

    private int mostrarAcciones() {
        int opcion;

        System.out.println("\n*********** Selecciona Una Opcion ***********");
        System.out.println("1. Mover trafico.");
        System.out.println("2. Ver estado de una interseccion.");
        System.out.println("3. Generar bloqueo.");
        System.out.println("4. Agregar vehiculo manualmente.");
        System.out.println("5. Ver vehiculo existente.");
        System.out.println("6. Terminar simulacion.");
        opcion = sn.nextInt();
        return opcion;
    }

    private void realizarAccion(int opcion) {
        switch (opcion) {
            case 1:
                System.out.println("Iniciando movimiento de trafico.");
                moverTrafico();
                break;

            case 2:
                System.out.println("Viendo estado de interseccion.");
                verEstadoInterseccion();
                break;
            case 3:
                System.out.println("Generando bloqueo.");
                generarBloqueo();
                break;
            case 4:
                System.out.println("Agregando vehiculo manualmente.");
                agregarVehiculo();
                break;
            case 5:
                System.out.println("Mostrando vehiculo.");
                verVehiculo();
                break;
            case 6:
                System.out.println("Abandonando simulacion.");
                simulacionTerminada = true;
                break;
            default:
                System.out.println("Syntax error.");
                break;
        }
    }

    private void moverTrafico() {
        // obteniendo interseccion de mayor prioridad (se elimina de la cola)
        Interseccion interseccionPrioritaria = arbolIntersecciones.desencolar();
        String nombreInterseccionSalida = interseccionPrioritaria.getNombre();
        // obteniendo numero de fila y columna de la interseccion a trabajar
        int filaActual = obtenerNumeroDeFila(nombreInterseccionSalida);
        int columnaActual = obtenerNumeroColumna(nombreInterseccionSalida);

        // verificando que no haya bloqueo
        if (interseccionPrioritaria.isBloqueda()) {
            System.out.println("En la interseccion: " + nombreInterseccionSalida
                    + ", no se puede mover el trafico porque hay un bloqueo.");
            System.out.println("Se necesita liberar el bloqueo para continuar...");
            registroEventos.push("Movimiento de trafico rechazado por bloqueo en interseccion: "
                    + nombreInterseccionSalida);
            return;
        }
        // iterando a cada direccion posible de la interseccion
        for (Direccion direccion : Direccion.values()) {
            // obteniendo cola en la direccion actual
            PriorityQueueV colaActual = interseccionPrioritaria.getColaPorDireccion(direccion);
            // si no hay cola en la direccion actual, se salta la iteracion
            if (colaActual == null)
                continue;

            // cola temporal para evitar enciclar en caso de bloqueo
            PriorityQueueV colaTemporal = new PriorityQueueV();

            for (int i = 0; i < colaActual.getSize(); i++) {
                // recorriendo cola vehiculo a vehiculo
                Vehiculo vehiculoActual = colaActual.desencolar();
                String nombreInterseccionDestino = vehiculoActual.getInterseccionDestino();
                // obteniendo numero de fila y columna de destino
                int filaDestino = obtenerNumeroDeFila(nombreInterseccionDestino);
                int columnaDestino = obtenerNumeroColumna(nombreInterseccionDestino);
                // calculando distancia de la posicion actual al destino del vehiculo
                int distanciaX = columnaDestino - columnaActual;
                int distanciaY = filaDestino - filaActual;
                // dada la distancia se define la direccion del movimiento
                Direccion direccionMovimiento = null;
                // si la distancia en x!=0, el vehiculo se desplaza horizontalmente
                if (distanciaX != 0) {
                    if (distanciaX > 0) {
                        direccionMovimiento = Direccion.ESTE;
                    } else {
                        direccionMovimiento = Direccion.OESTE;
                    }
                    // si la distancia en x=0 y en y!=0, el vehiculo se desplaza verticalmente
                } else if (distanciaY != 0) {
                    if (distanciaY > 0) {
                        direccionMovimiento = Direccion.SUR;
                    } else {
                        direccionMovimiento = Direccion.NORTE;
                    }
                }
                // actualizando coordenada dependiendo del movimiento
                if (direccionMovimiento != null) {
                    int nuevaFila = filaActual;
                    int nuevaColumna = columnaActual;

                    switch (direccionMovimiento) {
                        case NORTE:
                            nuevaFila--;
                            break;
                        case SUR:
                            nuevaFila++;
                            break;
                        case ESTE:
                            nuevaColumna++;
                            break;
                        case OESTE:
                            nuevaColumna--;
                            break;
                    }

                    // obteniendo interseccion donde se ira el vehiculo
                    Interseccion interseccionMovimiento = ciudad.obtenerDato(nuevaFila, nuevaColumna);
                    // verificando datos de interseccion
                    if (interseccionMovimiento != null && !interseccionMovimiento.isBloqueda()) {
                        int distanciaRestanteX = columnaDestino - nuevaColumna;
                        int distanciaRestanteY = filaDestino - nuevaFila;

                        // verificando el proximo movimiento, para insertar el vehiculo en la cola con
                        // esa direccion
                        Direccion proximaDireccion = null;
                        if (distanciaRestanteX != 0) {
                            if (distanciaRestanteX > 0) {
                                proximaDireccion = Direccion.ESTE;
                            } else {
                                proximaDireccion = Direccion.OESTE;
                            }

                        } else if (distanciaRestanteY != 0) {
                            if (distanciaRestanteY > 0) {
                                proximaDireccion = Direccion.SUR;
                            } else {
                                proximaDireccion = Direccion.NORTE;
                            }
                        }
                        if (proximaDireccion != null) {
                            // insertando el vehiculo en la cola de la direccion del proximo movimiento
                            interseccionMovimiento.getColaPorDireccion(proximaDireccion).insertar(vehiculoActual);
                        } else {
                            registroEventos
                                    .push("Vehiculo " + vehiculoActual.getPlaca() + " ya llego a su destino: "
                                            + vehiculoActual.getInterseccionDestino());
                        }

                        // registrando movimiento
                        registroEventos.push("Vehiculo " + vehiculoActual.getPlaca() + " se movio de " +
                                interseccionPrioritaria.getNombre() + ", a " + interseccionMovimiento.getNombre());
                        interseccionMovimiento.calcularComplejidad();
                    } else {
                        // No pudo moverse, se guarda en la cola temporal
                        colaTemporal.insertar(vehiculoActual);
                    }
                } else {
                    // cuando ya no hay direccion de movimiento el vehiculo ya llego a su destino
                    registroEventos.push("Vehiculo " + vehiculoActual.getPlaca() + " ya llego a su destino: "
                            + vehiculoActual.getInterseccionDestino());
                }
            }
            //llenando cola actual con la temporal con los vehiculos que no se pudieron mover
            while (!colaTemporal.estaVacia()) {
                colaActual.insertar(colaTemporal.desencolar());
            }
        }
        // actualizando complejidad y volviendo a encolar la interseccion
        interseccionPrioritaria.calcularComplejidad();
        arbolIntersecciones.insertar(interseccionPrioritaria);
    }

    private int obtenerNumeroDeFila(String nombreInterseccion) {
        char letraDeFila = Character.toUpperCase(nombreInterseccion.charAt(0));
        int numeroDeFila = -1;

        for (int i = 0; i < letrasFilas.length; i++) {
            if (letrasFilas[i] == letraDeFila) {
                numeroDeFila = i;
                break;
            }
        }

        return numeroDeFila;
    }

    private int obtenerNumeroColumna(String nombreInterseccion) {
        int numeroDeColumna = Integer.parseInt(nombreInterseccion.substring(1)) - 1;
        return numeroDeColumna;
    }

    private void verEstadoInterseccion() {
        sn.nextLine();
        System.out.println("Ingresa el nombre de la interseccion que deseas ver (A1, B3, C2, etc).");
        String nombreInterseccion = sn.nextLine();

        Interseccion interseccionBuscada = obtenerInterseccion(nombreInterseccion);

        if (interseccionBuscada != null) {
            System.out.println("\n****Estado de la interseccion****");
            System.out.println("Nombre: " + interseccionBuscada.getNombre());
            System.out.println("Complejidad: " + interseccionBuscada.getComplejidad());
            System.out.println("Hay bloqueo?: " + interseccionBuscada.isBloqueda());
            System.out.println("Representacion en consola: " + interseccionBuscada.getRepresentacionConsola());
            if (interseccionBuscada.getColaNorte() != null) {
                System.out.println("Cola norte:");
                interseccionBuscada.getColaNorte().imprimir();
            }
            if (interseccionBuscada.getColaSur() != null) {
                System.out.println("Cola sur:");
                interseccionBuscada.getColaSur().imprimir();
            }
            if (interseccionBuscada.getColaEste() != null) {
                System.out.println("Cola este:");
                interseccionBuscada.getColaEste().imprimir();
            }
            if (interseccionBuscada.getColaOeste() != null) {
                System.out.println("Cola oeste:");
                interseccionBuscada.getColaOeste().imprimir();
            }
        }
    }

    private void generarBloqueo() {
    }

    private void agregarVehiculo() {
    }

    private void verVehiculo() {
    }
}
