package monopoly.excepciones;

/* Nivel 3 de la jerarquía de excepciones.
 * Se nombra una casilla que no está en el tablero.
 */
public class CasillaNoExisteException extends CasillaException {
    public CasillaNoExisteException(String mensaje) {
        super(mensaje);
    }
}
