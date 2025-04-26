package com.mycompany.sistema_de_trafico.flow;

import com.mycompany.sistema_de_trafico.edd.AVLTree;
import com.mycompany.sistema_de_trafico.edd.HashTable;
import com.mycompany.sistema_de_trafico.edd.OrthogonalMatrix;
import com.mycompany.sistema_de_trafico.edd.Stack;
import com.mycompany.sistema_de_trafico.objects.Interseccion;

public class Simulador {

    // tablero de la ciudad
    private OrthogonalMatrix<Interseccion> ciudad;
    // arbol para buscar la ruta mas compleja
    private AVLTree arbolIntersecciones;
    // pila donde se guardaran los eventos
    private Stack<String> registroEventos;
    // tabla donde se almacenan los vehiculos
    private HashTable tablaVehiculos;

    public Simulador(HashTable tablaVehiculos, int ancho, int alto) {
        this.tablaVehiculos = tablaVehiculos;
        ciudad = new OrthogonalMatrix<>(ancho, alto);
        arbolIntersecciones = new AVLTree();
        registroEventos = new Stack<>();

    }

    public void iniciarSimulador() {

    }

    private void generarMapaCiudad(){
        
    }

}
