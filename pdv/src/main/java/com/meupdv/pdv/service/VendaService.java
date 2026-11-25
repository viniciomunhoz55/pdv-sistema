package com.meupdv.pdv.service;

import com.meupdv.pdv.entity.*;
import com.meupdv.pdv.repository.VendaRepository;
import com.meupdv.pdv.repository.ProdutoRepository;
import com.meupdv.pdv.repository.CupomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.meupdv.pdv.pagamento.Factory;
import com.meupdv.pdv.decorator.CalculoTotal;
import com.meupdv.pdv.decorator.TotalBase;
import com.meupdv.pdv.decorator.PagamentoDecorator;
import com.meupdv.pdv.decorator.ArredondamentoDecorator;
import com.meupdv.pdv.lanche.CatalogoLanches;
import com.meupdv.pdv.lanche.Lanche;
import com.meupdv.pdv.historico.HistoricoVendas;


@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CupomRepository cupomRepository;
    @Autowired
    private HistoricoVendas historicoVendas;
    @Autowired
    private CatalogoLanches catalogo;

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
            builder.adicionarItem(criarItem(itemRecebido));
        }

        Venda venda = builder.build();

        CalculoTotal calcule = new TotalBase(venda.getTotal());
        calcule = new PagamentoDecorator(calcule, Factory.criar(venda.getFormaPagamento()));
        calcule = new ArredondamentoDecorator(calcule);
        venda.setTotal(calcule.calcular());

        Venda vendaSalva = vendaRepository.save(venda);

        // Geração automática do cupom fiscal
        Cupom cupom = new Cupom();
        cupom.setNumeroCupom("CUPOM FISCAL-" + vendaSalva.getIdVenda());
        cupom.setDataEmissao(LocalDateTime.now());
        cupom.setValorTotal(vendaSalva.getTotal());
        cupom.setVenda(vendaSalva);
        cupomRepository.save(cupom);
        historicoVendas.registrar(vendaSalva);

        return vendaSalva;
    }

    // lanche (Decorator) ou produto do estoque
    private ItemVenda criarItem(ItemVenda itemRecebido) {
        if (itemRecebido.getLanche() != null && !itemRecebido.getLanche().isBlank()) {
            return criarItemLanche(itemRecebido);
        }
        return criarItemProduto(itemRecebido);
    }

    private ItemVenda criarItemLanche(ItemVenda itemRecebido) {
        // o preço nunca vem da tela: o servidor monta o lanche com os Decorators e calcula
        Lanche pedido = catalogo.montar(itemRecebido.getLanche(), listaDeAdicionais(itemRecebido.getAdicionais()));

        ItemVenda item = new ItemVenda();
        item.setDescricao(pedido.getDescricao());
        item.setQuantidade(itemRecebido.getQuantidade());
        item.setPrecoUnitario(pedido.custo());
        item.setSubtotal(pedido.custo().multiply(BigDecimal.valueOf(itemRecebido.getQuantidade())));
        return item;
    }

    private ItemVenda criarItemProduto(ItemVenda itemRecebido) {
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
        return item;
    }

    private List<String> listaDeAdicionais(String texto) {
        if (texto == null) {
            return List.of();
        }
        return Arrays.stream(texto.split(","))
                .map(String::trim)
                .filter(nome -> !nome.isEmpty())
                .toList();
    }

    @Transactional
    public void cancelarVenda(Long idVenda) {
        Venda venda = vendaRepository.findById(idVenda)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        for (ItemVenda item : venda.getItens()) {
            devolverEstoque(item);
        }

        Cupom cupom = cupomRepository.findByVenda(venda);
        if (cupom != null) {
            cupomRepository.delete(cupom);
        }
        historicoVendas.remover(idVenda);

        vendaRepository.delete(venda);
    }

    // lanche não tem estoque para devolver
    private void devolverEstoque(ItemVenda item) {
        Produto produto = item.getProduto();
        if (produto == null) {
            return;
        }
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + item.getQuantidade());
        produtoRepository.save(produto);
    }

    public void deletar(Long id) {
        vendaRepository.deleteById(id);
    }
}