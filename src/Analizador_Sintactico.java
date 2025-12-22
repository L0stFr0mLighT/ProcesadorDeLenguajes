import Enumeraciones.*;
import Errores.*;
import es.upm.aedlib.Node;
import es.upm.aedlib.Pair;
import es.upm.aedlib.positionlist.*;
import tslib.TS;
import tslib.TS_Gestor;

import java.util.List;

public class Analizador_Sintactico {

    private Token token;
    private TokenTexto tkT;
    private String parse;
    private TipoToken type;
    private int linea;
    private int columna;
    private int lineaAnterior;
    private int columnaAnterior;
    private int lineaSF;
    private int columnaSF;
    private List<String> codigoFuente;
    
    //Variable para la analizador semantico
    private TS_Gestor gestorTs;
    private TS tablaSimbolosActual;
    private int despGlob;
    private int despLoc;
    private boolean zonaDecl;

    public Analizador_Sintactico (TokenTexto tkT, List<String> codigoFuente, TS_Gestor gestorTs){
        this.tkT = tkT;
        parse = "D ";
        this.codigoFuente = codigoFuente;
        this.gestorTs = gestorTs;
    }

    //Funcion auxiliar para pedir el siguiente token
    private void nextToken (){
        //Pair<Token, Pair<nLinea,nColumna>>
        Pair<Token, Pair <Integer, Integer>> par = tkT.nextTokenWithPos();
        lineaAnterior = linea;
        columnaAnterior = columna;
        token = par.getLeft();
        type = token.getTipo();
        linea = par.getRight().getLeft();
        columna = par.getRight().getRight();
    }

