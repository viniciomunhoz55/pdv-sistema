package com.meupdv.pdv.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cupom")
public class Cupom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCupom;

    private String numeroCupom;

    private LocalDateTime dataEmissao;

    private BigDecimal valorTotal;

    @OneToOne
    @JoinColumn(name = "id_venda")
    private Venda venda;

    public Cupom() {}

    public Long getIdCupom() { return idCupom; }
    public void setIdCupom(Long idCupom) { this.idCupom = idCupom; }

    public String getNumeroCupom() { return numeroCupom; }
    public void setNumeroCupom(String numeroCupom) { this.numeroCupom = numeroCupom; }

    public LocalDateTime getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(LocalDateTime dataEmissao) { this.dataEmissao = dataEmissao; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public Venda getVenda() { return venda; }
    public void setVenda(Venda venda) { this.venda = venda; }
}