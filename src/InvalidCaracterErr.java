public class InvalidCaracterErr extends Err {
    public InvalidCaracterErr(int linea, int columna, int estado, Caracteres carEnum) {
        super(linea, columna, estado, TipoError.LEXICO, CodError.INVALIDCARACTER, carEnum,
              "Carácter inválido en estado " + estado + ": " + carEnum);
    }
}