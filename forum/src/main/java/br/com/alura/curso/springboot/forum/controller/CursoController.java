package br.com.alura.curso.springboot.forum.controller;

import br.com.alura.curso.springboot.forum.DTO.CursoDTO;
import br.com.alura.curso.springboot.forum.util.CustomResponseEntity;
import br.com.alura.curso.springboot.forum.form.CursoForm;
import br.com.alura.curso.springboot.forum.model.Curso;
import br.com.alura.curso.springboot.forum.repository.CategoriaRepository;
import br.com.alura.curso.springboot.forum.repository.CursoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    private static final Logger logger = LoggerFactory.getLogger(CursoController.class);
    private static final String CACHE_NOME_CURSO = "Curso";
    private static final String CACHE_KEY_CURSO = "#id";

    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    CategoriaRepository categoriaRepository;

    @Override
    @Transactional
    @CacheEvict(cacheNames = CACHE_NOME_CURSO,allEntries = true)
    public ResponseEntity<CursoDTO> cadastrar(@RequestHeader("Authorization") String token,@RequestBody @Valid CursoForm cursoForm, UriComponentsBuilder uriComponentsBuilder) {
        Curso curso = cursoForm.converter(categoriaRepository);
        logger.debug(CursoForm.class.getName()+" foi convertido com para Curso ");
        if (curso != null) {
            cursoRepository.save(curso);
            logger.debug("Curso "+curso.getNome()+" salva em Banco");
            URI uri = uriComponentsBuilder.path("/curso/{id}").buildAndExpand(curso.getId()).toUri();
            return  ResponseEntity.created(uri).body(new CursoDTO(curso));
        } else {
          return  ResponseEntity.notFound().build();
        }
    }

    @Override
    @Cacheable(cacheNames = CACHE_NOME_CURSO,key = "#root.method.name")
    public Page<CursoDTO> listar(@PathVariable(required = false) String campo, Pageable page) {
        Page cursos;
        if (campo == null || campo.isEmpty()) {
            return CursoDTO.transformaDTO(cursoRepository.findAll(page));
        } else {
            return CursoDTO.transformaDTO((Page<Curso>) cursoRepository.findByNome(campo));
        }

    }

    @Override
    @Cacheable(cacheNames = CACHE_NOME_CURSO,key = CACHE_KEY_CURSO)
    public CustomResponseEntity<CursoDTO> detalhar(@PathVariable  Long id) {
        Optional<Curso> curso = cursoRepository.findById(id);
        if (curso.isPresent()) {
            logger.debug("Curso "+id+" encontrado na busca");
            return new CustomResponseEntity(new CursoDTO(curso.get()),HttpStatus.OK);
        }
        System.out.println("Curso"+ id+" informado não foi encontrado");
        logger.error("Curso"+ id+" informado não foi encontrado");
        return new CustomResponseEntity(HttpStatus.NOT_FOUND);
    }

    @Override
    @Transactional
    @CachePut(cacheNames = CACHE_NOME_CURSO,key = CACHE_KEY_CURSO)
    public CustomResponseEntity<CursoDTO> atualizar(@PathVariable  Long id, @RequestBody @Valid CursoDTO cursoDTO) {
        return cursoDTO.atualizarCurso(cursoRepository, id, categoriaRepository);
    }


    @Override
    @Transactional
    @CacheEvict(cacheNames = CACHE_NOME_CURSO,key = CACHE_KEY_CURSO)
    public CustomResponseEntity remover(@PathVariable  Long id) {
        if (cursoRepository.findById(id).isPresent()) {
            cursoRepository.deleteById(id);
            logger.debug("Curso com id  "+id+ " removido com sucesso");
            return new CustomResponseEntity(HttpStatus.OK);
        }
        logger.debug("Curso com id "+id+" não encontrado");
        return new CustomResponseEntity(HttpStatus.NOT_FOUND);
    }
}
