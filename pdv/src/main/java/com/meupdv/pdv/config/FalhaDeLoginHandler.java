package com.meupdv.pdv.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class FalhaDeLoginHandler implements AuthenticationFailureHandler {

    private final TentativasDeLogin tentativas;

    public FalhaDeLoginHandler(TentativasDeLogin tentativas) {
        this.tentativas = tentativas;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String login = request.getParameter("username");
        if (login == null) {
            login = "";
        }

        HttpSession sessao = request.getSession();
        Instant bloqueadoAte = tentativas.bloqueadoAte(login);

        if (bloqueadoAte != null) {
            sessao.setAttribute("loginBloqueadoAte", bloqueadoAte.toEpochMilli());
            sessao.removeAttribute("loginRestantes");
        } else {
            sessao.removeAttribute("loginBloqueadoAte");
            sessao.setAttribute("loginRestantes", tentativas.tentativasRestantes(login));
        }

        response.sendRedirect("/login?erro");
    }
}