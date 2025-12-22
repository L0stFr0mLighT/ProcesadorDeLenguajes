import tslib.TS_Gestor;
import tslib.TS_Gestor.DescripcionAtributo;
import tslib.TS_Gestor.TipoDatoAtributo;

public class TablaDeSimbolos {
    private TS_Gestor gestorTs;

    public TablaDeSimbolos (TS_Gestor gestor){
        this.gestorTs = gestor;
    }

    public void iniciar(){
        gestorTs.createTPalabrasReservadas();
        gestorTs.addEntradaTPalabrasReservadas("let");
        gestorTs.addEntradaTPalabrasReservadas("int");
        gestorTs.addEntradaTPalabrasReservadas("float");
        gestorTs.addEntradaTPalabrasReservadas("boolean");
        gestorTs.addEntradaTPalabrasReservadas("string");
        gestorTs.addEntradaTPalabrasReservadas("void");
        gestorTs.addEntradaTPalabrasReservadas("for");
        gestorTs.addEntradaTPalabrasReservadas("function");
        gestorTs.addEntradaTPalabrasReservadas("return");
        gestorTs.addEntradaTPalabrasReservadas("if");
        gestorTs.addEntradaTPalabrasReservadas("read");
        gestorTs.addEntradaTPalabrasReservadas("write");
    }

    public void crearTablaGlobal(){
        gestorTs.createAtributo("desplazamiento", DescripcionAtributo.DIR, TipoDatoAtributo.ENTERO);
        gestorTs.createTSGlobal();
    }

}
