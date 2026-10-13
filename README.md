# Procesador de Lenguajes

Analizador léxico, sintáctico y semántico en **Java**. Es la práctica de la asignatura *Procesadores de Lenguajes* de la Universidad Politécnica de Madrid.

## Qué hace

A partir de un fichero con código fuente, el procesador:

- **Analizador léxico:** reconoce los tokens con un autómata finito (tabla de transiciones) y rellena la tabla de símbolos.
- **Analizador sintáctico:** es descendente recursivo y valida el programa con una gramática LL(1). Genera el *parse* (la secuencia de reglas aplicadas).
- **Gestión de errores:** detecta errores léxicos (cadenas o comentarios sin cerrar, números o identificadores demasiado largos, caracteres no válidos), sintácticos (tokens inesperados) y semánticos (secuencia incorrecta)

El lenguaje admite variables tipadas (`let int x;`), `if`, `for`, `read`/`write`, funciones con parámetros y `return`, y operadores aritméticos, relacionales y lógicos.

## Estructura

```
src/
├── Procesador.java            # Punto de entrada
├── Analizador_Lexico.java     # Autómata del léxico
├── Analizador_Sintactico.java # Parser descendente recursivo
├── TablaDeSimbolos.java
├── Token.java / TokenTexto.java
├── Enumeraciones/             # Tipos de token, estados, caracteres
├── Errores/                   # Un tipo de excepción por cada error
└── gramaticaASt.txt           # Gramática del lenguaje
lib/                           # Librerías de la asignatura (tabla de símbolos)
ArbolC*.html                   # Ejemplos de árboles sintácticos generados
```

## Cómo ejecutarlo

1. Abre el proyecto en VS Code con la extensión de Java (las librerías de `lib/` se cargan solas).
2. En `Procesador.java`, cambia la variable `ruta` para que apunte a tu fichero de código.
3. Ejecuta `Procesador`. Se generan tres ficheros:
   - `tokens.txt`: lista de tokens
   - `parse.txt`: parse del programa (se puede visualizar como árbol con la herramienta VASt de la UPM)
   - `tabladesimbolos.txt`: contenido de la tabla de símbolos

## Autores

Jia Jie Ye - L0stFr0mLighT
Yurong Cheng - Yurong0q
Yun Chen - YuuAhhm

Trabajo en equipo de:
- **Ye** – [GitHub](https://github.com/TU-USUARIO)
- **Nombre del compañero** – [GitHub](https://github.com/SU-USUARIO)
