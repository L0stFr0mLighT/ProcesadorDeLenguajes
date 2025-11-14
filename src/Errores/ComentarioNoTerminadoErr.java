package Errores;
public class ComentarioNoTerminadoErr extends Err{
    public ComentarioNoTerminadoErr(int linea, int columna, int estado, Character car) {
        super(linea, columna, estado, TipoError.LEXICO, CodError.COMENTARIO_NO_TERMINADO, car,"Comentario no terminado");
    }
}
