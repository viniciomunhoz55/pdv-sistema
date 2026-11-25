package com.meupdv.pdv.lanche;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CatalogoLanches {

    Map<String, BigDecimal> lanches();

    Map<String, BigDecimal> adicionais();

    Lanche montar(String nomeLanche, List<String> nomesAdicionais);
}