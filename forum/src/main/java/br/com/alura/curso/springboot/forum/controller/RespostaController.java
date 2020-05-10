package br.com.alura.curso.springboot.forum.controller;

import br.com.alura.curso.springboot.forum.DTO.RespostaDTO;
import br.com.alura.curso.springboot.forum.form.RespostaForm;
import br.com.alura.curso.springboot.forum.model.Resposta;
import br.com.alura.curso.springboot.forum.model.Topico;
import br.com.alura.curso.springboot.forum.repository.RespostaRepository;
import br.com.alura.curso.springboot.forum.repository.TopicoRepository;
import br.com.alura.curso.springboot.forum.repository.UsuarioRepository;
import br.com.alura.curso.springboot.forum.service.TokenService;
import br.com.alura.curso.springboot.forum.util.RecuperaToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
@RequestMapping("resposta")
public class RespostaController  extends  BaseController<Resposta,Long, RespostaForm, RespostaDTO>{
    private static final Logger logger = LoggerFactory.getLogger(RespostaController.class);
    private static final String CACHE_NOME_RESPOSTA = "Resposta";
    private static final String CACHE_KEY_RESPOSTA = "#id";


    @Autowired
    RespostaRepository respostaRepository;

    @Autowired
    TopicoRepository topicoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    TokenService tokenService;


    @Override
    @Transactional
    @CacheEvict(cacheNames = CACHE_NOME_RESPOSTA,key = CACHE_KEY_RESPOSTA)
    public ResponseEntity<RespostaDTO> cadastrar(@RequestHeader(name = "Authorization") String token, @RequestBody  @Valid RespostaForm respostaForm, UriComponentsBuilder uriComponentsBuilder) {
        try {
            logger.info("Conversão inicializada...");
            Resposta resposta = respostaForm.converter(topicoRepository,tokenService, RecuperaToken.retornaToken(token),usuarioRepository);
            logger.info("Conversão realizada");
            respostaRepository.save(resposta);
            logger.info("Transação realizada");
            URI uri = uriComponentsBuilder.path("/curso/{id}").buildAndExpand(resposta.getId()).toUri();
         return ResponseEntity.created(uri).build().ok(new RespostaDTO(resposta));
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @Override
    @Cacheable(cacheNames = CACHE_NOME_RESPOSTA,key = "#root.method.name")
    public Page<RespostaDTO> listar(@PathVariable String topico, Pageable page) {
        Topico topicoRetornado = topicoRetornado(topico);
        Page<Resposta> respostasPorTopico = respostaRepository.findByTopico(topicoRetornado, page);
        return  respostasPorTopico.map(RespostaDTO::new);
    }


    @Override
    @Transactional
    @Cacheable(cacheNames = CACHE_NOME_RESPOSTA,key = CACHE_KEY_RESPOSTA)
    public ResponseEntity<RespostaDTO> detalhar(@PathVariable Long id) {
        Optional<Resposta> respostaRetornada = respostaRepository.findById(id);
        if(respostaRetornada.isPresent()){
            logger.debug("Resposta "+ respostaRetornada.get().getMensagem()+" retornada");
            return ResponseEntity.ok(new RespostaDTO(respostaRetornada.get()));
        }
        logger.error("Resposta com o "+ id+ "não foi encontrado");
        return ResponseEntity.badRequest().build();
    }

    @Override
    @Transactional
    @CachePut(cacheNames = CACHE_NOME_RESPOSTA,key = CACHE_KEY_RESPOSTA)
    public ResponseEntity<RespostaDTO> atualizar(@PathVariable Long id,@RequestBody RespostaDTO respostaDTO) {
        try {
            Resposta resposta = respostaDTO.converter(respostaRepository, id, topicoRepository);
            logger.debug("Resposta "+resposta.getMensagem()+" convertida");
             return ResponseEntity.ok(new RespostaDTO(resposta));
        }catch (Exception e) {
            logger.error("Resposta de id "+id+ " não foi atualizada com sucesso");
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = CACHE_NOME_RESPOSTA,key = CACHE_KEY_RESPOSTA)
    public ResponseEntity remover(@PathVariable  Long id) {
        try {
            respostaRepository.deleteById(id);
            logger.debug("Resposta com id  "+id+ " removida com sucesso");
           return  ResponseEntity.ok().build();
        }catch (Exception e) {
            logger.debug("Resposta com id "+id+" não encontrada");
            return ResponseEntity.badRequest().build();
        }
    }

    private Topico topicoRetornado(String topico) {
        Optional<Topico> optionalTopico = topicoRepository.findByTitulo(topico).stream().findFirst();
        if(optionalTopico.isPresent()) {
            logger.debug("Topico  " + topico + " retornado com sucesso");
            return optionalTopico.get();
        }
        else {
            logger.error("Topico " + topico + " não foi encontrado");
            return null;
        }
    }
}
