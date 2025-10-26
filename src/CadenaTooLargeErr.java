public class CadenaTooLargeErr extends Err {
    public CadenaTooLargeErr(int linea, int columna, Integer estado, Character car, String lex, int length) {
        super(linea, columna, estado, TipoError.LEXICO, CodError.CADENA_TOO_LARGE, car,
              "Cadena: " + lex + " demasiado grande ("+length+" > 64)");
    }
}