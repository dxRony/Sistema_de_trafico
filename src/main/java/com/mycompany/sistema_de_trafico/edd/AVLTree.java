package com.mycompany.sistema_de_trafico.edd;

import com.mycompany.sistema_de_trafico.objects.Interseccion;

public class AVLTree {
    // raiz del arbol
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
            // cuando el nodo actual sea null, se crea con el dato recibido
            return new Node<Interseccion>(data);
        }
        // cuando el nodo no sea null
        // comparando complejidad de interseccion
        if (data.getComplejidad() < nodo.getData().getComplejidad()) {
            // si la complejidad es menor, va al lado izquierdo
            nodo.setLeft(insertarPriv(nodo.getLeft(), data));
        } else {
            // si la complejidad es mayor, va al lado derecho
            nodo.setRight(insertarPriv(nodo.getRight(), data));
        }
        // actualizando la altura del nodo insertado
        actualizarAlturaNodo(nodo);
        // llamando a metodo de balanceo
        return balancear(nodo);
    }

    private void actualizarAlturaNodo(Node<Interseccion> nodo) {
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

    private int calcularDiferenciaDeAlturaHijos(Node<Interseccion> nodo) {
        // metodo para obtener el factor de balance
        // si es -1, 0 o 1, el arbol esta balanceado, si no esta desbalanceado
        if (nodo == null) {
            return 0;
        }
        return (getAltura(nodo.getLeft()) - getAltura(nodo.getRight()));
    }

    private Node<Interseccion> balancear(Node<Interseccion> nodo) {
        int balance = calcularDiferenciaDeAlturaHijos(nodo);

        if (balance > 1) {
            // en este caso hay desbalanceo izquierdo
            if (calcularDiferenciaDeAlturaHijos(nodo.getLeft()) < 0) {
                // si el subarbol izquierdo tiene una diferencia < 0, LR y luego LL
                nodo.setLeft(rotacionIzquierda(nodo.getLeft()));
            }
            // rotacion derecha LL
            return rotacionDerecha(nodo);
        }
        if (balance < -1) {
            // en este caso hay desbalanceo derecho
            if (calcularDiferenciaDeAlturaHijos(nodo.getRight()) > 0) {
                // si el subarbol derecho tiene una diferencia > 0, RL y luego RR
                nodo.setRight(rotacionDerecha(nodo.getRight()));
            }
            // rotacion izquierda RR
            return rotacionIzquierda(nodo);
        }
        return nodo;
    }

    private Node<Interseccion> rotacionDerecha(Node<Interseccion> y) {       
        Node<Interseccion> x = y.getLeft(); //hijo izquierdo de y
        Node<Interseccion> z = x.getRight(); //hijo derecho de z
        //x se convierte en el padre de y
        x.setRight(y);
        //z se convierte en el hijo izquierdo de y
        y.setLeft(z);
        // actualizando altura de ambos nodos
        actualizarAlturaNodo(y);
        actualizarAlturaNodo(x);
        // retornando la nueva raiz
        return x;
    }

    private Node<Interseccion> rotacionIzquierda(Node<Interseccion> x) {
        Node<Interseccion> y = x.getRight();//hijo derecho de x
        Node<Interseccion> z = y.getLeft();//hijo izquierdo de y
        //y se convierte en el padre de x
        y.setLeft(x);
        //z se convierte en el hijo de derecho de x
        x.setRight(z);
        // actualizando altura de ambos
        actualizarAlturaNodo(x);
        actualizarAlturaNodo(y);
        // retornando nueva raiz
        return y;
    }

}
