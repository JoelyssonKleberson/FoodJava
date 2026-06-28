package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.enums.Categoria;
import br.edu.ifpb.ads.foodjava.enums.StatusPedido;
import br.edu.ifpb.ads.foodjava.exceptions.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.exceptions.ItemVinculadoException;
import br.edu.ifpb.ads.foodjava.exceptions.PrecoInvalidoException;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.model.Pedido;
import br.edu.ifpb.ads.foodjava.repository.CardapioRepository;
import br.edu.ifpb.ads.foodjava.repository.PedidoRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

public class CardapioController {

    private final CardapioRepository cardapioRepository;
    private final PedidoRepository pedidoRepository;

    public CardapioController(CardapioRepository cardapioRepository, PedidoRepository pedidoRepository) {
        this.cardapioRepository = cardapioRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public void adicionarItem(String nome, String descricao, Double preco, Categoria categoria, boolean disponivel, String imagemPath) {
        if (preco == null || preco <= 0) {
            throw new PrecoInvalidoException("O preço do item é inválido!");
        }

        ItemCardapio novoItem = new ItemCardapio(nome, descricao, preco, categoria, disponivel, imagemPath);
        cardapioRepository.salvarItem(novoItem);
    }

    public void atualizarItem(ItemCardapio itemAtualizado) {
        if (itemAtualizado.getPreco() <= 0) {
            throw new PrecoInvalidoException("O preço do item é inválido!");
        }
        cardapioRepository.atualizarItem(itemAtualizado);
    }

    public void removerItem(ItemCardapio item) {
        List<Pedido> pedidosAbertos = pedidoRepository.buscarTodos().stream().
                filter(pedido -> pedido.getStatus() != StatusPedido.ENTREGUE &&
                pedido.getStatus() != StatusPedido.CANCELADO).toList();

        for (Pedido pedido : pedidosAbertos) {
            if (pedido.getItens().containsKey(item)) {
                throw new ItemVinculadoException("O item '" + item.getNome() + "' não pode ser excluído pois está vinculado a um pedido em aberto");
            }
        }
        cardapioRepository.removerItem(item.getNome());
    }

    public List<ItemCardapio> listarCardapioCompleto() {
        return cardapioRepository.buscarTodos();
    }

    public List<ItemCardapio> listarPorCategoria(Categoria categoria) {
        return cardapioRepository.buscarPorCategoria(categoria);
    }

    public void importarCardapio(String caminhoArquivo) {
        try (Reader reader = new FileReader(caminhoArquivo)) {
            Gson gson = new Gson();

            JsonObject jsonRoot = JsonParser.parseReader(reader).getAsJsonObject();
            JsonElement arrayCardapio = jsonRoot.get("cardapio");

            Type tipoLista = new TypeToken<List<ItemCardapio>>(){}.getType();
            List<ItemCardapio> itensImportados = gson.fromJson(arrayCardapio, tipoLista);

            if (itensImportados != null) {
                for (ItemCardapio item : itensImportados) {
                    adicionarItem(item.getNome(), item.getDescricao(), item.getPreco(), item.getCategoria(), item.isDisponivel(), item.getImagemPath());
                }
            }
        } catch (Exception e) {
            throw new ArquivoImportacaoException("Erro ao importar o arquivo de cardápio: " + e.getMessage());
        }
    }
}
