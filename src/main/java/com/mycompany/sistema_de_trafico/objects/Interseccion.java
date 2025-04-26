package com.mycompany.sistema_de_trafico.objects;

import com.mycompany.sistema_de_trafico.enums.TipoInterseccion;
import java.util.PriorityQueue;

public class Interseccion {

    int complejidad;
    String nombre;
    boolean bloqueda;
    PriorityQueue<Vehiculo> colaNorte;
    PriorityQueue<Vehiculo> colaSur;
    PriorityQueue<Vehiculo> colaEste;
    PriorityQueue<Vehiculo> colaOeste;
    char representacionConsola;
    TipoInterseccion tipoInterseccion;

    public Interseccion(int complejidad, String nombre, TipoInterseccion tipoInterseccion) {
        this.complejidad = complejidad;
        this.nombre = nombre;
        this.tipoInterseccion = tipoInterseccion;

        this.crearColasInterseccion();

    }

    public Interseccion() {
    }

    private void crearColasInterseccion() {
        switch (this.tipoInterseccion) {

            case CRUCELVOLTEADAIZQUIERDA:
                this.colaSur = new PriorityQueue<>();
                this.colaEste = new PriorityQueue<>();
                this.representacionConsola = 'L';
                break;

            case CRUCET:
                this.colaOeste = new PriorityQueue<>();
                this.colaSur = new PriorityQueue<>();
                this.colaEste = new PriorityQueue<>();
                this.representacionConsola = 'T';
                break;

            case CRUCELOPUESTA:
                this.colaOeste = new PriorityQueue<>();
                this.colaSur = new PriorityQueue<>();
                this.representacionConsola = 'L';
                break;

            case CRUCETVOLTEADAIZQUIERDA:
                this.colaNorte = new PriorityQueue<>();
                this.colaSur = new PriorityQueue<>();
                this.colaEste = new PriorityQueue<>();
                this.representacionConsola = 'T';
                break;

            case CRUCETVOLTEADADRECHA:
                this.colaNorte = new PriorityQueue<>();
                this.colaOeste = new PriorityQueue<>();
                this.colaSur = new PriorityQueue<>();
                this.representacionConsola = 'T';
                break;

            case CRUCEL:
                this.colaNorte = new PriorityQueue<>();
                this.colaEste = new PriorityQueue<>();
                this.representacionConsola = 'L';
                break;

            case CRUCETOPUESTA:
                this.colaOeste = new PriorityQueue<>();
                this.colaEste = new PriorityQueue<>();
                this.colaNorte = new PriorityQueue<>();
                this.representacionConsola = 'T';
                break;

            case CRUCELVOLTEADADERECHA:
                this.colaOeste = new PriorityQueue<>();
                this.colaNorte = new PriorityQueue<>();
                this.representacionConsola = 'L';
                break;

            case CRUCEMAS:
                this.colaEste = new PriorityQueue<>();
                this.colaOeste = new PriorityQueue<>();
                this.colaNorte = new PriorityQueue<>();
                this.colaSur = new PriorityQueue<>();
                this.representacionConsola = '+';
                break;

            default:
                System.out.println("Syntax error");
                break;
        }
    }

    public int getComplejidad() {
        return complejidad;
    }

    public void setComplejidad(int complejidad) {
        this.complejidad = complejidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isBloqueda() {
        return bloqueda;
    }

    public void setBloqueda(boolean bloqueda) {
        this.bloqueda = bloqueda;
    }

    public char getRepresentacionConsola() {
        return representacionConsola;
    }

    public void setRepresentacionConsola(char representacionConsola) {
        this.representacionConsola = representacionConsola;
    }

    public TipoInterseccion getTipoInterseccion() {
        return tipoInterseccion;
    }

    public void setTipoInterseccion(TipoInterseccion tipoInterseccion) {
        this.tipoInterseccion = tipoInterseccion;
    }

}
