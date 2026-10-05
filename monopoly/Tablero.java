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
        this.banca = banca;
        this.posiciones = new ArrayList<>();
        this.grupos = new HashMap<>();
        this.generarCasillas();
        this.generarGrupos();
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
        ArrayList<Casilla> lado = new ArrayList<>();

        lado.add(new Casilla("Parking", "Especial", 21, banca));
        lado.add(new Casilla("Solar12", "Solar", 22, 2200000, banca));
        lado.add(new Casilla("Suerte", "Suerte", 23, banca));
        lado.add(new Casilla("Solar13", "Solar", 24, 2200000, banca));
        lado.add(new Casilla("Solar14", "Solar", 25, 2400000, banca));
        lado.add(new Casilla("Trans3", "Transporte", 26, 500000, banca));
        lado.add(new Casilla("Solar15", "Solar", 27, 2600000, banca));
        lado.add(new Casilla("Solar16", "Solar", 28, 2600000, banca));
        lado.add(new Casilla("Serv2", "Servicios", 29, 500000, banca));
        lado.add(new Casilla("Solar17", "Solar", 30, 2800000, banca));

        posiciones.add(lado);
    }

    //Método para insertar las casillas del lado sur.
    private void insertarLadoSur() {
        ArrayList<Casilla> lado = new ArrayList<>();

        lado.add(new Casilla("Salida", "Especial", 1, banca));
        lado.add(new Casilla("Solar1", "Solar", 2, 600000, banca));
        lado.add(new Casilla("Caja", "Comunidad", 3, banca));
        lado.add(new Casilla("Solar2", "Solar", 4, 600000, banca));
        lado.add(new Casilla("Imp1", 5, 2000000, banca));
        lado.add(new Casilla("Trans1", "Transporte", 6, 500000, banca));
        lado.add(new Casilla("Solar3", "Solar", 7, 1000000, banca));
        lado.add(new Casilla("Suerte", "Suerte", 8, banca));
        lado.add(new Casilla("Solar4", "Solar", 9, 1000000, banca));
        lado.add(new Casilla("Solar5", "Solar", 10, 1200000, banca));

        posiciones.add(lado);
    }

    //Método que inserta casillas del lado oeste.
    private void insertarLadoOeste() {
        ArrayList<Casilla> lado = new ArrayList<>();

        lado.add(new Casilla("Carcel", "Especial", 11, banca));
        lado.add(new Casilla("Solar6", "Solar", 12, 1400000, banca));
        lado.add(new Casilla("Serv1", "Servicios", 13, 500000, banca));
        lado.add(new Casilla("Solar7", "Solar", 14, 1400000, banca));
        lado.add(new Casilla("Solar8", "Solar", 15, 1600000, banca));
        lado.add(new Casilla("Trans2", "Transporte", 16, 500000, banca));
        lado.add(new Casilla("Solar9", "Solar", 17, 1800000, banca));
        lado.add(new Casilla("Caja", "Comunidad", 18, banca));
        lado.add(new Casilla("Solar10", "Solar", 19, 1800000, banca));
        lado.add(new Casilla("Solar11", "Solar", 20, 2200000, banca));

        posiciones.add(lado);
    }

    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {
        ArrayList<Casilla> lado = new ArrayList<>();

        lado.add(new Casilla("IrCarcel", "Especial", 31, banca));
        lado.add(new Casilla("Solar18", "Solar", 32, 3000000, banca));
        lado.add(new Casilla("Solar19", "Solar", 33, 3000000, banca));
        lado.add(new Casilla("Caja", "Comunidad", 34, banca));
        lado.add(new Casilla("Solar20", "Solar", 35, 3200000, banca));
        lado.add(new Casilla("Trans4", "Transporte", 36, 500000, banca));
        lado.add(new Casilla("Suerte", "Suerte", 37, banca));
        lado.add(new Casilla("Solar21", "Solar", 38, 3500000, banca));
        lado.add(new Casilla("Imp2", 39, 2000000, banca));
        lado.add(new Casilla("Solar22", "Solar", 40, 4000000, banca));

        posiciones.add(lado);
    }

    //Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {
        return "";
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

    public Casilla obtenerCasilla(int posicion) {
        if (posicion < 1 || posicion > 40) { // Posición fuera del tablero, proteccion
            return null;
        }
        int lado = (posicion - 1) / 10;   // 0=Sur, 1=Oeste, 2=Norte, 3=Este
        int indice = (posicion - 1) % 10; // posición dentro de ese lado (0-9)
        return posiciones.get(lado).get(indice);
    }

    //crea los grupos de solares y los guarda en el HashMap (clave = nombre del color).
    private void generarGrupos() {
        grupos.put("Blanco",   new Grupo(obtenerCasilla(2),  obtenerCasilla(4),                     Valor.WHITE));  // Solar1-2
        grupos.put("Cian",     new Grupo(obtenerCasilla(7),  obtenerCasilla(9),  obtenerCasilla(10), Valor.CYAN));   // Solar3-5
        grupos.put("Rosa",     new Grupo(obtenerCasilla(12), obtenerCasilla(14), obtenerCasilla(15), Valor.PURPLE)); // Solar6-8
        grupos.put("Amarillo", new Grupo(obtenerCasilla(17), obtenerCasilla(19), obtenerCasilla(20), Valor.YELLOW)); // Solar9-11
        grupos.put("Rojo",     new Grupo(obtenerCasilla(22), obtenerCasilla(24), obtenerCasilla(25), Valor.RED));    // Solar12-14
        grupos.put("Azul",     new Grupo(obtenerCasilla(27), obtenerCasilla(28), obtenerCasilla(30), Valor.BLUE));   // Solar15-17
        grupos.put("Verde",    new Grupo(obtenerCasilla(32), obtenerCasilla(33), obtenerCasilla(35), Valor.GREEN));  // Solar18-20
        grupos.put("Negro",    new Grupo(obtenerCasilla(38), obtenerCasilla(40),                     Valor.BLACK));  // Solar21-22
    }

    public ArrayList<ArrayList<Casilla>> getPosiciones() { //esto permite hacer desde menu tablero.getPosiciones()
        return this.posiciones;
    }

    public HashMap<String, Grupo> getGrupos() {
        return this.grupos;
    }

    public Jugador getBanca() {
        return this.banca;
    }

}

