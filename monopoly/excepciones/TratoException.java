package monopoly.excepciones;

/* Nivel 2 de la jerarquía de excepciones.
 * Errores en los tratos entre jugadores.
 */
public abstract class TratoException extends MonopolyException {
    public TratoException(String mensaje) {
        super(mensaje);
    }
}
