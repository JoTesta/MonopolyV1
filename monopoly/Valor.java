package monopoly;


public class Valor {
    //Se incluyen una serie de constantes útiles para no repetir valores.
    public static final float FORTUNA_BANCA = Float.POSITIVE_INFINITY; // Cantidad que tiene inicialmente la Banca, infinito positivo
    public static final float FORTUNA_INICIAL = 15000000f; // Cantidad que recibe cada jugador al comenzar la partida
    public static final float SUMA_VUELTA = 2000000f; // Cantidad que recibe un jugador al pasar pos la Salida

    //añadidos los precios que faltan.
    public static final float SALIR_CARCEL = 500000f; // Cantidad a pagar para salir de la carcel
    public static final float PAGAR_IMPUESTOS = 2000000f;
    public static final float FACTOR_SERVICIOS = 50000f;


    //Colores del texto:
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

}
