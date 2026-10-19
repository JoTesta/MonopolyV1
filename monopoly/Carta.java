package monopoly;

import partida.*;                       //importamos partida para usar jugador avatar y dado.

public class Carta {
    private String tipo;
    private int numero;                 //numero asignado a esa carta, 1 a 7 en suerte, y 1 a 6 en comunidad
    private String descripcion;         //descripción que se le muestra al jugador


    //no hace falta constructor vacio ya que no hay arraylist.
    public Carta(String tipo, int numero, String descripcion){
        this.tipo = tipo;
        this.numero = numero;
        this.descripcion = descripcion;
    }

    //getters y setters
    public String getTipo(){
        return this.tipo;
    }

    public void setTipo(String tipo){
        this.tipo =tipo;
    }

    public int getNumero(){
        return this.numero;
    }

    public void setNumero(int numero){
        this.numero = numero;
    }

    public String getDescripcion(){
        return this.descripcion;
    }

    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }
}
