package Errores;

import java.util.List;

public class CadenaNoTerminadaErr extends Err {
    public CadenaNoTerminadaErr(int linea, int columna, String lex, List<String> codigoFuente, int longitudLex) {
        super(linea, columna-longitudLex,linea,columna,linea,columna, TipoError.LEXICO, CodError.CADENA_NO_TERMINADA,"", codigoFuente,longitudLex);
    }
}
