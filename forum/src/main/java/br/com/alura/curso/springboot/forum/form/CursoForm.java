package br.com.alura.curso.springboot.forum.form;

import br.com.alura.curso.springboot.forum.model.Categoria;
import br.com.alura.curso.springboot.forum.model.Curso;
import br.com.alura.curso.springboot.forum.repository.CategoriaRepository;
import br.com.alura.curso.springboot.forum.repository.CursoRepository;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;


public class CursoForm extends BaseForm<Curso, CursoRepository>{

    private static final Logger logger = LoggerFactory.getLogger(CursoForm.class);
    private String descricao;
    private String categoria;



    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }


    public Curso converter(CategoriaRepository categoriaRepository){
        Optional<Categoria> categoriaOptional = categoriaRepository.findByDescricao(this.categoria).stream().findFirst();
        if(categoriaOptional.isPresent()){
            logger.debug("Categoria "+categoriaOptional.get().getId()+" retornada");
            Categoria categoria = categoriaOptional.get();
            return new Curso(this.descricao,categoria);
        }
            logger.error("Categoria "+ this.categoria + "não foi encontrada");
        throw new EntityNotFoundException();
    }
}
