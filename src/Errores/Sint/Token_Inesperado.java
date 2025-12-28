package Errores.Sint;

import java.util.List;

import Enumeraciones.TipoToken;
import Errores.CodError;
import Errores.Err;
import Errores.TipoError;

public class Token_Inesperado extends Err{
    
    public Token_Inesperado (int lineaAnterior, int columnaAnterior,int lineaSiguiente, int columnaSiguiente, int lineaS, int columnaS, String contexto, 
     TipoToken encontrado, String esperado, List<String> codigoFuente, String lexTk){
        super(lineaAnterior, columnaAnterior,lineaSiguiente,columnaSiguiente,lineaS, columnaS,TipoError.SINTACTICO,CodError.TOKEN_INESPERADO,
        ConstructorMensaje(contexto, encontrado, esperado), codigoFuente, longtitudErr(lexTk));
    }
    
    //Contructor para el mensaje a pasar como descrip
    private static String ConstructorMensaje (String contexto, TipoToken encontrado, String esperado){
        return contexto + "\nEncontrado: "+ encontrado + 
            "\nEsperado: "+esperado;
    }

    private static int longtitudErr(String lexTk){
        return lexTk == null? 1:lexTk.length();
    }
}
