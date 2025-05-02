package com.mycompany.sistema_de_trafico.flow;

import java.util.Scanner;

import com.mycompany.sistema_de_trafico.edd.LinkedList;
import com.mycompany.sistema_de_trafico.edd.Stack;
import com.mycompany.sistema_de_trafico.objects.Vehiculo;
import com.mycompany.sistema_de_trafico.util.InsercionDirecta;
import com.mycompany.sistema_de_trafico.util.MetodoDeLaSacudida;

public class Reporte {

    private LinkedList<Vehiculo> listaVehiculos;
    private LinkedList<Vehiculo> vehiculosDuplicados;
    private Stack<String> registroUltimosEventos;
    private InsercionDirecta insercionDirecta;
    private MetodoDeLaSacudida metodoDeLaSacudida;

    public Reporte() {
        listaVehiculos = new LinkedList<>();
        vehiculosDuplicados = new LinkedList<>();
        registroUltimosEventos = new Stack<>();
        insercionDirecta = new InsercionDirecta();
        metodoDeLaSacudida = new MetodoDeLaSacudida();
    }

    public void mostrarOpcionesReportes() {
        Scanner sn = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("Selecciona el reporte que deseas ver...");
            System.out.println("1. Ver ranking de vehiculos.");
            System.out.println("2. Ver cantidad de vehiculos que pasaron por cada interseccion.");
            System.out.println("3. Tiempo promedio por tipo de vehiculo.");
            System.out.println("4. Lista de vehiculos duplicados.");
            System.out.println("5. Registro de ultimos 20 eventos.");
            System.out.println("6. Regresar a menu principal.");
            opcion = sn.nextInt();

            switch (opcion) {
                case 1:
                    mostrarOpcionesRanking();
                    break;
                case 2:
                    verVehiculosCirculadosPorInterseccion();
                    break;
                case 3:
                    verTiempoPromedioPorTipoDeVehiculo();
                    break;
                case 4:
                    verListaVehiculosDuplicados();
                    break;
                case 5:
                    verRegistroUltimosEventos();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Opcion invalida, intenta de nuevo");
                    break;
            }
        } while (opcion != 6);

    }

    private void mostrarOpcionesRanking() {

        int opcion;
        Scanner sn = new Scanner(System.in);
        do {
            System.out.println("Selecciona como quieres ver el ranking de vehiculos...");
            System.out.println("1. Ver vehiculos por prioridad (usando insercion directa).");
            System.out.println("2. Ver vehiculos ordenados por tiempo (usando metodo de la sacudida).");
            System.out.println("3. Regresar al menu anterior.");
            opcion = sn.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("Mostrando vehiculos ordenados por prioridad...");
                    mostrarVehiculosPrioridad();
                    break;
                case 2:
                    System.out.println("Mostrando vehiculos ordenados por tiempo...");
                    mostrarVehiculosTiempo();
                    break;
                case 3:
                    return;

                default:
                    break;
            }
        } while (opcion != 3);
    }

    private void mostrarVehiculosPrioridad() {
        LinkedList<Vehiculo> lista = insercionDirecta.ordenarPorPrioridad(listaVehiculos);
        lista.imprimir();
    }

    private void mostrarVehiculosTiempo() {
        metodoDeLaSacudida.ordenarPorTiempoDeEspera(listaVehiculos);
        listaVehiculos.imprimir();
    }

    private void verVehiculosCirculadosPorInterseccion() {
        System.out.println("Mostrando vehiculos circulados por interseccion...");
        
    }

    private void verTiempoPromedioPorTipoDeVehiculo() {
        System.out.println("Mostrando tiempo promedio por tipo de vehiculo...");

    }

    private void verListaVehiculosDuplicados() {
        System.out.println("Mostrando lista de vehiculos duplicados...");
        vehiculosDuplicados.imprimir();
    }

    private void verRegistroUltimosEventos() {
        System.out.println("Mostrando ultimos 20 eventos...");
        registroUltimosEventos.imprimirUltimosEventos();
    }

    public LinkedList<Vehiculo> getListaVehiculos() {
        return listaVehiculos;
    }

    public void setListaVehiculos(LinkedList<Vehiculo> listaVehiculos) {
        this.listaVehiculos = listaVehiculos;
    }

    public LinkedList<Vehiculo> getVehiculosDuplicados() {
        return vehiculosDuplicados;
    }

    public void setVehiculosDuplicados(LinkedList<Vehiculo> vehiculosDuplicados) {
        this.vehiculosDuplicados = vehiculosDuplicados;
    }

    public Stack<String> getRegistroUltimosEventos() {
        return registroUltimosEventos;
    }

    public void setRegistroUltimosEventos(Stack<String> registroUltimosEventos) {
        this.registroUltimosEventos = registroUltimosEventos;
    }

}
