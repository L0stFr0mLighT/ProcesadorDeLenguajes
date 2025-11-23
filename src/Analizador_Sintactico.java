import Enumeraciones.*;
import Errores.*;
import es.upm.aedlib.Pair;

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

    List<String> codigoFuente;

    public Analizador_Sintactico (TokenTexto tkT, List<String> codigoFuente){
        this.tkT = tkT;
        parse = "D ";
        this.codigoFuente = codigoFuente;

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
        P();

        if (token.getTipo() != TipoToken.TYPE_EOF){
            throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "P",token.getTipo(),"EOF", codigoFuente, token.getLex());
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

    //AXIOMA
    private void P() {
        //1. P -> S P 
        if (type == TipoToken.TYPE_IDENTIFICADOR || type == TipoToken.TYPE_WRITE ||
        type == TipoToken.TYPE_READ || type == TipoToken.TYPE_RETURN ||
        type == TipoToken.TYPE_LET || type == TipoToken.TYPE_IF || type == TipoToken.TYPE_FOR){
            parse = parse.concat("1 "); 
            S();
            P();
        } //2. P -> F P 
        else if(type == TipoToken.TYPE_FUNCTION) {
            parse = parse.concat("2 ");
            F(); 
            P();
        } //3. P -> EOF
        else if (type == TipoToken.TYPE_EOF) {
            parse = parse.concat("3 ");
            equipara(new Token(TipoToken.TYPE_EOF, (Integer) null));
        } else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, 
            "Se esperaba una sentencia, una declaración de función o el fin del archivo (P)",token.getTipo(),
            "ID, WRITE, READ, RETURN, LET, IF, FOR, FUNCTION, EOF", codigoFuente, token.getLex());
    }

    private void S(){
        //23. S -> S_SIMPLE
        lineaSF = linea;
        columnaSF = columna;
        if(type == TipoToken.TYPE_IDENTIFICADOR || type == TipoToken.TYPE_WRITE ||
            type == TipoToken.TYPE_READ || type == TipoToken.TYPE_RETURN){
                parse = parse.concat("23 ");
                S_SIMPLE();
        } //24. S -> S_COMPUESTA
        else if(type == TipoToken.TYPE_LET || type == TipoToken.TYPE_IF || type == TipoToken.TYPE_FOR){
                parse = parse.concat("24 ");
                S_COMPUESTA();
        }
         
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Sentencia no válida (S)",token.getTipo()
        ,"ID, WRITE, READ, RETURN, LET, IF, FOR", codigoFuente, token.getLex());
    
    }
    
    private void F() {
        lineaSF = linea;
        columnaSF = columna;
        //52. F -> function H id ( B ) { C }
        parse = parse.concat("52 ");
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
    
    private void E() {
        //4. E -> E1 E_AND
        if(type == TipoToken.TYPE_NEGACION || type == TipoToken.TYPE_IDENTIFICADOR ||
            type == TipoToken.TYPE_PARENTESIS || type == TipoToken.TYPE_CADENA ||
            type == TipoToken.TYPE_ENTERO || type == TipoToken.TYPE_REAL){
                parse = parse.concat("4 ");
                E1();
                E_AND();
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Se esperaba el inicio de una expresión (E)",token.getTipo()
        ,"NEGACION, ID, PARENTESIS, CADENA, ENTERO, REAL", codigoFuente, token.getLex());
    }

    private void E_AND(){
        //5. E_AND -> && E1 E_AND
        if(type == TipoToken.TYPE_AND){
            parse = parse.concat("5 ");
            equipara(new Token (TipoToken.TYPE_AND, (Integer) null));
            E1();
            E_AND();
        }
        //6. E_AND -> lambda | FOLLOW(E_AND) = { ")", ",", ";" }
        else if (type == TipoToken.TYPE_PUNTO_Y_COMA || type == TipoToken.TYPE_PARENTESIS ||
                type == TipoToken.TYPE_COMA){
            parse = parse.concat("6 ");
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Expresion no terminada (E_AND)",token.getTipo()
        ,"AND, PUNTO_Y_COMA, PARENTESIS, COMA", codigoFuente, token.getLex());
    }
    
    private void E1(){
        //7. E1 -> E2 E1_REL
        if(type == TipoToken.TYPE_NEGACION || type == TipoToken.TYPE_IDENTIFICADOR ||
            type == TipoToken.TYPE_PARENTESIS || type == TipoToken.TYPE_CADENA ||
            type == TipoToken.TYPE_ENTERO || type == TipoToken.TYPE_REAL){
            parse = parse.concat("7 ");
            E2();
            E1_REL();
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Expresión incompleta después de operador logico (E1)",token.getTipo()
        ,"NEGACION, ID, PARENTESIS, CADENA, ENTERO, REAL", codigoFuente, token.getLex());
    }
    
    private void E1_REL(){
        //8. E1_REL  -> > E2 E1_REL
        if (token.equals(new Token(TipoToken.TYPE_RELACION,(Integer)0))){
            parse = parse.concat("8 ");
            equipara(new Token (TipoToken.TYPE_RELACION,(Integer)0));
            E2();
            E1_REL();
        }
        //9. E1_REL  -> < E2 E1_REL
        else if (token.equals(new Token(TipoToken.TYPE_RELACION,(Integer)1))){
            parse = parse.concat("9 ");
            equipara(new Token (TipoToken.TYPE_RELACION,(Integer)1));
            E2();
            E1_REL();
        }
        //10. E1_REL  -> lambda | FOLLOW(E1_REL) = { "&&", ")", ",", ";" }
        else if (type == TipoToken.TYPE_AND || type == TipoToken.TYPE_PARENTESIS ||
                type == TipoToken.TYPE_COMA || type == TipoToken.TYPE_PUNTO_Y_COMA){
            parse = parse.concat("10 ");
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Expresion no terminada (E1_REL)",token.getTipo()
        ,">, <, AND, ) , COMA, PUNTO_Y_COMA", codigoFuente, token.getLex());
    }
    
    private void E2(){
        //11. E2 -> E3 E2_ARIT
        if(type == TipoToken.TYPE_NEGACION || type == TipoToken.TYPE_IDENTIFICADOR ||
            type == TipoToken.TYPE_PARENTESIS || type == TipoToken.TYPE_CADENA ||
            type == TipoToken.TYPE_ENTERO || type == TipoToken.TYPE_REAL){
            parse = parse.concat("11 ");
            E3();
            E2_ARIT();
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Expresión aritmética incompleta (E2)",token.getTipo()
        ,"NEGACION, ID, PARENTESIS, CADENA, ENTERO, REAL", codigoFuente, token.getLex());
    }
    
    private void E2_ARIT(){
        //12. E2_ARIT -> + E3 E2_ARIT
        if (token.equals(new Token(TipoToken.TYPE_ARITMETICO,(Integer)0))){
            parse = parse.concat("12 ");
            equipara(new Token (TipoToken.TYPE_ARITMETICO,(Integer)0));
            E3();
            E2_ARIT();
        }
        //13. E2_ARIT -> - E3 E2_ARIT
        else if (token.equals(new Token(TipoToken.TYPE_ARITMETICO,(Integer)1))){
            parse = parse.concat("13 ");
            equipara(new Token (TipoToken.TYPE_ARITMETICO,(Integer)1));
            E3();
            E2_ARIT();
        }
        //14. E2_ARIT -> lambda | FOLLOW(E2_ARIT) = { ">", "<", "&&", ")", ",", ";" }
        else if (type == TipoToken.TYPE_AND || type == TipoToken.TYPE_PARENTESIS ||
                type == TipoToken.TYPE_COMA || type == TipoToken.TYPE_PUNTO_Y_COMA ||
                type == TipoToken.TYPE_RELACION){
            parse = parse.concat("14 ");
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Expresion no terminada (E2_ARIT)",token.getTipo()
        ,"ARITMETICO, AND, PARENTESIS, COMA, PUNTO_Y_COMA, RELACION", codigoFuente, token.getLex());

    }
    
    private void E3(){
        //15. E3  -> ! E3 
        if (type == TipoToken.TYPE_NEGACION){
            parse = parse.concat("15 ");
            equipara(new Token(TipoToken.TYPE_NEGACION,(Integer)null));
            E3();
        }
        //16. E3  -> id E4
        else if (type == TipoToken.TYPE_IDENTIFICADOR){
            parse = parse.concat("16 ");
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            E4();
        }
        //17. E3  -> ( E )
        else if (token.equals(new Token(TipoToken.TYPE_PARENTESIS,(Integer)0))){
            parse = parse.concat("17 ");
            equipara(new Token(TipoToken.TYPE_PARENTESIS,(Integer)0));
            E();
            equipara(new Token(TipoToken.TYPE_PARENTESIS,(Integer)1));
        }
        //18. E3  -> cadena
        else if (type == TipoToken.TYPE_CADENA){
            parse = parse.concat("18 ");
            equipara(new Token(TipoToken.TYPE_CADENA,(Integer)null));
        }
        //19. E3  -> entero
        else if (type == TipoToken.TYPE_ENTERO){
            parse = parse.concat("19 ");
            equipara(new Token(TipoToken.TYPE_ENTERO,(Integer)null));
        }
        //20. E3  -> real 
        else if (type == TipoToken.TYPE_REAL){
            parse = parse.concat("20 ");
            equipara(new Token(TipoToken.TYPE_REAL,(Integer)null));
        }
        else{
            System.out.println("DEBUG E3 token tipo=" + token.getTipo() + " atributo=" + token.getAtributo());
            throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Elemento de expresión no válido o incompleto. (E3)",token.getTipo()
            ,"NEGACION, ID, PARENTESIS, CADENA, ENTERO, REAL", codigoFuente, token.getLex());

        } 
    }

    private void E4(){
        //21. E4  -> ( L )
        if (token.equals(new Token(TipoToken.TYPE_PARENTESIS,(Integer)0))){
            parse = parse.concat("21 ");
            equipara(new Token(TipoToken.TYPE_PARENTESIS,(Integer)0));
            L();
            equipara(new Token(TipoToken.TYPE_PARENTESIS,(Integer)1));
        }
        //22. E4  -> lambda | FOLLOW(E4) = { "+", "-", ">", "<", "&&", ")", ",", ";" }
        else if (type == TipoToken.TYPE_AND || token.equals(new Token(TipoToken.TYPE_PARENTESIS,(Integer) 1)) ||
                type == TipoToken.TYPE_COMA || type == TipoToken.TYPE_PUNTO_Y_COMA ||
                type == TipoToken.TYPE_RELACION || type == TipoToken.TYPE_ARITMETICO){
            parse = parse.concat("22 ");
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Después del identificador se esperaba una llamada a función '(...)' o nada (E4)",token.getTipo()
        ,"PARENTESIS, AND, COMA, PUNTO_Y_COMA, RELACION, ARITMETICO", codigoFuente, token.getLex());

    }
    
    private void S_SIMPLE(){
        //25. S_SIMPLE -> id ASIGFUN
        if(type == TipoToken.TYPE_IDENTIFICADOR){
            parse = parse.concat("25 ");
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            //System.out.println("DEBUG token: tipo=" + token.getTipo() + " atributo=" + token.getAtributo());
            ASIGFUN();
        } //26. S_SIMPLE -> write E ;
        else if(type == TipoToken.TYPE_WRITE){
            parse = parse.concat("26 ");
            equipara(new Token(TipoToken.TYPE_WRITE, (Integer) null));
            E();
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
        } //27. S_SIMPLE -> read id ;
        else if(type == TipoToken.TYPE_READ){
            parse = parse.concat("27 ");
            equipara(new Token(TipoToken.TYPE_READ, (Integer) null));
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
        } //28. S_SIMPLE -> return X ;
        else if(type == TipoToken.TYPE_RETURN){
            parse = parse.concat("28 ");
            equipara(new Token(TipoToken.TYPE_RETURN, (Integer) null));
            X();
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
        } else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Sentencia simple mal formada (S_SIMPLE)",token.getTipo()
        ,"ID, WRITE, READ, RETURN", codigoFuente, token.getLex());

    }  
    
    private void S_COMPUESTA(){
        //39. S_COMPUESTA -> let T id ;
        if(type == TipoToken.TYPE_LET){
            parse = parse.concat("39 ");
            equipara(new Token(TipoToken.TYPE_LET, (Integer) null));
            T();
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
        }
        //40. S_COMPUESTA -> if ( E ) S
        else if(type == TipoToken.TYPE_IF){
            parse = parse.concat("40 ");
            equipara(new Token(TipoToken.TYPE_IF, (Integer) null));
            equipara(new Token(TipoToken.TYPE_PARENTESIS, (Integer)0));
            E();
            equipara(new Token(TipoToken.TYPE_PARENTESIS,(Integer) 1));
            S();
        }
        //41. S_COMPUESTA -> for ( INI ; E ; ACTU ) { C }
        else if(type == TipoToken.TYPE_FOR){
            parse = parse.concat("41 ");
            equipara(new Token(TipoToken.TYPE_FOR, (Integer) null));
            equipara(new Token(TipoToken.TYPE_PARENTESIS, (Integer)0));
            INI();
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
            E();
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
            ACTU();
            equipara(new Token(TipoToken.TYPE_PARENTESIS,(Integer) 1));
            equipara(new Token(TipoToken.TYPE_LLAVE, (Integer)0));
            C();
            equipara(new Token(TipoToken.TYPE_LLAVE, (Integer)1));
        } else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Sentencia compuesta incompleta o mal escrita (S_COMPUESTA)",token.getTipo()
        ,"LET, IF, FOR", codigoFuente, token.getLex());

    }
    
    private void ASIGFUN(){
        //29. ASIGFUN  -> A E ;
        if(token.getTipo() == TipoToken.TYPE_ASIGNACION) {
            parse = parse.concat("29 ");
            A();
            E();
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
        }
        //30. ASIGFUN  -> ( L ) ;
        else if(token.equals(new Token(TipoToken.TYPE_PARENTESIS, (Integer)0))){
            parse = parse.concat("30 ");
            equipara(new Token(TipoToken.TYPE_PARENTESIS, (Integer)0));
            L();
            equipara(new Token(TipoToken.TYPE_PARENTESIS, (Integer)1));
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
        } else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Se esperaba una asignación '= expr;' o una llamada a función '(...)' (ASIGFUN)",token.getTipo()
        ,"ASIGNACION, PARENTESIS", codigoFuente, token.getLex());

    }
    
    private void A(){
        //31. A -> =
        if(token.equals(new Token(TipoToken.TYPE_ASIGNACION, (Integer) 0))){
            parse = parse.concat("31 ");
            equipara(new Token(TipoToken.TYPE_ASIGNACION, (Integer) 0));
        } //32. A -> |=
        else if(token.equals(new Token(TipoToken.TYPE_ASIGNACION, (Integer) 1))){
            parse = parse.concat("32 ");
            equipara(new Token(TipoToken.TYPE_ASIGNACION, (Integer) 1));
        } else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Se esperaba un operador de asignación (= o |=) (A)",token.getTipo()
        ,"ASGINACION", codigoFuente, token.getLex());

    }

    private void L(){
        //33. L -> E Q
        if(type == TipoToken.TYPE_NEGACION || type == TipoToken.TYPE_IDENTIFICADOR ||
            type == TipoToken.TYPE_CADENA || type == TipoToken.TYPE_ENTERO || type == TipoToken.TYPE_REAL ||
            token.equals(new Token(TipoToken.TYPE_PARENTESIS, (Integer) 0))){
                parse = parse.concat("33 ");
                E();
                Q();
        } //34. L -> lambda FOLLOW(L) = {")"}
        else if (token.equals(new Token(TipoToken.TYPE_PARENTESIS, (Integer) 1))){
            parse = parse.concat("34 ");
        } else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Lista de argumentos mal formada (L)",token.getTipo()
        ,"NEGACION, ID, PARENTESIS, CADENA, ENTERO, REAL", codigoFuente, token.getLex());

    }

    private void Q(){
        //35. Q -> , E Q
        if(type == TipoToken.TYPE_COMA){
            parse = parse.concat("35 ");
            equipara(new Token(TipoToken.TYPE_COMA, (Integer) null));
            E();
            Q();
        } //36. Q -> lambda FOLLOW(Q) = {")"}
        else if (token.equals(new Token(TipoToken.TYPE_PARENTESIS, (Integer) 1))){
            parse = parse.concat("36 ");
        } else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Se esperaba un elemento después de la coma (Q)",token.getTipo()
        ,"COMA, PARENTESIS", codigoFuente, token.getLex());

    }
    
    private void X(){
        //37. X -> E
        if(type == TipoToken.TYPE_NEGACION || type == TipoToken.TYPE_IDENTIFICADOR ||
            type == TipoToken.TYPE_PARENTESIS || type == TipoToken.TYPE_CADENA ||
            type == TipoToken.TYPE_ENTERO || type == TipoToken.TYPE_REAL){
                parse = parse.concat("37 ");
                E();
        } //38. X -> lambda FOLLOW(X) = {";"}
        else if (type == TipoToken.TYPE_PUNTO_Y_COMA){
            parse = parse.concat("38 ");
        } else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Después de 'return' debe venir una expresión o un ';' (X)",token.getTipo()
        ,"NEGACION, ID, PARENTESIS, CADENA, ENTERO, REAL, PUNTO_Y_COMA", codigoFuente, token.getLex());
    }
    
    private void T(){
        //42. T -> int
        if(type == TipoToken.TYPE_INT){
            parse = parse.concat("42 ");
            equipara(new Token(TipoToken.TYPE_INT, (Integer) null));
        } //43. T -> float
        else if(type == TipoToken.TYPE_FLOAT){
            parse = parse.concat("43 ");
            equipara(new Token(TipoToken.TYPE_FLOAT, (Integer) null));
        } //44. T -> boolean
        else if(type == TipoToken.TYPE_BOOLEAN){
            parse = parse.concat("44 ");
            equipara(new Token(TipoToken.TYPE_BOOLEAN, (Integer) null));
        } //45. T -> string
        else if(type == TipoToken.TYPE_STRING){
            parse = parse.concat("45 ");
            equipara(new Token(TipoToken.TYPE_STRING, (Integer) null));
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Leyendo tipo de dato (int, float, boolean, string) (T)",token.getTipo()
        ,"INT, FLOAT, BOOLEAN, STRING", codigoFuente, token.getLex());
    }
    
    private void INI(){
        //46. INI ->  id = E
        if(type == TipoToken.TYPE_IDENTIFICADOR){
            parse = parse.concat("46 ");
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            equipara(new Token(TipoToken.TYPE_ASIGNACION, (Integer) 0));
            E();
        } //47. INI -> lambda FOLLOW(INI) = {";"}
        else if (type == TipoToken.TYPE_PUNTO_Y_COMA){
            parse = parse.concat("47 ");
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Inicialización inválida en el 'for' (INI)",token.getTipo()
        ,"ID, PUNTO_Y_COMA", codigoFuente, token.getLex());
    }
    
    private void ACTU(){
        //48. ACTU -> id A E ;
        if(type == TipoToken.TYPE_IDENTIFICADOR){
            parse = parse.concat("48 ");
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            A();
            E();
        } //49. ACTU -> lambda FOLLOW(ACTU) = {")"}
        else if (token.equals(new Token(TipoToken.TYPE_PARENTESIS, (Integer) 1))){
            parse = parse.concat("49 ");
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Actualización inválida en el 'for' (ACTU)",token.getTipo()
        ,"ID, PARENTESIS", codigoFuente, token.getLex());
    }
    
    private void C(){
        //50. C -> S C
        if(type == TipoToken.TYPE_IDENTIFICADOR || type == TipoToken.TYPE_WRITE ||
        type == TipoToken.TYPE_READ || type == TipoToken.TYPE_RETURN ||
        type == TipoToken.TYPE_LET || type == TipoToken.TYPE_IF || type == TipoToken.TYPE_FOR){
            parse = parse.concat("50 ");
            S();
            C();
        } //51. C -> lambda FOLLOW(C) = {"}"}
        else if (token.equals(new Token(TipoToken.TYPE_LLAVE, (Integer) 1))){
            parse = parse.concat("51 ");
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Bloque de código mal formado (C)",token.getTipo()
        ,"ID, WRITE, READ, RETURN, LET, IF, FOR, LLAVE", codigoFuente, token.getLex());
    }
    
    private void H(){
        //53. H -> T
        if(type == TipoToken.TYPE_INT || type == TipoToken.TYPE_FLOAT ||
        type == TipoToken.TYPE_BOOLEAN || type == TipoToken.TYPE_STRING){
            parse = parse.concat("53 ");
            T();
        } //54. H -> void
        else if(type == TipoToken.TYPE_VOID){
            parse = parse.concat("54 ");
            equipara(new Token(TipoToken.TYPE_VOID, (Integer) null));
        } 
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Se esperaba un tipo de retorno o 'void' (H)",token.getTipo()
        ,"INT, FLOAT, BOOLEAN, STRING, VOID", codigoFuente, token.getLex());
    }

    private void B(){
        //55. B -> T id K
        if(type == TipoToken.TYPE_INT || type == TipoToken.TYPE_FLOAT ||
        type == TipoToken.TYPE_BOOLEAN || type == TipoToken.TYPE_STRING){
            parse = parse.concat("55 ");
            T();
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            K();
        } //56. B -> void
        else if (type == TipoToken.TYPE_VOID){
            parse = parse.concat("56 ");
            equipara(new Token(TipoToken.TYPE_VOID, (Integer) null));
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Lista de parámetros mal formada o incompleta (B)",token.getTipo()
        ,"INT, FLOAT, BOOLEAN, STRING, VOID", codigoFuente, token.getLex());
    }

    private void K(){
        //57. K -> , T id K
        if(type == TipoToken.TYPE_COMA){
            parse = parse.concat("57 ");
            equipara(new Token(TipoToken.TYPE_COMA, (Integer) null));
            T();
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            K();
        } //58. K -> lambda FOLLOW(K) = {")"}
        else if (token.equals(new Token(TipoToken.TYPE_PARENTESIS, (Integer) 1))){
            parse = parse.concat("58 ");
        }
        else throw new Token_Inesperado(lineaAnterior, columnaAnterior,linea, columna,lineaSF, columnaSF, "Error en los parametros de la funcion (K)",token.getTipo()
        ,"COMA, PARENTESIS", codigoFuente, token.getLex());
    }
} 
