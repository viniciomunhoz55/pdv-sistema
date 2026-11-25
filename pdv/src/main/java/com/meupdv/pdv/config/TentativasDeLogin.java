package com.meupdv.pdv.config;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TentativasDeLogin {

    private static final int MAXIMO_DE_ERROS = 3;
    private static final Duration TEMPO_DE_BLOQUEIO = Duration.ofMinutes(1);

    private final Map<String, Integer> erros = new ConcurrentHashMap<>();
    private final Map<String, Instant> bloqueadosAte = new ConcurrentHashMap<>();

    public void registrarErro(String login) {
        int total = erros.merge(login, 1, Integer::sum);
        if (total >= MAXIMO_DE_ERROS) {
            bloqueadosAte.put(login, Instant.now().plus(TEMPO_DE_BLOQUEIO));
            erros.remove(login);
        }
    }

    public void registrarSucesso(String login) {
        erros.remove(login);
    }

    public boolean estaBloqueado(String login) {
        return bloqueadoAte(login) != null;
    }

    // até quando está bloqueado; null se não está
    public Instant bloqueadoAte(String login) {
        Instant ate = bloqueadosAte.get(login);
        if (ate == null) {
            return null;
        }
        if (Instant.now().isAfter(ate)) {
            bloqueadosAte.remove(login);
            return null;
        }
        return ate;
    }

    public int tentativasRestantes(String login) {
        return Math.max(0, MAXIMO_DE_ERROS - erros.getOrDefault(login, 0));
    }
}