
package com.meupdv.pdv.pagamento;
import java.math.BigDecimal;

public interface FormaPagamento {

    BigDecimal calcularValorFinal(BigDecimal valorOriginal);
    String getDescricao();
}
    

