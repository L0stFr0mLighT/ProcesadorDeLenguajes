import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import Errores.*;
import tslib.TS_Gestor;
import tslib.TS_Gestor.Tabla;
import java.util.List;

public class Procesador {
    public static void main(String[] args) {
        try {
            
            //Ruta al archivo a leer
            String ruta = "C:\\Users\\User\\Downloads\\PIdG81 (7).txt";

            //Guardo en una lista de String el codigo a analizar para su uso en los errores
            List<String> codigoFuente = Files.readAllLines(Paths.get(ruta), StandardCharsets.ISO_8859_1);
            TS_Gestor gestorTs = new TS_Gestor("tabladesimbolos.txt");
            TablaDeSimbolos tabla = new TablaDeSimbolos(gestorTs);
            tabla.iniciar();
            tabla.crearTablaGlobal();

            Analizador_Lexico lexico = new Analizador_Lexico(ruta, gestorTs, codigoFuente);
            LexerConTexto aLex = new LexerConTexto(lexico, "tokens.txt");
            FileWriter parsetxt = new FileWriter("parse.txt");
            

            Analizador_Sint_Sem sintactico = new Analizador_Sint_Sem(aLex,codigoFuente, gestorTs);
            String parse = sintactico.iniciar();

            if (parse != null) parsetxt.write(parse);
            aLex.close();
            parsetxt.close();
            
            //Escritura y Errores en TS
            int res=gestorTs.write(Tabla.GLOBAL);
            if(res!=0){
                System.out.println("Error al intentar escribir la tabla global en el fichero");
            }
            res=gestorTs.destroy(Tabla.GLOBAL);
            if(res!=0){
                System.out.println("Error al intentar destruir la tabla global");
            }
            res=gestorTs.destroy(Tabla.PALRES);
            if(res!=0){
                System.out.println("Error al intentar destruir la tabla de palabras reservadas");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Err e) {
            System.out.println(e.getMessage());
        }
    }
}