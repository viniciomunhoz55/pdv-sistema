package com.meupdv.pdv.config;

import com.meupdv.pdv.entity.Usuario;
import com.meupdv.pdv.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CriadorDeUsuarios implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final String senhaCaixa;

    public CriadorDeUsuarios(UsuarioRepository usuarioRepository,
                             PasswordEncoder passwordEncoder,
                             @Value("${pdv.caixa.senha:}") String senhaCaixa) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.senhaCaixa = senhaCaixa;
    }

    @Override
    public void run(String... args) {
        definirPerfilDosUsuariosAntigos();
        criarUsuarioCaixa();
    }

    // quem já existia era ADMIN antes dos perfis
    private void definirPerfilDosUsuariosAntigos() {
        for (Usuario usuario : usuarioRepository.findAll()) {
            if (usuario.getPerfil() == null) {
                usuario.setPerfil("ADMIN");
                usuarioRepository.save(usuario);
            }
        }
    }

    private void criarUsuarioCaixa() {
        if (senhaCaixa.isBlank() || usuarioRepository.findByLogin("caixa").isPresent()) {
            return;
        }
        Usuario caixa = new Usuario();
        caixa.setLogin("caixa");
        caixa.setSenha(passwordEncoder.encode(senhaCaixa));
        caixa.setPerfil("CAIXA");
        usuarioRepository.save(caixa);
    }
}