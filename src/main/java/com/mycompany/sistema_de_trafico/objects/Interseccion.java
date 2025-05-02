package com.mycompany.sistema_de_trafico.objects;

import com.mycompany.sistema_de_trafico.edd.Node;
import com.mycompany.sistema_de_trafico.edd.PriorityQueueV;
import com.mycompany.sistema_de_trafico.enums.Direccion;
import com.mycompany.sistema_de_trafico.enums.TipoInterseccion;

public class Interseccion {

    int complejidad;
    String nombre;
    boolean bloqueda;
    PriorityQueueV colaNorte;
    PriorityQueueV colaSur;
    PriorityQueueV colaEste;
    PriorityQueueV colaOeste;
    char representacionConsola;
    TipoInterseccion tipoInterseccion;
    int vehiculosCirculados;

    public Interseccion(String nombre, TipoInterseccion tipoInterseccion) {
        this.complejidad = 0;
        this.nombre = nombre;
        this.bloqueda = false;
        this.vehiculosCirculados = 0;
        this.tipoInterseccion = tipoInterseccion;

        this.crearColasInterseccion();

    }

    public Interseccion() {
    }

    private void crearColasInterseccion() {
        switch (this.tipoInterseccion) {
            case CRUCELVOLTEADAIZQUIERDA:
                this.colaSur = new PriorityQueueV();
                this.colaEste = new PriorityQueueV();
                this.representacionConsola = 'L';
                break;

            case CRUCET:
                this.colaOeste = new PriorityQueueV();
                this.colaSur = new PriorityQueueV();
                this.colaEste = new PriorityQueueV();
                this.representacionConsola = 'T';
                break;

            case CRUCELOPUESTA:
                this.colaOeste = new PriorityQueueV();
                this.colaSur = new PriorityQueueV();
                this.representacionConsola = 'L';
                break;

            case CRUCETVOLTEADAIZQUIERDA:
                this.colaNorte = new PriorityQueueV();
                this.colaSur = new PriorityQueueV();
                this.colaEste = new PriorityQueueV();
                this.representacionConsola = 'T';
                break;

            case CRUCETVOLTEADADERECHA:
                this.colaNorte = new PriorityQueueV();
                this.colaOeste = new PriorityQueueV();
                this.colaSur = new PriorityQueueV();
                this.representacionConsola = 'T';
                break;

            case CRUCEL:
                this.colaNorte = new PriorityQueueV();
                this.colaEste = new PriorityQueueV();
                this.representacionConsola = 'L';
                break;

            case CRUCETOPUESTA:
                this.colaOeste = new PriorityQueueV();
                this.colaEste = new PriorityQueueV();
                this.colaNorte = new PriorityQueueV();
                this.representacionConsola = 'T';
                break;

            case CRUCELVOLTEADADERECHA:
                this.colaOeste = new PriorityQueueV();
                this.colaNorte = new PriorityQueueV();
                this.representacionConsola = 'L';
                break;

            case CRUCEMAS:
                this.colaEste = new PriorityQueueV();
                this.colaOeste = new PriorityQueueV();
                this.colaNorte = new PriorityQueueV();
                this.colaSur = new PriorityQueueV();
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

    private void calcularComplejidadCola(PriorityQueueV colaActual) {
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

    public PriorityQueueV getColaPorDireccion(Direccion direccion) {
        switch (direccion) {
            case NORTE:
                return colaNorte;

            case SUR:
                return colaSur;

            case ESTE:
                return colaEste;

            case OESTE:
                return colaOeste;
            default:
                return null;
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

    public PriorityQueueV getColaNorte() {
        return colaNorte;
    }

    public PriorityQueueV getColaSur() {
        return colaSur;
    }

    public PriorityQueueV getColaEste() {
        return colaEste;
    }

    public PriorityQueueV getColaOeste() {
        return colaOeste;
    }

    public int getVehiculosCirculados() {
        return vehiculosCirculados;
    }

    public void setVehiculosCirculados(int vehiculosCirculados) {
        this.vehiculosCirculados = vehiculosCirculados;
    }

}
