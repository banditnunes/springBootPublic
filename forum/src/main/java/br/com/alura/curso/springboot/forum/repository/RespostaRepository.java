package br.com.alura.curso.springboot.forum.repository;

import br.com.alura.curso.springboot.forum.model.Resposta;
import br.com.alura.curso.springboot.forum.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RespostaRepository extends JpaRepository<Resposta,Long> {

    Page<Resposta> findByTopico(Topico topico, Pageable page);
}
