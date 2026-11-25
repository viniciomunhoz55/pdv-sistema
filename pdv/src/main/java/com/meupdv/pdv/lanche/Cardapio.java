package com.meupdv.pdv.lanche;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class Cardapio implements CatalogoLanches {

    private final Map<String, BigDecimal> precosDosLanches = new LinkedHashMap<>();
    private final Map<String, UnaryOperator<Lanche>> adicionaisDisponiveis = new LinkedHashMap<>();

    public Cardapio() {
        precosDosLanches.put("X-Salada", new BigDecimal("8.00"));
        precosDosLanches.put("Hambúrguer Doritos", new BigDecimal("19.00"));
        precosDosLanches.put("X-Quebrada", new BigDecimal("20.00"));
        precosDosLanches.put("Monstrão", new BigDecimal("40.00"));

        adicionaisDisponiveis.put("Mussarela", Mussarela::new);
        adicionaisDisponiveis.put("Queijo prato", QueijoPrato::new);
        adicionaisDisponiveis.put("Bacon", Bacon::new);
        adicionaisDisponiveis.put("Alface", Alface::new);
        adicionaisDisponiveis.put("Tomate", Tomate::new);
    }

    @Override
    public Map<String, BigDecimal> lanches() {
        return Collections.unmodifiableMap(precosDosLanches);
    }

    @Override
    public Map<String, BigDecimal> adicionais() {
        Map<String, BigDecimal> precos = new LinkedHashMap<>();
        Lanche vazio = new LancheBase("", BigDecimal.ZERO);
        adicionaisDisponiveis.forEach((nome, adicional) -> precos.put(nome, adicional.apply(vazio).custo()));
        return precos;
    }

    @Override
    public Lanche montar(String nomeLanche, List<String> nomesAdicionais) {
        BigDecimal preco = precosDosLanches.get(nomeLanche);
        if (preco == null) {
            throw new IllegalArgumentException("Lanche inválido: " + nomeLanche);
        }
        Lanche pedido = new LancheBase(nomeLanche, preco);
        for (String nome : nomesAdicionais) {
            UnaryOperator<Lanche> adicional = adicionaisDisponiveis.get(nome);
            if (adicional == null) {
                throw new IllegalArgumentException("Adicional inválido: " + nome);
            }
            pedido = adicional.apply(pedido);
        }
        return pedido;
    }
}