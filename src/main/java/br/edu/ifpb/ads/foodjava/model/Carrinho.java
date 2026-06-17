package br.edu.ifpb.ads.foodjava.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Carrinho {

    private final Map<ItemCardapio, Integer> itens;

    public Carrinho() {
        this.itens = new LinkedHashMap<>();
    }

    public Map<ItemCardapio, Integer> getItens() {
        return Collections.unmodifiableMap(itens);
    }

    public boolean estaVazio() {
        return itens.isEmpty();
    }

    public void adicionarItem(ItemCardapio item, int quantidade) {
        if (item == null || quantidade <= 0) {
            return;
        }
        itens.put(item, itens.getOrDefault(item, 0) + quantidade);
    }

    public void removerItem(ItemCardapio item) {
        if (item != null) {
            itens.remove(item);
        }
    }

    public void alterarQuantidade(ItemCardapio item, int novaQuantidade) {
        if (item == null) {
            return;
        }
        if (novaQuantidade <= 0) {
            removerItem(item);
        } else {
            itens.put(item, novaQuantidade);
        }
    }

    public void limparCarrinho() {
        itens.clear();
    }

    public double calcularTotal() {
        double total = 0.0;
        for (Map.Entry<ItemCardapio, Integer> entrada : itens.entrySet()) {
            total += entrada.getKey().getPreco() * entrada.getValue();
        }
        return total;
    }

}
