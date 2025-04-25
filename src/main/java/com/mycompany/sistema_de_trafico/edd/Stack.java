package com.mycompany.sistema_de_trafico.edd;

public class Stack<T> {
    private Node<T> top;
    private int size;

    public Stack() {
        this.top = null;
        this.size = 0;
    }

    public void push(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.setNext(top);
        top = newNode;
        size++;
    }

    public T pop() {
        if (isEmpty()) {
            System.out.println("La pila esta vacia");
            return null;
        }
        T data = top.getData();
        top = top.getNext();
        size--;
        return data;
    }

    public T peek() {
        if (isEmpty()) {
            System.out.println("La pila esta vacia");
            return null;
        }
        T topData = top.getData();
        return topData;
    }

    public void imprimir() {
        if (isEmpty()) {
            System.out.println("La pila esta vacia");
            return;
        }

        System.out.println("Mostrando pila");
        Node<T> current = top;
        while (current != null) {
            System.out.println("  "+current.getData());
            System.out.println("  |");            
            current = current.getNext();
        }
        System.out.println("Fin de pila");
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int getSize() {
        return this.size;
    }

}
