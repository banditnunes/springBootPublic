package br.com.alura.curso.springboot.forum.repository;

import br.com.alura.curso.springboot.forum.model.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.FetchType;
import java.util.List;


public interface CursoRepository extends JpaRepository<Curso,Long> {
    @EntityGraph(value = "cursoCategoria",type = EntityGraph.EntityGraphType.LOAD)
    public Curso findByNome(String nome);

    @Override
    @EntityGraph(value = "cursoCategoria",type = EntityGraph.EntityGraphType.LOAD)
    Page<Curso> findAll(Pageable page);
}
