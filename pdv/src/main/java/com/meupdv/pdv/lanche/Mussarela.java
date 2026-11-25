package com.meupdv.pdv.lanche;

import java.math.BigDecimal;

public class Mussarela extends AdicionalDecorator {

    public Mussarela(Lanche lanche) {
        super(lanche);
    }

    @Override
    public String getDescricao() {
        return lanche.getDescricao() + ", Mussarela";
    }

    @Override
    public BigDecimal custo() {
        return lanche.custo().add(new BigDecimal("3.00"));
    }
}