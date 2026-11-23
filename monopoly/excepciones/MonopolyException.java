package monopoly.excepciones;

/* Nivel 1 de la jerarquía de excepciones.
 * Raíz de todas las excepciones propias del juego.
 */
public abstract class MonopolyException extends Exception {
    public MonopolyException(String mensaje) {
        super(mensaje);
    }
}
