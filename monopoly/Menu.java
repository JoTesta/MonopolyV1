package monopoly;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

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

    public Menu() {
        iniciarPartida();
        Scanner sc = new Scanner(System.in);
        while (true) {
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
        this.tablero = new Tablero(banca);
        this.turno = 0;
        this.lanzamientos = 0;
        this.tirado = false;
        this.solvente = true;
    }

    private void crearJugador(String nombre, String tipo) {
        if (jugadores.size() >= 4) {
            System.out.println("Ya hay 4 jugadores registrados, no se pueden añadir más");
            return;
        }
        for (Jugador j : jugadores) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {     //compara dos strings ignorando mayusculas y minusculas
                System.out.println("Ya existe un jugador con este nombre");
                return;
            }
        }
        String t = tipo.toLowerCase();                      //convierte tipo a minuscula
        if (!t.equals("coche") && !t.equals("sombrero") && !t.equals("esfinge") && !t.equals("pelota")) {
            System.out.println("Tipo de avatar inválido (coche, esfinge, sombrero o pelota). ");
            return;
        }
        Casilla salida = tablero.encontrar_casilla("Salida");
        Jugador nuevo = new Jugador(nombre, tipo, salida, avatares);
        jugadores.add(nuevo);
        System.out.println("{\n  nombre: " + nuevo.getNombre() +
                ",\n  avatar: " + nuevo.getAvatar().getId() + "\n}");
        System.out.println(tablero);

    }

    /*Método que interpreta el comando introducido y toma la accion correspondiente.
     * Parámetro: cadena de caracteres (el comando).
     */
    private void analizarComando(String comando) {
        String[] p = comando.trim().split("\\s+");      //quita los espacios innecesarios y hae un vector con cada palabra
        switch (p[0].toLowerCase()) {
            case "crear":
                if (p.length == 4 && p[1].equalsIgnoreCase("jugador")) crearJugador(p[2], p[3]);
                else System.out.println("Sintaxis: crear jugador <nombre> <tipo>");
                break;

            case "jugador":
                if (p.length == 1) mostrarJugadorActual();
                else System.out.println("Comando no válido.");
                break;

            case "listar":
                if (p.length < 2) {
                    System.out.println("Uso: listar jugadores|avatares|enventa");
                    break;
                }
                if (p[1].equalsIgnoreCase("jugadores")) listarJugadores();
                else if (p[1].equalsIgnoreCase("avatares")) listarAvatares();
                else if (p[1].equalsIgnoreCase("enventa")) listarVenta();
                else System.out.println("Comando no válido.");
                break;

            case "acabar":
                if (p.length == 2 && p[1].equalsIgnoreCase("turno")) {
                    acabarTurno();
                } else System.out.println("Sintaxis: Acabar turno");

                break;

            case "ver":
                System.out.println(tablero);
                break;

            case "lanzar":
                if (p.length == 2 && p[1].equalsIgnoreCase("dados")) {
                    lanzarDados(0, 0);
                    // tirada aleatoria
                } else if (p.length == 3 && p[1].equalsIgnoreCase("dados")) {
                    try {
                        String[] d = p[2].split("\\+");// separamos el texto por el +, y el try por si semete a+b || 5 || 9+12
                        int dado1Valor = Integer.parseInt(d[0]);
                        int dado2Valor = Integer.parseInt(d[1]);
                        if (dado1Valor < 1 || dado1Valor > 6 || dado2Valor < 1 || dado2Valor > 6) {
                            System.out.println("Los dados son de 1 a 6, por ejemplo 2+6");
                        } else lanzarDados(dado1Valor, dado2Valor);
                        // tirada forzada
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        // qué hacer SI falla (en vez de cerrar el programa)
                        System.out.println("Los dados son de 1 a 6, por ejemplo 2+6");
                    }
                } else {
                    System.out.println("Sintaxis: lanzar dados || lanzar dados X+Y");
                }
                break;

            case "describir":
                if ((p.length == 3) && p[1].equalsIgnoreCase("jugador")) {
                    descJugador(p);
                } else if ((p.length == 3) && p[1].equalsIgnoreCase("avatar")) {
                    descAvatar(p[2]);
                } else if ((p.length == 3) && p[1].equalsIgnoreCase("casilla")) {
                    descCasilla(p[2]);
                } else if ((p.length == 2)) {
                    descCasilla(p[1]);
                } else
                    System.out.println("Sintaxis: describir <casilla> | describir jugador <nombre> | describir avatar <id>");

                break;

            case "comprar":
                if ((p.length == 2)) {
                    comprar(p[1]);
                } else {
                    System.out.println("Sintaxis: comprar nombre_casilla");
                }
                break;

            case "salir":
                if (p.length == 1) {
                    System.exit(0); // Cierra el programa por completo
                } else if (p.length == 2 && (p[1].equalsIgnoreCase("cárcel") || p[1].equalsIgnoreCase("carcel"))) {
                    salirCarcel();
                } else {
                    System.out.println("Sintaxis: salir | salir carcel");
                }
                break;


            case "comandos":
                if (p.length == 2) {
                    ejecutarFichero(p[1]);

                } else {
                    System.out.println("Sintaxis: comandos <fichero>");
                }
                break;

            default:
                System.out.println("Comando no válido");
        }
    }

    /*Método que realiza las acciones asociadas al comando 'describir jugador'.
     * Parámetro: comando introducido
     */
    private void descJugador(String[] partes) {
        for (Jugador j : jugadores) {
            if(j.getNombre().equalsIgnoreCase(partes[2])) {
                System.out.println(j.describir());
                return;
            }

        }
        System.out.println("No existe el jugador " + partes[2]);
    }

    /*Método que realiza las acciones asociadas al comando 'describir avatar'.
     * Parámetro: id del avatar a describir.
     */
    private void descAvatar(String ID) {
        for (Avatar a : avatares) {
            if(a.getId().equalsIgnoreCase(ID)) {
                System.out.println(a.describir());
                return;
            }

        }
        System.out.println("No existe el avatar " + ID);

    }

    /* Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.
     * Parámetros: nombre de la casilla a describir.
     */
    private void descCasilla(String nombre) {
        Casilla c=tablero.encontrar_casilla(nombre);
        if(c == null){
            System.out.println("No existe la casilla " + nombre);
        }else {
            String info = c.infoCasilla();
            if (info.equalsIgnoreCase("")) {
                System.out.println("La casilla " + nombre + " no se puede describir");
            } else {
                System.out.println(info);
            }
        }
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
    private void lanzarDados(int d1, int d2) {

        Jugador actual = jugadores.get(turno); //Coge de la lista jugadores al jugador que tiene actualmente el turno.
        //comprobación de que no haya tirado ya los dados.
        if (tirado){
            System.out.println("El jugador "+actual.getNombre()+ " ya ha tirado los dados.");
            return;
        }

        //obtener valor de los dados.
        int valorDado1;
        int valorDado2;

        if (d1 == 0 && d2 == 0) {
            valorDado1 = dado1.hacerTirada(); //llama a la función public int hacerTirada() que devuelve un numero aleatorio del 1 al 6
            valorDado2 = dado2.hacerTirada(); //hace lo mismo
        } else {
            valorDado1 = d1;
            valorDado2 = d2;
        }

        int tirada = valorDado1 + valorDado2; // suma los dos valores y los guarda en tirada
        boolean dobles = valorDado1==valorDado2;
        if (actual.isEnCarcel()){
            if (dobles){
                System.out.println("¡Has sacado dobles! (" +valorDado1+"+"+valorDado2+"). Sales de la carcel gratis.");
                actual.setEnCarcel(false);
            }
            else{
                actual.incrementarTiradasCarcel();
                System.out.println("No has sacado dobles. Turnos en la carcel: "+ actual.getTiradasCarcel());
                if(actual.getTiradasCarcel()==3){
                    if (actual.getFortuna()>=Valor.SALIR_CARCEL){
                        System.out.println("Llevas ya tres turnos en la carcel. Pagas 500.000€ obligatoriamente y sales de la carcel");
                        actual.sumarFortuna(-Valor.SALIR_CARCEL);
                        actual.sumarGastos(Valor.SALIR_CARCEL);
                        actual.setEnCarcel(false);
                    }
                    else{
                        System.out.println("Llevas 3 turnos y no tienes 500.000€ para salir de la cárcel. " + actual.getNombre() + " se declara en bancarrota.");
                        actual.declararBancarrota(banca);
                        tirado = true;
                        return; // Se acaba su turno y su partida
                    }

                } else {
                    tirado = true; // Pierde el turno y no se mueve
                    return;
                }
            }
        }

        //logica para los 3 lanzamientos dobles seguidos
        if (dobles){
            lanzamientos++;
            if (lanzamientos==3){
                System.out.println("Has sacado 3 dobles seguidos! Vas directamente a la carcel.");
                actual.encarcelar(tablero.getPosiciones());
                tirado = true;
                System.out.println(tablero);
                return;
            }
        }
        else{
            tirado = true;
        }

        actual.getAvatar().moverAvatar(tablero.getPosiciones(), tirada); //mueve el avatar de Pedro tantas posiciones como haya salido en los dados.

        Casilla casillaActual = actual.getAvatar().getLugar(); // preguntamos después de moverlo en que casilla esta

        if (casillaActual.getNombre().equalsIgnoreCase("IrCarcel")) {
            actual.encarcelar(tablero.getPosiciones());
            tirado= true;

        } else {
            solvente = casillaActual.evaluarCasilla(actual, banca, tirada);//Evalúa lo que ocurre en la casilla donde acaba de caer el jugador y guarda si pudo pagar sus deudas.
            if (!solvente) {
                System.out.println("No tienes dinero suficiente. Debes hipotecar alguna propiedad o declararte en bancarrota.");
            }
            if (solvente && casillaActual.getTipo().equalsIgnoreCase("impuesto")) {
                Casilla parking = tablero.encontrar_casilla("Parking");
                if (parking != null) {
                    parking.sumarValor(casillaActual.getImpuesto());
                }
            }
        }
        //repetir tirada en caso de lanzar dobles.
        if (dobles && !actual.isEnCarcel()){
            System.out.println("¡Has sacado dobles! ("+ valorDado1 + "+" + valorDado2 + "). Vuelve a lanzar los dados.");
            tirado = false;//se le permite tirar otra vez
        }
        System.out.println(tablero);

    }

    /*Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
     * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    private void comprar(String nombre) {
        Jugador actual = jugadores.get(turno);
        Casilla objetivo = tablero.encontrar_casilla(nombre);

        //comprobacion !null
        if (objetivo == null){
            System.out.println("No existe la casilla "+nombre);
            return;
        }
        if (!objetivo.getTipo().equalsIgnoreCase("solar") &&
                !objetivo.getTipo().equalsIgnoreCase("servicio") &&
                !objetivo.getTipo().equalsIgnoreCase("transporte")) {

            System.out.println("La casilla " + nombre + " no se puede comprar.");
            return;
        }
        //comprobar que el jugador está en esta casilla
        if (!actual.getAvatar().getLugar().getNombre().equalsIgnoreCase(nombre)){
            System.out.println("No puedes comprar "+nombre+ " porque no estás en ella.");
            return;
        }
        //comprobar que la casilla pertenece a la banca
        if (objetivo.getDuenho()!=banca){
            System.out.println("La casilla "+ nombre+ " no pertenece a la banca");
            return;
        }
        //comprobar si es comprable
        if (actual.getFortuna()<objetivo.getValor()){
            System.out.println("No tienes dinero suficiente para comprar "+ nombre);
            return;
        }
        //ejecutar la compra con el método que tenemos en casilla
        objetivo.comprarCasilla(actual,banca);
        System.out.println("El jugador "+actual.getNombre() + " compra la casilla "+ nombre + " por " + objetivo.getValor() + " €. Su fortuna actual es de "+ actual.getFortuna()+ " €.");
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
    private void salirCarcel() {
        Jugador actual = jugadores.get(turno);
        //comprobacion de que esté en la carcel actualmente
        if (!actual.isEnCarcel()){
            System.out.println("El jugador "+ actual.getNombre()+ " no se encuentra en la carcel.");
            return;
        }
        if(tirado){
            System.out.println("El jugador "+ actual.getNombre()+ " ya ha tirado los dados.");
            return;
        }
        if(actual.getFortuna()<Valor.SALIR_CARCEL){
            System.out.println("El jugador " + actual.getNombre()+" no tiene el dinero necesario para salir de la carcel.");
            return;
        }
        actual.sumarFortuna(-Valor.SALIR_CARCEL);
        actual.sumarGastos(Valor.SALIR_CARCEL);
        actual.setEnCarcel(false);
        System.out.println("El jugador "+ actual.getNombre()+ " paga "+ Valor.SALIR_CARCEL + " € y sale de la carcel. Puede lanzar los dados.");

    }

    // Método que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla c : lado) {
                if (c.getDuenho() == banca && !(c.casEnVenta().isEmpty()) ){
                    System.out.println(c.casEnVenta());
                }
            }
        }

    }

    //metodo privado para mostrar el jugador actual

    private void mostrarJugadorActual() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores");
            return;
        }
        Jugador j = jugadores.get(turno);
        System.out.println("{\n  nombre: " + j.getNombre() +
                ",\n  avatar: " + j.getAvatar().getId() + "\n}");
    }

    // Método que realiza las acciones asociadas al comando 'listar jugadores'.
    private void listarJugadores() {
        if (jugadores.isEmpty()){
            System.out.println("No hay jugadores");
        }else {
            for (Jugador j : jugadores) {
                System.out.println(j.describir());
            }
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar avatares'.
    private void listarAvatares() {
        if (avatares.isEmpty()){
            System.out.println("No hay avatares");
        }else {
            for (Avatar a : avatares) {
                System.out.println(a.describir());
            }
        }
    }

    // Método que realiza las acciones asociadas al comando 'acabar turno'.
    private void acabarTurno() {
        if (!tirado) {
            System.out.println("No puedes acabar el turno sin lanzar los dados.");
            return;
        }
        //pasarle el turno al siguiente jugador
        turno = (turno + 1) % jugadores.size();
        //preparar el siguiente turno
        tirado = false;
        lanzamientos = 0;

        Jugador actual = jugadores.get(turno);
        System.out.println("El jugador actual es "+ actual.getNombre());
    }


//

    //Metodo para leer y ejecutar comandos desde un .txt
    private void ejecutarFichero(String fichero) {
        try {
            File archivo = new File(fichero);
            Scanner lector = new Scanner(archivo);

            while (lector.hasNextLine()) {
                String linea = lector.nextLine().trim();
                if (!linea.isEmpty()) {
                    System.out.println("$> " + linea);      //se imprime para que se vea qué está  haciendo el script
                    analizarComando(linea);
                }
            }
            lector.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: No se ha encontrado el archivo '" + fichero + "'.");
        }
    }
}