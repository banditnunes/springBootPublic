package br.com.alura.curso.springboot.forum.form;

import br.com.alura.curso.springboot.forum.model.Curso;
import br.com.alura.curso.springboot.forum.model.Topico;
import br.com.alura.curso.springboot.forum.repository.CursoRepository;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

public class TopicoForm {

    private static Logger logger = LoggerFactory.getLogger("TopicoForm");
    @NotNull @Length(min = 2,max = 120)
    private String titulo;
    @NotNull @Length(min = 2,max = 1200)
    private String mensagem;
    @NotNull @Length(min = 2,max = 12)
    private String nomeCurso;

    public Topico converter(CursoRepository cursoRepository){
        logger.debug("Iniciando Busca pelo Curso "+getNomeCurso());
        Curso curso = cursoRepository.findByNome(getNomeCurso());
        logger.debug("Curso retornado: "+curso.getNome());
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
