package monopoly.excepciones;

/* Nivel 3 de la jerarquía de excepciones.
 * Se intenta aceptar o eliminar un trato que no existe o que no le corresponde al jugador.
 */
public class TratoNoExisteException extends TratoException {
    public TratoNoExisteException(String mensaje) {
        super(mensaje);
    }
}
