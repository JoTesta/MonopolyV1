package partida;

import java.util.ArrayList;

import monopoly.*;


public class Jugador {

    //Atributos:
    private String nombre; //Nombre del jugador
    private Avatar avatar; //Avatar que tiene en la partida.
    private float fortuna; //Dinero que posee.
    private float gastos; //Gastos realizados a lo largo del juego.
    private boolean enCarcel; //Será true si el jugador está en la carcel
    private int tiradasCarcel; //Cuando está en la carcel, contará las tiradas sin éxito que ha hecho allí para intentar salir (se usa para limitar el numero de intentos).
    private int vueltas; //Cuenta las vueltas dadas al tablero.
    private ArrayList<Casilla> propiedades; //Propiedades que posee el jugador.
    private float dineroInvertido;
    private float pagoTasasEImpuestos;
    private float pagoDeAlquileres;
    private float cobroDeAlquileres;
    private float pasarPorCasillaDeSalida;
    private float premiosInversionesOBote;
    private int vecesEnLaCarcel;

    //Constructor vacío. Se usará para crear la banca.
    public Jugador() {
        this.nombre = "Banca";
        this.fortuna = Valor.FORTUNA_BANCA;
        this.gastos = 0f;
        this.enCarcel = false;
        this.tiradasCarcel = 0;
        this.vueltas = 0;
        this.avatar = null;
        this.propiedades = new ArrayList<>();
        this.dineroInvertido = 0;
        this.pagoTasasEImpuestos = 0;
        this.pagoDeAlquileres = 0;
        this.cobroDeAlquileres = 0;
        this.pasarPorCasillaDeSalida = 0;
        this.premiosInversionesOBote = 0;
        this.vecesEnLaCarcel = 0;
    }

    /*Constructor principal. Requiere parámetros:
    * Nombre del jugador, tipo del avatar que tendrá, casilla en la que empezará y ArrayList de
    * avatares creados (usado para dos propósitos: evitar que dos jugadores tengan el mismo nombre y
    * que dos avatares tengan mismo ID). Desde este constructor también se crea el avatar.
     */
    public Jugador(String nombre, String tipoAvatar, Casilla inicio, ArrayList<Avatar> avCreados) {
        this.nombre = nombre;
        //avatar al final
        this.fortuna = Valor.FORTUNA_INICIAL;
        this.gastos = 0f;
        this.enCarcel = false;
        this.tiradasCarcel = 0;
        this.vueltas = 0;
        this.propiedades = new ArrayList<>();
        this.avatar = new Avatar(tipoAvatar,this, inicio,avCreados);
    }

    //Otros métodos:
    //Método para añadir una propiedad al jugador. Como parámetro, la casilla a añadir.
    public void anhadirPropiedad(Casilla casilla) {
        if (casilla!=null && !this.propiedades.contains(casilla)){
            this.propiedades.add(casilla);
        }
    }

    //Método para eliminar una propiedad del arraylist de propiedades de jugador.
    public void eliminarPropiedad(Casilla casilla) {
        if (casilla != null && this.propiedades.contains(casilla)){
            this.propiedades.remove(casilla);
        }
    }

    //Método para añadir fortuna a un jugador
    //Como parámetro se pide el valor a añadir. Si hay que restar fortuna, se pasaría un valor negativo.
    public void sumarFortuna(float valor) {
        this.fortuna += valor;
    }

    //Método para sumar gastos a un jugador.
    //Parámetro: valor a añadir a los gastos del jugador (será el precio de un solar, impuestos pagados...).
    public void sumarGastos(float valor) {
        this.gastos += valor;
    }

    //Método para sumar vueltas
    public void sumarVueltas(){
        this.vueltas++;
    }


    /*Método para establecer al jugador en la cárcel. 
    * Se requiere disponer de las casillas del tablero para ello (por eso se pasan como parámetro).*/
    public void encarcelar(ArrayList<ArrayList<Casilla>> pos) {
        this.enCarcel = true;
        registrarEntradaCarcel();
        this.tiradasCarcel = 0;
        Casilla carcel = pos.get(1).get(0);

        if (this.avatar!=null){
            if (this.avatar.getLugar()!=null){
                this.avatar.getLugar().eliminarAvatar(this.avatar);
            }
            this.avatar.setLugar(carcel);
            carcel.anhadirAvatar(this.avatar);
        }

    }

    //metodo para poner enCarcel a cualquier valor
    public void setEnCarcel(boolean enCarcel){
        this.enCarcel=enCarcel;
        if (!enCarcel){this.tiradasCarcel = 0;}
    }

    public void incrementarTiradasCarcel(){
        this.tiradasCarcel++;
    }


    //getters fundamentales.
    public String getNombre(){
        return this.nombre;
    }

    public Avatar getAvatar() {
        return this.avatar;
    }

    public float getFortuna() {
        return this.fortuna;
    }

