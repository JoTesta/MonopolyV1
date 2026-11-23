package monopoly.excepciones;

/* Nivel 3 de la jerarquía de excepciones.
 * El jugador no tiene dinero para comprar, edificar, deshipotecar, salir de la cárcel...
 */
public class FortunaInsuficienteException extends DineroException {
    public FortunaInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
