package com.mycompany.sistema_de_trafico.edd;

public class Node<T> {
    private T data;
    private Node<T> next;
    private Node<T> prev;
    private Node<T> up;
    private Node<T> down;
    private Node<T> left;
    private Node<T> right;
    private int height; // atributo exclusivo para el arbol avl

    public Node(T data) {
        this.data = data;
        this.next = null;
        this.prev = null;
        this.up = null;
        this.down = null;
        this.left = null;
        this.right = null;
        this.height = 1;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public Node<T> getPrev() {
        return prev;
    }

    public void setPrev(Node<T> prev) {
        this.prev = prev;
    }

    public Node<T> getUp() {
        return up;
    }

    public void setUp(Node<T> up) {
        this.up = up;
    }

    public Node<T> getDown() {
        return down;
    }

    public void setDown(Node<T> down) {
        this.down = down;
    }

    public Node<T> getLeft() {
        return left;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public Node<T> getRight() {
        return right;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
