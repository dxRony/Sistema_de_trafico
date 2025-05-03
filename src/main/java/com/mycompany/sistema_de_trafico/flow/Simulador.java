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
    // lista donde se guardan los vehiculos con placa duplicada
    private LinkedList<Vehiculo> listaDuplicados;
    // lista donde se guardan todos los vehiculos
    private LinkedList<Vehiculo> listaGeneralVehiculos;

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
        listaDuplicados = new LinkedList<>();
        listaGeneralVehiculos = new LinkedList<>();

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
        if (filas > 26) {                                                       //1
            System.out.println("El numero de filas no debe ser mayor a 26");  //1  
            return;                                                             //1
        }   
        for (int y = 0; y < filas; y++) {                                       //n
            char letraDeFila = letrasFilas[y];                                  //n
            for (int x = 0; x < columnas; x++) {                                //n^2
                TipoInterseccion tipoInterseccion;                              //n^2
                String nombreInterseccion = letraDeFila + String.valueOf(x + 1);//n^2
                if (x == 0 && y == 0) {                                         //n^2
                    // tipoInterseccion en esquina superior izquierda
                    tipoInterseccion = TipoInterseccion.CRUCELVOLTEADAIZQUIERDA;//n^2
                } else if (x == columnas - 1 && y == 0) {                       //n^2
                    // tipoInterseccion en esquina superior derecha
                    tipoInterseccion = TipoInterseccion.CRUCELOPUESTA;          //n^2
                } else if (x == 0 && y == filas - 1) {                          //n^2
                    // tipoInterseccion en esquina inferior izquierda
                    tipoInterseccion = TipoInterseccion.CRUCEL;                 //n^2
                } else if (x == columnas - 1 && y == filas - 1) {               //n^2
                    // tipoInterseccion en esquina inferior derecha
                    tipoInterseccion = TipoInterseccion.CRUCELVOLTEADADERECHA;  //n^2
                } else if (x == 0) {                                            //n^2
                    // tipoInterseccion en columna afuera izquierda
                    tipoInterseccion = TipoInterseccion.CRUCETVOLTEADAIZQUIERDA;//n^2
                } else if (x == columnas - 1) {                                 //n^2
                    // tipoInterseccion en columna afuera derecha
                    tipoInterseccion = TipoInterseccion.CRUCETVOLTEADADERECHA;  //n^2
                } else if (y == 0) {                                            //n^2
                    // tipoInterseccion en fila arriba
                    tipoInterseccion = TipoInterseccion.CRUCET;                 //n^2
                } else if (y == filas - 1) {                                    //n^2
                    // tipoInterseccion en fila abajo   
                    tipoInterseccion = TipoInterseccion.CRUCETOPUESTA;          //n^2
                } else {                                                        //n^2
                    // tipoInterseccion en centro de matriz
                    tipoInterseccion = TipoInterseccion.CRUCEMAS;               //n^2
                }
                Interseccion interseccion = new Interseccion(nombreInterseccion, tipoInterseccion);
                ciudad.insertarDato(x, y, interseccion);                        //n^2
            }
        }
    }

    private void repartirVehiculosCiudad(LinkedList<Vehiculo> listaVehiculos) {
        if (listaVehiculos == null || listaVehiculos.getSize() == 0) {      //1
            System.out.println("No hay vehiculos para repartir");           //1
            return;                                                         //1
        }
        int vehiculosRepartidos = 0;                                        //1
        int vehiculosPerdidos = 0;                                          //1

        while (!listaVehiculos.isEmpty()) {                                 //n
            Node<Vehiculo> nodoVehiculo = listaVehiculos.getHead();         //n
            Vehiculo vehiculo = nodoVehiculo.getData();                     //n

            Interseccion interseccionOrigen = obtenerInterseccion(vehiculo.getInterseccionOrigen());
            Interseccion interseccionDestino = obtenerInterseccion(vehiculo.getInterseccionDestino());

            if (interseccionOrigen != null && interseccionDestino != null) {//n
                PriorityQueueV cola = obtenerCola(interseccionOrigen);      //n

                if (cola != null) {                                         //n
                    boolean insertado = tablaVehiculos.insertar(vehiculo);  //n
                    if (!insertado) {                                       //n
                        listaDuplicados.add(vehiculo);                      //n
                    } else {                                                //n
                        cola.insertar(vehiculo);                            //n
                        listaGeneralVehiculos.add(vehiculo);                //n
                        vehiculosRepartidos++;                              //n
                        vehiculo.setInterseccionActual(interseccionOrigen.getNombre());
                        interseccionOrigen.setVehiculosCirculados(interseccionOrigen.getVehiculosCirculados() + 1);
                    }
                }
            } else {                                                        //n
                System.out.println("Vehiculo con placa: " + vehiculo.getPlaca() +
                        ", tiene un origen o destino inexistente");         //n
                vehiculosPerdidos++;                                        //n
            }
            listaVehiculos.remove(vehiculo);                                //n
        }
        System.out.println("Se repartieron: " + vehiculosRepartidos + "\nSe perdieron: " + vehiculosPerdidos);
    }

    private Interseccion obtenerInterseccion(String nombreInterseccion) {
        if (nombreInterseccion == null) {                       //1
            return null;                                        //1
        }

        int numeroDeFila = obtenerNumeroDeFila(nombreInterseccion); //1
        int numeroDeColumna = obtenerNumeroColumna(nombreInterseccion);//1

        if (numeroDeFila >= 0 && numeroDeFila < filas && numeroDeColumna >= 0 && numeroDeColumna < columnas) {
            Node<Interseccion> nodoInterseccion = ciudad.obtenerNodo(numeroDeColumna, numeroDeFila);    //1
            if (nodoInterseccion != null) {                     //1
                return nodoInterseccion.getData();              //1
            } else {                                            //1
                return null;                                    //1
            }
        } else {                                                //1
            // excediendo limites del tablero -> nodo inexistente   
            return null;                                        //1
        }
    }

    private PriorityQueueV obtenerCola(Interseccion interseccion) {

        if (interseccion.getColaNorte() != null) {          //1
            return interseccion.getColaNorte();             //1
        } else if (interseccion.getColaSur() != null) {     //1
            return interseccion.getColaSur();               //1
        } else if (interseccion.getColaEste() != null) {    //1
            return interseccion.getColaEste();              //1
        } else if (interseccion.getColaOeste() != null) {   //1
            return interseccion.getColaOeste();             //1
        }
        return null;
    }

    private void calcularComplejidades() {
        for (int y = 0; y < filas; y++) {                               //n
            for (int x = 0; x < columnas; x++) {                        //n^2
                Interseccion interseccion = ciudad.obtenerDato(x, y);   //n^2
                interseccion.calcularComplejidad();                     //n^2
                arbolIntersecciones.insertar(interseccion);             //n^2
            }
        }
    }

    private int mostrarAcciones() {
        int opcion;
        System.out.println("\n*********** Selecciona Una Opcion ***********");
        System.out.println("1. Mover trafico.");
        System.out.println("2. Ver estado de una interseccion.");
        System.out.println("3. Ver interseccion mas congestionada.");
        System.out.println("4. Generar bloqueo.");
        System.out.println("5. Agregar vehiculo manualmente.");
        System.out.println("6. Ver vehiculo existente.");
        System.out.println("7. Ver vehiculos en destino y no en destino.");
        System.out.println("8. Mostrar ultimos eventos.");
        System.out.println("9. Mostrar complejidades de intersecciones.");
        System.out.println("10. Terminar simulacion.");
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
                System.out.println("\nMostrando interseccion mas congestionada...");
                verInterseccionMasCongestionada();
                break;
            case 4:
                System.out.println("\nGenerando bloqueo...");
                generarBloqueo();
                break;
            case 5:
                System.out.println("\nAgregando vehiculo manualmente...");
                agregarVehiculo();
                break;
            case 6:
                System.out.println("\nMostrando vehiculo...");
                verVehiculo();
                break;
            case 7:
                System.out.println("\nMostrando vehiculos...");
                mostrarVehiculosEnDestino();
                break;
            case 8:
                System.out.println("\nMostrando ultimos 20 eventos...");
                mostrarUltimosEventos();
                break;
            case 9:
                System.out.println("\nMostrando complejidades de intersecciones...");
                mostrarComplejidades();
                break;
            case 10:
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
        Interseccion interseccionPrioritaria = arbolIntersecciones.desencolar();        //1
        if (interseccionPrioritaria.getComplejidad() == 0) {                            //1
            System.out.println("Ya no hay trafico en la ciudad, buenas noches.");     //1
            simulacionTerminada = true;                                                 //1
            return;                                                                     //1
        }
        // verificando que no este bloqueada
        if (interseccionPrioritaria.isBloqueda()) {                                     //1
            verificarBloqueo(interseccionPrioritaria);                                  //1
            return;                                                                     //1
        }
        String nombreOrigen = interseccionPrioritaria.getNombre();                      //1
        String mensaje = "Moviendo trafico en interseccion: " + nombreOrigen    
                + ", con prioridad: " + interseccionPrioritaria.getComplejidad();       //1

        System.out.println(mensaje);                                                    //1
        registroEventos.push(mensaje);                                                  //1
        // obteniendo numero de fila y columna de la interseccion a trabajar
        int filaActual = obtenerNumeroDeFila(nombreOrigen);                             //1
        int columnaActual = obtenerNumeroColumna(nombreOrigen);                         //1

        // iterando a cada direccion posible de la interseccion
        for (Direccion direccion : Direccion.values()) {
            PriorityQueueV colaActual = interseccionPrioritaria.getColaPorDireccion(direccion);
            if (colaActual == null) {                                                   //n
                continue;                                                               //n
            }
            moverVehiculosDeCola(colaActual, interseccionPrioritaria, filaActual, columnaActual);
        }
        // actualizando complejidad y volviendo a encolar la interseccion
        interseccionPrioritaria.calcularComplejidad();                                  //1
        arbolIntersecciones.insertar(interseccionPrioritaria);                          //1
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
        PriorityQueueV colaTemporal = new PriorityQueueV();                             //1

        while (!colaActual.estaVacia()) {                                               //n
            // recorriendo vehiculo a vehiculo y procesandolo
            Vehiculo vehiculo = colaActual.desencolar();                                //n
            procesarVehiculo(vehiculo, origen, filaOrigen, columnaOrigen, colaTemporal);//n
        }
        while (!colaTemporal.estaVacia()) {                                             //n
            // si hubo vehiculos que no se movieron se vuelven a guardar
            colaActual.insertar(colaTemporal.desencolar());                             //n
        }
    }

    private void procesarVehiculo(Vehiculo vehiculo, Interseccion origen, int filaActual, int columnaActual,
            PriorityQueueV colaTemporal) {
        String destino = vehiculo.getInterseccionDestino();
        System.out.println("\nVehiculo:" + vehiculo.getPlaca() + ", con destino: " + destino + ", intenta avanzar.");
        int filaDestino = obtenerNumeroDeFila(destino);                         //1
        int columnaDestino = obtenerNumeroColumna(destino);                     //1

        if (filaActual == filaDestino && columnaActual == columnaDestino) {     //1
            // validacion extra por si el vehiculo inicia en su destino
            System.out.println("El vehiculo: " + vehiculo.getPlaca() + ", ya esta en su destino.");
            vehiculo.setEnDestino(true);                                //1
            return;                                                             //1
        }
        // calculando mocimineto
        int nuevaFila = filaActual;                                             //1
        int nuevaColumna = columnaActual;                                       //1

        int distanciaFila = filaDestino - filaActual;                           //1
        int distanciaColumna = columnaDestino - columnaActual;                  //1
        Direccion direccionMovimiento = calcularDireccionMovimiento(distanciaFila, distanciaColumna);
        switch (direccionMovimiento) {                                          //1
            case NORTE:                                                         //1
                nuevaFila--;                                                    //1
                break;                                                          //1
            case SUR:                                                           //1
                nuevaFila++;                                                    //1
                break;                                                          //1
            case ESTE:                                                          //1
                nuevaColumna++;                                                 //1
                break;                                                          //1
            case OESTE:                                                         //1
                nuevaColumna--;                                                 //1
                break;                                                          //1
        }
        System.out.println("El vehiculo se movera en direccion: " + direccionMovimiento);
        Interseccion interseccionDestino = ciudad.obtenerDato(nuevaColumna, nuevaFila);

        if (!interseccionDestino.isBloqueda()) {                                //1
            // verificando si la interseccion a la que se movera no este bloqueada
            moverVehiculo(vehiculo, origen, interseccionDestino, filaDestino, columnaDestino, nuevaFila, nuevaColumna);
        } else {                                                                //1
            System.out.println("El vehiculo no se pudo mover por que la interseccion destino con nombre: "
                    + interseccionDestino.getNombre() + ", esta bloqueada.");   //1
            vehiculo.setTiempoDeEspera(vehiculo.getTiempoDeEspera() + 1);       //1
            colaTemporal.insertar(vehiculo);                                    //1
        }
    }

    private Direccion calcularDireccionMovimiento(int distanciaFila, int distanciaColumna) {

        if (distanciaColumna != 0) {            //1
            if (distanciaColumna < 0) {         //1
                return Direccion.OESTE;         //1
            } else {                            //1
                return Direccion.ESTE;          //1
            }
        } else if (distanciaFila != 0) {        //1
            if (distanciaFila < 0) {            //1
                return Direccion.NORTE;         //1
            } else {
                return Direccion.SUR;           //1
            }
        } else {                                //1
            return null;                        //1
        }
    }

    private void moverVehiculo(Vehiculo vehiculo, Interseccion origen, Interseccion destino, int filaDestino,
            int colDestino, int nuevaFila, int nuevaColumna) {
        System.out.println("El vehiculo se esta moviendo...");              //1
        // calculando siguiente movimiento si lo hay    
        int distanciaRestanteX = filaDestino - nuevaFila;                       //1
        int distanciaRestanteY = colDestino - nuevaColumna;                     //1
        Direccion proximaDireccion = calcularProximaDireccion(distanciaRestanteX, distanciaRestanteY);

        if (proximaDireccion != null) {                                         //1
            // si hay proxima direccion se inserta en el carril hacia ella
            destino.getColaPorDireccion(proximaDireccion).insertar(vehiculo);   //1
            String mensaje = "Vehiculo " + vehiculo.getPlaca() + " se movio de " +
                    origen.getNombre() + " a " + destino.getNombre() + ", al carril: " + proximaDireccion + ",";
            vehiculo.setTiempoDeEspera(vehiculo.getTiempoDeEspera() + 1);       //1
            vehiculo.setInterseccionActual(destino.getNombre());                //1
            System.out.println(mensaje);                                        //1
            registroEventos.push(mensaje);                                      //1
            destino.setVehiculosCirculados(destino.getVehiculosCirculados() + 1);//1
            arbolIntersecciones.actualizarInterseccion(destino.getNombre());    //1
        } else {                                                                //1
            // si no hay proxima direccion, el vehiculo ya esta en su destino
            String mensaje = "Vehiculo con placa: " + vehiculo.getPlaca()       
                    + ", llego a su destino: " + vehiculo.getInterseccionDestino();
            vehiculo.setTiempoDeEspera(vehiculo.getTiempoDeEspera() + 1);       //1
            vehiculo.setEnDestino(true);                                //1
            System.out.println(mensaje);                                        //1
            registroEventos.push(mensaje);                                      //1
        }
    }

    private Direccion calcularProximaDireccion(int distanciaFila, int distanciaColumna) {
        if (distanciaColumna != 0) {                        //1
            if (distanciaColumna < 0) {                     //1
                return Direccion.OESTE;                     //1
            } else {                                        //1
                return Direccion.ESTE;                      //1
            }
        } else if (distanciaFila != 0) {                    //1
            if (distanciaFila < 0) {                        //1
                return Direccion.NORTE;                     //1
            } else {                                        //1
                return Direccion.SUR;                       //1
            }   
        } else {                                            //1
            return null;                                    //1
        }
    }

    private int obtenerNumeroDeFila(String nombreInterseccion) {
        return (int) nombreInterseccion.charAt(0) - 65;
    }

    private int obtenerNumeroColumna(String nombreInterseccion) {
        int numeroDeColumna = Integer.parseInt(nombreInterseccion.substring(1)) - 1;
        return numeroDeColumna;
    }

    private void verEstadoInterseccion() {
        sn.nextLine();
        System.out.println("Ingresa el nombre de la interseccion que deseas ver (A1, B3, C2, etc).");         //1
        String nombreInterseccion = sn.nextLine();                                                              //1
        Interseccion interseccionBuscada = obtenerInterseccion(nombreInterseccion);                             //1

        if (interseccionBuscada != null) {                                                                      //1
            System.out.println("\n****Estado de la interseccion****");                                        //1
            System.out.println("Nombre: " + interseccionBuscada.getNombre());                                   //1
            System.out.println("Complejidad: " + interseccionBuscada.getComplejidad());                         //1
            System.out.println("Hay bloqueo?: " + interseccionBuscada.isBloqueda());                            //1
            System.out.println("Representacion en consola: " + interseccionBuscada.getRepresentacionConsola()); //1
            System.out.println("Vehiculos circulados: " + interseccionBuscada.getVehiculosCirculados());        //1
            if (interseccionBuscada.getColaNorte() != null) {                                                   //1
                System.out.println("Cola norte:");                                                            //1
                interseccionBuscada.getColaNorte().imprimir();                                                  //1
            }
            if (interseccionBuscada.getColaSur() != null) {                                                     //1
                System.out.println("Cola sur:");                                                              //1
                interseccionBuscada.getColaSur().imprimir();                                                    //1
            }
            if (interseccionBuscada.getColaEste() != null) {                                                    //1
                System.out.println("Cola este:");                                                             //1
                interseccionBuscada.getColaEste().imprimir();                                                   //1
            }
            if (interseccionBuscada.getColaOeste() != null) {                                                   //1
                System.out.println("Cola oeste:");                                                            //1
                interseccionBuscada.getColaOeste().imprimir();                                                  //1
            }
        }
    }

    private void verInterseccionMasCongestionada() {
        System.out.println(arbolIntersecciones.getHead().getData().toString());
    }

    private void generarBloqueo() {
        Scanner sn = new Scanner(System.in);                                                                //1
        System.out.print("Ingresa el nombre de la interseccion donde quieres generar el bloqueo: ");      //1
        String nombreInterseccion = sn.next();                                                              //1
        Interseccion interseccionABloquear = obtenerInterseccion(nombreInterseccion);                       //1
        if (interseccionABloquear != null) {                                                                //1
            System.out.println("Bloqueando interseccion");                                                //1
            interseccionABloquear.setBloqueda(true);                                               //1
            String mensaje = "Bloqueando interseccion: " + interseccionABloquear.getNombre();               //1
            System.out.println(mensaje);                                                                    //1
            registroEventos.push(mensaje);                                                                  //1
        }
    }

    private void agregarVehiculo() {
        Scanner sn = new Scanner(System.in);                                        //1
        System.out.println("Ingresa los datos del vehiculo manualmente!!!");      //1
        System.out.print("Ingresa el tipo de vehiculo: ");                          //1
        String tipo = sn.next();                                                    //1
        TipoVehiculo tipoVehiculo = TipoVehiculo.valueOf(tipo);                     //1
        System.out.print("Ingresa la placa del vehiculo: ");                        //1
        String placa = sn.next();                                                   //1
        System.out.print("Ingresa la interseccion de origen: ");                    //1
        String interSeccionOrigen = sn.next();                                      //1
        System.out.print("Ingresa la interseccion de destino: ");                   //1
        String interseccionDestino = sn.next();                                     //1
        System.out.print("Ingresa la prioridad: ");                                 //1
        int prioridad = sn.nextInt();                                               //1
        System.out.print("Ingresa el tiempo de espera: ");                          //1
        int tiempoDeEspera = sn.nextInt();                                          //1
        Vehiculo nuevoVehiculo = new Vehiculo(tipoVehiculo, placa, interSeccionOrigen, interseccionDestino, prioridad,
                tiempoDeEspera);                                                    //1
        colocarVehiculo(nuevoVehiculo);                                             //1
    }

    private void colocarVehiculo(Vehiculo vehiculo) {
        Interseccion interseccionOrigen = obtenerInterseccion(vehiculo.getInterseccionOrigen());        //1
        Interseccion interseccionDestino = obtenerInterseccion(vehiculo.getInterseccionDestino());      //1

        if (interseccionOrigen != null && interseccionDestino != null) {                                //1
            PriorityQueueV cola = obtenerCola(interseccionOrigen);                                      //1
            if (cola != null) {                                                                         //1
                boolean insertado = tablaVehiculos.insertar(vehiculo);                                  //1
                if (!insertado) {                                                                       //1
                    listaDuplicados.add(vehiculo);                                                      //1
                } else {                                                                                //1
                    cola.insertar(vehiculo);                                                            //1
                    interseccionOrigen.setVehiculosCirculados(interseccionOrigen.getVehiculosCirculados() + 1);
                    vehiculo.setInterseccionActual(interseccionOrigen.getNombre());                     //1
                    String mensaje = "Agregando vehiculo: " + vehiculo.toString();                      //1
                    System.out.println(mensaje);                                                        //1
                    registroEventos.push(mensaje);                                                      //1
                    listaGeneralVehiculos.add(vehiculo);                                                //1
                }
            }
        }
    }

    private void verVehiculo() {
        Scanner sn = new Scanner(System.in);                                            //1
        System.out.print("Ingresa la placa del vehiculo que quieres ver: ");          //1
        String placaABuscar = sn.next();                                                //1
        Vehiculo vehiculoBuscado = tablaVehiculos.buscar(placaABuscar);                 //1
        if (vehiculoBuscado != null) {                                                  //1
            System.out.println(vehiculoBuscado.toString());                             //1
        } else {
            System.out.println("Vehiculo con placa: " + placaABuscar + ", no existe."); //1
            return;                                                                     //1
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

    public PriorityQueueI getArbolIntersecciones() {
        return arbolIntersecciones;
    }

    public Stack<String> getRegistroEventos() {
        return registroEventos;
    }

    public HashTable getTablaVehiculos() {
        return tablaVehiculos;
    }

    public LinkedList<Vehiculo> getListaDuplicados() {
        return listaDuplicados;
    }

    public LinkedList<Vehiculo> getListaGeneralVehiculos() {
        return listaGeneralVehiculos;
    }

}
