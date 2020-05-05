package br.com.alura.curso.springboot.forum.DTO;

import br.com.alura.curso.springboot.forum.model.Categoria;
import br.com.alura.curso.springboot.forum.model.Curso;
import br.com.alura.curso.springboot.forum.repository.CategoriaRepository;
import br.com.alura.curso.springboot.forum.repository.CursoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public class CursoDTO extends BaseDTO implements Serializable {

    private Long id;
    private String nome;
    private String categoria;

    public CursoDTO() {

    }

    public static Page<CursoDTO> transformaDTO(Page<Curso> listaCursos) {
        return listaCursos.map(CursoDTO::new);
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }


    public CursoDTO(Curso curso) {
        this.id = curso.getId();
        this.nome = curso.getNome();
        this.categoria = curso.getCategoria().getDescricao ();
    }

    public ResponseEntity<CursoDTO> atualizarCurso(CursoRepository cursoRepository, Long id, CategoriaRepository categoriaRepository) {
        Optional<Curso> curso = cursoRepository.findById(id);
        if(curso.isPresent()) {
            curso.get().setNome(getNome());

            Optional<Categoria> categoriaRetornada = categoriaRepository.findByDescricao(getCategoria()).stream().findFirst();
            categoriaRetornada.ifPresent(value -> curso.get().setCategoria(value));

            setId(curso.get().getId());
            return ResponseEntity.ok(this);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    private void setId(Long id) {
        this.id=id;
    }
}
