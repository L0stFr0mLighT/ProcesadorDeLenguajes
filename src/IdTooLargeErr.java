public class IdTooLargeErr extends Err {
    public IdTooLargeErr(int linea, int columna, Integer estado, Character car){
    super(linea, columna, estado, TipoError.LEXICO, CodError.IDTOOLARGE, car,
            "Identificador demasiado largo (ID.length > 64 ))");
    }
}
