package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.enums.Categoria;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;

import java.util.List;

public interface CardapioRepository {

    void salvarItem(ItemCardapio item);
    void atualizarItem(ItemCardapio item);
    void removerItem(String nomeItem);
    List<ItemCardapio> buscarTodos();
    List<ItemCardapio> buscarPorCategoria(Categoria categoria);
}
