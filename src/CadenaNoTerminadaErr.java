public class CadenaNoTerminadaErr extends Err {
    public CadenaNoTerminadaErr(int linea, int columna, int estado, Character car, String lex) {
        super(linea, columna, estado, TipoError.LEXICO, CodError.CADENA_NO_TERMINADA, car,"Cadena no terminada: " + lex);
    }
}
