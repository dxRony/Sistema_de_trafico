package com.mycompany.sistema_de_trafico.edd;

import com.mycompany.sistema_de_trafico.objects.Vehiculo;
import com.mycompany.sistema_de_trafico.util.CalculadoraPrioridadVehiculo;

public class PriorityQueue {
    private Node<Vehiculo> head;

    public void insertar(Vehiculo vehiculo) {
        Node<Vehiculo> nuevo = new Node<>(vehiculo);

        if (head == null || CalculadoraPrioridadVehiculo.comparar(vehiculo, head.getData()) > 0) {
            nuevo.setNext(head);
            if (head != null) {
                head.setPrev(nuevo);
            }
            head = nuevo;

        } else {
            Node<Vehiculo> actual = head;
            while (actual.getNext() != null && CalculadoraPrioridadVehiculo.comparar(
                    vehiculo, actual.getNext().getData()) <= 0) {
                actual = actual.getNext();
            }

            nuevo.setNext(actual.getNext());
            if (actual.getNext() != null) {
                actual.getNext().setPrev(nuevo);
            }
            actual.setNext(nuevo);
            nuevo.setPrev(actual);
        }
    }

    public Vehiculo desencolar() {
        if (head == null) {
            return null;
        }

        Vehiculo vehiculo = head.getData();
        head = head.getNext();
        if (head != null) {
            head.setPrev(null);
        }

        return vehiculo;
    }

    public void imprimir() {
        Node<Vehiculo> actual = head;
        while (actual != null) {
            System.out.print(actual.getData() + " -> ");
            actual = actual.getNext();
        }
        System.out.println();
    }
}
