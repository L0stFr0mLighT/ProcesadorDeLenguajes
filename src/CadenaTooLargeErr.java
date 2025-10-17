public class CadenaTooLargeErr extends Err {
    public CadenaTooLargeErr(int linea, int columna, Integer estado, Caracteres carEnum, String lex, int length) {
        super(linea, columna, estado, TipoError.LEXICO, CodError.CADENATOOLARGE, carEnum,
              "Cadena: " + lex + " demasiado grande ("+length+" > 64)");
    }
}