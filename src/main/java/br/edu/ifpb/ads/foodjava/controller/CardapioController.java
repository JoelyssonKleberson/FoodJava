package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.enums.Categoria;
import br.edu.ifpb.ads.foodjava.enums.StatusPedido;
import br.edu.ifpb.ads.foodjava.exceptions.ItemVinculadoException;
import br.edu.ifpb.ads.foodjava.exceptions.PrecoInvalidoException;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.model.Pedido;
import br.edu.ifpb.ads.foodjava.repository.CardapioRepository;
import br.edu.ifpb.ads.foodjava.repository.PedidoRepository;

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
}
