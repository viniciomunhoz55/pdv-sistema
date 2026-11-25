package com.meupdv.pdv.lanche;

import java.math.BigDecimal;

public class SolucaoDecoratorLanche {

    public static void main(String[] args) {
        Lanche pedido = new LancheBase("X-Salada", new BigDecimal("8.00"));
        pedido = new Mussarela(pedido);
        pedido = new Bacon(pedido);
        pedido = new Tomate(pedido);

        System.out.println(pedido.getDescricao() + " custa R$ " + pedido.custo());
    }
}