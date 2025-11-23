package Errores;

import java.util.List;

import Enumeraciones.TipoToken;

public class Terminal_Inesperado extends Err {
    
    public Terminal_Inesperado(int lineaAntigua, int columnaAntigua, int lineaSiguiente, int columnaSiguiente, int lineaS, int columnaS, TipoToken tipoTokenIntro, Integer atriTokenIntro,
        TipoToken tipoTokenEsp, Integer atriTokenEsp, List<String> codigoFuente, String lexTk){
        super(lineaAntigua, columnaAntigua, lineaSiguiente, columnaSiguiente, lineaS, columnaS, TipoError.SINTACTICO, CodError.TERMINAL_INESPERADO, 
        ConstructorMensaje(lineaSiguiente, columnaSiguiente, tipoTokenIntro, atriTokenIntro, tipoTokenEsp, atriTokenEsp), codigoFuente, longtitudErr(lexTk));
    }
    
    //Contructor para el mensaje a pasar como descrip
    private static String ConstructorMensaje (int linea, int columna, TipoToken tipoTokenIntro, Integer atriTokenIntro,
        TipoToken tipoTokenEsp, Integer atriTokenEsp){
        String res;
        if (atriTokenEsp != null) 
            res = "Tipo terminal esperado: "+ tipoTokenEsp+"(atributo: "+atriTokenEsp+")."+"\nTipo terminal introducido: "+tipoTokenIntro+"(atributo: "+atriTokenIntro+").";
        else 
            res = "Tipo terminal esperado: "+ tipoTokenEsp+" ."+"\nTipo terminal introducido: "+tipoTokenIntro+ " .";
        return res;
    }
    private static int longtitudErr(String lexTk){
        return lexTk == null? 1:lexTk.length();
    }
}