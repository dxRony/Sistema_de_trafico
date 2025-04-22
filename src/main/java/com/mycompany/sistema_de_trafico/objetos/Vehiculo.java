package com.mycompany.sistema_de_trafico.objetos;

import com.mycompany.sistema_de_trafico.enums.TipoVehiculo;

public class Vehiculo {
    private TipoVehiculo tipo;
    private String placa;
    private String interseccionOrigen;
    private String interseccionDestino;
    private int prioridad;
    private int tiempoDeEspera;

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

}
