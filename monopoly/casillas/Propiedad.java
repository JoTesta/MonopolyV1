package monopoly.casillas;
import monopoly.Casilla;
import monopoly.Menu;
import monopoly.excepciones.*;
import partida.Jugador;
public abstract class Propiedad extends Casilla{
    private float precio;        // precio de compra
    private Jugador duenho;      // dueño actual
    private final Jugador banca; // la banca (dueña inicial)
    private float hipoteca;      // dinero que da hipotecarla
    private boolean hipotecada;
    private float rentabilidad;  // dinero que ha generado (estadísticas)

    public Propiedad(String nombre, int posicion, float precio, Jugador banca) {
        super(nombre, posicion);
        this.precio = precio;
        this.banca = banca;
        this.duenho = banca;          // al principio todo es de la banca
        this.hipoteca = precio / 2;
        this.hipotecada = false;
        this.rentabilidad = 0;
    }

    //getters y setters.
    public Jugador getDuenho() { return duenho; }
    public void setDuenho(Jugador duenho) { this.duenho = duenho; }
    protected Jugador getBanca() { return banca; }
    public float getPrecio() { return precio; }
    public void sumarPrecio(float suma) { this.precio += suma; }
    public float getHipoteca() { return hipoteca; }
    public float getRentabilidad() { return rentabilidad; }
    public void sumarRentabilidad(float cantidad) { this.rentabilidad += cantidad; }

    // Requisito 26
    public boolean perteneceAJugador(Jugador jugador) {
        return this.duenho == jugador;
    }

    public boolean estaHipotecada() {
        return hipotecada;
    }

    // Abstractos: cada subclase los implementa a su manera.
    public abstract boolean alquiler();                  // ¿hay que cobrar alquiler?
    public abstract float valor();                       // precio de compra
    public abstract float calcularAlquiler(int tirada);  // cuánto se cobra

    public void comprar(Jugador jugador) throws MonopolyException {
        if (duenho != banca) {
            throw new OperacionNoPermitidaException("La casilla " + getNombre()
                    + " ya pertenece a " + duenho.getNombre() + ".");
        }
        if (jugador.getFortuna() < valor()) {
            throw new FortunaInsuficienteException(jugador.getNombre()
                    + " no tiene dinero suficiente para comprar " + getNombre() + ".");
        }
        jugador.sumarFortuna(-valor());
        jugador.sumarGastos(valor());
        jugador.registrarDineroInvertido(valor());
        banca.sumarFortuna(valor());

        banca.eliminarPropiedad(this);
        jugador.anhadirPropiedad(this);
        this.duenho = jugador;
    }

    public void hipotecar() throws MonopolyException {
        if (hipotecada) {
            throw new OperacionNoPermitidaException(duenho.getNombre() + " no puede hipotecar "
                    + getNombre() + ". Ya está hipotecada.");
        }
        duenho.sumarFortuna(hipoteca);
        banca.sumarFortuna(-hipoteca);
        hipotecada = true;
    }

    public void deshipotecar() throws MonopolyException {
        if (!hipotecada) {
            throw new OperacionNoPermitidaException(duenho.getNombre() + " no puede deshipotecar "
                    + getNombre() + ". No está hipotecada.");
        }
        if (duenho.getFortuna() < hipoteca) {
            throw new FortunaInsuficienteException(duenho.getNombre()
                    + " no tiene dinero suficiente para deshipotecar " + getNombre() + ".");
        }
        duenho.sumarFortuna(-hipoteca);
        duenho.sumarGastos(hipoteca);
        banca.sumarFortuna(hipoteca);
        hipotecada = false;
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (!alquiler() || duenho == actual) {
            return true;                       // no hay que pagar nada
        }
        float cantidad = calcularAlquiler(tirada);
        if (actual.getFortuna() < cantidad) {
            return false;                      // no puede pagar: deuda
        }
        actual.sumarFortuna(-cantidad);
        actual.sumarGastos(cantidad);
        duenho.sumarFortuna(cantidad);
        actual.registrarPagoAlquiler(cantidad);
        duenho.registrarCobroAlquiler(cantidad);
        sumarRentabilidad(cantidad);
        Menu.consola.imprimir("Se han pagado " + (long) cantidad + " € de alquiler a "
                + duenho.getNombre() + ".");
        return true;
    }
}
