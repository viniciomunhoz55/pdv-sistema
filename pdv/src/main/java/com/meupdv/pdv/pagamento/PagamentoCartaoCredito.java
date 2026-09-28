 package com.meupdv.pdv.pagamento;

import java.math.BigDecimal;

public class PagamentoCartaoCredito implements FormaPagamento {

    @Override
    public BigDecimal calcularValorFinal(BigDecimal valorOriginal) {
        BigDecimal acrescimo = valorOriginal.multiply(new BigDecimal("0.03"));
        return valorOriginal.add(acrescimo);
    }

    @Override
    public String getDescricao() {
        return "Cartão de crédito";
    }
}
