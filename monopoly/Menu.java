package monopoly;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import partida.*;



public class Menu {

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    private int turno = 0; //Índice correspondiente a la posición en el arrayList del jugador (y el avatar) que tienen el turno
    private int lanzamientos; //Variable para contar el número de lanzamientos de un jugador en un turno.
    private Tablero tablero; //Tablero en el que se juega.
    private Dado dado1; //Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private Jugador banca; //El jugador banca.
    private boolean tirado; //Booleano para comprobar si el jugador que tiene el turno ha tirado o no.
    private boolean solvente; //Booleano para comprobar si el jugador que tiene el turno es solvente, es decir, si ha pagado sus deudas.

    public menu(){
        iniciarPartida();
        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.print("$> ");
            if (!sc.hasNextLine()) break;
            String linea = sc.nextLine().trim();
            if (linea.isEmpty()) continue;
            if (linea.equalsIgnoreCase("salir")) break;
            analizarComando(linea);
        }
    }

    // Método para inciar una partida: crea los jugadores y avatares.
    private void iniciarPartida() {
        this.dado1 = new Dado();
        this.dado2 = new Dado();//Inicializamos los dados
        this.jugadores = new ArrayList<>();
        this.avatares = new ArrayList<>();
        this.banca = new Jugador();
        this.tablero = new Tablero();
        this.turno = 0;
        this.lanzamientos = 0;
        this.tirado = false;
        this.solvente = true;
    }
    
    /*Método que interpreta el comando introducido y toma la accion correspondiente.
    * Parámetro: cadena de caracteres (el comando).
    */
    private void analizarComando(String comando) {
        String[] p = comando.trim().split("\\s+");      //quita los espacios innecesarios y hae un vector con cada palabra
        switch(p[0].toLowerCase()){
            case "crear":
                if (p.length == 4 && p[1].equalsIgnoreCase("jugador")) crearJugador(p[2], p[3]);
                else System.out.println("Sintaxis: crear jugador <nombre> <tipo>");
                break;

            case "jugador":
                if (p.length == 1) mostrarJugadorActual();
                else System.out.println("Comando no válido.");
                break;

            case "listar":
                if (p.length < 2) { System.out.println("Uso: listar jugadores|avatares|enventa"); break; }
                if (p[1].equalsIgnoreCase("jugadores")) listarJugadores();
                else if (p[1].equalsIgnoreCase("avatares")) listarAvatares();
                else if (p[1].equalsIgnoreCase("enventa")) listarVenta();
                else System.out.println("Comando no válido.");
                break;

            case "acabar":
                acabarTurno();
                break;

            case "ver":
                System.out.println(tablero);
                break;

            case "lanzar": //Hacer más adelante
                break;

            case "describir": //más adelante
                descJugador(p);
                break;

            case "comprar":
                if (p.length == 2) comprar(p[1]);
                break;

            case "salir":
                salirCarcel();
                break;

            default: System.out.println("Comando no válido");
        }
    }

    /*Método que realiza las acciones asociadas al comando 'describir jugador'.
    * Parámetro: comando introducido
     */
    private void descJugador(String[] partes) {
    }

    /*Método que realiza las acciones asociadas al comando 'describir avatar'.
    * Parámetro: id del avatar a describir.
    */
    private void descAvatar(String ID) {
    }

    /* Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.
    * Parámetros: nombre de la casilla a describir.
    */
    private void descCasilla(String nombre) {
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
    private void lanzarDados() {
        int valorDado1 = dado1.hacerTirada(); //llama a la función public int hacerTirada() que devuelve un numero aleatorio del 1 al 6
        int valorDado2 = dado2.hacerTirada(); //hace lo mismo

        int tirada = valorDado1 + valorDado2; // suma los dos valores y los guarda en tirada
        Jugador actual = jugadores.get(turno); //Coge de la lista jugadores al jugador que tiene actualmente el turno.

        actual.getAvatar().moverAvatar(tablero.getPosiciones(), tirada); //mueve el avatar de Pedro tantas posiciones como haya salido en los dados.

        Casilla casillaActual = actual.getAvatar().getLugar(); // preguntamos después de moverlo en que casilla esta

        actual.getAvatar().moverAvatar(tablero.getPosiciones(), tirada);
        if (casillaActual.getNombre().equalsIgnoreCase("IrCarcel")) {
            actual.encarcelar(tablero.getPosiciones());
        }
        else{
            solvente = casillaActual.evaluarCasilla(actual, banca, tirada);//Evalúa lo que ocurre en la casilla donde acaba de caer el jugador y guarda si pudo pagar sus deudas.
            if (solvente && casillaActual.getTipo().equalsIgnoreCase("impuesto")){
                Casilla parking = tablero.encontrar_casilla("Parking");
                if (parking != null){
                    parking.sumarValor(casillaActual.getImpuesto());
                }
            }
        }

        }

    /*Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
    * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    private void comprar(String nombre) {
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'. 
    private void salirCarcel() {
    }

    // Método que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
    }

    // Método que realiza las acciones asociadas al comando 'listar jugadores'.
    private void listarJugadores() {
    }

    // Método que realiza las acciones asociadas al comando 'listar avatares'.
    private void listarAvatares() {
    }

    // Método que realiza las acciones asociadas al comando 'acabar turno'.
    private void acabarTurno() {
    }

}
