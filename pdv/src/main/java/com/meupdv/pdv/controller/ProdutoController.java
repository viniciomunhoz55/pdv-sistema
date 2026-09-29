package com.meupdv.pdv.controller;

import com.meupdv.pdv.entity.Produto;
import com.meupdv.pdv.service.ProdutoService;
import com.meupdv.pdv.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", produtoService.listarTodos());
        return "produtos";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("produto", new Produto());
        model.addAttribute("categorias", categoriaService.listarTodos());
        return "produto-form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("produto", produtoService.buscarPorId(id));
        model.addAttribute("categorias", categoriaService.listarTodos());
        return "produto-form";
    }

    @PostMapping
    public String salvar(@ModelAttribute Produto produto) {
        produtoService.salvar(produto);
        return "redirect:/produtos";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        produtoService.deletar(id);
        return "redirect:/produtos";
    }
    
    @GetMapping("/buscar")
public String buscarPorId(@RequestParam(required = false) Long id, Model model) {
    List<Produto> resultado = new ArrayList<>();
    if (id != null) {
        Produto produto = produtoService.buscarPorId(id);
        if (produto != null) {
            resultado.add(produto);
        }
    } else {
        resultado = produtoService.listarTodos();
    }
    model.addAttribute("produtos", resultado);
    return "produtos";
    }   
}