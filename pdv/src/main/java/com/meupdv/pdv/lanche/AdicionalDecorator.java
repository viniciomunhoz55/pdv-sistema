package com.meupdv.pdv.lanche;

import java.math.BigDecimal;

public abstract class AdicionalDecorator implements Lanche {

    protected final Lanche lanche;

    protected AdicionalDecorator(Lanche lanche) {
        this.lanche = lanche;
    }

    @Override
    public String getDescricao() {
        return lanche.getDescricao();
    }

    @Override
    public BigDecimal custo() {
        return lanche.custo();
    }
}