package monopoly;

import partida.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Casilla {

    //Atributos:
    private String nombre; //Nombre de la casilla
    private String tipo; //Tipo de casilla (Solar, Especial, Transporte, Servicios, Comunidad, Suerte y Impuesto).
    private float valor; //Valor de esa casilla (en la mayoría será valor de compra, en la casilla parking se usará como el bote).
    private int posicion; //Posición que ocupa la casilla en el tablero (entero entre 1 y 40).
    private Jugador duenho; //Dueño de la casilla (por defecto sería la banca).
    private Grupo grupo; //Grupo al que pertenece la casilla (si es solar).
    private float impuesto; //Cantidad a pagar por caer en la casilla: el alquiler en solares/servicios/transportes o impuestos.
    private float hipoteca;//Valor otorgado por hipotecar una casilla
    private boolean hipotecada; //Para saber si esta hipotecada o no
    private float rentabilidad; //acumula el dinero q genera esa casilla
    private int vecesVisitada; //el numero de veces que se ha visitado
    private float valorCasa; //Valor de la casa
    private float valorHotel; //Valor de un hotel
    private float valorPiscina; //Valor de piscina
    private float valorPistaDeporte; //Valor pista de deportes


    private float alquilerCasa; //Alquiler de la casa
    private float alquilerHotel; //Alquiler del hotel
    private float alquilerPiscina; //Alquiler de la piscina
    private float alquilerPistaDeporte; //Alquiler de la pista de deportes
    private ArrayList<Avatar> avatares; //Avatares que están situados en la casilla.

    private ArrayList<Edificio> edificios; //edificios que estan en la casilla





    //Constructores:
    public Casilla() {
        this.edificios = new ArrayList<>();// inicializamos el arraylist de edificios
        this.avatares = new ArrayList<>(); //Esto hace que cada casilla empiece con una lista vacía donde luego podremos meter los avatares que estén situados en ella.
    }//Parámetros vacíos

    /*Constructor para casillas tipo Solar, Servicios o Transporte:
     * Parámetros: nombre casilla, tipo (debe ser solar, serv. o transporte), posición en el tablero, valor y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, float valor, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.valor = valor;
        this.duenho = duenho;
        this.edificios = new ArrayList<>();// inicializamos el arraylist de edificios
        this.avatares = new ArrayList<>();
    } // Si hacemos new Casilla("Trans1", "Transporte", 5, 500000, banca); se guardaría Nombre = Trans1, tipo = transporte...

    /*Constructor utilizado para inicializar las casillas de tipo IMPUESTOS.
     * Parámetros: nombre, posición en el tablero, impuesto establecido y dueño.
     */
    public Casilla(String nombre, int posicion, float impuesto, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = "impuesto";
        this.posicion = posicion;
        this.impuesto = impuesto;
        this.duenho = duenho;
        this.edificios = new ArrayList<>();// inicializamos el arraylist de edificios
        this.avatares = new ArrayList<>();
    } // Si hacemos new Casilla("Imp1", 4, 2000000, banca); se guardaria que caer en impuestos implica pagar 2000000

    /*Constructor utilizado para crear las otras casillas (Suerte, Caja de comunidad y Especiales):
     * Parámetros: nombre, tipo de la casilla (será uno de los que queda), posición en el tablero y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.duenho = duenho;
        this.edificios = new ArrayList<>();// inicializamos el arraylist de edificios
        this.avatares = new ArrayList<>();
    }

    //Método utilizado para añadir un avatar al array de avatares en casilla.
    public void anhadirAvatar(Avatar av) {
        avatares.add(av);
    } // Solamente queremos meter av en la lista, de tal forma que nos quedaría que si hacemos casilla.anhadirAvatar(avatarPedro),avatares =[] pasaría a ser avatares = [avatarPedro]

    //Método utilizado para eliminar un avatar del array de avatares en casilla.
    public void eliminarAvatar(Avatar av) {
        avatares.remove(av);
    } //Lo mismo que en la anterior solo que en vez de usar la función add usamos remove

    /*Método para evaluar qué hacer en una casilla concreta. Parámetros:
     * - Jugador cuyo avatar está en esa casilla.
     * - La banca (para ciertas comprobaciones).
     * - El valor de la tirada: para determinar impuesto a pagar en casillas de servicios.
     * Valor devuelto: true en caso de ser solvente (es decir, de cumplir las deudas), y false
     * en caso de no cumplirlas.*/
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        incrementarVisitas();

        if (tipo.equalsIgnoreCase("solar")) { //comprobamos que si la casilla en la que hemos caido es un solar
            if (hipotecada) {
                return true;
            }
            if (duenho != banca && duenho != actual) {//Comprobamos dos cosas, primero que si el dueño no es la banca, de ser asi comprobamos que si el dueño no es el jugador que ha caido en ella

                float alquiler = calcularAlquiler(); //Creamos una variable llamada alquiler y le damos inicialmente el valor de impuesto, hacemos esto para que si el usuariotiene tienetodo el grupo no se cambie el valor de impuesto



                if (actual.getFortuna() >= alquiler) {  //Si el jugador tiene dinero para pagar el alquiler del solar

                    actual.sumarFortuna(-alquiler);  //Le restamos ealquiler al jugador
                    actual.sumarGastos(alquiler);    //Registramos dicha intervención
                    duenho.sumarFortuna(alquiler);//Le añadimos el valor del alquiler pagado al propietario del solar

                    actual.registrarPagoAlquiler(alquiler);
                    duenho.registrarCobroAlquiler(alquiler);
                    this.sumarRentabilidad(alquiler);

                    if(grupo!=null){
                        grupo.sumarRentabilidad(alquiler);
                    }

                    System.out.println("Se han pagado " + (long) alquiler + " € de alquiler a " + duenho.getNombre() + ".");


                    return true; //Confirmamos que el jugador pudo pagar su deuda
                }

                return false; //Confirmamos que el jugador no pudo pagar su deuda
            }
            return true; //En caso de que pertenezca a la abanca o no pertenezca a otro jugador confirmamos que no tiene que pagar nada
        }

        if (tipo.equalsIgnoreCase("servicio")) { //Comprobamos que si la casilla en la que hemos caido es un servicio

            if (hipotecada){
                return true;
            }

            if (duenho != banca && duenho != actual) { //Si el dueño es distinto de la banca o distinto del jugador

                float alquiler = 4 * tirada * Valor.FACTOR_SERVICIOS;  //El alquiler se multiplica por 4 y por la tirada del jugador

                if (actual.getFortuna() >= alquiler) { //Si la fortuna del jugador es mayor que el dinero que tiene que pagar de alquiler

                    actual.sumarFortuna(-alquiler); //Le restamos al jugador el dinero que tiene que pagar por el alquiler
                    actual.sumarGastos(alquiler);  //Guardamos la realización de dicha operación
                    duenho.sumarFortuna(alquiler); //Le sumamos al propietario el dinero del alquiler que ha pagado el jugador

                    actual.registrarPagoAlquiler(alquiler);
                    duenho.registrarCobroAlquiler(alquiler);
                    this.sumarRentabilidad(alquiler);

                    return true; //Confirmamos que el jugador ha pagado su deuda
                }

                return false;//Confirmamos que el jugador no pudo pagar su deuda
            }
        }

        if (tipo.equalsIgnoreCase("transporte")){ //Comprobamos que la casilla en la que hemos caido es la de transporte

            if (hipotecada){
                return true;
            }

            if (duenho != banca && duenho != actual){ //Si el dueño es diferente de la banca o del propio jugador

                float transporte = 250000; //El precio a pagar por caer en dicha casilla es de 250000

                if(actual.getFortuna() >= transporte){ //Comprobamos que el jugador tiene mas fortuna que lo que tiene que pagar

                    actual.sumarFortuna(-transporte); //Le quitamos al jugador el dinero del transporte
                    actual.sumarGastos(transporte); //Guardamos la operación que acabamos de hacer
                    duenho.sumarFortuna(transporte);//Le añadimos al dueño de dicha casilla el importe que se ha pagado

                    actual.registrarPagoAlquiler(transporte);
                    duenho.registrarCobroAlquiler(transporte);
                    this.sumarRentabilidad(transporte);

                    return true; //Confirmamos que el jugador pudo pagar el importe
                }

                return false; //Confirmamos que el jugador no pudo pagar el importe
            }
        }

        if (tipo.equalsIgnoreCase("suerte") || tipo.equalsIgnoreCase("comunidad")) { //Comprobamos que la casilla en la que hemos caido es la de suerte o caja de comunidad

            return true; //Por ahora no hacemos nada ya que es la primera entrega
        }

        if (tipo.equalsIgnoreCase("impuesto")){ //Comprobamos que en la casilla que hemos caido es la de impuestos

            if (actual.getFortuna() >= impuesto){ //Comprobamos que el jugador tiene dinero suficiente para pagar el impuesto

                actual.sumarFortuna(-impuesto); //Le restamos el valor del impuesto a la fortuna del jugador
                actual.sumarGastos(impuesto);  //Guardamos la operacion

                actual.registrarPagoTasasImpuesto(impuesto);

                System.out.println("El jugador " + actual.getNombre() + " paga " + (long) impuesto + " € que se depositan en el Parking.");


                return true;//Confirmamos que el jugador pudo pagar el impuesto
            }

            return false; //Confirmamos que el jugador no pudo pagar el impuesto
        }

        if (tipo.equalsIgnoreCase("especial") && nombre.equalsIgnoreCase("Parking")) { //Comprobamos que la casilla en la que hemos caido es la de parking

            actual.sumarFortuna(valor); //Le sumamos al jugador el valor del parking

            actual.registrarPremioInversionOBote(valor);

            System.out.println("El jugador " + actual.getNombre() + " recibe " + (long) valor + " € del Parking.");
            valor = 0; //Reiniciamos el valor del parking

            return true; //Confirmamos que se pudo realizar la operacion
        }

        if (tipo.equalsIgnoreCase("especial") && nombre.equalsIgnoreCase("Carcel")) { //Comprobamos que la casilla en la que estamos es carcel

            return true; //Confirmamos que no hay que hacer nada
        }

        if (tipo.equalsIgnoreCase("especial") && nombre.equalsIgnoreCase("Salida")) { //Comprobamos que la casilla en la que estamos es salida

            return true;
        }

        return true;
    }

    /*Método usado para comprar una casilla determinada. Parámetros:
     * - Jugador que solicita la compra de la casilla.
     * - Banca del monopoly (es el dueño de las casillas no compradas aún).*/
    public void comprarCasilla(Jugador solicitante, Jugador banca) {

        if (this.duenho == banca && solicitante.getFortuna() >= this.valor) {//Este if comprueba primero que si la casilla aun pertenece a la banca y segundo si el jugador tene dinero suficiente para pagarla.

            solicitante.sumarFortuna(-this.valor); //Le restamos el valor de la casilla al saldo del jugador
            solicitante.sumarGastos(this.valor); // Registramos que ha gastado ese dinero
            solicitante.registrarDineroInvertido(this.valor);

            banca.sumarFortuna(this.valor); //Añadimos el dinero gastado por el participante a la banca

            banca.eliminarPropiedad(this); //Eliminamos la propiedad de la banca
            solicitante.anhadirPropiedad(this); //Le añadimos la propiedad al jugador

            this.duenho = solicitante; //Adjudicamos que el dueño de dicha propiedad es el que la compro
        }
    }

    /*Método para añadir valor a una casilla. Utilidad:
     * - Sumar valor a la casilla de parking.
     * - Sumar valor a las casillas de solar al no comprarlas tras cuatro vueltas de todos los jugadores.
     * Este método toma como argumento la cantidad a añadir del valor de la casilla.*/
    public void sumarValor(float suma) {
        this.valor += suma;
    }

    /*Método para mostrar información sobre una casilla.
     * Devuelve una cadena con información específica de cada tipo de casilla.*/
    public String infoCasilla() {

        if (tipo.equalsIgnoreCase("impuesto")) { //Comprobamos que la casilla es impuesto
            return "{\n" +
                    "tipo: impuesto,\n" +
                    "a pagar: " + impuesto + "\n" +
                    "}";
        }

        if (tipo.equalsIgnoreCase("especial") && nombre.equalsIgnoreCase("Parking")) { //Comprobamos que la casilla es parking

            String jugadoresParking = ""; //Creamos una cadena vacia donde guardaremos los nombres de los jugadores que esten en parking

            for (Avatar av : avatares) { //Recorremos todos los avatares que esten situados en esta casilla

                if (!jugadoresParking.isEmpty()) { //Si la cadena de jugadores no esta vacia
                    jugadoresParking += ", "; //Añadimos una coma y un espacio antes de añadir otro jugador
                }

                jugadoresParking += av.getJugador().getNombre(); //Obtenemos el nombre del jugador al que pertenece ese avatar
            }

            return "{\n" +
                    "bote: " + valor + ",\n" +
                    "jugadores: [" + jugadoresParking + "]\n" +
                    "}";
        }
        if (tipo.equalsIgnoreCase("especial") && nombre.equalsIgnoreCase("Carcel")) { //Comprobamos que la casilla es carcel

            String jugadoresCarcel = ""; //Creamos una cadena vacia donde guardaremos los nombres de los jugadores que esten en la carcel

            for (Avatar av : avatares) { //Recorremos todos los avatares que esten situados en esta casilla

                Jugador jugador = av.getJugador(); //Cogemos el jugador propietario del avatar y lo guardamos en una variable llamada jugador

                if (!jugadoresCarcel.isEmpty()) { //Si la cadena de jugadores no esta vacia
                    jugadoresCarcel += " "; //Ponemos un espacio antes del siguiente
                }

                jugadoresCarcel += "[" +
                        jugador.getNombre() + "," +
                        jugador.getTiradasCarcel() + "]";
            }

            return "{\n" +
                    "salir: " + Valor.SALIR_CARCEL + ",\n" +
                    "jugadores: " + jugadoresCarcel + "\n" +
                    "}";
        }

        if (tipo.equalsIgnoreCase("especial") && nombre.equalsIgnoreCase("Salida")) { //Comprobamos que la casilla es Salida

            return "{\n" +
                    "tipo: especial,\n" +
                    "cantidad por vuelta: " + Valor.SUMA_VUELTA + "\n" +
                    "}";
        }

        if (tipo.equalsIgnoreCase("transporte")) { //Comprobamos que la casilla es transporte

            return "{\n" +
                    "tipo: transporte,\n" +
                    "propietario: " + duenho.getNombre() + ",\n" +
                    "valor: " + valor + ",\n" +
                    "alquiler: 250000\n" +
                    "}";
        }

        if (tipo.equalsIgnoreCase("servicio")) { //Comprobamos que la casilla es servicio

            return "{\n" +
                    "tipo: servicio,\n" +
                    "propietario: " + duenho.getNombre() + ",\n" +
                    "valor: " + valor + ",\n" +
                    "factor servicio: " + Valor.FACTOR_SERVICIOS + "\n" +
                    "}";
        }

        if (tipo.equalsIgnoreCase("solar")) { //Comprobamos que la casilla es un solar

            return "{\n" +
                    "tipo: solar,\n" +
                    "grupo: " + grupo.getNombreGrupo() + ",\n" +
                    "propietario: " + duenho.getNombre() + ",\n" +
                    "valor: " + valor + ",\n" +
                    "alquiler: " + impuesto + ",\n" +
                    "hipoteca: " + hipoteca + ",\n" +
                    "hipotecada: " + hipotecada + ",\n" +
                    "valor hotel: " + valorHotel + ",\n" +
                    "valor casa: " + valorCasa + ",\n" +
                    "valor piscina: " + valorPiscina + ",\n" +
                    "valor pista de deporte: " + valorPistaDeporte + ",\n" +
                    "alquiler casa: " + alquilerCasa + ",\n" +
                    "alquiler hotel: " + alquilerHotel + ",\n" +
                    "alquiler piscina: " + alquilerPiscina + ",\n" +
                    "alquiler pista de deporte: " + alquilerPistaDeporte + "\n" +
                    "}";
        }

        // SUERTE, COMUNIDAD e IrCarcel no se describen
        return "";
    }



    /* Método para mostrar información de una casilla en venta.
     * Valor devuelto: texto con esa información.
     */
    public String casEnVenta() { //funcion para devolver un texto corto con la información necesaria para mostrar una propiedad que está en venta.
        if (tipo.equalsIgnoreCase("solar")) { //Si la casilla es un solar
            return "{\n" +
                    "tipo: solar,\n" +
                    "grupo: " + grupo.getNombreGrupo() + ",\n" +
                    "valor: " + valor + "\n" +
                    "}";
        }

        if (tipo.equalsIgnoreCase("transporte")) { //Si la casilla es un trasnporte
            return "{\n" +
                    "tipo: transporte,\n" +
                    "valor: " + valor + "\n" +
                    "}";
        }

        if (tipo.equalsIgnoreCase("servicio")) { //Si la casilla es un servicio
            return "{\n" +
                    "tipo: servicio,\n" +
                    "valor: " + valor + "\n" +
                    "}";
        }

        return "";
    }



    public int getPosicion() {
        return this.posicion;
    }//Funcion para devolver en que casilla esta el jugador

    public String getNombre() {
        return this.nombre;
    }
    public void setDuenho(Jugador duenho){
        this.duenho = duenho; //Establece que jugador es el dueño
    }
    public Jugador getDuenho(){
        return this.duenho; //Devuelve que jugador es el dueño
    }
    public void setImpuesto(float impuesto) {
        this.impuesto = impuesto;
    } //Guarda en el atributo impuesto de esta casilla el valor que me han pasado como parámetro

    public float getImpuesto() {
        return this.impuesto; //Poder consultar desde el menu cuanto se ha pagado
    }

    public Grupo getGrupo() {
        return this.grupo; //Devuelve el grupo al que pertenece
    }

    public void setGrupo(Grupo grupo){
        this.grupo = grupo; //Asigna el grupo a la casilla
    }

    public String getTipo() {
        return this.tipo; //Sirve para consultar desde otra clase qué tipo de casilla es
    }

    public void setValoresEdificacion(float valorCasa, float valorHotel, float valorPiscina, float valorPistaDeporte, float alquilerCasa, float alquilerHotel, float alquilerPiscina, float alquilerPistaDeporte) { //Función para guardar de golpe todos los precios y alquileres relacionados con las edificaciones de un solar.

        this.valorCasa = valorCasa;
        this.valorHotel = valorHotel;
        this.valorPiscina = valorPiscina;
        this.valorPistaDeporte = valorPistaDeporte;

        this.alquilerCasa = alquilerCasa;
        this.alquilerHotel = alquilerHotel;
        this.alquilerPiscina = alquilerPiscina;
        this.alquilerPistaDeporte = alquilerPistaDeporte;
    }

    public void setHipoteca(float hipoteca) { //metodo para asignar la hipoteca
        this.hipoteca = hipoteca;
    }

    public ArrayList<Avatar> getAvatares() {
        return this.avatares;
    }
    public float getValor() {
        return this.valor;
    }

    public float getHipoteca() { //devuelve el dinero q vale hipotecar la propiedad
        return this.hipoteca;
    }

    public boolean isHipotecada() { //Para saber si esta hipotecada o no
        return this.hipotecada;
    }

    public void setHipotecada(boolean hipotecada) { //para marcarla como hipotecada
        this.hipotecada = hipotecada;
    }
    public float getRentabilidad() { //para devolver la rentabilidad
        return this.rentabilidad;
    }

    public void sumarRentabilidad(float cantidad) { //para aumentar la rentabilidad cada vez que pagan al caer
        this.rentabilidad += cantidad;
    }

    public int getVecesVisitada() { //para devolver las veces que se devolvio
        return this.vecesVisitada;
    }

    public ArrayList<Edificio> getEdificios(){return this.edificios; }

    public void incrementarVisitas() { //para aumar las veces que se visita una casilla
        this.vecesVisitada++;
    }
    public boolean esHipotecable() {
        return tipo.equalsIgnoreCase("solar") ||
                tipo.equalsIgnoreCase("transporte") ||
                tipo.equalsIgnoreCase("servicio");
    }
    public boolean puedeHipotecar(Jugador jugador) { //función para hipotecar

        if (!esHipotecable()) { //si no es hipotecable
            return false;
        }

        if (duenho != jugador) { //si el dueño es distinto del jugador
            return false;
        }

        if (hipotecada) { //si ya esta hipotecada
            return false;
        }

        if (tipo.equalsIgnoreCase("solar") && tieneEdificios()){
            return false;
        }

        return true;
    }
    public boolean puedeDeshipotecar(Jugador jugador) {

        if (!esHipotecable()) { //si no es hipotecable
            return false;
        }

        if (duenho != jugador) { //si el dueño no es el jugador
            return false;
        }

        if (!hipotecada) { //si no esta hipotecada
            return false;
        }

        if (jugador.getFortuna() < hipoteca) { //si el jugador no tiene dinero suficiente
            return false;
        }

        return true;
    }

    public void hipotecar(Jugador jugador){
        jugador.sumarFortuna(hipoteca);
        this.hipotecada = true;

    }
    public void deshipotecar (Jugador jugador){
        jugador.sumarFortuna(-hipoteca);
        this.hipotecada= false;
    }

    public boolean tieneEdificios(){
        if (this.getEdificios().isEmpty() ){
            return false;
        } else {
            return true;
        }

    }

    public int contarEdificios(String tipo){// cuenta cierto tipo de edificio en una casilla
        int contador = 0;
        for (Edificio e : edificios){
            if (e.getTipoEdificio().equalsIgnoreCase(tipo)){
                contador++;
            }
        }

        return contador;
    }

    public float getPrecioEdificio(String tipo){
        if (tipo.equalsIgnoreCase("casa")){
            return valorCasa;
        } else if (tipo.equalsIgnoreCase("piscina")){
            return valorPiscina;
        } else if (tipo.equalsIgnoreCase("hotel")){
            return valorHotel;
        } else if (tipo.equalsIgnoreCase("pista_deporte")) {
            return valorPistaDeporte;
        } return -1;
    }

    public void edificar(String tipo, Jugador jugador, Jugador banca){
        float precio = getPrecioEdificio(tipo);

        if(precio == -1){
            System.out.println("Tipo de edificio no valido.");
            return;
        }
        if(!this.tipo.equalsIgnoreCase("solar")){
            System.out.println("No se puede edificar en esta casilla.");
            return;
        }
        if(jugador != this.duenho){
            System.out.println("La casilla " + this.nombre + " no pertenece a " + jugador.getNombre());
            return;
        }
        if(!grupo.esDuenhoGrupo(jugador)){
            System.out.println(jugador.getNombre()+ " no es dueño de todo el grupo.");
            return;
        }
        if (grupo.tieneHipotecadas()){
            System.out.println("No se puede edificar porque hay una propiedad hipotecada en el grupo");
            return;
        }
        if(tipo.equalsIgnoreCase("casa")){
            if(contarEdificios("hotel") >= 1){
                System.out.println("Ya hay un hotel en la casilla");
                return;
            }
            if(contarEdificios("casa") >= 4){
                System.out.println("Ya hay 4 casas en la casilla");
                return;
            }
        }
        if(tipo.equalsIgnoreCase("hotel")){
            if(contarEdificios("hotel") >= 1){
                System.out.println("Ya hay un hotel en la casilla");
                return;
            }
            if(contarEdificios("casa") < 4){
                System.out.println("No hay 4 casas en la casilla");
                return;
            }
        }
        if(tipo.equalsIgnoreCase("piscina")){
            if(contarEdificios("hotel") < 1){
                System.out.println("Aun no hay un hotel en la casilla");
                return;
            }
            if(contarEdificios("piscina") >= 1){
                System.out.println("Ya hay una piscina en la casilla");
                return;
            }
        }
        if(tipo.equalsIgnoreCase("pista_deporte")){
            if(contarEdificios("hotel") < 1 || contarEdificios("piscina") < 1){
                System.out.println("Se necesita un hotel y una piscina para construir una pista de deporte.");
                return;
            }
            if (contarEdificios("pista_deporte") >= 1){
                System.out.println("Ya hay una pista de deporte");
                return;
            }
        }
        if(jugador.getFortuna() < precio){
            System.out.println("La fortuna de " + jugador.getNombre() + " no es suficiente para edificar un " + tipo
                    + " en la " + this.getNombre());
            return;
        }
        if(tipo.equalsIgnoreCase("hotel")){
            for (int i = edificios.size() - 1; i >= 0; i--){
                if(edificios.get(i).getTipoEdificio().equalsIgnoreCase("casa")){
                    edificios.remove(i);
                }
            }
        }

        jugador.sumarFortuna(-precio);
        jugador.sumarGastos(precio);
        banca.sumarFortuna(precio);
        jugador.registrarDineroInvertido(precio);

        Edificio edificio = new Edificio(tipo, this, jugador, precio);
        edificios.add(edificio);

        System.out.println("Se ha edificado un/a " + tipo + " en " + this.nombre + ". La fortuna de "
                + jugador.getNombre() + " se reduce en " + (long) precio + "€.");

    }

    public float getAlquilerEdificio(String tipo){

        if (tipo.equalsIgnoreCase("casa")){
            return alquilerCasa;
        } else if (tipo.equalsIgnoreCase("piscina")){
            return alquilerPiscina;
        } else if (tipo.equalsIgnoreCase("hotel")){
            return alquilerHotel;
        } else if (tipo.equalsIgnoreCase("pista_deporte")) {
            return alquilerPistaDeporte;
        } return -1;
    }

    public float calcularAlquiler(){

        if(!tieneEdificios()){
            float alquiler= this.impuesto;
            if(grupo != null && grupo.esDuenhoGrupo(duenho)){
                alquiler = alquiler*2;

            }
            return alquiler;
        }

        float total = 0;

        for(Edificio e: edificios){
            total = total + getAlquilerEdificio(e.getTipoEdificio());
        }
        return total;

    }

    public void venderEdificio(String tipo, int cantidad, Jugador jugador, Jugador banca ){
        float precio = getPrecioEdificio(tipo);

        if (precio == -1){
            System.out.println("Tipo de edificio no valido.");
            return;
        }

        if(duenho != jugador) {
            System.out.println("No se pueden vender " + tipo + " en " + this.getNombre() +
                    ". Esta propiedad no pertenece a " + jugador.getNombre() + ".");
            return;
        }

        if(this.contarEdificios(tipo) == 0){
            System.out.println("No hay ningún/a "+ tipo +" en "+ this.getNombre()+ ".");
            return;
        }

        int contarEdificios = contarEdificios(tipo);
        int aVender = Math.min(contarEdificios, cantidad);
        int vendidos = 0;

        Iterator<Edificio> it = edificios.iterator();

        while (it.hasNext() && vendidos < aVender) {
            Edificio e = it.next();
            if (e.getTipoEdificio().equalsIgnoreCase(tipo)) {
                it.remove();
                vendidos++;
            }
        }
        float dinero = precio * aVender;
        jugador.sumarFortuna(dinero);
        banca.sumarFortuna(-dinero);

        if (aVender< cantidad){
            System.out.println("Solamente se puede vender " + aVender + " " + tipo + ", recibiendo " + (long) dinero + "€.");
        } else{
            System.out.println(jugador.getNombre() + " ha vendido " + aVender + " " + tipo + " en " + this.nombre
                    + ", recibiendo " + (long) dinero + "€. En la propiedad quedan " + contarEdificios(tipo) + " " + tipo + ".");

        }

    }

    // Devuelve los ids de los edificios de un tipo, por ejemplo [casa-1, casa-2], o "-" si no hay.
    public String idsEdificios(String tipo) {
        String ids = "";
        for (Edificio e : edificios) {
            if (e.getTipoEdificio().equalsIgnoreCase(tipo)) {
                if (!ids.isEmpty()) {
                    ids += ", ";
                }
                ids += e.getId();
            }
        }
        if (ids.isEmpty()) {
            return "-";
        }
        return "[" + ids + "]";
    }

    // Devuelve la información de los edificios de esta casilla para 'listar edificios <color>'.
    public String infoEdificiosCasilla() {
        return "{\n" +
                "propiedad: " + this.nombre + ",\n" +
                "hoteles: " + idsEdificios("hotel") + ",\n" +
                "casas: " + idsEdificios("casa") + ",\n" +
                "piscinas: " + idsEdificios("piscina") + ",\n" +
                "pistasDeDeporte: " + idsEdificios("pista_deporte") + ",\n" +
                "alquiler: " + (long) calcularAlquiler() + "\n" +
                "}";
    }

}
