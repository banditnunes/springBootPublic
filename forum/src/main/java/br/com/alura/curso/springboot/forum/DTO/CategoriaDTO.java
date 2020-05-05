package br.com.alura.curso.springboot.forum.DTO;

import br.com.alura.curso.springboot.forum.model.Categoria;

public class CategoriaDTO extends BaseDTO{

    private String descricao;

    public CategoriaDTO(){}

    public CategoriaDTO(Categoria categoria) {
        this.descricao = categoria.getDescricao();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
