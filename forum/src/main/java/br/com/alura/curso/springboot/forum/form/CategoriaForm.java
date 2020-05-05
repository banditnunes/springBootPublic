package br.com.alura.curso.springboot.forum.form;

import br.com.alura.curso.springboot.forum.model.Categoria;
import br.com.alura.curso.springboot.forum.repository.CategoriaRepository;

public class CategoriaForm extends BaseForm<Categoria, CategoriaRepository>{

    private String descricao;

    public Categoria converter() {
        return new Categoria(descricao);
    }

    public String getDescricao() {
        return descricao;
    }
}
