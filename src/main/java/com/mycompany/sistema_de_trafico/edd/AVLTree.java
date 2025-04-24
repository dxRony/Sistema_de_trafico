package com.mycompany.sistema_de_trafico.edd;

import com.mycompany.sistema_de_trafico.objects.Interseccion;

public class AVLTree {
    private Node<Interseccion> root;

    public AVLTree() {
        this.root = null;
    }

    public void insertar(Interseccion data) {
        // se llama a metodo insertar pasando nodo raiz y dato a insertar
        root = insertarPriv(root, data);
    }

    private Node<Interseccion> insertarPriv(Node<Interseccion> nodo, Interseccion data) {
        // metodo de insercion, que funciona de manera recursiva
        if (nodo == null) {
            // cuando el nodo actual sea null, se inserta el dato
            return new Node<Interseccion>(data);
        }

        // comparando complejidad de interseccion para ver a que subarbol va
        if (data.getComplejidad() < nodo.getData().getComplejidad()) {
            nodo.setLeft(insertarPriv(nodo.getLeft(), data));
        } else {
            nodo.setRight(insertarPriv(nodo.getRight(), data));
        }
        // actualizando la altura del nodo insertado
        actualizarAltura(nodo);
        // llamando a metodo de balanceo
        return balancear(nodo);
    }

    private void actualizarAltura(Node<Interseccion> nodo) {
        // metodo de actualizacion de altura
        // obteniendo la altura de subarbol izquierdo
        int alturaIzq = getAltura(nodo.getLeft());
        // obteniendo la altura de subarbol derecho
        int alturaDer = getAltura(nodo.getRight());
        int mayorAltura;

        // con las alturas obtenidas, determinamos la mayor
        if (alturaIzq > alturaDer) {
            mayorAltura = alturaIzq;
        } else {
            mayorAltura = alturaDer;
        }
        // de la altura mayor sumanos 1 y esa sera la altura del nodo
        nodo.setHeight(mayorAltura + 1);
    }

    private int getAltura(Node<Interseccion> nodo) {
        if (nodo == null) {
            return 0;
        }
        // devolviendo altura del nodo
        return nodo.getHeight();
    }

    private Node<Interseccion> balancear(Node<Interseccion> nodo) {
        int balance = getFactorBalance(nodo);

        if (balance > 1) {
            //en este caso, el arbol izquierdo esta desbalanceado
            if (getFactorBalance(nodo.getLeft()) < 0) {
                //si el subarbol izquierdo tiene un factor < 0, es un caso LR
                nodo.setLeft(rotacionIzquierda(nodo.getLeft()));
            }
            //si solo es necesaria una rotacion a la derecha, es un caso LL
            return rotacionDerecha(nodo);
        }

        if (balance < -1) {
            //en este caso, el arbol derecho esta desbalanceado
            if (getFactorBalance(nodo.getRight()) > 0) {
                //si el subarbol derecho tiene un factor > 0, es un caso RL
                nodo.setRight(rotacionDerecha(nodo.getRight()));
            }
            //si solo es necesaria una rotacion a la izquierda, es un caso RR
            return rotacionIzquierda(nodo);
        }
        //si no hay algun tipo de balanceo se retorna el nodo
        return nodo;
    }

    private int getFactorBalance(Node<Interseccion> nodo) {
        //metodo para obtener el factor de balance
        //si es -1, 0 o 1, el arbol esta balanceado
        if (nodo == null) {
            return 0;
        }
        return getAltura(nodo.getLeft()) - getAltura(nodo.getRight());
    }

    private Node<Interseccion> rotacionDerecha(Node<Interseccion> y) {
        //guardando subarbol izquierdo del nodo recibido
        Node<Interseccion> x = y.getLeft();
        //guardando subarbol derecho de x
        Node<Interseccion> T2 = x.getRight();

        //para x su nuevo subarbol derecho sera su antiguo padre
        x.setRight(y);
        //para y su nuevo subarbol izquierod sera el subarbol derecho de x
        y.setLeft(T2);

        //actualizando altura de ambos nodos
        actualizarAltura(y);
        actualizarAltura(x);

        //retornando la nueva raiz
        return x;
    }

    private Node<Interseccion> rotacionIzquierda(Node<Interseccion> x) {
        //guardando subarbol derecho del nodo recibido
        Node<Interseccion> y = x.getRight();
        //guardando subarbol izquierdo de y
        Node<Interseccion> T2 = y.getLeft();

        //para y su nuevo izquierdo sera su antiguo padre        
        y.setLeft(x);
        //para el antiguo padre su subarbol derecho sera el subarbol izquierdo de y
        x.setRight(T2);

        //actualizando altura de ambos
        actualizarAltura(x);
        actualizarAltura(y);

        //retornando nueva raiz
        return y;
    }

    public void imprimir() {
        imprimirDescendente(root, 0);
    }

    private void imprimirDescendente(Node<Interseccion> nodo, int espacio) {
        if (nodo == null) {
            return;
        }
        int sangria = 5;        
        espacio += sangria;

        imprimirDescendente(nodo.getRight(), espacio);

        for (int i = 0; i < espacio; i++) {
            System.out.print(" ");
        }

        System.out.println(nodo.getData().toString());
        imprimirDescendente(nodo.getLeft(), espacio);
    }

}
