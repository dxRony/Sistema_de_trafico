package com.mycompany.sistema_de_trafico.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.mycompany.sistema_de_trafico.edd.LinkedList;
import com.mycompany.sistema_de_trafico.enums.TipoVehiculo;
import com.mycompany.sistema_de_trafico.objects.Vehiculo;

public class Archivo {

    public LinkedList<Vehiculo> leerArchivo(String ruta) {                      
        LinkedList<Vehiculo> listaVehiculos = new LinkedList<>();               //1
        File archivo = new File(ruta);                                          //1
        System.out.println("Abriendo archivo CSV: " + ruta);                    //1

        try (FileReader lector = new FileReader(archivo);                       //1
                BufferedReader buffer = new BufferedReader(lector)) {           //1

            System.out.println("Leyendo datos de vehículos...");                //1
            String linea;                                                       //1

            while ((linea = buffer.readLine()) != null) {                       //n

                String[] datos = linea.split(",");                        //n

                try {
                    TipoVehiculo tipo = TipoVehiculo.valueOf(datos[0].trim());  //n
                    String placa = datos[1].trim();                             //n
                    String origen = datos[2].trim();                            //n
                    String destino = datos[3].trim();                           //n
                    int prioridad = Integer.parseInt(datos[4].trim());          //n
                    int tiempoEspera = Integer.parseInt(datos[5].trim());       //n

                    Vehiculo vehiculo = new Vehiculo(tipo, placa, origen, destino, prioridad, tiempoEspera);

                    listaVehiculos.add(vehiculo);                               //n
                } catch (IllegalArgumentException e) {                          //n
                    System.err.println("Error al procesar el dato " + e.getMessage());
                } catch (Exception e){
                    System.out.println("Error al procesar la linea");
                }
            }
        } catch (IOException e) {
            System.err.println("\nError al leer el archivo " + e.getMessage()); //1
        }
        return listaVehiculos;                                                  //1
    }
}
