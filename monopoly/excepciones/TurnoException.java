package monopoly.excepciones;

/* Nivel 2 de la jerarquía de excepciones.
 * Errores por las reglas del turno.
 */
public abstract class TurnoException extends MonopolyException {
    public TurnoException(String mensaje) {
        super(mensaje);
    }
}
