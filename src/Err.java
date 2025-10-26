public abstract class Err extends Exception {

    public Err(int linea, int columna,int estado, TipoError tipoError, CodError codError, Character car, String descrip ) {
        
        super("ERROR: " + tipoError + 
            "\nLinea: " + linea +
            "\nColumna: " + columna +
            "\nCodErr: " + codError + 
            "\n"+ descrip);
        
    }
        
}