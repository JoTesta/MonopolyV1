package monopoly;

import partida.Jugador;

public class Edificio {

    //atributos

    private String tipoEdificio;
    private String id;
    private Jugador propietario;
    private float coste;
    private Casilla casilla;

    // contadores compartidos
    private static int numCasas = 0;
    private static int numHoteles = 0;
    private static int numPiscinas = 0;
    private static int numPistas = 0;




// no hace falta constructor vacio

    public Edificio(String tipoEdificio, Casilla casilla, Jugador propietario, float coste){

        this.tipoEdificio = tipoEdificio;
        this.casilla = casilla;
        this.propietario = propietario;
        this.coste = coste;

        if(tipoEdificio.equalsIgnoreCase("casa")){
            numCasas++;
            this.id = "casa-" + numCasas;
        } else if (tipoEdificio.equalsIgnoreCase("hotel")) {
            numHoteles++;
            this.id = "hotel-" + numHoteles;
        } else if (tipoEdificio.equalsIgnoreCase("piscina")){
            numPiscinas++;
            this.id = "piscina-" + numPiscinas;
        } else if (tipoEdificio.equalsIgnoreCase("pista_deporte")) {
            numPistas++;
            this.id = "pista-" + numPistas;
        }

    }


    //setters y getters

    public void setPropietario(Jugador nuevoPropietario){
        if (nuevoPropietario != null) {
            this.propietario = nuevoPropietario;
        }
    }

    public String getId(){
        return this.id;
    }

    public String getTipoEdificio(){
        return this.tipoEdificio;
    }

    public Jugador getPropietario(){
        return this.propietario;
    }


    public Casilla getCasilla(){
        return this.casilla;
    }

    public float getCoste(){
        return this.coste;
    }

    public String infoEdificio(){

        return "{ \nId: " + this.getId() + ",\n" + "Propietario: " + this.getPropietario().getNombre() + ",\n" +
                "Casilla: " + this.getCasilla().getNombre() + ",\n" + "Grupo: " + this.getCasilla().getGrupo().getNombreGrupo() + ",\n" +
                "Coste: " + (long) this.getCoste() + "\n }";
    }





}