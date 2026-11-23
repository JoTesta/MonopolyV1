package monopoly.excepciones;

/* Nivel 3 de la jerarquía de excepciones.
 * El jugador opera sobre una propiedad que no es suya (hipotecar, edificar, vender...).
 */
public class NoEsPropietarioException extends CasillaException {
    public NoEsPropietarioException(String mensaje) {
        super(mensaje);
    }
}
