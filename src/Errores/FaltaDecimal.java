package Errores;

import java.util.List;

public class FaltaDecimal extends Err {
    public FaltaDecimal(int linea, int columna, List<String> codigoFuente) {
        super(linea, columna,linea,columna,linea,columna,TipoError.LEXICO, CodError.FALTA_DECIMAL,
              "Se detecto una ausencia de decimales.", codigoFuente, 1);
    }
}