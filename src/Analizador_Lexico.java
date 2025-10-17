import java.io.*;

import es.upm.aedlib.Pair;
import es.upm.aedlib.map.*;
import tslib.TS_Gestor;
import es.upm.aedlib.indexedlist.ArrayIndexedList;


public class Analizador_Lexico {
    private HashTableMap<Integer,ArrayIndexedList<Pair<Integer,Character>>> automata;
    private Integer estado_actual;
    private Integer estado_destino;
    private File archivo;
    private FileReader fr;
    private TS_Gestor gestor;
    private Character carActual;
    private int lineaActual;
    private int columna;
    private boolean primeraLectura = true;

    //(DEL, /, *, ', DIGITO,. , LETRA, _, &, |, (, ), {, }, ;, +, -, <, >, =, !, EOF)
    
    public Analizador_Lexico(String ruta, TS_Gestor gestor) {
        this.archivo = new File(ruta);
        this.gestor = gestor;
        this.carActual = null;
        this.lineaActual = 1;
        this.columna = 0;
        try {
            this.fr = new FileReader(archivo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        automata = new HashTableMap<>();
        ArrayIndexedList<Pair<Integer,Character>> estado0 = new ArrayIndexedList<>();
        estado0.add(Caracteres.DEL.ordinal(), new Pair<>(0, 'A'));
        estado0.add(Caracteres.SLASH.ordinal(), new Pair<>(1, 'A'));
        estado0.add(Caracteres.ASTERISCO.ordinal(), new Pair<>(Caracteres.ASTERISCO.ordinal(), null));
        estado0.add(Caracteres.COMILLA.ordinal(), new Pair<>(9, 'M'));
        estado0.add(Caracteres.DIGITO.ordinal(), new Pair<>(4, 'B'));
        estado0.add(Caracteres.PUNTO.ordinal(), new Pair<>(null, null));
        estado0.add(Caracteres.LETRA.ordinal(), new Pair<>(8, 'H'));
        estado0.add(Caracteres.GUION_BAJO.ordinal(), new Pair<>(8, 'H'));
        estado0.add(Caracteres.AMPERSAND.ordinal(), new Pair<>(6, 'H'));
        estado0.add(Caracteres.BARRA_VERTICAL.ordinal(), new Pair<>(7, 'H'));
        estado0.add(Caracteres.PARENTESIS_IZQ.ordinal(), new Pair<>(16, '('));
        estado0.add(Caracteres.PARENTESIS_DER.ordinal(), new Pair<>(16, ')'));
        estado0.add(Caracteres.LLAVE_IZQ.ordinal(), new Pair<>(16, '{'));
        estado0.add(Caracteres.LLAVE_DER.ordinal(), new Pair<>(16, '}'));
        estado0.add(Caracteres.PUNTO_Y_COMA.ordinal(), new Pair<>(16, ';'));
        estado0.add(Caracteres.MAS.ordinal(), new Pair<>(15, '+'));
        estado0.add(Caracteres.MENOS.ordinal(), new Pair<>(15, '-'));
        estado0.add(Caracteres.MENOR_QUE.ordinal(), new Pair<>(15, '<'));
        estado0.add(Caracteres.MAYOR_QUE.ordinal(), new Pair<>(15, '>'));
        estado0.add(Caracteres.IGUAL.ordinal(), new Pair<>(14, '='));
        estado0.add(Caracteres.NEGACION.ordinal(), new Pair<>(14, '!'));
        estado0.add(Caracteres.EOF.ordinal(), new Pair<>(19, 'P'));
        estado0.add(Caracteres.ESPACIO_TAB.ordinal(), new Pair<>(0, 'A'));
        estado0.add(Caracteres.COMA.ordinal(), new Pair<>(19, ','));
        automata.put(0,estado0);
        

        ArrayIndexedList<Pair<Integer,Character>> estado1 = new ArrayIndexedList<>();
        estado1.add(Caracteres.DEL.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.SLASH.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.ASTERISCO.ordinal(), new Pair<>(2, 'A'));
        estado1.add(Caracteres.COMILLA.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.DIGITO.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.PUNTO.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.LETRA.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.GUION_BAJO.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.AMPERSAND.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.BARRA_VERTICAL.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.PARENTESIS_IZQ.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.PARENTESIS_DER.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.LLAVE_IZQ.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.LLAVE_DER.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.PUNTO_Y_COMA.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.MAS.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.MENOS.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.MENOR_QUE.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.MAYOR_QUE.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.IGUAL.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.NEGACION.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.EOF.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.ESPACIO_TAB.ordinal(), new Pair<>(null, null));
        estado1.add(Caracteres.COMA.ordinal(), new Pair<>(null, null));
        automata.put(1,estado1);


        ArrayIndexedList<Pair<Integer,Character>> estado2 = new ArrayIndexedList<>();
        estado2.add(Caracteres.DEL.ordinal(), new Pair<>(2,'A'));
        estado2.add(Caracteres.SLASH.ordinal(), new Pair<>(2,'A'));
        estado2.add(Caracteres.ASTERISCO.ordinal(), new Pair<>(3, 'A'));
        estado2.add(Caracteres.COMILLA.ordinal(), new Pair<>(2,'A'));
        estado2.add(Caracteres.DIGITO.ordinal(), new Pair<>(2,'A'));
        estado2.add(Caracteres.PUNTO.ordinal(), new Pair<>(2, 'A'));
        estado2.add(Caracteres.LETRA.ordinal(), new Pair<>(2,'A'));
        estado2.add(Caracteres.GUION_BAJO.ordinal(), new Pair<>(2, 'A'));
        estado2.add(Caracteres.AMPERSAND.ordinal(), new Pair<>(2,'A'));
        estado2.add(Caracteres.BARRA_VERTICAL.ordinal(), new Pair<>(2,'A'));
        estado2.add(Caracteres.PARENTESIS_IZQ.ordinal(), new Pair<>(2,'A'));
        estado2.add(Caracteres.PARENTESIS_DER.ordinal(), new Pair<>(2,'A'));
        estado2.add(Caracteres.LLAVE_IZQ.ordinal(), new Pair<>(2,'A'));
        estado2.add(Caracteres.LLAVE_DER.ordinal(), new Pair<>(2,'A'));
        estado2.add(Caracteres.PUNTO_Y_COMA.ordinal(), new Pair<>(2,'A'));
        estado2.add(Caracteres.MAS.ordinal(), new Pair<>(2,'A'));
        estado2.add(Caracteres.MENOS.ordinal(), new Pair<>(2,'A'));
        estado2.add(Caracteres.MENOR_QUE.ordinal(), new Pair<>(2,'A'));
        estado2.add(Caracteres.MAYOR_QUE.ordinal(), new Pair<>(2,'A'));
        estado2.add(Caracteres.IGUAL.ordinal(), new Pair<>(2,'A'));
        estado2.add(Caracteres.NEGACION.ordinal(), new Pair<>(2,'A'));
        estado2.add(Caracteres.EOF.ordinal(), new Pair<>(null,'A'));
        estado2.add(Caracteres.ESPACIO_TAB.ordinal(), new Pair<>(2,'A'));
        estado2.add(Caracteres.COMA.ordinal(), new Pair<>(2,'A'));
        automata.put(2,estado2);
        
        
        ArrayIndexedList<Pair<Integer,Character>> estado3 = new ArrayIndexedList<>();
        estado3.add(Caracteres.DEL.ordinal(), new Pair<>(2,'A'));
        estado3.add(Caracteres.SLASH.ordinal(), new Pair<>(0,'A'));
        estado3.add(Caracteres.ASTERISCO.ordinal(), new Pair<>(3, 'A'));
        estado3.add(Caracteres.COMILLA.ordinal(), new Pair<>(2,'A'));
        estado3.add(Caracteres.DIGITO.ordinal(), new Pair<>(2,'A'));
        estado3.add(Caracteres.PUNTO.ordinal(), new Pair<>(2, 'A'));
        estado3.add(Caracteres.LETRA.ordinal(), new Pair<>(2,'A'));
        estado3.add(Caracteres.GUION_BAJO.ordinal(), new Pair<>(2, 'A'));
        estado3.add(Caracteres.AMPERSAND.ordinal(), new Pair<>(2,'A'));
        estado3.add(Caracteres.BARRA_VERTICAL.ordinal(), new Pair<>(2,'A'));
        estado3.add(Caracteres.PARENTESIS_IZQ.ordinal(), new Pair<>(2,'A'));
        estado3.add(Caracteres.PARENTESIS_DER.ordinal(), new Pair<>(2,'A'));
        estado3.add(Caracteres.LLAVE_IZQ.ordinal(), new Pair<>(2,'A'));
        estado3.add(Caracteres.LLAVE_DER.ordinal(), new Pair<>(2,'A'));
        estado3.add(Caracteres.PUNTO_Y_COMA.ordinal(), new Pair<>(2,'A'));
        estado3.add(Caracteres.MAS.ordinal(), new Pair<>(2,'A'));
        estado3.add(Caracteres.MENOS.ordinal(), new Pair<>(2,'A'));
        estado3.add(Caracteres.MENOR_QUE.ordinal(), new Pair<>(2,'A'));
        estado3.add(Caracteres.MAYOR_QUE.ordinal(), new Pair<>(2,'A'));
        estado3.add(Caracteres.IGUAL.ordinal(), new Pair<>(2,'A'));
        estado3.add(Caracteres.NEGACION.ordinal(), new Pair<>(2,'A'));
        estado3.add(Caracteres.EOF.ordinal(), new Pair<>(null,null));
        estado3.add(Caracteres.ESPACIO_TAB.ordinal(), new Pair<>(2,'A'));
        estado3.add(Caracteres.COMA.ordinal(), new Pair<>(2,'A'));
        automata.put(3,estado3);

        
        ArrayIndexedList<Pair<Integer,Character>> estado4 = new ArrayIndexedList<>();
        estado4.add(Caracteres.DEL.ordinal(), new Pair<>(10,'D'));
        estado4.add(Caracteres.SLASH.ordinal(), new Pair<>(10,'D'));
        estado4.add(Caracteres.ASTERISCO.ordinal(), new Pair<>(10, 'D'));
        estado4.add(Caracteres.COMILLA.ordinal(), new Pair<>(10,'D'));
        estado4.add(Caracteres.DIGITO.ordinal(), new Pair<>(4,'C'));
        estado4.add(Caracteres.PUNTO.ordinal(), new Pair<>(5, 'A'));
        estado4.add(Caracteres.LETRA.ordinal(), new Pair<>(10,'D'));
        estado4.add(Caracteres.GUION_BAJO.ordinal(), new Pair<>(10, 'D'));
        estado4.add(Caracteres.AMPERSAND.ordinal(), new Pair<>(10,'D'));
        estado4.add(Caracteres.BARRA_VERTICAL.ordinal(), new Pair<>(10,'D'));
        estado4.add(Caracteres.PARENTESIS_IZQ.ordinal(), new Pair<>(10,'D'));
        estado4.add(Caracteres.PARENTESIS_DER.ordinal(), new Pair<>(10,'D'));
        estado4.add(Caracteres.LLAVE_IZQ.ordinal(), new Pair<>(10,'D'));
        estado4.add(Caracteres.LLAVE_DER.ordinal(), new Pair<>(10,'D'));
        estado4.add(Caracteres.PUNTO_Y_COMA.ordinal(), new Pair<>(10,'D'));
        estado4.add(Caracteres.MAS.ordinal(), new Pair<>(10,'D'));
        estado4.add(Caracteres.MENOS.ordinal(), new Pair<>(10,'D'));
        estado4.add(Caracteres.MENOR_QUE.ordinal(), new Pair<>(10,'D'));
        estado4.add(Caracteres.MAYOR_QUE.ordinal(), new Pair<>(10,'D'));
        estado4.add(Caracteres.IGUAL.ordinal(), new Pair<>(10,'D'));
        estado4.add(Caracteres.NEGACION.ordinal(), new Pair<>(10,'D'));
        estado4.add(Caracteres.EOF.ordinal(), new Pair<>(10,'D'));
        estado4.add(Caracteres.ESPACIO_TAB.ordinal(), new Pair<>(10,'D'));
        estado4.add(Caracteres.COMA.ordinal(), new Pair<>(10,'D'));
        automata.put(4,estado4);


        ArrayIndexedList<Pair<Integer,Character>> estado5 = new ArrayIndexedList<>();
        estado5.add(Caracteres.DEL.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.SLASH.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.ASTERISCO.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.COMILLA.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.DIGITO.ordinal(), new Pair<>(5,'F'));
        estado5.add(Caracteres.PUNTO.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.LETRA.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.GUION_BAJO.ordinal(), new Pair<>(11, 'G'));
        estado5.add(Caracteres.AMPERSAND.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.BARRA_VERTICAL.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.PARENTESIS_IZQ.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.PARENTESIS_DER.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.LLAVE_IZQ.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.LLAVE_DER.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.PUNTO_Y_COMA.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.MAS.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.MENOS.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.MENOR_QUE.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.MAYOR_QUE.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.IGUAL.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.NEGACION.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.EOF.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.ESPACIO_TAB.ordinal(), new Pair<>(11,'G'));
        estado5.add(Caracteres.COMA.ordinal(), new Pair<>(11,'G'));
        automata.put(5,estado5);

        
        ArrayIndexedList<Pair<Integer,Character>> estado6 = new ArrayIndexedList<>();
        estado6.add(Caracteres.DEL.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.SLASH.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.ASTERISCO.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.COMILLA.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.DIGITO.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.PUNTO.ordinal(), new Pair<>(null, null));
        estado6.add(Caracteres.LETRA.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.GUION_BAJO.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.AMPERSAND.ordinal(), new Pair<>(12,'I'));
        estado6.add(Caracteres.BARRA_VERTICAL.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.PARENTESIS_IZQ.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.PARENTESIS_DER.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.LLAVE_IZQ.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.LLAVE_DER.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.PUNTO_Y_COMA.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.MAS.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.MENOS.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.MENOR_QUE.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.MAYOR_QUE.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.IGUAL.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.NEGACION.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.EOF.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.ESPACIO_TAB.ordinal(), new Pair<>(null,null));
        estado6.add(Caracteres.COMA.ordinal(), new Pair<>(null,null));
        automata.put(6,estado6);

        
        ArrayIndexedList<Pair<Integer,Character>> estado7 = new ArrayIndexedList<>();
        estado7.add(Caracteres.DEL.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.SLASH.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.ASTERISCO.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.COMILLA.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.DIGITO.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.PUNTO.ordinal(), new Pair<>(null, null));
        estado7.add(Caracteres.LETRA.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.GUION_BAJO.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.AMPERSAND.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.BARRA_VERTICAL.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.PARENTESIS_IZQ.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.PARENTESIS_DER.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.LLAVE_IZQ.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.LLAVE_DER.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.PUNTO_Y_COMA.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.MAS.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.MENOS.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.MENOR_QUE.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.MAYOR_QUE.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.IGUAL.ordinal(), new Pair<>(13,'J'));
        estado7.add(Caracteres.NEGACION.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.EOF.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.ESPACIO_TAB.ordinal(), new Pair<>(null,null));
        estado7.add(Caracteres.COMA.ordinal(), new Pair<>(null,null));
        automata.put(7,estado7);

        
        ArrayIndexedList<Pair<Integer,Character>> estado8 = new ArrayIndexedList<>();
        estado8.add(Caracteres.DEL.ordinal(), new Pair<>(17,'L'));
        estado8.add(Caracteres.SLASH.ordinal(), new Pair<>(17,'L'));
        estado8.add(Caracteres.ASTERISCO.ordinal(), new Pair<>(17, 'L'));
        estado8.add(Caracteres.COMILLA.ordinal(), new Pair<>(17,'L'));
        estado8.add(Caracteres.DIGITO.ordinal(), new Pair<>(8,'K'));
        estado8.add(Caracteres.PUNTO.ordinal(), new Pair<>(17, 'L'));
        estado8.add(Caracteres.LETRA.ordinal(), new Pair<>(8,'K'));
        estado8.add(Caracteres.GUION_BAJO.ordinal(), new Pair<>(8, 'K'));
        estado8.add(Caracteres.AMPERSAND.ordinal(), new Pair<>(17,'L'));
        estado8.add(Caracteres.BARRA_VERTICAL.ordinal(), new Pair<>(17,'L'));
        estado8.add(Caracteres.PARENTESIS_IZQ.ordinal(), new Pair<>(17,'L'));
        estado8.add(Caracteres.PARENTESIS_DER.ordinal(), new Pair<>(17,'L'));
        estado8.add(Caracteres.LLAVE_IZQ.ordinal(), new Pair<>(17,'L'));
        estado8.add(Caracteres.LLAVE_DER.ordinal(), new Pair<>(17,'L'));
        estado8.add(Caracteres.PUNTO_Y_COMA.ordinal(), new Pair<>(17,'L'));
        estado8.add(Caracteres.MAS.ordinal(), new Pair<>(17,'L'));
        estado8.add(Caracteres.MENOS.ordinal(), new Pair<>(17,'L'));
        estado8.add(Caracteres.MENOR_QUE.ordinal(), new Pair<>(17,'L'));
        estado8.add(Caracteres.MAYOR_QUE.ordinal(), new Pair<>(17,'L'));
        estado8.add(Caracteres.IGUAL.ordinal(), new Pair<>(17,'L'));
        estado8.add(Caracteres.NEGACION.ordinal(), new Pair<>(17,'L'));
        estado8.add(Caracteres.EOF.ordinal(), new Pair<>(17,'L'));
        estado8.add(Caracteres.ESPACIO_TAB.ordinal(), new Pair<>(17, 'L'));
        estado8.add(Caracteres.COMA.ordinal(), new Pair<>(17,'L'));
        automata.put(8,estado8);
        
        
        ArrayIndexedList<Pair<Integer,Character>> estado9 = new ArrayIndexedList<>();
        estado9.add(Caracteres.DEL.ordinal(), new Pair<>(null,null));
        estado9.add(Caracteres.SLASH.ordinal(), new Pair<>(9,'K'));
        estado9.add(Caracteres.ASTERISCO.ordinal(), new Pair<>(9, 'K'));
        estado9.add(Caracteres.COMILLA.ordinal(), new Pair<>(18,'O'));
        estado9.add(Caracteres.DIGITO.ordinal(), new Pair<>(9,'K'));
        estado9.add(Caracteres.PUNTO.ordinal(), new Pair<>(9, 'K'));
        estado9.add(Caracteres.LETRA.ordinal(), new Pair<>(9,'K'));
        estado9.add(Caracteres.GUION_BAJO.ordinal(), new Pair<>(9, 'K'));
        estado9.add(Caracteres.AMPERSAND.ordinal(), new Pair<>(9,'K'));
        estado9.add(Caracteres.BARRA_VERTICAL.ordinal(), new Pair<>(9,'K'));
        estado9.add(Caracteres.PARENTESIS_IZQ.ordinal(), new Pair<>(9,'K'));
        estado9.add(Caracteres.PARENTESIS_DER.ordinal(), new Pair<>(9,'K'));
        estado9.add(Caracteres.LLAVE_IZQ.ordinal(), new Pair<>(9,'K'));
        estado9.add(Caracteres.LLAVE_DER.ordinal(), new Pair<>(9,'K'));
        estado9.add(Caracteres.PUNTO_Y_COMA.ordinal(), new Pair<>(9,'K'));
        estado9.add(Caracteres.MAS.ordinal(), new Pair<>(9,'K'));
        estado9.add(Caracteres.MENOS.ordinal(), new Pair<>(9,'K'));
        estado9.add(Caracteres.MENOR_QUE.ordinal(), new Pair<>(9,'K'));
        estado9.add(Caracteres.MAYOR_QUE.ordinal(), new Pair<>(9,'K'));
        estado9.add(Caracteres.IGUAL.ordinal(), new Pair<>(9,'K'));
        estado9.add(Caracteres.NEGACION.ordinal(), new Pair<>(9,'K'));
        estado9.add(Caracteres.EOF.ordinal(), new Pair<>(null,null));
        estado9.add(Caracteres.ESPACIO_TAB.ordinal(), new Pair<>(9, 'K'));
        estado9.add(Caracteres.COMA.ordinal(), new Pair<>(9,'K'));
        automata.put(9,estado9);

        estado_actual = 0;
    }

    public Token iniciar() throws Err {
        Token token = null;
        Integer charEnum;
        int countDecimales = 0;
        double num = 0;
        double dec = 0;
        String lex = "";
        char accion;
        estado_actual = 0;

        try{
            if(primeraLectura){
                carActual = leer(fr);
            } primeraLectura = false;

            while (estado_actual < 10){

            
                charEnum = getcharEnum(carActual);
                
                if(charEnum == -1){
                    if(estado_actual == 9){
                        accion = 'K'; estado_destino = 9;
                    } else throw new InvalidCaracterErr(lineaActual,columna,estado_actual,Caracteres.values()[charEnum]);

                } else{
                    accion = automata.get(estado_actual).get(charEnum).getRight();
                    estado_destino = automata.get(estado_actual).get(charEnum).getLeft();
                }
                
                //Gestion de errores
                if (estado_destino == null) {
                    throw new InvalidCaracterErr(lineaActual,columna,estado_actual,Caracteres.values()[charEnum]);
                }
                
                //Se lee siempre menos cuando oc = true
                switch (accion) {
                    //Leo (0-0)
                    case 'A': carActual = leer(fr); break;
                    //Principio digito (0-4)
                    case 'B':
                        num = (int) carActual - 48;
                        carActual = leer(fr);
                        break;
                    //Concateno digitos (4-4) 
                    case 'C':
                        num = num * 10 + ((int) carActual - 48);
                        carActual = leer(fr);
                        break;
                    //Genero Token Entero (4-10)
                    case 'D':
                        if(num >= 32767) throw new IntTooLargeErr(lineaActual,columna,estado_actual,Caracteres.values()[charEnum], (int)num);
                        token = new Token(TipoToken.TYPE_ENTERO, (Integer) (int) num);
                        break;
                    //Comienzo contador decimal (4-5)
                    case 'E':
                        countDecimales = 0;
                        dec = 0;
                        carActual = leer(fr);
                        break;
                    //Concateno decimal (5-5)
                    case 'F':
                        countDecimales++;
                        dec = dec * 10 + ((int) carActual - 48);
                        carActual = leer(fr);
                        break;
                    //Genero Token real (5-11)
                    case 'G':
                        if(!(countDecimales > 0 && num < 117549436.0))
                            throw new RealTooLargeErr(lineaActual,columna,estado_actual,Caracteres.values()[charEnum],(double)num, countDecimales); 
                        token = new Token(TipoToken.TYPE_REAL, num + dec / Math.pow(10, countDecimales));
                        break;
                    //Principio de lexema (0-6) (0-7) (0-8)
                    case 'H':
                        lex = Character.toString(carActual);
                        carActual = leer(fr);
                        break;
                    //Genero Token AND (6-12)
                    case 'I':
                        lex = lex.concat(Character.toString(carActual));
                        token = new Token(TipoToken.TYPE_AND, (Integer) null);
                        carActual = leer(fr);
                        break;
                    //Genero Token |= (7-13)
                    case 'J':
                        lex.concat(Character.toString(carActual));
                        token = new Token(TipoToken.TYPE_ASIGNACION, (Integer) 1);
                        carActual = leer(fr);
                        break;
                    //Concateno caracter (9-9) (8-8)
                    case 'K':
                        lex = lex.concat(Character.toString(carActual));
                        carActual = leer(fr);
                        break;
                    //Genero Token ID (8-17)
                    case 'L':
                        if (lex.length() >= 64)throw new IdTooLargeErr(lineaActual, columna,estado_actual, Caracteres.values()[charEnum]);
                        if(gestor.getEntradaTPalabrasReservadas(lex) == 0){
                            if(gestor.getEntradaTSGlobal(lex) == 0){
                                int pos=gestor.addEntradaTSGlobal(lex);
                                if(pos==0){
                                    System.out.println("Error al añadir un identificador a la tabla de símbolos global");
                                }
                                token = new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) pos);
                            } else token = new Token(TipoToken.TYPE_IDENTIFICADOR, (Integer) gestor.getEntradaTSGlobal(lex));
                        } else {
                            switch(gestor.getEntradaTPalabrasReservadas(lex)){
                                case 1: token = new Token(TipoToken.TYPE_LET, (Integer) null); break;
                                case 2: token = new Token(TipoToken.TYPE_INT, (Integer) null); break;
                                case 3: token = new Token(TipoToken.TYPE_FLOAT, (Integer) null); break;
                                case 4: token = new Token(TipoToken.TYPE_BOOLEAN, (Integer) null); break;
                                case 5: token = new Token(TipoToken.TYPE_STRING, (Integer) null); break;
                                case 6: token = new Token(TipoToken.TYPE_VOID, (Integer) null); break;
                                case 7: token = new Token(TipoToken.TYPE_FOR, (Integer) null); break;
                                case 8: token = new Token(TipoToken.TYPE_FUNCTION, (Integer) null); break;
                                case 9: token = new Token(TipoToken.TYPE_RETURN, (Integer) null); break;
                                case 10: token = new Token(TipoToken.TYPE_IF, (Integer) null); break;
                                case 11: token = new Token(TipoToken.TYPE_READ, (Integer) null); break;
                                case 12: token = new Token(TipoToken.TYPE_WRITE, (Integer) null); break;
                            }
                        }
                        break;
                    //Reinicio Cadena (0-9)
                    case 'M':
                        lex = "";
                        carActual = leer(fr);
                        break;
                    //Genero Token Cadena (9-18)
                    case 'O':
                        if(lex.length() >= 64) throw new CadenaTooLargeErr(lineaActual, columna,estado_actual, Caracteres.values()[charEnum],lex, lex.length());
                        token = new Token(TipoToken.TYPE_CADENA, lex);
                        carActual = leer(fr);
                        break;
                    //(0-19)
                    case 'P':
                        token = new Token(TipoToken.TYPE_EOF, (Integer) null);
                        carActual = leer(fr);
                        break;
                    //(0-16)
                    case ';':
                        token = new Token(TipoToken.TYPE_PUNTO_Y_COMA, (Integer) null);
                        carActual = leer(fr);
                        break;
                    //(0-16)
                    case '(':
                        token = new Token(TipoToken.TYPE_PARENTESIS, (Integer) 0);
                        carActual = leer(fr);
                        break;
                    //(0-16)
                    case ')':
                        token = new Token(TipoToken.TYPE_PARENTESIS, (Integer) 1);
                        carActual = leer(fr);
                        break;
                    //(0-16)
                    case '{':
                        token = new Token(TipoToken.TYPE_LLAVE, (Integer) 0);
                        carActual = leer(fr);
                        break;
                    //(0-16)
                    case '}':
                        token = new Token(TipoToken.TYPE_LLAVE, (Integer) 1);
                        carActual = leer(fr);
                        break;
                    //(0-15)
                    case '+':
                        token = new Token(TipoToken.TYPE_ARITMETICO, (Integer) 0);
                        carActual = leer(fr);
                        break;
                    //(0-15) 
                    case '-':
                        token = new Token(TipoToken.TYPE_ARITMETICO, (Integer) 1);
                        carActual = leer(fr);
                        break;
                    //(0-15)
                    case '>':
                        token = new Token(TipoToken.TYPE_RELACION, (Integer) 0);
                        carActual = leer(fr);
                        break;
                    //(0-15)
                    case '<':
                        token = new Token(TipoToken.TYPE_RELACION, (Integer) 1);
                        carActual = leer(fr);
                        break;
                    //(0-14)
                    case '=':
                        token = new Token(TipoToken.TYPE_ASIGNACION, (Integer) 0);
                        carActual = leer(fr);
                        break;
                    //(0-14)
                    case '!':
                        token = new Token(TipoToken.TYPE_NEGACION, (Integer) null);
                        carActual = leer(fr);
                        break;
                    //TO-DO
                    case ',':
                        token = new Token(TipoToken.TYPE_COMA, (Integer) null);
                        carActual = leer(fr);
                        break;
                    
                        
                } //switch

                estado_actual = estado_destino;

            } //while
        } 
        catch(FileNotFoundException e){
            System.err.println("Archivo inexistente");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return token;
    }

    
    private int getcharEnum(Character c) {

        switch(c){
            case '/': return Caracteres.SLASH.ordinal();
            case '*': return Caracteres.ASTERISCO.ordinal();
            case '\'': return Caracteres.COMILLA.ordinal();
            case '_': return Caracteres.GUION_BAJO.ordinal();
            case '&': return Caracteres.AMPERSAND.ordinal();
            case '|': return Caracteres.BARRA_VERTICAL.ordinal();
            case '(': return Caracteres.PARENTESIS_IZQ.ordinal();
            case ')': return Caracteres.PARENTESIS_DER.ordinal();
            case '{': return Caracteres.LLAVE_IZQ.ordinal();
            case '}': return Caracteres.LLAVE_DER.ordinal();
            case ';': return Caracteres.PUNTO_Y_COMA.ordinal();
            case '+': return Caracteres.MAS.ordinal();
            case '-': return Caracteres.MENOS.ordinal();
            case '<': return Caracteres.MENOR_QUE.ordinal();
            case '>': return Caracteres.MAYOR_QUE.ordinal();
            case '=': return Caracteres.IGUAL.ordinal();
            case '!': return Caracteres.NEGACION.ordinal();
            case '.': return Caracteres.PUNTO.ordinal();
            case ' ': return Caracteres.ESPACIO_TAB.ordinal();
            case '\t': return Caracteres.ESPACIO_TAB.ordinal();
            case 26: return Caracteres.EOF.ordinal();
            case ',': return Caracteres.COMA.ordinal();
            default:
                if(Character.isDigit(c)){
                    return Caracteres.DIGITO.ordinal();
                } else if(Character.isLetter(c)){
                    return Caracteres.LETRA.ordinal();
                    
                } else if(Character.isWhitespace(c)){
                    return Caracteres.DEL.ordinal();
                }
        }
        return -1;
    }

    private Character leer(FileReader fr) throws IOException {
        int cIntLeido = fr.read();
        columna++;
        if(cIntLeido == -1){
            cIntLeido = 26; //Simulo EOF
        }else if (cIntLeido == 10 ){
            columna = 0;
            lineaActual++;
        }
        return (char) cIntLeido;
    }
}