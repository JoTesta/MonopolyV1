package monopoly.excepciones;

/* Nivel 3 de la jerarquía de excepciones.
 * La propiedad no admite esa acción: ya comprada, no comprable, ya hipotecada, no hipotecada, no se puede edificar...
 */
public class OperacionNoPermitidaException extends CasillaException {
    public OperacionNoPermitidaException(String mensaje) {
        super(mensaje);
    }
}
