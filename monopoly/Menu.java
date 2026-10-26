package monopoly;

import java.lang.reflect.Array;
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
    private ArrayList<Carta> mazoSuerte; //ArrayList con las cartas de suerte
    private ArrayList<Carta> mazoComunidad; //ArrayList con cartas de comunidad
    private int contSuerte = 0;
    private int contComunidad = 0;
    private float deudaPendiente = 0;
    private Jugador acreedorPendiente = null;


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
        this.mazoComunidad = new ArrayList<>();
        this.mazoSuerte = new ArrayList<>();
        this.contSuerte = 0;
        this.contComunidad = 0;
        crearMazos();
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

    private void crearMazos(){
        //primero creamos las de Suerte
        mazoSuerte.add(new Carta("Suerte",1,"Decides hacer un viaje de placer. Avanza hasta Solar19. Si pasas por la casilla de Salida, cobra 2.000.000€."));
        mazoSuerte.add(new Carta("Suerte", 2, "Los acreedores te persiguen por impago. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar los 2.000.000€."));
        mazoSuerte.add(new Carta("Suerte", 3, "¡Has ganado el bote de la lotería! Recibe 1.000.000€."));
        mazoSuerte.add(new Carta("Suerte", 4, "Has sido elegido presidente de la junta directiva. Paga a cada jugador 250.000€."));
        mazoSuerte.add(new Carta("Suerte", 5, "¡Hora punta de tráfico! Retrocede tres casillas."));
        mazoSuerte.add(new Carta("Suerte", 6, "Te multan por usar el móvil mientras conduces. Paga 150.000€."));
        mazoSuerte.add(new Carta("Suerte", 7, "Avanza hasta la casilla de transporte más cercana. Si no tiene dueño, puedes comprarla. Si tiene dueño, paga al dueño el doble de la operación indicada."));

        //después creamos las de Comunidad
        mazoComunidad.add(new Carta("Comunidad", 1, "Paga 500.000€ por un fin de semana en un balneario de 5 estrellas."));
        mazoComunidad.add(new Carta("Comunidad", 2, "Te investigan por fraude de identidad. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar los 2.000.000€."));
        mazoComunidad.add(new Carta("Comunidad", 3, "Colócate en la casilla de Salida. Cobra 2.000.000€."));
        mazoComunidad.add(new Carta("Comunidad", 4, "Devolución de Hacienda. Cobra 500.000€."));
        mazoComunidad.add(new Carta("Comunidad", 5, "Retrocede hasta Solar1 para comprar antigüedades exóticas."));
        mazoComunidad.add(new Carta("Comunidad", 6, "Ve a Solar20 para disfrutar del San Fermín. Si pasas por la casilla de Salida, cobra 2.000.000€."));
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

            case "edificar":
                if (p.length == 2) {
                    edificar(p[1]);
                } else {
                    System.out.println("Sintaxis: edificar casa | hotel | piscina | pista_deporte");
                }
                break;

            case "vender":
                if (p.length == 4) {
                    try {
                        int cantidad = Integer.parseInt(p[3]);
                        vender(p[1], p[2], cantidad);
                    } catch (NumberFormatException e) {
                        System.out.println("La cantidad tiene que ser un número, por ejemplo: vender casas Solar5 2");
                    }
                } else {
                    System.out.println("Sintaxis: vender <tipo> <casilla> <cantidad>");
                }
                break;

            case "listar":
                if (p.length < 2) {
                    System.out.println("Uso: listar jugadores|avatares|enventa|edificios");
                    break;
                }
                if (p[1].equalsIgnoreCase("jugadores")) listarJugadores();
                else if (p[1].equalsIgnoreCase("avatares")) listarAvatares();
                else if (p[1].equalsIgnoreCase("enventa")) listarVenta();
                else if (p[1].equalsIgnoreCase("edificios") && p.length == 2) listarEdificios();
                else if (p[1].equalsIgnoreCase("edificios") && p.length == 3) listarEdificiosGrupo(p[2]);
                else System.out.println("Comando no válido.");
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

            case "declarar":
                if (p.length == 2 && p[1].equalsIgnoreCase("bancarrota")){declararBancarrota();}
                else{System.out.println("Sintaxis: declarar bancarrota");}
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
        if (jugadores.size() < 2) {
            System.out.println("Hacen falta al menos 2 jugadores para jugar.");
            return;
        }

        Jugador actual = jugadores.get(turno); //Coge de la lista jugadores al jugador que tiene actualmente el turno.
        if (!solvente) {
            System.out.println("Tienes una deuda pendiente. Hipoteca una propiedad o declara bancarrota.");
            return;
        }
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
                        eliminarJugador(actual,banca);
                        return;
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

        if (actual.isEnCarcel()) {          // ha caído en IrCarcel
            System.out.println("El avatar se coloca en la casilla de Cárcel.");
            tirado = true;

        } else {
            evaluarDestino(actual, casillaActual, tirada);

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
        if (jugadores.size() < 2) {
            System.out.println("Hacen falta al menos 2 jugadores para jugar.");
            return;
        }
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

    // Método que ejecuta el comando 'edificar <tipo>'.
    private void edificar(String tipo) {
        if (jugadores.size() < 2) {
            System.out.println("Hacen falta al menos 2 jugadores para jugar.");
            return;
        }
        Jugador actual = jugadores.get(turno);
        Casilla casillaActual = actual.getAvatar().getLugar();
        casillaActual.edificar(tipo, actual, banca);
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
    private void salirCarcel() {
        if (jugadores.size() < 2) {
            System.out.println("Hacen falta al menos 2 jugadores para jugar.");
            return;
        }
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
                if (c.getDuenho() == banca && !(c.casEnVenta().isEmpty())) {
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
        if (jugadores.size() < 2) {
            System.out.println("Hacen falta al menos 2 jugadores para jugar.");
            return;
        }
        if (!solvente) {
            System.out.println("No puedes acabar el turno con una deuda pendiente. Hipoteca una propiedad o declara bancarrota.");
            return;
        }
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


    //metodo para sacar una carta del mazo.
    private void elegirCarta(Jugador jugadorActual, String tipo){
        Carta carta;
        if(tipo.equalsIgnoreCase("suerte")){
            carta = mazoSuerte.get(contSuerte);
            contSuerte = (contSuerte+1)%mazoSuerte.size();
        }
        else if (tipo.equalsIgnoreCase("comunidad")) {
            carta = mazoComunidad.get(contComunidad);
            contComunidad = (contComunidad+1) % mazoComunidad.size();
        }
        else {
            System.out.println("Tipo de carta no válido ("+tipo+").");
            return;
        }

        //mostrar por pantalla.
        System.out.println(jugadorActual.getNombre() + ", elige una carta: "+ carta.getNumero()+".");
        System.out.println("Acción: "+carta.getDescripcion());
        ejecutarCarta(carta,jugadorActual);
    }

    private void ejecutarCarta(Carta carta, Jugador jugador){
        String tipo = carta.getTipo();
        if (tipo.equalsIgnoreCase("suerte")){
            switch (carta.getNumero()){
                case 1: //avanza a solar19
                    moverAcasilla(jugador,tablero.encontrar_casilla("Solar19"),true);
                    break;

                case 2: //carcel
                    jugador.encarcelar(tablero.getPosiciones());
                    tirado = true;
                    System.out.println("El jugador "+ jugador.getNombre()+ " va directamente a la cárcel sin pasar por la salida");
                    break;

                case 3: //recibe 1.000.000€
                    jugador.sumarFortuna(1000000);
                    System.out.println(jugador.getNombre() + " recibe 1000000€.");
                    break;

                case 4: //paga 250.000€ a cada jugador
                    float total = 250000*(jugadores.size()-1);      //calcula el total a pagar;
                    if (jugador.getFortuna()<total){
                        registrarDeuda(total, null);

                    }
                    else{
                        for (Jugador acreedor : jugadores){
                            if (acreedor!=jugador){
                                pagar(jugador, 250000, acreedor);
                            }
                        }
                    }
                    break;

                case 5: //retrocede 3 casillas
                    int posactual = jugador.getAvatar().getLugar().getPosicion();
                    int nueva = (posactual - 1 - 3 + 40) % 40 + 1;      //+40 para que no haya numeros negativos
                    moverAcasilla(jugador, tablero.obtenerCasilla(nueva),false);    //al retroceder, no cobra salida.
                    break;

                case 6: //paga 150.000€
                    pagar(jugador, 150000,banca);
                    break;

                case 7: //transporte más cercano
                    transporteMasCercano(jugador);
                    break;

                default:
                    System.out.println("Carta de suerte no válida.");
            }
        }
        else if (tipo.equalsIgnoreCase("comunidad")){
            switch(carta.getNumero()){
                case 1: //paga 500.000
                    pagar(jugador,500000,banca);
                    break;

                case 2: //carcel
                    jugador.encarcelar(tablero.getPosiciones());
                    tirado = true;
                    System.out.println("El jugador "+ jugador.getNombre()+ " va directamente a la cárcel sin pasar por la salida");
                    break;

                case 3: //ir a salida
                    moverAcasilla(jugador, tablero.encontrar_casilla("Salida"),true);
                    break;

                case 4: //cobra 500.000€
                    jugador.sumarFortuna(500000);
                    System.out.println(jugador.getNombre() + " recibe 500000€.");
                    break;

                case 5: //retrocede hasta solar1
                    moverAcasilla(jugador,tablero.encontrar_casilla("Solar1"),false);
                    break;

                case 6: //ve a solar20
                    moverAcasilla(jugador,tablero.encontrar_casilla("Solar20"),true);
                    break;

                default:
                    System.out.println("Carta de comunidad no válida.");
            }
        }
        else {
            System.out.println("Tipo de carta no válido ("+tipo+").");
            return;
        }
    }

    private boolean pagar(Jugador deudor,float cantidad, Jugador acreedor){
        if (deudor.getFortuna()<cantidad){
            System.out.println("No tienes dinero suficiente. Debes hipotecar alguna propiedad o declararte en bancarrota.");
            solvente = false;
            deudaPendiente = cantidad;
            acreedorPendiente = acreedor;
            return false;
        }
        else {
            deudor.sumarGastos(cantidad);
            deudor.sumarFortuna(-cantidad);
            if (acreedor != banca) {
                acreedor.sumarFortuna(cantidad);
            }
            System.out.println(deudor.getNombre() + " paga " + (long) cantidad + "€ a " + acreedor.getNombre() + ".");
            return true;
        }
    }

    //mueve al jugador a la casilla deseada, y le paga en funcion de si pasa por la salida y si cobrarSalida es true.
    private void moverAcasilla(Jugador jugador, Casilla destino, boolean cobraSalida){
        Avatar av = jugador.getAvatar();
        Casilla origen = av.getLugar();

        if (cobraSalida && destino.getPosicion()<origen.getPosicion()){
            jugador.sumarFortuna(Valor.SUMA_VUELTA);
            jugador.sumarVueltas();
            System.out.println("El jugador " + jugador.getNombre() + " pasa por la Salida y recibe 2000000€.");
        }
        origen.eliminarAvatar(av);
        av.setLugar(destino);
        destino.anhadirAvatar(av);
        System.out.println(jugador.getNombre()+" se mueve hasta la casilla "+destino.getNombre()+ ".");

        evaluarDestino(jugador,destino, 0);
    }

    private void evaluarDestino(Jugador jugador, Casilla destino, int tirada){
        String t = destino.getTipo();
        if (t.equalsIgnoreCase("suerte") || t.equalsIgnoreCase("comunidad")) {
            elegirCarta(jugador, t);
            return;
        }
        solvente = destino.evaluarCasilla(jugador, banca, tirada);
        if (!solvente) {
            float cantidad = 0;
            Jugador acreedor = banca;
            if (t.equalsIgnoreCase("solar")) {
                cantidad = destino.calcularAlquiler();
                acreedor = destino.getDuenho();
            } else if (t.equalsIgnoreCase("servicio")) {
                cantidad = 4 * tirada * Valor.FACTOR_SERVICIOS;
                acreedor = destino.getDuenho();
            } else if (t.equalsIgnoreCase("transporte")) {
                cantidad = 250000;
                acreedor = destino.getDuenho();
            } else if (t.equalsIgnoreCase("impuesto")) {
                cantidad = destino.getImpuesto();
            }
            registrarDeuda(cantidad, acreedor);
        } else if (t.equalsIgnoreCase("impuesto")) {
            Casilla parking = tablero.encontrar_casilla("Parking");
            if (parking != null) parking.sumarValor(destino.getImpuesto());
        }
    }

    private void transporteMasCercano(Jugador jugador){
        int pos = jugador.getAvatar().getLugar().getPosicion();
        Casilla destino = null;
        //bucle para encontrar la casilla de transporte más cercana
        for (int i=0;i<40 && destino == null; i++){
            Casilla c = tablero.obtenerCasilla((pos + i -1)%40 +1);
            if (c.getTipo().equalsIgnoreCase("transporte")){destino = c;}
        }
        //movimiento manual, sin evaluarDestino porque el alquiler es el doble
        Avatar av = jugador.getAvatar();
        Casilla inicial = av.getLugar();
        if (destino.getPosicion() < inicial.getPosicion()) {
            jugador.sumarFortuna(Valor.SUMA_VUELTA);
            jugador.sumarVueltas();
            System.out.println("El jugador " + jugador.getNombre() + " pasa por la Salida y recibe 2000000€.");
        }

        inicial.eliminarAvatar(av);
        av.setLugar(destino);
        destino.anhadirAvatar(av);
        System.out.println("El avatar " + av.getId() + " avanza hasta " + destino.getNombre() + ".");

        //si se cae en transporte y tiene dueño, se paga el doble de lo indicado.
        Float alquiler = 2.f* 250000;
        Jugador duenho = destino.getDuenho();
        if (duenho==banca){
            System.out.println("La casilla "+ destino.getNombre()+ " no tiene dueño, puedes comprarla con: comprar "+destino.getNombre());
        }
        else if (duenho!=jugador){
            if (jugador.getFortuna()>=alquiler){
                pagar(jugador, alquiler,duenho);}
            else{
                registrarDeuda(alquiler, duenho);
            }
        }
    }


    private void declararBancarrota(){
        if (jugadores.size() < 2) {
            System.out.println("Hacen falta al menos 2 jugadores para jugar.");
            return;
        }
        if (solvente) {
            System.out.println("No puedes declararte en bancarrota: no tienes ninguna deuda pendiente.");
            return;
        }
        Jugador actual = jugadores.get(turno);

        // El acreedor es el dueño de la casilla donde está el avatar, o la banca en cualquier otro caso.
        Jugador acreedor = banca;
        Casilla lugar = actual.getAvatar().getLugar();
        String t = lugar.getTipo();
        boolean comprable = t.equalsIgnoreCase("solar") || t.equalsIgnoreCase("servicio") || t.equalsIgnoreCase("transporte");
        if (comprable && lugar.getDuenho() != null && lugar.getDuenho() != actual) {
            acreedor = lugar.getDuenho();
        }

        eliminarJugador(actual, acreedor);


    }

    private void eliminarJugador(Jugador quebrado, Jugador acreedor){
        System.out.println("El jugador " + quebrado.getNombre() + " se declara en bancarrota. " + "Sus propiedades pasan a " + acreedor.getNombre() + ".");

        quebrado.declararBancarrota(acreedor);
        avatares.remove(quebrado.getAvatar());

        int indice = jugadores.indexOf(quebrado);
        jugadores.remove(quebrado);
        if (indice < turno) turno--;             // por si no fuera el jugador actual
        if (turno >= jugadores.size()) turno = 0;

        //preparar el turno del siguiente.
        tirado = false;
        lanzamientos = 0;

        if (jugadores.size() == 1) {
            System.out.println("¡" + jugadores.get(0).getNombre() + " gana la partida!");
        } else {
            System.out.println("El jugador actual es " + jugadores.get(turno).getNombre());
        }
    }

    private void reintentarDeuda(Jugador j){
        if (solvente || deudaPendiente <= 0) return;
        if (j.getFortuna()>=deudaPendiente){
            float c = deudaPendiente;
            Jugador a = acreedorPendiente;
            deudaPendiente = 0; acreedorPendiente = null; solvente = true;
            if (a == null) {                                   // carta 4: pagar a todos
                float parte = c / (jugadores.size() - 1);
                for (Jugador otro : jugadores) {
                    if (otro != j) pagar(j, parte, otro);
                }
            } else {
                pagar(j, c, a);
            }        }
        else {
            System.out.println("Aún no tienes dinero suficiente para pagar " + (long) deudaPendiente + "€.");
        }
    }



    // Convierte el tipo que escribe el usuario
    private String tipoSingular(String tipo) {
        if (tipo.equalsIgnoreCase("casa") || tipo.equalsIgnoreCase("casas")) {
            return "casa";
        } else if (tipo.equalsIgnoreCase("hotel") || tipo.equalsIgnoreCase("hoteles")) {
            return "hotel";
        } else if (tipo.equalsIgnoreCase("piscina") || tipo.equalsIgnoreCase("piscinas")) {
            return "piscina";
        } else if (tipo.equalsIgnoreCase("pista_deporte") || tipo.equalsIgnoreCase("pistas_deporte")) {
            return "pista_deporte";
        }
        return tipo;
    }

    // Método que ejecuta el comando 'vender <tipo> <casilla> <cantidad>'.
    private void vender(String tipo, String nombreCasilla, int cantidad) {
        if (jugadores.size() < 2) {
            System.out.println("Hacen falta al menos 2 jugadores para jugar.");
            return;
        }
        Jugador actual = jugadores.get(turno);
        Casilla casilla = tablero.encontrar_casilla(nombreCasilla);

        if (casilla == null) {
            System.out.println("No existe la casilla " + nombreCasilla);
            return;
        }
        casilla.venderEdificio(tipoSingular(tipo), cantidad, actual, banca);
    }

    // Método que realiza las acciones asociadas al comando 'listar edificios'.
    private void listarEdificios() {
        boolean hayEdificios = false;

        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla c : lado) {
                for (Edificio e : c.getEdificios()) {
                    System.out.println(e.infoEdificio());
                    hayEdificios = true;
                }
            }
        }

        if (!hayEdificios) {
            System.out.println("No hay edificios construidos.");
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar edificios <color>'.
    private void listarEdificiosGrupo(String color) {
        boolean existeGrupo = false;
        boolean puedeCasa = false, puedeHotel = false, puedePiscina = false, puedePista = false;

        for (ArrayList<Casilla> lado : tablero.getPosiciones()) {
            for (Casilla c : lado) {
                if (c.getGrupo() != null && c.getGrupo().getNombreGrupo().equalsIgnoreCase(color)) {
                    existeGrupo = true;
                    System.out.println(c.infoEdificiosCasilla());

                    // ¿Qué se podría construir todavía en esta casilla?
                    if (c.contarEdificios("hotel") == 0 && c.contarEdificios("casa") < 4) puedeCasa = true;
                    if (c.contarEdificios("hotel") == 0) puedeHotel = true;
                    if (c.contarEdificios("piscina") == 0) puedePiscina = true;
                    if (c.contarEdificios("pista_deporte") == 0) puedePista = true;
                }
            }
        }

        if (!existeGrupo) {
            System.out.println("No existe el grupo " + color + ".");
            return;
        }

        String sePuede = "";
        if (puedeCasa) sePuede += "casas, ";
        if (puedeHotel) sePuede += "hoteles, ";
        if (puedePiscina) sePuede += "piscinas, ";
        if (puedePista) sePuede += "pistas de deporte, ";

        if (sePuede.isEmpty()) {
            System.out.println("Ya no se puede edificar nada más en este grupo.");
        } else {
            // quitamos la última ", "
            System.out.println("Aún se puede edificar: " + sePuede.substring(0, sePuede.length() - 2) + ".");
        }
    }

    private void registrarDeuda(float cantidad, Jugador acreedor){
        solvente = false;
        deudaPendiente = cantidad;
        acreedorPendiente = acreedor;
        System.out.println("No tienes dinero suficiente. Debes hipotecar alguna propiedad o declararte en bancarrota.");
    }
    }

