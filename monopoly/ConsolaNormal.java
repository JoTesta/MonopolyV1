package monopoly;
import java.util.Scanner;

public class ConsolaNormal {
    private final Scanner sc = new Scanner(System.in);
    public void imprimir(String mensaje) { System.out.println(mensaje); }
    public String leer(String descripcion) {
        System.out.print(descripcion);
        return sc.nextLine();
    }
}
