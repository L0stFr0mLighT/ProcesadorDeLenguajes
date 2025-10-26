public class InvalidCaracterErr extends Err {
    public InvalidCaracterErr(int linea, int columna, int estado, Character car) {
        super(linea, columna, estado, TipoError.LEXICO, CodError.INVALID_CARACTER, car,
              "Carácter inválido en estado " + estado + ": " + car);
    }
}