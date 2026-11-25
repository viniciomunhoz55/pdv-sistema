package com.meupdv.pdv.service;

import com.meupdv.pdv.config.TentativasDeLogin;
import com.meupdv.pdv.entity.Usuario;
import com.meupdv.pdv.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final TentativasDeLogin tentativas;

    public AutenticacaoService(UsuarioRepository usuarioRepository, TentativasDeLogin tentativas) {
        this.usuarioRepository = usuarioRepository;
        this.tentativas = tentativas;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return User
                .withUsername(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(perfilDe(usuario))
                .accountLocked(tentativas.estaBloqueado(login))
                .build();
    }

    // sem perfil definido, vale o menor acesso (CAIXA)
    private String perfilDe(Usuario usuario) {
        return usuario.getPerfil() == null ? "CAIXA" : usuario.getPerfil();
    }
}