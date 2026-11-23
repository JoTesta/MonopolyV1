package monopoly.excepciones;

/* Nivel 3 de la jerarquía de excepciones.
 * Acción que no se puede hacer en este momento del turno: ya ha tirado, no ha tirado, deuda pendiente, faltan jugadores...
 */
public class AccionTurnoException extends TurnoException {
    public AccionTurnoException(String mensaje) {
        super(mensaje);
    }
}
