package com.meupdv.pdv.lanche;

import java.math.BigDecimal;

public class Tomate extends AdicionalDecorator {

    public Tomate(Lanche lanche) {
        super(lanche);
    }

    @Override
    public String getDescricao() {
        return lanche.getDescricao() + ", Tomate";
    }

    @Override
    public BigDecimal custo() {
        return lanche.custo().add(new BigDecimal("8.00"));
    }
}