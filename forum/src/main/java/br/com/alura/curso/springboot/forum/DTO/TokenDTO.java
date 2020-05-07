package br.com.alura.curso.springboot.forum.DTO;

import java.io.Serializable;

public class TokenDTO implements Serializable {
    private final String tipo;
    private final String token;


    public TokenDTO(String token, String tipo) {
        this.token = token;
        this.tipo = tipo;

    }

    public String getTipo() {
        return tipo;
    }

    public String getToken() {
        return token;
    }
}
