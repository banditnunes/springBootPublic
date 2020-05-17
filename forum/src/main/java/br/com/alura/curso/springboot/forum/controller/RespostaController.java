package br.com.alura.curso.springboot.forum.controller;

import br.com.alura.curso.springboot.forum.DTO.RespostaDTO;
import br.com.alura.curso.springboot.forum.form.RespostaForm;
import br.com.alura.curso.springboot.forum.model.Resposta;
import br.com.alura.curso.springboot.forum.model.Topico;
import br.com.alura.curso.springboot.forum.repository.RespostaRepository;
import br.com.alura.curso.springboot.forum.repository.TopicoRepository;
import br.com.alura.curso.springboot.forum.repository.UsuarioRepository;
import br.com.alura.curso.springboot.forum.service.TokenService;
import br.com.alura.curso.springboot.forum.util.CustomResponseEntity;
import br.com.alura.curso.springboot.forum.util.RecuperaToken;
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
import org.springframework.http.HttpStatus;
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
    @CacheEvict(cacheNames = CACHE_NOME_RESPOSTA,allEntries = true)
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
    public Page<RespostaDTO> listar(@RequestParam(required = false) String nomeTopico,
                                    @PageableDefault(page=0,size = 10,direction = Sort.Direction.ASC) Pageable page) {
        Topico topicoRetornado = topicoRetornado(nomeTopico);
        Page<Resposta> respostasPorTopico = respostaRepository.findByTopico(topicoRetornado, page);
        return  respostasPorTopico.map(RespostaDTO::new);
    }


    @Override
    @Transactional
    @Cacheable(cacheNames = CACHE_NOME_RESPOSTA,key = CACHE_KEY_RESPOSTA)
    public CustomResponseEntity<RespostaDTO> detalhar(@PathVariable Long id) {
        Optional<Resposta> respostaRetornada = respostaRepository.findById(id);
        if(respostaRetornada.isPresent()){
            logger.debug("Resposta "+ respostaRetornada.get().getMensagem()+" retornada");
            return new CustomResponseEntity(new RespostaDTO(respostaRetornada.get()), HttpStatus.OK);
        }
        logger.error("Resposta com o "+ id+ "não foi encontrado");
        return new CustomResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional
    @CachePut(cacheNames = CACHE_NOME_RESPOSTA,key = CACHE_KEY_RESPOSTA)
    public CustomResponseEntity<RespostaDTO> atualizar(@PathVariable Long id,@RequestBody RespostaDTO respostaDTO) {
        try {
            Resposta resposta = respostaDTO.converter(respostaRepository, id, topicoRepository);
            logger.debug("Resposta "+resposta.getMensagem()+" convertida");
             return new CustomResponseEntity(new RespostaDTO(resposta),HttpStatus.OK);
        }catch (Exception e) {
            logger.error("Resposta de id "+id+ " não foi atualizada com sucesso");
            return new CustomResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = CACHE_NOME_RESPOSTA,key = CACHE_KEY_RESPOSTA)
    public CustomResponseEntity remover(@PathVariable  Long id) {
        try {
            respostaRepository.deleteById(id);
            logger.debug("Resposta com id  "+id+ " removida com sucesso");
           return  new CustomResponseEntity(HttpStatus.OK);
        }catch (Exception e) {
            logger.debug("Resposta com id "+id+" não encontrada");
            return new CustomResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    private Topico topicoRetornado(String topico) {
        System.out.println("Buscando Titulo "+topico);
        Optional<Topico> optionalTopico = topicoRepository.findByTitulo(topico).stream().findFirst();
        if(optionalTopico.isPresent()) {
            System.out.println("Titulo Encontrado");
            logger.debug("Topico  " + topico + " retornado com sucesso");
            return optionalTopico.get();
        }
        else {
            logger.error("Topico " + topico + " não foi encontrado");
            return null;
        }
    }
}
