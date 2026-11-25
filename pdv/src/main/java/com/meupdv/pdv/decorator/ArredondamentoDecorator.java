package com.meupdv.pdv.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ArredondamentoDecorator extends TotalDecorator {

    public ArredondamentoDecorator(CalculoTotal origem) {
        super(origem);
    }

    @Override
    public BigDecimal calcular() {
        return origem.calcular().setScale(2, RoundingMode.HALF_UP);
    }
}