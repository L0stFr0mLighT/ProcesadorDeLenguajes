package Errores;

import java.util.List;

public class RealTooLargeErr extends Err {
    public RealTooLargeErr(int linea, int columna, double num, List<String> codigoFuente, int longitudErr) {
        super(linea, columna-longitudErr,linea,columna,linea,columna,TipoError.LEXICO, CodError.REAL_ERROR,
              "Número real demasiado grande  (num:" + num + " > 117549436.0).", codigoFuente, longitudErr);
    }
}