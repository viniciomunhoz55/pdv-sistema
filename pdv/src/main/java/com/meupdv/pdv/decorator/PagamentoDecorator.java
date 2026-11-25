package com.meupdv.pdv.decorator;

import com.meupdv.pdv.pagamento.FormaPagamento;
import java.math.BigDecimal;

public class PagamentoDecorator extends TotalDecorator {

    private final FormaPagamento formaPagamento;

    public PagamentoDecorator(CalculoTotal origem, FormaPagamento formaPagamento) {
        super(origem);
        this.formaPagamento = formaPagamento;
    }

    @Override
    public BigDecimal calcular() {
        return formaPagamento.calcularValorFinal(origem.calcular());
    }
}