package br.com.alura.curso.springboot.forum.controller;

import br.com.alura.curso.springboot.forum.DTO.CategoriaDTO;
import br.com.alura.curso.springboot.forum.form.CategoriaForm;
import br.com.alura.curso.springboot.forum.model.Categoria;
import br.com.alura.curso.springboot.forum.repository.CategoriaRepository;
import br.com.alura.curso.springboot.forum.util.CustomResponseEntity;
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
@RequestMapping("categoria")
public class CategoriaController extends BaseController<Categoria,Long, CategoriaForm, CategoriaDTO>{

    private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);
    private static final String CACHE_NAME_CATEGORIA = "Categoria";
    private static final String CACHE_KEY_CATEGORIA = "#id";

    @Autowired
    CategoriaRepository categoriaRepository;

    @Override
    @Transactional
    @CacheEvict(cacheNames = CACHE_NAME_CATEGORIA,allEntries = true)
    public ResponseEntity<CategoriaDTO> cadastrar(@RequestHeader("Authorization") String token,@RequestBody  @Valid CategoriaForm categoriaForm, UriComponentsBuilder uriComponentsBuilder) {
        Categoria categoria = categoriaForm.converter();
        logger.debug(CategoriaForm.class.getName()+" foi convertido com para Categoria ");
        categoriaRepository.save(categoria);
        logger.debug("Categoria "+categoria.getDescricao()+" salva em Banco");
        URI uri = uriComponentsBuilder.buildAndExpand("/categoria/{id}").toUri();
        return ResponseEntity.created(uri).body(new CategoriaDTO(categoria));

    }

    @Override
    @Cacheable(cacheNames = CACHE_NAME_CATEGORIA,key ="#root.method.name")
    public Page<CategoriaDTO> listar(String campo, Pageable page) {
        Page<Categoria> categoriasRetornadas = categoriaRepository.findAll(page);
        return categoriasRetornadas.map(CategoriaDTO::new);
    }

    @Override
    @Cacheable(cacheNames = CACHE_NAME_CATEGORIA,key = CACHE_KEY_CATEGORIA)
    public CustomResponseEntity<CategoriaDTO> detalhar(@PathVariable Long id) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        if(categoriaOptional.isPresent()){
            logger.debug("Categoria"+id+" encontrado na busca");
            return new CustomResponseEntity(new CategoriaDTO(categoriaOptional.get()), HttpStatus.OK);
        }
        logger.error("Categoria"+ id+" informado não foi encontrado");
        return new CustomResponseEntity(HttpStatus.NOT_FOUND);
    }

    @Override
    @Transactional
    @CachePut(cacheNames = CACHE_NAME_CATEGORIA,key = CACHE_KEY_CATEGORIA)
    public CustomResponseEntity<CategoriaDTO> atualizar(@PathVariable Long id, @RequestBody @Valid CategoriaDTO categoriaDTO) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        if(categoriaOptional.isPresent()){
            logger.debug("Categoria de id "+id+" encontrado na busca");
            categoriaOptional.get().setDescricao(categoriaDTO.getDescricao());
            return new CustomResponseEntity(new CategoriaDTO(categoriaOptional.get()),HttpStatus.OK);
        }
        logger.debug("Categoria de id "+id+" não encpntrado");
        return new  CustomResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = CACHE_NAME_CATEGORIA,key = CACHE_KEY_CATEGORIA)
    public CustomResponseEntity remover(@PathVariable Long id) {
        try {
             categoriaRepository.deleteById(id);
                logger.debug("Categoria com id  "+id+ " removida com sucesso");
             return new CustomResponseEntity(HttpStatus.OK);
        }catch (Exception e) {
            logger.debug("Categoria com id "+id+" não encpntrada");
            return new CustomResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
