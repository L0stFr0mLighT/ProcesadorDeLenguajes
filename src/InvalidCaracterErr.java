public class InvalidCaracterErr extends Err {
    public InvalidCaracterErr(int linea, int columna, int estado, Character carEnum) {
        super(linea, columna, estado, TipoError.LEXICO, CodError.INVALIDCARACTER, carEnum,
              "Carácter inválido en estado " + estado + ": " + carEnum);
    }
}