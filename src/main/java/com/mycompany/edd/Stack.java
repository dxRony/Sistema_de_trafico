package com.mycompany.edd;

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

    public T getTop() {
        if (isEmpty()) {
            System.out.println("La pila esta vacia");
            return null;
        }
        T topData = top.getData();
        return topData;
    }

    private boolean isEmpty() {
        return top == null;
    }

    public int getSize() {
        return this.size;
    }

}
