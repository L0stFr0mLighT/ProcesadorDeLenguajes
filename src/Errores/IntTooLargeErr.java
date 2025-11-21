package Errores;

import java.util.List;

public class IntTooLargeErr extends Err {
    public IntTooLargeErr(int linea, int columna, int num, List<String> codigoFuente, int longitudErr) {
        super(linea, columna-longitudErr,linea,columna,linea,columna,TipoError.LEXICO, CodError.INT_TOO_LARGE,
              "Entero demasiado grande ("+num+" > 32767).", codigoFuente, longitudErr);
    }
}