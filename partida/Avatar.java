package partida;

import monopoly.*;

import java.util.ArrayList;


public class Avatar {

    //Atributos
    private String id; //Identificador: una letra generada aleatoriamente.
    private String tipo; //Sombrero, Esfinge, Pelota, Coche
    private Jugador jugador; //Un jugador al que pertenece ese avatar.
    private Casilla lugar; //Los avatares se sitúan en casillas del tablero.

    //Constructor vacío
    public Avatar() {
    }

    /*Constructor principal. Requiere éstos parámetros:
    * Tipo del avatar, jugador al que pertenece, lugar en el que estará ubicado, y un arraylist con los
    * avatares creados (usado para crear un ID distinto del de los demás avatares).
     */
    public Avatar(String tipo, Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) {
    }

    //A continuación, tenemos otros métodos útiles para el desarrollo del juego.
    /*Método que permite mover a un avatar a una casilla concreta. Parámetros:
    * - Un array con las casillas del tablero. Se trata de un arrayList de arrayList de casillas (uno por lado).
    * - Un entero que indica el numero de casillas a moverse (será el valor sacado en la tirada de los dados).
    * EN ESTA VERSIÓN SUPONEMOS QUE valorTirada siemrpe es positivo.
     */
    public void moverAvatar(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {
        int posicionActual = this.lugar.getPosicion();
        int nuevaPosicion = (posicionActual + valorTirada) % 40;
        boolean pasaPorSalida = nuevaPosicion < posicionActual;

        if (pasaPorSalida){
            this.jugador.sumarFortuna(2000000);
            System.out.println("El jugador "+ this.jugador.getNombre() + " pasa por la salida y recibe 2.000.000€  ");
        }

        this.lugar.eliminarAvatar(this);

        Casilla nuevaCasilla = null;
        for (ArrayList<Casilla> lado : casillas){
            for (Casilla c : lado) {
                if (c.getPosicion() == nuevaPosicion) {
                    nuevaCasilla = c;
                    break;
                    // Salimos del bucle interior al encontrarla

                }
            }
        }

        if (nuevaCasilla != null){
            this.lugar = nuevaCasilla;
            this.lugar.anhadirAvatar(this);

            if (this.lugar.getNombre().equalsIgnoreCase("IrCarcel")) {}
        }



    }

    /*Método que permite generar un ID para un avatar. Sólo lo usamos en esta clase (por ello es privado).
    * El ID generado será una letra mayúscula. Parámetros:
    * - Un arraylist de los avatares ya creados, con el objetivo de evitar que se generen dos ID iguales.
     */
    private void generarId(ArrayList<Avatar> avCreados) {
        String candidato;
        boolean repetido;
        do {
            char letra = (char) (Math.random() * 256);
            candidato = String.valueOf(letra);
            repetido = false;
            for (Avatar av : avCreados) {
                if (av.id != null && av.id.equals(candidato)) {
                    repetido = true;
                    break;
                }
            }
        } while (repetido);
        this.id = candidato;
    }

    public Casilla getLugar() {
        return this.lugar;
    }

    public void setLugar(Casilla lugar){
        this.lugar = lugar;
    }
// este set y get para encarcelar en judador

    public class Lugar {
        private String nombre;

        public Lugar(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return this.nombre;
        }
    }

    public String getId() {
        return this.id;
    }

}