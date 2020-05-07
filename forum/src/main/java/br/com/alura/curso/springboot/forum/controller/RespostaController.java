package br.com.alura.curso.springboot.forum.controller;

import br.com.alura.curso.springboot.forum.DTO.RespostaDTO;
import br.com.alura.curso.springboot.forum.config.security.AutenticacaoService;
import br.com.alura.curso.springboot.forum.form.RespostaForm;
import br.com.alura.curso.springboot.forum.model.Resposta;
import br.com.alura.curso.springboot.forum.model.Topico;
import br.com.alura.curso.springboot.forum.model.Usuario;
import br.com.alura.curso.springboot.forum.repository.RespostaRepository;
import br.com.alura.curso.springboot.forum.repository.TopicoRepository;
import br.com.alura.curso.springboot.forum.repository.UsuarioRepository;
import br.com.alura.curso.springboot.forum.service.TokenService;
import br.com.alura.curso.springboot.forum.util.RecuperaToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.OverridesAttribute;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("resposta")
public class RespostaController  extends  BaseController<Resposta,Long, RespostaForm, RespostaDTO>{
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
    public ResponseEntity<RespostaDTO> cadastrar(@RequestHeader(name = "Authorization") String token, @RequestBody  @Valid RespostaForm respostaForm, UriComponentsBuilder uriComponentsBuilder) {
        try {
            System.out.println("Conversão inicializada...");
            Resposta resposta = respostaForm.converter(topicoRepository,tokenService, RecuperaToken.retornaToken(token),usuarioRepository);
            System.out.println("Conversão realizada");
            respostaRepository.save(resposta);
            System.out.println("Transação realizada");
            URI uri = uriComponentsBuilder.path("/curso/{id}").buildAndExpand(resposta.getId()).toUri();
         return ResponseEntity.created(uri).build().ok(new RespostaDTO(resposta));
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @Override
    public Page<RespostaDTO> listar(@PathVariable String topico, Pageable page) {
        Topico topicoRetornado = topicoRetornado(topico);
        Page<Resposta> respostasPorTopico = respostaRepository.findByTopico(topicoRetornado, page);
        return  respostasPorTopico.map(RespostaDTO::new);
    }


    @Override
    @Transactional
    public ResponseEntity<RespostaDTO> detalhar(@PathVariable Long id) {
        Optional<Resposta> respostaRetornada = respostaRepository.findById(id);
        if(respostaRetornada.isPresent()){
            return ResponseEntity.ok(new RespostaDTO(respostaRetornada.get()));
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    @Transactional
    public ResponseEntity<RespostaDTO> atualizar(@PathVariable Long id,@RequestBody RespostaDTO respostaDTO) {
        try {
            Resposta resposta = respostaDTO.converter(respostaRepository, id, topicoRepository);
            System.out.println(resposta);
             return ResponseEntity.ok(new RespostaDTO(resposta));
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    @Transactional
    public ResponseEntity remover(@PathVariable  Long id) {
        try {
            respostaRepository.deleteById(id);
           return  ResponseEntity.ok().build();
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private Topico topicoRetornado(String topico) {
        Optional<Topico> optionalTopico = topicoRepository.findByTitulo(topico).stream().findFirst();
        if(optionalTopico.isPresent())
        return optionalTopico.get();
        else
            return null;
    }
}
