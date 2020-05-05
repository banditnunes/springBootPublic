package br.com.alura.curso.springboot.forum.DTO;

import br.com.alura.curso.springboot.forum.model.StatusTopico;
import br.com.alura.curso.springboot.forum.model.Topico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DetalheTopicoDTO {

    private Long id;
    private String titulo;
    private  StatusTopico status;
    //private String autor;
    private String mensagem;
    private LocalDateTime dtCriacao;

    private List<RespostaDetalheDTO> respostasDTO ;

    public DetalheTopicoDTO(Topico topico) {
        this.id = topico.getId();
        this.titulo = topico.getTitulo();
        //this.autor = topico.getAutor().getNome();
        this.dtCriacao = topico.getDataCriacao();
        this.mensagem = topico.getMensagem();
        this.respostasDTO = new ArrayList<>();
        this.respostasDTO = topico.getRespostas().stream().map(RespostaDetalheDTO::new).collect(Collectors.toList());
        this.status = topico.getStatus();

    }

    public DetalheTopicoDTO() {

    }

    public String getTitulo() {
        return titulo;
    }

    //public String getAutor() {
      //  return autor;
   // }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getDtCriacao() {
        return dtCriacao;
    }
}
