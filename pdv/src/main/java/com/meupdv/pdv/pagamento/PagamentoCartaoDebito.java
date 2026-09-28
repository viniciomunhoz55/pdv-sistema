package com.meupdv.pdv.pagamento;

import java.math.BigDecimal;

public class PagamentoCartaoDebito implements FormaPagamento {

    @Override
    public BigDecimal calcularValorFinal(BigDecimal valorOriginal) {
        return valorOriginal; 
    }

    @Override
    public String getDescricao() {
        return "Cartão de débito";
    }
}