package br.com.alura.curso.springboot.forum.repository;

import br.com.alura.curso.springboot.forum.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    Page<Topico> findByCursoNome(String nomeCurso, Pageable pagina);

    @Query("SELECT t FROM topico t WHERE t.titulo = :nomeTopico")
    List<Topico> findByTitulo(@Param("nomeTopico") String titulo);
}
