public class IntTooLargeErr extends Err {
    public IntTooLargeErr(int linea, int columna, int estado, Caracteres carEnum, int num) {
        super(linea, columna, estado, TipoError.LEXICO, CodError.INTTOOLARGE, carEnum,
              "Entero demasiado grande ("+num+" > 32767)");
    }
}