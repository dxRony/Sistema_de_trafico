package com.mycompany.sistema_de_trafico.edd;

import com.mycompany.sistema_de_trafico.objects.Vehiculo;

public class HashTable {
    // array de tipo lista enlazada
    private LinkedList<Vehiculo>[] tabla;
    // tamaño del array
    private int tamaño;

    // constructor que recibe el tamaño del arreglo
    @SuppressWarnings("unchecked")
    public HashTable(int tamaño) {

        this.tamaño = tamaño;                                                   //1
        // inicializando array de lista con tamaño
        tabla = new LinkedList[tamaño];                                         //1
        for (int i = 0; i < tamaño; i++) {                                      //n
            // recorriendo cada posicion del array e inicializando una lista
            tabla[i] = new LinkedList<>();                                      //n
        }
    }

    private int funcionHash(String placa) {
        int sumaAscii = 0;                                      //1
        for (char c : placa.toCharArray()) {                    //n
            // sumando todos los valores ascii de la placa
            sumaAscii += c;                                     //n
        }
        // devolviendo el modulo entre la suma y el tamaño
        return sumaAscii % tamaño;                              //1
    }

    public boolean insertar(Vehiculo vehiculo) {
        if (placaDuplicada(vehiculo.getPlaca())) {                                                              //1
            System.out.println("\nError, el vehiculo con la placa: " + vehiculo.getPlaca() + ", ya existe.");   //1
            return false;                                                                                       //1
        }
        // obteniendo el indice del vehiculo
        int indice = funcionHash(vehiculo.getPlaca());                                                          //1
        // agregando al vehiculo a la tabla con el indice que devolvio la funcion hash
        tabla[indice].add(vehiculo);                                                                            //1
        return true;                                                                                            //1
    }

    private boolean placaDuplicada(String placa) {
        int indice = funcionHash(placa);                        //1
        boolean existe = false;                                 //1
        if (tabla[indice].buscarPorPlaca(placa) != null) {      //1
            existe = true;                                      //1
        }
        return existe;                                          //1
    }

    public Vehiculo buscar(String placa) {                              //1
        System.out.println("Buscando vehiculo con placa: " + placa);    //1
        // obteniendo el indice del vehiculo a buscar
        int indice = funcionHash(placa);                                //1
        // buscando y retornando al vehivulo por la placa
        return tabla[indice].buscarPorPlaca(placa);                     //1
    }

    public void mostrarVehiculosEnDestino(HashTable tablaVehiculos, int opcion) {
        int numero = 0;                                                                 //1
        System.out.println();                                                           //1
        for (int i = 0; i < tablaVehiculos.tamaño; i++) {                               //n

            // recorriendo cada bucket de la tabla
            LinkedList<Vehiculo> bucketActual = tablaVehiculos.tabla[i];                //n
            // recorriendo lista que esta en el bucket [i]
            Node<Vehiculo> actual = bucketActual.getHead();                             //n

            while (actual != null) {                                                    //n^2
                Vehiculo vehiculoActual = actual.getData();                             //n^2
                if (opcion == 1) {                                                      //n^2
                    if (vehiculoActual.isEnDestino()) {                                 //n^2
                        numero++;                                                       //n^2
                        System.out.println(numero + ". " + vehiculoActual.toString());  //n^2
                    }
                } else if (opcion == 2) {                                               //n^2
                    if (!vehiculoActual.isEnDestino()) {                                //n^2
                        numero++;                                                       //n^2
                        System.out.println(numero + ". " + vehiculoActual.toString());  //n^2
                    }
                }
                actual = actual.getNext();                                              //n^2
            }
        }
    }
}
