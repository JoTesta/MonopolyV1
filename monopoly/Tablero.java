package monopoly;

import partida.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Tablero {
    //Atributos.
    private ArrayList<ArrayList<Casilla>> posiciones; //Posiciones del tablero: se define como un arraylist de arraylists de casillas (uno por cada lado del tablero).
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será el color del grupo).
    private Jugador banca; //Un jugador que será la banca.

    //Constructor: únicamente le pasamos el jugador banca (que se creará desde el menú).
    public Tablero(Jugador banca) {
    }

    
    //Método para crear todas las casillas del tablero. Formado a su vez por cuatro métodos (1/lado).
    private void generarCasillas() {
        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoNorte();
        this.insertarLadoEste();
    }
    
    //Método para insertar las casillas del lado norte.
    private void insertarLadoNorte() {
    }

    //Método para insertar las casillas del lado sur.
    private void insertarLadoSur() {
    }

    //Método que inserta casillas del lado oeste.
    private void insertarLadoOeste() {
    }

    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {
    }

    //Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {
    }
    
    //Método usado para buscar la casilla con el nombre pasado como argumento:
    public Casilla encontrar_casilla(String nombre){
        for (ArrayList<Casilla> lado : posiciones) {//Recorremos las posiciones de cada lado

            for (Casilla casilla : lado) { // Recorremos casilla a casilla del ado actual

                if (casilla.getNombre().equalsIgnoreCase(nombre)) { //Comparamos el nombre de la casilla que estamos mirando con el nombre de la casilla que estamos buscando
                    return casilla; //devolvemos la casilla si coinciden
                }
            }
        }

        return null; //Si hemos recorrido todas las casilla y ninguna coincide retornamos null
    }

    public ArrayList<ArrayList<Casilla>> getPosiciones() { //esto permite hacer desde menu tablero.getPosiciones()
        return this.posiciones;
    }
}

