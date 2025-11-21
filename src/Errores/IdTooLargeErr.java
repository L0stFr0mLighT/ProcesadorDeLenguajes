package Errores;

import java.util.List;

public class IdTooLargeErr extends Err {
    public IdTooLargeErr(int linea, int columna, List<String> codigoFuente, int longitudErr){
    super(linea, columna-longitudErr,linea,columna,linea,columna,TipoError.LEXICO, CodError.ID_TOO_LARGE,
            "Identificador demasiado largo (ID.length > 64 ).", codigoFuente, longitudErr);
    }
}
