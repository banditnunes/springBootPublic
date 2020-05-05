package br.com.alura.curso.springboot.forum.form;

import br.com.alura.curso.springboot.forum.config.security.AutenticadViaTokenFilter;
import br.com.alura.curso.springboot.forum.model.Resposta;
import br.com.alura.curso.springboot.forum.model.Topico;
import br.com.alura.curso.springboot.forum.model.Usuario;
import br.com.alura.curso.springboot.forum.repository.TopicoRepository;
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

    public Resposta converter(TopicoRepository topicoRepository) {
        Usuario autor = AutenticadViaTokenFilter.retornaUsuarioLogado();



        Optional<Topico> topicoOptional = topicoRepository.findByTitulo(topico).stream().findFirst();
        System.out.println(topicoOptional.get().getId());
        if(topicoOptional.isPresent()){
            Topico topico = topicoOptional.get();
            return new Resposta(mensagem,topico,autor,false);
        }else {
            throw new NoSuchParameterException("A Resposta não está associada a um Tópico existente");
        }
    }
}
