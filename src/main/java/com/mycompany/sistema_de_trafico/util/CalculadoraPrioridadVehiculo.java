package com.mycompany.sistema_de_trafico.util;

import com.mycompany.sistema_de_trafico.enums.TipoVehiculo;
import com.mycompany.sistema_de_trafico.objects.Vehiculo;

public class CalculadoraPrioridadVehiculo {

    public static int comparar(Vehiculo v1, Vehiculo v2) {
        // comparando tipo de vehiculo
        int valorTipo1 = valorTipo(v1.getTipo());
        int valorTipo2 = valorTipo(v2.getTipo());

        if (valorTipo1 != valorTipo2) {
            return valorTipo1 - valorTipo2;
        }

        // comparando tipo de prioridad
        if (v1.getPrioridad() != v2.getPrioridad()) {
            return v1.getPrioridad() - v2.getPrioridad();
        }

        // comparando tiempo de espera
        if (v1.getTiempoDeEspera() != v2.getTiempoDeEspera()) {
            return v1.getTiempoDeEspera() - v2.getTiempoDeEspera();
        }
        int tiempoDeEspera1 = v1.getTiempoDeEspera();
        int tiempoDeEspera2 = v2.getTiempoDeEspera();

        return tiempoDeEspera1 - tiempoDeEspera2;
    }

    private static int valorTipo(TipoVehiculo tipo) {
        switch (tipo) {
            case AMBULANCIA:
                return 4;
            case POLICIA:
                return 3;
            case TRANSPORTE:
                return 2;
            case PARTICULAR:
                return 1;
            default:
                return 0;
        }
    }
}
