package com.mycompany.sistema_de_trafico.util;

import com.mycompany.sistema_de_trafico.edd.LinkedList;
import com.mycompany.sistema_de_trafico.edd.Node;
import com.mycompany.sistema_de_trafico.objects.Vehiculo;

public class InsercionDirecta {

    public LinkedList<Vehiculo> ordenarPorPrioridad(LinkedList<Vehiculo> listaDesordenada) {
        LinkedList<Vehiculo> listaOrdenada = new LinkedList<>();

        Node<Vehiculo> actual = listaDesordenada.getHead();
        while (actual != null) {
            Vehiculo vehiculoActual = actual.getData();
            insertarVehiculo(listaOrdenada, vehiculoActual);
            actual = actual.getNext();
        }
        return listaOrdenada;
    }

    private void insertarVehiculo(LinkedList<Vehiculo> lista, Vehiculo vehiculo) {
        Node<Vehiculo> nuevoNodo = new Node<>(vehiculo);

        if (lista.isEmpty()) {
            // si la lista esta vacia se inserta al inicio
            lista.add(vehiculo);
            return;
        }

        Node<Vehiculo> actual = lista.getHead();
        Node<Vehiculo> anterior = null;

        // se busca la poscion para insertar
        while (actual != null && actual.getData().getPrioridad() >= vehiculo.getPrioridad()) {
            anterior = actual;
            actual = actual.getNext();
        }

        // si no hay un anterior se inserta al inicio
        if (anterior == null) {
            nuevoNodo.setNext(lista.getHead());
            if (lista.getHead() != null) {
                lista.getHead().setPrev(nuevoNodo);
            }
            lista.setHead(nuevoNodo);
        } else {
            // si hay anterior se inserta en el medio o final de la lista
            nuevoNodo.setNext(actual);
            nuevoNodo.setPrev(anterior);
            anterior.setNext(nuevoNodo);

            if (actual != null) {
                actual.setPrev(nuevoNodo);
            }
        }
        lista.setSize(lista.getSize() + 1);
    }
}
