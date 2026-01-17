package Errores.Sem;

import java.util.List;

import Errores.CodError;
import Errores.Err;
import Errores.TipoError;

public class ErrSemantico extends Err{
    
    public ErrSemantico (int lineaAnterior, int columnaAnterior,int lineaSiguiente, int columnaSiguiente, int lineaS, 
        int columnaS, String contexto, String desc, List<String> codigoFuente, String lexTk){
        super(lineaAnterior, columnaAnterior,lineaSiguiente,columnaSiguiente,lineaS, columnaS,TipoError.SEMANTICA,CodError.ERR_SEMANTICO.ordinal(),
       contexto+"\n"+desc, codigoFuente, longtitudErr(lexTk));
    }

    public ErrSemantico (int lineaAnterior, int columnaAnterior,int lineaSiguiente, int columnaSiguiente, int lineaS, 
        int columnaS, String contexto, String desc, List<String> codigoFuente, int longitudErr){
        super(lineaAnterior, columnaAnterior,lineaSiguiente,columnaSiguiente,lineaS, columnaS,TipoError.SEMANTICA,CodError.ERR_SEMANTICO.ordinal(),
        contexto+"\n"+desc, codigoFuente, longitudErr);
    }

    private static int longtitudErr(String lexTk){
        return lexTk == null? 1:lexTk.length();
    }

}
