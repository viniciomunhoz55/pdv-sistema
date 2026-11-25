package com.meupdv.pdv.lanche;

import java.math.BigDecimal;

public class QueijoPrato extends AdicionalDecorator {

    public QueijoPrato(Lanche lanche) {
        super(lanche);
    }

    @Override
    public String getDescricao() {
        return lanche.getDescricao() + ", Queijo prato";
    }

    @Override
    public BigDecimal custo() {
        return lanche.custo().add(new BigDecimal("4.00"));
    }
}