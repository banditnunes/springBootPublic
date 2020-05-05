package br.com.alura.curso.springboot.forum.DTO;

import br.com.alura.curso.springboot.forum.model.Topico;
import br.com.alura.curso.springboot.forum.repository.TopicoRepository;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class AtualizaTopicoDTO {
    @NotNull
    @Length(min = 2,max = 120)
    private String titulo;
    @NotNull @Length(min = 2,max = 1200)
    private String mensagem;

    public void atualizarTopico(Long id, TopicoRepository repository){
        Topico topico = repository.getOne(id);
        topico.setTitulo(this.titulo);
        topico.setMensagem(this.mensagem);

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
