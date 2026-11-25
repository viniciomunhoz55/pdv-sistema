package com.meupdv.pdv.config;

import com.meupdv.pdv.lanche.Cardapio;
import com.meupdv.pdv.lanche.CatalogoLanches;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LancheConfig {

    @Bean
    public CatalogoLanches catalogoLanches() {
        return new Cardapio();
    }
}