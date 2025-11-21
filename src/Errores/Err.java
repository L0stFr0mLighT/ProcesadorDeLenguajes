package Errores;

import java.util.List;

public abstract class Err extends RuntimeException {

    public Err(int lineaAntigua, int columnaAntigua, int lineaSiguiente,int columnaSiguiente, int lineaS, int columnaS, TipoError tipoError, CodError codError, String descrip, List<String> codigoFuente, int longitudErr) {
        
        super(constructorMensaje(lineaAntigua, columnaAntigua,lineaSiguiente, columnaSiguiente, lineaS, columnaS, tipoError, codError, descrip, codigoFuente, longitudErr));
        
    }

    private static String constructorMensaje (int lineaAntigua, int columnaAntigua, int lineaSiguiente, int columnaSiguiente, int lineaS, int columnaS, TipoError tipoError, CodError codError, String descrip, List<String> codigoFuente, int longitudErr){
        
        String mensaje = "\n------ ERROR " + tipoError + " ------\n";
        mensaje += "(" + lineaAntigua + "," + columnaAntigua + ") " + codError + ": ";
        mensaje += descrip + "\n";
        mensaje += "\nCONTEXTO DEL ERROR:\n";
        // Asegurar rangos válidos
        int inicio = Math.max(1, lineaS);
        int fin = Math.min(codigoFuente.size(), lineaSiguiente);  

        // Imprimir todas las líneas desde S hasta Siguiente (Contexto del error)
        for (int linea = inicio; linea <= fin; linea++) {

            String texto = codigoFuente.get(linea - 1);

            //Imprimimos linea de forma: " xx| texto" (Espero que no haya más de 99 líneas :) )
            mensaje += String.format("%2d| %s%n", linea, texto); //%n para nueva línea (es sinonimo de \n)

            // Línea con el error = marcar ^
            if (linea == lineaAntigua) {

                mensaje += "    "; // espacio alineado con el margen "xx| "

                // Espacios hasta la columna donde ocurrió el error
                for (int i = 1; i < columnaAntigua; i++) mensaje += " ";

                // Marcar el error
                for (int i = 0; i < longitudErr; i++) mensaje += "^";

                mensaje += "\n";
            }
        }
        mensaje += "--------------------------\n";
        return mensaje;
    }  
}