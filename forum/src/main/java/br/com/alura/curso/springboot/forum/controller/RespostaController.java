package br.com.alura.curso.springboot.forum.controller;

import br.com.alura.curso.springboot.forum.DTO.RespostaDTO;
import br.com.alura.curso.springboot.forum.form.RespostaForm;
import br.com.alura.curso.springboot.forum.model.Resposta;
import br.com.alura.curso.springboot.forum.model.Topico;
import br.com.alura.curso.springboot.forum.repository.RespostaRepository;
import br.com.alura.curso.springboot.forum.repository.TopicoRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("resposta")
public class RespostaController  extends  BaseController<Resposta,Long, RespostaForm, RespostaDTO>{
    @Autowired
    RespostaRepository respostaRepository;

    @Autowired
    TopicoRepository topicoRepository;

    @Override
    @Transactional
    public ResponseEntity<RespostaDTO> cadastrar(@RequestBody  @Valid RespostaForm respostaForm, UriComponentsBuilder uriComponentsBuilder) {
        try {
        Resposta resposta = respostaForm.converter(topicoRepository);
            Resposta respostaSalva = respostaRepository.save(resposta);
         return ResponseEntity.ok(new RespostaDTO(respostaSalva));
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public Page<RespostaDTO> listar(String topico, Pageable page) {
        Topico topicoRetornado = topicoRetornado(topico);
        Page<Resposta> respostasPorTopico = respostaRepository.findByTopico(topicoRetornado, page);
        return  respostasPorTopico.map(RespostaDTO::new);
    }

    private Topico topicoRetornado(String topico) {
        Optional<Topico> optionalTopico = topicoRepository.findByTitulo(topico).stream().findFirst();
        if(optionalTopico.isPresent())
        return optionalTopico.get();
        else
            return null;
    }

    @Override
    @Transactional
    public ResponseEntity<RespostaDTO> detalhar(@PathVariable Long id) {
        return ResponseEntity.badRequest().build();
    }

    @Override
    @Transactional
    public ResponseEntity<RespostaDTO> atualizar(@PathVariable Long id,@RequestBody RespostaDTO respostaDTO) {
        return ResponseEntity.badRequest().build();
    }

    @Override
    @Transactional
    public ResponseEntity remover(@PathVariable  Long id) {
        return ResponseEntity.badRequest().build();
    }
}
