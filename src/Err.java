public abstract class Err extends Exception {

    public Err(int linea, int columna,int estado, TipoError tipoError, CodError codError, Caracteres carEnum, String descrip ) {
        
        super("ERROR: " + tipoError + 
            "\nLinea: " + linea +
            "\nColumna: " + columna +
            "\nCodErr: " + codError + 
            "\n"+ descrip);
        
    }
        
}