package com.meupdv.pdv.lanche;

import java.math.BigDecimal;

public class LancheBase implements Lanche {

    private final String nome;
    private final BigDecimal preco;

    public LancheBase(String nome, BigDecimal preco) {
        this.nome = nome;
        this.preco = preco;
    }

    @Override
    public String getDescricao() {
        return nome;
    }

    @Override
    public BigDecimal custo() {
        return preco;
    }
}