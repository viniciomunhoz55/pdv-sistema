package com.meupdv.pdv.historico;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.meupdv.pdv.entity.ItemVenda;
import com.meupdv.pdv.entity.Venda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class FirestoreHistoricoVendas implements HistoricoVendas {

    private static final Logger LOG = LoggerFactory.getLogger(FirestoreHistoricoVendas.class);
    private static final String COLECAO = "vendas";

    private final Firestore firestore;

    public FirestoreHistoricoVendas(@Value("${firebase.credenciais:}") String credenciais,
                                    @Value("${firebase.arquivo:firebase-key.json}") String arquivo) {
        this.firestore = conectar(credenciais, arquivo);
    }

    @Override
    public void registrar(Venda venda) {
        if (firestore == null) {
            return;
        }
        try {
            firestore.collection(COLECAO)
                    .document(String.valueOf(venda.getIdVenda()))
                    .set(documento(venda))
                    .get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOG.warn("Venda {} não foi salva no Firebase: {}", venda.getIdVenda(), e.getMessage());
        }
    }

    @Override
    public void remover(Long idVenda) {
        if (firestore == null) {
            return;
        }
        try {
            firestore.collection(COLECAO)
                    .document(String.valueOf(idVenda))
                    .delete()
                    .get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOG.warn("Venda {} não foi removida do Firebase: {}", idVenda, e.getMessage());
        }
    }

    // sem chave ou sem internet o sistema continua funcionando só com o MySQL
    private Firestore conectar(String credenciais, String arquivo) {
        try (InputStream chave = abrirChave(credenciais, arquivo)) {
            if (chave == null) {
                LOG.warn("Firebase desligado: chave não encontrada.");
                return null;
            }
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(chave))
                        .build());
            }
            return FirestoreClient.getFirestore();
        } catch (Exception e) {
            LOG.warn("Firebase indisponível: {}", e.getMessage());
            return null;
        }
    }

    // no Railway a chave vem de uma variável de ambiente; no computador, do arquivo
    private InputStream abrirChave(String credenciais, String arquivo) throws IOException {
        if (!credenciais.isBlank()) {
            return new ByteArrayInputStream(credenciais.getBytes(StandardCharsets.UTF_8));
        }
        Path caminho = Path.of(arquivo);
        return Files.exists(caminho) ? Files.newInputStream(caminho) : null;
    }

    private Map<String, Object> documento(Venda venda) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("idVenda", venda.getIdVenda());
        dados.put("dataHora", venda.getDataHora().toString());
        dados.put("formaPagamento", venda.getFormaPagamento());
        dados.put("total", venda.getTotal().doubleValue());
        dados.put("cliente", venda.getCliente() == null ? null : venda.getCliente().getNome());
        dados.put("itens", itens(venda));
        return dados;
    }

    private List<Map<String, Object>> itens(Venda venda) {
        List<Map<String, Object>> lista = new ArrayList<>();
        for (ItemVenda item : venda.getItens()) {
            Map<String, Object> dados = new HashMap<>();
            dados.put("descricao", item.getDescricao() != null ? item.getDescricao() : item.getProduto().getNome());
            dados.put("quantidade", item.getQuantidade());
            dados.put("precoUnitario", item.getPrecoUnitario().doubleValue());
            dados.put("subtotal", item.getSubtotal().doubleValue());
            lista.add(dados);
        }
        return lista;
    }
}