    public String iniciar(){

        //No hagp try-catch de errores para asi parar en el primer error 
        //que me salga imprimiendo mensaje
        nextToken();
        I();

        if (token.getTipo() != TipoToken.TYPE_EOF){
            throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "I",token.getTipo(),"EOF", codigoFuente, token.getLex());
        }
        return parse;
    }

    //Funcion auxiliar para comprobar si el terminal leido es correcto 
    private void equipara (Token t){
            //En ID,INT,FLOAT,STRING imponemos una restriccion mas suave (no miramos su atributo)
            if (token.getTipo() == TipoToken.TYPE_IDENTIFICADOR && TipoToken.TYPE_IDENTIFICADOR == t.getTipo() || 
                token.getTipo() == TipoToken.TYPE_ENTERO && TipoToken.TYPE_ENTERO == t.getTipo() || 
                token.getTipo() == TipoToken.TYPE_REAL && TipoToken.TYPE_REAL == t.getTipo() ||
                token.getTipo() == TipoToken.TYPE_CADENA && TipoToken.TYPE_CADENA == t.getTipo() ||
                token.equals(t)) 

                nextToken();
                
            else {
                //Terminal_Inesperado gestiona internamente el mensaje a imprimir segun si atributo == null
                // throw new Terminal_Inesperado(lineaAnterior, columnaAnterior, linea, columna,lineaSF, columnaSF, token.getTipo(),token.getAtributo(),t.getTipo(),t.getAtributo(), codigoFuente, token.getLex());
                throw new Token_Inesperado(lineaAnterior, columnaAnterior, linea, columna, lineaSF, columnaSF, "Equiparando", token.getTipo(), t.getTipo().toString(), codigoFuente, token.getLex());
            }
    }

    private void I (){
        //1. I -> P
        if(type == TipoToken.TYPE_IDENTIFICADOR || type == TipoToken.TYPE_WRITE ||
        type == TipoToken.TYPE_READ || type == TipoToken.TYPE_RETURN ||
        type == TipoToken.TYPE_LET || type == TipoToken.TYPE_IF || type == TipoToken.TYPE_FOR ||
        type == TipoToken.TYPE_FUNCTION || type == TipoToken.TYPE_EOF){
            //Acciones Semanticas
            gestorTs.createTSGlobal();
            despGlob = 0;
            zonaDecl = false;

            parse = parse.concat("1 ");
            P();
        }
    };

    //AXIOMA
    private void P() {
        //2. P -> S P 
        if (type == TipoToken.TYPE_IDENTIFICADOR || type == TipoToken.TYPE_WRITE ||
        type == TipoToken.TYPE_READ || type == TipoToken.TYPE_RETURN ||
        type == TipoToken.TYPE_LET || type == TipoToken.TYPE_IF || type == TipoToken.TYPE_FOR){
            parse = parse.concat("2 "); 
            S();
            P();
        } //3. P -> F P 
        else if(type == TipoToken.TYPE_FUNCTION) {
            parse = parse.concat("3 ");
            F(); 
            P();
        } //4. P -> EOF
        else if (type == TipoToken.TYPE_EOF) {
            parse = parse.concat("4 ");
            equipara(new Token(TipoToken.TYPE_EOF, (Integer) null));
        } else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, 
            "Se esperaba una sentencia, una declaración de función o el fin del archivo (P)",token.getTipo(),
            "ID, WRITE, READ, RETURN, LET, IF, FOR, FUNCTION, EOF", codigoFuente, token.getLex());
    }

    private Pair<TipoSemantico, TipoSemantico> S(){
        //24. S -> S_SIMPLE
        lineaSF = linea;
        columnaSF = columna;
        TipoSemantico tipo;
        TipoSemantico ret;
        if(type == TipoToken.TYPE_IDENTIFICADOR || type == TipoToken.TYPE_WRITE ||
           type == TipoToken.TYPE_READ || type == TipoToken.TYPE_RETURN){
            parse = parse.concat("24 ");
            Pair<TipoSemantico, TipoSemantico> par = S_SIMPLE();
            tipo = par.getLeft();
            ret = par.getRight();
        } //25. S -> S_COMPUESTA
        else if(type == TipoToken.TYPE_LET || type == TipoToken.TYPE_IF || type == TipoToken.TYPE_FOR){
                parse = parse.concat("25 ");
                Pair<TipoSemantico, TipoSemantico> par = S_COMPUESTA();
                tipo = par.getLeft();
                ret = par.getRight();
        }

        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Sentencia no válida (S)",token.getTipo()
        ,"ID, WRITE, READ, RETURN, LET, IF, FOR", codigoFuente, token.getLex());
        
        return new Pair<TipoSemantico, TipoSemantico>(tipo, ret);
    }
    
    private void F() {
        lineaSF = linea;
        columnaSF = columna;
        //53. F -> function H id ( B ) { C }
        parse = parse.concat("53 ");
        equipara(new Token(TipoToken.TYPE_FUNCTION, (Integer) null));
        H();
        equipara(new Token (TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
        equipara(new Token (TipoToken.TYPE_PARENTESIS, (Integer)0));
        B();
        equipara(new Token (TipoToken.TYPE_PARENTESIS,(Integer) 1));
        equipara(new Token (TipoToken.TYPE_LLAVE, (Integer)0));
        C();
        equipara(new Token (TipoToken.TYPE_LLAVE, (Integer)1));

    }
    
    private String E() {

        String E_tipo;
        String E1_tipo;
        String E_AND_tipo;

        //5. E -> E1 E_AND
        if(type == TipoToken.TYPE_NEGACION || type == TipoToken.TYPE_IDENTIFICADOR ||
            type == TipoToken.TYPE_PARENTESIS || type == TipoToken.TYPE_CADENA ||
            type == TipoToken.TYPE_ENTERO || type == TipoToken.TYPE_REAL){
                parse = parse.concat("5 ");
                E1_tipo = E1();
                E_AND_tipo = E_AND();

                //Accion Semantica
                E_tipo = E1_tipo.equals(E_AND_tipo) && E1_tipo.equals("lógico") ||
                        E_AND_tipo.equals("tipo_ok") ? E1_tipo : "tipo_error";

        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Se esperaba el inicio de una expresión (E)",token.getTipo()
        ,"NEGACION, ID, PARENTESIS(ABIERTO), CADENA, ENTERO, REAL", codigoFuente, token.getLex());

        return E_tipo;
    }

    private String E_AND(){

        String E_AND_tipo;
        String E1_tipo;
        String E_AND1_tipo;

        //6. E_AND -> && E1 E_AND
        if(type == TipoToken.TYPE_AND){
            parse = parse.concat("6 ");
            equipara(new Token (TipoToken.TYPE_AND, (Integer) null));
            E1_tipo = E1();
            E_AND1_tipo = E_AND();

            //Accion Semantica
            E_AND_tipo = (E1_tipo.equals(E_AND1_tipo) && E1_tipo.equals("lógico")) ||
                        E_AND1_tipo:equals("tipo_ok") ? E1_tipo : "tipo_error";

        }
        //7. E_AND -> lambda | FOLLOW(E_AND) = { ")", ",", ";" }
        else if (type == TipoToken.TYPE_PUNTO_Y_COMA || type == TipoToken.TYPE_PARENTESIS ||
                type == TipoToken.TYPE_COMA){
            parse = parse.concat("7 ");

            //Accion Semantica
            E_AND_tipo = "tipo_ok";

        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Expresion no terminada (E_AND)",token.getTipo()
        ,"AND, PUNTO_Y_COMA, PARENTESIS, COMA", codigoFuente, token.getLex());
        
        return E_AND_tipo; 
    }
    
    private String E1(){
        String E1_tipo;
        String E2_tipo;
        String E1_REL_tipo;

        //8. E1 -> E2 E1_REL
        if(type == TipoToken.TYPE_NEGACION || type == TipoToken.TYPE_IDENTIFICADOR ||
            type == TipoToken.TYPE_PARENTESIS || type == TipoToken.TYPE_CADENA ||
            type == TipoToken.TYPE_ENTERO || type == TipoToken.TYPE_REAL){
            parse = parse.concat("8 ");
            E2_tipo = E2();
            E1_REL_tipo = E1_REL();

            //Accion Semantica
            E1_tipo = E1_REL_tipo.equals("tipo_ok") ? E2_tipo : 
                    E2_tipo.equals("real") || E2_tipo.equals("entero") ? "lógico":
                    "tipo_error";
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Expresión incompleta después de operador logico (E1)",token.getTipo()
        ,"NEGACION, ID, PARENTESIS, CADENA, ENTERO, REAL", codigoFuente, token.getLex());

        return E1_tipo;
    }
    
    private String E1_REL(){
        String E1_REL_tipo;
        String E2_tipo;
        String E1_REL1_tipo;

        //9. E1_REL  -> > E2 E1_REL
        if (token.equals(new Token(TipoToken.TYPE_RELACION,(Integer)0))){
            parse = parse.concat("9 ");
            equipara(new Token (TipoToken.TYPE_RELACION,(Integer)0));
            E2_tipo = E2();
            E1_REL1_tipo = E1_REL();

            //Accion Semantica
            E1_REL_tipo = (E2_tipo.equals("real") || E2_tipo.equals("entero")) && 
                        E1_REL1_tipo.equals("tipo_ok") ? E2_tipo : "tipo_error";

        }
        //10. E1_REL  -> < E2 E1_REL
        else if (token.equals(new Token(TipoToken.TYPE_RELACION,(Integer)1))){
            parse = parse.concat("10 ");
            equipara(new Token (TipoToken.TYPE_RELACION,(Integer)1));
            E2_tipo = E2();
            E1_REL1_tipo = E1_REL();

            //Accion Semantica
            E1_REL_tipo = (E2_tipo.equals("real") || E2_tipo.equals("entero")) && 
                        E1_REL1_tipo.equals("tipo_ok") ? E2_tipo : "tipo_error";

        }
        //11. E1_REL  -> lambda | FOLLOW(E1_REL) = { "&&", ")", ",", ";" }
        else if (type == TipoToken.TYPE_AND || type == TipoToken.TYPE_PARENTESIS ||
                type == TipoToken.TYPE_COMA || type == TipoToken.TYPE_PUNTO_Y_COMA){
            parse = parse.concat("11 ");

            //Accion Semantica
            E1_REL_tipo = "tipo_ok";
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Expresion no terminada (E1_REL)",token.getTipo()
        ,">, <, AND, ) , COMA, PUNTO_Y_COMA", codigoFuente, token.getLex());

        return E1_REL_tipo;
    }
    
    private String E2(){
        String E2_tipo;
        String E3_tipo;
        String E2_ARIT_tipo;

        //12. E2 -> E3 E2_ARIT
        if(type == TipoToken.TYPE_NEGACION || type == TipoToken.TYPE_IDENTIFICADOR ||
            type == TipoToken.TYPE_PARENTESIS || type == TipoToken.TYPE_CADENA ||
            type == TipoToken.TYPE_ENTERO || type == TipoToken.TYPE_REAL){
            parse = parse.concat("12 ");
            E3_tipo = E3();
            E2_ARIT_tipo = E2_ARIT();

            //Accion Semantica
            E2_tipo = (E3_tipo.equals("real") || E3_tipo.equals("entero")) && 
                            E2_ARIT_tipo.equals(E3_tipo) ? "tipo_ok" : "tipo_error";

        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Expresión aritmética incompleta (E2)",token.getTipo()
        ,"NEGACION, ID, PARENTESIS, CADENA, ENTERO, REAL", codigoFuente, token.getLex());

        return E2_tipo;
    }
    
    private String E2_ARIT(){
        String E2_ARIT_tipo;
        String E3_tipo;
        String E2_ARIT1_tipo;

        //13. E2_ARIT -> + E3 E2_ARIT
        if (token.equals(new Token(TipoToken.TYPE_ARITMETICO,(Integer)0))){
            parse = parse.concat("13 ");
            equipara(new Token (TipoToken.TYPE_ARITMETICO,(Integer)0));
            E3_tipo = E3();
            E2_ARIT1_tipo = E2_ARIT();

            //Accion Semantica 
            E2_ARIT_tipo = (E3_tipo.equals("real") || E3_tipo.equals("entero")) && 
                            E2_ARIT1_tipo.equals(E3_tipo) ? "tipo_ok" : "tipo_error";

        }
        //14. E2_ARIT -> - E3 E2_ARIT
        else if (token.equals(new Token(TipoToken.TYPE_ARITMETICO,(Integer)1))){
            parse = parse.concat("14 ");
            equipara(new Token (TipoToken.TYPE_ARITMETICO,(Integer)1));
            E3_tipo = E3();
            E2_ARIT1_tipo =E2_ARIT();

            //Accion Semantica 
            E2_ARIT_tipo = (E3_tipo.equals("real") || E3_tipo.equals("entero")) && 
                            E2_ARIT1_tipo.equals(E3_tipo) ? "tipo_ok" : "tipo_error";

        }
        //15. E2_ARIT -> lambda | FOLLOW(E2_ARIT) = { ">", "<", "&&", ")", ",", ";" }
        else if (type == TipoToken.TYPE_AND || type == TipoToken.TYPE_PARENTESIS ||
                type == TipoToken.TYPE_COMA || type == TipoToken.TYPE_PUNTO_Y_COMA ||
                type == TipoToken.TYPE_RELACION){
            parse = parse.concat("15 ");

            //Accion Semantica 
            E2_ARIT_tipo = "tipo_ok";
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Expresion no terminada (E2_ARIT)",token.getTipo()
        ,"ARITMETICO, AND, PARENTESIS, COMA, PUNTO_Y_COMA, RELACION", codigoFuente, token.getLex());

        return E2_ARIT_tipo;
    }
    
    private String E3(){
        String E3_tipo;
        String E31_tipo;
        String E_tipo;
        String L_tipo;
        NodePositionList<String> E4_tipo;

        //16. E3  -> ! E3 
        if (type == TipoToken.TYPE_NEGACION){
            parse = parse.concat("16 ");
            equipara(new Token(TipoToken.TYPE_NEGACION,(Integer)null));
            E31_tipo = E3();

            //Accion Semantica
            E3_tipo = (E31_tipo == "lógico") ? "lógico" : "tipo_error";
        }
        //17. E3  -> id E4 //TODO
        else if (type == TipoToken.TYPE_IDENTIFICADOR){
            parse = parse.concat("17 ");
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            E4_tipo = E4();
        }
        //18. E3  -> ( E )
        else if (token.equals(new Token(TipoToken.TYPE_PARENTESIS,(Integer)0))){
            parse = parse.concat("18 ");
            equipara(new Token(TipoToken.TYPE_PARENTESIS,(Integer)0));
            E_tipo = E();

            //Accion Semantica
            E3_tipo = E_tipo;
            
            equipara(new Token(TipoToken.TYPE_PARENTESIS,(Integer)1));
        }
        //19. E3  -> cadena
        else if (type == TipoToken.TYPE_CADENA){
            parse = parse.concat("19 ");
            equipara(new Token(TipoToken.TYPE_CADENA,(Integer)null));

            //Accion Semantica 
            E3_tipo  = "cadena";

        }
        //20. E3  -> entero
        else if (type == TipoToken.TYPE_ENTERO){
            parse = parse.concat("20 ");
            equipara(new Token(TipoToken.TYPE_ENTERO,(Integer)null));

            //Accion Semantica 
            E3_tipo = "entero";
            
        }
        //21. E3  -> real 
        else if (type == TipoToken.TYPE_REAL){
            parse = parse.concat("21 ");
            equipara(new Token(TipoToken.TYPE_REAL,(Integer)null));

            //Accion Semantica 
            E3_tipo = "real";

        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Elemento de expresión no válido o incompleto. (E3)",token.getTipo()
        ,"NEGACION, ID, PARENTESIS, CADENA, ENTERO, REAL", codigoFuente, token.getLex());

        return E3_tipo;
    }

    private NodePositionList<String> E4(){

        NodePositionList<String> E4_tipo;
        NodePositionList<String> L_tipo;

        //22. E4  -> ( L )
        if (token.equals(new Token(TipoToken.TYPE_PARENTESIS,(Integer)0))){
            parse = parse.concat("22 ");
            equipara(new Token(TipoToken.TYPE_PARENTESIS,(Integer)0));
            L_tipo = L();

            //Accion Semantica
            E4_tipo = new NodePositionList<>(L_tipo);

            equipara(new Token(TipoToken.TYPE_PARENTESIS,(Integer)1));
        }
        //23. E4  -> lambda | FOLLOW(E4) = { "+", "-", ">", "<", "&&", ")", ",", ";" }
        else if (type == TipoToken.TYPE_AND || token.equals(new Token(TipoToken.TYPE_PARENTESIS,(Integer) 1)) ||
                type == TipoToken.TYPE_COMA || type == TipoToken.TYPE_PUNTO_Y_COMA ||
                type == TipoToken.TYPE_RELACION || type == TipoToken.TYPE_ARITMETICO){
            parse = parse.concat("23 ");

            //Accion Semantica
            E4_tipo = new NodePositionList<>();
            E4_tipo.addLast("vacío");
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Después del identificador se esperaba una llamada a función '(...)' o nada (E4)",token.getTipo()
        ,"PARENTESIS, AND, COMA, PUNTO_Y_COMA, RELACION, ARITMETICO", codigoFuente, token.getLex());

        return E4_tipo;
    }
    
    private Pair<String, String> S_SIMPLE(){
        String S_SIMPLE_tipo;
        NodePositionList<String> ASIGFUN_tipo;
        String S_SIMPLE_ret;
        String idTipo;
        int idPos;

        String E_tipo;
        String X_tipo;
        //26. S_SIMPLE -> id ASIGFUN //TODO
        if(type == TipoToken.TYPE_IDENTIFICADOR){
            parse = parse.concat("26 ");
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            //System.out.println("DEBUG token: tipo=" + token.getTipo() + " atributo=" + token.getAtributo());
            ASIGFUN_tipo = ASIGFUN();
            //Acciones Semanticas
            idPos = token.getPos();
            // idTipo = TipoSemantico.valueOf(tablaSimbolosActual.getTipo(idPos));
            // if (idTipo == null){
            //     gestorTs.setTipo(idPos, "entero");
            //     idTipo = TipoSemantico.valueOf("entero");
            //     gestorTs.setValorAtributoEnt(idPos, "desplazamiento", idPos);
            //     despGlob++;
            // }
            
            // if (idTipo == TipoSemantico.función){
            //     if (gestorTs.getValorAtributoLista(idPos, parse) == ASIGFUN_tipo){
            // }

            // tipo = TipoSemantico.TIPO_OK;

            // ret = TipoSemantico.TIPO_OK;

        } //27. S_SIMPLE -> write E ;
        else if(type == TipoToken.TYPE_WRITE){
            parse = parse.concat("27 ");
            equipara(new Token(TipoToken.TYPE_WRITE, (Integer) null));
            E_tipo = E();
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));

            // Accion Semantica
            S_SIMPLE_tipo = E_tipo.equals("real") || 
                            E_tipo.equals("entero") ||
                            E_tipo.equals("cadena") ? "tipo_ok" : "tipo_error";

        } //28. S_SIMPLE -> read id ; //TODO
        else if(type == TipoToken.TYPE_READ){
            parse = parse.concat("28 ");
            equipara(new Token(TipoToken.TYPE_READ, (Integer) null));
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));


        } //29. S_SIMPLE -> return X ;
        else if(type == TipoToken.TYPE_RETURN){
            parse = parse.concat("29 ");
            equipara(new Token(TipoToken.TYPE_RETURN, (Integer) null));
            X_tipo = X();
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));

            //Accion Semantica
            S_SIMPLE_tipo = "tipo_ok";
            S_SIMPLE_ret = X_tipo;

        } else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Sentencia simple mal formada (S_SIMPLE)",token.getTipo()
        ,"ID, WRITE, READ, RETURN", codigoFuente, token.getLex());

        return new Pair<String, String>(S_SIMPLE_tipo, S_SIMPLE_ret);
    }  
    
    private Pair<String, String> S_COMPUESTA(){
        String S_COMPUESTA_tipo;
        String S_COMPUESTA_ret;
        String T_tipo;
        String E_tipo;
        String S_SIMPLE_tipo;
        String S_SIMPLE_ret;
        String INI_tipo;
        String ACTU_tipo;
        String C_tipo;
        String C_ret;

        //40. S_COMPUESTA -> let T id ; //TODO
        if(type == TipoToken.TYPE_LET){
            parse = parse.concat("40 ");
            equipara(new Token(TipoToken.TYPE_LET, (Integer) null));
            T();
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
        }
        //41. S_COMPUESTA -> if ( E ) S_SIMPLE
        else if(type == TipoToken.TYPE_IF){
            parse = parse.concat("41 ");
            equipara(new Token(TipoToken.TYPE_IF, (Integer) null));
            equipara(new Token(TipoToken.TYPE_PARENTESIS, (Integer)0));
            E_tipo = E();
            equipara(new Token(TipoToken.TYPE_PARENTESIS,(Integer) 1));
            Pair<String, String> par = S_SIMPLE();
            S_SIMPLE_tipo = par.getLeft();
            S_SIMPLE_ret = par.getRight();

            //Accion Semantica
            S_COMPUESTA_tipo = E_tipo.equals("lógico") ? S_SIMPLE_tipo : "tipo_error";
            S_COMPUESTA_ret = S_SIMPLE_ret;

        }
        //42. S_COMPUESTA -> for ( INI ; E ; ACTU ) { C }
        else if(type == TipoToken.TYPE_FOR){
            parse = parse.concat("42 ");
            equipara(new Token(TipoToken.TYPE_FOR, (Integer) null));
            equipara(new Token(TipoToken.TYPE_PARENTESIS, (Integer)0));
            INI_tipo = INI();
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
            E_tipo = E();
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
            ACTU_tipo = ACTU();
            equipara(new Token(TipoToken.TYPE_PARENTESIS,(Integer) 1));
            equipara(new Token(TipoToken.TYPE_LLAVE, (Integer)0));
            Pair<String, String> par = C();
            C_tipo = par.getLeft();
            C_ret = par.getRight();
            equipara(new Token(TipoToken.TYPE_LLAVE, (Integer)1));

            //Accion Semantica
            S_COMPUESTA_tipo = INI_tipo.equals("tipo_ok") &&
                                ACTU_tipo.equals("tipo_ok") && 
                                E_tipo.equals("lógico") ? C_tipo : "tipo_error";
            
            S_COMPUESTA_ret = C_ret;

        } else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Sentencia compuesta incompleta o mal escrita (S_COMPUESTA)",token.getTipo()
        ,"LET, IF, FOR", codigoFuente, token.getLex());

        return new Pair<String, String>(S_COMPUESTA_tipo, S_COMPUESTA_ret);
    }
    
    private NodePositionList<String> ASIGFUN(){
        NodePositionList<String> ASIGFUN_tipo;
        NodePositionList<String> L_tipo;
        String A_tipo;
        String E_tipo;

        //30. ASIGFUN  -> A E ;
        if(token.getTipo() == TipoToken.TYPE_ASIGNACION) {
            parse = parse.concat("30 ");
            A_tipo = A();
            E_tipo = E();
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));

            //Accion Semantica
            ASIGFUN_tipo = new NodePositionList<>();
            if (A_tipo.equals("lógico") && E_tipo.equals("lógico") ||
                A_tipo.equals("tipo_ok")){
                    ASIGFUN_tipo.addLast(E_tipo);
            } else ASIGFUN_tipo.addLast("tipo_error");

        }
        //31. ASIGFUN  -> ( L ) ;
        else if(token.equals(new Token(TipoToken.TYPE_PARENTESIS, (Integer)0))){
            parse = parse.concat("31 ");
            equipara(new Token(TipoToken.TYPE_PARENTESIS, (Integer)0));
            L_tipo = L();
            equipara(new Token(TipoToken.TYPE_PARENTESIS, (Integer)1));
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));

            //Accion Semantica
            ASIGFUN_tipo = new NodePositionList<>(L_tipo);

        } else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Se esperaba una asignación '= expr;' o una llamada a función '(...)' (ASIGFUN)",token.getTipo()
        ,"ASIGNACION, PARENTESIS", codigoFuente, token.getLex());

        return ASIGFUN_tipo;
    }
    
    private String A(){
        String A_tipo;

        //32. A -> =
        if(token.equals(new Token(TipoToken.TYPE_ASIGNACION, (Integer) 0))){
            parse = parse.concat("32 ");
            equipara(new Token(TipoToken.TYPE_ASIGNACION, (Integer) 0));

            //Accion Semantica
            A_tipo = "tipo_ok";

        } //33. A -> |=
        else if(token.equals(new Token(TipoToken.TYPE_ASIGNACION, (Integer) 1))){
            parse = parse.concat("33 ");
            equipara(new Token(TipoToken.TYPE_ASIGNACION, (Integer) 1));

            //Accion Semantica
            A_tipo = "lógico";

        } else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Se esperaba un operador de asignación (= o |=) (A)",token.getTipo()
        ,"ASGINACION", codigoFuente, token.getLex());

        return A_tipo;
    }

    private NodePositionList<String> L(){
        NodePositionList<String> L_tipo;
        NodePositionList<String> Q_tipo;
        String E_tipo;
        
        //34. L -> E Q
        if(type == TipoToken.TYPE_NEGACION || type == TipoToken.TYPE_IDENTIFICADOR ||
            type == TipoToken.TYPE_CADENA || type == TipoToken.TYPE_ENTERO || type == TipoToken.TYPE_REAL ||
            token.equals(new Token(TipoToken.TYPE_PARENTESIS, (Integer) 0))){
                parse = parse.concat("34 ");
                E_tipo = E();
                Q_tipo = Q();
            
                //Accion Semantica
                L_tipo = new NodePositionList<>(Q_tipo);
                L_tipo.addLast(E_tipo);
                
        } //35. L -> lambda FOLLOW(L) = {")"}
        else if (token.equals(new Token(TipoToken.TYPE_PARENTESIS, (Integer) 1))){
            parse = parse.concat("35 ");

            //Accion Semantica
            L_tipo = new NodePositionList<String>();
            L_tipo.addLast("vacío");

        } else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Lista de argumentos mal formada (L)",token.getTipo()
        ,"NEGACION, ID, PARENTESIS, CADENA, ENTERO, REAL", codigoFuente, token.getLex());

        return L_tipo;
    }

    private NodePositionList<String> Q(){
        NodePositionList<String> Q_tipo;
        NodePositionList<String> Q1_tipo;
        String E_tipo; 

        //36. Q -> , E Q
        if(type == TipoToken.TYPE_COMA){
            parse = parse.concat("36 ");
            equipara(new Token(TipoToken.TYPE_COMA, (Integer) null));
            E_tipo = E();
            Q1_tipo = Q();

            //Accion Semantica
            Q_tipo = new NodePositionList<>(Q1_tipo);
            Q_tipo.addLast(E_tipo);
            
        } //37. Q -> lambda FOLLOW(Q) = {")"}
        else if (token.equals(new Token(TipoToken.TYPE_PARENTESIS, (Integer) 1))){
            parse = parse.concat("37 ");

            //Accion Semantica
            Q_tipo = new NodePositionList<>();
            Q_tipo.addLast("vacío"); 

        } else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Se esperaba un elemento después de la coma (Q)",token.getTipo()
        ,"COMA, PARENTESIS", codigoFuente, token.getLex());

        return Q_tipo;
    }
    
    private String X(){
        String X_tipo;
        String E_tipo;
        
        //38. X -> E
        if(type == TipoToken.TYPE_NEGACION || type == TipoToken.TYPE_IDENTIFICADOR ||
            type == TipoToken.TYPE_PARENTESIS || type == TipoToken.TYPE_CADENA ||
            type == TipoToken.TYPE_ENTERO || type == TipoToken.TYPE_REAL){
                parse = parse.concat("38 ");
            E_tipo = E();
                
            //Accion Semantica
            X_tipo = E_tipo;

        } //39. X -> lambda FOLLOW(X) = {";"}
        else if (type == TipoToken.TYPE_PUNTO_Y_COMA){
            parse = parse.concat("39 ");

            //Accion Semantica
            X_tipo = "vacío";
        
        } else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Después de 'return' debe venir una expresión o un ';' (X)",token.getTipo()
        ,"NEGACION, ID, PARENTESIS, CADENA, ENTERO, REAL, PUNTO_Y_COMA", codigoFuente, token.getLex());
        
        return X_tipo;
    }
    
    private Pair<String, Integer> T(){
        String T_tipo;
        Integer T_ancho;

        //43. T -> int
        if(type == TipoToken.TYPE_INT){
            parse = parse.concat("43 ");
            equipara(new Token(TipoToken.TYPE_INT, (Integer) null));

            //Accion Semantica
            T_tipo = "entero";
            T_ancho = 1;

        } //44. T -> float
        else if(type == TipoToken.TYPE_FLOAT){
            parse = parse.concat("44 ");
            equipara(new Token(TipoToken.TYPE_FLOAT, (Integer) null));

            //Accion Semantica
            T_tipo = "real";
            T_ancho = 2;

        } //45. T -> boolean
        else if(type == TipoToken.TYPE_BOOLEAN){
            parse = parse.concat("45 ");
            equipara(new Token(TipoToken.TYPE_BOOLEAN, (Integer) null));

            //Accion Semantica
            T_tipo = "lógico";
            T_ancho = 1;

        } //46. T -> string
        else if(type == TipoToken.TYPE_STRING){
            parse = parse.concat("46 ");
            equipara(new Token(TipoToken.TYPE_STRING, (Integer) null));
            
            //Accion Semantica
            T_tipo = "cadena";
            T_ancho = 64;

        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Leyendo tipo de dato (int, float, boolean, string) (T)",token.getTipo()
        ,"INT, FLOAT, BOOLEAN, STRING", codigoFuente, token.getLex());

        return new Pair<String, Integer>(T_tipo, T_ancho);
    }
    
    private String INI(){
        String INI_tipo;
        String E_tipo;

        //47. INI ->  id = E //TODO
        if(type == TipoToken.TYPE_IDENTIFICADOR){
            parse = parse.concat("47 ");
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            equipara(new Token(TipoToken.TYPE_ASIGNACION, (Integer) 0));
            E_tipo = E();

            //Accion Semantica


        } //48. INI -> lambda FOLLOW(INI) = {";"}
        else if (type == TipoToken.TYPE_PUNTO_Y_COMA){
            parse = parse.concat("48 ");

            //Accion Semantica
            INI_tipo = "tipo_ok";
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Inicialización inválida en el 'for' (INI)",token.getTipo()
        ,"ID, PUNTO_Y_COMA", codigoFuente, token.getLex());

        return INI_tipo;
    }
    
    private String ACTU(){
        String ACTU_tipo;


        //49. ACTU -> id A E ;
        if(type == TipoToken.TYPE_IDENTIFICADOR){
            parse = parse.concat("49 ");
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            A();
            E();
        } //50. ACTU -> lambda FOLLOW(ACTU) = {")"}
        else if (token.equals(new Token(TipoToken.TYPE_PARENTESIS, (Integer) 1))){
            parse = parse.concat("50 ");

            //Accion Semantica
            ACTU_tipo = "tipo_ok";
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Actualización inválida en el 'for' (ACTU)",token.getTipo()
        ,"ID, PARENTESIS", codigoFuente, token.getLex());

        return ACTU_tipo;
    }
    
    private Pair<String, String> C(){
        String C_tipo;
        String C_ret;
        String S_tipo;
        String S_ret;
        String C1_tipo;
        String C1_ret;
        
        //51. C -> S C
        if(type == TipoToken.TYPE_IDENTIFICADOR || type == TipoToken.TYPE_WRITE ||
        type == TipoToken.TYPE_READ || type == TipoToken.TYPE_RETURN ||
        type == TipoToken.TYPE_LET || type == TipoToken.TYPE_IF || type == TipoToken.TYPE_FOR){
            parse = parse.concat("51 ");
            Pair<String, String> parS = S();
            S_tipo = parS.getLeft();
            S_ret = parS.getRight();
            Pair<String, String > parC = C();
            C1_tipo = parC.getLeft();
            C1_ret = parC.getRight();

            //Accion Semantica
            C_tipo = S_tipo.equals("tipo_ok") ? C1_tipo : "tipo_error";
            C_ret = S_ret.equals("tipo_ok") ? C1_ret :
                    C1_ret.equals("tipo_ok") || S_ret.equals(C1_ret) ? S_ret : "tipo_error";

        } //52. C -> lambda FOLLOW(C) = {"}"}
        else if (token.equals(new Token(TipoToken.TYPE_LLAVE, (Integer) 1))){
            parse = parse.concat("52 ");

            //Accion Semantica
            C_tipo = "tipo_ok";
            C_ret = "tipo_ok";

        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Bloque de código mal formado (C)",token.getTipo()
        ,"ID, WRITE, READ, RETURN, LET, IF, FOR, LLAVE", codigoFuente, token.getLex());

        return new Pair<String, String> (C_tipo, C_ret);
    }
    
    private String H(){
        String H_tipo;
        String T_tipo;

        //54. H -> T
        if(type == TipoToken.TYPE_INT || type == TipoToken.TYPE_FLOAT ||
        type == TipoToken.TYPE_BOOLEAN || type == TipoToken.TYPE_STRING){
            parse = parse.concat("54 ");
            Pair<String, Integer> par = T();
            T_tipo = par.getLeft();

            //Accion Semantica
            H_tipo = T_tipo;

        } //55. H -> void
        else if(type == TipoToken.TYPE_VOID){
            parse = parse.concat("55 ");
            equipara(new Token(TipoToken.TYPE_VOID, (Integer) null));

            //Accion Semantica
            H_tipo = "vacío";

        } 
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Se esperaba un tipo de retorno o 'void' (H)",token.getTipo()
        ,"INT, FLOAT, BOOLEAN, STRING, VOID", codigoFuente, token.getLex());

        return H_tipo;
    }

    private NodePositionList<String> B(){

        NodePositionList<String> B_tipo;

        //56. B -> T id K //TODO
        if(type == TipoToken.TYPE_INT || type == TipoToken.TYPE_FLOAT ||
        type == TipoToken.TYPE_BOOLEAN || type == TipoToken.TYPE_STRING){
            parse = parse.concat("56 ");
            T();
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            K();
        } //57. B -> void
        else if (type == TipoToken.TYPE_VOID){
            parse = parse.concat("57 ");
            equipara(new Token(TipoToken.TYPE_VOID, (Integer) null));

            //Accion Semantica
            B_tipo = new NodePositionList<>();
            B_tipo.addLast("vacío");
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Lista de parámetros mal formada o incompleta (B)",token.getTipo()
        ,"INT, FLOAT, BOOLEAN, STRING, VOID", codigoFuente, token.getLex());

        return B_tipo;
    }

    private NodePositionList<String> K(){
        NodePositionList<String> K_tipo;
        NodePositionList<String> K1_tipo;
        String T_tipo;
        Integer T_ancho;

        //58. K -> , T id K //TODO
        if(type == TipoToken.TYPE_COMA){
            parse = parse.concat("58 ");
            equipara(new Token(TipoToken.TYPE_COMA, (Integer) null));
            T();
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            K();


        } //59. K -> lambda FOLLOW(K) = {")"}
        else if (token.equals(new Token(TipoToken.TYPE_PARENTESIS, (Integer) 1))){
            parse = parse.concat("59 ");

            //Accion Semantica
            K_tipo = new NodePositionList<>();
            K_tipo.addLast("tipo_ok");

        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Error en los parametros de la funcion (K)",token.getTipo()
        ,"COMA, PARENTESIS", codigoFuente, token.getLex());

        return K_tipo;
    }
}