package monopoly.excepciones;

/* Nivel 2 de la jerarquía de excepciones.
 * Errores al escribir comandos.
 */
public abstract class ComandoException extends MonopolyException {
    public ComandoException(String mensaje) {
        super(mensaje);
    }
}
