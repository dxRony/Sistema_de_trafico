package com.mycompany.sistema_de_trafico.objects;

import com.mycompany.sistema_de_trafico.enums.TipoVehiculo;

public class Vehiculo {
    private TipoVehiculo tipo;
    private String placa;
    private String interseccionOrigen;
    private String interseccionDestino;
    private int prioridad;
    private int tiempoDeEspera;

    public Vehiculo(TipoVehiculo tipo, String placa, String interseccionOrigen, String interseccionDestino,
            int prioridad, int tiempoDeEspera) {
        this.tipo = tipo;
        this.placa = placa;
        this.interseccionOrigen = interseccionOrigen;
        this.interseccionDestino = interseccionDestino;
        this.prioridad = prioridad;
        this.tiempoDeEspera = tiempoDeEspera;
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

    @Override
    public String toString() {
        return "Vehiculo [tipo=" + tipo + ", placa=" + placa + ", interseccionOrigen=" + interseccionOrigen
                + ", interseccionDestino=" + interseccionDestino + ", prioridad=" + prioridad + ", tiempoDeEspera="
                + tiempoDeEspera + "]";
    }

}
