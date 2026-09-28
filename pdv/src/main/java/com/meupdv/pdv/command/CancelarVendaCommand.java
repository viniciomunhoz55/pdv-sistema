package com.meupdv.pdv.command;

import com.meupdv.pdv.service.VendaService;

public class CancelarVendaCommand implements Command {

    private final VendaService vendaService;
    private final Long idVenda;

    public CancelarVendaCommand(VendaService vendaService, Long idVenda) {
        this.vendaService = vendaService;
        this.idVenda = idVenda;
    }

    @Override
    public void executar() {
        vendaService.cancelarVenda(idVenda);
    }
}