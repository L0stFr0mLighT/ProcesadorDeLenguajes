public abstract class Err extends Exception {

    private int linea;
    private int columna;
    private TipoError tipoError;
    private CodError codError;
    private Caracteres tipoCaracteres;
    private String res = "ERROR ";

    public Err(int linea, int columna,int estado, TipoError tipoError, CodError codError, Caracteres carEnum, String descrip ) {
        
        super("ERROR: " + tipoError + 
            "\nLinea: " + linea +
            "\nColumna: " + columna +
            "\nCodErr: " + codError + 
            "\n"+ descrip);
        
    }
        
}