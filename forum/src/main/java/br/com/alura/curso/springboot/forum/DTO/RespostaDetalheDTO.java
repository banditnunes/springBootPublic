package br.com.alura.curso.springboot.forum.DTO;

import br.com.alura.curso.springboot.forum.model.Resposta;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RespostaDetalheDTO implements Serializable {

    private  String mensagem;
    private String autor;
    private  LocalDateTime dataCriacao;

    public RespostaDetalheDTO(){}

    public RespostaDetalheDTO(Resposta resposta) {
        this.mensagem = resposta.getMensagem();
        this.autor = resposta.getAutor().getNome();
        this.dataCriacao = resposta.getDataCriacao();
    }
}
