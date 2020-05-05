package br.com.alura.curso.springboot.forum.form;

import br.com.alura.curso.springboot.forum.model.Categoria;
import br.com.alura.curso.springboot.forum.model.Curso;
import br.com.alura.curso.springboot.forum.repository.CategoriaRepository;
import br.com.alura.curso.springboot.forum.repository.CursoRepository;

import java.util.Optional;

public class CursoForm extends BaseForm<Curso, CursoRepository>{
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


    public Curso converter(CategoriaRepository categoriaRepository) {
        System.out.println(this.categoria);
        Optional<Categoria> categoriaOptional = categoriaRepository.findByDescricao(this.categoria).stream().findFirst();
        if(categoriaOptional.isPresent()){
            System.out.println(categoriaOptional.get().getId());
            Categoria categoria = categoriaOptional.get();
            return new Curso(this.descricao,categoria);
        }
            //Deveria retornar erro informando sobre a Categoria não existente
         return null;
    }
}
