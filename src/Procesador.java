import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import Enumeraciones.*;
import Errores.*;
import tslib.TS_Gestor;
import tslib.TS_Gestor.Tabla;
import java.util.List;

public class Procesador {
    public static void main(String[] args) {
        try {
            
            //Ruta al archivo a leer
            String ruta = "C:\\Users\\User\\OneDrive\\Escritorio\\Pdl\\Pruebas\\C3.txt";

            //Guardo en una lista de String el codigo a analizar para su uso en los errores
            List<String> codigoFuente = Files.readAllLines(Paths.get(ruta));
            TS_Gestor gestorTs = new TS_Gestor("tabladesimbolos.txt");
            TablaDeSimbolos tabla = new TablaDeSimbolos(gestorTs);
            tabla.iniciar();

            Analizador_Lexico lexico = new Analizador_Lexico(ruta, gestorTs, codigoFuente);
            TokenTexto tkT = new TokenTexto(lexico, "tokens.txt");
            FileWriter parsetxt = new FileWriter("parse.txt");
            

            Analizador_Sintactico sintactico = new Analizador_Sintactico(tkT,codigoFuente);
            String parse = sintactico.iniciar();

            if (parse != null) parsetxt.write(parse);
            tkT.close();
            parsetxt.close();

            //IMPRESION DE MENSAJE ANTIGUO EN ARCHIVO TOKEN

            // Token token;
            // //Proceso tokens hasta que encuentre EOF o null (error)
            // do {
            //     token = lexico.iniciar().getLeft();
            //     if (token == null) {
            //         writer.write("Ha ocurrido un error generando este token");
            //     } else if (token.getTipo() == TipoToken.TYPE_CADENA) {
            //         writer.write("<" + token.getTipo().ordinal() + ", \"" + token.getLex() + "\">\n");
            //     } else if (token.getTipo() == TipoToken.TYPE_REAL) {
            //         writer.write("<" + token.getTipo().ordinal() + ", " + token.getNum() + ">\n");
            //     } else if (token.getAtributo() == null){
            //         writer.write("<" + token.getTipo().ordinal() + ", " + ">\n");
            //     } else {
            //         writer.write("<" + token.getTipo().ordinal() + ", " + token.getAtributo() + ">\n");
            //     }
            // } while (token != null && token.getTipo() != TipoToken.TYPE_EOF);
            
            //Errores en TS
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