package Errores;
public class RealTooLargeErr extends Err {
    public RealTooLargeErr(int linea, int columna, int estado, Character car, double num, int decimal) {
        super(linea, columna, estado, TipoError.LEXICO, CodError.REAL_ERROR, car,
              "Número real demasiado grande o countDecimal == 0 (countDecimal: " + decimal + " && num: " + num + " > 117549436.0)");
    }
}