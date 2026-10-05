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
        this.tipo = tipo;
        this.jugador = jugador;
        this.lugar = lugar;
        this.generarId(avCreados);
        avCreados.add(this);
        if (this.lugar != null) {
            this.lugar.anhadirAvatar(this);   // el avatar aparece en la Salida
        }
    }

    //A continuación, tenemos otros métodos útiles para el desarrollo del juego.
    /*Método que permite mover a un avatar a una casilla concreta. Parámetros:
     * - Un array con las casillas del tablero. Se trata de un arrayList de arrayList de casillas (uno por lado).
     * - Un entero que indica el numero de casillas a moverse (será el valor sacado en la tirada de los dados).
     * EN ESTA VERSIÓN SUPONEMOS QUE valorTirada siemrpe es positivo.
     */
    public void moverAvatar(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {
        //1. Calcular la nueva posición (las casillas van de 1 a 40)
        int posicionActual = this.lugar.getPosicion();
        int nuevaPosicion = (posicionActual - 1 + valorTirada) % 40 + 1;
        boolean pasaPorSalida = nuevaPosicion < posicionActual;

        //2. Buscar la casilla destino
        Casilla nuevaCasilla = null;
        for (ArrayList<Casilla> lado : casillas) {
            for (Casilla c : lado) {
                if (c.getPosicion() == nuevaPosicion) {
                    nuevaCasilla = c;
                    break;                          // sale del bucle interior
                }
            }
            if (nuevaCasilla != null) break;        // sale también del exterior
        }

        if (nuevaCasilla == null) {
            System.out.println("Error: no existe la casilla " + nuevaPosicion);
            return;
        }

        //3. Cobrar la Salida si ha dado la vuelta
        if (pasaPorSalida) {
            this.jugador.sumarFortuna(Valor.SUMA_VUELTA);
            this.jugador.sumarVueltas();
            System.out.println("El jugador " + this.jugador.getNombre() + " pasa por la Salida y recibe 2.000.000€.");
        }

        //4. Mover el avatar de la casilla actual a la nueva
        Casilla origen = this.lugar;
        this.lugar.eliminarAvatar(this);
        this.lugar = nuevaCasilla;
        this.lugar.anhadirAvatar(this);

        System.out.println("El avatar " + this.id + " avanza " + valorTirada + " posiciones, desde "
                + origen.getNombre() + " hasta " + this.lugar.getNombre() + ".");

        //5. Si cae en IrCarcel, va a la cárcel
        if (this.lugar.getNombre().equalsIgnoreCase("IrCarcel")) {
            this.jugador.encarcelar(casillas);
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
            char letra = (char) ('A' + (int) (Math.random() * 26));
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

    //Getters y setters
    public String getId() {
        return this.id;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Jugador getJugador() {
        return this.jugador;
    }

    public Casilla getLugar() {
        return this.lugar;
    }

    public void setLugar(Casilla lugar) {
        this.lugar = lugar;   // lo usa Jugador.encarcelar()
    }
}