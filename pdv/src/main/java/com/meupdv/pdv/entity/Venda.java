package com.meupdv.pdv.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venda")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVenda;

    private LocalDateTime dataHora;

    private BigDecimal total;

    private String formaPagamento;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private List<ItemVenda> itens = new ArrayList<>();

    public Venda() {}

    public Long getIdVenda() { return idVenda; }
    public void setIdVenda(Long idVenda) { this.idVenda = idVenda; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public List<ItemVenda> getItens() { return itens; }
    public void setItens(List<ItemVenda> itens) { this.itens = itens; }
    
    public void adicionarItem(ItemVenda item) {
    item.setVenda(this);
    itens.add(item);
}
    //  Padrão de projeto BUILDER
    public static class VendaBuilder {
        private final Venda venda;

        public VendaBuilder() {
            this.venda = new Venda();
            this.venda.setDataHora(LocalDateTime.now());
            this.venda.setTotal(BigDecimal.ZERO);
        }

        public VendaBuilder comCliente(Cliente cliente) {
            venda.setCliente(cliente);
            return this;
        }

        public VendaBuilder comFormaPagamento(String formaPagamento) {
            venda.setFormaPagamento(formaPagamento);
            return this;
        }

        public VendaBuilder adicionarItem(ItemVenda item) {
            venda.adicionarItem(item);
            if (item.getSubtotal() != null) {
                venda.setTotal(venda.getTotal().add(item.getSubtotal()));
            }
            return this;
        }

        public Venda build() {
            return venda;
        }
    }

    public static VendaBuilder builder() {
        return new VendaBuilder();
    }
}