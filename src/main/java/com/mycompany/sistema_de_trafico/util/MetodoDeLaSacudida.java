package com.mycompany.sistema_de_trafico.util;

import com.mycompany.sistema_de_trafico.edd.LinkedList;
import com.mycompany.sistema_de_trafico.edd.Node;
import com.mycompany.sistema_de_trafico.objects.Vehiculo;

public class MetodoDeLaSacudida {

    public void ordenarPorTiempoDeEspera(LinkedList<Vehiculo> lista) {
        // bandera para finalizar proceso                   
        boolean ordenado;                                           //1
        // limite inferior
        Node<Vehiculo> primero = lista.getHead();                   //1
        // limite superior
        Node<Vehiculo> ultimo = null;                               //1
        do {
            ordenado = true;                                        //n
            Node<Vehiculo> actual = primero;                        //n
            // recorriendo de izquierda a derecha
            while (actual.getNext() != ultimo) {                    //n^2
                if (actual.getData().getTiempoDeEspera() < actual.getNext().getData().getTiempoDeEspera()) {
                    // si el actual es menor que el siguiente, se intercambian los datos del nodo
                    Vehiculo tmp = actual.getData();                //n^2
                    actual.setData(actual.getNext().getData());     //n^2
                    actual.getNext().setData(tmp);                  //n^2
                    // se cambia la bandera para seguir iterando
                    ordenado = false;                               //n^2
                }
                actual = actual.getNext();                          //n^2
            }
            // actualizando limite superior
            ultimo = actual;                                        //n
            if (ordenado) {                                         //n
                break;                                              //n
            }
            ordenado = true;                                        //n
            // recorriendo de derecha a izquierda
            actual = ultimo;                                        //n
            while (actual != primero && actual.getPrev() != null) { 
                if (actual.getPrev().getData().getTiempoDeEspera() < actual.getData().getTiempoDeEspera()) {
                    // si el anterior es menor que le actual, se intercambian los datos del nodo
                    Vehiculo tmp = actual.getPrev().getData();      //n^2
                    actual.getPrev().setData(actual.getData());     //n^2
                    actual.setData(tmp);                            //n^2
                    ordenado = false;                               //n^2
                }
                actual = actual.getPrev();                          //n^2
            }
            // actualizando limite inferior
            primero = actual;                                       //n
        } while (!ordenado);                                        //n
    }
}
