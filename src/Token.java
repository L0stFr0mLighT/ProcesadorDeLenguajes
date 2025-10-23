public class Token {

    private TipoToken tipo;
    private Integer atributo;
    private String atrLex;
    private double atrNum;

    public Token(TipoToken tipo, Integer atributo) {
        this.tipo = tipo;
        this.atributo = atributo;
    }

    public Token(TipoToken tipo, String lex){
        this.tipo = tipo;
        this.atrLex = lex;
    }

    public Token(TipoToken tipo, double num) {
        this.tipo = tipo;
        this.atrNum = num;
    }

    public TipoToken getTipo() {
        return tipo;
    }

    public Integer getAtributo() {
        return atributo;
    }

    public String getLex() {
        return atrLex;
    }

    public double getNum() {
        return atrNum;
    }
}