package com.mycompany.sistema_de_trafico.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.mycompany.sistema_de_trafico.edd.HashTable;
import com.mycompany.sistema_de_trafico.enums.TipoVehiculo;
import com.mycompany.sistema_de_trafico.objects.Vehiculo;

public class Archivo {

    public HashTable leerArchivo(String ruta, int tamaño) {
        HashTable tablaVehiculos = new HashTable(tamaño);
        File archivo = new File(ruta);
        System.out.println("Abriendo archivo CSV: " + ruta);

        try (FileReader lector = new FileReader(archivo);
                BufferedReader buffer = new BufferedReader(lector)) {

            System.out.println("Leyendo datos de vehículos...");
            String linea;

            while ((linea = buffer.readLine()) != null) {

                String[] datos = linea.split(",");

                try {
                    TipoVehiculo tipo = TipoVehiculo.valueOf(datos[0].trim());
                    String placa = datos[1].trim();
                    String origen = datos[2].trim();
                    String destino = datos[3].trim();
                    int prioridad = Integer.parseInt(datos[4].trim());
                    int tiempoEspera = Integer.parseInt(datos[5].trim());

                    Vehiculo vehiculo = new Vehiculo(tipo, placa, origen, destino, prioridad, tiempoEspera);

                    tablaVehiculos.insertar(vehiculo);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error al procesar el dato " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("\nError al leer el archivo " + e.getMessage());
        }
        return tablaVehiculos;
    }
}
