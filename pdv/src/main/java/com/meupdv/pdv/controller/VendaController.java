package com.meupdv.pdv.controller;

import com.meupdv.pdv.entity.Venda;
import com.meupdv.pdv.entity.Cliente;
import com.meupdv.pdv.entity.Produto;
import com.meupdv.pdv.service.VendaService;
import com.meupdv.pdv.service.ClienteService;
import com.meupdv.pdv.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.meupdv.pdv.command.RegistrarVendaCommand;
import com.meupdv.pdv.command.CancelarVendaCommand;
import org.springframework.web.bind.annotation.PathVariable;
@Controller
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProdutoService produtoService;

    // tela de PDV (nova venda)
    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("produtos", produtoService.listarTodos());
        return "pdv";
    }

    // finaliza a venda (baixar estoque mais gerar cupom)
@PostMapping
public String finalizar(@ModelAttribute Venda venda, RedirectAttributes ra) {
    if (venda.getCliente() != null && venda.getCliente().getIdCliente() == null) {
        venda.setCliente(null);
    }
    try {
        RegistrarVendaCommand comando = new RegistrarVendaCommand(vendaService, venda);
        comando.executar();
        Venda vendaSalva = comando.getResultado();
        return "redirect:/vendas/" + vendaSalva.getIdVenda() + "/cupom";
    } catch (RuntimeException e) {
        ra.addFlashAttribute("erro", e.getMessage());
        return "redirect:/vendas/nova";
    }
}
    

    // exibe o cupom gerado pois a venda
    @GetMapping("/{id}/cupom")
    public String cupom(@PathVariable Long id, Model model) {
        model.addAttribute("venda", vendaService.buscarPorId(id));
        return "cupom";
    }
        
        //  lista todas as vendas e cupom
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("vendas", vendaService.listarTodos());
        return "vendas";
    }

    //  cancelar a venda e devolve o estoque bd
    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            CancelarVendaCommand comando = new CancelarVendaCommand(vendaService, id);
            comando.executar();
            ra.addFlashAttribute("sucesso", "Venda cancelada e estoque devolvido.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/vendas";
    }
}
 