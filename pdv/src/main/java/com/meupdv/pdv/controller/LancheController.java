package com.meupdv.pdv.controller;

import com.meupdv.pdv.lanche.CatalogoLanches;
import com.meupdv.pdv.lanche.Lanche;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/lanches")
public class LancheController {

    private final CatalogoLanches catalogo;

    public LancheController(CatalogoLanches catalogo) {
        this.catalogo = catalogo;
    }

    @GetMapping
    public String tela(Model model) {
        model.addAttribute("lanches", catalogo.lanches());
        model.addAttribute("adicionais", catalogo.adicionais());
        return "lanches";
    }

    @GetMapping("/calcular")
    public String calcular(@RequestParam String lanche,
                           @RequestParam(required = false) List<String> adicionais,
                           Model model) {
        try {
            List<String> marcados = adicionais == null ? List.of() : adicionais;
            Lanche pedido = catalogo.montar(lanche, marcados);
            model.addAttribute("descricao", pedido.getDescricao());
            model.addAttribute("total", pedido.custo());
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
        }
        return "lanche-resultado";
    }
}