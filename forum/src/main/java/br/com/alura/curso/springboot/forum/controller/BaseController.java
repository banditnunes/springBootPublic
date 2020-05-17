package br.com.alura.curso.springboot.forum.controller;

import br.com.alura.curso.springboot.forum.DTO.DetalheTopicoDTO;
import br.com.alura.curso.springboot.forum.DTO.RespostaDTO;
import br.com.alura.curso.springboot.forum.form.RespostaForm;
import br.com.alura.curso.springboot.forum.util.CustomResponseEntity;
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


public abstract class BaseController<T , Long,TForm,TDTO>{

    @PostMapping
    @Transactional
    public abstract ResponseEntity<TDTO> cadastrar(String header, @RequestBody @Valid TForm tForm, UriComponentsBuilder uriComponentsBuilder);

    @GetMapping
    public abstract Page<TDTO> listar(@RequestParam(required = false) String campo, Pageable page);

    @GetMapping("{id}")
    public abstract CustomResponseEntity<TDTO> detalhar(@PathVariable Long id);

    @PutMapping("{id}")
    @Transactional
    public abstract CustomResponseEntity<TDTO> atualizar(@PathVariable Long id,@RequestBody TDTO tdto);

    @DeleteMapping("{id}")
    @Transactional
    public abstract CustomResponseEntity remover(@PathVariable Long id);

}
