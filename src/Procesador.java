import java.io.FileWriter;
import java.io.IOException;

import tslib.TS_Gestor;
import tslib.TS_Gestor.Tabla;

public class Procesador {
    public static void main(String[] args) {
        try (FileWriter writer = new FileWriter("tokens.txt")) {
            
            TS_Gestor gestorTs = new TS_Gestor("tabladesimbolos.txt");

            TablaDeSimbolos tabla = new TablaDeSimbolos(gestorTs);
            tabla.iniciar();

            Analizador_Lexico lexico = new Analizador_Lexico("C:\\Users\\User\\Downloads\\IC3.txt", gestorTs);

            Token token;
            //Proceso tokens hasta que encuentre EOF o null (error)
            do {
                token = lexico.iniciar();
                if (token == null) {
                    writer.write("Ha ocurrido un error generando este token");
                } else if (token.getTipo() == TipoToken.TYPE_CADENA) {
                    writer.write("<" + token.getTipo().ordinal() + ", \"" + token.getLex() + "\">\n");
                } else if (token.getTipo() == TipoToken.TYPE_REAL) {
                    writer.write("<" + token.getTipo().ordinal() + ", " + token.getNum() + ">\n");
                } else if (token.getAtributo() == null){
                    writer.write("<" + token.getTipo().ordinal() + ", " + ">\n");
                } else {
                    writer.write("<" + token.getTipo().ordinal() + ", " + token.getAtributo() + ">\n");
                }
            } while (token != null && token.getTipo() != TipoToken.TYPE_EOF);
            
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