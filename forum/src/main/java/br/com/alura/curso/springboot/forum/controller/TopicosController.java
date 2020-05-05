package br.com.alura.curso.springboot.forum.controller;

import br.com.alura.curso.springboot.forum.DTO.AtualizaTopicoDTO;
import br.com.alura.curso.springboot.forum.DTO.DetalheTopicoDTO;
import br.com.alura.curso.springboot.forum.DTO.TopicoDTO;
import br.com.alura.curso.springboot.forum.form.TopicoForm;
import br.com.alura.curso.springboot.forum.model.Topico;
import br.com.alura.curso.springboot.forum.repository.CursoRepository;
import br.com.alura.curso.springboot.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("topicos")
public class TopicosController {

    @Autowired
    TopicoRepository topicoRepository;
    @Autowired
    CursoRepository cursoRepository;

    @GetMapping
    @Cacheable(value = "listaTopicos")
    public Page<TopicoDTO> listarTopicosPorCurso(@RequestParam(required = false) String nomeCurso,
                                                 @PageableDefault(page=0,size = 10,direction = Sort.Direction.ASC)
                                                         Pageable page){
        if(nomeCurso==null) {
            return TopicoDTO.transformaTopicoDTO(topicoRepository.findAll(page));
        }else {
            return  TopicoDTO.transformaTopicoDTO(topicoRepository.findByCursoNome(nomeCurso,page));
        }

    }
    @PostMapping
    @Transactional
    @CacheEvict(value = "listaTopicos",allEntries = true)
    public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriComponentsBuilder){

        Topico topico =topicoForm.converter(cursoRepository);
         topicoRepository.save(topico);
        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }

    @GetMapping("{id}")
    public ResponseEntity<DetalheTopicoDTO> detalhar(@PathVariable Long id){
        Optional<Topico> topico = topicoRepository.findById(id);
        if(topico.isPresent()){
            return ResponseEntity.ok(new DetalheTopicoDTO(topico.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("{id}")
    @Transactional
    @CacheEvict(value = "listaTopicos",allEntries = true)
    public ResponseEntity<AtualizaTopicoDTO> atualizar(@PathVariable Long id,@RequestBody AtualizaTopicoDTO atualizaTopicoDTO){
        Optional<Topico> topico = topicoRepository.findById(id);
        if(topico.isPresent()) {
         atualizaTopicoDTO.atualizarTopico(id,topicoRepository);
        return ResponseEntity.ok(atualizaTopicoDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    @Transactional
    @CacheEvict(value = "listaTopicos",allEntries = true)
    public ResponseEntity remover(@PathVariable Long id){
        Optional<Topico> topico = topicoRepository.findById(id);
        if(topico.isPresent()) {
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
