package Errores;
public class IdTooLargeErr extends Err {
    public IdTooLargeErr(int linea, int columna, Integer estado, Character car){
    super(linea, columna, estado, TipoError.LEXICO, CodError.ID_TOO_LARGE, car,
            "Identificador demasiado largo (ID.length > 64 ))");
    }
}
