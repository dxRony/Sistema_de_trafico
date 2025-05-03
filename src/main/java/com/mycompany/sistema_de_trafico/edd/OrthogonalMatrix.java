package com.mycompany.sistema_de_trafico.edd;

import com.mycompany.sistema_de_trafico.objects.Interseccion;

public class OrthogonalMatrix<T> {

    private Node<T> head;
    private int ancho;
    private int alto;
    private char[] letrasFilas = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
            'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };;

    public OrthogonalMatrix(int ancho, int alto) {
        this.ancho = ancho;                                 //1
        this.alto = alto;                                   //1
        head = new Node<>(null);                       //1
        Node<T> nodoActual = head;                          //1

        for (int y = 0; y < alto; y++) {                    //n
            for (int x = 0; x < ancho; x++) {               //n^2
                if (x == 0 && y == 0) {                     //n^2
                    continue;                               //n^2
                }
                Node<T> nuevoNodo = new Node<>(null);  //n^2
                if (x > 0) {                                //n^2
                    nuevoNodo.setLeft(nodoActual);          //n^2
                    nodoActual.setRight(nuevoNodo);         //n^2
                    nodoActual = nuevoNodo;                 //n^2
                } else {
                    nodoActual = nuevoNodo;                 //n^2
                }
                if (y > 0) {                                    //n^2
                    Node<T> nodoAbajo = obtenerNodo(x, y - 1);  //n^2
                    nuevoNodo.setDown(nodoAbajo);               //n^2
                    nodoAbajo.setUp(nuevoNodo);                 //n^2
                }
            }
        }
    }

    public Node<T> obtenerNodo(int x, int y) {
        if (x < 0 || y < 0 || x >= ancho || y >= alto) {    //1
            return null;                                    //1
        }
        Node<T> actual = head;                              //1

        for (int i = 0; i < x && actual != null; i++) {     //n
            actual = actual.getRight();                     //n
        }
        for (int i = 0; i < y && actual != null; i++) {     //n
            actual = actual.getUp();                        //n
        }
        return actual;                                      //n
    }

    public void insertarDato(int x, int y, T data) {                    
        Node<T> nodo = obtenerNodo(x, y);                               //1

        if (nodo != null) {                                             //1
            nodo.setData(data);                                         //1
        } else {
            throw new IndexOutOfBoundsException("Posicion invalida"); //1
        }
    }

    public T obtenerDato(int x, int y) {
        Node<T> nodo = obtenerNodo(x, y);                               //1
        if (nodo != null) {                                             //1
            T dato = nodo.getData();                                    //1
            return dato;                                                //1
        } else {    
            throw new IndexOutOfBoundsException("Posicion invalida"); //1
        }
    }

    public void imprimir() {
        System.out.println("\nMapa de la ciudad:");                                 //1
        System.out.println("******************************");                       //1
        System.out.print("   ");                                                    //1
        for (int x = 0; x < ancho; x++) {                                             //n
            System.out.print(" " + (x + 1) + " ");                                    //n
        }
        System.out.println();                                                         //1  
        for (int y = 0; y < alto; y++) {                                              //n
            System.out.print(letrasFilas[y] + "  ");                                    //n

            Node<T> fila = obtenerNodo(0, y);                                       //n
            for (int x = 0; x < ancho && fila != null; x++) {                         //n^2
                Interseccion interseccion = (Interseccion) fila.getData();            //n^2
                System.out.print(" " + interseccion.getRepresentacionConsola() + " ");//n^2
                fila = fila.getRight();                                               //n^2
            }
            System.out.println();                                                     //n
        }
        System.out.println("******************************\n");                     //1
    }

}
