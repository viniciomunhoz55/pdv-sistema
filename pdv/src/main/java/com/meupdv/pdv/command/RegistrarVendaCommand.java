package com.meupdv.pdv.command;

import com.meupdv.pdv.entity.Venda;
import com.meupdv.pdv.service.VendaService;

public class RegistrarVendaCommand implements Command {

    private final VendaService vendaService;
    private final Venda venda;
    private Venda resultado;

    public RegistrarVendaCommand(VendaService vendaService, Venda venda) {
        this.vendaService = vendaService;
        this.venda = venda;
    }

    @Override
    public void executar() {
        resultado = vendaService.finalizarVenda(venda);
    }

    public Venda getResultado() {
        return resultado;
    }
}