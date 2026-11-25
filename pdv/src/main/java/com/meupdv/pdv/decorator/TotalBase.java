package com.meupdv.pdv.decorator;

import java.math.BigDecimal;

public class TotalBase implements CalculoTotal {

    private final BigDecimal valor;

    public TotalBase(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public BigDecimal calcular() {
        return valor;
    }
}