package br.com.alura.curso.springboot.forum.controller;

import br.com.alura.curso.springboot.forum.DTO.AtualizaTopicoDTO;
import br.com.alura.curso.springboot.forum.DTO.DetalheTopicoDTO;
import br.com.alura.curso.springboot.forum.DTO.TopicoDTO;
import br.com.alura.curso.springboot.forum.form.TopicoForm;
import br.com.alura.curso.springboot.forum.model.Topico;
import br.com.alura.curso.springboot.forum.repository.CursoRepository;
import br.com.alura.curso.springboot.forum.repository.TopicoRepository;
import br.com.alura.curso.springboot.forum.repository.UsuarioRepository;
import br.com.alura.curso.springboot.forum.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
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
    @Autowired
    TokenService tokenService;
    @Autowired
    UsuarioRepository usuarioRepository;

    private static final Logger logger = LoggerFactory.getLogger(TopicosController.class);

    @GetMapping
    @Cacheable(cacheNames = "Topico",key = "#root.method.name")
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
    @CacheEvict(cacheNames = "Topico",allEntries = true)
    public ResponseEntity<TopicoDTO> cadastrar(@RequestHeader("Authorization") String token,@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriComponentsBuilder){
        Topico topico =topicoForm.converter(cursoRepository,token,tokenService,usuarioRepository);
        logger.debug("TopicoForm foi convertido com para Topico ");
        topicoRepository.save(topico);
        logger.debug("Topico "+topico.getTitulo()+" salvo em Banco");
        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }

    @GetMapping("{id}")
    @Cacheable(cacheNames = "Topico",key = "#id")
    public ResponseEntity<DetalheTopicoDTO> detalhar(@PathVariable Long id){
        Optional<Topico> topico = topicoRepository.findById(id);
        if(topico.isPresent()){
            logger.debug("Topico"+id+" encontrado na busca");
            return ResponseEntity.ok(new DetalheTopicoDTO(topico.get()));
        }
        logger.error("Topico"+ id+" informado não foi encontrado");
        return ResponseEntity.notFound().build();
    }

    @PutMapping("{id}")
    @Transactional
    @CachePut(cacheNames ="Topico",key = "#id")
    public ResponseEntity<AtualizaTopicoDTO> atualizar(@PathVariable Long id,@RequestBody AtualizaTopicoDTO atualizaTopicoDTO){
        Optional<Topico> topico = topicoRepository.findById(id);
        if(topico.isPresent()) {
            logger.debug("Topico com id "+id +" encontrado na busca");
         atualizaTopicoDTO.atualizarTopico(id,topicoRepository);
         logger.debug("Topico "+atualizaTopicoDTO.getId()+ " atualizado com sucesso");
        return ResponseEntity.ok(atualizaTopicoDTO);
        }
        logger.debug("Tópico de id "+id+" não encpntrado");
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    @Transactional
    @CacheEvict(cacheNames = "Topico",key = "#id")
    public ResponseEntity<DetalheTopicoDTO> remover(@PathVariable Long id){
        Optional<Topico> topico = topicoRepository.findById(id);
        if(topico.isPresent()) {
            logger.debug("Topico encontrado na busca");
            topicoRepository.deleteById(id);
            logger.debug("Topico "+id+ " removido com sucesso");
            return ResponseEntity.ok().build();
        }
        logger.debug("Tópico "+id+" não encpntrado");
        return ResponseEntity.notFound().build();
    }

}
