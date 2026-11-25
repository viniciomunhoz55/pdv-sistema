package com.meupdv.pdv.lanche;

import java.math.BigDecimal;

public class Bacon extends AdicionalDecorator {

    public Bacon(Lanche lanche) {
        super(lanche);
    }

    @Override
    public String getDescricao() {
        return lanche.getDescricao() + ", Bacon";
    }

    @Override
    public BigDecimal custo() {
        return lanche.custo().add(new BigDecimal("2.00"));
    }
}