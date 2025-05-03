package com.mycompany.sistema_de_trafico.edd;

import com.mycompany.sistema_de_trafico.objects.Vehiculo;

public class LinkedList<T> {
    private Node<T> head;
    private int size;

    public LinkedList() {
        this.head = null;
        this.size = 0;
    }

    public void add(T data) {
        Node<T> newNode = new Node<>(data);         //1
        if (head == null) {
            head = newNode;                         //1
        } else {
            Node<T> current = head;                 //1              
            while (current.getNext() != null) {     //n
                current = current.getNext();        //n
            }
            current.setNext(newNode);               //1
            newNode.setPrev(current);               //1
        }   
        size++;                                     //1
    }

    public T find(T data) {
        Node<T> current = head;                     //1
        while (current != null) {                   //n
            if (current.getData().equals(data)) {   //n
                return current.getData();           //n
            }
            current = current.getNext();            //n
        }
        return null;                                //1
    }

    public boolean remove(T data) {
        Node<T> current = head;                                     //1

        while (current != null) {                                   //n
            if (current.getData().equals(data)) {                   //n
                if (current.getPrev() != null) {                    //n
                    current.getPrev().setNext(current.getNext());   //n
                } else {
                    head = current.getNext();                       //n
                }

                if (current.getNext() != null) {                    //n
                    current.getNext().setPrev(current.getPrev());   //n
                }
                size--;                                             //n
                return true;                                        //n
            }
            current = current.getNext();                            //n
        }
        return false;                                               //n
    }

    public void imprimir() {
        Node<T> current = head;                                     //1
        while (current != null) {                                   //n
            System.out.print(current.getData() + "\n");             //n
            current = current.getNext();                            //n
        }
        System.out.println();                                       //1
    }

    public Vehiculo buscarPorPlaca(String placa) {
        Node<T> current = head;                                     //1

        while (current != null) {                                   //n
            if (current.getData() instanceof Vehiculo) {            //n
                Vehiculo vehiculo = (Vehiculo) current.getData();   //n
                if (vehiculo.getPlaca().equals(placa)) {            //n
                    return vehiculo;                                //n
                }
            }
            current = current.getNext();                            //n
        }
        return null;                                                //1
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Node<T> getHead() {
        return head;
    }

    public void setHead(Node<T> head) {
        this.head = head;
    }

}
