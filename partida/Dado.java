package partida;

public class Dado {
    //El dado solo tiene un atributo en nuestro caso: su valor.
    private int valor;

    //Metodo para simular lanzamiento de un dado: devolverá un valor aleatorio entre 1 y 6.
    public int hacerTirada() {
        //Math.random... genera un número del 0 al 5, por eso se le suma 1.
        this.valor = (int)(Math.random()*6)+1;
        return this.valor;
    }

    //Metodo para hacer el lanzamiento con números determinados.
    public int hacerTirada(int numDado){
        this.valor = numDado;
        return this.valor;
    }
}
