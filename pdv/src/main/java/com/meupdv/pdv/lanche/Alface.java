package com.meupdv.pdv.lanche;

import java.math.BigDecimal;

public class Alface extends AdicionalDecorator {

    public Alface(Lanche lanche) {
        super(lanche);
    }

    @Override
    public String getDescricao() {
        return lanche.getDescricao() + ", Alface";
    }

    @Override
    public BigDecimal custo() {
        return lanche.custo().add(new BigDecimal("5.00"));
    }
}