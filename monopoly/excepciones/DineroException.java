package monopoly.excepciones;

/* Nivel 2 de la jerarquía de excepciones.
 * Errores por falta de dinero.
 */
public abstract class DineroException extends MonopolyException {
    public DineroException(String mensaje) {
        super(mensaje);
    }
}
