package br.com.alura.curso.springboot.forum.DTO;

import br.com.alura.curso.springboot.forum.model.Resposta;

import java.io.Serializable;

public class RespostaDTO implements Serializable {

    private Long id;
    private String mensagem;
    private String topico;
    private Boolean resolvido;

    public RespostaDTO(){}

    public RespostaDTO(Resposta respostaSalva) {
            this.id = respostaSalva.getId();
            this.mensagem=respostaSalva.getMensagem();
            this.topico=respostaSalva.getTopico().getTitulo();
            this.resolvido=respostaSalva.getSolucao();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getTopico() {
        return topico;
    }

    public void setTopico(String topico) {
        this.topico = topico;
    }

    public Boolean getResolvido() {
        return resolvido;
    }

    public void setResolvido(Boolean resolvido) {
        this.resolvido = resolvido;
    }
}
