package br.com.alura.curso.springboot.forum.DTO;

import br.com.alura.curso.springboot.forum.model.Resposta;
import br.com.alura.curso.springboot.forum.model.Topico;
import br.com.alura.curso.springboot.forum.repository.RespostaRepository;
import br.com.alura.curso.springboot.forum.repository.TopicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.Optional;

public class RespostaDTO implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(RespostaDTO.class);
    private Long id;
    private String mensagem;
    private String topico;
    private boolean resolvido;

    public RespostaDTO(){}

    public RespostaDTO(Resposta respostaSalva) {
            this.id = respostaSalva.getId();
            this.mensagem=respostaSalva.getMensagem();
            this.topico=respostaSalva.getTopico().getTitulo();
            this.resolvido=respostaSalva.getSolucao();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getTopico() {
        return topico;
    }

    public void setTopico(String topico) {
        this.topico = topico;
    }

    public boolean isResolvido() {
        return resolvido;
    }

    public void setResolvido(boolean resolvido) {
        this.resolvido = resolvido;
    }

    public Resposta converter(RespostaRepository respostaRepository, Long id, TopicoRepository topicoRepository) {
        Resposta resposta = respostaRepository.findById(id).get();
        logger.debug("Resposta de id "+id+" foi retornada" );
        resposta.setMensagem(getMensagem());
        Optional<Topico> topicoRetornado = topicoRepository.findByTitulo(getTopico()).stream().findFirst();

        if(topicoRetornado.isPresent()){
            logger.debug("Topico "+getTopico()+" retornado");
            resposta.setTopico(topicoRetornado.get());
        }else{
            logger.error("Topico "+getTopico()+" não encontrado");
            throw new EntityNotFoundException();
        }
        if(isResolvido()) {
            resposta.setSolucao(isResolvido());
        }
        return resposta;
    }
}
