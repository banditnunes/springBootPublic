package br.com.alura.curso.springboot.forum.config.security;

import br.com.alura.curso.springboot.forum.model.Usuario;
import br.com.alura.curso.springboot.forum.repository.UsuarioRepository;
import br.com.alura.curso.springboot.forum.service.TokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AutenticadViaTokenFilter extends OncePerRequestFilter {

    private  TokenService tokenService;
    private UsuarioRepository repository;

    public AutenticadViaTokenFilter(TokenService tokenService, UsuarioRepository repository) {
        this.tokenService=tokenService;
        this.repository=repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recuperaToken(request);
        //Verifica se o token existe
        boolean valido = tokenService.isTokenValido(token);
        if(valido){
            autenticarCliente(token);
        }
        filterChain.doFilter(request,response);
    }

    private void autenticarCliente(String token) {
        //Recupera id do usuario passando o token
        Long usuarioId = tokenService.getIdUsuario(token);
        //Retorna o usuario do banco
        Usuario usuario = repository.findById(usuarioId).get();
        //Cria o usuario autenticado com as permissões
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario,null,usuario.getAuthorities());
        //Coloca o usuario autenticado no contexto da app
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String recuperaToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token==null || token.isEmpty() || !token.startsWith("Bearer")){
            return  null;
        }
        return token.substring(7,token.length());

    }

    public static Usuario retornaUsuarioLogado(String token){


        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
