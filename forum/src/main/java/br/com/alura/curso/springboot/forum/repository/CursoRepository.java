package br.com.alura.curso.springboot.forum.repository;

import br.com.alura.curso.springboot.forum.model.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CursoRepository extends JpaRepository<Curso,Long> {
    public Curso findByNome(String nome);
}
