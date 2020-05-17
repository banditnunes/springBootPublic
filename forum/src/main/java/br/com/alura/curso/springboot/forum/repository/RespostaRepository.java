package br.com.alura.curso.springboot.forum.repository;

import br.com.alura.curso.springboot.forum.model.Resposta;
import br.com.alura.curso.springboot.forum.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RespostaRepository extends JpaRepository<Resposta,Long> {
    @EntityGraph(value = "topicoResposta",type = EntityGraph.EntityGraphType.LOAD)
    Page<Resposta> findByTopico(Topico topico, Pageable page);

    @Override
    @EntityGraph(value = "topicoResposta",type = EntityGraph.EntityGraphType.LOAD)
    Optional<Resposta> findById(Long aLong);
}
