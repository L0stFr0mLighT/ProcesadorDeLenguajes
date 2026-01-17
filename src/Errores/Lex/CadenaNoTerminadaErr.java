package Errores.Lex;

import java.util.List;

import Errores.CodError;
import Errores.Err;
import Errores.TipoError;

public class CadenaNoTerminadaErr extends Err {
    public CadenaNoTerminadaErr(int linea, int columna, String lex, List<String> codigoFuente, int longitudLex) {
        super(linea, columna-longitudLex,linea,columna,linea,columna, TipoError.LEXICO, CodError.CADENA_NO_TERMINADA.ordinal(),"", codigoFuente,longitudLex);
    }
}
