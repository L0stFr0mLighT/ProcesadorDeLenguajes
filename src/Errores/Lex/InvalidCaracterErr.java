package Errores.Lex;

import java.util.List;

import Errores.CodError;
import Errores.Err;
import Errores.TipoError;

public class InvalidCaracterErr extends Err {
    public InvalidCaracterErr(int linea, int columna, int estado, Character car, List<String> codigoFuente) {
        super(linea, columna,linea,columna,linea,columna, TipoError.LEXICO, CodError.INVALID_CARACTER,
              "Carácter inválido en estado " + estado + ": \"" + car + "\" .", codigoFuente,1);
    }
}