package com.mycompany.sistema_de_trafico.util;

import com.mycompany.sistema_de_trafico.edd.LinkedList;
import com.mycompany.sistema_de_trafico.edd.Node;
import com.mycompany.sistema_de_trafico.objects.Vehiculo;

public class MetodoDeLaSacudida {

    public void ordenarPorTiempoDeEspera(LinkedList<Vehiculo> lista) {
        // bandera para finalizar proceso
        boolean ordenado;
        // limite inferior
        Node<Vehiculo> primero = lista.getHead();
        // limite superior
        Node<Vehiculo> ultimo = null;

        do {
            ordenado = true;
            Node<Vehiculo> actual = primero;

            // recorriendo de izquierda a derecha
            while (actual.getNext() != ultimo) {
                if (actual.getData().getTiempoDeEspera() < actual.getNext().getData().getTiempoDeEspera()) {
                    // si el actual es menor que el siguiente, se intercambian los datos del nodo
                    Vehiculo tmp = actual.getData();
                    actual.setData(actual.getNext().getData());
                    actual.getNext().setData(tmp);
                    // se cambia la bandera para seguir iterando
                    ordenado = false;
                }
                actual = actual.getNext();
            }
            // actualizando limite superior
            ultimo = actual;

            if (ordenado) {
                break;
            }

            ordenado = true;

            // recorriendo de derecha a izquierda
            actual = ultimo;
            while (actual != primero && actual.getPrev() != null) {
                if (actual.getPrev().getData().getTiempoDeEspera() < actual.getData().getTiempoDeEspera()) {
                    // si el anterior es menor que le actual, se intercambian los datos del nodo
                    Vehiculo tmp = actual.getPrev().getData();
                    actual.getPrev().setData(actual.getData());
                    actual.setData(tmp);
                    ordenado = false;
                }
                actual = actual.getPrev();
            }
            // actualizando limite inferior
            primero = actual;

        } while (!ordenado);
    }
}
