package br.com.alura.curso.springboot.forum.controller;

import br.com.alura.curso.springboot.forum.DTO.CategoriaDTO;
import br.com.alura.curso.springboot.forum.form.CategoriaForm;
import br.com.alura.curso.springboot.forum.model.Categoria;
import br.com.alura.curso.springboot.forum.repository.CategoriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("categoria")
public class CategoriaController extends BaseController<Categoria,Long, CategoriaForm, CategoriaDTO>{

    private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);
    @Autowired
    CategoriaRepository categoriaRepository;

    @Override
    @Transactional
    public ResponseEntity<CategoriaDTO> cadastrar(@RequestHeader String header,@RequestBody  @Valid CategoriaForm categoriaForm, UriComponentsBuilder uriComponentsBuilder) {
        Categoria categoria = categoriaForm.converter();
        logger.debug(CategoriaForm.class.getName()+" foi convertido com para Categoria ");
        categoriaRepository.save(categoria);
        logger.debug("Categoria "+categoria.getDescricao()+" salva em Banco");
        URI uri = uriComponentsBuilder.buildAndExpand("/categoria/{id}").toUri();
        return ResponseEntity.created(uri).body(new CategoriaDTO(categoria));

    }

    @Override
    public Page<CategoriaDTO> listar(String campo, Pageable page) {
        Page<Categoria> categoriasRetornadas = categoriaRepository.findAll(page);
        return categoriasRetornadas.map(CategoriaDTO::new);
    }

    @Override
    public ResponseEntity<CategoriaDTO> detalhar(@PathVariable Long id) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        if(categoriaOptional.isPresent()){
            logger.debug("Categoria"+id+" encontrado na busca");
            return ResponseEntity.ok(new CategoriaDTO(categoriaOptional.get()));
        }
        logger.error("Categoria"+ id+" informado não foi encontrado");
        return ResponseEntity.notFound().build();
    }

    @Override
    @Transactional
    public ResponseEntity<CategoriaDTO> atualizar(@PathVariable Long id,@RequestBody @Valid CategoriaDTO categoriaDTO) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        if(categoriaOptional.isPresent()){
            logger.debug("Categoria de id "+id+" encontrado na busca");
            categoriaOptional.get().setDescricao(categoriaDTO.getDescricao());
            return  ResponseEntity.ok(new CategoriaDTO(categoriaOptional.get()));
        }
        logger.debug("Categoria de id "+id+" não encpntrado");
        return ResponseEntity.badRequest().build();
    }

    @Override
    @Transactional
    public ResponseEntity remover(@PathVariable Long id) {
        try {
             categoriaRepository.deleteById(id);
                logger.debug("Categoria com id  "+id+ " removida com sucesso");
             return ResponseEntity.ok().build();
        }catch (Exception e) {
            logger.debug("Categoria com id "+id+" não encpntrada");
            return ResponseEntity.notFound().build();
        }
    }
}
