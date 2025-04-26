package com.mycompany.sistema_de_trafico.flow;

import java.util.Scanner;

public class MotorDeSimulador {

    public void mostrarMenu() {

        Scanner scanner = new Scanner(System.in);

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

                break;

            default:
                System.out.println("Opcion incorrecta");
                break;
        }
        scanner.close();
    }

    private void empezarSimulacion() {

    }

    private void verReportes() {

    }
}
