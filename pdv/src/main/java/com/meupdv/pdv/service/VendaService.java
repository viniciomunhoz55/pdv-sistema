package com.meupdv.pdv.service;

import com.meupdv.pdv.entity.*;
import com.meupdv.pdv.repository.VendaRepository;
import com.meupdv.pdv.repository.ProdutoRepository;
import com.meupdv.pdv.repository.CupomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.meupdv.pdv.pagamento.FormaPagamento;
import com.meupdv.pdv.pagamento.Factory;


@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CupomRepository cupomRepository;

    public List<Venda> listarTodos() {
        return vendaRepository.findAll();
    }

    public Venda buscarPorId(Long id) {
        return vendaRepository.findById(id).orElse(null);
    }

    // Aqui está o processo de negócio automatizado
  @Transactional
public Venda finalizarVenda(Venda vendaRecebida) {
    Venda.VendaBuilder builder = Venda.builder()
            .comCliente(vendaRecebida.getCliente())
            .comFormaPagamento(vendaRecebida.getFormaPagamento());

    for (ItemVenda itemRecebido : vendaRecebida.getItens()) {
        Produto produto = produtoRepository.findById(itemRecebido.getProduto().getIdProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (produto.getQuantidadeEstoque() < itemRecebido.getQuantidade()) {
            throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
        }

        // Baixa automática de estoque e bd
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - itemRecebido.getQuantidade());
        produtoRepository.save(produto);

        BigDecimal precoUnitario = produto.getPrecoVenda();
        BigDecimal subtotal = precoUnitario.multiply(BigDecimal.valueOf(itemRecebido.getQuantidade()));

        ItemVenda item = new ItemVenda();
        item.setProduto(produto);
        item.setQuantidade(itemRecebido.getQuantidade());
        item.setPrecoUnitario(precoUnitario);
        item.setSubtotal(subtotal);

        builder.adicionarItem(item);
    }

    Venda venda = builder.build();
    FormaPagamento formaPagamento = Factory.criar(venda.getFormaPagamento());
BigDecimal totalComRegra = formaPagamento.calcularValorFinal(venda.getTotal());
venda.setTotal(totalComRegra);
    Venda vendaSalva = vendaRepository.save(venda);

    // Geração automática do cupom fiscal
    Cupom cupom = new Cupom();
    cupom.setNumeroCupom("CUPOM FISCAL-" + vendaSalva.getIdVenda());
    cupom.setDataEmissao(LocalDateTime.now());
    cupom.setValorTotal(vendaSalva.getTotal());
    cupom.setVenda(vendaSalva);
    cupomRepository.save(cupom);

    return vendaSalva;
}
@Transactional
public void cancelarVenda(Long idVenda) {
    Venda venda = vendaRepository.findById(idVenda)
            .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

    for (ItemVenda item : venda.getItens()) {
        Produto produto = item.getProduto();
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + item.getQuantidade());
        produtoRepository.save(produto);
    }
    
    Cupom cupom = cupomRepository.findByVenda(venda);
    if (cupom != null) {
    cupomRepository.delete(cupom);
}

    vendaRepository.delete(venda);
}

    public void deletar(Long id) {
        vendaRepository.deleteById(id);
    }
}