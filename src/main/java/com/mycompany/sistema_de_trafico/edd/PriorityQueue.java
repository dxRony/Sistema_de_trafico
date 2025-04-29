package com.mycompany.sistema_de_trafico.edd;

import com.mycompany.sistema_de_trafico.objects.Vehiculo;
import com.mycompany.sistema_de_trafico.util.CalculadoraPrioridadVehiculo;

public class PriorityQueue {
    private Node<Vehiculo> head;
    private int size;

    public PriorityQueue() {
        this.head = null;
        this.size = 0;
    }

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
        size++;
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
        size--;
        return vehiculo;
    }

    public void imprimir() {
        if (estaVacia()) {
            System.out.println("Cola de prioridad vacía\n");
            return;
        }

        System.out.println("Mostrando cola de vehiculos:");

        Node<Vehiculo> actual = head;
        int posicion = 1;
        while (actual != null) {
            Vehiculo v = actual.getData();
            System.out.println(posicion + ". " + v);
            actual = actual.getNext();
            posicion++;
        }
        System.out.println("======================================\n");

    }

    public boolean estaVacia() {
        return head == null;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Node<Vehiculo> getHead() {
        return head;
    }

    public void setHead(Node<Vehiculo> head) {
        this.head = head;
    }

    

}
