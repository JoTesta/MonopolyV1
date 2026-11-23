package monopoly.excepciones;

/* Nivel 3 de la jerarquía de excepciones.
 * El trato no se puede proponer o aceptar: propiedad que no pertenece al jugador, dinero insuficiente...
 */
public class TratoInvalidoException extends TratoException {
    public TratoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