    public float getGastos() {
        return this.gastos;
    }

    public boolean isEnCarcel() {
        return this.enCarcel;
    }

    public int getTiradasCarcel() {
        return this.tiradasCarcel;
    }

    public int getVueltas() {
        return this.vueltas;
    }

    public ArrayList<Casilla> getPropiedades() {
        return this.propiedades;
    }

    public String describir() {
        String props = propiedades.isEmpty() ? "-" : nombresPropiedades();
        return "{\n" +
                "  nombre: " + nombre + ",\n" +
                "  avatar: " + (avatar != null ? avatar.getId() : "-") + ",\n" +
                "  fortuna: " + (long) fortuna + ",\n" +
                "  propiedades: " + props + "\n" +
                "  hipotecas: " + nombresHipotecas() + "\n" +
                "  edificios: -\n" +
                "}";
    }

    private String nombresPropiedades() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < propiedades.size(); i++) {
            sb.append(propiedades.get(i).getNombre());
            if (i < propiedades.size() - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    public void declararBancarrota(Jugador acreedor) {
        for (Casilla c : new ArrayList<>(propiedades)) {
            c.setDuenho(acreedor);
            if (acreedor!=null) acreedor.anhadirPropiedad(c);
        }
        propiedades.clear();
        this.fortuna = 0;
        if (avatar != null && avatar.getLugar() != null) {
            avatar.getLugar().eliminarAvatar(avatar);
        }
    }
    public void registrarDineroInvertido(float cantidad){
        this.dineroInvertido += cantidad;
    }

    public void registrarPagoTasasImpuesto(float cantidad){
        this.pagoTasasEImpuestos += cantidad;
    }

    public void registrarPagoAlquiler ( float cantidad){
        this.pagoDeAlquileres += cantidad;
    }
    public void registrarCobroAlquiler (float cantidad){
        this.cobroDeAlquileres += cantidad;

    }
    public void registrarPremioInversionOBote(float cantidad) {
        this.premiosInversionesOBote += cantidad;
    }
    public void registrarEntradaCarcel(){
        this.vecesEnLaCarcel ++;
    }

    public float getDineroInvertido() {
        return this.dineroInvertido;
    }

    public float getPagoTasasEImpuestos() {
        return this.pagoTasasEImpuestos;
    }

    public float getPagoDeAlquileres() {
        return this.pagoDeAlquileres;
    }

    public float getCobroDeAlquileres() {
        return this.cobroDeAlquileres;
    }

    public float getPasarPorCasillaDeSalida() {
        return this.pasarPorCasillaDeSalida;
    }

    public float getPremiosInversionesOBote() {
        return this.premiosInversionesOBote;
    }

    public int getVecesEnLaCarcel() {
        return this.vecesEnLaCarcel;
    }

    public String mostrarEstadisticas(){
        return "{\n" +
                "dineroInvertido: " + dineroInvertido + ",\n" +
                "pagoTasasEImpuestos: " + pagoTasasEImpuestos + ",\n" +
                "pagoDeAlquileres: " + pagoDeAlquileres + ",\n" +
                "cobroDeAlquileres: " + cobroDeAlquileres + ",\n" +
                "pasarPorCasillaDeSalida: " + pasarPorCasillaDeSalida + ",\n" +
                "premiosInversionesOBote: " + premiosInversionesOBote + ",\n" +
                "vecesEnLaCarcel: " + vecesEnLaCarcel + "\n" +
                "}";
    }
    public void registrarPasoSalida(float cantidad) {
        this.pasarPorCasillaDeSalida += cantidad;
    }

    private String nombresHipotecas() {

        StringBuilder sb = new StringBuilder("[");
        boolean primera = true; //contador para saber si tenemos alguna hipotecada

        for (Casilla c : propiedades) { //recorremos propiedades

            if (c.isHipotecada()) { //si esta hipotecada

                if (!primera) { //y no es la primera
                    sb.append(", "); //la añade con una coma
                }

                sb.append(c.getNombre());
                primera = false;
            }
        }

        if (primera) {
            return "-";
        }

        sb.append("]");
        return sb.toString();
    }

    public Jugador jugadorEnCabeza(ArrayList<Jugador> jugadores) {

        Jugador mejor = null; //inicializamos la variable de cual es mejor
        float mayorFortunaTotal = -1;

        for (Jugador j : jugadores) { //recorremos los jugadores

            float fortunaTotal = j.getFortuna();

            for (Casilla c : j.getPropiedades()) {

                fortunaTotal += c.getValor();

                for (Edificio e : c.getEdificios()) {
                    fortunaTotal += e.getCoste();
                }
            }

            if (mejor == null || fortunaTotal > mayorFortunaTotal) {
                mejor = j;
                mayorFortunaTotal = fortunaTotal;
            }
        }

        return mejor;
    }



}
