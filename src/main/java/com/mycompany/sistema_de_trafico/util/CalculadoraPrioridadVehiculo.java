package com.mycompany.sistema_de_trafico.util;

import com.mycompany.sistema_de_trafico.enums.Direccion;
import com.mycompany.sistema_de_trafico.enums.TipoVehiculo;
import com.mycompany.sistema_de_trafico.objects.Vehiculo;

public class CalculadoraPrioridadVehiculo {
    public static int comparar(Vehiculo v1, Vehiculo v2) {
        //comparando tipo de vehiculo
        int valorTipo1 = valorTipo(v1.getTipo());
        int valorTipo2 = valorTipo(v2.getTipo());        

        if (valorTipo1 != valorTipo2) {            
            return valorTipo2 - valorTipo1;
        }

        //comparando tipo de prioridad
        if (v1.getPrioridad() != v2.getPrioridad()) {
            return v2.getPrioridad() - v1.getPrioridad();
        }

        //comparando tiempo de espera
        if (v1.getTiempoDeEspera() != v2.getTiempoDeEspera()) {
            return v2.getTiempoDeEspera() - v1.getTiempoDeEspera();
        }

        //comparando direccion
        int valorDireccion1 = valorDireccion(v1.getDireccion());
        int valorDireccion2 = valorDireccion(v2.getDireccion());        
        return valorDireccion2 - valorDireccion1;
    }

    private static int valorTipo(TipoVehiculo tipo) {
        switch (tipo) {
            case AMBULANCIA:
                return 4;
            case POLICIA:
                return 3;
            case TRANSPORTE_PUBLICO:
                return 2;
            case PARTICULAR:
                return 1;
            default:
                return 0;
        }
    }

    private static int valorDireccion(Direccion direccion) {
        switch (direccion) {
            case NORTE:
                return 4;
            case SUR:
                return 3;
            case ESTE:
                return 2;
            case OESTE:
                return 1;
            default:
                return 0;
        }
    }
}
