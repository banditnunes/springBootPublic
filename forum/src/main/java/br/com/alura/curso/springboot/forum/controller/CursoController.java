package br.com.alura.curso.springboot.forum.controller;

import br.com.alura.curso.springboot.forum.DTO.CursoDTO;
import br.com.alura.curso.springboot.forum.form.CursoForm;
import br.com.alura.curso.springboot.forum.model.Curso;
import br.com.alura.curso.springboot.forum.repository.CategoriaRepository;
import br.com.alura.curso.springboot.forum.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("cursos")
public class CursoController extends BaseController<Curso, Long, CursoForm, CursoDTO> {

    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    CategoriaRepository categoriaRepository;

    @Override
    @Transactional
    public ResponseEntity<CursoDTO> cadastrar(@RequestBody @Valid CursoForm cursoForm, UriComponentsBuilder uriComponentsBuilder) {
        Curso curso = cursoForm.converter(categoriaRepository);
        if (curso != null) {
            cursoRepository.save(curso);
            URI uri = uriComponentsBuilder.path("/curso/{id}").buildAndExpand(curso.getId()).toUri();
            return ResponseEntity.created(uri).body(new CursoDTO(curso));
        } else {
          return  ResponseEntity.notFound().build();
        }
    }

    @Override
    public Page<CursoDTO> listar(String campo, Pageable page) {
        Page cursos;
        if (campo == null || campo.isEmpty()) {
            return CursoDTO.transformaDTO(cursoRepository.findAll(page));
        } else {
            return CursoDTO.transformaDTO((Page<Curso>) cursoRepository.findByNome(campo));
        }

    }

    @Override
    public ResponseEntity<CursoDTO> detalhar(@PathVariable  Long id) {
        Optional<Curso> curso = cursoRepository.findById(id);
        if (curso.isPresent()) {
            return ResponseEntity.ok(new CursoDTO(curso.get()));
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    @Transactional
    public ResponseEntity<CursoDTO> atualizar(@PathVariable  Long id,@RequestBody @Valid CursoDTO cursoDTO) {
        return cursoDTO.atualizarCurso(cursoRepository, id, categoriaRepository);
    }


    @Override
    @Transactional
    public ResponseEntity remover(@PathVariable  Long id) {
        if (cursoRepository.findById(id).isPresent()) {
            cursoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
