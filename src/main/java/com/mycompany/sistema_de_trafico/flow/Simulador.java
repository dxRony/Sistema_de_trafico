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
import com.mycompany.sistema_de_trafico.enums.TipoVehiculo;
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
            'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
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

        System.out.println("Generando el mapa de la ciudad...");
        generarMapaCiudad();
        System.out.println("Repartiendo a los vehiculos en la ciudad...");
        repartirVehiculosCiudad(vehiculosCargados);
        calcularComplejidades();

    }

    public void iniciarSimulador() {
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
                    vehiculo.setInterseccionActual(interseccionOrigen.getNombre());
                    tablaVehiculos.insertar(vehiculo);
                    interseccionOrigen.setVehiculosCirculados(interseccionOrigen.getVehiculosCirculados() + 1);
                }

            } else {
                System.out.println("Vehiculo con placa: " + vehiculo.getPlaca() +
                        ", tiene un origen o destino inexistente");
                vehiculosPerdidos++;
            }
            listaVehiculos.remove(vehiculo);
        }
        System.out.println("Se repartieron: " + vehiculosRepartidos + "\nSe perdieron: " + vehiculosPerdidos);
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
        System.out.println("6. Ver vehiculos en destino y no en destino.");
        System.out.println("7. Mostrar ultimos eventos.");
        System.out.println("8. Mostrar complejidades de intersecciones.");
        System.out.println("9. Terminar simulacion.");
        opcion = sn.nextInt();
        return opcion;
    }

    private void realizarAccion(int opcion) {
        switch (opcion) {
            case 1:
                System.out.println("\nIniciando movimiento de trafico...");
                moverTrafico();
                break;

            case 2:
                System.out.println("\nViendo estado de interseccion...");
                verEstadoInterseccion();
                break;
            case 3:
                System.out.println("\nGenerando bloqueo...");
                generarBloqueo();
                break;
            case 4:
                System.out.println("\nAgregando vehiculo manualmente...");
                agregarVehiculo();
                break;
            case 5:
                System.out.println("\nMostrando vehiculo...");
                verVehiculo();
                break;
            case 6:
                System.out.println("\nMostrando vehiculos...");
                mostrarVehiculosEnDestino();
                break;
            case 7:
                System.out.println("\nMostrando ultimos 20 eventos...");
                mostrarUltimosEventos();
                break;
            case 8:
                System.out.println("\nMostrando complejidades de intersecciones...");
                mostrarComplejidades();
                break;
            case 9:
                System.out.println("\nAbandonando simulacion...");
                simulacionTerminada = true;
                break;
            default:
                System.out.println("\nSyntax error.");
                break;
        }
    }

    private void moverTrafico() {
        // obteniendo interseccion de mayor prioridad (se elimina de la cola)
        Interseccion interseccionPrioritaria = arbolIntersecciones.desencolar();
        if (interseccionPrioritaria.getComplejidad() == 0) {
            System.out.println("Ya no hay trafico en la ciudad, buenas noches.");
            simulacionTerminada = true;
            return;
        }
        // verificando que no este bloqueada
        if (interseccionPrioritaria.isBloqueda()) {
            verificarBloqueo(interseccionPrioritaria);
            return;
        }
        String nombreOrigen = interseccionPrioritaria.getNombre();
        String mensaje = "Moviendo trafico en interseccion: " + nombreOrigen
                + ", con prioridad: " + interseccionPrioritaria.getComplejidad();

        System.out.println(mensaje);
        registroEventos.push(mensaje);
        // obteniendo numero de fila y columna de la interseccion a trabajar
        int filaActual = obtenerNumeroDeFila(nombreOrigen);
        int columnaActual = obtenerNumeroColumna(nombreOrigen);

        // iterando a cada direccion posible de la interseccion
        for (Direccion direccion : Direccion.values()) {
            PriorityQueueV colaActual = interseccionPrioritaria.getColaPorDireccion(direccion);
            if (colaActual == null) {
                continue;
            }
            moverVehiculosDeCola(colaActual, interseccionPrioritaria, filaActual, columnaActual);
        }
        // actualizando complejidad y volviendo a encolar la interseccion
        interseccionPrioritaria.calcularComplejidad();
        arbolIntersecciones.insertar(interseccionPrioritaria);
    }

    private void verificarBloqueo(Interseccion interseccion) {
        System.out.println("En la interseccion: " + interseccion.getNombre()
                + ", no se puede mover el trafico porque hay un bloqueo.");
        System.out.println("Se necesita liberar el bloqueo para continuar...");
        registroEventos.push("Movimiento de trafico rechazado por bloqueo en interseccion: "
                + interseccion.getNombre());
    }

    private void moverVehiculosDeCola(PriorityQueueV colaActual, Interseccion origen, int filaOrigen,
            int columnaOrigen) {
        PriorityQueueV colaTemporal = new PriorityQueueV();

        while (!colaActual.estaVacia()) {
            // recorriendo vehiculo a vehiculo y procesandolo
            Vehiculo vehiculo = colaActual.desencolar();
            procesarVehiculo(vehiculo, origen, filaOrigen, columnaOrigen, colaTemporal);
        }
        while (!colaTemporal.estaVacia()) {
            // si hubo vehiculos que no se movieron se vuelven a guardar
            colaActual.insertar(colaTemporal.desencolar());
        }
    }

    private void procesarVehiculo(Vehiculo vehiculo, Interseccion origen, int filaActual, int columnaActual,
            PriorityQueueV colaTemporal) {
        String destino = vehiculo.getInterseccionDestino();
        System.out.println("\nVehiculo:" + vehiculo.getPlaca() + ", con destino: " + destino + ", intenta avanzar.");

        int filaDestino = obtenerNumeroDeFila(destino);
        int columnaDestino = obtenerNumeroColumna(destino);

        if (filaActual == filaDestino && columnaActual == columnaDestino) {
            // validacion extra por si el vehiculo inicia en su destino
            System.out.println("El vehiculo: " + vehiculo.getPlaca() + ", ya esta en su destino.");
            vehiculo.setEnDestino(true);
            return;
        }
        // calculando mocimineto
        int nuevaFila = filaActual;
        int nuevaColumna = columnaActual;

        int distanciaFila = filaDestino - filaActual;
        int distanciaColumna = columnaDestino - columnaActual;
        Direccion direccionMovimiento = calcularDireccionMovimiento(distanciaFila, distanciaColumna);
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

        System.out.println("El vehiculo se movera en direccion: " + direccionMovimiento);
        Interseccion interseccionDestino = ciudad.obtenerDato(nuevaColumna, nuevaFila);

        if (!interseccionDestino.isBloqueda()) {
            // verificando si la interseccion a la que se movera no este bloqueada
            moverVehiculo(vehiculo, origen, interseccionDestino, filaDestino, columnaDestino, nuevaFila, nuevaColumna);
        } else {
            System.out.println("El vehiculo no se pudo mover por que la interseccion destino con nombre: "
                    + interseccionDestino.getNombre() + ", esta bloqueada.");
            vehiculo.setTiempoDeEspera(vehiculo.getTiempoDeEspera() + 1);
            colaTemporal.insertar(vehiculo);
        }
    }

    private Direccion calcularDireccionMovimiento(int distanciaFila, int distanciaColumna) {

        if (distanciaColumna != 0) {
            if (distanciaColumna < 0) {
                return Direccion.OESTE;
            } else {
                return Direccion.ESTE;
            }
        } else if (distanciaFila != 0) {
            if (distanciaFila < 0) {
                return Direccion.NORTE;
            } else { // Izquierda = OESTE
                return Direccion.SUR;
            }
        } else {
            return null;
        }
    }

    private void moverVehiculo(Vehiculo vehiculo, Interseccion origen, Interseccion destino, int filaDestino,
            int colDestino, int nuevaFila, int nuevaColumna) {
        System.out.println("El vehiculo se esta moviendo...");
        // calculando siguiente movimiento si lo hay
        int distanciaRestanteX = filaDestino - nuevaFila;
        int distanciaRestanteY = colDestino - nuevaColumna;
        Direccion proximaDireccion = calcularProximaDireccion(distanciaRestanteX, distanciaRestanteY);

        if (proximaDireccion != null) {
            // si hay proxima direccion se inserta en el carril hacia ella
            destino.getColaPorDireccion(proximaDireccion).insertar(vehiculo);
            String mensaje = "Vehiculo " + vehiculo.getPlaca() + " se movio de " +
                    origen.getNombre() + " a " + destino.getNombre() + ", al carril: " + proximaDireccion + ",";
            vehiculo.setTiempoDeEspera(vehiculo.getTiempoDeEspera() + 1);
            vehiculo.setInterseccionActual(destino.getNombre());
            System.out.println(mensaje);
            registroEventos.push(mensaje);
            destino.setVehiculosCirculados(destino.getVehiculosCirculados() + 1);
            arbolIntersecciones.actualizarInterseccion(destino.getNombre());
        } else {
            // si no hay proxima direccion, el vehiculo ya esta en su destino
            String mensaje = "Vehiculo con placa: " + vehiculo.getPlaca()
                    + ", llego a su destino: " + vehiculo.getInterseccionDestino();
            vehiculo.setTiempoDeEspera(vehiculo.getTiempoDeEspera() + 1);
            vehiculo.setEnDestino(true);
            System.out.println(mensaje);
            registroEventos.push(mensaje);
        }
    }

    private Direccion calcularProximaDireccion(int distanciaFila, int distanciaColumna) {
        if (distanciaColumna != 0) {
            if (distanciaColumna < 0) {
                return Direccion.OESTE;
            } else {
                return Direccion.ESTE;
            }
        } else if (distanciaFila != 0) {
            if (distanciaFila < 0) {
                return Direccion.NORTE;
            } else { // Izquierda = OESTE
                return Direccion.SUR;
            }
        } else {
            return null;
        }
    }

    private int obtenerNumeroDeFila(String nombreInterseccion) {
        return nombreInterseccion.charAt(0) - 'A';
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
            System.out.println("Vehiculos circulados: " + interseccionBuscada.getVehiculosCirculados());
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
        Scanner sn = new Scanner(System.in);
        System.out.print("Ingresa el nombre de la interseccion donde quieres generar el bloqueo: ");
        String nombreInterseccion = sn.next();
        Interseccion interseccionABloquear = obtenerInterseccion(nombreInterseccion);
        if (interseccionABloquear != null) {
            System.out.println("Bloqueando interseccion");
            interseccionABloquear.setBloqueda(true);
            String mensaje = "Bloqueando interseccion: " + interseccionABloquear.getNombre();
            System.out.println(mensaje);
            registroEventos.push(mensaje);
        }
    }

    private void agregarVehiculo() {
        Scanner sn = new Scanner(System.in);
        System.out.println("Ingresa los datos del vehiculo manualmente!!!");
        System.out.print("Ingresa el tipo: ");
        String tipo = sn.next();
        TipoVehiculo tipoVehiculo = TipoVehiculo.valueOf(tipo);
        System.out.print("Ingresa la placa del vehiculo: ");
        String placa = sn.next();
        System.out.print("Ingresa la interseccion de origen: ");
        String interSeccionOrigen = sn.next();
        System.out.print("Ingresa la interseccion de destino: ");
        String interseccionDestino = sn.next();
        System.out.print("Ingresa la prioridad: ");
        int prioridad = sn.nextInt();
        System.out.print("Ingresa el tiempo de espera: ");
        int tiempoDeEspera = sn.nextInt();
        Vehiculo nuevoVehiculo = new Vehiculo(tipoVehiculo, placa, interSeccionOrigen, interseccionDestino, prioridad,
                tiempoDeEspera);
        colocarVehiculo(nuevoVehiculo);
    }

    private void colocarVehiculo(Vehiculo vehiculo) {
        Interseccion interseccionOrigen = obtenerInterseccion(vehiculo.getInterseccionOrigen());
        Interseccion interseccionDestino = obtenerInterseccion(vehiculo.getInterseccionDestino());

        if (interseccionOrigen != null && interseccionDestino != null) {
            PriorityQueueV cola = obtenerCola(interseccionOrigen);
            if (cola != null) {
                cola.insertar(vehiculo);
                tablaVehiculos.insertar(vehiculo);
                interseccionOrigen.setVehiculosCirculados(interseccionOrigen.getVehiculosCirculados() + 1);
                vehiculo.setInterseccionActual(interseccionOrigen.getNombre());
                String mensaje = "Agregando vehiculo: " + vehiculo.toString();
                System.out.println(mensaje);
                registroEventos.push(mensaje);
            }
        }
    }

    private void verVehiculo() {
        Scanner sn = new Scanner(System.in);
        System.out.print("Ingresa la placa del vehiculo que quieres ver: ");
        String placaABuscar = sn.next();
        Vehiculo vehiculoBuscado = tablaVehiculos.buscar(placaABuscar);
        if (vehiculoBuscado != null) {
            System.out.println(vehiculoBuscado.toString());
        } else {
            return;
        }
    }

    private void mostrarVehiculosEnDestino() {
        Scanner sn = new Scanner(System.in);
        System.out.println("Selecciona que vehiculos quieres ver:");
        System.out.println("1. Vehiculos en destino.");
        System.out.println("2. Vehiculos no en destino.");
        int opcion = sn.nextInt();
        tablaVehiculos.mostrarVehiculosEnDestino(tablaVehiculos, opcion);
    }

    private void mostrarUltimosEventos() {
        registroEventos.imprimirUltimosEventos();

    }

    private void mostrarComplejidades() {
        arbolIntersecciones.imprimir();
    }
}
