package com.mycompany.sistema_de_trafico.objects;

import com.mycompany.sistema_de_trafico.enums.Direccion;
import com.mycompany.sistema_de_trafico.enums.TipoVehiculo;

public class Vehiculo {
    private TipoVehiculo tipo;
    private String placa;
    private String interseccionOrigen;
    private String interseccionDestino;
    private int prioridad;
    private int tiempoDeEspera;
    private Direccion direccion;
    
    public Vehiculo(TipoVehiculo tipo, String placa, String interseccionOrigen, String interseccionDestino,
            int prioridad, int tiempoDeEspera, Direccion direccion) {
        this.tipo = tipo;
        this.placa = placa;
        this.interseccionOrigen = interseccionOrigen;
        this.interseccionDestino = interseccionDestino;
        this.prioridad = prioridad;
        this.tiempoDeEspera = tiempoDeEspera;
        this.direccion = direccion;
    }
    
    
    public Vehiculo() {
    }


    public TipoVehiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVehiculo tipo) {
        this.tipo = tipo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getInterseccionOrigen() {
        return interseccionOrigen;
    }

    public void setInterseccionOrigen(String interseccionOrigen) {
        this.interseccionOrigen = interseccionOrigen;
    }

    public String getInterseccionDestino() {
        return interseccionDestino;
    }

    public void setInterseccionDestino(String interseccionDestino) {
        this.interseccionDestino = interseccionDestino;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public int getTiempoDeEspera() {
        return tiempoDeEspera;
    }

    public void setTiempoDeEspera(int tiempoDeEspera) {
        this.tiempoDeEspera = tiempoDeEspera;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
    
}
