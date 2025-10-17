public class RealTooLargeErr extends Err {
    public RealTooLargeErr(int linea, int columna, int estado, Caracteres carEnum, double num, int decimal) {
        super(linea, columna, estado, TipoError.LEXICO, CodError.REALTOOLARGE, carEnum,
              "Número real demasiado grande o countDecimal == 0 (countDecimal: " + decimal + " && num: " + num + " > 117549436.0)");
    }
}