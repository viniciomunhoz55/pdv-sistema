package com.meupdv.pdv.pagamento;

import java.math.BigDecimal;

public class PagamentoPix implements FormaPagamento {

    @Override
    public BigDecimal calcularValorFinal(BigDecimal valorOriginal) {
        BigDecimal desconto = valorOriginal.multiply(new BigDecimal("0.05"));
        return valorOriginal.subtract(desconto);
    }

    @Override
    public String getDescricao() {
        return "Pix";
    }
}