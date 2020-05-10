package br.com.alura.curso.springboot.forum.form;

import br.com.alura.curso.springboot.forum.model.Resposta;
import br.com.alura.curso.springboot.forum.model.Topico;
import br.com.alura.curso.springboot.forum.model.Usuario;
import br.com.alura.curso.springboot.forum.repository.TopicoRepository;
import br.com.alura.curso.springboot.forum.repository.UsuarioRepository;
import br.com.alura.curso.springboot.forum.service.TokenService;
import org.hibernate.procedure.NoSuchParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class RespostaForm {

    private static final Logger logger = LoggerFactory.getLogger(RespostaForm.class);
    private String mensagem;
    private String topico;

    public String getMensagem() {
        return mensagem;
    }

    public String getTopico() {
        return topico;
    }

    public Resposta converter(TopicoRepository topicoRepository, TokenService tokenService, String token, UsuarioRepository usuarioRepository) {
        logger.info("Buscando usuario logado");
        Usuario autor = retornaUsuarioLogado(token, tokenService, usuarioRepository);
        logger.debug("Usuario:" + autor.getNome());
        Optional<Topico> topicoOptional = topicoRepository.findByTitulo(topico).stream().findFirst();
        if (topicoOptional.isPresent()) {
            logger.debug("Tópico " + topico + " retornado");
            Topico topico = topicoOptional.get();
            return new Resposta(mensagem, topico, autor, false);
        } else {
            logger.error("Tópico " + topico + " não relacionado a resposta");
            throw new NoSuchParameterException("A Resposta não está associada a um Tópico existente");
        }
    }


    private Usuario retornaUsuarioLogado(String token, TokenService tokenService, UsuarioRepository usuarioRepository) {
        Long idUsuario = tokenService.getIdUsuario(token);
        return usuarioRepository.findById(idUsuario).get();
    }
}
