package br.com.alura.curso.springboot.forum.form;

import br.com.alura.curso.springboot.forum.config.security.AutenticadViaTokenFilter;
import br.com.alura.curso.springboot.forum.model.Resposta;
import br.com.alura.curso.springboot.forum.model.Topico;
import br.com.alura.curso.springboot.forum.model.Usuario;
import br.com.alura.curso.springboot.forum.repository.TopicoRepository;
import br.com.alura.curso.springboot.forum.repository.UsuarioRepository;
import br.com.alura.curso.springboot.forum.service.TokenService;
import com.sun.xml.bind.v2.runtime.output.SAXOutput;
import org.hibernate.procedure.NoSuchParameterException;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import java.awt.print.Pageable;
import java.util.Optional;

public class RespostaForm {

    private String mensagem;
    private String topico;

    public String getMensagem() {
        return mensagem;
    }

    public String getTopico() {
        return topico;
    }

    public Resposta converter(TopicoRepository topicoRepository,TokenService tokenService,String token,UsuarioRepository usuarioRepository) {
        System.out.println("Buscando usuario logado");
        System.out.println("Topico:"+topico);
        Usuario autor = retornaUsuarioLogado(token,tokenService,usuarioRepository);
        System.out.println("Usuario:"+autor.getNome());
        Optional<Topico> topicoOptional = topicoRepository.findByTitulo(topico).stream().findFirst();
        System.out.println(topicoOptional.isPresent());
        if(topicoOptional.isPresent()){
            Topico topico = topicoOptional.get();
            return new Resposta(mensagem,topico,autor,false);
        }else {
            throw new NoSuchParameterException("A Resposta não está associada a um Tópico existente");
        }
    }



    private Usuario retornaUsuarioLogado(String token, TokenService tokenService, UsuarioRepository usuarioRepository){
        Long idUsuario = tokenService.getIdUsuario(token);
        System.out.println(idUsuario);
        return usuarioRepository.findById(idUsuario).get();
    }
}
