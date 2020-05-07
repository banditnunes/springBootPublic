package br.com.alura.curso.springboot.forum.util;

public class RecuperaToken {
    public static String retornaToken(String tokenCompleto){
        int caracteres = tokenCompleto.length();
        return tokenCompleto.substring(7,caracteres);
    }
}
