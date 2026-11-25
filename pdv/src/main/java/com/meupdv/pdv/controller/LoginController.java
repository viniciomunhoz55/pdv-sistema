package com.meupdv.pdv.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(HttpSession sessao, Model model) {
        Object restantes = sessao.getAttribute("loginRestantes");
        Object bloqueadoAte = sessao.getAttribute("loginBloqueadoAte");

        // a mensagem de tentativas aparece uma vez só
        sessao.removeAttribute("loginRestantes");

        // o bloqueio continua aparecendo (mesmo recarregando) até o tempo acabar
        if (bloqueadoAte instanceof Long fim && fim > System.currentTimeMillis()) {
            model.addAttribute("bloqueadoAte", fim);
        } else {
            sessao.removeAttribute("loginBloqueadoAte");
        }

        if (restantes != null) {
            model.addAttribute("restantes", restantes);
        }
        return "login";
    }
}