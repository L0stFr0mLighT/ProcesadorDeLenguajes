package Errores.Lex;

import java.util.List;

import Errores.CodError;
import Errores.Err;
import Errores.TipoError;

public class CadenaNoTerminadaErr extends Err {
    public CadenaNoTerminadaErr(int linea, int columna, String lex, List<String> codigoFuente, int longitudLex, int contTab) {
        super(linea, columna,linea,columna-contTab-3 + contTab*8,linea,columna, TipoError.LEXICO, CodError.CADENA_NO_TERMINADA.ordinal(),"Cadena no terminada: " + lex, codigoFuente,1);
    }
}
