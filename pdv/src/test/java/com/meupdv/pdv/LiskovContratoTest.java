package com.meupdv.pdv;

import com.meupdv.pdv.decorator.ArredondamentoDecorator;
import com.meupdv.pdv.decorator.CalculoTotal;
import com.meupdv.pdv.decorator.PagamentoDecorator;
import com.meupdv.pdv.decorator.TotalBase;
import com.meupdv.pdv.lanche.Cardapio;
import com.meupdv.pdv.lanche.Lanche;
import com.meupdv.pdv.lanche.LancheBase;
import com.meupdv.pdv.pagamento.Factory;
import com.meupdv.pdv.pagamento.FormaPagamento;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.meupdv.pdv.lanche.CatalogoLanches;
/**
 * LSP (Liskov): onde o código espera o "pai", qualquer "filha" tem que funcionar
 * sem erro e com o mesmo comportamento combinado.
 */
class LiskovContratoTest {

    private static final BigDecimal CEM = new BigDecimal("100.00");
    private final CatalogoLanches catalogo = new Cardapio();

    @Test
    void todasAsFormasDePagamentoSubstituemFormaPagamento() {
        List<String> tipos = List.of("DINHEIRO", "PIX", "CARTAO_DEBITO", "CARTAO_CREDITO");

        for (String tipo : tipos) {
            FormaPagamento forma = Factory.criar(tipo);

            BigDecimal valorFinal = forma.calcularValorFinal(CEM);

            assertNotNull(valorFinal, tipo + " não pode devolver null");
            assertTrue(valorFinal.signum() > 0, tipo + " tem que devolver valor positivo");
            assertFalse(forma.getDescricao().isBlank(), tipo + " precisa de descrição");
        }
    }

    @Test
    void cadaFormaDePagamentoMantemSuaRegraSemQuebrarOContrato() {
        assertEquals(0, new BigDecimal("95").compareTo(Factory.criar("PIX").calcularValorFinal(CEM)));
        assertEquals(0, new BigDecimal("95").compareTo(Factory.criar("DINHEIRO").calcularValorFinal(CEM)));
        assertEquals(0, new BigDecimal("100").compareTo(Factory.criar("CARTAO_DEBITO").calcularValorFinal(CEM)));
        assertEquals(0, new BigDecimal("103").compareTo(Factory.criar("CARTAO_CREDITO").calcularValorFinal(CEM)));
    }

    @Test
    void todosOsAdicionaisSubstituemLanche() {
        Lanche base = new LancheBase("X-Salada", new BigDecimal("8.00"));

        for (String adicional : catalogo.adicionais().keySet()) {
            Lanche comAdicional = catalogo.montar("X-Salada", List.of(adicional));

            assertNotNull(comAdicional.custo(), adicional + " não pode devolver custo null");
            assertTrue(comAdicional.custo().compareTo(base.custo()) > 0,
                    adicional + " tem que aumentar o custo");
            assertTrue(comAdicional.getDescricao().startsWith(base.getDescricao()),
                    adicional + " tem que manter a descrição do lanche");
            assertTrue(comAdicional.getDescricao().contains(adicional),
                    adicional + " tem que aparecer na descrição");
        }
    }

    @Test
    void variosAdicionaisEmCadeiaContinuamSendoUmLanche() {
        Lanche pedido = catalogo.montar("Monstrão", List.of("Bacon", "Tomate"));

        assertEquals("Monstrão, Bacon, Tomate", pedido.getDescricao());
        assertEquals(0, new BigDecimal("50").compareTo(pedido.custo()));
    }

    @Test
    void todosOsDecoratorsDeCalculoSubstituemCalculoTotal() {
        CalculoTotal base = new TotalBase(CEM);
        List<CalculoTotal> todos = List.of(
                base,
                new PagamentoDecorator(base, Factory.criar("PIX")),
                new ArredondamentoDecorator(base));

        for (CalculoTotal calculo : todos) {
            assertNotNull(calculo.calcular(), calculo.getClass().getSimpleName() + " não pode devolver null");
        }
    }
}