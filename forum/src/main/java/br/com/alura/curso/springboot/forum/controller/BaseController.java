package br.com.alura.curso.springboot.forum.controller;

import br.com.alura.curso.springboot.forum.DTO.DetalheTopicoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
public abstract class BaseController<T , Long,TForm,TDTO>{

    @PostMapping
    @Transactional
    public abstract ResponseEntity<TDTO> cadastrar(@RequestBody @Valid TForm tForm,UriComponentsBuilder uriComponentsBuilder);

    @GetMapping
    public abstract Page<TDTO> listar(@RequestParam(required = false) String campo, Pageable page);

    @GetMapping("{id}")
    public abstract ResponseEntity<TDTO> detalhar(@PathVariable Long id);

    @PutMapping("{id}")
    @Transactional
    public abstract ResponseEntity<TDTO> atualizar(@PathVariable Long id,@RequestBody TDTO tdto);

    @DeleteMapping("{id}")
    @Transactional
    public abstract ResponseEntity remover(@PathVariable Long id);

}
