package br.com.alura.curso.springboot.forum.repository;

import br.com.alura.curso.springboot.forum.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria,Long> {

    public List<Categoria> findByDescricao(String categoria);
}
