package com.meupdv.pdv.historico;

import com.meupdv.pdv.entity.Venda;

public interface HistoricoVendas {

    void registrar(Venda venda);

    void remover(Long idVenda);
}