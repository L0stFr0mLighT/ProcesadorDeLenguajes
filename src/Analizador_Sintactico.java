import Enumeraciones.*;
import Errores.*;

import java.io.FileWriter;
import java.io.IOException;


public class Analizador_Sintactico {
    private Token token;
    private Analizador_Lexico ALex;
    private String parse;
    private TipoToken type;

    public Analizador_Sintactico (Analizador_Lexico ALex){
        this.ALex = ALex;
        parse = "D ";
    }

    public void iniciar(){
        try {
            token = ALex.iniciar();
            type = token.getTipo();
            P();
            if (token.getTipo() != TipoToken.TYPE_EOF){
                //TO-DO ERROR
            }

        } catch (Exception e) {
        
        }

    }

    private void equipara (Token t){
        try{
            if (TipoToken.TYPE_IDENTIFICADOR == t.getTipo() || 
                TipoToken.TYPE_ENTERO == t.getTipo() || 
                TipoToken.TYPE_REAL == t.getTipo() ||
                TipoToken.TYPE_CADENA == t.getTipo() ||
                token.equals(t)) {
                    token = ALex.iniciar();
                    type = token.getTipo();
                } 
            else;//ERROR TERMINAL INESPERADO
        }catch(Exception e){

        }
    }

    private void P() {
        //1. P -> S P 
        if (type == TipoToken.TYPE_IDENTIFICADOR || type == TipoToken.TYPE_WRITE ||
        type == TipoToken.TYPE_READ || type == TipoToken.TYPE_RETURN ||
        type == TipoToken.TYPE_LET || type == TipoToken.TYPE_IF || type == TipoToken.TYPE_FOR){
            parse.concat("1 "); 
            S(); P();        
        } //2. P -> F P 
        else if(type == TipoToken.TYPE_FUNCTION) {
            parse.concat("2 ");
            F(); P();
        } //3. P -> lambda
        else if (type == TipoToken.TYPE_EOF) {
            parse.concat("3 ");
            equipara(new Token(TipoToken.TYPE_EOF, (Integer) null));
        } else; //ERROR TOKEN INESPERADO
    }

    private void S(){
        //23. S -> S_SIMPLE
        if(type == TipoToken.TYPE_IDENTIFICADOR || type == TipoToken.TYPE_WRITE ||
            type == TipoToken.TYPE_READ || type == TipoToken.TYPE_RETURN){
                parse.concat("23 ");
                S_SIMPLE();
        } //24. S -> S_COMPUESTA
        else if(type == TipoToken.TYPE_LET || type == TipoToken.TYPE_IF || type == TipoToken.TYPE_FOR){
                parse.concat("24 ");
                S_COMPUESTA();
        } else; //ERROR TOKEN INESPERADO
    }
    
