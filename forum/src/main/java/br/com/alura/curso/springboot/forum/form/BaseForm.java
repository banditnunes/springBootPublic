package br.com.alura.curso.springboot.forum.form;

import br.com.alura.curso.springboot.forum.model.Usuario;
import br.com.alura.curso.springboot.forum.repository.UsuarioRepository;
import br.com.alura.curso.springboot.forum.service.TokenService;

public abstract class BaseForm<T,TRepository> {

    public Usuario retornaUsuarioLogado(String token, TokenService tokenService, UsuarioRepository usuarioRepository){
        System.out.println(token);
        Long idUsuario = tokenService.getIdUsuario(token);
        System.out.println(idUsuario);
        return usuarioRepository.findById(idUsuario).get();
    }
}
