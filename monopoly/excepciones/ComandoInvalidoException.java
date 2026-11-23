package monopoly.excepciones;

/* Nivel 3 de la jerarquía de excepciones.
 * Comando que no existe o mal escrito (sintaxis incorrecta).
 */
public class ComandoInvalidoException extends ComandoException {
    public ComandoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
