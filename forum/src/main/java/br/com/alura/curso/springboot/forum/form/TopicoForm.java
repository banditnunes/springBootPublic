package br.com.alura.curso.springboot.forum.form;

import br.com.alura.curso.springboot.forum.model.Curso;
import br.com.alura.curso.springboot.forum.model.Topico;
import br.com.alura.curso.springboot.forum.repository.CursoRepository;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class TopicoForm {

    @NotNull @Length(min = 2,max = 120)
    private String titulo;
    @NotNull @Length(min = 2,max = 1200)
    private String mensagem;
    @NotNull @Length(min = 2,max = 12)
    private String nomeCurso;

    public Topico converter(CursoRepository cursoRepository){
        Curso curso = cursoRepository.findByNome(getNomeCurso());
        return new Topico(titulo,mensagem,curso);
    }



    public String getTitulo() {
        return titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }
}
