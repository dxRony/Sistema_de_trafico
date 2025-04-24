package com.mycompany.sistema_de_trafico.edd;

public class OrthogonalMatrix<T> {

    private Node<T> head;
    private int ancho;
    private int alto;

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

    private Node<T> obtenerNodo(int x, int y) {
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

    public void imprimir(){
        System.out.println("******************************");
        for (int y = alto-1; y >= 0; y--) {
            Node<T> fila = obtenerNodo(0, y);
            for (int x = 0; x < ancho && fila != null; x++) {
                System.out.print(fila.getData()+ " ");
                fila = fila.getRight();
            }
            System.out.println();
        }
        System.out.println("******************************");
    }

}
