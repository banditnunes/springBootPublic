package br.com.alura.curso.springboot.forum.DTO;

import br.com.alura.curso.springboot.forum.model.Resposta;

import java.time.LocalDateTime;

public class RespostaDetalheDTO {

    private final String mensagem;
    private final String autor;
    private final LocalDateTime dataCriacao;

    public RespostaDetalheDTO(Resposta resposta) {
        this.mensagem = resposta.getMensagem();
        this.autor = resposta.getAutor().getNome();
        this.dataCriacao = resposta.getDataCriacao();
    }
}
