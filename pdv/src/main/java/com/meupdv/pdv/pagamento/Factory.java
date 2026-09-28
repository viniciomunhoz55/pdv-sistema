package com.meupdv.pdv.pagamento;

public class Factory {

    public static FormaPagamento criar(String tipo) {
        switch (tipo) {
            case "DINHEIRO":
                return new PagamentoDinheiro();
            case "PIX":
                return new PagamentoPix();
            case "CARTAO_DEBITO":
                return new PagamentoCartaoDebito();
            case "CARTAO_CREDITO":
                return new PagamentoCartaoCredito();
            default:
                throw new IllegalArgumentException("Forma de pagamento inválida: " + tipo);
        }
    }
}