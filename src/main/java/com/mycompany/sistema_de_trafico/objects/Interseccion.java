package com.mycompany.sistema_de_trafico.objects;

import com.mycompany.sistema_de_trafico.edd.Node;
import com.mycompany.sistema_de_trafico.edd.PriorityQueue;
import com.mycompany.sistema_de_trafico.enums.TipoInterseccion;

public class Interseccion {

    int complejidad;
    String nombre;
    boolean bloqueda;
    PriorityQueue colaNorte;
    PriorityQueue colaSur;
    PriorityQueue colaEste;
    PriorityQueue colaOeste;
    char representacionConsola;
    TipoInterseccion tipoInterseccion;

    public Interseccion(String nombre, TipoInterseccion tipoInterseccion) {
        this.complejidad = 0;
        this.nombre = nombre;
        this.bloqueda = false;
        this.tipoInterseccion = tipoInterseccion;

        this.crearColasInterseccion();

    }

    public Interseccion() {
    }

    private void crearColasInterseccion() {
        switch (this.tipoInterseccion) {
            case CRUCELVOLTEADAIZQUIERDA:
                this.colaSur = new PriorityQueue();
                this.colaEste = new PriorityQueue();
                this.representacionConsola = 'L';
                break;

            case CRUCET:
                this.colaOeste = new PriorityQueue();
                this.colaSur = new PriorityQueue();
                this.colaEste = new PriorityQueue();
                this.representacionConsola = 'T';
                break;

            case CRUCELOPUESTA:
                this.colaOeste = new PriorityQueue();
                this.colaSur = new PriorityQueue();
                this.representacionConsola = 'L';
                break;

            case CRUCETVOLTEADAIZQUIERDA:
                this.colaNorte = new PriorityQueue();
                this.colaSur = new PriorityQueue();
                this.colaEste = new PriorityQueue();
                this.representacionConsola = 'T';
                break;

            case CRUCETVOLTEADADERECHA:
                this.colaNorte = new PriorityQueue();
                this.colaOeste = new PriorityQueue();
                this.colaSur = new PriorityQueue();
                this.representacionConsola = 'T';
                break;

            case CRUCEL:
                this.colaNorte = new PriorityQueue();
                this.colaEste = new PriorityQueue();
                this.representacionConsola = 'L';
                break;

            case CRUCETOPUESTA:
                this.colaOeste = new PriorityQueue();
                this.colaEste = new PriorityQueue();
                this.colaNorte = new PriorityQueue();
                this.representacionConsola = 'T';
                break;

            case CRUCELVOLTEADADERECHA:
                this.colaOeste = new PriorityQueue();
                this.colaNorte = new PriorityQueue();
                this.representacionConsola = 'L';
                break;

            case CRUCEMAS:
                this.colaEste = new PriorityQueue();
                this.colaOeste = new PriorityQueue();
                this.colaNorte = new PriorityQueue();
                this.colaSur = new PriorityQueue();
                this.representacionConsola = '+';
                break;
            default:
                System.out.println("Syntax error");
                break;
        }
    }

    public void calcularComplejidad() {
        // limpiando valor antes de calcular complejidad
        this.complejidad = 0;
        if (this.colaNorte != null) {
            calcularComplejidadCola(colaNorte);
        }
        if (this.colaSur != null) {
            calcularComplejidadCola(colaSur);
        }
        if (this.colaEste != null) {
            calcularComplejidadCola(colaEste);
        }
        if (this.colaOeste != null) {
            calcularComplejidadCola(colaOeste);
        }
    }

    public void calcularComplejidadCola(PriorityQueue colaActual) {
        Node<Vehiculo> nodoActual = colaActual.getHead();

        while (nodoActual != null) {
            // visitando vehiculo - calculando complejidad de vehiculo - avanzando a
            // siguiente
            Vehiculo vehiculoActual = nodoActual.getData();
            // agregando complejidad por el tipo
            switch (vehiculoActual.getTipo()) {
                case AMBULANCIA:
                    complejidad += 15;
                    break;
                case POLICIA:
                    complejidad += 10;
                    break;
                case TRANSPORTE:
                    complejidad += 7;
                    break;
                case PARTICULAR:
                    complejidad += 4;
                    break;
            }
            // agregando complejidad por el tiempo de espera
            complejidad += vehiculoActual.getTiempoDeEspera() * 2;
            // agregnado complejidad por la prioridad
            complejidad += vehiculoActual.getPrioridad() * 2;
            nodoActual = nodoActual.getNext();
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

    public PriorityQueue getColaNorte() {
        return colaNorte;
    }

    public PriorityQueue getColaSur() {
        return colaSur;
    }

    public PriorityQueue getColaEste() {
        return colaEste;
    }

    public PriorityQueue getColaOeste() {
        return colaOeste;
    }

}
