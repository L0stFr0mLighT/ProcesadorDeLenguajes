public class IntTooLargeErr extends Err {
    public IntTooLargeErr(int linea, int columna, int estado, Character car, int num) {
        super(linea, columna, estado, TipoError.LEXICO, CodError.INT_TOO_LARGE, car,
              "Entero demasiado grande ("+num+" > 32767)");
    }
}