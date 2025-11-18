package Errores;

import java.util.List;

public class InvalidCaracterErr extends Err {
    public InvalidCaracterErr(int linea, int columna, int estado, Character car, List<String> codigoFuente) {
        super(linea, columna, TipoError.LEXICO, CodError.INVALID_CARACTER,
              "Carácter inválido en estado " + estado + ": \"" + car + "\" .", codigoFuente,1);
    }
}