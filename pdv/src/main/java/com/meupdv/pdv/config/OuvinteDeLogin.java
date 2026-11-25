package com.meupdv.pdv.config;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class OuvinteDeLogin {

    private final TentativasDeLogin tentativas;

    public OuvinteDeLogin(TentativasDeLogin tentativas) {
        this.tentativas = tentativas;
    }

    @EventListener
    public void aoFalhar(AuthenticationFailureBadCredentialsEvent evento) {
        tentativas.registrarErro(evento.getAuthentication().getName());
    }

    @EventListener
    public void aoEntrar(AuthenticationSuccessEvent evento) {
        tentativas.registrarSucesso(evento.getAuthentication().getName());
    }
}