    private void F() {
        //52. F -> function H id ( B ) { C }
        parse.concat("52 ");
        equipara(new Token(TipoToken.TYPE_FUNCTION, (Integer) null));
        H();
        equipara(new Token (TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
        equipara(new Token (TipoToken.TYPE_PARENTESIS, 0));
        B();
        equipara(new Token (TipoToken.TYPE_PARENTESIS, 1));
        equipara(new Token (TipoToken.TYPE_LLAVE, 0));
        C();
        equipara(new Token (TipoToken.TYPE_LLAVE, 1));
    }
    
    private void E() {
        //4. E -> E1 E_AND
        if(type == TipoToken.TYPE_NEGACION || type == TipoToken.TYPE_IDENTIFICADOR ||
            type == TipoToken.TYPE_PARENTESIS || type == TipoToken.TYPE_CADENA ||
            type == TipoToken.TYPE_ENTERO || type == TipoToken.TYPE_REAL){
                parse.concat("4 ");
                E1();
                E_AND();
        }
        else; //ERROR TOKEN INESPERADO
    }

    private void E_AND(){
        //5. E_AND -> && E1 E_AND
        if(type == TipoToken.TYPE_AND){
            parse.concat("5 ");
            equipara(new Token (TipoToken.TYPE_AND, (Integer) null));
            E1();
            E_AND();
        }
        //6. E_AND -> lambda
        else if (type == TipoToken.TYPE_EOF){
            parse.concat("6 ");
            equipara(new Token(TipoToken.TYPE_EOF, (Integer) null));
        }
        else; //ERROR TOKEN INESPERADO
    }
    
    private void E1(){
        //7. E1 -> E2 E1_REL
        if(type == TipoToken.TYPE_NEGACION || type == TipoToken.TYPE_IDENTIFICADOR ||
            type == TipoToken.TYPE_PARENTESIS || type == TipoToken.TYPE_CADENA ||
            type == TipoToken.TYPE_ENTERO || type == TipoToken.TYPE_REAL){
            parse.concat("7 ");
            E2();
            E1_REL();
        }
        else; //ERROR TOKEN INESPERADO
    }
    
    private void E1_REL(){
        //8. E1_REL  -> > E2 E1_REL
        if (token.equals(new Token(TipoToken.TYPE_RELACION,(Integer)0))){
            equipara(new Token (TipoToken.TYPE_RELACION,(Integer)0));
            E2();
            E1_REL();
        }
        //9. E1_REL  -> < E2 E1_REL
        else if (token.equals(new Token(TipoToken.TYPE_RELACION,(Integer)1))){
            equipara(new Token (TipoToken.TYPE_RELACION,(Integer)1));
            E2();
            E1_REL();
        }
        //10. E1_REL  -> lambda
        else if (type == TipoToken.TYPE_EOF){
            parse.concat("10 ");
            equipara(new Token(TipoToken.TYPE_EOF, (Integer) null));
        }
        else; //ERROR TOKEN INESPERADO
    }
    
    private void E2(){
        //11. E2 -> E3 E2_ARIT
        if(type == TipoToken.TYPE_NEGACION || type == TipoToken.TYPE_IDENTIFICADOR ||
            type == TipoToken.TYPE_PARENTESIS || type == TipoToken.TYPE_CADENA ||
            type == TipoToken.TYPE_ENTERO || type == TipoToken.TYPE_REAL){
            parse.concat("11 ");
            E3();
            E2_ARIT();
        }
        else; //ERROR TOKEN INESPERADO
    }
    
    private void E2_ARIT(){
        //12. E2_ARIT -> + E3 E2_ARIT
        if (token.equals(new Token(TipoToken.TYPE_ARITMETICO,(Integer)0))){
            equipara(new Token (TipoToken.TYPE_ARITMETICO,(Integer)0));
            E3();
            E2_ARIT();
        }
        //13. E2_ARIT -> - E3 E2_ARIT
        else if (token.equals(new Token(TipoToken.TYPE_ARITMETICO,(Integer)1))){
            equipara(new Token (TipoToken.TYPE_ARITMETICO,(Integer)1));
            E3();
            E2_ARIT();
        }
        //14. E2_ARIT -> lambda
        else if (type == TipoToken.TYPE_EOF){
            parse.concat("14 ");
            equipara(new Token(TipoToken.TYPE_EOF, (Integer) null));
        }
        else; //ERROR TOKEN INESPERADO

    }
    
    private void E3(){
        //15. E3  -> ! E3 
        if (type == TipoToken.TYPE_NEGACION){
            equipara(new Token(TipoToken.TYPE_NEGACION,(Integer)null));
            E3();
        }
        //16. E3  -> id E4
        else if (type == TipoToken.TYPE_IDENTIFICADOR){
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            E4();
        }
        //17. E3  -> ( E )
        else if (token.equals(new Token(TipoToken.TYPE_PARENTESIS,(Integer)0))){
            equipara(new Token(TipoToken.TYPE_PARENTESIS,(Integer)0));
            E();
            equipara(new Token(TipoToken.TYPE_PARENTESIS,(Integer)1));
        }
        //18. E3  -> cadena
        else if (type == TipoToken.TYPE_CADENA){
            equipara(new Token(TipoToken.TYPE_CADENA,(Integer)null));
        }
        //19. E3  -> entero
        else if (type == TipoToken.TYPE_ENTERO){
            equipara(new Token(TipoToken.TYPE_ENTERO,(Integer)null));
        }
        //20. E3  -> real 
        else if (type == TipoToken.TYPE_REAL){
            equipara(new Token(TipoToken.TYPE_REAL,(Integer)null));
        }
        else; //ERROR TOKEN INESPERADO

    }

    private void E4(){
        //21. E4  -> ( L )
        if (token.equals(new Token(TipoToken.TYPE_PARENTESIS,(Integer)0))){
            equipara(new Token(TipoToken.TYPE_PARENTESIS,(Integer)0));
            L();
            equipara(new Token(TipoToken.TYPE_PARENTESIS,(Integer)1));
        }
        //22. E4  -> lambda
        else if (type == TipoToken.TYPE_EOF){
            parse.concat("14 ");
            equipara(new Token(TipoToken.TYPE_EOF, (Integer) null));
        }
        else; //ERROR TOKEN INESPERADO

    }
    
    private void S_SIMPLE(){
        //25. S_SIMPLE -> id ASIGFUN
        if(type == TipoToken.TYPE_IDENTIFICADOR){
            parse.concat("25 ");
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            ASIGFUN();
        } //26. S_SIMPLE -> write E ;
        else if(type == TipoToken.TYPE_READ){
            parse.concat("26 ");
            equipara(new Token(TipoToken.TYPE_WRITE, (Integer) null));
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
            E();
        } //27. S_SIMPLE -> read id ;
        else if(type == TipoToken.TYPE_READ){
            parse.concat("27 ");
            equipara(new Token(TipoToken.TYPE_READ, (Integer) null));
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
        } //28. S_SIMPLE -> return X ;
        else if(type == TipoToken.TYPE_RETURN){
            parse.concat("28 ");
            equipara(new Token(TipoToken.TYPE_RETURN, (Integer) null));
            X();
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
        } else; //ERROR TOKEN INESPERADO

    }  
    
    private void S_COMPUESTA(){
        //39. S_COMPUESTA -> let T id ;
        if(type == TipoToken.TYPE_LET){
            parse.concat("39 ");
            equipara(new Token(TipoToken.TYPE_LET, (Integer) null));
            T();
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
        }
        //40. S_COMPUESTA -> if ( E ) S
        else if(type == TipoToken.TYPE_IF){
            parse.concat("40 ");
            equipara(new Token(TipoToken.TYPE_IF, (Integer) null));
            equipara(new Token(TipoToken.TYPE_PARENTESIS, 0));
            E();
            equipara(new Token(TipoToken.TYPE_PARENTESIS, 1));
            S();
        }
        //41. S_COMPUESTA -> for ( INI ; E ; ACTU ) { C }
        else if(type == TipoToken.TYPE_FOR){
            parse.concat("41 ");
            equipara(new Token(TipoToken.TYPE_FOR, (Integer) null));
            equipara(new Token(TipoToken.TYPE_PARENTESIS, 0));
            INI();
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
            E();
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
            ACTU();
            equipara(new Token(TipoToken.TYPE_PARENTESIS, 1));
            equipara(new Token(TipoToken.TYPE_LLAVE, 0));
            C();
            equipara(new Token(TipoToken.TYPE_LLAVE, 1));
        } else; //ERROR TOKEN INESPERADO

    }
    
    private void ASIGFUN(){
        //29. ASIGFUN  -> A E ;
        if(token.equals(new Token(TipoToken.TYPE_ASIGNACION, 0)) || 
        token.equals(new Token(TipoToken.TYPE_ASIGNACION, 1))) {
            parse.concat("29 ");
            A();
            E();
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
        }
        //30. ASIGFUN  -> ( L ) ;
        else if(type == TipoToken.TYPE_PARENTESIS && token.getAtributo() == 0){
            parse.concat("30 ");
            equipara(new Token(TipoToken.TYPE_PARENTESIS, 0));
            L();
            equipara(new Token(TipoToken.TYPE_PARENTESIS, 1));
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
        } else; //ERROR TOKEN INESPERADO

    }
    
    private void A(){
        //31. A -> =
        if(token.equals(new Token(TipoToken.TYPE_ASIGNACION, 0))){
            parse.concat("31 ");
            equipara(new Token(TipoToken.TYPE_ASIGNACION, 0));
        } //32. A -> |=
        else if(token.equals(new Token(TipoToken.TYPE_ASIGNACION, 1))){
            parse.concat("32 ");
            equipara(new Token(TipoToken.TYPE_ASIGNACION, 1));
        } else; //ERROR TOKEN INESPERADO

    }

    private void L(){
        //33. L -> E Q
        if(type == TipoToken.TYPE_NEGACION || type == TipoToken.TYPE_IDENTIFICADOR ||
            type == TipoToken.TYPE_PARENTESIS || type == TipoToken.TYPE_CADENA ||
            type == TipoToken.TYPE_ENTERO || type == TipoToken.TYPE_REAL){
                parse.concat("33 ");
                E();
                Q();
        } //34. L -> lambda
        else if (type == TipoToken.TYPE_EOF){
            parse.concat("34 ");
            equipara(new Token(TipoToken.TYPE_EOF, (Integer) null));
        } else; //ERROR TOKEN INESPERADO

    }

    private void Q(){
        //35. Q -> , E Q
        if(type == TipoToken.TYPE_COMA){
            parse.concat("35 ");
            equipara(new Token(TipoToken.TYPE_COMA, (Integer) null));
            E();
            Q();
        } //36. Q -> lambda
        else if (type == TipoToken.TYPE_EOF){
            parse.concat("36 ");
            equipara(new Token(TipoToken.TYPE_EOF, (Integer) null));
        } else; //ERROR TOKEN INESPERADO

    }
    
    private void X(){
        //37. X -> E
        if(type == TipoToken.TYPE_NEGACION || type == TipoToken.TYPE_IDENTIFICADOR ||
            type == TipoToken.TYPE_PARENTESIS || type == TipoToken.TYPE_CADENA ||
            type == TipoToken.TYPE_ENTERO || type == TipoToken.TYPE_REAL){
                parse.concat("37 ");
                E();
        } //38. X -> lambda
        else if (type == TipoToken.TYPE_EOF){
            parse.concat("38 ");
            equipara(new Token(TipoToken.TYPE_EOF, (Integer) null));
        } else; //ERROR TOKEN INESPERADO
    }
    
    private void T(){
        //42. T -> int
        if(type == TipoToken.TYPE_INT){
            parse.concat("42 ");
            equipara(new Token(TipoToken.TYPE_INT, (Integer) null));
        } //43. T -> float
        else if(type == TipoToken.TYPE_FLOAT){
            parse.concat("43 ");
            equipara(new Token(TipoToken.TYPE_FLOAT, (Integer) null));
        } //44. T -> boolean
        else if(type == TipoToken.TYPE_BOOLEAN){
            parse.concat("44 ");
            equipara(new Token(TipoToken.TYPE_BOOLEAN, (Integer) null));
        } //45. T -> string
        else if(type == TipoToken.TYPE_STRING){
            parse.concat("45 ");
            equipara(new Token(TipoToken.TYPE_STRING, (Integer) null));
        }
        else; //ERROR TOKEN INESPERADO
    }
    
    private void INI(){
        //46. INI ->  id = E ;
        if(type == TipoToken.TYPE_IDENTIFICADOR){
            parse.concat("46 ");
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            equipara(new Token(TipoToken.TYPE_ASIGNACION, 0));
            E();
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
        } //47. INI -> lambda
        else if (type == TipoToken.TYPE_EOF){
            parse.concat("47 ");
            equipara(new Token(TipoToken.TYPE_EOF, (Integer) null));
        }
        else; //ERROR TOKEN INESPERADO
    }
    
    private void ACTU(){
        //48. ACTU -> id A = E ;
        if(type == TipoToken.TYPE_IDENTIFICADOR){
            parse.concat("49 ");
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            A();
            equipara(new Token(TipoToken.TYPE_ASIGNACION, 0));
            E();
            equipara(new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null));
        } //49. ACTU -> lambda
        else if (type == TipoToken.TYPE_EOF){
            parse.concat("51 ");
            equipara(new Token(TipoToken.TYPE_EOF, (Integer) null));
        }
        else; //ERROR TOKEN INESPERADO
    }
    
    private void C(){
        //50. C -> S C
        if(type == TipoToken.TYPE_IDENTIFICADOR || type == TipoToken.TYPE_WRITE ||
        type == TipoToken.TYPE_READ || type == TipoToken.TYPE_RETURN ||
        type == TipoToken.TYPE_LET || type == TipoToken.TYPE_IF || type == TipoToken.TYPE_FOR){
            parse.concat("50 ");
            S(); C();
        } //51. C -> lambda
        else if (type == TipoToken.TYPE_EOF){
            parse.concat("51 ");
            equipara(new Token(TipoToken.TYPE_EOF, (Integer) null));
        }
        else; //ERROR TOKEN INESPERADO
    }
    
    private void H(){
        //53. H -> T
        if(type == TipoToken.TYPE_INT || type == TipoToken.TYPE_FLOAT ||
        type == TipoToken.TYPE_BOOLEAN || type == TipoToken.TYPE_STRING){
            parse.concat("53 ");
            equipara(new Token(TipoToken.TYPE_INT, (Integer) null));
        } //54. H -> void
        else if(type == TipoToken.TYPE_VOID){
            parse.concat("54 ");
            equipara(new Token(TipoToken.TYPE_VOID, (Integer) null));
        } else; //ERROR TOKEN INESPERADO
    }

    private void B(){
        //55. B -> T id K
        if(type == TipoToken.TYPE_INT || type == TipoToken.TYPE_FLOAT ||
        type == TipoToken.TYPE_BOOLEAN || type == TipoToken.TYPE_STRING){
            parse.concat("55 ");
            T();
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            K();
        } //56. B -> void
        else if (type == TipoToken.TYPE_VOID){
            parse.concat("56 ");
            equipara(new Token(TipoToken.TYPE_VOID, (Integer) null));
        }
        else; //ERROR TOKEN INESPERADO
    }

    private void K(){
        //57. K -> , T id K
        if(type == TipoToken.TYPE_COMA){
            parse.concat("57 ");
            equipara(new Token(TipoToken.TYPE_COMA, (Integer) null));
            T();
            equipara(new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) null));
            K();
        } //58. K -> lambda
        else if (type == TipoToken.TYPE_EOF){
            parse.concat("58 ");
            equipara(new Token(TipoToken.TYPE_EOF, (Integer) null));
        }
        else; //ERROR TOKEN INESPERADO
    }
}
