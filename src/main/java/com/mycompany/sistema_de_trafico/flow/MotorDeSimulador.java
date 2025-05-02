package com.mycompany.sistema_de_trafico.flow;

import java.util.Scanner;

import com.mycompany.sistema_de_trafico.edd.LinkedList;
import com.mycompany.sistema_de_trafico.objects.Vehiculo;
import com.mycompany.sistema_de_trafico.util.Archivo;

public class MotorDeSimulador {
    private boolean finalizarEjecucion = false;
    Reporte reporte = new Reporte();

    public void mostrarMenu() {

        Scanner scanner = new Scanner(System.in);

        while (!finalizarEjecucion) {
            System.out.println("   BIENVENIDO AL SIMULADOR DE TRAFICO   ");
            System.out.println("*************Menu Principal*************");
            System.out.println("Elije una opcion");
            System.out.println("1. Empezar simulacion");
            System.out.println("2. Ver reportes");
            System.out.println("3. Salir");
            int opcionMenuPrincipal = scanner.nextInt();

            switch (opcionMenuPrincipal) {
                case 1:
                    this.empezarSimulacion();
                    break;

                case 2:
                    this.verReportes();
                    break;

                case 3:
                    finalizarEjecucion = true;
                    System.out.println("Finalizando ejecucion...");
                    System.out.println("Bye");
                    break;

                default:
                    System.out.println("Opcion incorrecta");
                    break;
            }
        }
        scanner.close();
    }

    private void empezarSimulacion() {
        Scanner scanner = new Scanner(System.in);
        Archivo archivo = new Archivo();

        System.out.println("Ingresa la ruta del archivo donde estan los vehiculos y su informacion...");
        String rutaArchivo = scanner.nextLine();

        LinkedList<Vehiculo> listaVehiculos = archivo.leerArchivo(rutaArchivo);
        System.out.print("Ingresa el ancho de tu ciudad: ");
        int ancho = scanner.nextInt();
        System.out.print("Ingresa el alto de tu ciudad: ");
        int alto = scanner.nextInt();
        System.out.print("Ingresa el tamaño de la tabla Hash: ");
        int tamañoTablaHash = scanner.nextInt();
        System.out.println("\nINICIANDO SIMULADOR");
        Simulador simulador = new Simulador(listaVehiculos, ancho, alto, tamañoTablaHash);
        simulador.iniciarSimulador();
        System.out.println("Simulacion terminada.\nGenerando reportes...");
        // cuando termina la simulacion se recolectan los datos a los reportes
        reporte.setRegistroUltimosEventos(simulador.getRegistroEventos());
        reporte.setListaVehiculos(simulador.getListaGeneralVehiculos());
        reporte.setVehiculosDuplicados(simulador.getListaDuplicados());
        reporte.setIntersecciones(simulador.getArbolIntersecciones());
    }

    private void verReportes() {
        System.out.println("Mostrando reportes...");
        reporte.mostrarOpcionesReportes();
    }
}
