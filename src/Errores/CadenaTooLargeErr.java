package Errores;

import java.util.List;

public class CadenaTooLargeErr extends Err {
    public CadenaTooLargeErr(int linea, int columna, String lex, int length, List<String> codigoFuente, int longitudErr) {
        super(linea, columna-longitudErr,linea,columna,linea,columna, TipoError.LEXICO, CodError.CADENA_TOO_LARGE,
              "Cadena: " + lex + " demasiado grande ("+length+" > 64).", codigoFuente, longitudErr);
    }
}