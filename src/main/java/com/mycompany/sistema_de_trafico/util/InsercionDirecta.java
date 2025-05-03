package com.mycompany.sistema_de_trafico.util;

import com.mycompany.sistema_de_trafico.edd.LinkedList;
import com.mycompany.sistema_de_trafico.edd.Node;
import com.mycompany.sistema_de_trafico.objects.Vehiculo;

public class InsercionDirecta {

    public LinkedList<Vehiculo> ordenarPorPrioridad(LinkedList<Vehiculo> listaDesordenada) {
        LinkedList<Vehiculo> listaOrdenada = new LinkedList<>();        //1

        Node<Vehiculo> actual = listaDesordenada.getHead();             //1
        while (actual != null) {                                        //n
            Vehiculo vehiculoActual = actual.getData();                 //n
            insertarVehiculo(listaOrdenada, vehiculoActual);            //n
            actual = actual.getNext();                                  //n
        }
        return listaOrdenada;                                           //n
    }

    private void insertarVehiculo(LinkedList<Vehiculo> lista, Vehiculo vehiculo) {
        Node<Vehiculo> nuevoNodo = new Node<>(vehiculo);        //1

        if (lista.isEmpty()) {                                  //1
            // si la lista esta vacia se inserta al inicio
            lista.add(vehiculo);                                //1
            return;
        }

        Node<Vehiculo> actual = lista.getHead();                //1
        Node<Vehiculo> anterior = null;                         //1

        // se busca la poscion para insertar
        while (actual != null && actual.getData().getPrioridad() >= vehiculo.getPrioridad()) {
            anterior = actual;                                  //n
            actual = actual.getNext();                          //n
        }

        // si no hay un anterior se inserta al inicio
        if (anterior == null) {                                 //1
            nuevoNodo.setNext(lista.getHead());                 //1
            if (lista.getHead() != null) {                      //1
                lista.getHead().setPrev(nuevoNodo);             //1
            }
            lista.setHead(nuevoNodo);                           //1
        } else {
            // si hay anterior se inserta en el medio o final de la lista
            nuevoNodo.setNext(actual);                          //1
            nuevoNodo.setPrev(anterior);                        //1
            anterior.setNext(nuevoNodo);                        //1

            if (actual != null) {                               //1
                actual.setPrev(nuevoNodo);                      //1
            }
        }
        lista.setSize(lista.getSize() + 1);                     //1
    }
}
