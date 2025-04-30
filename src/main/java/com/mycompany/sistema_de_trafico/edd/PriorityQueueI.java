package com.mycompany.sistema_de_trafico.edd;

import com.mycompany.sistema_de_trafico.objects.Interseccion;

public class PriorityQueueI {
    private Node<Interseccion> head;
    private int size;

    public PriorityQueueI() {
        this.head = null;
        this.size = 0;
    }

    public void insertar(Interseccion interseccion) {
        Node<Interseccion> nuevo = new Node<>(interseccion);

        if (head == null || compararPorComplejidad(interseccion, head.getData()) > 0) {
            nuevo.setNext(head);
            if (head != null) {
                head.setPrev(nuevo);
            }
            head = nuevo;
        } else {
            Node<Interseccion> actual = head;
            while (actual.getNext() != null &&
                    compararPorComplejidad(interseccion, actual.getNext().getData()) <= 0) {
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

    private int compararPorComplejidad(Interseccion i1, Interseccion i2) {
        return i1.getComplejidad() - i2.getComplejidad();
    }

    public Interseccion desencolar() {
        if (head == null) {
            return null;
        }

        Interseccion interseccion = head.getData();
        head = head.getNext();
        if (head != null) {
            head.setPrev(null);
        }
        size--;
        return interseccion;
    }

    public void actualizarInterseccion(Interseccion interseccion) {
        eliminar(interseccion);
        insertar(interseccion);
    }

    public boolean eliminar(Interseccion interseccion) {
        Node<Interseccion> actual = head;

        while (actual != null) {
            if (actual.getData().equals(interseccion)) {
                if (actual.getPrev() != null) {
                    actual.getPrev().setNext(actual.getNext());
                } else {
                    head = actual.getNext();
                }

                if (actual.getNext() != null) {
                    actual.getNext().setPrev(actual.getPrev());
                }

                size--;
                return true;
            }
            actual = actual.getNext();
        }
        return false;
    }

    public void imprimir() {
        if (estaVacia()) {
            System.out.println("Cola de prioridad vacia\n");
            return;
        }

        System.out.println("Mostrando cola de intersecciones por prioridad:");

        Node<Interseccion> actual = head;
        int posicion = 1;
        while (actual != null) {
            Interseccion i = actual.getData();
            System.out.println(posicion + ". " + i.getNombre() + " - Complejidad: " + i.getComplejidad());
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

    public Node<Interseccion> getHead() {
        return head;
    }
}
