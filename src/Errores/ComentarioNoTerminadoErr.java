package Errores;

import java.util.List;

public class ComentarioNoTerminadoErr extends Err{
    public ComentarioNoTerminadoErr(int linea, int columna, List<String> codigoFuente) {
        super(linea, columna,linea,columna,linea,columna,TipoError.LEXICO, CodError.COMENTARIO_NO_TERMINADO, "Comentario no terminado.",codigoFuente, 1);
    }
}
