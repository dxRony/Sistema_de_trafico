package com.mycompany.sistema_de_trafico.edd;

import com.mycompany.sistema_de_trafico.objects.Vehiculo;

public class HashTable {
    // array de tipo lista enlazada
    private LinkedList<Vehiculo>[] tabla;
    // tamaño del array
    private int tamaño;

    // constructor que recibe el tamaño del arreglo
    public HashTable(int tamaño) {

        this.tamaño = tamaño;
        // inicializando array de lista con tamaño
        tabla = new LinkedList[tamaño];
        for (int i = 0; i < tamaño; i++) {
            // recorriendo cada posicion del array e inicializando una lista
            tabla[i] = new LinkedList<>();
        }
    }

    private int funcionHash(String placa) {
        int sumaAscii = 0;
        for (char c : placa.toCharArray()) {
            // sumando todos los valores ascii de la placa
            sumaAscii += c;
        }
        // devolviendo el modulo entre la suma y el tamaño
        return sumaAscii % tamaño;
    }

    public void insertar(Vehiculo vehiculo) {
        // obteniendo el indice del vehiculo
        int indice = funcionHash(vehiculo.getPlaca());
        // agregando al vehiculo a la tabla con el indice que devolvio la funcion hash
        tabla[indice].add(vehiculo);
    }

    public Vehiculo buscar(String placa) {
        // obteniendo el indice del vehiculo a buscar
        int indice = funcionHash(placa);
        // buscando y retornando al vehivulo por la placa
        return tabla[indice].buscarPorPlaca(placa);
    }

    public void imprimir() {
        for (int i = 0; i < tamaño; i++) {
            System.out.print("indice " + i + ": ");
            tabla[i].imprimir();
        }
    }
}
