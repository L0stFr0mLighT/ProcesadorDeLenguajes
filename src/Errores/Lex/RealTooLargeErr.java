package Errores.Lex;

import java.util.List;

import Errores.CodError;
import Errores.Err;
import Errores.TipoError;

public class RealTooLargeErr extends Err {
    public RealTooLargeErr(int linea, int columna, double num, List<String> codigoFuente, int longitudErr) {
        super(linea, columna-longitudErr,linea,columna-longitudErr,linea,columna-longitudErr,TipoError.LEXICO, CodError.REAL_ERROR.ordinal(),
              "Número real demasiado grande  (num:" + num + " > 117549436.0).", codigoFuente, longitudErr);
    }
}