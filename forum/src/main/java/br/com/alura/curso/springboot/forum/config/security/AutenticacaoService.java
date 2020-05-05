package br.com.alura.curso.springboot.forum.config.security;

import br.com.alura.curso.springboot.forum.model.Usuario;
import br.com.alura.curso.springboot.forum.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {
    @Autowired
    UsuarioRepository usuarioRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> email = usuarioRepository.findByEmail(username);
        if(email.isPresent()){
            return email.get();
        }
        throw new UsernameNotFoundException("Erro na validação dos dados");

    }
}
