package Errores;

import java.util.List;

public abstract class Err extends RuntimeException {

    public Err(int linea, int columna, TipoError tipoError, CodError codError, String descrip, List<String> codigoFuente, int longitudErr) {
        
        super(constructorMensaje(linea, columna, tipoError, codError, descrip, codigoFuente, longitudErr));
        
    }

    private static String constructorMensaje (int linea, int columna, TipoError tipoError, CodError codError, String descrip, List<String> codigoFuente, int longitudErr){
        
        String mensaje = "\n------ ERROR " + tipoError + " ------\n";
        mensaje += "(" + linea + "," + columna + ") " + codError + ": ";
        mensaje += descrip + "\n";

        //Mostrar la línea exacta con el error
        //Miramos que no sea vacio el archivo y tampoco linea sea mayor que lo que tengamos en codigoFuente (esto no deberia dispararse nunca)
        if (codigoFuente != null && linea-1 < codigoFuente.size()) {
            String lineaError = codigoFuente.get(linea-1);
            mensaje += "-> ";
            mensaje += lineaError + "\n";

            //Marcar la columna del error con ^
            //Primero imprimimos espacios hasta la columna
            for (int i = 1; i < columna+3; i++) mensaje += " "; 
            //Ahora apuntamos al error
            for (int i = 0; i < longitudErr; i++) mensaje += "^";

            mensaje += "\n";
        }

        mensaje += "--------------------------\n";
        return mensaje;
    }  
}