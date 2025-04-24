package com.mycompany.sistema_de_trafico.objects;

import java.util.PriorityQueue;

public class Interseccion {
    int complejidad;
    String nombre;
    char representacion;
    PriorityQueue colaPrioritaria;

    public Interseccion(int complejidad, String nombre, char representacion) {
        this.complejidad = complejidad;
        this.nombre = nombre;
        this.representacion = representacion;
        this.colaPrioritaria = new PriorityQueue<Vehiculo>();
    }

    public Interseccion() {
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

    public char getRepresentacion() {
        return representacion;
    }

    public void setRepresentacion(char representacion) {
        this.representacion = representacion;
    }

    public PriorityQueue getColaPrioritaria() {
        return colaPrioritaria;
    }

    public void setColaPrioritaria(PriorityQueue colaPrioritaria) {
        this.colaPrioritaria = colaPrioritaria;
    }

}
