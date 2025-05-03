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
        Node<Interseccion> nuevo = new Node<>(interseccion);                                //1

        if (head == null || compararPorComplejidad(interseccion, head.getData()) > 0) {     //1
            nuevo.setNext(head);                                                            //1
            if (head != null) {                                                             //1
                head.setPrev(nuevo);                                                        //1
            }
            head = nuevo;                                                                   //1
        } else {                                                                            //1
            Node<Interseccion> actual = head;                                               //1
            while (actual.getNext() != null &&                          
                    compararPorComplejidad(interseccion, actual.getNext().getData()) <= 0) {//n
                actual = actual.getNext();                                                  //n
            }

            nuevo.setNext(actual.getNext());                                                //1
            if (actual.getNext() != null) {                                                 //1
                actual.getNext().setPrev(nuevo);                                            //1
            }
            actual.setNext(nuevo);                                                          //1
            nuevo.setPrev(actual);                                                          //1
        }
        size++;                                                                             //1
    }

    private int compararPorComplejidad(Interseccion i1, Interseccion i2) {
        return i1.getComplejidad() - i2.getComplejidad();
    }

    public Interseccion desencolar() {
        if (head == null) {                         //1
            return null;                            //1
        }

        Interseccion interseccion = head.getData(); //1
        head = head.getNext();                      //1
        if (head != null) {                         //1
            head.setPrev(null);                //1
        }
        size--;                                     //1
        return interseccion;                        //1
    }

    public boolean eliminar(Interseccion interseccion) {            //1
        Node<Interseccion> actual = head;                           //1

        while (actual != null) {                                    //n
            if (actual.getData().equals(interseccion)) {            //n
                if (actual.getPrev() != null) {                     //n
                    actual.getPrev().setNext(actual.getNext());     //n
                } else {                                            //n
                    head = actual.getNext();                        //n
                }

                if (actual.getNext() != null) {                     //n
                    actual.getNext().setPrev(actual.getPrev());     //n
                }
                size--;                                             //n
                return true;                                        //n
            }
            actual = actual.getNext();                              //n
        }
        return false;                                               //1
    }

    public void actualizarInterseccion(String nombre) {
        if (head == null){                                                      //1
            return;                                                             //1
        }

        Node<Interseccion> actual = head;                                       //1

        while (actual != null) {                                                //n
            if (actual.getData().getNombre().equals(nombre)) {                  //n
                Interseccion interseccion = actual.getData();                   //n

                if (actual.getPrev() != null) {                                 //n
                    actual.getPrev().setNext(actual.getNext());                 //n
                } else {                                                        //n
                    head = actual.getNext();                                    //n
                }

                if (actual.getNext() != null) {                                 //n
                    actual.getNext().setPrev(actual.getPrev());                 //n
                }
                size--;                                                         //n

                interseccion.calcularComplejidad();                             //n

                insertar(interseccion);                                         //n
                return;                                                         //n
            }
            actual = actual.getNext();                                          //n
        }
    }

    public void imprimir() {                                                    
        if (estaVacia()) {                                                    //1
            System.out.println("Cola de prioridad vacia\n");                //1
            return;                                                             //1
        }

        Node<Interseccion> actual = head;                                   //1
        int posicion = 1;                                                   //1
        while (actual != null) {                                            //n
            Interseccion i = actual.getData();                              //n
            System.out.println(posicion + ") " + i.getNombre() + " - complejidad: " + i.getComplejidad());
            actual = actual.getNext();                                      //n
            posicion++;                                                     //n
        }
        System.out.println("======================================\n");     //1
    }

    public void mostrarVehiculosCirculados() {

        Node<Interseccion> actual = head;
        int posicion = 1;
        while (actual != null) {
            Interseccion i = actual.getData();
            System.out.println(
                    posicion + ") " + " En la interseccion: " + i.getNombre() + ", circularon: "
                            + i.getVehiculosCirculados() + ", vehiculos.");
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
