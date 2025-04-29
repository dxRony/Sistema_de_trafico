package com.mycompany.sistema_de_trafico.edd;

import com.mycompany.sistema_de_trafico.objects.Interseccion;

public class OrthogonalMatrix<T> {

    private Node<T> head;
    private int ancho;
    private int alto;
    private char[] letrasFilas = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
            'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };;

    public OrthogonalMatrix(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        head = new Node<>(null);
        Node<T> nodoActual = head;

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                if (x == 0 && y == 0) {
                    continue;
                }

                Node<T> nuevoNodo = new Node<>(null);

                if (x > 0) {
                    nuevoNodo.setLeft(nodoActual);
                    nodoActual.setRight(nuevoNodo);
                    nodoActual = nuevoNodo;
                } else {
                    nodoActual = nuevoNodo;
                }

                if (y > 0) {
                    Node<T> nodoAbajo = obtenerNodo(x, y - 1);
                    nuevoNodo.setDown(nodoAbajo);
                    nodoAbajo.setUp(nuevoNodo);
                }
            }
        }

    }

    public Node<T> obtenerNodo(int x, int y) {
        if (x < 0 || y < 0 || x >= ancho || y >= alto) {
            return null;
        }
        Node<T> actual = head;

        for (int i = 0; i < x && actual != null; i++) {
            actual = actual.getRight();
        }
        for (int i = 0; i < y && actual != null; i++) {
            actual = actual.getUp();
        }

        return actual;
    }

    public void insertarDato(int x, int y, T data) {
        Node<T> nodo = obtenerNodo(x, y);

        if (nodo != null) {
            nodo.setData(data);
        } else {
            throw new IndexOutOfBoundsException("Posicion invalida");
        }
    }

    public T obtenerDato(int x, int y) {
        Node<T> nodo = obtenerNodo(x, y);
        if (nodo != null) {
            T dato = nodo.getData();
            return dato;
        } else {
            throw new IndexOutOfBoundsException("Posicion invalida");
        }
    }

    public void imprimir() {
        System.out.println("\nMapa de la ciudad:");
        System.out.println("******************************");
        System.out.print("   ");
        for (int x = 0; x < ancho; x++) {
            System.out.print(" " + (x + 1) + " ");
        }
        System.out.println();
        for (int y = 0; y < alto; y++) {
            System.out.print(letrasFilas[y] + "  ");

            Node<T> fila = obtenerNodo(0, y);
            for (int x = 0; x < ancho && fila != null; x++) {
                Interseccion interseccion = (Interseccion) fila.getData();
                System.out.print(" " + interseccion.getRepresentacionConsola() + " ");
                fila = fila.getRight();
            }
            System.out.println();
        }
        System.out.println("******************************\n");
    }

}
