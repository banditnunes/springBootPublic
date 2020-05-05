package br.com.alura.curso.springboot.forum.controller;

import br.com.alura.curso.springboot.forum.DTO.CategoriaDTO;
import br.com.alura.curso.springboot.forum.form.CategoriaForm;
import br.com.alura.curso.springboot.forum.model.Categoria;
import br.com.alura.curso.springboot.forum.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("categoria")
public class CategoriaController extends BaseController<Categoria,Long, CategoriaForm, CategoriaDTO>{

    @Autowired
    CategoriaRepository categoriaRepository;

    @Override
    @Transactional
    public ResponseEntity<CategoriaDTO> cadastrar(@RequestBody  @Valid CategoriaForm categoriaForm, UriComponentsBuilder uriComponentsBuilder) {
        Categoria categoria = categoriaForm.converter();
        categoriaRepository.save(categoria);
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
            return ResponseEntity.ok(new CategoriaDTO(categoriaOptional.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    @Transactional
    public ResponseEntity<CategoriaDTO> atualizar(@PathVariable Long id,@RequestBody @Valid CategoriaDTO categoriaDTO) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        if(categoriaOptional.isPresent()){
            categoriaOptional.get().setDescricao(categoriaDTO.getDescricao());
            return  ResponseEntity.ok(new CategoriaDTO(categoriaOptional.get()));
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    @Transactional
    public ResponseEntity remover(@PathVariable Long id) {
        try {
             categoriaRepository.deleteById(id);
             return ResponseEntity.ok().build();
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
