package monopoly.excepciones;

/* Nivel 2 de la jerarquía de excepciones.
 * Errores relacionados con casillas y propiedades.
 */
public abstract class CasillaException extends MonopolyException {
    public CasillaException(String mensaje) {
        super(mensaje);
    }
}
