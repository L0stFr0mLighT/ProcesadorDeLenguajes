public class IdTooLargeErr extends Err {
    public IdTooLargeErr(int linea, int columna, Integer estado, Caracteres carEnum){
    super(linea, columna, estado, TipoError.LEXICO, CodError.IDTOOLARGE, carEnum,
            "Identificador demasiado largo (ID.length > 64 ))");
    }
}
