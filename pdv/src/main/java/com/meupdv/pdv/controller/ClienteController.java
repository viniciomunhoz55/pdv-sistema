package com.meupdv.pdv.controller;

import com.meupdv.pdv.entity.Cliente;
import com.meupdv.pdv.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // listar todos clientes
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "clientes";
    }

    // cliente novo form
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente-form";
    }

    // mostra formulário preenchido
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", clienteService.buscarPorId(id));
        return "cliente-form";
    }

    //cliente salvar (novo ou edição)
    @PostMapping
    public String salvar(@ModelAttribute Cliente cliente) {
        clienteService.salvar(cliente);
        return "redirect:/clientes";
    }

    // cliente excluir ou deletar
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        clienteService.deletar(id);
        return "redirect:/clientes";
    }
}